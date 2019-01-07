package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Delivery {
  public Number userID;
  public Supplier supplier;
  private VDMSet products = SetUtil.set();
  public Transport transport;
  public Object status;

  public void cg_init_Delivery_1(
      final Number id, final VDMSet p, final Transport t, final Supplier s) {

    userID = id;
    products = Utils.copy(p);
    supplier = s;
    transport = t;
    status = glovo.quotes.NullQuote.getInstance();
    if (!(Utils.equals(t, null))) {
      status = glovo.quotes.TransportQuote.getInstance();
    }

    return;
  }

  public Delivery(final Number id, final VDMSet p, final Transport t, final Supplier s) {

    cg_init_Delivery_1(id, Utils.copy(p), t, s);
  }

  public VDMSet getProducts() {

    return Utils.copy(products);
  }

  public void setProducts(final VDMSet prod) {

    products = Utils.copy(prod);
  }

  public Transport getTransport() {

    return transport;
  }

  public Supplier getSupplier() {

    return supplier;
  }

  public void setTransport(final Transport t) {

    transport = t;
    status = glovo.quotes.TransportQuote.getInstance();
  }

  public Boolean doing() {

    Boolean orResult_1 = false;

    if (Utils.equals(status, glovo.quotes.SupplierQuote.getInstance())) {
      orResult_1 = true;
    } else {
      orResult_1 = Utils.equals(status, glovo.quotes.TransportQuote.getInstance());
    }

    return orResult_1;
  }

  public void update() {

    Object quotePattern_1 = status;
    Boolean success_1 = Utils.equals(quotePattern_1, glovo.quotes.TransportQuote.getInstance());

    if (!(success_1)) {
      Object quotePattern_2 = status;
      success_1 = Utils.equals(quotePattern_2, glovo.quotes.SupplierQuote.getInstance());

      if (success_1) {
        status = glovo.quotes.ClientQuote.getInstance();
      }

    } else {
      status = glovo.quotes.SupplierQuote.getInstance();
    }
  }

  public Delivery() {}

  public String toString() {

    return "Delivery{"
        + "userID := "
        + Utils.toString(userID)
        + ", supplier := "
        + Utils.toString(supplier)
        + ", products := "
        + Utils.toString(products)
        + ", transport := "
        + Utils.toString(transport)
        + ", status := "
        + Utils.toString(status)
        + "}";
  }
}
