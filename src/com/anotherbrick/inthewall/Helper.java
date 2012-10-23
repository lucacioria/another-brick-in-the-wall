package com.anotherbrick.inthewall;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import processing.core.PApplet;

public class Helper {

  public static int hex2Rgb(String colorStr, PApplet p) {
    int r = Integer.valueOf(colorStr.substring(1, 3), 16);
    int g = Integer.valueOf(colorStr.substring(3, 5), 16);
    int b = Integer.valueOf(colorStr.substring(5, 7), 16);
    return p.color(r, g, b);
  }

  public static int[] toInt(String[] values) {
    int[] out = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      out[i] = Integer.parseInt(values[i]);
    }
    return out;
  }

  public static float costrain(float value, float minValue, float maxValue) {
    return Math.min(Math.max(value, minValue), maxValue);
  }

  public static String formatMoneyValue(Integer value) {
    if (value > 1000 && value < 1000000) {
      value /= 1000;
      return value.toString() + "K";
    } else if (value >= 1000000) {
      value /= 1000000;
      return value.toString() + "M";
    }

    return value.toString();
  }

  public static String floatToString(float number, int decimalValues, boolean separateThousands) {
    return String.format("%" + (separateThousands ? "," : "") + "." + decimalValues + "f", number);
  }

  public static String limitStringLength(String s, int length, boolean dots) {
    if (s.length() > length) {
      if (dots)
        return s.substring(0, length - 3) + "É";
      else
        return s.substring(0, length - 1);
    } else
      return s;
  }

  public static String readFile(String path) throws IOException {
    FileInputStream stream = new FileInputStream(new File(path));
    try {
      FileChannel fc = stream.getChannel();
      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      /* Instead of using default, pass in a decoder. */
      return Charset.defaultCharset().decode(bb).toString();
    } finally {
      stream.close();
    }

  }

  public static Float getOverallXMin(ArrayList<PlotData> plots) {
    float xmin = Float.MAX_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getXMin() < xmin) {
        xmin = p.getXMin();
      }
    }
    return xmin;
  }

  public static Float getOverallXMax(ArrayList<PlotData> plots) {
    float xmax = Float.MIN_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getXMax() > xmax) {
        xmax = p.getXMax();
      }
    }
    return xmax;

  }

  public static Float getOverallYMax(ArrayList<PlotData> plots) {
    float ymax = Float.MIN_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getYMax() > ymax) {
        ymax = p.getYMax();
      }
    }

    return ymax;
  }

  public static Float getOverallYMin(ArrayList<PlotData> plots) {
    float ymin = Float.MAX_VALUE;
    for (PlotData p : plots) {
      if (p != null && p.getYMin() < ymin) {
        ymin = p.getYMin();
      }
    }

    return ymin;
  }

  public static double getTicksRange(int nOfTicks, ArrayList<PlotData> plots) {
    double range = getOverallYMax(plots);
    int tickCount = nOfTicks;
    double unroundedTickSize = range / (tickCount - 1);

    double x = Math.ceil(Math.log10(unroundedTickSize) - 1);
    double pow10x = Math.pow(10, x);
    double roundedTickRange = Math.ceil(unroundedTickSize / pow10x) * pow10x;

    return roundedTickRange;
  }

}
