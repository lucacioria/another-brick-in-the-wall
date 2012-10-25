package com.anotherbrick.inthewall;

public abstract class AbstractMarker extends VizPanel {

  public AbstractMarker(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public abstract boolean draw();

}
