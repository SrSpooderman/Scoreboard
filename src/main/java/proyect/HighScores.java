package proyect;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScores {
    private List<ScoreInfo> highScores;

    public HighScores() {
        this.highScores = new ArrayList<>();
        loadScores();
    }

    public void saveScore() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/highscores.txt"))) {
            for (ScoreInfo scoreInfo : highScores) {
                writer.println(scoreInfo.getName() + ":" + scoreInfo.getScore());
            }
        } catch (IOException e) {
            System.out.println("Error guardando los puntajes: " + e.getMessage());
        }
    }

    public void addScore(ScoreInfo score) {
        highScores.add(score);
    }

    private void loadScores(){
        highScores.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/highscores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null){
                addScore(new ScoreInfo(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("HIGH$$");
        for (ScoreInfo score : highScores) {
            sb.append(score.getName()).append(":").append(score.getScore()).append(";");
        }
        return sb.toString();
    }
}
