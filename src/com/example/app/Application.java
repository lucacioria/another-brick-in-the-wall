package com.example.app;

import com.anotherbrick.inthewall.MapStyles;
import com.anotherbrick.inthewall.MarkerType;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizMap;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.Config.MyColorEnum;

public class Application extends VizPanel implements TouchEnabled {

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  private VizMap map;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    map = new VizMap(0, 0, 512, 256, this);
    map.setup();
    addTouchSubscriber(map);
    map.changeMapStyle(MapStyles.MICROSOFT_AERIAL);
    map.addLocation(41.9f, -87.6f, MarkerType.DEFAULT_MARKER, 0);
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.DARK_BLUE);
    map.draw();
    popStyle();
    return false;
  }

}
