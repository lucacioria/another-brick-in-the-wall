package com.anotherbrick.inthewall;

import java.awt.event.MouseWheelListener;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.modestmaps.InteractiveMap;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Microsoft;
import com.modestmaps.providers.Yahoo;

public class VizMap extends VizPanel implements TouchEnabled {

  private InteractiveMap map;
  private PVector mapOffset;
  private PVector mapSize;
  private PVector lastTouchPos, lastTouchPos2;
  private PVector initTouchPos, initTouchPos2;
  private int touchID1, touchID2;
  private Hashtable<Integer, VizTouch> touchList;
  private static int id;
  private boolean mapTouched;
  private Logger logger;

  Location locationChicago = new Location(41.9f, -87.6f);

  public VizMap(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    this.parent = parent;
  }

  @Override
  public void setup() {
    logger = Logger.getLogger(VizMap.class.getName());

    lastTouchPos = new PVector();
    lastTouchPos2 = new PVector();
    initTouchPos = new PVector();
    initTouchPos2 = new PVector();

    touchList = new Hashtable<Integer, VizTouch>();

    mapOffset = new PVector(0, 0);
    mapSize = new PVector(300, 300);

    map = new InteractiveMap(m.p, new Microsoft.RoadProvider(), mapOffset.x, mapOffset.y,
        mapSize.x, mapSize.y);
    map.setCenterZoom(locationChicago, 11);

    m.p.addMouseWheelListener(new MouseWheelListener() {

      @Override
      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
        mouseWheel(evt.getWheelRotation());
      }
    });

  }

  protected void mouseWheel(int wheelRotation) {
    float sc = 1.0f;
    if (wheelRotation < 0) {
      sc = 1.05f;
    } else if (wheelRotation > 0) {
      sc = 1.0f / 1.05f;
    }
    float mx = m.touchX - getWidth() / 2;
    float my = m.touchY - getHeight() / 2;
    map.tx -= mx / map.sc;
    map.ty -= my / map.sc;
    map.sc *= sc;
    map.tx += mx / map.sc;
    map.ty += my / map.sc;

  }

  @Override
  public boolean draw() {
    pushStyle();

    background(MyColorEnum.BLACK);
    updateMapZoomAndPosition();

    map.draw();

    noFill();
    stroke(MyColorEnum.RED);
    strokeWeight(10);
    rect(mapOffset.x, mapOffset.y, mapSize.x, mapSize.y);

    popStyle();
    return false;
  }

  private void updateMapZoomAndPosition() {
    if (mapTouched) {
      if (touchList.size() < 2) {
        map.tx += (m.touchX - lastTouchPos.x) / map.sc;
        map.ty += (m.touchY - lastTouchPos.y) / map.sc;
      } else if (touchList.size() == 2) {
        float sc = dist(lastTouchPos.x, lastTouchPos.y, lastTouchPos2.x, lastTouchPos2.y);
        float initPos = dist(initTouchPos.x, initTouchPos.y, initTouchPos2.x, initTouchPos2.y);

        PVector midpoint = new PVector((lastTouchPos.x + lastTouchPos2.x) / 2,
            (lastTouchPos.y + lastTouchPos2.y) / 2);
        sc -= initPos;
        sc /= 5000;
        sc += 1;
        float mx = (midpoint.x - mapOffset.x) - mapSize.x / 2;
        float my = (midpoint.y - mapOffset.y) - mapSize.y / 2;
        map.tx -= mx / map.sc;
        map.ty -= my / map.sc;
        map.sc *= sc;
        map.tx += mx / map.sc;
        map.ty += my / map.sc;
      }
    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      lastTouchPos.x = x;
      lastTouchPos.y = y;

      VizTouch t = new VizTouch(id, x, y, 5, 5);
      touchList.put(id, t);

      if (touchList.size() == 1) {
        touchID1 = id;
        initTouchPos.x = x;
        initTouchPos.y = y;
      } else if (touchList.size() == 2) {
        touchID2 = id;
        initTouchPos2.x = x;
        initTouchPos2.y = y;
      }
      mapTouched = true;
      logger.log(Level.INFO, "Map Touched");
    } else if (!down) {
      touchList.remove(id);
      mapTouched = false;
    }

    return false;
  }

}
