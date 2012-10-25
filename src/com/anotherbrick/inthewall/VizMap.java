package com.anotherbrick.inthewall;

import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
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

  // touch an event location on the map and get more data about it
  // be able to cluster the data on the map and graph (i.e. if I zoom our or
  // into the map or graph the data should form into clusters or clusters should
  // break apart into individual instances)

  private InteractiveMap map;
  private PVector mapOffset;
  private PVector mapSize;
  private PVector lastTouchPos, lastTouchPos2;
  private PVector initTouchPos, initTouchPos2;
  private int touchID1, touchID2;
  private Hashtable<Integer, VizTouch> touchList;
  private HashMap<Integer, LocationWrapper> locationsList;
  private ArrayList<AbstractMarker> markers;
  private static int id;
  private boolean mapTouched;
  private float touchWidth = 5;
  public float MARKER_WIDTH = 10;
  public float MARKER_HEIGHT = 10;

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
    locationsList = new HashMap<Integer, LocationWrapper>();
    markers = new ArrayList<AbstractMarker>();

    mapOffset = new PVector(0, 0);
    mapSize = new PVector(getWidth(), getHeight());

    map = new InteractiveMap(m.p, new Microsoft.RoadProvider(), mapOffset.x, mapOffset.y,
        mapSize.x, mapSize.y);

    m.p.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
        mouseWheel(evt.getWheelRotation());
      }
    });

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

  public void addLocation(float latitude, float longitude, MarkerType markerType, Integer id) {
    Location location = new Location(latitude, longitude);
    LocationWrapper wrapper = new LocationWrapper(location, markerType);
    locationsList.put(id, wrapper);
  }

  public void removeLocation(Integer id) {
    if (locationsList.containsKey(id)) {
      locationsList.remove(id);
    }
  }

  private void drawLocationMarkers() {
    markers.clear();
    for (Map.Entry<Integer, LocationWrapper> pair : locationsList.entrySet()) {
      LocationWrapper wrapper = pair.getValue();
      Point2f point = map.locationPoint(wrapper.getLocation());
      Integer id = pair.getKey();
      MarkerType markerType = wrapper.getMarkerType();
      AbstractMarker marker = null;

      switch (markerType) {
      case DEFAULT_MARKER:
        marker = new DefaultMarker(point.x, point.y, MARKER_WIDTH, MARKER_HEIGHT, this, id);
        break;
      default:
        break;
      }
      markers.add(marker);
      marker.draw();
    }
  }

  public void changeMapStyle(MapStyles style) {
    switch (style) {
    case MICROSOFT_ROAD:
      map.setMapProvider(new Microsoft.RoadProvider());
      break;
    case MICROSOFT_AERIAL:
      map.setMapProvider(new Microsoft.AerialProvider());
      break;
    case MICROSOFT_HYBRID:
      map.setMapProvider(new Microsoft.HybridProvider());
      break;
    default:
      map.setMapProvider(new Microsoft.RoadProvider());
      break;
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

      for (AbstractMarker marker : markers) {
        if (marker.containsPoint(x, y)) {
          LocationWrapper wrapper = locationsList.get(marker.getId());
          Location location = wrapper.getLocation();
          log("You have touched location located at lat: " + location.lat + " and long: "
              + location.lon);
        }
      }

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
}
