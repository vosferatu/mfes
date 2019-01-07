package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Transport {
  private String name;
  private Location location;

  public void cg_init_Transport_1(final String transportName, final Number x, final Number y) {

    name = transportName;
    location = new Location(x, y);
    return;
  }

  public Transport(final String transportName, final Number x, final Number y) {

    cg_init_Transport_1(transportName, x, y);
  }

  public String getName() {

    return name;
  }

  public Location getLocation() {

    return location;
  }

  public void setLocation(final Location loc) {

    location = loc;
  }

  public Transport() {}

  public String toString() {

    return "Transport{"
        + "name := "
        + Utils.toString(name)
        + ", location := "
        + Utils.toString(location)
        + "}";
  }
}
