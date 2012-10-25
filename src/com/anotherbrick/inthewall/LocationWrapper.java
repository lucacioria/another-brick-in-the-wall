package com.anotherbrick.inthewall;

import com.modestmaps.geo.Location;

public class LocationWrapper {

  private Location location;
  private MarkerType markerType;

  public LocationWrapper(Location location, MarkerType markerType) {
    this.setLocation(location);
    this.setMarkerType(markerType);
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public MarkerType getMarkerType() {
    return markerType;
  }

  public void setMarkerType(MarkerType markerType) {
    this.markerType = markerType;
  }

}
