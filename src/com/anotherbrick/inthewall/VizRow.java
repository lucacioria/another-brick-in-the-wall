package com.anotherbrick.inthewall;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class VizRow extends VizPanel {

  private static final int ROW_HEIGHT = 0;
  private static final float MOVIE_ROW_X = 0;
  private static final float YEAR_VALUE_X = 0;
  private static final float FIRST_PLOT_X = 0;
  private static final float SECOND_PLOT_X = 0;
  private static final float THIRD_PLOT_X = 0;
  private static final float FOURTH_PLOT_X = 0;
  private ArrayList<PVector> values;
  public boolean selected = false;
  public MyColorEnum backgroundColorSelected, backgroundColor, strokeColor;
  private String name;
  public int cropAtNChars = -1;

  public VizRow(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public void setValues(ArrayList<PVector> values) {
    this.values = values;
  }

  public ArrayList<PVector> getValues() {
    return this.values;
  }

  public void addValue(PVector value) {
    values.add(value);
  }

  public void removeValue(PVector value) {
    if (values.contains(value)) {
      values.remove(value);
    }
  }

  public boolean drawStrings(String name) {
    this.name = name;
    pushStyle();
    noStroke();
    textSize(12);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.LEFT, PApplet.BASELINE);
    text(cropAtNChars != -1 ? Helper.limitStringLength(name, cropAtNChars, true) : name,
        MOVIE_ROW_X, ROW_HEIGHT - 1);
    popStyle();

    return false;
  }

  public void drawBackground() {
    pushStyle();
    if (selected) {
      background(backgroundColorSelected);
    } else {
      background(backgroundColor);
    }
    if (strokeColor != null) {
      stroke(strokeColor);
    } else {
      noStroke();
    }
    popStyle();
  }

  public boolean draw() {
    pushStyle();
    drawBackground();
    fill(MyColorEnum.WHITE);
    textSize(12);
    textAlign(PApplet.CENTER, PApplet.BASELINE);
    if (values != null && values.size() > 0) {
      int year = (int) values.get(0).x;
      text(Integer.toString(year), YEAR_VALUE_X, ROW_HEIGHT - 1);
      textAlign(PApplet.RIGHT, PApplet.BASELINE);
      if (values.get(0) != null)
        text(((Float) values.get(0).y).toString(), FIRST_PLOT_X, ROW_HEIGHT - 1);
      if (values.size() == 2)
        text(((Float) values.get(1).y).toString(), SECOND_PLOT_X, ROW_HEIGHT - 1);
      if (values.size() == 3)
        text(((Float) values.get(2).y).toString(), THIRD_PLOT_X, ROW_HEIGHT - 1);
      if (values.size() == 4)
        text(((Float) values.get(3).y).toString(), FOURTH_PLOT_X, ROW_HEIGHT - 1);
    }
    popStyle();
    return false;
  }

  public String getName() {
    return name;
  }

@Override
public void setup() {
    // TODO Auto-generated method stub
    
}
}
