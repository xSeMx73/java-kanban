package test;

import manager.InMemoryTaskManager;
import task.*;
import task.Status;
import task.Task;


class Test {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @org.junit.jupiter.api.Test
    public void equalsTaskForID() {
        Task task1 = new Task("1", "2", Status.NEW);
        manager.addTask(task1);
        Task task2 = manager.getTask(task1.getId());

        if (task1.equals(task2)) {
            System.out.println("Tasks are equal");
        } else {
            System.out.println("Tasks are not equal");
        }
    }
    @org.junit.jupiter.api.Test
    public void equalsEpicForID() {
        Epic epic1 = new Epic("3", "4");
        manager.addEpic(epic1);
        Epic epic2 = manager.getEpic((epic1.getId()));

        if (epic1.equals(epic2)) {
            System.out.println("Epics are equal");
        } else {
            System.out.println("Epics are not equal");
        }
    }

        @org.junit.jupiter.api.Test
        public void equalsSubTaskForID () {
            Epic epic1 = new Epic("3", "4");
            manager.addEpic(epic1);
            Subtask subtask1 = new Subtask("3", "4", Status.NEW, epic1.getId());
            manager.addSubtask(subtask1);
            Subtask subtask2 = manager.getSubtask((subtask1.getId()));

            if (subtask1.equals(subtask2)) {
                System.out.println("Subtasks are equal");
            } else {
                System.out.println("Subtasks are not equal");
            }
        }
    }


