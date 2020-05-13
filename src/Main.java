import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        WarehouseServer server = new WarehouseServer();
//        server.addEmployee("Kacper", "Bojarczuk", 3200);
//        server.addOrder(2);
//        server.addOrder(1);
//        server.addOrder(3);
//        server.addOrder(4);
//        server.addOrder(5);
//        List<List<Integer>> order = new ArrayList<>();
//        List<Integer> order_det = new ArrayList<>();
//        order_det.add(1);
//        order_det.add(2);
//        order.add(order_det);
//        order_det.clear();
//        order_det.add(2);
//        order_det.add(1);
//        order.add(order_det);
//        server.addOrder(3, order);
//        server.enlistEmployees();
//        server.assignOrders();
//        server.employeeApps.get(2).completeOrder();
//        server.assignOrders();
        server.resetOrders();
//        server.removeShop(2);
//        server.removeEmployee(3);
//        Map<String, String> arg =  new HashMap<>();
//        arg.put("First_name", "'Gonzales'");
//        arg.put("Salary", "7000");
//        server.updateEmployee(1,arg);
    }

    public static void print(Object arg){
        System.out.println(arg);
    }
}
