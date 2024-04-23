package manager;

import task.*;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;



    public static HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();

    }


    @Override
    public Task addTask(Task newTask) {
        newTask.setId(++id);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        newEpic.setId(++id);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    @Override
    public Subtask addSubtask(Subtask newSubtask) {
        if (epics.containsKey(newSubtask.getEpicID())) {
            newSubtask.setId(++id);
            subtasks.put(newSubtask.getId(), newSubtask);
            epics.get(newSubtask.getEpicID()).addSubIdToEpic(newSubtask.getId());
            checkEpicStatus(epics.get(newSubtask.getEpicID()));

        }
        return newSubtask;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer sub : epics.get(id).getEpicSubtasks()) {
                subtasksList.add(subtasks.get(sub));

            }
        }
        return subtasksList;
    }


    @Override
    public void updateTask(Task task, int id) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }


    @Override
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }


    @Override
    public void deleteTask(int id) {
        removeFromHistory(id);
        tasks.remove(id);
    }

    @Override
    public void deleteAllTasks() {
         Set<Integer> tasksID = tasks.keySet();
        for (Integer id: tasksID) {
            removeFromHistory(id);
        }
        tasks.clear();
    }


    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(id).setDescription(epic.getDescription());
            epics.get(id).setTitle(epic.getTitle());
        }
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }


    @Override
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }


    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
                removeFromHistory(subtaskId);
            }
        }
        removeFromHistory(id);
        epics.remove(id);
    }

    @Override
    public void deleteAllEpics() {
        Set<Integer> tasksID = new HashSet<>(epics.keySet());
        tasksID.addAll(subtasks.keySet());
        for (Integer id: tasksID) {
            removeFromHistory(id);
        }
        epics.clear();
        subtasks.clear();
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (Objects.equals(subtasks.get(subtask.getId()).getEpicID(), subtask.getEpicID())) {
                subtasks.put(subtask.getId(), subtask);
                checkEpicStatus(epics.get(subtask.getEpicID()));
            }
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicID()).delSubIdEpic(id);
            checkEpicStatus(epics.get(subtasks.get(id).getEpicID()));
            subtasks.remove(id);
            removeFromHistory(id);
        }
    }

    @Override
    public void deleteAllSubtask() {
        Set<Integer> tasksID = subtasks.keySet();
        for (Integer id: tasksID) {
            removeFromHistory(id);
        }
        for (Epic ep : epics.values()) {
            ep.clearSubIdEpic();
            checkEpicStatus(ep);
        }
        subtasks.clear();
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    private void removeFromHistory(int id) {
        historyManager.remove(id);
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

