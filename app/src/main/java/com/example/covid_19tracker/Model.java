package com.example.covid_19tracker;

public class Model {
    String cityName;
    int active, confirmed, deceased, recovered;

    public Model(String cityName, int active, int confirmed, int deceased, int recovered){
        this.cityName = cityName;
        this.active = active;
        this.confirmed = confirmed;
        this.deceased = deceased;
        this.recovered = recovered;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public long getDeceased() {
        return deceased;
    }

    public void setDeceased(int deceased) {
        this.deceased = deceased;
    }

    public long getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
}