package manager;

import task.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
   protected int id;
   protected final Map<Integer, Task> tasks;
   protected final Map<Integer, Subtask> subtasks;
   protected final Map<Integer, Epic> epics;
   protected Set<Task> prioritizedTasks;

   protected HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>(taskComparator);
    }


    @Override
    public Task addTask(Task newTask) {
        if (!(newTask.getStartTime() == null)) {
            if (!valid(newTask)) {
                System.out.println("Задача c Названием:" + newTask.getTitle() + " пересекается по времени с другой задачей");
                return null;
            } else {
                newTask.setId(++id);
                prioritizedTasks.add(newTask);
                tasks.put(newTask.getId(), newTask);

            }
        } else {
            newTask.setId(++id);
            tasks.put(newTask.getId(), newTask);
        }
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
            if (!(newSubtask.getStartTime() == null)) {
                if (!valid(newSubtask)) {
                    System.out.println("Подзадача c названием:" + newSubtask.getTitle() + " пересекается по времени с другой задачей");
                    return null;
                } else {
                    newSubtask.setId(++id);
                    prioritizedTasks.add(newSubtask);
                    epics.get(newSubtask.getEpicID()).setStartTime(newSubtask.getStartTime());
                    epics.get(newSubtask.getEpicID()).setDuration(newSubtask.getDuration());
                    epics.get(newSubtask.getEpicID()).setEndTime(newSubtask.getEndTime());
                }
            }
            newSubtask.setId(++id);
            subtasks.put(newSubtask.getId(), newSubtask);
            epics.get(newSubtask.getEpicID()).addSubIdToEpic(newSubtask.getId());
            checkEpicStatus(epics.get(newSubtask.getEpicID()));
        }
        return newSubtask;
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        List<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            subtasksList = getEpic(id).getEpicSubtasks().stream()
                    .map(subtasks::get)
                    .collect(Collectors.toList());
            }
        return subtasksList;
    }

    @Override
    public void updateTask(Task task, int id) {
        if (tasks.containsKey(id)) {
            if (!(task.getStartTime() == null)) {
                if (!valid(task)) {
                    System.out.println("Задача c ID:" + task.getId() + " Название:" + task.getTitle() + " " + "пересекается по времени с другой задачей");
                    return;
                } else prioritizedTasks.add(task);

            }
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
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(getTask(id));
            removeFromHistory(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        Set<Integer> tasksID = tasks.keySet();
        for (Integer id : tasksID) {
            prioritizedTasks.remove(getTask(id));
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
                prioritizedTasks.remove(getSubtask(subtaskId));
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
        for (Integer id : tasksID) {
          if (getSubtask(id) != null) {
              prioritizedTasks.remove(getSubtask(id));
          }
            removeFromHistory(id);
        }
        epics.clear();
        subtasks.clear();
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        if (Objects.equals(subtasks.get(subtask.getId()).getEpicID(), subtask.getEpicID())) {
          if (subtasks.containsKey(subtask.getId())) {
              if (!(subtask.getStartTime() == null)) {
                  if (!valid(subtask)) {
                      System.out.println("Подзадача c ID:" + subtask.getId() + " Название:" + subtask.getTitle() + " " + "пересекается по времени с другой задачей");
                  } else {
                      subtasks.put(subtask.getId(), subtask);
                      checkEpicStatus(epics.get(subtask.getEpicID()));
                      prioritizedTasks.add(subtask);
                      epics.get(subtask.getEpicID()).setStartTime(subtask.getStartTime());
                      epics.get(subtask.getEpicID()).setDuration(subtask.getDuration());
                      epics.get(subtask.getEpicID()).setEndTime(subtask.getEndTime());
                  }
               } else {
                  subtasks.put(subtask.getId(), subtask);
                  checkEpicStatus(epics.get(subtask.getEpicID()));
              }
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

    public List<Task> getPrioritizedTasks() {
       return new ArrayList<>(prioritizedTasks);
    }


    @Override
    public void deleteSubtask(int id) {
        Subtask tempSub = subtasks.get(id);
        if (subtasks.containsKey(id)) {
            epics.get(tempSub.getEpicID()).delSubIdEpic(id);
            checkEpicStatus(epics.get(tempSub.getEpicID()));

                checkEpicTime(tempSub);
                prioritizedTasks.remove(tempSub);

            subtasks.remove(id);
            removeFromHistory(id);
        }
    }

    @Override
    public void deleteAllSubtask() {
        Set<Integer> tasksID = subtasks.keySet();
        for (Integer id : tasksID) {
           epics.get(getSubtask(id).getEpicID()).timeToNuLL(getSubtask(id));
            prioritizedTasks.remove(getSubtask(id));
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

    private void checkEpicTime (Subtask sub) {
        ArrayList<LocalDateTime> start = new ArrayList<>();
        ArrayList<LocalDateTime> finish = new ArrayList<>();
        ArrayList<Subtask> subtasks = new ArrayList<>(getEpicSubtasks(sub.getEpicID()));
        for (Subtask s : subtasks) {
            if (!(s.getStartTime() == null)) {
                start.add(s.getStartTime());
                finish.add(s.getEndTime());
           }
        }
        if (!start.isEmpty() && !finish.isEmpty()) {
            LocalDateTime minStartTime = Collections.min(start);
            LocalDateTime maxEndTime = Collections.max(finish);
            getEpic(sub.getEpicID()).timeToNuLL(sub);
            epics.get(sub.getEpicID()).setStartTime(minStartTime);
            epics.get(sub.getEpicID()).setEndTime(maxEndTime);
        }
    }



    private boolean valid (Task task) {
        if (prioritizedTasks.isEmpty()) {
            return true;
        }
        LocalDateTime start = task.getStartTime();
        LocalDateTime finish = task.getEndTime();
        if (start == null) {
            return true;
        }
        for (Task prioritizedTask : prioritizedTasks) {
            LocalDateTime begin = prioritizedTask.getStartTime();
            LocalDateTime end = prioritizedTask.getEndTime();

            if (start.isEqual(begin) || start.isEqual(end) || finish.isEqual(end) || finish.isEqual(begin)) return false;
            if ((start.isAfter(begin) && start.isBefore(end)) || (finish.isAfter(begin) && finish.isBefore(end))) return false;
            if (start.isBefore(begin) && finish.isAfter(end)) return false;
        }
        return true;
    }


    Comparator<Task> taskComparator = (o1, o2) -> {
        if (o1.getId() == o2.getId()) {
            return 0;
        }
        if (o1.getStartTime() == null) {
            return 1;
        }
        if (o2.getStartTime() == null) {
            return -1;
        }
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        } else {
            return o1.getId() - o2.getId();
        }
    };

}

