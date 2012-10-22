package com.anotherbrick.inthewall.datasource;

import java.util.ArrayList;

public class DSYear {
  public int number_of_movies; //
  public int production_year;
  public double average_rating; //
  public double average_budget; //
  public int average_votes; //
  public int number_of_movies_rated;
  public int total_number_of_monster_movies;
  public int total_number_of_all_movies;

  public ArrayList<DSMovie> movies = new ArrayList<DSMovie>();
}
