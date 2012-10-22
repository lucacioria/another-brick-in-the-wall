package com.anotherbrick.inthewall;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.Config.MyFontEnum;
import com.anotherbrick.inthewall.TouchEnabled.TouchTypeEnum;

public class VizPanel {
  private Main p;
  float x0Zoom, y0Zoom, widthZoom, heightZoom, parentX0Zoom, parentY0Zoom;
  float x0, y0, width, height, parentX0, parentY0;
  boolean redraw = true, firstDraw = true;
  ArrayList<TouchEnabled> touchChildren = new ArrayList<TouchEnabled>();
  public Model m;
  public Config c;
  PGraphics pg;
  private boolean isPg = false;
  private boolean isPGDrawing = false;
  private boolean visible = true;

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    if (visible == this.visible) return;
    this.visible = visible;
    setToRedraw();
  }

  public boolean toggleVisible() {
    this.visible = !visible;
    setToRedraw();
    return visible;
  }

  public VizPanel(PGraphics pg) {
    this.pg = pg;
    isPg = true;
  }

  public VizPanel(float x0, float y0, float width, float height, float parentX0, float parentY0) {
    // with zoom applied
    this.x0Zoom = s(x0 + parentX0);
    this.y0Zoom = s(y0 + parentY0);
    this.parentX0Zoom = parentX0;
    this.parentY0Zoom = parentY0;
    this.widthZoom = s(width);
    this.heightZoom = s(height);

    // without zoom applied
    this.x0 = x0 + parentX0;
    this.y0 = y0 + parentY0;
    this.parentX0 = parentX0;
    this.parentY0 = parentY0;
    this.width = width;
    this.height = height;

    // useful singletons..
    this.p = Model.getInstance().p;
    this.m = Model.getInstance();
    this.c = Config.getInstance();
  }

  public void setModal(boolean val) {
    if (val) {
      m.currentModalVizPanel = (TouchEnabled) this;
      log("Setting " + toString() + " as modal..");
    } else {
      m.currentModalVizPanel = null;
      log("Removing " + toString() + " as modal..");
    }
  }

  public float getX0Absolute() {
    return x0;
  }

  public float getY0Absolute() {
    return y0;
  }

  public float getX0AbsoluteZoom() {
    return x0Zoom;
  }

  public float getY0AbsoluteZoom() {
    return y0Zoom;
  }

  public float getX0() {
    return x0 - parentX0;
  }

  public float getY0() {
    return y0 - parentY0;
  }

  public void setToRedraw() {
    redraw = true;
  }

  public boolean needsRedraw() {
    return redraw || firstDraw;
  }

  public void redrawFinished() {
    redraw = false;
    firstDraw = false;
  }

  public void modifyPositionAndSize(float newX0, float newY0, float newWidth, float newHeight) {
    modifyPosition(newX0, newY0);
    modifySize(newWidth, newHeight);
  }

  public void modifyPositionAndSizeWithAbsoluteValue(float newX0, float newY0, float newWidth,
      float newHeight) {
    modifyPositionWithAbsoluteValue(newX0, newY0);
    modifySize(newWidth, newHeight);
  }

  public void modifyPosition(float newX0, float newY0) {
    this.x0 = newX0 + parentX0;
    this.y0 = newY0 + parentY0;
    this.x0Zoom = s(x0);
    this.y0Zoom = s(y0);
  }

  public void modifyPositionWithAbsoluteValue(float newX0, float newY0) {
    this.x0 = newX0;
    this.y0 = newY0;
    this.x0Zoom = s(x0);
    this.y0Zoom = s(y0);
  }

  public void modifySize(float newWidth, float newHeight) {
    this.width = newWidth;
    this.height = newHeight;
    this.widthZoom = s(width);
    this.heightZoom = s(height);
  }

  public VizPanel(float x0, float y0, float width, float height) {
    this(x0, y0, width, height, 0f, 0f);
  }

  public static VizPanel createFromXYandDimensions(float x0, float y0, float width, float height) {
    return new VizPanel(x0, y0, width, height);
  }

  public static VizPanel createFromXY0andXY1(float x0, float y0, float x1, float y1) {
    return new VizPanel(x0, y0, x1 - x0, y1 - y0);
  }

  public void line(float x1, float y1, float x2, float y2) {
    if (isPg) {
      pg.line(s(x1), s(y1), s(x2), s(y2));
    } else {
      p.line(x0Zoom + s(x1), y0Zoom + s(y1), x0Zoom + s(x2), y0Zoom + s(y2));
    }
  }

  public void rect(float a, float b, float c, float d) {
    p.rect(x0Zoom + s(a), y0Zoom + s(b), s(c), s(d));
  }

  public void rect(float a, float b, float c, float d, float r) {
    p.rect(x0Zoom + s(a), y0Zoom + s(b), s(c), s(d), s(r));
  }

  public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
    p.rect(x0Zoom + s(a), y0Zoom + s(b), s(c), s(d), s(tl), s(tr), s(br), s(bl));
  }

  public void rectMode(int mode) {
    p.rectMode(mode);
  }

  public void image(PImage img, float a, float b) {
    p.image(img, x0Zoom + s(a), y0Zoom + s(b));
  }

  public void image(PImage img, float a, float b, float c, float d) {
    p.image(img, x0Zoom + s(a), y0Zoom + s(b), s(c), s(d));
  }

  public void imageMode(int mode) {
    p.imageMode(mode);
  }

  public void ellipse(float a, float b, float c, float d) {
    p.ellipse(x0Zoom + s(a), y0Zoom + s(b), s(c), s(d));
  }

  public void ellipseMode(int mode) {
    p.ellipseMode(mode);
  }

  public void text(String str, float x, float y) {
    p.text(str, x0Zoom + s(x), y0Zoom + s(y));
  }

  public void textAlign(int alignX, int alignY) {
    p.textAlign(alignX, alignY);
  }

  public void pushStyle() {
    p.pushStyle();
  }

  public void popStyle() {
    p.popStyle();
  }

  public void pushMatrix() {
    p.pushMatrix();
  }

  public void popMatrix() {
    p.popMatrix();
  }

  public void background(MyColorEnum color) {
    pushStyle();
    fill(c.myColor(color));

    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom);
    popStyle();
  }

  public void background(MyColorEnum color, float alpha) {
    pushStyle();
    fill(c.myColor(color), alpha);
    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom);
    popStyle();
  }

  public void background(MyColorEnum color, float alpha, float tl, float tr, float br, float bl) {
    pushStyle();
    fill(c.myColor(color), alpha);
    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom, s(tl), s(tr), s(br), s(bl));
    popStyle();
  }

  public void background(int rgb) {
    pushStyle();
    fill(rgb);

    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom);
    popStyle();
  }

  public void background(int rgb, float alpha) {
    pushStyle();
    fill(rgb, alpha);
    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom);
    popStyle();
  }

  public void textSize(float size) {
    p.textSize(s(size));
  }

  public void textFont(MyFontEnum font) {
    p.textFont(c.myFont(font));
  }

  public void fill(MyColorEnum color) {
    p.fill(c.myColor(color));
  }

  public void fill(MyColorEnum color, float alpha) {
    p.fill(c.myColor(color), alpha);
  }

  public void fill(int rgb) {
    p.fill(rgb);
  }

  public void fill(int rgb, float alpha) {
    p.fill(rgb, alpha);
  }

  public void stroke(int rgb) {
    p.stroke(rgb);
  }

  public void stroke(int rgb, float alpha) {
    p.stroke(rgb, alpha);
  }

  public void stroke(MyColorEnum color) {
    p.stroke(c.myColor(color));
  }

  public void stroke(MyColorEnum color, float alpha) {
    p.stroke(c.myColor(color), alpha);
  }

  public void strokeWeight(float weight) {
    p.strokeWeight(s(weight));
  }

  public void noStroke() {
    p.noStroke();
  }

  public void noFill() {
    p.noFill();
  }

  public void beginShape() {
    p.beginShape();
  }

  public void endShape(int mode) {
    p.endShape(mode);
  }

  public void endShape() {
    p.endShape();
  }

  public void shape(PShape shape, float x, float y) {
    p.shape(shape, x0Zoom + s(x), y0Zoom + s(y));
  }

  public void shape(PShape shape, float x, float y, float width, float height) {
    p.shape(shape, x0Zoom + s(x), y0Zoom + s(y), width, height);
  }

  public void vertex(float x, float y) {
    p.vertex(s(x) + x0Zoom, s(y) + y0Zoom);
  }

  @SuppressWarnings("static-access")
  public float radians(float angle) {
    return p.radians(angle);
  }

  public void arc(float x, float y, float startEdge, float stopEdge, float startAngle,
      float stopAngle) {
    p.arc(s(x), s(y), s(startEdge), s(stopEdge), startAngle, stopAngle);
  }

  // kevin
  public PGraphics createGraphics(float a, float b) {
    return p.createGraphics((int) s(a), (int) s(b));
  }

  public void createGraphicsImage(PGraphics pg, float c, float d) {
    p.image(pg, s(c), s(d));
  }

  public PShape loadShape(String filename) {
    return p.loadShape(filename);

  }

  public void textLeading(float a) {
    p.textLeading(s(a));
  }

  // kevin ends here

  public void println(String what) {
    PApplet.println(what);
  }

  protected float s(float x) {
    return x * Config.getInstance().multiply;
  }

  protected int s(int x) {
    return x * Config.getInstance().multiply;
  }

  public boolean containsPoint(float x, float y) {
    return x > x0Zoom && x < x0Zoom + widthZoom && y > y0Zoom && y < y0Zoom + heightZoom;
  }

  public boolean draw() {
    p.pushStyle();
    p.fill(0xff000000);
    p.rect(x0Zoom, y0Zoom, widthZoom, heightZoom);
    p.popStyle();

    return false;
  }

  public void addTouchSubscriber(TouchEnabled child) {
    touchChildren.add(child);
  }

  public boolean propagateTouch(float x, float y, boolean down, TouchTypeEnum touchType) {
    boolean consumed = false;
    for (TouchEnabled child : touchChildren) {
      if (child.containsPoint(x, y) && (((VizPanel) child).isVisible())
          && child.touch(x, y, down, touchType)) {
        consumed = true;
        break;
      }
    }
    return consumed;
  }

  boolean startDraw() {
    if (!needsRedraw() || !isVisible()) return false;
    log("DRAW " + toString());
    return true;
  }

  boolean endDraw(boolean needsToBeRedrawn) {
    if (!needsToBeRedrawn) {
      redrawFinished();
      return false;
    } else {
      setToRedraw();
      return true;
    }
  }

  public String toString() {
    return this.getClass().getSimpleName();
  }

  public void log(String msg) {
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    println("[" + dateFormat.format(date) + String.format("%25s", this.getClass().getSimpleName())
        + "] " + msg);
  }

  public void beginPGDraw() {
    if (!isPGDrawing) {
      pg.beginDraw();
    }
  }

  public PGraphics getPGImage() {
    if (isPGDrawing == false)
      return null;
    else {
      pg.endDraw();
      return pg;
    }
  }
}
