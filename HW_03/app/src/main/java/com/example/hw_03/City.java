package com.example.hw_03;

public class City {
    String citykey;
    String cityname;
    String county;
    String temperature;
    String favourite;

    public String getCitykey() {
        return citykey;
    }

    public void setCitykey(String citykey) {
        this.citykey = citykey;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public City(String citykey, String cityname, String county, String temperature, String favourite) {
        this.citykey = citykey;
        this.cityname = cityname;
        this.county = county;
        this.temperature = temperature;
        this.favourite = favourite;
    }
}
