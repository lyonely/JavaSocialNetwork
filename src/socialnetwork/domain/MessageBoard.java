package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;

public class MessageBoard implements Board {

  int size = 0;
  private Node<Message> head, tail;

  public MessageBoard() {
    head = new Node<>(null, Integer.MIN_VALUE, null);
    tail = new Node<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<Message> find(Node<Message> start, int key) {
    Node<Message> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);  // until curr.key >= key
    return new Position<Message>(pred, curr);
  }

  public boolean contains(Message item) {
    Node<Message> node = new Node<>(item);
    Position<Message> expectedPosition = find(head, node.key());

    return expectedPosition.curr.key() == node.key();
  }

  public boolean addMessage(Message item) {
    Node<Message> node = new Node<Message>(item, item.getMessageId());
    Position<Message> where = find(head, node.key());
    if (where.curr.key() == node.key()) {
      return false;
    } else {
      node.setNext(where.curr);
      where.pred.setNext(node);
      size += 1;
      return true;
    }
  }

  public boolean deleteMessage(Message item) {
    Node<Message> node = new Node<>(item);
    Position<Message> where = find(head, node.key());
    if (where.curr.key() > node.key()) {
      return false;
    } else {
      where.pred.setNext(where.curr.next());
      size -= 1;
      return true;
    }
  }

  public int size() {
    return size;
  }

  public List<Message> getBoardSnapshot() {
    List<Message> snapshot = new ArrayList<>();
    Node<Message> curr = head.next();
    while (curr.key() != tail.key()) {
      snapshot.add(0, curr.item());
      curr = curr.next();
    }
    return snapshot;
  }
}


