import java.util.*;

public class WarehouseServer{
    public DataBaseConnector connector;
    public Map<Integer, EmployeeApp> employeeApps;
    public List<EmployeeApp> workQueue;

    public WarehouseServer(){
        connector = new DataBaseConnector();
        employeeApps = new HashMap<>();
        workQueue = new ArrayList<>();
    }

    public void changeOrderStatus(String status, String orderID){
        connector.fetch(String.format("UPDATE \"WH_Orders\"\n" +
                "SET \"Status\" = '%s'\n" +
                "WHERE \"Order_id\" = %s", status, orderID));
    }

    public void changeOrderStatus(String status, int orderID){
        connector.query(String.format("UPDATE \"WH_Orders\"\n" +
                "SET \"Status\" = '%s'\n" +
                "WHERE \"Order_id\" = %d", status, orderID));
    }

    public void employeeCompleteOrder(EmployeeApp employee) {
        changeOrderStatus("Completed", employee.current_order_id);
        connector.query(String.format("UPDATE \"WH_Employees\"\n" +
                "    SET \"Completed_orders\" = (SELECT \"Completed_orders\" FROM \"WH_Employees\"\n" +
                "    WHERE \"Employee_id\" = %d) + 1\n" +
                "WHERE \"Employee_id\" = %d", employee.employee_id, employee.employee_id));
        workQueue.add(employee);
    }

    public void enlist(EmployeeApp employeeApp) {
        employeeApps.put(employeeApp.employee_id, employeeApp);
        workQueue.add(employeeApp);
    }

    public int addOrder(int shopID, List<List<Integer>> products){
        int orderID = Integer.parseInt(connector.fetch("SELECT MAX(\"Order_id\") FROM \"WH_Orders\"").get(0).get("MAX(\"ORDER_ID\")")) + 1;
        connector.query(String.format("INSERT INTO \"WH_Orders\"\n" +
                "    VALUES (%d, %d, NULL, 'Waiting')", orderID, shopID));
        int orderProdID = Integer.parseInt(connector.fetch("SELECT MAX(\"WH_Ordered_product_id\") FROM \"WH_Ordered_products\"")
                .get(0).get("MAX(\"WH_ORDERED_PRODUCT_ID\")"));
        for(List<Integer> product : products){
            orderProdID += 1;
            int productID = product.get(0);
            int count = product.get(1);
            connector.query(String.format("INSERT INTO \"WH_Ordered_products\" VALUES (%d, %d, %d, %d)",orderProdID, orderID, productID, count));
        }
        return orderID;
    }

    public void employeeGetOrder(int orderID){
        EmployeeApp employee = workQueue.remove(0);
        connector.query(String.format("UPDATE \"WH_Orders\"\n" +
                "SET \"Employee_id\" = '%d'\n" +
                "WHERE \"Order_id\" = %d", employee.employee_id, orderID));
        changeOrderStatus("In progress", orderID);
        employee.getOrder(orderID);
    }

    public void assignOrders(){
        List<Integer> orders = getWaitingOrders();
        while(workQueue.size() > 0 && orders.size() > 0){
            employeeGetOrder(orders.remove(0));
        }
    }

    public List<Integer> getWaitingOrders(){
        List<Map<String, String>> ordersRaw = connector.fetch("SELECT \"Order_id\" FROM \"WH_Orders\"\n" +
                "    WHERE \"Status\" = 'Waiting' ORDER BY \"Order_id\"");
        List<Integer> orders = new ArrayList<>();
        for(Map<String, String> row : ordersRaw){
            orders.add(Integer.parseInt(row.get("Order_id")));
        }
        return orders;
    }

    public void enlistEmployees(){
        List<Map<String, String>> employeesRaw = connector.fetch("SELECT \"Employee_id\" FROM \"WH_Employees\"" );
        for(Map<String, String> row : employeesRaw){
            new EmployeeApp(this, Integer.parseInt(row.get("Employee_id")));
        }
        List<Map<String, String>> pendingOrdersRaw = connector.fetch("SELECT \"Order_id\" " +
                " FROM \"WH_Orders\" WHERE \"Status\" = 'In progress'");
        for(Map<String, String> row : pendingOrdersRaw){
            int orderID = Integer.parseInt(row.get("Order_id"));
            employeeGetOrder(orderID);
        }
    }

    public void addEmployee(String firstName, String lastName, double salary){
        int employeeID = Integer.parseInt(connector.fetch("SELECT MAX(\"Employee_id\") FROM \"WH_Employees\"")
                .get(0).get("MAX(\"EMPLOYEE_ID\")")) + 1;
        connector.query(String.format(Locale.ROOT, "INSERT into \"WH_Employees\"\n" +
                "    values (%d, '%s', '%s', 0, %.2f)", employeeID, firstName, lastName, salary));
        new EmployeeApp(this, employeeID);
    }

    public List<Map<String, String>> getProducts(){
        return connector.fetch("SELECT * FROM \"WH_Products\"");
    }

    public List<Map<String, String>> getEmployees(){
        return connector.fetch("SELECT * FROM \"WH_Employees\"");
    }

    public List<Map<String, String>> getOrders(){
        return connector.fetch("SELECT * FROM \"WH_Orders\"");
    }

    public List<Map<String, String>> getShops(){
        return connector.fetch("SELECT * FROM \"WH_Products\"");
    }

    public List<Map<String, String>> getOrderedProducts(){
        return connector.fetch("SELECT * FROM \"WH_Products\"");
    }

    public List<Map<String, String>> searchProductName(String name){
        return connector.fetch(String.format("SELECT * FROM \"WH_Products\" WHERE \"Name\" LIKE '%s%'", name));
    }

    public List<Map<String, String>> searchProductID(String id){
        return connector.fetch(String.format("SELECT * FROM \"WH_Products\" WHERE \"Product_id\" LIKE '%s%'", id));
    }

    public List<Map<String, String>> searchEmployeeID(String id){
        return connector.fetch(String.format("SELECT * FROM \"WH_Employees\" WHERE \"Employee_id\" LIKE '%s%'", id));
    }

    public List<Map<String, String>> searchEmployeeName(String firstName, String lastName){
        return connector.fetch(String.format("SELECT * FROM \"WH_Employees\" WHERE \"First_name\" LIKE '%s%' AND " +
                "\"Last_name\" LIKE '%s%'", firstName, lastName));
    }

    public List<Map<String, String>> searchOrderID(String id){
        return connector.fetch(String.format("SELECT * FROM \"WH_Orders\" WHERE \"Order_id\" = '%s'", id));
    }

    public List<Map<String, String>> searchOrderShopID(String id){
        return connector.fetch(String.format("SELECT * FROM \"WH_Orders\" WHERE \"Shop_id\" = '%s'", id));
    }

    public List<Map<String, String>> searchOrderShopName(String name){
        return connector.fetch(String.format("SELECT * FROM \"WH_Orders\"\n" +
                "JOIN \"WH_Shops\" ON \"WH_Orders\".\"Shop_id\" = \"WH_Shops\".\"Shop_id\"\n" +
                "WHERE \"WH_Shops\".\"Name\" = '%s'", name));
    }

    public String getOrderStatus(int orderID){
        return connector.fetch(String.format("SELECT \"Status\" FROM \"WH_Orders\" WHERE \"Order_id\" = '%s'", orderID)).
                get(0).get("STATUS");
    }

    public void cancelOrder(int orderID){
        changeOrderStatus("Canceled", orderID);
        int employeeID = Integer.parseInt(connector.fetch(String.format(
                "SELECT \"Employee_id\" FROM \"WH_Orders\" WHERE \"Order_id\" = %d", orderID)).get(0).get("EMPLOYEE_ID"));
        employeeApps.get(employeeID).is_working = false;
        employeeApps.get(employeeID).current_order_id = 0;
    }

    public List<Map<String, String>> getProductsByOrder(int id) {
        return connector.fetch(String.format("SELECT \"Product_id\", \"Count\" FROM \"WH_Ordered_products\" WHERE \"Order_id\" = %d", id));
    }

    public void addShop(String name, String address){
        int shopID = Integer.parseInt(connector.fetch("SELECT MAX(\"Shop_id\") FROM \"WH_Shops\"")
                .get(0).get("MAX(\"SHOP_ID\")")) + 1;
        connector.query(String.format("INSERT INTO \"WH_Shops\"\n" +
                "VALUES (%d, %s, %s)", shopID, name, address));
    }

    public void removeShop(int shopID){
        connector.query(String.format("DELETE FROM \"WH_Shops\"\n" +
                "    WHERE \"Shop_id\" = %d", shopID));
    }

    public void removeEmployee(int employeeID){
        connector.query(String.format("DELETE FROM \"WH_Employees\"\n" +
                "    WHERE \"Employee_id\" = %d", employeeID));
        if(employeeApps.get(employeeID).is_working) changeOrderStatus("Waiting", employeeApps.get(employeeID).current_order_id);
        employeeApps.remove(employeeID);
    }

    public void updateEmployee(int employeeID, Map<String, String> kwargs){
        StringBuilder query = new StringBuilder("UPDATE \"WH_Employees\"\n" +
                "SET ");
        for(String arg : kwargs.keySet()){
            query.append("\"").append(arg).append("\" = ").append(kwargs.get(arg)).append(",");
        }
        String sql = query.toString().substring(0, query.lastIndexOf(",")) + "WHERE \"Employee_id\" = " + employeeID;
        connector.query(sql);
    }

    public void updateShop(int shopID, Map<String, String> kwargs){
        StringBuilder query = new StringBuilder("UPDATE \"WH_Shops\"\n" +
                "SET ");
        for(String arg : kwargs.keySet()){
            query.append("\"").append(arg).append("\" = ").append(kwargs.get(arg)).append(",");
        }
        String sql = query.toString().substring(0, query.lastIndexOf(",")) + "WHERE \"Shop_id\" = " + shopID;
        connector.query(sql);
    }

    public void updateShop(String name, Map<String, String> kwargs){
        StringBuilder query = new StringBuilder("UPDATE \"WH_Shops\"\n" +
                "SET ");
        for(String arg : kwargs.keySet()){
            query.append("\"").append(arg).append("\" = ").append(kwargs.get(arg)).append(",");
        }
        String sql = query.toString().substring(0, query.lastIndexOf(",")) + "WHERE \"Name\" = " + name;
        connector.query(sql);
    }

    public void updateProduct(int productID, Map<String, String> kwargs){
        StringBuilder query = new StringBuilder("UPDATE \"WH_Products\"\n" +
                "SET ");
        for(String arg : kwargs.keySet()){
            query.append("\"").append(arg).append("\" = ").append(kwargs.get(arg)).append(",");
        }
        String sql = query.toString().substring(0, query.lastIndexOf(",")) + "WHERE \"Product_id\" = " + productID;
        connector.query(sql);
    }

    public void updateProduct(String name, Map<String, String> kwargs){
        StringBuilder query = new StringBuilder("UPDATE \"WH_Products\"\n" +
                "SET ");
        for(String arg : kwargs.keySet()){
            query.append("\"").append(arg).append("\" = ").append(kwargs.get(arg)).append(",");
        }
        String sql = query.toString().substring(0, query.lastIndexOf(",")) + "WHERE \"Name\" = '" + name + "'";
        connector.query(sql);
    }


    public void addProduct(String name, int count){
        int productID = Integer.parseInt(connector.fetch("SELECT MAX(\"Product_id\") FROM \"WH_Product\"")
                .get(0).get("MAX(\"PRODUCT_ID\")")) + 1;
        connector.query(String.format("INSERT INTO \"WH_Products\"\n" +
                "VALUES (%d, %s, %d)", productID, name, count));
    }

    public void removeProduct(int productID){
        connector.query(String.format("DELETE FROM \"WH_Products\"\n" +
                "    WHERE \"Product_id\" = %d", productID));
    }
}
