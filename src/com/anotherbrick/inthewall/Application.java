package com.anotherbrick.inthewall;

public class Application extends VizPanel implements TouchEnabled {

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  @Override
  public boolean draw() {
    return false;
  }

  public void setup() {
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return true;
  }

}