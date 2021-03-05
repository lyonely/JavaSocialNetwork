package socialnetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import socialnetwork.domain.Message;

public class User extends Thread {

  private static final AtomicInteger nextId = new AtomicInteger(0);

  protected final SocialNetwork socialNetwork;
  private final int id;
  private final String name;

  public User(String username, SocialNetwork socialNetwork) {
    this.name = username;
    this.id = User.nextId.getAndIncrement();
    this.socialNetwork = socialNetwork;
  }

  public int getUserId() {
    return id;
  }

  private Set<User> getUsers() {
    return socialNetwork.getAllUsers();
  }

  private void sendMessage() {
    Set<User> allUsers = getUsers();
    Random random = new Random();
    List<User> receivers = new ArrayList<>();
    for (User user : allUsers) {
      if (random.nextBoolean()) {
        receivers.add(user);
      }
    }
    socialNetwork.postMessage(this, receivers, "Hello");
  }

  private void deleteRandomMessages() {
    List<Message> snapshot = socialNetwork.getBoards().get(this).getBoardSnapshot();
    if (!snapshot.isEmpty()) {
      Random random = new Random();
      for (Message message : snapshot) {
        if (random.nextBoolean()) {
          socialNetwork.deleteMessage(message);
        }
      }
    }
  }

  @Override
  public void run() {
    Random random = new Random();
    do {
      do {
        Set<User> users = getUsers();
      } while (random.nextBoolean());
      do {
        sendMessage();
      } while (random.nextBoolean());
      do {
        deleteRandomMessages();
      } while (random.nextBoolean());
    } while (random.nextBoolean());
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public int hashCode() {
    return id;
  }
}
