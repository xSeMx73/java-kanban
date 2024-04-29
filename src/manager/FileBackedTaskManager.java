package manager;
import java.io.FileWriter;
import Exceptions.ManagerSaveException;
import task.*;
import java.io.*;



public class FileBackedTaskManager extends InMemoryTaskManager {

    private File dataBase;
    public FileBackedTaskManager(File dataBase) {
       this.dataBase = dataBase;
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(dataBase)) {
            writer.write("id,type,name,status,description,epic" + "\n");
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
   public FileBackedTaskManager loadFromFile (File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int id;
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
                        tasks.put(id, task);
                        break;
                    case "EPIC":
                        id = Integer.parseInt(split[0]);
                        Epic epic = new Epic(split[2], split[4]);
                        epic.setId(id);
                        epics.put(id, epic);
                        break;
                    case "SUBTASK":
                        id = Integer.parseInt(split[0]);
                        Subtask subtask = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
                        subtask.setId(id);
                        subtasks.put(id, subtask);
                        break;
                }
            } else {
                        String history = bufferedReader.readLine();
                        loadHistory(history);
                    }
        }
        bufferedReader.close();
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

        FileBackedTaskManager manager = new FileBackedTaskManager(new File("dataBase.csv"));
         manager.loadFromFile(new File("dataBase.csv"));

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

        Subtask subtask13 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic2.getId());
        manager.addSubtask(subtask13);

        manager.getTask(1);
        manager.getSubtask(5);
        manager.getEpic(3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getHistory());

    }
}


