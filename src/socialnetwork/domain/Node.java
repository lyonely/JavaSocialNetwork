package socialnetwork.domain;

public class Node<E> {

  private final E item;
  private final int key;
  private Node<E> next;

  public Node(E item) {
    this(item, null);
  }

  public Node(E item, Node<E> next) {
    this(item, item.hashCode(), next);
  }

  protected Node(E item, int key) {
    this.item = item;
    this.key = key;
    this.next = null;
  }

  protected Node(E item, int key, Node<E> next) {
    this.item = item;
    this.key = key;
    this.next = next;
  }

  public E item() {
    return item;
  }

  public int key() {
    return key;
  }

  public Node<E> next() {
    return next;
  }

  public void setNext(Node<E> next) {
    this.next = next;
  }
}


