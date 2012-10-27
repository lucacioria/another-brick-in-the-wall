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

    public int TICK_COUNT = 10;
    public int CLUSTER_SIZE = 10;
    public float PLOT_PADDING_LEFT = 40;
    public float PLOT_PADDING_BOTTOM = 40;
    public float SLIDER_HEIGHT;
    private float xStart = 0, xStop = 0;
    private ArrayList<PlotData> plots;
    private ArrayList<PlotData> clusteredPlots;
    private YearSlider yearSlider;
    private VizTimeSlider timeSlider;
    private boolean clustered;

    public ArrayList<PlotData> getClusteredPlots() {
	return clusteredPlots;
    }

    public boolean isClustered() {
	return clustered;
    }

    public void setClustered(boolean clustered) {
	this.clustered = clustered;
	updateBounds();
    }

    public MyColorEnum[] palette = { MyColorEnum.GRAPH_COLOR_1,
	    MyColorEnum.GRAPH_COLOR_2, MyColorEnum.GRAPH_COLOR_3,
	    MyColorEnum.GRAPH_COLOR_4 };

    public VizGraph(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);
	SLIDER_HEIGHT = height;
	clustered = false;
	setPlots(new ArrayList<PlotData>());
	yearSlider = new YearSlider(0, 0, SLIDER_HEIGHT, this);
	this.clusteredPlots = new ArrayList<PlotData>();
	if (plots.isEmpty()) {
	    plots.ensureCapacity(4);
	    while (plots.size() < 4) {
		plots.add(null);
	    }
	}
	if (clusteredPlots.isEmpty()) {
	    clusteredPlots.ensureCapacity(4);
	    while (clusteredPlots.size() < 4) {
		clusteredPlots.add(null);
	    }
	}
    }

    public void setup() {
	updateBounds();
    }

    public void setTimeSLider(VizTimeSlider timeSlider) {
	this.timeSlider = timeSlider;
    }

    private void addClusteredPlot(PlotData plot, int index) {

	PlotData clusteredPlot = calculateClusteredPlot(plot, CLUSTER_SIZE);
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

    private void updateBounds() {
	if (clustered) {
	    xStop = getOverallXMax(clusteredPlots);
	    xStart = getOverallXMin(clusteredPlots);
//	    timeSlider.setMaxTimeRange();
	} else {
	    xStop = getOverallXMax(getPlots());
	}
    }

    private void sortPlots() {
	ArrayList<PVector> plots = new ArrayList<PVector>();
	for (PlotData p : clusteredPlots) {
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

    public void addPlot(PlotData plot, int index) {
	plots.set(index, plot);
	addClusteredPlot(plot, index);
	updateBounds();
	sortPlots();
	setToRedraw();
    }

    private static PlotData calculateClusteredPlot(PlotData plot, int step) {
	float sum = 0;
	ArrayList<PVector> points = new ArrayList<PVector>();
	for (int i = 0; i < plot.getPoints().size(); i += step) {
	    sum = 0;
	    for (int j = i; j < step + i && j < plot.getPoints().size(); j++) {
		sum += plot.getPoints().get(j).y;
	    }
	    points.add(new PVector(plot.getPoints().get(i).x, sum));   
	}

	PlotData clusteredPlot = new PlotData(points, plot.getColor());
	return clusteredPlot;
    }

    public void removePlot(PlotData plot) {
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
	for (PlotData p : plots) {
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
	if (!startDraw())
	    return false;
	pushStyle();
	fill(MyColorEnum.DARK_GRAY);
	rect(getX0(), getY0(), getWidth(), getHeight());
	textSize(20);
	drawBackground();
	drawAxisLabels();
	ArrayList<PlotData> drawPlots = new ArrayList<PlotData>();
	if (!clustered) {
	    for (PlotData p : getPlots()) {
		if (p != null) {
		    drawPlots.add(p);
		}
	    }
	} else {
	    for (PlotData p : clusteredPlots) {
		if (p != null) {
		    drawPlots.add(p);
		}
	    }
	}

	Collections.sort(drawPlots, new Comparator<PlotData>() {

	    @Override
	    public int compare(PlotData p1, PlotData p2) {
		return (int) (p2.getYPointsSum() - p1.getYPointsSum());
	    }
	});

	for (PlotData cluster : drawPlots) {
	    drawPlot(cluster, drawPlots);
	}

	updateYearSliderPosition();
	yearSlider.draw();
	popStyle();
	return endDraw(yearSlider.moving);
    }

    private void drawPlot(PlotData plot, ArrayList<PlotData> plots) {
	if (plot != null) {

	    ArrayList<PVector> points = plot.getPoints();
	    pushStyle();
	    stroke(plot.getColor());
	    strokeWeight(plot.getWeight());
	    fill(plot.getColor(), plot.getAlpha());
	    float histogramOffset = ((getWidth() - PLOT_PADDING_LEFT) / points
		    .size()) / (plots.indexOf(plot) * (float) 0.25 + 1);

	    Object[] p = points.toArray();

	    beginShape();
	    for (int i = (int) xStart, j = 0; i <= xStop && j < points.size(); i++, j++) {
		float x = PApplet.map(
			((PVector) p[i - (int) plot.getXMin()]).x, xStart,
			xStop, PLOT_PADDING_LEFT, getWidth());
		float y = PApplet.map(
			((PVector) p[i - (int) plot.getXMin()]).y,
			getOverallYMin(plots), getOverallYMax(plots),
			getHeight() - PLOT_PADDING_BOTTOM, 0);
		if (!clustered) {
		    vertex(x, y);
		} else if (j != points.size() - 1) {
		    Float year = ((PVector) p[i - (int) plot.getXMin()]).x;
		    pushStyle();
		    fill(MyColorEnum.WHITE);
		    textAlign(PApplet.LEFT, PApplet.TOP);
		    text(Integer.toString(year.intValue()), x, getHeight() - PLOT_PADDING_BOTTOM);
		    popStyle();
		    vertex(x, getHeight() - PLOT_PADDING_BOTTOM);
		    vertex(x, y - PLOT_PADDING_BOTTOM);
		    vertex(x + histogramOffset, y - PLOT_PADDING_BOTTOM);
		    vertex(x + histogramOffset, getHeight() - PLOT_PADDING_BOTTOM);

		}
	    }

	    if (plot.isFilled()) {
		vertex(getWidth(), getHeight() - PLOT_PADDING_BOTTOM);
		vertex(PLOT_PADDING_LEFT, getHeight() - PLOT_PADDING_BOTTOM);
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
	rect(PLOT_PADDING_LEFT, 0, getWidth() - PLOT_PADDING_LEFT, getHeight()
		- PLOT_PADDING_BOTTOM);
	popStyle();
    }

    private void drawAxisLabels() {
	drawXAxisLabels();
	drawYAxisLabels();
    }

    private void drawXAxisLabels() {

    }

    private void drawYAxisLabels() {
	ArrayList<PlotData> plots = new ArrayList<PlotData>();
	int range;

	if (!clustered) {
	    plots = this.plots;
	} else {
	    plots = this.clusteredPlots;
	}

	if (m.currentDataDisplayed == Datas.AVERAGE_RATING
		|| getOverallYMax(plots) <= 10) {
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
	    int y = (int) PApplet
		    .map(i, getOverallYMin(plots), getOverallYMax(plots),
			    getHeight() - PLOT_PADDING_BOTTOM, 0);

	    if (m.currentDataDisplayed == Datas.AVERAGE_BUDGET
		    || m.currentDataDisplayed == Datas.AVERAGE_VOTES) {
		text(formatMoneyValue(i), PLOT_PADDING_LEFT, y);
	    } else {
		text(Integer.toString(i), PLOT_PADDING_LEFT, y);
	    }

	    line(PLOT_PADDING_LEFT, y, getWidth(), y);
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
	    yearSlider
		    .modifyPositionWithAbsoluteValue(
			    costrain(m.touchX, getWidth() + 20,
				    PLOT_PADDING_LEFT + 20), getY0Absolute());
	    setYear(yearSlider.getX0());
	}
    }

    public void forceYearSliderUpdate() {
	yearSlider.modifyPositionWithAbsoluteValue(
		costrain(m.touchX, getWidth() + 20, PLOT_PADDING_LEFT + 20),
		getY0Absolute());
	setYear(yearSlider.getX0());
    }

    private void setYear(float position) {
	float year = PApplet.map(position + 11, PLOT_PADDING_LEFT, getWidth(),
		xStart, xStop);
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
	private static final float SLIDER_IMAGE_RATIO = 0.106f;
	
	// THE SLIDER ARROW SHOULD EXCEED THE X AXIS BY THE ARROW SIZE
	private static final float SLIDER_IMAGE_ARROW_RATIO = (float) 1/8;

	public YearSlider(float x0, float y0, float height, VizPanel parent) {
	    super(x0, y0, (height + SLIDER_IMAGE_ARROW_RATIO * height)
		    * SLIDER_IMAGE_RATIO, (height + SLIDER_IMAGE_ARROW_RATIO
		    * height), parent);
	    s = c.getShape("yearSlider", "svg");
	}

	@Override
	public boolean draw() {
	    pushStyle();
	    shape(s, getX0(), getY0(), getWidth(), getHeight());
	    popStyle();
	    return false;
	}

	public boolean isMoving() {
	    return moving;
	}

	public void setMoving(boolean moving) {
	    this.moving = moving;
	}

	@Override
	public void setup() {

	}

    }

    public void toggleClustered() {
	setClustered(!clustered);
	updateBounds();
    }

    public ArrayList<PlotData> getPlots() {
	return plots;
    }

    public void setPlots(ArrayList<PlotData> plots) {
	this.plots = plots;
    }

}
