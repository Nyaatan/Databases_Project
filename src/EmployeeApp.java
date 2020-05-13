import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeApp {
    public int current_order_id;
    public int employee_id;
    public boolean is_working = false;
    public List<Map<String,String>> orderProducts;
    public WarehouseServer server;

    public EmployeeApp(WarehouseServer server, int employee_id){
        this.server = server;
        this.employee_id = employee_id;
        server.enlist(this);
    }

    public void completeOrder(){
        is_working = false;
        orderProducts = new ArrayList<>();
        server.employeeCompleteOrder(this);
    }

    public void getOrder(int id){
        this.current_order_id = id;
        is_working = true;
        orderProducts = server.getProductsByOrder(id);
    }

    public String toString(){
        return String.format("Employee: %d\nIs working: %b\nOrder: %d\n", employee_id, is_working, current_order_id);
    }
}
