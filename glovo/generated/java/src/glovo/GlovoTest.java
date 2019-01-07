package glovo;

import java.util.*;
import org.overture.codegen.runtime.*;
import glovo.quotes.*;

@SuppressWarnings("all")
public class GlovoTest extends MyTestCase {
  private Client client1 = new Client("carolina", 0L, 0L);
  private Client client2 = new Client("joao", 5L, 0L);
  private Transport transport1 = new Transport("mota", 0L, 5L);
  private Transport transport2 = new Transport("carro", 5L, -5L);
  private Supplier supplier1 = new Supplier("mac", -5L, 0L);
  private Supplier supplier2 = new Supplier("pizza", -5L, 5L);
  private Product product1 = new Product("burguer", 1L);
  private Product product2 = new Product("pizza", 2L);

  private void testLocationClass() {

    Location loc1 = new Location(0L, 0L);
    Location loc2 = new Location(1L, 0L);
    assertTrue(Utils.equals(loc1.getX(), 0L));
    assertTrue(Utils.equals(loc1.getY(), 0L));
    assertTrue(Utils.equals(loc2.getX(), 1L));
    assertTrue(Utils.equals(loc2.getY(), 0L));
    assertEqual(Location.dist(loc1, loc2), 1L);
  }

  private void testCreateClient() {

    Client client = new Client("x", 0L, 0L);
    assertEqual(client.getName(), "x");
    assertEqual(client.getBalance(), 5L);
    assertEqual(client.location.getX(), 0L);
    assertEqual(client.location.getY(), 0L);
    client.setLocation(new Location(3L, 4L));
    assertEqual(client.location.getX(), 3L);
    assertEqual(client.location.getY(), 4L);
  }

  private void testClientID() {

    Client x = new Client("carolina", 0L, 0L);
    Client y = new Client("joao", 5L, 0L);
    assertTrue(x.getID().longValue() < y.getID().longValue());
    assertTrue(!(Client.cg_equals(x, y)));
  }

  private void testClientBalance() {

    Client client = new Client("carolina", 0L, 0L);
    Product pr = new Product("mota", 1L);
    client.credit(15L);
    assertEqual(client.getBalance(), 20L);
    client.debit(5L);
    assertEqual(client.getBalance(), 15L);
    pr.increaseQuantity(4L);
    assertTrue(client.checkCredit(SetUtil.set(pr), 2L));
    client.pay(SetUtil.set(pr), 2L);
    assertEqual(client.getBalance(), 8L);
    client.refund(SetUtil.set(pr));
    assertEqual(client.getBalance(), 13L);
    client.refund(SetUtil.set(pr));
    assertEqual(client.getBalance(), 18L);
  }

  private void testClientClass() {

    testCreateClient();
    testClientID();
    testClientBalance();
  }

  private void testProductClass() {

    Product pr = new Product("mota", 1L);
    assertTrue(Utils.equals(pr.getName(), "mota"));
    assertEqual(pr.getQuantity(), 1L);
    assertEqual(pr.getPrice(), 1L);
    pr.increaseQuantity(5L);
    assertEqual(pr.getQuantity(), 6L);
    pr.decreaseQuantity(3L);
    assertEqual(pr.getQuantity(), 3L);
    pr.setPrice(5.6);
    assertTrue(Utils.equals(pr.getPrice(), 5.6));
    assertEqual(pr.totalCost(), 5.6 * 3L);
  }

  private void testTransportClass() {

    Transport transport = new Transport("mota", 1L, 1L);
    assertTrue(Utils.equals(transport.getName(), "mota"));
    assertEqual(transport.getLocation().getX(), 1L);
    assertEqual(transport.getLocation().getY(), 1L);
    transport.setLocation(new Location(3L, 4L));
    assertEqual(transport.getLocation().getX(), 3L);
    assertEqual(transport.getLocation().getY(), 4L);
  }

  private void testSupplierClass() {

    Supplier sr = new Supplier("pizza", -5L, 5L);
    Product pr1 = new Product("burguer", 1L);
    Product pr2 = new Product("pizza", 2L);
    assertTrue(Utils.equals(sr.getName(), "pizza"));
    assertEqual(sr.getLocation().getX(), -5L);
    assertEqual(sr.getLocation().getY(), 5L);
    assertEqual(sr.getProducts(), SetUtil.set());
    assertEqual(sr.getProducts(), SetUtil.set());
    sr.addProduct(pr1);
    assertEqual(sr.getProducts(), SetUtil.set(pr1));
    assertEqual(sr.getProducts(), SetUtil.set(pr1));
    sr.addProduct(pr2);
    assertEqual(sr.getProducts(), SetUtil.set(pr1, pr2));
    assertEqual(sr.getProductsByName("burguer"), SetUtil.set(pr1));
    sr.setPrice("burguer", 5L);
    pr1.setPrice(5L);
    assertEqual(sr.getProductsByName("burguer"), SetUtil.set(pr1));
    sr.removeProduct(pr1);
    assertEqual(sr.getProducts(), SetUtil.set(pr2));
    sr.setLocation(new Location(3L, 4L));
    assertEqual(sr.getLocation().getX(), 3L);
    assertEqual(sr.getLocation().getY(), 4L);
  }

  private void testDeliveryClass() {

    Supplier sr = new Supplier("pizza", -5L, 5L);
    Product pr1 = new Product("burguer", 1L);
    Product pr2 = new Product("pizza", 2L);
    Transport tr1 = new Transport("mota", 0L, 5L);
    Delivery deli1 = new Delivery(1L, SetUtil.set(pr1), null, sr);
    Delivery deli2 = new Delivery(1L, SetUtil.set(pr1, pr2), tr1, sr);
    assertEqual(deli1.getSupplier(), sr);
    assertEqual(deli2.getSupplier(), sr);
    assertEqual(deli1.getProducts(), SetUtil.set(pr1));
    assertEqual(deli2.getProducts(), SetUtil.set(pr1, pr2));
    deli1.setProducts(SetUtil.set(pr1, pr2));
    assertEqual(deli1.getProducts(), SetUtil.set(pr1, pr2));
    assertEqual(deli1.getTransport(), null);
    assertEqual(deli2.getTransport(), tr1);
    assertEqual(((Object) deli1.status), glovo.quotes.NullQuote.getInstance());
    assertTrue(!(deli1.doing()));
    assertTrue(deli2.doing());
    deli1.setTransport(tr1);
    assertEqual(deli1.getTransport(), tr1);
    assertEqual(((Object) deli1.status), glovo.quotes.TransportQuote.getInstance());
    assertTrue(deli1.doing());
    assertTrue(deli2.doing());
    deli1.update();
    assertEqual(((Object) deli1.status), glovo.quotes.SupplierQuote.getInstance());
    assertTrue(deli1.doing());
    deli1.update();
    assertEqual(((Object) deli1.status), glovo.quotes.ClientQuote.getInstance());
    assertTrue(!(deli1.doing()));
  }

  public void testTransportGlovo() {

    Glovo glovo = new Glovo();
    Transport a = new Transport("mota", 5L, 0L);
    Transport b = new Transport("bicla", 2L, 0L);
    Supplier sr = new Supplier("pizza", 0L, 0L);
    assertEqual(glovo.getTransports(), SetUtil.set());
    glovo.addTransport(a);
    assertEqual(glovo.getTransports(), SetUtil.set(a));
    glovo.addTransport(b);
    assertEqual(glovo.getTransports(), SetUtil.set(a, b));
    glovo.removeTransport(a);
    assertEqual(glovo.getTransports(), SetUtil.set(b));
    glovo.addTransport(a);
    assertEqual(glovo.chooseBestTransport(sr), a);
  }

  public void testClientGlovo() {

    Glovo glovo = new Glovo();
    Client a = new Client("a", 5L, 0L);
    Client b = new Client("b", 2L, 0L);
    assertEqual(glovo.getClients(), SetUtil.set());
    glovo.addClient(a);
    assertEqual(glovo.getClients(), SetUtil.set(a));
    glovo.addClient(b);
    assertEqual(glovo.getClients(), SetUtil.set(a, b));
    glovo.removeClient(a);
    assertEqual(glovo.getClients(), SetUtil.set(b));
    glovo.addClient(a);
    assertEqual(glovo.getClientByID(1L), null);
    assertEqual(glovo.getClientByID(a.getID()), a);
    assertEqual(glovo.getClientByID(b.getID()), b);
    assertEqual(glovo.getClientByName("a"), SetUtil.set(a));
  }

  public void testSupplierGlovo() {

    Glovo glovo = new Glovo();
    Supplier sr1 = new Supplier("1", 0L, 0L);
    Supplier sr2 = new Supplier("2", 0L, 0L);
    assertEqual(glovo.getSuppliers(), MapUtil.map());
    glovo.addSupplier(sr1);
    assertEqual(glovo.getSuppliers(), MapUtil.map(new Maplet(sr1.getName(), sr1)));
    glovo.addSupplier(sr2);
    assertEqual(
        glovo.getSuppliers(),
        MapUtil.map(new Maplet(sr1.getName(), sr1), new Maplet(sr2.getName(), sr2)));
    assertEqual(glovo.getSupplierByName("1"), sr1);
    assertEqual(glovo.getSupplierByName("2"), sr2);
    glovo.removeSupplier("1");
    assertTrue(!(SetUtil.inSet("1", MapUtil.dom(glovo.getSuppliers()))));
  }

  public void testDeliveryGlovo() {

    Glovo glovo = new Glovo();
    Supplier sr1 = new Supplier("a", 5L, 0L);
    Supplier sr2 = new Supplier("b", 2L, 0L);
    Supplier sr3 = new Supplier("c", 3L, 0L);
    Product pr1 = new Product("burguer", 2L);
    Client cli = new Client("a", 0L, 0L);
    Delivery deli = new Delivery(cli.getID(), SetUtil.set(pr1), null, sr2);
    Transport tr1 = new Transport("mota", 0L, 0L);
    glovo.addClient(cli);
    assertEqual(glovo.getSuppliers(), MapUtil.map());
    glovo.addSupplier(sr1);
    assertEqual(glovo.getSuppliers(), MapUtil.map(new Maplet(sr1.getName(), sr1)));
    glovo.addSupplier(sr2);
    assertEqual(
        glovo.getSuppliers(),
        MapUtil.map(new Maplet(sr1.getName(), sr1), new Maplet(sr2.getName(), sr2)));
    assertEqual(glovo.getSupplierByName("a"), sr1);
    assertEqual(glovo.getSupplierByName("b"), sr2);
    glovo.removeSupplier("a");
    assertTrue(!(SetUtil.inSet("a", MapUtil.dom(glovo.getSuppliers()))));
    assertEqual(glovo.orders, SetUtil.set());
    glovo.addDelivery(deli);
    assertEqual(glovo.getDeliveryToDo(), SetUtil.set(deli));
    assertEqual(glovo.getDeliveryDone(), SetUtil.set());
    assertEqual(glovo.getDeliveryDoing(), SetUtil.set());
    assertEqual(glovo.getClientDeliveries(cli), SetUtil.set(deli));
    for (Iterator iterator_14 = glovo.orders.iterator(); iterator_14.hasNext(); ) {
      Delivery order = (Delivery) iterator_14.next();
      order.setTransport(tr1);
    }
    assertEqual(glovo.getDeliveryDoing(), SetUtil.set(deli));
    for (Iterator iterator_15 = glovo.orders.iterator(); iterator_15.hasNext(); ) {
      Delivery order = (Delivery) iterator_15.next();
      order.update();
    }
    glovo.removeSupplier("b");
    for (Iterator iterator_16 = glovo.orders.iterator(); iterator_16.hasNext(); ) {
      Delivery order = (Delivery) iterator_16.next();
      order.update();
    }
    assertEqual(glovo.getDeliveryDone(), SetUtil.set(deli));
    glovo.addSupplier(sr3);
    assertTrue(SetUtil.inSet("c", MapUtil.dom(glovo.getSuppliers())));
  }

  public void testOrderGlovo() {

    {
      Glovo glovo = new Glovo();
      Supplier sr1 = new Supplier("a", 5L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Client cli = new Client("a", 0L, 0L);
      Transport tr1 = new Transport("mota", 0L, 0L);
      cli.credit(50L);
      glovo.addClient(cli);
      sr1.addProduct(pr1);
      glovo.addSupplier(sr1);
      glovo.addTransport(tr1);
      assertEqual(glovo.getClientDeliveries(cli), SetUtil.set());
      glovo.order(cli, sr1, SetUtil.set(pr1));
      assertTrue(Utils.equals(glovo.getClientDeliveries(cli).size(), 1L));
      assertEqual(glovo.getTransports(), SetUtil.set());
    }

    {
      Glovo glovo = new Glovo();
      Supplier sr1 = new Supplier("a", 5L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Client cli = new Client("a", 0L, 0L);
      Transport tr1 = new Transport("mota", 0L, 0L);
      cli.credit(50L);
      sr1.addProduct(pr1);
      glovo.addClient(cli);
      glovo.addSupplier(sr1);
      glovo.addTransport(tr1);
      assertEqual(glovo.getClientDeliveries(cli), SetUtil.set());
    }

    {
      Glovo glovo = new Glovo();
      Supplier sr1 = new Supplier("a", 5L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Client cli = new Client("a", 0L, 0L);
      cli.credit(50L);
      sr1.addProduct(pr1);
      glovo.addClient(cli);
      glovo.addSupplier(sr1);
      assertEqual(glovo.getClientDeliveries(cli), SetUtil.set());
      glovo.order(cli, sr1, SetUtil.set(pr1));
      assertTrue(Utils.equals(glovo.getClientDeliveries(cli).size(), 1L));
      for (Iterator iterator_17 = glovo.orders.iterator(); iterator_17.hasNext(); ) {
        Delivery order = (Delivery) iterator_17.next();
        glovo.removeOrder(order);
      }
      assertTrue(Utils.equals(glovo.getClientDeliveries(cli).size(), 0L));
    }

    {
      Glovo glovo = new Glovo();
      Supplier sr2 = new Supplier("b", 2L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Product pr2 = new Product("pizza", 20L);
      Client cli = new Client("a", 0L, 0L);
      Delivery deli = new Delivery(cli.getID(), SetUtil.set(pr1), null, sr2);
      cli.credit(50L);
      glovo.addClient(cli);
      glovo.addSupplier(sr2);
      assertEqual(glovo.orders, SetUtil.set());
      glovo.addDelivery(deli);
      assertEqual(glovo.getDeliveryToDo(), SetUtil.set(deli));
      glovo.editOrder(deli, SetUtil.set(pr2));
      assertEqual(deli.getProducts(), SetUtil.set(pr2));
    }

    {
      Glovo glovo = new Glovo();
      Supplier sr2 = new Supplier("b", 2L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Product pr2 = new Product("pizza", 20L);
      Client cli = new Client("a", 0L, 0L);
      Delivery deli = new Delivery(cli.getID(), SetUtil.set(pr1), null, sr2);
      glovo.addClient(cli);
      glovo.addSupplier(sr2);
      assertEqual(glovo.orders, SetUtil.set());
      glovo.addDelivery(deli);
      assertEqual(glovo.getDeliveryToDo(), SetUtil.set(deli));
      glovo.editOrder(deli, SetUtil.set(pr2));
      assertEqual(glovo.orders, SetUtil.set());
    }

    {
      Glovo glovo = new Glovo();
      Supplier sr2 = new Supplier("b", 2L, 0L);
      Product pr1 = new Product("burguer", 2L);
      Client cli = new Client("a", 0L, 0L);
      Transport tr1 = new Transport("mota", 0L, 0L);
      Delivery deli = new Delivery(cli.getID(), SetUtil.set(pr1), tr1, sr2);
      glovo.addClient(cli);
      glovo.addSupplier(sr2);
      sr2.addProduct(pr1);
      glovo.addTransport(tr1);
      assertEqual(glovo.orders, SetUtil.set());
      glovo.addDelivery(deli);
      assertEqual(glovo.getDeliveryDoing(), SetUtil.set(deli));
      glovo.meetSupplier(deli);
      assertEqual(tr1.getLocation(), sr2.getLocation());
      glovo.removeTransport(tr1);
      assertEqual(((Object) deli.status), SupplierQuote.getInstance());
      assertEqual(glovo.getTransports(), SetUtil.set());
      glovo.meetClient(deli);
      assertEqual(cli.location, tr1.getLocation());
      assertEqual(((Object) deli.status), ClientQuote.getInstance());
      assertEqual(glovo.getTransports().size(), 1L);
    }
  }

  public void testGlovoClass() {

    IO.print("testTransportGlovo -> ");
    testTransportGlovo();
    IO.println("Success");
    IO.print("testClientGlovo -> ");
    testClientGlovo();
    IO.println("Success");
    IO.print("testSupplierGlovo -> ");
    testSupplierGlovo();
    IO.println("Success");
    IO.print("testDeliveryGlovo -> ");
    testDeliveryGlovo();
    IO.println("Success");
    IO.print("testOrderGlovo -> ");
    testOrderGlovo();
    IO.println("Success");
  }

  public static void main() {

    GlovoTest glovoTest = new GlovoTest();
    IO.print("testLocationClass -> ");
    glovoTest.testLocationClass();
    IO.println("Success");
    IO.print("testClientClass -> ");
    glovoTest.testClientClass();
    IO.println("Success");
    IO.print("testProductClass -> ");
    glovoTest.testProductClass();
    IO.println("Success");
    IO.print("testTransportClass -> ");
    glovoTest.testTransportClass();
    IO.println("Success");
    IO.print("testSupplierClass -> ");
    glovoTest.testSupplierClass();
    IO.println("Success");
    IO.print("testDeliveryClass -> ");
    glovoTest.testDeliveryClass();
    IO.println("Success");
    IO.print("testGlovoClass -> ");
    glovoTest.testGlovoClass();
    IO.println("Success");
  }

  public GlovoTest() {}

  public String toString() {

    return "GlovoTest{"
        + "client1 := "
        + Utils.toString(client1)
        + ", client2 := "
        + Utils.toString(client2)
        + ", transport1 := "
        + Utils.toString(transport1)
        + ", transport2 := "
        + Utils.toString(transport2)
        + ", supplier1 := "
        + Utils.toString(supplier1)
        + ", supplier2 := "
        + Utils.toString(supplier2)
        + ", product1 := "
        + Utils.toString(product1)
        + ", product2 := "
        + Utils.toString(product2)
        + "}";
  }
}
