package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();
    Task addTask(Task newTask);

    Epic addEpic(Epic newEpic);

    Subtask addSubtask(Subtask newSubtask);

    List<Subtask> getEpicSubtasks(int id);

    void updateTask(Task task, int id);

    Task getTask(int id);

   List<Task> getTasks();

    void deleteTask(int id);

    void deleteAllTasks();

    void updateEpic(Epic epic);

    Epic getEpic(int id);

   List<Epic> getEpics();

    void deleteEpic(int id);

    void deleteAllEpics();

    void updateSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    List<Subtask> getSubtasks();

    void deleteSubtask(int id);

    void deleteAllSubtask();

}
