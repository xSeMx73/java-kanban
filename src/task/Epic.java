package task;
import static task.TaskType.EPIC;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> epicSubtasksID;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        epicSubtasksID = new ArrayList<>();
    }

    @Override
    public void setDuration(Duration duration) {
        if (super.duration != null) {
            super.setDuration(super.duration.plus(duration));
        } else {
            super.setDuration(duration);
        }
    }
public void timeToNuLL (Subtask subtask){
        super.setStartTime(null);
        endTime = null;
    if (!(super.duration == null) && !(subtask.getDuration() == null)) {
        super.setDuration(super.duration.minus(subtask.getDuration()));
        if (super.getDuration().toMinutes() == 0){
            super.setDuration(null);
        }
    }
}



    @Override
    public void setStartTime(LocalDateTime startTime) {
        if (super.startTime == null) {
            super.setStartTime(startTime);
        } else if (super.startTime.isAfter(startTime)) {
            super.setStartTime(startTime);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        if (this.endTime == null) {
            this.endTime = endTime;
        } else if (this.endTime.isBefore(endTime)) {
            this.endTime = endTime;
        }
    }
    @Override
    public LocalDateTime getEndTime() {
            return this.endTime;
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
        return id + ","
                + this.getType() + ","
                + this.getTitle() + ","
                + this.getStatus() + ","
                + this.getDescription() + ","
                + ","
                + Objects.toString(getStartTime(), "") + ","
                + Objects.toString(getDuration(), "") + ","
                + Objects.toString(getEndTime(), "");
    }
}