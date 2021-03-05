package socialnetwork.domain;

import java.util.Optional;

public class NetworkBacklog implements Backlog{

  int size = 0;
  private Node<Task> head, tail;

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
    Node<Task> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);  // until curr.key >= key
    return new Position<Task>(pred, curr);
  }

  public boolean contains(Task item) {
    Node<Task> node = new Node<>(item);
    Position<Task> expectedPosition = find(head, node.key());

    return expectedPosition.curr.key() == node.key();
  }

  public boolean remove(Task item) {
    Node<Task> node = new Node<>(item);
    Position<Task> where = find(head, node.key());
    if (where.curr.key() > node.key()) {
      return false;
    } else {
      where.pred.setNext(where.curr.next());
      size -= 1;
      return true;
    }
  }

  public int size() {
    return size;
  }

}
