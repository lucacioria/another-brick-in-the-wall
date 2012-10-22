package com.anotherbrick.inthewall.datasource;

import static us.monoid.web.Resty.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

import com.anotherbrick.inthewall.Config;

public class DataSource {

  public final static String NONE = "none";

  public final static int KEYWORDS = 33;
  public final static String KEYWORD_GIANT_INSECT = "giant-insect";
  public final static String KEYWORD_YETI = "yeti";
  public final static String KEYWORD_ROBOT = "robot";
  public final static String KEYWORD_GHOST = "ghost";
  public final static String KEYWORD_MINOTAUR = "minotaur";
  public final static String KEYWORD_FAIRY = "fairy";
  public final static String KEYWORD_VAMPIRE = "vampire";
  public final static String KEYWORD_MERMAID = "mermaid";
  public final static String KEYWORD_VOODOO = "voodoo";
  public final static String KEYWORD_DRAGON = "dragon";
  public final static String KEYWORD_WEREWOLF = "werewolf";
  public final static String KEYWORD_CYCLOPS = "cyclops";
  public final static String KEYWORD_ELF = "elf";
  public final static String KEYWORD_WITCH = "witch";
  public final static String KEYWORD_DEMON = "demon";
  public final static String KEYWORD_ZOMBIE = "zombie";
  public final static String KEYWORD_VOODOO_ZOMBIE = "voodoozombie";
  public final static String KEYWORD_GHOUL_ZOMBIE = "ghoulzombie";
  public final static String KEYWORD_ALIEN = "alien";
  public final static String KEYWORD_OGRE = "ogre";
  public final static String KEYWORD_MUTANT = "mutant";
  public final static String KEYWORD_MUMMY = "mummy";
  public final static String KEYWORD_BIGFOOT = "bigfoot";
  public final static String KEYWORD_FRANKENSTEIN = "frankenstein";
  public final static String KEYWORD_CERBERUS = "cerberus";
  public final static String KEYWORD_CHIMERA = "chimera";
  public final static String KEYWORD_GIANT_MONSTER = "giant-monster";
  public final static String KEYWORD_LOCH_NESS = "loch-ness";
  public final static String KEYWORD_CENTAUR = "centaur";
  public final static String KEYWORD_GARGOYLE = "gargoyle";
  public final static String KEYWORD_TROLL = "troll";
  public final static String KEYWORD_SEA_MONSTER = "sea-monster";
  public final static String KEYWORD_GNOME = "gnome";
  public final static String KEYWORD_GHOUL = "ghoul";
  public final static String KEYWORD_HYDRA = "hydra";

  public final static int GENRES = 26;
  public final static String GENRE_WESTERN = "Western";
  public final static String GENRE_ANIMATION = "Animation";
  public final static String GENRE_MUSICAL = "Musical";
  public final static String GENRE_THRILLER = "Thriller";
  public final static String GENRE_HORROR = "Horror";
  public final static String GENRE_ACTION = "Action";
  public final static String GENRE_COMEDY = "Comedy";
  public final static String GENRE_NEWS = "News";
  public final static String GENRE_TALK_SHOW = "Talk-Show";
  public final static String GENRE_FANTASY = "Fantasy";
  public final static String GENRE_SPORT = "Sport";
  public final static String GENRE_CRIME = "Crime";
  public final static String GENRE_WAR = "War";
  public final static String GENRE_HISTORY = "History";
  public final static String GENRE_REALITY_TV = "Reality-TV";
  public final static String GENRE_SCI_FI = "Sci-Fi";
  public final static String GENRE_SHORT = "Short";
  public final static String GENRE_FILM_NOIR = "Film-Noir";
  public final static String GENRE_BIOGRAPHY = "Biography";
  public final static String GENRE_FAMILY = "Family";
  public final static String GENRE_ADVENTURE = "Adventure";
  public final static String GENRE_ROMANCE = "Romance";
  public final static String GENRE_DRAMA = "Drama";
  public final static String GENRE_MYSTERY = "Mystery";
  public final static String GENRE_MUSIC = "Music";
  public final static String GENRE_GAME_SHOW = "Game-Show";

  public final static int COUNTRIES = 99;
  public final static String COUNTRIES_BOSNINERZEGOVINA = "Bosnia-and-Herzegovina";
  public final static String COUNTRIES_WESERMANY = "West-Germany";
  public final static String COUNTRIES_SPAIN = "Spain";
  public final static String COUNTRIES_SOUTFRICA = "South-Africa";
  public final static String COUNTRIES_GUATEMALA = "Guatemala";
  public final static String COUNTRIES_YUGOSLAVIA = "Yugoslavia";
  public final static String COUNTRIES_SERBINONTENEGRO = "Serbia-and-Montenegro";
  public final static String COUNTRIES_CANADA = "Canada";
  public final static String COUNTRIES_NETHERLANDS = "Netherlands";
  public final static String COUNTRIES_CUBA = "Cuba";
  public final static String COUNTRIES_BRAZIL = "Brazil";
  public final static String COUNTRIES_TAIWAN = "Taiwan";
  public final static String COUNTRIES_HAITI = "Haiti";
  public final static String COUNTRIES_LUXEMBOURG = "Luxembourg";
  public final static String COUNTRIES_LITHUANIA = "Lithuania";
  public final static String COUNTRIES_FEDERAEPUBLIUGOSLAVIA = "Federal-Republic-of-Yugoslavia";
  public final static String COUNTRIES_UKRAINE = "Ukraine";
  public final static String COUNTRIES_UK = "UK";
  public final static String COUNTRIES_SWITZERLAND = "Switzerland";
  public final static String COUNTRIES_JAMAICA = "Jamaica";
  public final static String COUNTRIES_SLOVAKIA = "Slovakia";
  public final static String COUNTRIES_BURKINASO = "Burkina-Faso";
  public final static String COUNTRIES_PUERTICO = "Puerto-Rico";
  public final static String COUNTRIES_CZECHOSLOVAKIA = "Czechoslovakia";
  public final static String COUNTRIES_ROMANIA = "Romania";
  public final static String COUNTRIES_BULGARIA = "Bulgaria";
  public final static String COUNTRIES_NORWAY = "Norway";
  public final static String COUNTRIES_COLOMBIA = "Colombia";
  public final static String COUNTRIES_JAPAN = "Japan";
  public final static String COUNTRIES_FRANCE = "France";
  public final static String COUNTRIES_NEEALAND = "New-Zealand";
  public final static String COUNTRIES_PORTUGAL = "Portugal";
  public final static String COUNTRIES_ARGENTINA = "Argentina";
  public final static String COUNTRIES_AUSTRIA = "Austria";
  public final static String COUNTRIES_PANAMA = "Panama";
  public final static String COUNTRIES_SWEDEN = "Sweden";
  public final static String COUNTRIES_BARBADOS = "Barbados";
  public final static String COUNTRIES_TOGO = "Togo";
  public final static String COUNTRIES_UNITETATEINOUTLYINSLANDS = "United-States-Minor-Outlying-Islands";
  public final static String COUNTRIES_CZECEPUBLIC = "Czech-Republic";
  public final static String COUNTRIES_ESTONIA = "Estonia";
  public final static String COUNTRIES_ICELAND = "Iceland";
  public final static String COUNTRIES_NORTOREA = "North-Korea";
  public final static String COUNTRIES_URUGUAY = "Uruguay";
  public final static String COUNTRIES_GREENLAND = "Greenland";
  public final static String COUNTRIES_BOLIVIA = "Bolivia";
  public final static String COUNTRIES_GERMANY = "Germany";
  public final static String COUNTRIES_SINGAPORE = "Singapore";
  public final static String COUNTRIES_HONONG = "Hong-Kong";
  public final static String COUNTRIES_COSTICA = "Costa-Rica";
  public final static String COUNTRIES_HUNGARY = "Hungary";
  public final static String COUNTRIES_ISLAN = "Isle-of-Man";
  public final static String COUNTRIES_ZIMBABWE = "Zimbabwe";
  public final static String COUNTRIES_IRAQ = "Iraq";
  public final static String COUNTRIES_EGYPT = "Egypt";
  public final static String COUNTRIES_SOVIENION = "Soviet-Union";
  public final static String COUNTRIES_BAHRAIN = "Bahrain";
  public final static String COUNTRIES_MEXICO = "Mexico";
  public final static String COUNTRIES_MONACO = "Monaco";
  public final static String COUNTRIES_CROATIA = "Croatia";
  public final static String COUNTRIES_UNITERAMIRATES = "United-Arab-Emirates";
  public final static String COUNTRIES_VENEZUELA = "Venezuela";
  public final static String COUNTRIES_INDONESIA = "Indonesia";
  public final static String COUNTRIES_SLOVENIA = "Slovenia";
  public final static String COUNTRIES_NIGERIA = "Nigeria";
  public final static String COUNTRIES_POLAND = "Poland";
  public final static String COUNTRIES_DOMINICAEPUBLIC = "Dominican-Republic";
  public final static String COUNTRIES_USA = "USA";
  public final static String COUNTRIES_THAILAND = "Thailand";
  public final static String COUNTRIES_PAKISTAN = "Pakistan";
  public final static String COUNTRIES_ARMENIA = "Armenia";
  public final static String COUNTRIES_TURKEY = "Turkey";
  public final static String COUNTRIES_AUSTRALIA = "Australia";
  public final static String COUNTRIES_LIECHTENSTEIN = "Liechtenstein";
  public final static String COUNTRIES_EASERMANY = "East-Germany";
  public final static String COUNTRIES_PERU = "Peru";
  public final static String COUNTRIES_ITALY = "Italy";
  public final static String COUNTRIES_TANZANIA = "Tanzania";
  public final static String COUNTRIES_RUSSIA = "Russia";
  public final static String COUNTRIES_ISRAEL = "Israel";
  public final static String COUNTRIES_VIETNAM = "Vietnam";
  public final static String COUNTRIES_SRANKA = "Sri-Lanka";
  public final static String COUNTRIES_SERBIA = "Serbia";
  public final static String COUNTRIES_SOUTOREA = "South-Korea";
  public final static String COUNTRIES_IRELAND = "Ireland";
  public final static String COUNTRIES_INDIA = "India";
  public final static String COUNTRIES_PHILIPPINES = "Philippines";
  public final static String COUNTRIES_IRAN = "Iran";
  public final static String COUNTRIES_MOROCCO = "Morocco";
  public final static String COUNTRIES_GREECE = "Greece";
  public final static String COUNTRIES_ANDORRA = "Andorra";
  public final static String COUNTRIES_MALAYSIA = "Malaysia";
  public final static String COUNTRIES_REPUBLIACEDONIA = "Republic-of-Macedonia";
  public final static String COUNTRIES_DENMARK = "Denmark";
  public final static String COUNTRIES_GHANA = "Ghana";
  public final static String COUNTRIES_CHILE = "Chile";
  public final static String COUNTRIES_FINLAND = "Finland";
  public final static String COUNTRIES_CHINA = "China";
  public final static String COUNTRIES_BELGIUM = "Belgium";

  public final static int BUDGTES = 3;
  public final static String BUDGET_LOW = "low";
  public final static String BUDGET_MEDIUM = "medium";
  public final static String BUDGET_HIGH = "high";
  public final static String BUDGET_NONE = "none";

  public final static int CERTIFICATES = 4;
  public final static String CERTIFICATE_G = "g";
  public final static String CERTIFICATE_PG = "pg";
  public final static String CERTIFICATE_PG13 = "pg13";
  public final static String CERTIFICATE_R = "r";
  public final static String CERTIFICATE_NC17 = "nc17";

  public final static int VOTES = 5;
  public final static String VOTES_LOW = "low";
  public final static String VOTES_MEDIUM = "medium";
  public final static String VOTES_HIGH = "high";

  public final static int RATINGS = 6;
  public final static String RATINGS_LOW = "low";
  public final static String RATINGS_MEDIUM = "medium";
  public final static String RATINGS_HIGH = "high";

  public final static int FORMATS = 7;
  public final static String FEATURE = "feature";
  public final static String TV = "tv";
  public final static String VIDEO = "video";

  private boolean onLocalServer = Config.getInstance().onLocalServer;

  public ArrayList<DSYear> getTimelinePoints(DSFilter filter) {
    try {
      ArrayList<DSYear> out = new ArrayList<DSYear>();
      JSONObject obj = new Resty()
          .json(
              "http://" + (onLocalServer ? "0.0.0.0:3000" : "monster.herokuapp.com")
                  + "/plot/filter", content(filter.toJson())).object().getJSONObject("result");
      for (Integer i = 1890; i <= 2012; i++) {
        try {
          DSYear dsYear = new DSYear();
          JSONObject objYear = (JSONObject) obj.get(i.toString());
          dsYear.number_of_movies = objYear.getInt("number_of_movies");
          dsYear.production_year = objYear.getInt("production_year");
          dsYear.average_rating = objYear.getDouble("rating");
          dsYear.average_budget = objYear.getDouble("average_budget");
          dsYear.total_number_of_monster_movies = objYear.getInt("total_number_of_monster_movies");
          dsYear.total_number_of_all_movies = objYear.getInt("total_number_of_all_movies");
          dsYear.average_votes = objYear.getInt("votes");
          dsYear.number_of_movies_rated = objYear.getInt("number_of_movies_rated");
          for (int j = 0; j < objYear.getJSONArray("movies").length(); j++) {
            JSONObject m = objYear.getJSONArray("movies").getJSONObject(j);
            DSMovie movie = new DSMovie();
            movie.title = m.getString("title");
            movie.database_id = m.getInt("database_id");
            dsYear.movies.add(movie);
          }
          out.add(dsYear);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      return out;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONArray getTopTen() {
    try {
      JSONArray obj = new Resty()
          .json(
              "http://" + (onLocalServer ? "0.0.0.0:3000" : "monster.herokuapp.com")
                  + "/monster/topten").object().getJSONArray("result");
      return obj;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public DSMonster getMonsterData(DSFilter filter) {
    DSMonster monster = new DSMonster();
    try {
      JSONObject obj = new Resty()
          .json(
              "http://" + (onLocalServer ? "0.0.0.0:3000" : "monster.herokuapp.com")
                  + "/monster/filter", content(filter.toJson())).object().getJSONObject("result");
      try {
        monster.name = obj.getString("name");
        // countries
        monster.countries = new HashMap<String, Float>();
        Iterator<String> it = obj.getJSONObject("countries").keys();
        while (it.hasNext()) {
          String key = it.next();
          monster.countries.put(key, (float) obj.getJSONObject("countries").getDouble(key));
        }
        // certificates
        monster.certificates = new HashMap<String, Float>();
        it = obj.getJSONObject("certificates").keys();
        while (it.hasNext()) {
          String key = it.next();
          monster.certificates.put(key, (float) obj.getJSONObject("certificates").getDouble(key));
        }
        // formats
        monster.formats = new HashMap<String, Float>();
        it = obj.getJSONObject("formats").keys();
        while (it.hasNext()) {
          String key = it.next();
          monster.formats.put(key, (float) obj.getJSONObject("formats").getDouble(key));
        }
        // genres
        monster.genres = new HashMap<String, Float>();
        it = obj.getJSONObject("genres").keys();
        while (it.hasNext()) {
          String key = it.next();
          monster.genres.put(key, (float) obj.getJSONObject("genres").getDouble(key));
        }
        // others
        monster.votes_total = obj.getInt("votes_total");
        monster.votes_avg = obj.getInt("votes_avg");
        monster.number_of_movies = obj.getInt("number_of_movies");
        monster.rating_avg = (float) obj.getDouble("rating_avg");
        monster.budget_usd_today_avg = obj.getInt("budget_usd_today_avg");
        //
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return monster;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public DSMovie getMovieByDatabaseId(int databaseId) {
    DSMovie movie = new DSMovie();
    try {
      JSONObject f = new JSONObject();
      f.put("database_id", databaseId);
      JSONObject obj = new Resty()
          .json(
              "http://" + (onLocalServer ? "0.0.0.0:3000" : "monster.herokuapp.com")
                  + "/movies/search_by_id", content(f)).object().getJSONObject("result");
      try {
        setMovieFields(movie, obj);
      } catch (JSONException e) {
        return null;
      }
      return movie;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void setMovieFields(DSMovie movie, JSONObject obj) throws JSONException {
    movie.title = obj.getString("title");
    movie.production_year = obj.getInt("production_year");
    movie.database_id = obj.getInt("database_id");
    movie.rating = (float) obj.getDouble("rating");
    movie.votes = obj.getInt("votes");
    movie.budget = obj.getString("budget");
    movie.certificate = obj.getString("certificate");
    movie.budget_usd_today = obj.getInt("budget_usd_today");
    // genres
    movie.genres = new ArrayList<String>();
    for (int i = 0; i < obj.getJSONArray("genres").length(); i++) {
      movie.genres.add(obj.getJSONArray("genres").getString(i));
    }
  }

  public ArrayList<DSMovie> getAllMovies(String searchString) {
    // returns something ONLY IF searchString is at least 3 characters long
    if (searchString.length() < 3) return null;
    //
    ArrayList<DSMovie> movies = new ArrayList<DSMovie>();
    try {
      JSONObject f = new JSONObject();
      f.put("search_string", searchString);
      JSONObject obj = new Resty()
          .json(
              "http://" + (onLocalServer ? "0.0.0.0:3000" : "monster.herokuapp.com")
                  + "/movies/search_by_title", content(f)).object().getJSONObject("result");
      try {
        for (int i = 0; i < obj.getJSONArray("movies").length(); i++) {
          JSONObject m = obj.getJSONArray("movies").getJSONObject(i);
          DSMovie movie = new DSMovie();
          setMovieFields(movie, m);
          movies.add(movie);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return movies;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }
}
