package com.anotherbrick.inthewall.datasource;

import java.util.ArrayList;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class DSFilter {

  public ArrayList<String> genres = new ArrayList<String>();
  public ArrayList<String> keywords = new ArrayList<String>();
  public ArrayList<String> budgets = new ArrayList<String>();
  public ArrayList<String> certificates = new ArrayList<String>();
  public ArrayList<String> countries = new ArrayList<String>();
  public ArrayList<String> ratings = new ArrayList<String>();
  public ArrayList<String> votes = new ArrayList<String>();
  public ArrayList<String> formats = new ArrayList<String>();

  public JSONObject toJson() {
    try {
      JSONObject o = new JSONObject();
      JSONObject f = new JSONObject();
      JSONArray genresObj = new JSONArray(genres);
      JSONArray keywordsObj = new JSONArray(keywords);
      JSONArray budgetsObj = new JSONArray(budgets);
      JSONArray certificatesObj = new JSONArray(certificates);
      JSONArray countriesObj = new JSONArray(countries);
      JSONArray ratingsObj = new JSONArray(ratings);
      JSONArray votesObj = new JSONArray(votes);
      JSONArray formatsObj = new JSONArray(formats);
      
      f.put("genres", genresObj);
      f.put("keywords", keywordsObj);
      f.put("budgets", budgetsObj);
      f.put("certificates", certificatesObj);
      f.put("countries", countriesObj);
      f.put("ratings", ratingsObj);
      f.put("votes", votesObj);
      f.put("formats", formatsObj);
      o.put("filter", f);
      
      return o;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

}
