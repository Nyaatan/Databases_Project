import java.util.*;

public class ShopApp {

    public int shop_id;
    public List<Integer> orders = new ArrayList();
    public WarehouseServer server;
    public List<Map<String, String>> productsAvailable;
    public List<List<Integer>> currOrder = new ArrayList<>();

    public ShopApp(WarehouseServer server, int shop_id){
        this.server = server;
        this.shop_id = shop_id;
        loadOrders(server.getOrdersShopID(this.shop_id));
    }

    private void loadOrders(List<Map<String, String>> ordersShopID) {
        for(Map<String, String> order : ordersShopID) orders.add(Integer.parseInt(order.get("Order_id")));
    }

    public void addToOrder(List<Integer> product){
        currOrder.add(product);
    }

    public void makeOrder(){
        orders.add(server.addOrder(this.shop_id, currOrder));
        currOrder.clear();
    }

    public void cancelOrder(int order_id){
        server.cancelOrder(order_id);
        orders.remove(order_id);
    }

    public List<Map<String, String>> getProducts(){
        productsAvailable = server.getProducts();
        return productsAvailable;
    }

    public Map<Integer,List<Product>> getOrdersDetails(){
        Map<Integer, List<Product>> ordersExtended = new HashMap<>();
        for(Integer orderID : orders) {
            List<Product> products = new ArrayList<>();
            List<Map<String, String>> productsRaw = server.getProductsByOrder(orderID);
            for (Map<String, String> product : productsRaw) {
                products.add(new Product(Integer.parseInt(product.get("Product_id"))
                        , Integer.parseInt(product.get("Count")), product.get("Name")));
            }
            ordersExtended.put(orderID, products);
        }
        return ordersExtended;
    }

    public String getOrderStatus(int orderID){
        return server.getOrderStatus(orderID);
    }
}
