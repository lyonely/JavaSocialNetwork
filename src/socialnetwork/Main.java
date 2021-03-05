package socialnetwork;

import socialnetwork.domain.*;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {
    Backlog backlog = new NetworkBacklog();

    SocialNetwork socialNetwork = new SocialNetwork(backlog);

    Worker[] workers = new Worker[3];
    Arrays.setAll(workers, i -> new Worker(backlog));
    Arrays.stream(workers).forEach(Thread::start);

    User[] userThreads = new User[5];
    Arrays.setAll(userThreads, i -> new User("user" + i, socialNetwork));
    Arrays.stream(userThreads)
        .forEach(
            u -> {
              socialNetwork.register(u, new MessageBoard());
              u.start();
            });

    Arrays.stream(userThreads)
        .forEach(
            u -> {
              try {
                u.join();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    while (backlog.numberOfTasksInTheBacklog() != 0) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    Arrays.stream(workers).forEach(Worker::interrupt);

    Arrays.stream(workers)
        .forEach(
            w -> {
              try {
                w.join();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

  }
}
