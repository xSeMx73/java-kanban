
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

        Subtask subtask11 = new Subtask("Эпик 1 Подзадача 1", "Пройти теорию", Status.DONE, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Эпик 1 Подзадача 2", "Выполнить задание спринта", Status.IN_PROGRESS, epic1.getId());
        manager.addSubtask(subtask12);


        Epic epic2 = new Epic("Эпик 2", "Отдых");
        manager.addEpic(epic2);
        Subtask subtask21 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic2.getId());
        manager.addSubtask(subtask21);


        task.Subtask subtask22 = new task.Subtask("Эпик 2 Подзадача 1", "Поспать еще", task.Status.IN_PROGRESS, epic2.getId());
   //     manager.updateSubtask(subtask22);
        System.out.println(manager.getSubtask(7));
        System.out.println(manager.getEpicSubtasks(epic2.getId()));
       System.out.println(manager.getSubtasks());
      System.out.println(manager.getEpicSubtasks(3));
      System.out.println("ид эпика 1: " + epic1.getId());
      System.out.println("ид эпика 2: " + epic2.getId());
     System.out.println("ид подзадачи 21 : " + subtask21.getId());



    }


}
