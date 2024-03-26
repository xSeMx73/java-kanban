package manager;

import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager{

    static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Node prev, Task task, Node next ) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;

    private HashMap<Integer, Node> idMap = new HashMap<>();


    @Override
    public void add(Task task) {
        if (task != null) {
            lastLink(task);
        }

    }

    private void lastLink(Task task) {
        remove(task.getId());
        final Node newNode = new Node(null, task, null);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        idMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        final ArrayList<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public void remove(int id) {
        final Node oldNode = idMap.remove(id);
        if (oldNode != null) {
            if (oldNode == head) {
                head = oldNode.next;
                if (head != null) {
                    head.prev = null;
                }
                if (tail == oldNode) {
                    tail = null;
                }
            } else if (oldNode == tail) {
                tail = oldNode.prev;
                if (tail != null) {
                    tail.next = null;
                }
            } else {
                oldNode.prev.next = oldNode.next;
                if (oldNode.next != null) {
                    oldNode.next.prev = oldNode.prev;
                }
            }
        }
    }
}

