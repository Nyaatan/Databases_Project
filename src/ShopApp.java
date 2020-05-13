import java.util.*;

public class ShopApp {

    public int shop_id;
    public List<Integer> orders = new ArrayList();
    public WarehouseServer server;
    public List<Map<String, String>> productsAvailable;

    public ShopApp(WarehouseServer server, int shop_id){
        this.server = server;
        this.shop_id = shop_id;
    }

    public void makeOrder(List<List<Integer>> productsList){
        orders.add(server.addOrder(this.shop_id, productsList));
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
                        , Integer.parseInt(product.get("Count"))));
            }
            ordersExtended.put(orderID, products);
        }
        return ordersExtended;
    }

    public static String[] getShopNames(WarehouseServer server){
        List<String> shopNames = new ArrayList<>();
        List<Map<String, String>> shops = server.getShops();
        for(Map<String, String> shop : shops){
            shopNames.add(shop.get("Name"));
        }
        return (String[]) shopNames.toArray();
    }
}
