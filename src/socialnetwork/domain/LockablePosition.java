package socialnetwork.domain;

class LockablePosition<T> {

  public final LockableNode<T> pred, curr;

  public LockablePosition(LockableNode<T> pred, LockableNode<T> curr) {
    this.pred = pred;
    this.curr = curr;
  }
}

