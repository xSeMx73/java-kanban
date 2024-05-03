package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static task.TaskType.SUBTASK;


public class Subtask extends Task {

    private int epicID;


    public Subtask(String title, String description, Status status, Integer epicID) {
        super(title, description,status);
        this.epicID = epicID;
    }
    public Subtask(String title, String description, Status status, Integer epicID, LocalDateTime startTime, Duration duration) {
        super(title, description,status,startTime,duration);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public TaskType getType(){
        return SUBTASK;
    }


    @Override
    public String toString() {
        return id + ","
                + this.getType() + ","
                + this.getTitle() + ","
                + this.getStatus() + ","
                + this.getDescription() + ","
                + this.getEpicID() + ","
                + Objects.toString(getStartTime(), "") + ","
                + Objects.toString(getDuration(), "") + ","
                + Objects.toString(getEndTime(), "");
    }
}