package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Product {
  public String name;
  private Number price;
  private Number quantity = 0L;

  public void cg_init_Product_1(final String product_name, final Number product_price) {

    name = product_name;
    price = product_price;
    quantity = 1L;
    return;
  }

  public Product(final String product_name, final Number product_price) {

    cg_init_Product_1(product_name, product_price);
  }

  public String getName() {

    return name;
  }

  public Number getPrice() {

    return price;
  }

  public Number getQuantity() {

    return quantity;
  }

  public void increaseQuantity(final Number amount) {

    quantity = quantity.longValue() + amount.longValue();
  }

  public void decreaseQuantity(final Number amount) {

    quantity = quantity.longValue() - amount.longValue();
  }

  public void setPrice(final Number newPrice) {

    price = newPrice;
  }

  public Number totalCost() {

    return quantity.longValue() * price.doubleValue();
  }

  public Product() {}

  public String toString() {

    return "Product{"
        + "name := "
        + Utils.toString(name)
        + ", price := "
        + Utils.toString(price)
        + ", quantity := "
        + Utils.toString(quantity)
        + "}";
  }
}
