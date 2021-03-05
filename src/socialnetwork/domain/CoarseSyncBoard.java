package socialnetwork.domain;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseSyncBoard extends MessageBoard{

  private final Lock lock = new ReentrantLock();

  @Override
  public boolean addMessage(Message item) {
    lock.lock();
    try {
      return super.addMessage(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean deleteMessage(Message item) {
    lock.lock();
    try {
      return super.deleteMessage(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    lock.lock();
    try {
      return super.size();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<Message> getBoardSnapshot() {
    lock.lock();
    try {
      return super.getBoardSnapshot();
    } finally {
      lock.unlock();
    }
  }
}
