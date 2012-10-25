package com.anotherbrick.inthewall;

import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.modestmaps.InteractiveMap;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Microsoft;

public class VizMap extends VizPanel implements TouchEnabled {

  private InteractiveMap map;
  private PVector mapOffset;
  private PVector mapSize;
  private PVector lastTouchPos, lastTouchPos2;
  private PVector initTouchPos, initTouchPos2;
  private int touchID1, touchID2;
  private Hashtable<Integer, VizTouch> touchList;
  private HashMap<Location, MarkerType> markersList;
  private static int id;
  private boolean mapTouched;
  private float touchWidth = 5;
  public float MARKER_WIDTH = 10;
  public float MARKER_HEIGHT = 10;

  // Features: different markers, depending on the data. Possibility to change
  // map style

  Location locationChicago = new Location(41.9f, -87.6f);

  public VizMap(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    this.parent = parent;
  }

  @Override
  public void setup() {

    lastTouchPos = new PVector();
    lastTouchPos2 = new PVector();
    initTouchPos = new PVector();
    initTouchPos2 = new PVector();

    touchList = new Hashtable<Integer, VizTouch>();
    markersList = new HashMap<Location, MarkerType>();

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
    drawLocationMarkers();

    noFill();
    stroke(MyColorEnum.RED);
    strokeWeight(10);
    rect(mapOffset.x, mapOffset.y, mapSize.x, mapSize.y);

    popStyle();
    return false;
  }

  public void addLocation(float latitude, float longitude, MarkerType markerType) {
    Location location = new Location(latitude, longitude);
    markersList.put(location, markerType);

  }

  private void drawLocationMarkers() {
    for (Map.Entry<Location, MarkerType> pair : markersList.entrySet()) {
      Point2f point = map.locationPoint(pair.getKey());
      MarkerType markerType = pair.getValue();
      AbstractMarker marker = null;

      switch (markerType) {
      case DEFAULT_MARKER:
        marker = new DefaultMarker(point.x, point.y, MARKER_WIDTH, MARKER_HEIGHT, this);

        break;

      default:
        break;
      }

      marker.draw();

    }
  }

  private void updateMapZoomAndPosition() {
    if (mapTouched) {

      float xPos = m.touchX;
      float yPos = m.touchY;

      if (touchList.size() < 2) {
        map.tx += (xPos - lastTouchPos.x) / map.sc;
        map.ty += (yPos - lastTouchPos.y) / map.sc;
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

      if (id == touchID1) {
        lastTouchPos.x = xPos;
        lastTouchPos.y = yPos;
      } else if (id == touchID2) {
        lastTouchPos2.x = xPos;
        lastTouchPos2.y = yPos;
      }

      // Update touch list
      VizTouch t = new VizTouch(id, xPos, yPos, touchWidth, touchWidth);
      touchList.put(id, t);

    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      lastTouchPos.x = x;
      lastTouchPos.y = y;

      VizTouch t = new VizTouch(id, x, y, touchWidth, touchWidth);
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
      log("Map touched");
    } else if (!down) {
      touchList.remove(id);
      mapTouched = false;
    }

    return false;
  }

}
