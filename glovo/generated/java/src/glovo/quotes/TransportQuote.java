package glovo.quotes;

import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class TransportQuote {
  private static int hc = 0;
  private static TransportQuote instance = null;

  public TransportQuote() {

    if (Utils.equals(hc, 0)) {
      hc = super.hashCode();
    }
  }

  public static TransportQuote getInstance() {

    if (Utils.equals(instance, null)) {
      instance = new TransportQuote();
    }

    return instance;
  }

  public int hashCode() {

    return hc;
  }

  public boolean equals(final Object obj) {

    return obj instanceof TransportQuote;
  }

  public String toString() {

    return "<Transport>";
  }
}
