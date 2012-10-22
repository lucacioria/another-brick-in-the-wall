package com.anotherbrick.inthewall;

import omicronAPI.OmicronTouchListener;

public class TouchListener implements OmicronTouchListener {

  private Main p;

  public TouchListener(Main p) {
    this.p = p;
  }

  // Called on a touch down event
  // mousePressed events also call this with an ID of -1 and an xWidth and
  // yWidth of 10.
  @Override
  public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    p.touchDown(ID, xPos, yPos, xWidth, yWidth);
  }// touchDown

  // Called on a touch move event
  // mouseDragged events also call this with an ID of -1 and an xWidth and
  // yWidth of 10.
  @Override
  public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    p.touchMove(ID, xPos, yPos, xWidth, yWidth);
  }// touchMove

  // Called on a touch up event
  // mouseReleased events also call this with an ID of -1 and an xWidth and
  // yWidth of 10.
  @Override
  public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    p.touchUp(ID, xPos, yPos, xWidth, yWidth);
  }// touchUp
}// TouchListener
