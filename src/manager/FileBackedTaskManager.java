package manager;
import java.io.FileWriter;
import Exceptions.ManagerSaveException;
import task.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {

     static File dataBase;
    public FileBackedTaskManager(File dataBase) {
        FileBackedTaskManager.dataBase = dataBase;
    }
    public static HistoryManager historyManager = Managers.getDefaultHistory();

    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(dataBase)) {
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Task task : getEpics()) {
                writer.write(task.toString() + "\n");
            }
            for (Task task : getSubtasks()) {
                writer.write(task.toString() + "\n");
            }

            for (Task history : getHistory()) {
                int idHistory = history.getId();
                writer.write(idHistory + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    // Привет Ирек)) Если уж совсем бред написал с методом загрузки истории, не указывай на каждую ошибку, полностью переделаю, дай только направление)
    // Было мало времени на теорию, что-то явно пропусти)
    // Тесты не писал т.к. думаю что придется все переделывать
    // И спасибо за прошлые ревью, было очень информативно и понятно.


public static List<Integer> loadHistory(File file) throws IOException{
        List<String> history = new ArrayList<>();
        List<Integer> historyInt = new ArrayList<>();
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (bufferedReader.ready()) {
           String line = bufferedReader.readLine();
           history = List.of(line.split(","));
           }
    for (String s : history) {
        historyInt.add(Integer.valueOf(s));
    }
        return historyInt;
}
    static FileBackedTaskManager loadFromFile (File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<Integer> history = loadHistory(file);
        int id;
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
                String[] split = line.split(",");
                for (String pieceString : split) {
                    switch (pieceString) {
                        case "TASK":
                            id = Integer.parseInt(split[0]);
                            Task task = new Task(split[2], split[4], Status.valueOf(split[3]));
                            task.setId(--id);
                            ++id;
                            manager.addTask(task);
                            for (Integer idHistory: history) {
                                if(idHistory == null) {break;}
                                if (idHistory == id) {
                                    historyManager.add(manager.getTask(id));
                                }
                            }
                            break;
                        case "EPIC":
                            id = Integer.parseInt(split[0]);
                            Epic epic = new Epic(split[2], split[4]);
                            epic.setId(--id);
                            ++id;
                            manager.addEpic(epic);
                            for (Integer idHistory: history) {
                                if(idHistory == null) {break;}
                                if (idHistory == id) {
                                    historyManager.add(manager.getEpic(id));
                                }
                            }
                            break;
                        case "SUBTASK":
                            id = Integer.parseInt(split[0]);
                            Subtask subtask = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
                            subtask.setId(--id);
                            ++id;
                            manager.addSubtask(subtask);
                            for (Integer idHistory: history) {
                                if(idHistory == null) {break;}
                                if (idHistory == id) {
                                    historyManager.add(manager.getSubtask(id));
                                }
                            }
                            break;
                    }
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

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) throws IOException {

          loadFromFile(dataBase);

//        manager.addTask(new Task("Задача 1", "Повторить спринт 4", Status.DONE));
//        manager.addTask(new Task("Задача 2", "Начать спринт 5", Status.NEW));
//
//
//        Epic epic1 = new Epic("Эпик 1", "Закончить спринт 4");
//        manager.addEpic(epic1);
//
//        Subtask subtask11 = new Subtask("Эпик 1 Подзадача 1", "Пройти теорию", Status.DONE, epic1.getId());
//        manager.addSubtask(subtask11);
//
//        Subtask subtask12 = new Subtask("Эпик 1 Подзадача 2", "Выполнить задание спринта", Status.IN_PROGRESS, epic1.getId());
//        manager.addSubtask(subtask12);
//
//        Epic epic2 = new Epic("Эпик 2", "Отдых");
//        manager.addEpic(epic2);
//
//        Subtask subtask13 = new Subtask("Эпик 2 Подзадача 1", "Поспать", Status.DONE, epic2.getId());
//        manager.addSubtask(subtask13);
//
//
//manager.getTask(1);
//manager.getSubtask(5);
//manager.getEpic(3);
//
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getSubtasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getHistory());

    }
}


