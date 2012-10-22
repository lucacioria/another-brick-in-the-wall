package com.anotherbrick.inthewall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import omicronAPI.OmicronAPI;
import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled.TouchTypeEnum;

public class Main extends PApplet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private boolean enableHandGesture = false;

  private ArrayList<Integer> touchIds = new ArrayList<Integer>();

  private Application application;
  private Model m;
  private Config c;
  OmicronAPI omicronManager;
  TouchListener touchListener;
  private boolean notYetSetup = true;
  private boolean configAlreadySetup = false;
  private Timer timer;
  private MyTimer timerTask5Fingers;
  private boolean timerIsWaitingToRun = false;
  private boolean performTouchUpInTimer = false;

  private class MyTimer extends TimerTask {
    @Override
    public void run() {
      System.out.println("-- TimerTask RUN");
      dispatchOneFingerTouchDown();
      System.out.println("-- TimerTask: justDidA5Fingers was: "
          + (justDidA5Fingers ? "true" : "false") + " .. now set to false");
      justDidA5Fingers = false;
      timerIsWaitingToRun = false;
      cancel();
    }
  };

  private boolean justDidA5Fingers;

  public Application getApplication() {
    return application;
  }

  protected void dispatchOneFingerTouchDown() {
    application.touch((int) m.touchX, (int) m.touchY, true, TouchTypeEnum.ONE_FINGER);
    System.out.println("-- 1 Finger touch DOWN");
  }

  protected void dispatchOneFingerTouchUp() {
    application.touch((int) m.touchX, (int) m.touchY, false, TouchTypeEnum.ONE_FINGER);
    System.out.println("-- 1 Finger touch UP");
  }

  @Override
  public void init() {
    super.init();
    setupConfig();
    // init omicron
    if (c.onWall) {
      omicronManager = new OmicronAPI(this);
      omicronManager.setFullscreen(true);
    }
  }

  private void setupConfig() {
    if (!configAlreadySetup) {
      // load config class
      Config.setup(this);
      c = Config.getInstance();
      c.loadConfig();
    }
  }

  @Override
  public void setup() {
    setupConfig();
    // load model class
    Model.setup(this);
    m = Model.getInstance();
    m.loadFiles();
    //
    if (c.onWall) {
      size(8160, 2304, c.defaultRenderer);
      this.application = new Application(0, 0, c.width, c.height);
    } else {
      size(c.getWidthZoom(), c.getHeightZoom(), c.defaultRenderer);
      this.application = new Application(0, 0, c.width, c.height);
    }
    if (c.onWall) {
      omicronManager.ConnectToTracker(7001, 7340, "131.193.77.159");
      touchListener = new TouchListener(this);
      omicronManager.setTouchListener(touchListener);
    }
    smooth();
    frameRate(10);
  }

  @Override
  public void draw() {
    if (notYetSetup) {
      application.setup();
      notYetSetup = false;
    }
    // mouse compatibility when not on wall
    if (!c.onWall) {
      setTouchXandYinModel();
    }

    // draw application
    application.draw();

    // draw red line on top quarter (too high buttons)
    if (c.enableSafeLine) {
      pushStyle();
      stroke(2);
      stroke(c.myColor(MyColorEnum.RED));
      line(0, height / 4, width, height / 4);
      popStyle();
    }

    // draw grid lines in red
    if (c.enableGridLines) {
      pushStyle();
      stroke(2);
      stroke(c.myColor(MyColorEnum.RED));
      line(0, height / 3, width, height / 3);
      line(0, 2 * height / 3, width, 2 * height / 3);
      for (int j = 1; j < 6; j++)
        line(j * width / 6, 0, j * width / 6, height);
      popStyle();
    }

    // draw current mouse position
    if (c.enableDrawMousePosition) {
      pushStyle();
      stroke(2);
      fill(255);
      rect((width - 80) * c.multiply, (height - 25) * c.multiply, (80) * c.multiply,
          (25) * c.multiply);
      fill(c.myColor(MyColorEnum.RED));
      textAlign(RIGHT, BOTTOM);
      textSize(20 * c.multiply);
      text("[" + mouseX + ", " + mouseY + "]", width, height);
      popStyle();
    }

    // omicron stuff happening..
    if (c.onWall) {
      omicronManager.process();
    }
  }

  @Override
  public void mousePressed() {
    if ((keyPressed == false)) {
      application.touch(mouseX, mouseY, true, TouchTypeEnum.ONE_FINGER);
      System.out.println("--- 1 Finger touch DOWN (mouse)");
    } else if ((keyPressed == true && key == CODED && keyCode == SHIFT)) {
      application.touch(mouseX, mouseY, true, TouchTypeEnum.FIVE_FINGERS);
      System.out.println("--- 5 Fingers touch DOWN (mouse)");
    }
    setTouchXandYinModel();
  }

  private void setTouchXandYinModel() {
    m.touchXZoom = mouseX;
    m.touchYZoom = mouseY;
    m.touchX = mouseX / c.multiply;
    m.touchY = mouseY / c.multiply;
  }

  @Override
  public void mouseReleased() {
    if ((keyPressed == false)) {
      application.touch(mouseX, mouseY, false, TouchTypeEnum.ONE_FINGER);
      System.out.println("--- 1 Finger touch UP (mouse)");
    } else if ((keyPressed == true && key == CODED && keyCode == SHIFT)) {
      application.touch(mouseX, mouseY, false, TouchTypeEnum.FIVE_FINGERS);
      System.out.println("--- 5 Fingers touch UP (mouse)");
    }
    setTouchXandYinModel();
  }

  public void touchDown(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    touchIds.add(ID);
    System.out.println("--- touchDown " + ID);
    if (c.drawTouch) {
      pushStyle();
      noFill();
      stroke(0);
      ellipse(xPos, yPos, xWidth * 2, yWidth * 2);
      popStyle();
    }
    if (enableHandGesture) {
      if (touchIds.size() == 1) {
        timer = new Timer();
        timerTask5Fingers = new MyTimer();
        timer.schedule(timerTask5Fingers, 200);
        timerIsWaitingToRun = true;
        performTouchUpInTimer = false;
        System.out.println("--- Timer Scheduled");
      } else if (touchIds.size() == 5) {
        application.touch((int) xPos, (int) yPos, true, TouchTypeEnum.FIVE_FINGERS);
        timerTask5Fingers.cancel();
        System.out.println("--- Timer Canceled");
        System.out.println("--- 5 Finger touch DOWN");
      }
    } else {
      application.touch((int) xPos, (int) yPos, true, TouchTypeEnum.ONE_FINGER);
    }
  }

  public void touchMove(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    if (c.drawTouch) {
      pushStyle();
      noFill();
      stroke(0);
      ellipse(xPos, yPos, xWidth * 2, yWidth * 2);
      popStyle();
    }
    m.touchXZoom = xPos;
    m.touchYZoom = yPos;
    m.touchX = xPos / c.multiply;
    m.touchY = yPos / c.multiply;
  }

  public void touchUp(int ID, float xPos, float yPos, float xWidth, float yWidth) {
    Iterator<Integer> i = touchIds.iterator();
    while (i.hasNext()) {
      if (i.next().equals(ID)) {
        i.remove();
      }
    }
    if (c.drawTouch) {
      pushStyle();
      noFill();
      stroke(0);
      ellipse(xPos, yPos, xWidth * 2, yWidth * 2);
      popStyle();
    }
    if (enableHandGesture) {
      if (touchIds.size() == 0 && !justDidA5Fingers) {
        application.touch((int) xPos, (int) yPos, false, TouchTypeEnum.ONE_FINGER);
        System.out.println("1 Finger touch UP (Out of Timer)");
      } else if (touchIds.size() == 4) {
        application.touch((int) xPos, (int) yPos, false, TouchTypeEnum.FIVE_FINGERS);
        justDidA5Fingers = true;
        System.out.println("5 Finger touch UP");
      }
    } else {
      application.touch((int) xPos, (int) yPos, false, TouchTypeEnum.ONE_FINGER);
    }
  }
}
