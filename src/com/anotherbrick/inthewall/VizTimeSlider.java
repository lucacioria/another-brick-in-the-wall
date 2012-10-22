package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Helper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizTimeline.Modes;

public class VizTimeSlider extends VizPanel implements TouchEnabled {

  private static final float TIMELINE_PADDING_LEFT = 0;
  private static final int HANDLE_WIDTH = 0;
  private static final int SLIDER_WIDTH = 0;
  private static final float HANDLE_HEIGHT = 0;
  private static final float GRAPH_WIDTH = 0;
  private static final float SLIDER_GRAPH_HEIGHT = 0;
  private static final float TIMELINE_WIDTH = 0;
  private static final float SLIDER_HEIGHT = 0;
  private static final int TIMELINE_X = 0;
  private VizGraph graph;
  private VizTable table;
  private VizTimeline timeline;
  private float maxValue, minValue;
  private float zoomAreaWidth;
  private ArrayList<Plot> plots;
  private Handle leftHandle, rightHandle;
  private ZoomArea zoomArea;
  private float distanceFromLeftHandle;
  private float distanceFromLRightHandle;

  public VizTimeSlider(float x0, float y0, float width, float height, float parentX0,
      float parentY0, VizGraph graph, VizTable table, VizTimeline timeline) {
    super(x0, y0, width, height, parentX0, parentY0);

    minValue = x0;
    maxValue = x0 + width;
    zoomAreaWidth = width;
    this.graph = graph;
    this.table = table;
    this.timeline = timeline;
  }

  public void setup() {
    plots = new ArrayList<Plot>();
    leftHandle = new Handle(TIMELINE_PADDING_LEFT, 0, x0, y0);
    rightHandle = new Handle(SLIDER_WIDTH - HANDLE_WIDTH, 0, x0, y0);
    zoomArea = new ZoomArea(leftHandle.getX0() + HANDLE_WIDTH, 0, rightHandle.getX0()
        - leftHandle.getX0() - HANDLE_WIDTH, x0, y0);
  }

  public void setMaxTimeRange() {
    leftHandle.modifyPosition(TIMELINE_PADDING_LEFT, 0);
    rightHandle.modifyPosition(SLIDER_WIDTH - HANDLE_WIDTH, 0);
    zoomArea.modifyPositionAndSize(leftHandle.getX0() + HANDLE_WIDTH, 0, rightHandle.getX0()
        - leftHandle.getX0() - HANDLE_WIDTH, HANDLE_HEIGHT);
  }

  private void setXStart(float x0) {
    ArrayList<Plot> plots = new ArrayList<Plot>();

    if (!graph.isClustered()) {
      plots = this.plots;
    } else {
      plots = graph.getClusteredPlots();
    }

    Float xStart = PApplet.map(x0, TIMELINE_PADDING_LEFT, GRAPH_WIDTH, getOverallXMin(plots),
        getOverallXMax(plots));
    graph.setX0(xStart.intValue());
    table.setXStart(xStart.intValue());
  }

  private void setXStop(float xn) {
    ArrayList<Plot> plots = new ArrayList<Plot>();

    if (!graph.isClustered()) {
      plots = this.plots;
    } else {
      plots = graph.getClusteredPlots();
    }

    Float xStop = PApplet.map(xn, TIMELINE_PADDING_LEFT, GRAPH_WIDTH, getOverallXMin(plots),
        getOverallXMax(plots));
    graph.setXn(xStop.intValue());
    table.setxStop(xStop.intValue());
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.DARK_GRAY);
    rectMode(PApplet.CORNER);
    fill(MyColorEnum.MEDIUM_GRAY);
    rect(TIMELINE_PADDING_LEFT, 0, GRAPH_WIDTH - TIMELINE_PADDING_LEFT, SLIDER_GRAPH_HEIGHT);
    ArrayList<Plot> drawPlots = new ArrayList<Plot>();

    for (Plot p : plots) {
      if (p != null) {
        drawPlots.add(p);
      }
    }

    Collections.sort(drawPlots, new Comparator<Plot>() {

      @Override
      public int compare(Plot p1, Plot p2) {
        return (int) (p2.getYPointsSum() - p1.getYPointsSum());
      }
    });

    for (Plot plot : drawPlots) {
      drawPlot(plot);
    }

    updateHandlesPosition();

    rightHandle.draw();
    leftHandle.draw();
    zoomArea.draw();
    drawXAxisLabels();
    popStyle();
    return (leftHandle.moving || rightHandle.moving || zoomArea.moving);
  }

  private void drawPlot(Plot plot) {
    if (plot != null) {
      ArrayList<PVector> points = plot.getPoints();

      pushStyle();
      stroke(plot.getColor());
      strokeWeight(plot.getWeight());
      fill(plot.getColor(), plot.getAlpha());

      beginShape();
      for (PVector point : points) {
        float x = PApplet.map(point.x, plot.getXMin(), plot.getXMax(), TIMELINE_PADDING_LEFT,
            TIMELINE_WIDTH);
        float y = PApplet.map(point.y, getOverallYMin(plots), getOverallYMax(plots),
            SLIDER_GRAPH_HEIGHT, 0);

        vertex(x, y);
      }

      if (plot.isFilled()) {
        vertex(TIMELINE_WIDTH, SLIDER_GRAPH_HEIGHT);
        vertex(TIMELINE_PADDING_LEFT, SLIDER_GRAPH_HEIGHT);
        endShape(PApplet.CLOSE);
      }
      endShape();
      popStyle();
    }
  }

  private void drawXAxisLabels() {
    pushStyle();
    fill(MyColorEnum.WHITE);
    stroke(MyColorEnum.WHITE);
    strokeWeight((float) 1);
    textAlign(PApplet.CENTER, PApplet.BASELINE);
    textSize(12);
    for (int i = getOverallXMin(plots).intValue(); i < getOverallXMax(plots).intValue(); i++) {
      if (i % 10 == 0) {
        int x = (int) PApplet.map(i, getOverallXMin(plots), getOverallXMax(plots),
            TIMELINE_PADDING_LEFT, GRAPH_WIDTH);
        text(Integer.toString(i), x, SLIDER_HEIGHT);
        line(x, HANDLE_HEIGHT, x, SLIDER_HEIGHT - 13);
      }
    }
    popStyle();
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    println("--> touch in VizTimeSlider");
    if (!graph.isClustered()) {
      if (down) {
        if (overLeftKnob(x, y)) {
          println("left handle touched..");
          leftHandle.moving = true;
        } else if (overRightKnob(x, y)) {
          rightHandle.moving = true;
        } else if (overZoomArea(x, y)) {
          leftHandle.moving = true;
          rightHandle.moving = true;
          zoomArea.moving = true;
          distanceFromLeftHandle = x / c.multiply - leftHandle.getX0Absolute();
          distanceFromLRightHandle = rightHandle.getX0Absolute() - x / c.multiply;
        }
        setModal(true);
        return true;
      } else if (!down) {
        leftHandle.moving = false;
        rightHandle.moving = false;
        zoomArea.setMoving(false);
        setModal(false);
      }
    }
    return false;
  }

  public void costrainHandlesForTable() {
    float e = TIMELINE_X + SLIDER_WIDTH;
    rightHandle.modifyPositionWithAbsoluteValue(
        costrain(leftHandle.x0 + 67, e - HANDLE_WIDTH, leftHandle.x0 + 67), rightHandle.y0);
    zoomArea.modifyPositionAndSizeWithAbsoluteValue(leftHandle.getX0Absolute() + HANDLE_WIDTH,
        zoomArea.getY0Absolute(), rightHandle.getX0() - leftHandle.getX0() - HANDLE_WIDTH,
        HANDLE_HEIGHT);

  }

  public void updateHandlesPosition() {
    if (!plots.isEmpty()) {
      if (zoomArea.moving) {
        float o = TIMELINE_PADDING_LEFT + TIMELINE_X;
        float e = TIMELINE_X + SLIDER_WIDTH;
        leftHandle.modifyPositionWithAbsoluteValue(
            costrain(m.touchX - distanceFromLeftHandle, e - zoomArea.width - 2 * HANDLE_WIDTH, o),
            leftHandle.y0);
        rightHandle.modifyPositionWithAbsoluteValue(
            costrain(m.touchX + distanceFromLRightHandle, e - HANDLE_WIDTH, o + HANDLE_WIDTH
                + zoomArea.width), rightHandle.y0);
        zoomArea.modifyPositionWithAbsoluteValue(leftHandle.getX0Absolute() + HANDLE_WIDTH,
            zoomArea.getY0Absolute());
        setXStart(leftHandle.getX0());
        setXStop(rightHandle.getX0() + HANDLE_WIDTH);
        graph.forceYearSliderUpdate();
      } else if (leftHandle.moving && timeline.selection == Modes.GRAPH) {
        leftHandle.modifyPositionWithAbsoluteValue(
            costrain(m.touchX, rightHandle.x0 - HANDLE_WIDTH, TIMELINE_PADDING_LEFT + TIMELINE_X),
            leftHandle.y0);
        zoomArea.modifyPositionAndSizeWithAbsoluteValue(leftHandle.getX0Absolute() + HANDLE_WIDTH,
            zoomArea.getY0Absolute(), rightHandle.getX0() - leftHandle.getX0() - HANDLE_WIDTH,
            HANDLE_HEIGHT);
        setXStart(leftHandle.getX0());
        graph.forceYearSliderUpdate();
      } else if (rightHandle.moving && timeline.selection == Modes.GRAPH) {
        rightHandle.modifyPositionWithAbsoluteValue(
            costrain(m.touchX, maxValue + TIMELINE_X - HANDLE_WIDTH, leftHandle.x0 + HANDLE_WIDTH),
            rightHandle.y0);
        zoomArea.modifyPositionAndSizeWithAbsoluteValue(leftHandle.getX0Absolute() + HANDLE_WIDTH,
            zoomArea.getY0Absolute(), rightHandle.getX0() - leftHandle.getX0() - HANDLE_WIDTH,
            HANDLE_HEIGHT);
        setXStop(rightHandle.getX0() + HANDLE_WIDTH);
        graph.forceYearSliderUpdate();
      }
    }

  }

  private boolean overZoomArea(float x, float y) {
    return zoomArea.containsPoint(x, y);
  }

  private boolean overRightKnob(float x, float y) {
    return rightHandle.containsPoint(x, y);
  }

  private boolean overLeftKnob(float x, float y) {
    return leftHandle.containsPoint(x, y);
  }

  private float costrain(float value, float maxValue, float minValue) {
    return Math.min(Math.max(value, minValue), maxValue);
  }

  public void addPlot(Plot plot, int index) {
    try {
      plots.set(index, plot);
    } catch (IndexOutOfBoundsException e) {
      plots.ensureCapacity(index + 1);
      while (plots.size() < index + 1) {
        plots.add(null);
      }
      plots.set(index, plot);
    }

  }

  public void removePlotAtIndex(int index) {
    try {
      plots.set(index, null);
    } catch (IndexOutOfBoundsException e) {
    }
  }

  private class Handle extends VizPanel {

    boolean moving = false;

    public Handle(float x0, float y0, float parentX0, float parentY0) {
      super(x0, y0, HANDLE_WIDTH, HANDLE_HEIGHT, parentX0, parentY0);
    }

    @Override
    public boolean draw() {
      background(MyColorEnum.WHITE);
      return false;
    }
  }

  private class ZoomArea extends VizPanel {

    private boolean moving = false;

    public ZoomArea(float x0, float y0, float width, float parentX0, float parentY0) {
      super(x0, y0, width, HANDLE_HEIGHT, parentX0, parentY0);
    }

    @Override
    public boolean draw() {
      background(MyColorEnum.WHITE, 100f);
      return false;
    }

    @SuppressWarnings("unused")
    public boolean isMoving() {
      return moving;
    }

    public void setMoving(boolean moving) {
      this.moving = moving;
    }

  }

}
