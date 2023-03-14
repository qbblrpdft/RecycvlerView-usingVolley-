package com.example.capstoneapp.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Matches {

    String matchId;
    String userId;
    String teamName;
    String opponent;
    String date;
//    private List<Plotting> plottings;
//    private String userId;
    String plottings;

    public Matches(String matchId, String teamName, String opponent, String date) {
        this.matchId = matchId;
        this.teamName = teamName;
        this.opponent = opponent;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        this.plottings = plottings;
    }

    @Override
    public String toString() {
        return "Matches{" +
                "matchId='" + matchId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", opponent='" + opponent + '\'' +
                ", date='" + date + '\'' +
//                ", plottings='" + plottings + '\'' +
                '}';
    }
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public List<Plotting> getPlottings() {
//        return plottings;
//    }

//    public String getPlottings() {
//        return plottings;
//    }
//
//    public void setPlottings(String plottings) {
//        this.plottings = plottings;
//    }

}
