package entities.manager;

import entities.Node;
import entities.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Node<Task>> history = new LinkedList<>();
    private final Map<Integer, Node<Task>> taskMap = new HashMap<>();

    private Node<Task> headNode;
    private Node<Task> tailNode;

    @Override
    public void addTask(Task task) {
        if (taskMap.containsKey(task.getId()))
            remove(task.getId());
        linkLast(new Task(task));
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeForDelete = taskMap.get(id);
        removeNode(nodeForDelete);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();

        if (headNode != null) {
            taskHistory.add(headNode.getData());
            Node<Task> node = headNode;
            while (node.getNextNode() != null && node != node.getNextNode()) {
                node = node.getNextNode();
                taskHistory.add(node.getData());
            }
        }
        return List.copyOf(taskHistory);
    }

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);

        if (headNode == null) {
            headNode = newNode;
            tailNode = newNode;
            headNode.setNextNode(tailNode);
            tailNode.setPrevNode(headNode);
            newNode.setNextNode(newNode);
            newNode.setPrevNode(newNode);
        } else {
            Node<Task> oldTail = tailNode;
            tailNode = newNode;
            oldTail.setNextNode(newNode);
            tailNode.setPrevNode(oldTail);
            newNode.setPrevNode(oldTail);
            headNode.setPrevNode(null);
            tailNode.setNextNode(null);
        }

        history.add(newNode);
        taskMap.put(task.getId(), newNode);
    }

    private void removeNode(Node<Task> nodeForDelete) {
        Node<Task> prevNode = nodeForDelete.getPrevNode();
        if (prevNode == null) {
            headNode = nodeForDelete.getNextNode();
        } else {
            prevNode.setNextNode(nodeForDelete.getNextNode());
        }

        Node<Task> nextNode = nodeForDelete.getNextNode();
        if (nextNode == null) {
            tailNode = nodeForDelete.getPrevNode();
        } else {
            nextNode.setPrevNode(nodeForDelete.getPrevNode());
        }

        nodeForDelete.setPrevNode(null);
        nodeForDelete.setNextNode(null);
    }
}
