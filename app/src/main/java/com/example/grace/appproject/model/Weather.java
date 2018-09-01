package com.example.grace.appproject.model;

/**
 * Created by hosun on 2018-08-30.
 */

public class Weather {

    public Location location;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Rain rain = new Rain();
    public Snow snow = new Snow()	;
    public Clouds clouds = new Clouds();

    public byte[] iconData;

    public  class CurrentCondition {
        private int weatherId;
        private String condition;
        private String descr;
        private String icon;

        private String mornDescr;
        private String mornIcon;
        private String aftDescr;
        private String aftIcon;
        private String evenDescr;
        private String evenIcon;


        private float pressure;
        private float humidity;

        private float mornHumidity;
        private float aftHumidity;
        private float evenHumidity;

        public int getWeatherId() {
            return weatherId;
        }
        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
        }
        public String getCondition() {
            return condition;
        }
        public void setCondition(String condition) {
            this.condition = condition;
        }
        public String getDescr() {
            return descr;
        }
        public void setDescr(String descr) {
            this.descr = descr;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public float getPressure() {
            return pressure;
        }
        public void setPressure(float pressure) {
            this.pressure = pressure;
        }
        public float getHumidity() {
            return humidity;
        }
        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }


        public float getMornHumidity() {
            return mornHumidity;
        }
        public float getAftHumidity() {
            return aftHumidity;
        }
        public float getEvenHumidity() {
            return evenHumidity;
        }
        public void setMornHumidity(float humidity) {
            this.mornHumidity = humidity;
        }
        public void setAftHumidity(float humidity) {
            this.aftHumidity = humidity;
        }
        public void setEvenHumidity(float humidity) {
            this.evenHumidity = humidity;
        }

        public String getMornDescr() {
            return mornDescr;
        }
        public void setMornDescr(String descr) {
            this.mornDescr = descr;
        }
        public String getMornIcon() {
            return mornIcon;
        }
        public void setMornIcon(String icon) {
            this.mornIcon = icon;
        }

        public String getAftDescr() {
            return aftDescr;
        }
        public void setAftDescr(String descr) {
            this.aftDescr = descr;
        }
        public String getAftIcon() {
            return aftIcon;
        }
        public void setAftIcon(String icon) {
            this.aftIcon = icon;
        }

        public String getEvenDescr() {
            return evenDescr;
        }
        public void setEvenDescr(String descr) {
            this.evenDescr = descr;
        }
        public String getEvenIcon() {
            return evenIcon;
        }
        public void setEvenIcon(String icon) {
            this.evenIcon = icon;
        }

    }

    public  class Temperature {
        private float temp;
        private float minTemp;
        private float maxTemp;

        public float getTemp() {
            return temp;
        }
        public void setTemp(float temp) {
            this.temp = temp;
        }
        public float getMinTemp() {
            return minTemp;
        }
        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }
        public float getMaxTemp() {
            return maxTemp;
        }
        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

    public  class Wind {
        private float speed;
        private float deg;
        public float getSpeed() {
            return speed;
        }
        public void setSpeed(float speed) {
            this.speed = speed;
        }
        public float getDeg() {
            return deg;
        }
        public void setDeg(float deg) {
            this.deg = deg;
        }


    }

    public  class Rain {
        private String time;
        private float ammount;
        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public float getAmmount() {
            return ammount;
        }
        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }


    }

    public  class Snow {
        private String time;
        private float ammount;

        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public float getAmmount() {
            return ammount;
        }
        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }


    }

    public  class Clouds {
        private int perc;

        public int getPerc() {
            return perc;
        }

        public void setPerc(int perc) {
            this.perc = perc;
        }


    }

}
