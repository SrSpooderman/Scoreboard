package proyect;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ScoreInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private String name;
    private int score;

    public ScoreInfo(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public ScoreInfo(String line) {
        try{
            String[] parts = line.split(":");
            String name = parts[0].trim();
            int scoreInt = Integer.parseInt(parts[1].trim());

            this.name = name;
            this.score = scoreInt;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
