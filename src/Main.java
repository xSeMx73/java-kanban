
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import manager.*;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addTask(new Task("Задача 1", "Повторить спринт 4", Status.DONE));
        manager.addTask(new Task("Задача 2", "Начать спринт 5", Status.NEW));


        Epic epic1 = new Epic("Эпик 1", "Закончить спринт 4");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Отдых");
        manager.addEpic(epic2);

        Subtask subtask11 = new Subtask("Эпик 1 Подзадача 1", "Пройти теорию", Status.DONE, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Эпик 1 Подзадача 2", "Выполнить задание спринта", Status.IN_PROGRESS, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic1.getId());
        manager.addSubtask(subtask13);
        Epic epic3 = new Epic("Эпик 3", "Эпик3");

        System.out.println(manager.getEpics());
        manager.updateEpic(epic3);
        System.out.println(manager.getEpics());


    }
}
