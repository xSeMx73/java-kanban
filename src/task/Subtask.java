package task;

import static task.TaskType.SUBTASK;


public class Subtask extends Task {

    private int epicID;


    public Subtask(String title, String description, Status status, Integer epicID) {
        super(title, description,status);
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
        return this.getId() + ","
                + this.getType() + ","
                + this.getTitle() + ","
                + this.getStatus() + ","
                + this.getDescription() + ","
                + this.getEpicID() ;
    }
}