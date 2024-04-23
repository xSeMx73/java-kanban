package task;
import static task.TaskType.EPIC;


import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> epicSubtasksID;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        epicSubtasksID = new ArrayList<>();
    }

    public ArrayList<Integer> getEpicSubtasks() {
        ArrayList<Integer> newEpicSubtasksID = epicSubtasksID;
        return newEpicSubtasksID;
    }

    public TaskType getType(){
        return EPIC;
    }

    public void addSubIdToEpic(int subtaskId) {
        epicSubtasksID.add(subtaskId);
    }

    public void delSubIdEpic(Integer subtaskId) {
        epicSubtasksID.remove(subtaskId);
    }
    public void clearSubIdEpic() {
        epicSubtasksID.clear();
    }



    @Override
    public String toString() {
        return this.getId() + ","
                + this.getType() + ","
                + this.getTitle() + ","
                + this.getStatus() + ","
                + this.getDescription() + ",";
    }
}