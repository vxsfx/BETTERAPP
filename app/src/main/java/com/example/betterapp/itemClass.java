package com.example.betterapp;

public class itemClass {
    //public static count can be used for no. items if required

    public String name;
    public int cost;
    public int total;
    public int weight;
    public int profit;

    public itemClass(String Name, int Cost, int Total, int Weight, int Profit){
        name = Name;
        cost = Cost;
        total = Total;
        weight = Weight;
        profit = Profit;
    }
}
