package com.anotherbrick.inthewall;

import processing.core.PApplet;

public class MyRect {

  float x0, y0, width, height;
  private PApplet p;

  public float getX0() {
    return x0;
  }

  public void setX0(float x0) {
    this.x0 = x0;
  }

  public float getY0() {
    return y0;
  }

  public void setY0(float y0) {
    this.y0 = y0;
  }

  public float getWidth() {
    return width;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public MyRect(float x0, float y0, float width, float height, PApplet p) {
    this.x0 = x0;
    this.y0 = y0;
    this.width = width;
    this.height = height;
    this.p = p;
  }

  public void draw() {
    p.rect(x0, y0, width, height);
  }

  public boolean clicked(int x, int y) {
    if (x > x0 && x < x0 + width && y > y0 && y < y0 + height) return true;
    return false;
  }

}
