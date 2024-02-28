

public class Subtask extends Task {

    private int epicID;


    public Subtask(String title, String description,Status status, Integer epicID) {
        super(title, description,status);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }
    

    @Override
    public String toString() {
        return "Подзадача{" +
                "ID " + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }
}