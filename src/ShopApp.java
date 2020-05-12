import java.util.ArrayList;
import java.util.List;

public class ShopApp {

    public int shop_id;
    public List<OrderApp> orders = new ArrayList();
    public WarehouseServer server;

    public ShopApp(WarehouseServer server, int shop_id){
        this.server = server;
        this.shop_id = shop_id;
        //server.enlist(this);
    }




    public void newOrder(List<List<Integer>> productsList){
        OrderApp order = new OrderApp(productsList);
        server.addOrder(this.shop_id, productsList);
        orders.add(order);
    }

    public void cancelOrder(int order_id){
        //server.cancelOrder(order_id);
        //orders.
    }

    public List<ProductApp> makeOrder(){
        List<ProductApp> orderList = new ArrayList();
        return orderList;
    };
}
