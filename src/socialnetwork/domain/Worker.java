package socialnetwork.domain;

import java.util.Optional;

public class Worker extends Thread {

  private final Backlog backlog;
  private boolean interrupted = false;

  public Worker(Backlog backlog) {
    this.backlog = backlog;
  }

  @Override
  public void run() {
    while (!interrupted) {
      Optional<Task> nextTask = backlog.getNextTaskToProcess();
      if (nextTask.isPresent()) {
        process(nextTask.get());
      } else {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void interrupt() {
    this.interrupted = true;
  }

  public void process(Task nextTask) {
    if (nextTask.getCommand() == Task.Command.POST) {
      nextTask.getBoard().addMessage(nextTask.getMessage());
    } else if (nextTask.getCommand() == Task.Command.DELETE) {
      if (!nextTask.getBoard().deleteMessage(nextTask.getMessage())) {
        backlog.add(nextTask);
      }
    }
  }
}
