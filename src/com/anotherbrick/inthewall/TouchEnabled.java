package com.anotherbrick.inthewall;

public interface TouchEnabled {

  public static enum TouchTypeEnum {
    ONE_FINGER, FIVE_FINGERS
  };

  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType);

  public boolean containsPoint(float x, float y);

  public boolean draw();

  public void setup();

}
