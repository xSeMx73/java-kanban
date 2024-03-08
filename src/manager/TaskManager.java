package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task addTask(Task newTask);

    Epic addEpic(Epic newEpic);

    Subtask addSubtask(Subtask newSubtask);

    ArrayList<Subtask> getEpicSubtasks(int id);

    void updateTask(Task task, int id);

    Task getTask(int id);

    ArrayList<Task> getTasks();

    void deleteTask(int id);

    void deleteAllTasks();

    void updateEpic(Epic epic);

    Epic getEpic(int id);

    ArrayList<Epic> getEpics();

    void deleteEpic(int id);

    void deleteAllEpics();

    void updateSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    ArrayList<Subtask> getSubtasks();

    void deleteSubtask(int id);

    void deleteAllSubtask();
}
