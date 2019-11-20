package com.george.econtactdemo;

import java.io.Serializable;

/**
 * Created by George on 10/3/2018.
 */

public class Professionals implements Serializable {

    public String name;
    public String mail;
    public String phone;
    public String address;
    public double latitude;
    public double longitude;
    public String profession;
    public String profile;




    public Professionals(String name, String mail,
                         String phone, String address,
                         double latitude, double longitude, String profession, String profile)
    {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profession = profession;
        this.profile = profile;

    }



        public  Professionals() {}




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

/**
 * 1) na mikrynoyme thn mainactivity -MainActivity
 * 2) Log in
 * 3) build a profile for each one
 *
 *
 *
 *
 */
