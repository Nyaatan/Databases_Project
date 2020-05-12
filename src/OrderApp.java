import java.util.ArrayList;
import java.util.List;

public class OrderApp {

    public List<List<Integer>> list = new ArrayList<>(); //lista produktów
    public int order_id;
    public int shop_id;
    public int employee_id;
    public boolean completed = false;
    public WarehouseServer server;

    public OrderApp(List<List<Integer>> productsList){
        this.server = server;
        this.order_id = order_id;
        this.list = productsList;
    }

}
