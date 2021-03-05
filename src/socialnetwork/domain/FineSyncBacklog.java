package socialnetwork.domain;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class FineSyncBacklog implements Backlog{

  AtomicInteger size = new AtomicInteger(0);
  private final LockableNode<Task> head;
  private final LockableNode<Task> tail;

  public FineSyncBacklog() {
    head = new LockableNode<>(null, Integer.MIN_VALUE, null);
    tail = new LockableNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  @Override
  public boolean add(Task item) {
    LockableNode<Task> node = new LockableNode<>(item, item.getId());
    LockableNode<Task> prev = null, curr = null;
    try {
      LockablePosition<Task> where = find(head, node.key());
      prev = where.pred;
      curr = where.curr;
      if (where.curr.key() == node.key()) {
        return false;
      } else {
        node.setNext(where.curr);
        where.pred.setNext(node);
        size.incrementAndGet();
        return true;
      }
    } finally {
      prev.unlock();
      curr.unlock();
    }
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    if (head.next().key() == tail.key()) {
      return Optional.empty();
    } else {
      LockableNode<Task> curr = null;
      try {
        head.lock();
        curr = head.next();
        head.next().lock();
        head.setNext(head.next().next());
        size.decrementAndGet();
        return Optional.of(head.next().item());
      } finally {
        head.unlock();
        curr.unlock();
      }
    }
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return size.get();
  }

  private LockablePosition<Task> find(LockableNode<Task> start, int key) {
    LockableNode<Task> prev, curr;
    prev = start;
    prev.lock();
    curr = start.next();
    curr.lock();
    while (curr.key() < key) {
      prev.unlock();
      prev = curr;
      curr = curr.next();
      curr.lock();
    }
    return new LockablePosition<>(prev, curr);
  }

}
