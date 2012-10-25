package com.anotherbrick.inthewall;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class DefaultMarker extends AbstractMarker {

  public DefaultMarker(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
    super(x0, y0, width, height, parent, id);
  }

  @Override
  public boolean draw() {
    pushStyle();

    fill(MyColorEnum.RED);
    ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
    popStyle();
    return false;
  }

  @Override
  public void setup() {

  }

}
