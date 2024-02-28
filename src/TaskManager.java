import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }


    public Task addTask(Task newTask) {
        newTask.setId(++id);;
        tasks.put(newTask.id, newTask);
        return newTask;
    }

    public Epic addEpic(Epic newEpic) {
        newEpic.setId(++id);
        epics.put(newEpic.id, newEpic);
        return newEpic;
    }

    public Subtask addSubtask(Subtask newSubtask) {
        if (epics.containsKey(newSubtask.getEpicID())) {
            newSubtask.setId(++id);
            subtasks.put(newSubtask.getId(), newSubtask);
            epics.get(newSubtask.getEpicID()).addSubIdToEpic(newSubtask.getId());
            checkEpicStatus(epics.get(newSubtask.getEpicID()));

        }
        return newSubtask;
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer sub : epics.get(id).getEpicSubtasks()) {
                    subtasksList.add(subtasks.get(sub));

            }
        }
        return subtasksList;
    }


    public void updateTask(Task task, int id) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }


    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }


    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }


    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(id).description = epic.description;
            epics.get(id).title = epic.title;
        }
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }


    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }


    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.remove(id);
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }


    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (Objects.equals(subtasks.get(subtask.id).getEpicID(), subtask.getEpicID())) {
                subtasks.put(subtask.id, subtask);
                checkEpicStatus(epics.get(subtask.getEpicID()));
            }
        }
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicID()).delSubIdEpic(id);
            checkEpicStatus(epics.get(subtasks.get(id).getEpicID()));
            subtasks.remove(id);
        }
    }

    public void deleteAllSubtask() {
        for (Epic ep : epics.values()) {
            ep.clearSubIdEpic();
            checkEpicStatus(ep);
        }
        subtasks.clear();
    }

    private void checkEpicStatus(Epic epic) {

        if (epic.getEpicSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubtasks()) {
            Status status = subtasks.get(epicSubtaskId).getStatus();
            if (!(status == Status.NEW)) {
                allTaskIsNew = false;
            }
            if (!(status == Status.DONE)) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epic.setStatus(Status.DONE);
        } else if (allTaskIsNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

}