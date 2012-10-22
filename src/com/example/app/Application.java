package com.example.app;

import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class Application extends VizPanel implements TouchEnabled {

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return false;
  }

}
