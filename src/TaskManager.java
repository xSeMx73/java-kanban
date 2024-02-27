import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class TaskManager {

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }


    public Task addTask(Task newTask) {
        newTask.id = IDGen.genNewID();
        tasks.put(newTask.id, newTask);
        return newTask;
    }

    public Epic addEpic(Epic newEpic) {
        newEpic.id = IDGen.genNewID();
        newEpic.setStatus(Status.NEW);
        epics.put(newEpic.id, newEpic);
        return newEpic;
    }

    public Subtask addSubtask(Subtask newSubtask) {
        if (epics.containsKey(newSubtask.getEpicID())) {
            newSubtask.id = IDGen.genNewID();
            subtasks.put(newSubtask.id, newSubtask);
            epics.get(newSubtask.getEpicID()).addSubIdToEpic(newSubtask.id);
        }
        return newSubtask;
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Subtask sub : subtasks.values()) {
                if (id == sub.getEpicID()) {
                    subtasksList.add(sub);
                }
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


    public void updateEpic(Epic epic, int id) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
        }
    }

    public Epic getEpic(int id) {
        checkEpicStatus(epics.get(id));
        return epics.get(id);
    }


    public ArrayList<Epic> getEpics() {
        for (Epic ep : epics.values()) {
            checkEpicStatus(ep);
        }
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


    public void updateSubtask(Subtask subtask, int id) {
        if (subtasks.containsKey(id)) {
            if (Objects.equals(subtasks.get(id).getEpicID(), subtask.getEpicID())) {
                subtasks.put(id, subtask);
                subtask.id = id;
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
            subtasks.remove(id);
            checkEpicStatus(epics.get(subtasks.get(id).getEpicID()));

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