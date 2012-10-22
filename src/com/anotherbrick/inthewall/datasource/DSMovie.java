package com.anotherbrick.inthewall.datasource;

import java.util.ArrayList;

public class DSMovie {

  public String title;
  public int database_id;

  public float rating;
  public ArrayList<String> genres;
  public int votes;
  public String budget;
  public int budget_usd_today;
  public String certificate; // g, pg, pg13, r, nc17
  public int production_year;

  @Override
  public String toString() {
    return title;
  }

}
