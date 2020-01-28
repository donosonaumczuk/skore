package ar.edu.itba.paw.webapp.dto;

public class ResultDto {

    private int scoreTeam1;
    private int scoreTeam2;

    private ResultDto(int scoreTeam1, int scoreTeam2) {
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }
}
