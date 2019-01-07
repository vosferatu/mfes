package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Supplier {
  private VDMSet products = SetUtil.set();
  private String name;
  private Location location;

  public void cg_init_Supplier_1(final String supplierName, final Number x, final Number y) {

    name = supplierName;
    products = SetUtil.set();
    location = new Location(x, y);
    return;
  }

  public Supplier(final String supplierName, final Number x, final Number y) {

    cg_init_Supplier_1(supplierName, x, y);
  }

  public String getName() {

    return name;
  }

  public VDMSet getProducts() {

    return Utils.copy(products);
  }

  public void addProduct(final Product product) {

    products = SetUtil.union(Utils.copy(products), SetUtil.set(product));
  }

  public void removeProduct(final Product product) {

    products = SetUtil.diff(Utils.copy(products), SetUtil.set(product));
  }

  public VDMSet getProductsByName(final String productName) {

    VDMSet setCompResult_1 = SetUtil.set();
    VDMSet set_1 = Utils.copy(products);
    for (Iterator iterator_1 = set_1.iterator(); iterator_1.hasNext(); ) {
      Product product = ((Product) iterator_1.next());
      if (Utils.equals(product.name, productName)) {
        setCompResult_1.add(product);
      }
    }
    return Utils.copy(setCompResult_1);
  }

  public void setPrice(final String productName, final Number price) {

    for (Iterator iterator_18 = getProductsByName(productName).iterator();
        iterator_18.hasNext();
        ) {
      Product product = (Product) iterator_18.next();
      product.setPrice(price);
    }
  }

  public Location getLocation() {

    return location;
  }

  public void setLocation(final Location loc) {

    location = loc;
  }

  public Supplier() {}

  public String toString() {

    return "Supplier{"
        + "products := "
        + Utils.toString(products)
        + ", name := "
        + Utils.toString(name)
        + ", location := "
        + Utils.toString(location)
        + "}";
  }
}
