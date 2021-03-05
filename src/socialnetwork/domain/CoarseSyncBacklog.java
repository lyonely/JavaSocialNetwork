package socialnetwork.domain;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseSyncBacklog extends NetworkBacklog{

  private final Lock lock = new ReentrantLock();

  @Override
  public boolean add(Task item) {
    lock.lock();
    try {
      return super.add(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    lock.lock();
    try {
      return super.getNextTaskToProcess();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    lock.lock();
    try {
      return super.numberOfTasksInTheBacklog();
    } finally {
      lock.unlock();
    }
  }

}
