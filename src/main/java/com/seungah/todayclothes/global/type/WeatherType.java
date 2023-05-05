package com.seungah.todayclothes.global.type;

public enum WeatherType {
    DAY_CLEAR_SKY("01d","맑음"),
    DAY_FEW_CLOUDS("02d","구름 많음"),
    DAY_SCATTERED_CLOUDS("03d","흐림"),
    DAY_BROKEN_CLOUDS("04d","흐림"),
    DAY_SHOWER_RAIN("09d","소나기"),
    DAY_RAIN("10d","비"),
    DAY_THUNDERSTORM("11d","천둥번개"),
    DAY_SNOW("13d","눈"),
    DAY_MIST("50d","안개"),
    NIGHT_CLEAR_SKY("01n","맑음"),
    NIGHT_FEW_CLOUDS("02n","구름 많음"),
    NIGHT_SCATTERED_CLOUDS("03n","흐림"),
    NIGHT_BROKEN_CLOUDS("04n","흐림"),
    NIGHT_SHOWER_RAIN("09n","소나기"),
    NIGHT_RAIN("10n","비"),
    NIGHT_THUNDERSTORM("11n","천둥번개"),
    NIGHT_SNOW("13n","눈"),
    NIGHT_MIST("50n","안개");

    private final String key;
    private final String description;

    WeatherType(String key, String description) {
        this.key = key;
        this.description = description;
    }
    public static WeatherType from(String Key) {
        for (WeatherType type : WeatherType.values()) {
            if (type.key.equals(Key)) {
                return type;
            }
        }
        return null;
    }
    public String getDescription() {
        return description;
    }
}
