import java.util.HashMap;
import java.util.Map;

public class AdminApp {
        WarehouseServer server;

        public AdminApp(WarehouseServer server){
            this.server = server;
        }

        public void addEmployee(String firstName, String lastName, double salary){
            server.addEmployee(firstName, lastName, salary);
        }
        public void deleteEmployee(int employeeID){
            server.removeEmployee(employeeID);
        }

        public void updateEmployee(int ID, Map<String, String> args){
            /*Map<String, String> args = new HashMap<>();
            args.put("Employee_id", Integer.toString(ID));
            args.put("First_name", String.format("'%s'", firstName));
            args.put("Last_name", String.format("'%s'", lastName));
            args.put("Salary", Double.toString(salary));*/
            server.updateEmployee(ID, args);
        }

        public void addProduct(String name, int amount){
            server.addProduct(name, amount);
        }

        public void updateProduct(String name, Map<String, String> args){
//            Map<String, String> args = new HashMap<>();
//            args.put("Count", Integer.toString(count));
//            args.put("Name", String.format("'%s'", newName));
            server.updateProduct(name, args);
        }
        public void updateProduct(int ID, Map<String, String> args){
//            Map<String, String> args = new HashMap<>();
//            args.put("Count", Integer.toString(count));
//            args.put("Name", String.format("'%s'", name));
            server.updateProduct(ID, args);
        }

        public void addShop(String name, String address){
            server.addShop(name, address);
        }

        public void updateShop(int ID, Map<String, String> args){
            server.updateShop(ID, args);
        }
        public void updateShop(String name, Map<String, String> args){
            server.updateShop(name, args);
        }

        public void cancelOrder(int ID){
            server.cancelOrder(ID);
        }

        public void removeProduct(int ID){
            server.removeProduct(ID);
        }

        public void removeShop(int ID){
            server.removeShop(ID);
        }
}
