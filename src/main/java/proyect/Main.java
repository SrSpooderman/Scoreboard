package proyect;

public class Main {
    public static void main(String[] args) {
        ScoreServer ss = new ScoreServer();

        Thread t = new Thread(ss);
        t.start();

        ScoreClient scoreClient = new ScoreClient();
    }
}