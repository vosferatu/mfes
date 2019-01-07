package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Glovo {
  private static final Number FEE = 1.9;
  public VDMSet clients = SetUtil.set();
  public VDMSet orders = SetUtil.set();
  public VDMMap suppliers = MapUtil.map();
  public VDMSet transports = SetUtil.set();

  public void cg_init_Glovo_1() {

    return;
  }

  public Glovo() {

    cg_init_Glovo_1();
  }

  public void addTransport(final Transport transport) {

    transports = SetUtil.union(Utils.copy(transports), SetUtil.set(transport));
  }

  public void removeTransport(final Transport tr) {

    transports = SetUtil.diff(Utils.copy(transports), SetUtil.set(tr));
  }

  public VDMSet getTransports() {

    return Utils.copy(transports);
  }

  public Transport chooseBestTransport(final Supplier supplier) {

    Transport best = null;
    for (Iterator iterator_19 = transports.iterator(); iterator_19.hasNext(); ) {
      Transport transport = (Transport) iterator_19.next();
      Boolean orResult_2 = false;

      if (Utils.equals(best, null)) {
        orResult_2 = true;
      } else {
        orResult_2 =
            Location.dist(supplier.getLocation(), transport.getLocation()).doubleValue()
                > Location.dist(supplier.getLocation(), best.getLocation()).doubleValue();
      }

      if (orResult_2) {
        best = transport;
      }
    }
    return best;
  }

  public VDMSet getClients() {

    return Utils.copy(clients);
  }

  public void addClient(final Client c) {

    clients = SetUtil.union(Utils.copy(clients), SetUtil.set(c));
  }

  public void removeClient(final Client c) {

    for (Iterator iterator_20 = clients.iterator(); iterator_20.hasNext(); ) {
      Client client = (Client) iterator_20.next();
      if (Client.cg_equals(client, c)) {
        clients = SetUtil.diff(Utils.copy(clients), SetUtil.set(client));
      }
    }
  }

  public Client getClientByID(final Number id) {

    Client target = null;
    for (Iterator iterator_21 = clients.iterator(); iterator_21.hasNext(); ) {
      Client client = (Client) iterator_21.next();
      if (Utils.equals(client.id, id)) {
        target = client;
      }
    }
    return target;
  }

  public VDMSet getClientByName(final String name) {

    VDMSet setCompResult_2 = SetUtil.set();
    VDMSet set_5 = Utils.copy(clients);
    for (Iterator iterator_5 = set_5.iterator(); iterator_5.hasNext(); ) {
      Client client = ((Client) iterator_5.next());
      if (client.textSearch(name)) {
        setCompResult_2.add(client);
      }
    }
    return Utils.copy(setCompResult_2);
  }

  public VDMMap getSuppliers() {

    return Utils.copy(suppliers);
  }

  public void addSupplier(final Supplier supplier) {

    suppliers =
        MapUtil.munion(
            Utils.copy(suppliers), MapUtil.map(new Maplet(supplier.getName(), supplier)));
  }

  public Supplier getSupplierByName(final String name) {

    return ((Supplier) Utils.get(suppliers, name));
  }

  public void removeSupplier(final String name) {

    Boolean result = false;
    for (Iterator iterator_22 = orders.iterator(); iterator_22.hasNext(); ) {
      Delivery order = (Delivery) iterator_22.next();
      Boolean andResult_6 = false;

      if (Utils.equals(order.supplier.getName(), name)) {
        if (!(Utils.equals(order.status, glovo.quotes.ClientQuote.getInstance()))) {
          andResult_6 = true;
        }
      }

      if (andResult_6) {
        result = true;
      }
    }
    if (!(result)) {
      suppliers = MapUtil.domResBy(SetUtil.set(name), Utils.copy(suppliers));
    }
  }

  public void addDelivery(final Delivery delivery) {

    orders = SetUtil.union(Utils.copy(orders), SetUtil.set(delivery));
  }

  public VDMSet getDeliveryToDo() {

    VDMSet setCompResult_3 = SetUtil.set();
    VDMSet set_6 = Utils.copy(orders);
    for (Iterator iterator_6 = set_6.iterator(); iterator_6.hasNext(); ) {
      Delivery order = ((Delivery) iterator_6.next());
      if (Utils.equals(order.status, glovo.quotes.NullQuote.getInstance())) {
        setCompResult_3.add(order);
      }
    }
    return Utils.copy(setCompResult_3);
  }

  public VDMSet getDeliveryDone() {

    VDMSet setCompResult_4 = SetUtil.set();
    VDMSet set_7 = Utils.copy(orders);
    for (Iterator iterator_7 = set_7.iterator(); iterator_7.hasNext(); ) {
      Delivery order = ((Delivery) iterator_7.next());
      if (Utils.equals(order.status, glovo.quotes.ClientQuote.getInstance())) {
        setCompResult_4.add(order);
      }
    }
    return Utils.copy(setCompResult_4);
  }

  public VDMSet getDeliveryDoing() {

    VDMSet setCompResult_5 = SetUtil.set();
    VDMSet set_8 = Utils.copy(orders);
    for (Iterator iterator_8 = set_8.iterator(); iterator_8.hasNext(); ) {
      Delivery order = ((Delivery) iterator_8.next());
      if (order.doing()) {
        setCompResult_5.add(order);
      }
    }
    return Utils.copy(setCompResult_5);
  }

  public VDMSet getClientDeliveries(final Client c) {

    VDMSet result = SetUtil.set();
    for (Iterator iterator_23 = orders.iterator(); iterator_23.hasNext(); ) {
      Delivery order = (Delivery) iterator_23.next();
      if (Utils.equals(order.userID, c.id)) {
        result = SetUtil.union(Utils.copy(result), SetUtil.set(order));
      }
    }
    return Utils.copy(result);
  }

  public Boolean checkSupplierProducts(final Supplier sup, final VDMSet prods) {

    for (Iterator iterator_24 = prods.iterator(); iterator_24.hasNext(); ) {
      Product product = (Product) iterator_24.next();
      if (!(SetUtil.inSet(product, sup.getProducts()))) {
        return false;
      }
    }
    return true;
  }

  public void order(final Client client, final Supplier supplier, final VDMSet products) {

    Transport transport = chooseBestTransport(supplier);
    if (client.checkCredit(Utils.copy(products), Glovo.FEE)) {
      client.pay(Utils.copy(products), Glovo.FEE);
    }

    addDelivery(new Delivery(client.id, Utils.copy(products), transport, supplier));
    if (!(Utils.equals(transport, null))) {
      removeTransport(transport);
    }
  }

  public void removeOrder(final Delivery deli) {

    orders = SetUtil.diff(Utils.copy(orders), SetUtil.set(deli));
    getClientByID(deli.userID).refund(deli.getProducts());
  }

  public void editOrder(final Delivery deli, final VDMSet prod) {

    getClientByID(deli.userID).refund(deli.getProducts());
    if (getClientByID(deli.userID).checkCredit(Utils.copy(prod), Glovo.FEE)) {
      getClientByID(deli.userID).pay(Utils.copy(prod), Glovo.FEE);
      deli.setProducts(Utils.copy(prod));

    } else {
      removeOrder(deli);
    }
  }

  public void meetSupplier(final Delivery deli) {

    deli.transport.setLocation(deli.supplier.getLocation());
    deli.update();
  }

  public void meetClient(final Delivery deli) {

    deli.transport.setLocation(getClientByID(deli.userID).location);
    deli.update();
    addTransport(deli.transport);
  }

  public String toString() {

    return "Glovo{"
        + "FEE = "
        + Utils.toString(FEE)
        + ", clients := "
        + Utils.toString(clients)
        + ", orders := "
        + Utils.toString(orders)
        + ", suppliers := "
        + Utils.toString(suppliers)
        + ", transports := "
        + Utils.toString(transports)
        + "}";
  }
}
