package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FineSyncBoard implements Board {

  private final LockableNode<Message> head;
  private final LockableNode<Message> tail;
  AtomicInteger size = new AtomicInteger(0);

  public FineSyncBoard() {
    head = new LockableNode<>(null, Integer.MIN_VALUE, null);
    tail = new LockableNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private LockablePosition<Message> find(LockableNode<Message> start, int key) {
    LockableNode<Message> prev;
    LockableNode<Message> curr;
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

  public boolean addMessage(Message item) {
    LockableNode<Message> node = new LockableNode<>(item, item.getMessageId());
    LockableNode<Message> prev = null;
    LockableNode<Message> curr = null;
    try {
      LockablePosition<Message> where = find(head, node.key());
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

  public boolean deleteMessage(Message item) {
    LockableNode<Message> node = new LockableNode<>(item);
    LockableNode<Message> prev = null;
    LockableNode<Message> curr = null;
    try {
      LockablePosition<Message> where = find(head, node.key());
      prev = where.pred;
      curr = where.curr;
      if (where.curr.key() > node.key()) {
        return false;
      } else {
        where.pred.setNext(where.curr.next());
        size.decrementAndGet();
        return true;
      }
    } finally {
      prev.unlock();
      curr.unlock();
    }
  }

  public int size() {
    return size.get();
  }

  public List<Message> getBoardSnapshot() {
    List<Message> snapshot = new ArrayList<>();
    LockableNode<Message> prev;
    LockableNode<Message> curr;
    prev = head;
    prev.lock();
    curr = head.next();
    curr.lock();
    while (curr.key() != tail.key()) {
      snapshot.add(0, curr.item());
      prev.unlock();
      prev = curr;
      curr = curr.next();
      curr.lock();
    }
    prev.unlock();
    curr.unlock();
    return snapshot;
  }
}
