package com.example.capstoneapp;

public class Records {
    private String date;
    private String teamName;
    private String opponent;

    public Records(String date, String teamName, String opponent) {
        this.date = date;
        this.teamName = teamName;
        this.opponent = opponent;
    }

    public String getDate() {
        return date;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getOpponent() {
        return opponent;
    }
}

