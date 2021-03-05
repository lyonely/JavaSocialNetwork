package socialnetwork.domain;

class LockablePosition<T> {

  public final LockableNode<T> pred;
  public final LockableNode<T> curr;

  public LockablePosition(LockableNode<T> pred, LockableNode<T> curr) {
    this.pred = pred;
    this.curr = curr;
  }
}

