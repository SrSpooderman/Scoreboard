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
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/highscores.txt", true))){
            for (ScoreInfo scoreInfo : highScores) {
                writer.println(scoreInfo.getName()+":"+scoreInfo.getScore());
            }
        }catch (IOException e) {
            System.out.println("Error guardando los puntajes: " + e.getMessage());
        }
    }

    public void addScore(String score) {
        try{
            String[] parts = score.split(":");
            String name = parts[0].trim();
            int scoreInt = Integer.parseInt(parts[1].trim());

            highScores.add(new ScoreInfo(name,scoreInt));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadScores(){
        highScores.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/highscores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null){
                addScore(line);
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
