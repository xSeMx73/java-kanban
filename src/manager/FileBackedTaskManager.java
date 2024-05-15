package manager;
import java.io.FileWriter;
import Exceptions.ManagerSaveException;
import task.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class FileBackedTaskManager extends InMemoryTaskManager {

    private File dataBase;
    public FileBackedTaskManager(File dataBase) {
       this.dataBase = dataBase;
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(dataBase)) {
            writer.write("id,type,name,status,description,epic,startTime,duration,endTime" + "\n");
            for (Task task : getTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic task : getEpics()) {
                writer.write(task.toString() + "\n");
            }
            for (Subtask task : getSubtasks()) {
                writer.write(task.toString() + "\n");
            }
            writer.write("\n");
            for (Task history : getHistory()) {
                writer.write(history.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

private void loadHistory (String history) {
    String[] splitHistory = history.split(",");
    for (String s : splitHistory) {
        int idH = Integer.parseInt(s);
        if (tasks.containsKey(idH)) {
            historyManager.add(getTask((idH)));
        } else if (epics.containsKey(idH)) {
            historyManager.add(getEpic((idH)));
        } else if (subtasks.containsKey(idH)) {
            historyManager.add(getSubtask((idH)));
        }
    }
}
    public static FileBackedTaskManager loadFromFile (File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int id = 0;
        int maxID = 0;
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.readLine();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            if (!line.isEmpty()) {
                String[] split = line.split(",");
                switch (split[1]) {
                    case "TASK":
                        id = Integer.parseInt(split[0]);
                        Task task = new Task(split[2], split[4], Status.valueOf(split[3]));
                        task.setId(id);
                        try {
                           if (!(split[6] == null)) {
                                task.setStartTime(LocalDateTime.parse(split[6]));
                                task.setDuration(Duration.parse(split[7]));
                                fileBackedTaskManager.prioritizedTasks.add(task);
                           }
                     } catch (RuntimeException ignored){

                        }
                        fileBackedTaskManager.tasks.put(id, task);
                        break;
                    case "EPIC":
                        id = Integer.parseInt(split[0]);
                        Epic epic = new Epic(split[2], split[4]);
                        epic.setId(id);
                        try {
                            if (!(split[6] == null)) {
                                epic.setStartTime(LocalDateTime.parse(split[6]));
                                epic.setDuration(Duration.parse(split[7]));
                                epic.setEndTime(LocalDateTime.parse(split[8]));
                            }
                        } catch (RuntimeException ignored){

                        }
                        fileBackedTaskManager.epics.put(id, epic);
                        break;
                    case "SUBTASK":
                        id = Integer.parseInt(split[0]);
                        Subtask subtask = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
                        subtask.setId(id);
                        try {
                            if (!(split[6] == null)) {
                                subtask.setStartTime(LocalDateTime.parse(split[6]));
                                subtask.setDuration(Duration.parse(split[7]));
                                fileBackedTaskManager.prioritizedTasks.add(subtask);
                            }
                        } catch (RuntimeException ignored){

                        }
                        fileBackedTaskManager.subtasks.put(id, subtask);
                        break;
                }
            } else {
                String history = bufferedReader.readLine();
                fileBackedTaskManager.loadHistory(history);
                    }
            if (id > maxID){
                maxID = id;
            }
        }
        bufferedReader.close();
        for (Subtask s : fileBackedTaskManager.subtasks.values()){
            int epicID = s.getEpicID();
            if(fileBackedTaskManager.epics.containsKey(epicID)){
                fileBackedTaskManager.epics.get(epicID).addSubIdToEpic(s.getId());
            }
        }
        fileBackedTaskManager.id = maxID;
     return fileBackedTaskManager;
    }

    @Override
    public Task addTask(Task newTask) {
        super.addTask(newTask);
        save();
        return newTask;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
        return newEpic;

    }

    @Override
    public Subtask addSubtask(Subtask newSubtask) {
        super.addSubtask(newSubtask);
        save();
        return newSubtask;
    }


    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    public static void main(String[] args) throws IOException {
// очистка файла
        PrintWriter writer = new PrintWriter("dataBase.csv");
        writer.print("");
        writer.flush();
        writer.close();

        FileBackedTaskManager manager = loadFromFile(new File("dataBase.csv"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTime1 = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);


        manager.addTask(new Task("Задача 1", "Повторить спринт 4", Status.DONE, startTime1.plusHours(12), Duration.ofHours(1)));
        manager.addTask(new Task("Задача 3", "Повторить спринт 8", Status.DONE, startTime1.plusHours(7), Duration.ofHours(1)));
        manager.addTask(new Task("Задача 2", "Начать спринт 5", Status.NEW));
        manager.addTask(new Task("Задача 4", "Повторить спринт 10", Status.DONE, startTime1.plusHours(7), Duration.ofHours(1)));

        Epic epic1 = new Epic("Эпик 1", "Закончить спринт 4");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Эпик 1 Подзадача 1", "Пройти теорию", Status.DONE, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Эпик 1 Подзадача 2", "Выполнить задание спринта", Status.IN_PROGRESS, epic1.getId(),startTime1.plusHours(9), Duration.ofHours(1));
        manager.addSubtask(subtask12);
        Subtask subtask14 = new Subtask("Эпик 1 Подзадача 3", "Выполнить задание спринта 8", Status.IN_PROGRESS, epic1.getId(),startTime1.plusHours(1), Duration.ofHours(1));
        manager.addSubtask(subtask14);
        Subtask subtask15 = new Subtask("Эпик 1 Подзадача 4", "Выполнить задание спринта 9", Status.IN_PROGRESS, epic1.getId(),startTime1.plusHours(1), Duration.ofHours(1));
        manager.addSubtask(subtask15);

        Epic epic2 = new Epic("Эпик 2", "Отдых");
        manager.addEpic(epic2);

        Subtask subtask13 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic2.getId());
        manager.addSubtask(subtask13);

        manager.getTask(1);
        manager.getSubtask(7);
        manager.getEpic(4);
        manager.updateEpic(manager.getEpic(10));

        System.out.println("Задачи " + manager.getTasks());
        System.out.println("Подзадачи " + manager.getSubtasks());
        System.out.println("Эпики " + manager.getEpics());

        System.out.println("История " + manager.getHistory());
        System.out.println("Подзадачи эпика 1 " + manager.getEpicSubtasks(4));
        System.out.println("Подзадачи эпика 2 " + manager.getEpicSubtasks(10));

        System.out.print("Задачи по порядку приоритета " + manager.prioritizedTasks);

            }
        }


