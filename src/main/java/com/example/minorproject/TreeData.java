package com.example.minorproject;

public class TreeData {
    private String month;
    private String year;
    private String organization;
    private String type;
    private int treesPlanted;
    private String country;

    public TreeData(String month, String year, String organization, String type, int treesPlanted, String country) {
        this.month = month;
        this.year = year;
        this.organization = organization;
        this.type = type;
        this.treesPlanted = treesPlanted;
        this.country = country;
    }

    public String getMonthYear() {
        return month + "-" + year;
    }

    public int getTreesPlanted() {
        return treesPlanted;
    }

    public String getCountry() {
        return country;
    }

    public String getYear() {
        return year;
    }

    public String getOrganization() {
        return organization;
    }

    public String getType() {
        return type;
    }

    public String getMonth() {
        return month;
    }
}
