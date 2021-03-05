package socialnetwork.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class QueueBacklog implements Backlog{

  private final AtomicInteger size;
  private volatile AtomicReference<Node<Task>> head;
  private volatile AtomicReference<Node<Task>> tail;


  public QueueBacklog() {
    Node<Task> dummyNode = new Node<>(null);
    head = new AtomicReference<>(dummyNode);
    tail = new AtomicReference<>(dummyNode);
    size = new AtomicInteger(0);
  }

  @Override
  public boolean add(Task task) {
    Node<Task> newNode = new Node(Objects.requireNonNull(task));
    Node<Task> currentTail = null;

    while (true) {
      currentTail = tail.get();
      Node<Task> tailSucc = tail.get().next.get();

      // Checking that nothing changed
      if (currentTail == tail.get()) {
        if (tailSucc == null) {

          //Trying to set append the new node
          if (currentTail.next.compareAndSet(null, newNode)) {
            break;
          }
        } else {
          // The something has been added after we read currentTail and it should become the new tail
          tail.compareAndSet(currentTail, tailSucc);
        }
      }
    }
    // Try to update the pointer to tail; if it fails, no worries, we will detect the inconsistency
    // also during pop
    tail.compareAndSet(currentTail, newNode);
    size.incrementAndGet();
    return true;
  }


  @Override
  public Optional<Task> getNextTaskToProcess() {
    Task result = null;
    while (true) {
      Node<Task> currentHead = head.get();
      Node<Task> currentTail = tail.get();

      Node<Task> headSucc = currentHead.next.get();

      //Checking that nothing changed
      if (currentHead == head.get()) {
        if (currentHead == currentTail) {

          // If headSucc is the dummy node, its successor is null and the queue is empty
          if (headSucc == null) {
            return Optional.empty();
          }
          tail.compareAndSet(currentTail,
              headSucc); //A node has been added as successor of head, but tail has not been updated
        } else {
          result = headSucc.value;
          if (head.compareAndSet(currentHead, headSucc)) { // Try to update head
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

  public int traversalSize() {
    int nodes = 0;
    for (Node<Task> current = head.get(); current != tail.get(); current = current.next.get()) {
      nodes++;
    }
    return nodes;
  }


  private static class Node<E> {

    private final E value;
    private volatile AtomicReference<Node<E>> next;

    public Node(E value) {
      this.value = value;
      this.next = new AtomicReference<>(null);
    }

  }
}

