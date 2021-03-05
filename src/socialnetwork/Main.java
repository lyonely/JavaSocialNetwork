package socialnetwork;

import socialnetwork.domain.Backlog;
import socialnetwork.domain.NetworkBacklog;

public class Main {

  public static void main(String[] args) {
    Backlog backlog = new NetworkBacklog();
    SocialNetwork socialNetwork = new SocialNetwork(backlog);


  }
}
