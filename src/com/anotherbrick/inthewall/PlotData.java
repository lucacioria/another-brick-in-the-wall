package com.anotherbrick.inthewall;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;

import processing.core.*;

public class PlotData {
	
	private ArrayList<PVector> points;
	private MyColorEnum color;
	private int weight;
	private float alpha;
	private boolean filled = false;
	
	public PlotData(ArrayList<PVector> points, MyColorEnum color) {
		this.points = points;
		alpha = 255;
		this.color = color;  
	}
	
	public ArrayList<PVector> getPoints() {
		return this.points;
	}

	public MyColorEnum getColor() {
		return color;
	}
	
	public void setColor(MyColorEnum color) {
		this.color = color;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	public float getXMax() {
		float max = Float.MIN_VALUE;
		for (PVector p:points) {
			if (p.x > max) {
				max = p.x;
			}
		}
		return max;
	}
	
	public float getXMin() {
		float min = Float.MAX_VALUE;
		for (PVector p:points) {
			if (p.x < min) {
				min = p.x;
			}
		}
		return min;
	}
	
	public float getYMax() {
		float max = 0;
		for (PVector p:points) {
			if (p.y > max) {
				max = p.y;
			}
		}
		return max;
	}
	
	public float getYMin() {
		float min = Float.MAX_VALUE;
		for (PVector p:points) {
			if (p.y < min) {
				min = p.y;
			}
		}
		return min;
	}
	
	public float getYPointsSum() {
		float sum = 0;
		for (PVector p: points) {
			sum += p.y;
		}
		return sum;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	

}
