package ar.edu.itba.paw.webapp.dto;

public class ResultDto {

    private int scoreTeam1;
    private int scoreTeam2;

    private ResultDto() {
      /* Required by JSON object mapper */
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }
}
