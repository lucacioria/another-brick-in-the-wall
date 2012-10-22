package com.anotherbrick.inthewall;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class Config {

  public boolean onWall = false;
  public String defaultRenderer = PApplet.P2D;
  public String path;
  static Config instance;
  private PApplet p;
  int height = 384;
  int width = 1360;
  int multiply;
  private PFont helvetica;
  private PFont myriad;
  public boolean onLocalServer = false;
  public int currentLanguage = ENGLISH;
  private HashMap<String, String> transations;
  public boolean enableSafeLine = false;
  public boolean enableGridLines = false;
  public boolean enableTagCloud = true;
  public boolean enableDrawMousePosition = false;
  public static final int ENGLISH = 1;
  public static final int ITALIAN = 2;
  public boolean initializeVisualization = false;
  public boolean drawTouch = true;

  public enum MyFontEnum {
    HELVETICA, MYRIAD
  }

  public enum MyColorEnum {
    BLACK, DARK_GRAY, BACKGROUND_COLOR, LIGHT_GRAY, DARK_ORANGE, DARK_BROWN, LIGHT_ORANGE, DARK_BLUE, LIGHT_BLUE, MEDIUM_GRAY, WHITE, LIGHT_GREEN, DARKER_ORANGE, DARKER_BLUE, DARKERER_ORANGE, DARKERER_BLUE, RED, GRAPH_COLOR_1, GRAPH_COLOR_2, GRAPH_COLOR_3, GRAPH_COLOR_4, DARK_WHITE, TEXT_GRAY
  }

  public static void setup(PApplet p) {
    Config.instance = new Config(p);
  }

  public void loadConfig() {
    try {
      // load config from file
      String configString = getFileContents("config.json");
      JSONObject obj = new JSONObject(configString);
      if (obj.getBoolean("translate_to_italian")) {
        this.currentLanguage = ITALIAN;
      } else {
        this.currentLanguage = ENGLISH;
      }
      if (obj.getString("renderer").equalsIgnoreCase("java2d")) {
        this.defaultRenderer = PApplet.JAVA2D;
      } else if (obj.getString("renderer").equalsIgnoreCase("p2d")) {
        this.defaultRenderer = PApplet.P2D;
      } else if (obj.getString("renderer").equalsIgnoreCase("p3d")) {
        this.defaultRenderer = PApplet.P3D;
      }

      enableSafeLine = obj.getBoolean("enable_safe_line");
      enableGridLines = obj.getBoolean("enable_grid_lines");
      onLocalServer = obj.getBoolean("on_local_server");
      enableTagCloud = obj.getBoolean("enable_tag_cloud");
      enableDrawMousePosition = obj.getBoolean("enable_draw_mouse_position");
      drawTouch = obj.getBoolean("draw_touch");
      onWall = obj.getBoolean("on_wall");
      if (obj.getBoolean("multiply_by_2")) {
        multiply = onWall ? 6 : 2;
      } else {
        multiply = onWall ? 6 : 1;
      }
      initializeVisualization = obj.getBoolean("initialize_visualization");
      // load string translation file
      String translationString = getFileContents("translations.json");
      obj = new JSONObject(translationString);
      transations = new HashMap<String, String>();
      Iterator<String> it = obj.keys();
      while (it.hasNext()) {
        String key = it.next();
        transations.put(key, obj.getString(key));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private Config(PApplet p) {
    this.p = p;

    // setup path for data directory
    if (path == null) {
      try {
        path = new java.io.File(".").getCanonicalPath();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (path.substring(path.length() - 4, path.length()).equalsIgnoreCase("/lib")
          || path.substring(path.length() - 4, path.length()).equalsIgnoreCase("/bin")) {
        path = path.substring(0, path.length() - 4);
      }
      path += "/data/";
    }

    // load fonts
    helvetica = p.loadFont(path + "fonts/Helvetica-120.vlw");
    myriad = p.loadFont(path + "fonts/MyriadPro-200.vlw");
  }

  public static Config getInstance() {
    return Config.instance;
  }

  public int getWidthZoom() {
    return width * multiply;
  }

  public int getHeightZoom() {
    return height * multiply;
  }

  public int myColor(MyColorEnum color) {
    switch (color) {
    case DARK_GRAY:
      return hex("#1f1f1f");
    case MEDIUM_GRAY:
      return hex("#282826"); // 2d2d2c
    case LIGHT_GRAY:
      return hex("#2d2d2c"); // 565656
    case TEXT_GRAY:
      return hex("#757575");
    case BLACK:
      return hex("#000000");
    case DARK_ORANGE:
      return hex("#CD6519");
    case DARKER_ORANGE:
      return hex("#854210");
    case DARKERER_ORANGE:
      return hex("#62300C");
    case DARK_BROWN:
      return hex("#453108");
    case DARK_BLUE:
      return hex("#3B6DC2");
    case DARKER_BLUE:
      return hex("#2a4e8b");
    case DARKERER_BLUE:
      return hex("#1D3660");
    case LIGHT_BLUE:
      return hex("#65B6F3");
    case LIGHT_GREEN:
      return hex("#52923C");
    case LIGHT_ORANGE:
      return hex("#EBBD61");
    case WHITE:
      return hex("#f7f7f7");
    case DARK_WHITE:
      return hex("#999999");
    case RED:
      return hex("#E14C41");
    case BACKGROUND_COLOR:
      return hex("#1f1f1f");
    case GRAPH_COLOR_1:
      return hex("#8da940");
    case GRAPH_COLOR_2:
      return hex("#adb0b0");
    case GRAPH_COLOR_3:
      return hex("#f16451");
    case GRAPH_COLOR_4:
      return hex("#4fc1bb");
    default:
      return 0;
    }
  }

  public PFont myFont(MyFontEnum font) {
    switch (font) {
    case HELVETICA:
      return helvetica;
    case MYRIAD:
      return myriad;
    default:
      return myriad;
    }
  }

  private int hex(String color) {
    return Helper.hex2Rgb(color, p);
  }

  public String translate(String x) {
    if (currentLanguage == ENGLISH) {
      return x;
    } else if (currentLanguage == ITALIAN) {
      if (transations.containsKey(x)) {
        return transations.get(x);
      } else {
        return x;
      }
    }
    return "!!!";
  }

  public PImage getImage(String name, String ext) {
    return p.loadImage(path + "images/" + name + multiply + "." + ext);
  }

  public String getFileContents(String filename) {
    try {
      return Helper.readFile(path + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public PShape getShape(String name, String ext) {
    return getShape(name + "." + ext);
  }

  public PShape getShape(String name) {
    PShape s = p.loadShape(path + "images/" + name);
    if (multiply != 1) {
      s.scale(multiply);
    }
    return s;
  }
}