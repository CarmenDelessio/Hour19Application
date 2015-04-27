package com.talkingandroid.hour19application;

import java.util.ArrayList;

public class Pie {
    int mId;
    String mName;
    String mDescription;
    double mPrice;
    boolean mIsFavorite;

    public Pie(){
    }

    public Pie (String name, String description, double price){
        this.mName = name;
        this.mDescription = description;
        this.mPrice = price;
        this.mIsFavorite=false;
    }

    public static ArrayList<Pie> makePies(){
        ArrayList<Pie> pies = new ArrayList<Pie>();
        pies.add(new Pie("Apple","An old-fashioned favorite. ", 1.0));
        pies.add(new Pie("Blueberry","Made with fresh Maine blueberries.", 1.5));
        pies.add(new Pie("Cherry","Delicious and fresh made daily.", 2.0));
        pies.add(new Pie("Coconut Cream","A customer favorite.", 2.5));
        return pies;
    }
}

