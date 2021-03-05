package socialnetwork.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class QueueBacklog implements Backlog {

  private final AtomicInteger size;
  private final AtomicReference<Node<Task>> head;
  private final AtomicReference<Node<Task>> tail;


  public QueueBacklog() {
    Node<Task> dummyNode = new Node<>(null);
    head = new AtomicReference<>(dummyNode);
    tail = new AtomicReference<>(dummyNode);
    size = new AtomicInteger(0);
  }

  @Override
  public boolean add(Task task) {
    Node<Task> newNode = new Node<>(Objects.requireNonNull(task));
    Node<Task> currentTail;

    while (true) {
      currentTail = tail.get();
      Node<Task> tailSucc = tail.get().next.get();

      if (currentTail == tail.get()) {
        if (tailSucc == null) {
          if (currentTail.next.compareAndSet(null, newNode)) {
            break;
          }
        } else {
          tail.compareAndSet(currentTail, tailSucc);
        }
      }
    }
    tail.compareAndSet(currentTail, newNode);
    size.incrementAndGet();
    return true;
  }


  @Override
  public Optional<Task> getNextTaskToProcess() {
    Task result;
    while (true) {
      Node<Task> currentHead = head.get();
      Node<Task> currentTail = tail.get();

      Node<Task> headSucc = currentHead.next.get();

      if (currentHead == head.get()) {
        if (currentHead == currentTail) {

          if (headSucc == null) {
            return Optional.empty();
          }
          tail.compareAndSet(currentTail,
              headSucc);
        } else {
          result = headSucc.value;
          if (head.compareAndSet(currentHead, headSucc)) {
            break;
          }
        }
      }
    }
    size.decrementAndGet();
    return Optional.of(result);
  }


  @Override
  public int numberOfTasksInTheBacklog() {
    return size.get();
  }

  private static class Node<E> {

    private final E value;
    private final AtomicReference<Node<E>> next;

    public Node(E value) {
      this.value = value;
      this.next = new AtomicReference<>(null);
    }

  }
}

