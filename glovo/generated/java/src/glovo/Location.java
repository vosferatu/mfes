package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Location {
  public Number x;
  public Number y;

  public void cg_init_Location_1(final Number i, final Number j) {

    x = i;
    y = j;
    return;
  }

  public Location(final Number i, final Number j) {

    cg_init_Location_1(i, j);
  }

  public Number getX() {

    return x;
  }

  public Number getY() {

    return y;
  }

  public Location() {}

  public static Number dist(final Location a, final Location b) {

    return (b.x.longValue() - a.x.longValue()) * (b.x.longValue() - a.x.longValue())
        + (b.y.longValue() - a.y.longValue()) * (b.y.longValue() - a.y.longValue());
  }

  public String toString() {

    return "Location{" + "x := " + Utils.toString(x) + ", y := " + Utils.toString(y) + "}";
  }
}
