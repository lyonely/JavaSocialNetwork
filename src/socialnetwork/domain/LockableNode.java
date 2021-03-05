package socialnetwork.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableNode<E> {

  private Lock lock = new ReentrantLock();

  private E item;
  private int key;
  private LockableNode<E> next;

  public LockableNode(E item) {
    this(item, null);
  }

  public LockableNode(E item, LockableNode<E> next) {
    this(item, item.hashCode(), next);
  }

  protected LockableNode(E item, int key) {
    this.item = item;
    this.key = key;
    this.next = null;
  }
  protected LockableNode(E item, int key, LockableNode<E> next) {
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

  public LockableNode<E> next() {
    return next;
  }

  public void setItem(E item) {
    this.item = item;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setNext(LockableNode<E> next) {
    this.next = next;
  }

  public void lock() {
    lock.lock();
  }

  public void unlock() {
    lock.unlock();
  }
}
