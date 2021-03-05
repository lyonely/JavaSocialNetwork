package socialnetwork.domain;

import java.util.Optional;

public class NetworkBacklog implements Backlog{

  int size = 0;
  private final Node<Task> head;
  private final Node<Task> tail;

  @Override
  public boolean add(Task item) {
    Node<Task> node = new Node<>(item, item.getId());
    Position<Task> where = find(head, node.key());
    if (where.curr.key() == node.key()) {
      return false;
    } else {
      node.setNext(where.curr);
      where.pred.setNext(node);
      size += 1;
      return true;
    }
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    if (head.next().key() == tail.key()) {
      return Optional.empty();
    } else {
      Node<Task> smallest = head.next();
      head.setNext(smallest.next());
      size -= 1;
      return Optional.of(smallest.item());
    }
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return size;
  }

  public NetworkBacklog() {
    head = new Node<>(null, Integer.MIN_VALUE, null);
    tail = new Node<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<Task> find(Node<Task> start, int key) {
    Node<Task> prev, curr;
    curr = start;
    do {
      prev = curr;
      curr = curr.next();
    } while (curr.key() < key);  // until curr.key >= key
    return new Position<>(prev, curr);
  }

}
