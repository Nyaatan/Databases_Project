
public class AdminApp {
        WarehouseServer server = new WarehouseServer();

        public void addEmployee(String firstName, String lastName, double salary){
            server.addEmployee(firstName, lastName, salary);
        }
        public void deleteEmployee(int employeeID){
            server.removeEmployee(employeeID);
        }

        public void changeEmployeeFirstName(int ID, String name){
            server.changeEmployeeFirstName(ID, name);
        }

        public void changeEmployeeLastName(int ID, String lastName){
            server.chamgeEmployeeLastName(ID, lastName);
        }

        public void changeEmployeeSalary(int ID, double salary){
            server.changeEmployeeSalary(ID, salary);
        }

        public void updateEmployee(int ID, String firstName, String lastName, double salary){
            server.changeEmployeeSalary(ID, salary);
            server.chamgeEmployeeLastName(ID, lastName);
            server.chamgeEmployeeLastName(ID, lastName);
        }

        public void addProduct(String name, int amount){
            server.addProduct(name, amount);
        }

        public void changeProductName(int ID, String name){
            server.changeProductName(ID, name);
        }
        public void changeProductName(String oldName, String newName){
            server.changeProductName(oldName, newName);
        }

        public void changeProductCount(int ID, int count){
            server.changeProductCount(ID, count);
        }
        public void changeProductCount(String name, int count){
           server.changeProductCount(name, count);
        }
        public void updateProduct(String oldName, String newName, int count){
            this.changeProductName(oldName, newName);
            this.changeProductCount(oldName,count);
        }
        public void updateProduct(int ID, String name, int count){
            this.changeProductName(ID, name);
            this.changeProductCount(ID,count);
        }

        public void addShop(String name, String address){
            server.addShop(name, address);
        }

        public void changeShopName(int ID, String name){
            server.changeShopName(ID, name);
        }
        public void changeShopName(String oldName, String newName){
            server.changeShopName(oldName, newName);
        }

        public void changeShopAddress(int ID, String address){
            server.changeShopAdress(ID, address);
        }
        public void changeShopAddress(String namme, String address){
            server.changeShopAdress(name, address);
        }
        public void updateShop(int ID, String name, String address){
            this.changeShopAddress(ID, address);
            this.changeShopName(ID, name);
        }
        public void updateShop(String oldName, String newName, String address){
            this.changeShopAddress(oldName, address);
            this.changeShopName(oldName, newName);
        }

        public void cancelOrder(int ID){
            server.deleteOrder(ID);
        }

        public


}
