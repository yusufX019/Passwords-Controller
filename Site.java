package com.ilova;

import java.util.Random;

public class Site {
    private static int id=0;
    private String password;
    private String logIn;
    private static String name;

    public Site(String password, String logIn, String name) {
        this.password = password;
        this.logIn = logIn;
        this.name = name;

        //setting random id to the objects
        Random randomizer=new Random();
        id=randomizer.nextInt(1000);
    }


    public String getPassword() {
        return password;
    }

    public String getLogIn() {
        return logIn;
    }


    public static String getName() {
        return name;
    }

    public static int getId() {
        return id;
    }



}
