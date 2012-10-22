package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Helper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.Model.Datas;

public class VizGraph extends VizPanel implements TouchEnabled {

  private static final float GRAPH_WIDTH = 0;
  private static final int GRAPH_HEIGHT = 0;
  private static final int TIMELINE_WIDTH = 0;
  private static final int TIMELINE_PADDING_LEFT = 0;
  private static final int TICK_COUNT = 0;
  private static final float YAXIS_TICKS_OFFSET = 0;
  private float xStart, xStop;
  private ArrayList<Plot> plots;
  private ArrayList<Plot> clusteredPlots;
  private YearSlider yearSlider;
  private VizTimeSlider timeSlider;
  private boolean clustered;

  public ArrayList<Plot> getClusteredPlots() {
    return clusteredPlots;
  }

  public boolean isClustered() {
    return clustered;
  }

  public void setClustered(boolean clustered) {
    this.clustered = clustered;
  }

  public MyColorEnum[] palette = { MyColorEnum.GRAPH_COLOR_1, MyColorEnum.GRAPH_COLOR_2,
      MyColorEnum.GRAPH_COLOR_3, MyColorEnum.GRAPH_COLOR_4 };

  public VizGraph(float x0, float y0, float width, float height, float parentX0, float parentY0) {
    super(x0, y0, width, height, parentX0, parentY0);
  }

  public void setup() {
    setPlots(new ArrayList<Plot>());
    yearSlider = new YearSlider(30, 0, 22, 252, x0, y0);
    this.clusteredPlots = new ArrayList<Plot>();
    clustered = false;
    xStart = 1980;
    xStop = 2012;
    if (plots.isEmpty()) {
      plots.ensureCapacity(4);
      while (plots.size() < 4) {
        plots.add(null);
      }
    }
  }

  public void setTimeSLider(VizTimeSlider timeSlider) {
    this.timeSlider = timeSlider;
  }

  private void addClusteredPlot(Plot plot, int index) {

    Plot clusteredPlot = calculateClusteredPlot(plot, 10);
    clusteredPlot.setColor(palette[index]);
    clusteredPlot.setFilled(true);

    try {
      clusteredPlots.set(index, clusteredPlot);
    } catch (IndexOutOfBoundsException e) {
      clusteredPlots.ensureCapacity(index + 1);
      while (clusteredPlots.size() < index + 1) {
        clusteredPlots.add(null);
      }
      clusteredPlots.set(index, clusteredPlot);
    }

  }

  private void cluster(boolean cluster) {
    if (cluster) {
      this.clustered = true;
      xStop = getOverallXMax(clusteredPlots);
      xStart = getOverallXMin(clusteredPlots);
      timeSlider.setMaxTimeRange();
    } else {
      this.clustered = false;
      xStop = getOverallXMax(getPlots());
    }
  }

  private void sortPlots() {
    ArrayList<PVector> plots = new ArrayList<PVector>();
    for (Plot p : clusteredPlots) {
      if (p != null) {
        plots.add(new PVector(clusteredPlots.indexOf(p), p.getYMax()));
      }
    }

    Collections.sort(plots, new Comparator<PVector>() {

      @Override
      public int compare(PVector p1, PVector p2) {
        return (int) (p1.y - p2.y);
      }
    });

  }

  public void addPlot(Plot plot, int index) {
    plots.set(index, plot);
    addClusteredPlot(plot, index);

    xStart = getOverallXMin(getPlots());
    xStop = getOverallXMax(getPlots());
    sortPlots();
    setToRedraw();
  }

  private Plot calculateClusteredPlot(Plot plot, int step) {
    float sum = 0;
    ArrayList<PVector> points = new ArrayList<PVector>();

    for (int i = 0; i < plot.getPoints().size(); i += step) {
      sum = 0;
      for (int j = i; j < step + i && j < plot.getPoints().size(); j++) {
        sum += plot.getPoints().get(j).y;
      }
      points.add(new PVector(plot.getPoints().get(i).x, sum));
    }

    Plot clusteredPlot = new Plot(points);
    return clusteredPlot;
  }

  public void removePlot(Plot plot) {
    if (getPlots().contains(plot)) {
      getPlots().remove(plot);
    }
  }

  public void removePlotAtIndex(int index) {
    getPlots().set(index, null);
    if (clusteredPlots.size() == index + 1) {
      clusteredPlots.set(index, null);
    }
  }

  public int getNoOfActivePlots() {
    int sum = 0;
    for (Plot p : plots) {
      if (p != null) {
        sum++;
      }
    }
    return sum;
  }

  @Override
  public float getX0() {
    return xStart;
  }

  public void setX0(float x0) {
    this.xStart = x0;
  }

  public float getXn() {
    return xStop;
  }

  public void setXn(float xn) {
    this.xStop = xn;
  }

  @Override
  public boolean draw() {
    if (!startDraw()) return false;
    pushStyle();
    // draw a black rect on the right to avoid glitches..
    fill(MyColorEnum.DARK_GRAY);
    rect(GRAPH_WIDTH, 0, 15, GRAPH_HEIGHT + 5);
    rect(-100, -20, 100, GRAPH_HEIGHT + 5);
    //
    textSize(20);
    drawBackground();
    drawAxisLabels();
    ArrayList<Plot> drawPlots = new ArrayList<Plot>();
    if (!clustered) {
      for (Plot p : getPlots()) {
        if (p != null) {
          drawPlots.add(p);
        }
      }
    } else {
      for (Plot p : clusteredPlots) {
        if (p != null) {
          drawPlots.add(p);
        }
      }
    }

    Collections.sort(drawPlots, new Comparator<Plot>() {

      @Override
      public int compare(Plot p1, Plot p2) {
        return (int) (p2.getYPointsSum() - p1.getYPointsSum());
      }
    });

    for (Plot cluster : drawPlots) {
      drawPlot(cluster, drawPlots);
    }

    updateYearSliderPosition();
    yearSlider.draw();
    popStyle();
    return endDraw(yearSlider.moving);
  }

  private void drawPlot(Plot plot, ArrayList<Plot> plots) {
    if (plot != null) {

      ArrayList<PVector> points = plot.getPoints();
      pushStyle();
      stroke(plot.getColor());
      strokeWeight(plot.getWeight());
      fill(plot.getColor(), plot.getAlpha());
      float histogramOffset = ((TIMELINE_WIDTH - TIMELINE_PADDING_LEFT) / points.size())
          / (plots.indexOf(plot) * (float) 0.25 + 1);

      Object[] p = points.toArray();

      beginShape();
      for (int i = (int) xStart, j = 0; i <= xStop && j < points.size(); i++, j++) {
        float x = PApplet.map(((PVector) p[i - (int) plot.getXMin()]).x, xStart, xStop,
            TIMELINE_PADDING_LEFT, TIMELINE_WIDTH);
        float y = PApplet.map(((PVector) p[i - (int) plot.getXMin()]).y, getOverallYMin(plots),
            getOverallYMax(plots), GRAPH_HEIGHT, 0);
        if (!clustered) {
          vertex(x, y);
        } else if (j != points.size() - 1) {
          Float year = ((PVector) p[i - (int) plot.getXMin()]).x;
          pushStyle();
          fill(MyColorEnum.WHITE);
          textAlign(PApplet.LEFT, PApplet.TOP);
          text(Integer.toString(year.intValue()), x, GRAPH_HEIGHT);
          popStyle();
          vertex(x, GRAPH_HEIGHT);
          vertex(x, y);
          vertex(x + histogramOffset, y);
          vertex(x + histogramOffset, GRAPH_HEIGHT);

        }
      }

      if (plot.isFilled()) {
        vertex(TIMELINE_WIDTH, GRAPH_HEIGHT);
        vertex(TIMELINE_PADDING_LEFT, GRAPH_HEIGHT);
        endShape(PApplet.CLOSE);
      } else {
        endShape();
      }
      popStyle();
    }
  }

  private void drawBackground() {
    pushStyle();
    noStroke();
    background(MyColorEnum.DARK_GRAY);
    fill(MyColorEnum.MEDIUM_GRAY);
    rect(TIMELINE_PADDING_LEFT, 0, GRAPH_WIDTH - TIMELINE_PADDING_LEFT, GRAPH_HEIGHT);
    popStyle();
  }

  private void drawAxisLabels() {
    drawXAxisLabels();
    drawYAxisLabels();
  }

  private void drawXAxisLabels() {

  }

  private void drawYAxisLabels() {
    ArrayList<Plot> plots = new ArrayList<Plot>();
    int range;

    if (!clustered) {
      plots = this.plots;
    } else {
      plots = this.clusteredPlots;
    }

    if (m.currentDataDisplayed == Datas.AVERAGE_RATING || getOverallYMax(plots) <= 10) {
      range = 1;
    } else {
      range = (int) getTicksRange(TICK_COUNT, plots);
    }
    pushStyle();
    stroke(MyColorEnum.WHITE);
    strokeWeight((float) 0.5);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.RIGHT, PApplet.CENTER);
    for (int i = getOverallYMin(plots).intValue(); i < getOverallYMax(plots); i += range) {
      int y = (int) PApplet.map(i, getOverallYMin(plots), getOverallYMax(plots), GRAPH_HEIGHT, 0);

      if (m.currentDataDisplayed == Datas.AVERAGE_BUDGET
          || m.currentDataDisplayed == Datas.AVERAGE_VOTES) {
        text(formatMoneyValue(i), YAXIS_TICKS_OFFSET, y);
      } else {
        text(Integer.toString(i), YAXIS_TICKS_OFFSET, y);
      }

      line(TIMELINE_PADDING_LEFT, y, TIMELINE_WIDTH, y);
    }
    popStyle();
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      if (overYearSlider(x, y)) {
        yearSlider.moving = true;
      }
      setModal(true);
      return true;
    } else if (!down) {
      yearSlider.moving = false;
      setModal(false);
    }
    return false;
  }

  public void updateYearSliderPosition() {
    if (!getPlots().isEmpty() && yearSlider.moving) {
      yearSlider.modifyPositionWithAbsoluteValue(
          costrain(m.touchX, GRAPH_WIDTH + 20, TIMELINE_PADDING_LEFT + 20), y0);
      setYear(yearSlider.getX0());
    }
  }

  public void forceYearSliderUpdate() {
    yearSlider.modifyPositionWithAbsoluteValue(
        costrain(m.touchX, GRAPH_WIDTH + 20, TIMELINE_PADDING_LEFT + 20), y0);
    setYear(yearSlider.getX0());
  }

  private void setYear(float position) {
    float year = PApplet.map(position + 11, TIMELINE_PADDING_LEFT, GRAPH_WIDTH, xStart, xStop);
    if (!clustered) {
      // yearFocus.setYear((int) year);
    } else {
      year /= 10;
      year = (float) Math.floor(year);
      year *= 10;
      if (year >= xStart) {
        // yearFocus.setYear((int) year);
      } else {
        year = xStart;
      }
    }
    setToRedraw();
  }

  private float costrain(float value, float maxValue, float minValue) {
    return Math.min(Math.max(value, minValue), maxValue);
  }

  public boolean overYearSlider(float x, float y) {
    return yearSlider.containsPoint(x, y);
  }

  private class YearSlider extends VizPanel {

    PShape s;
    private boolean moving = false;

    public YearSlider(float x0, float y0, float width, float height, float parentX0, float parentY0) {
      super(x0, y0, width, height, parentX0, parentY0);
      s = c.getShape("yearSlider", "svg");
    }

    @Override
    public boolean draw() {
      pushStyle();
      shape(s, 0, 0, 22, 252);
      popStyle();
      return false;
    }

    public boolean isMoving() {
      return moving;
    }

    public void setMoving(boolean moving) {
      this.moving = moving;
    }

  }

  public void toggleClustered() {
    if (clustered) {
      cluster(false);
    } else {
      cluster(true);
    }

  }

  public ArrayList<Plot> getPlots() {
    return plots;
  }

  public void setPlots(ArrayList<Plot> plots) {
    this.plots = plots;
  }

}
