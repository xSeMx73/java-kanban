package task;


import static task.TaskType.TASK;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String title;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;


    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;

    }
    public Task (String title, String description, Status status,LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        } else if (duration == null) {
            return startTime;
        } else {
            return startTime.plus(duration);
        }
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public Duration getDuration() {
        return duration;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public TaskType getType(){
        return TASK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status);
    }


}