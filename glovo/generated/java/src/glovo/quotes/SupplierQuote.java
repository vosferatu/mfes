package glovo.quotes;

import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class SupplierQuote {
  private static int hc = 0;
  private static SupplierQuote instance = null;

  public SupplierQuote() {

    if (Utils.equals(hc, 0)) {
      hc = super.hashCode();
    }
  }

  public static SupplierQuote getInstance() {

    if (Utils.equals(instance, null)) {
      instance = new SupplierQuote();
    }

    return instance;
  }

  public int hashCode() {

    return hc;
  }

  public boolean equals(final Object obj) {

    return obj instanceof SupplierQuote;
  }

  public String toString() {

    return "<Supplier>";
  }
}
