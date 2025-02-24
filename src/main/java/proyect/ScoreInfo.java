package proyect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreInfo {
    private String name;
    private int score;

    public ScoreInfo(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public ScoreInfo() {

    }
}
