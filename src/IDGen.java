public class IDGen {
    private static int taskID = 1;

    public static int genNewID() {
        return taskID++;
    }

}
