package com.anotherbrick.inthewall;

public abstract class AbstractMarker extends VizPanel {

  private Integer id;

  public AbstractMarker(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
    super(x0, y0, width, height, parent);
    this.setId(id);
  }

  public abstract boolean draw();

  public Integer getId() {
    return id;
  }

  private void setId(Integer id) {
    this.id = id;
  }

}
