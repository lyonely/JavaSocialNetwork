package socialnetwork.domain;

class Position<T> {

  public final Node<T> pred, curr;

  public Position(Node<T> pred, Node<T> curr) {
    this.pred = pred;
    this.curr = curr;
  }
}