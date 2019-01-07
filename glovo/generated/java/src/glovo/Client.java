package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Client {
  private String name;
  public static Number clientID = 0L;
  public Number id = Client.clientID;
  private Number balance;
  public Location location;

  public void cg_init_Client_1(final String clientName, final Number x, final Number y) {

    name = clientName;
    id = Client.clientID;
    clientID = Client.clientID.longValue() + 1L;
    balance = 5L;
    location = new Location(x, y);
    return;
  }

  public Client(final String clientName, final Number x, final Number y) {

    cg_init_Client_1(clientName, x, y);
  }

  public String getName() {

    return name;
  }

  public Number getID() {

    return id;
  }

  public Number getBalance() {

    return balance;
  }

  public void credit(final Number amount) {

    balance = balance.doubleValue() + amount.doubleValue();
  }

  public void debit(final Number amount) {

    balance = balance.doubleValue() - amount.doubleValue();
  }

  public Boolean checkCredit(final VDMSet list, final Number fee) {

    Number sum = 0L;
    for (Iterator iterator_11 = list.iterator(); iterator_11.hasNext(); ) {
      Product product = (Product) iterator_11.next();
      sum = sum.doubleValue() + product.totalCost().doubleValue();
    }
    return balance.doubleValue() > sum.doubleValue() + fee.doubleValue();
  }

  public void pay(final VDMSet list, final Number fee) {

    Number sum = 0L;
    for (Iterator iterator_12 = list.iterator(); iterator_12.hasNext(); ) {
      Product product = (Product) iterator_12.next();
      sum = sum.doubleValue() + product.totalCost().doubleValue();
    }
    if (balance.doubleValue() > sum.doubleValue() + fee.doubleValue()) {
      debit(sum.doubleValue() + fee.doubleValue());
    }
  }

  public void refund(final VDMSet list) {

    Number sum = 0L;
    for (Iterator iterator_13 = list.iterator(); iterator_13.hasNext(); ) {
      Product product = (Product) iterator_13.next();
      sum = sum.doubleValue() + product.totalCost().doubleValue();
    }
    credit(sum);
  }

  public void setLocation(final Location loc) {

    location = loc;
  }

  public Boolean textSearch(final String q) {

    String tmp = name;
    Boolean match = false;
    Boolean whileCond_1 = true;
    while (whileCond_1) {
      Boolean andResult_3 = false;

      if (tmp.length() >= q.length()) {
        if (!(match)) {
          andResult_3 = true;
        }
      }

      whileCond_1 = andResult_3;

      if (!(whileCond_1)) {
        break;
      }

      {
        match = true;
        long toVar_1 = q.length();

        for (Long index = 1L; index <= toVar_1; index++) {
          Boolean andResult_4 = false;

          if (match) {
            if (!(Utils.equals(q.charAt(Utils.index(index)), tmp.charAt(Utils.index(index))))) {
              andResult_4 = true;
            }
          }

          if (andResult_4) {
            match = false;
          }
        }
        if (match) {
          return true;

        } else {
          tmp = SeqUtil.tail(tmp);
          match = false;
        }
      }
    }

    return false;
  }

  public Client() {}

  public static Boolean cg_equals(final Client c1, final Client c2) {

    return Utils.equals(c1.id, c2.id);
  }

  public String toString() {

    return "Client{"
        + "name := "
        + Utils.toString(name)
        + ", clientID := "
        + Utils.toString(clientID)
        + ", id := "
        + Utils.toString(id)
        + ", balance := "
        + Utils.toString(balance)
        + ", location := "
        + Utils.toString(location)
        + "}";
  }
}
