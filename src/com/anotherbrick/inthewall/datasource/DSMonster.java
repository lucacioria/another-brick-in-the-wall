package com.anotherbrick.inthewall.datasource;

import java.util.HashMap;

public class DSMonster {

  public String name;
  public HashMap<String, Float> countries;
  public HashMap<String, Float> certificates; // g, pg, pg13, r, nc17
  public HashMap<String, Float> formats; // feature, tv, video
  public HashMap<String, Float> genres; // give only top 2 

  // votes
  public int votes_total, votes_avg, votes_percentage;

  // rating
  public Float rating_avg;

  // budget
  public int budget_usd_today_avg;

  // number of movies
  public int number_of_movies;
}
