

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        manager.addTask(new Task("Задача 1", "Повторить спринт 4", Status.DONE));
        manager.addTask(new Task("Задача 2", "Начать спринт 5", Status.NEW));


        Epic epic1 = new Epic("Эпик 1", "Закончить спринт 4");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Эпик 1 Подзадача 1", "Пройти теорию", Status.DONE, epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик 1 Подзадача 2", "Выполнить задание спринта", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);


        Epic epic2 = new Epic("Эпик 2", "Отдых");
        manager.addEpic(epic2);
        Subtask subtask21 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic2);
        manager.addSubtask(subtask21);

        // Вывод
        System.out.println("\n Вывод Эпиков : \n" + manager.getEpics());
        System.out.println("\n Вывод Задач : \n" +  manager.getTasks());
        System.out.println("\n Вывод Подзадач : \n" + manager.getSubtasks());

        // Удаляем одну задачу

        manager.deleteTask(1);
        System.out.println("\n Осталось Задач : \n" +  manager.getTasks());

        // Удаляем Эпик

        manager.deleteEpic(6);
        System.out.println("\n Осталось Эпиков : \n" +  manager.getEpics());

        // Проверка
        System.out.println("\n Итого Эпиков : \n" + manager.getEpics());
        System.out.println("\n Итого Задач : \n" +  manager.getTasks());
        System.out.println("\n Итого Подзадач : \n" + manager.getSubtasks());

    }
}
