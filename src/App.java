import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.swing.*;

public class App extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JMenuBar shopMenuBar;
    private JMenuBar employeeMenuBar;
    private JMenuBar adminMenuBar;
    private JMenu shopOrdersMenu;
    private JMenu adminOrdersMenu;
    private JMenu adminEmployeesMenu;
    private JMenu adminShopsMenu;
    private JMenu shopModeMenu;
    private JMenu employeeModeMenu;
    private JMenu adminModeMenu;
    private JMenuItem shopOrderListMenu;
    private JMenuItem adminOrderListMenu;
    private JMenuItem shopNewOrderMenu;
    private JMenuItem shopToEmployeeMode;
    private JMenuItem shopToAdminMode;
    private JMenuItem employeeToShopMode;
    private JMenuItem employeeToAdminMode;
    private JMenuItem adminToShopMode;
    private JMenuItem adminToEmployeeMode;
    private JMenuItem adminEmployeeListMenu;
    private JMenuItem adminShopListMenu;
    private JPanel shopOrderListPanel;
    private JPanel shopNewOrderPanel;
    private JPanel adminOrderListPanel;
    private JPanel adminEmployeeListPanel;
    private JPanel adminShopListPanel;
    private JPanel employeePanel;

    private EmployeeApp employee;
    private ShopApp shop;
    private AdminApp admin;
    private WarehouseServer server;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.createMenus();

        this.shopOrderListPanel = new JPanel();
        this.shopOrderListPanel.add(new JLabel("Lista zamówień"));
        this.shopNewOrderPanel = new JPanel();
        this.shopNewOrderPanel.add(new JLabel("Nowe zamówienie"));
        this.employeePanel = new JPanel();
        this.employeePanel.add(new JLabel("Obsługiwane zamówienie"));
        this.adminOrderListPanel = new JPanel();
        this.adminOrderListPanel.add(new JLabel("Lista zamówień"));
        this.adminEmployeeListPanel = new JPanel();
        this.adminEmployeeListPanel.add(new JLabel("Lista pracowników"));
        this.adminShopListPanel = new JPanel();
        this.adminShopListPanel.add(new JLabel("Lista sklepów"));

        this.shop = null;
        this.employee = null;
        this.server = new WarehouseServer();
        this.admin = new AdminApp(this.server);

        String[] choices = { "sklep", "pracownik", "admin" };
        String input = (String) JOptionPane.showInputDialog(this, "Kim jesteś:",
                "Wybór roli", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        if(input == choices[0]) {
            this.runShopMode();
        } else if (input == choices[1]){
            this.runEmployeeMode();
        } else if (input == choices[2]) {
            this.runAdminMode();
        }
    }

    private void runShopMode() {
        if(this.shop == null) {
            List<String> choices = new ArrayList<String>();
            List<Integer> ids = new ArrayList<Integer>();

            List<Map<String, String>> shops = this.admin.getShops();

            for(int i = 0; i < shops.size(); i++) {
                choices.add(shops.get(i).get("Name"));
                ids.add(Integer.parseInt(shops.get(i).get("Shop_id")));
            }
            String[] ch_array = new String[choices.size()];
            choices.toArray(ch_array);

            String input = (String) JOptionPane.showInputDialog(this, "Wybierz swoją firmę:",
                    "Wybór sklepu", JOptionPane.QUESTION_MESSAGE, null,
                    ch_array,
                    ch_array[0]);

            for(int i = 0; i < choices.size(); i++) {
                if(choices.get(i) == input) this.shop = new ShopApp(this.server, ids.get(i));
            }
        }
        this.setTitle("Obsługa sklepu id: " + Integer.toString(this.shop.shop_id));
        this.setJMenuBar(this.shopMenuBar);
        this.displayShopNewOrder();
        this.setVisible(true);
    }

    private void runEmployeeMode() {
        if(this.employee == null) {
            List<String> choices = new ArrayList<String>();
            List<Integer> ids = new ArrayList<Integer>();

            List<Map<String, String>> employees = this.admin.getEmployees();

            for(int i = 0; i < employees.size(); i++) {
                choices.add(employees.get(i).get("First_name") + " " + employees.get(i).get("Last_name"));
                ids.add(Integer.parseInt(employees.get(i).get("Employee_id")));
            }
            String[] ch_array = new String[choices.size()];
            choices.toArray(ch_array);

            String input = (String) JOptionPane.showInputDialog(null, "Wybierz siebie z listy:",
                    "Wybór pracownika", JOptionPane.QUESTION_MESSAGE, null,
                    ch_array,
                    ch_array[0]);

            for(int i = 0; i < choices.size(); i++) {
                if(choices.get(i) == input) this.employee = new EmployeeApp(this.server, ids.get(i));
            }
        }
        setTitle("Obsługa pracownika id: " + Integer.toString(this.employee.employee_id));
        this.setJMenuBar(this.employeeMenuBar);
        this.displayEmployee();
        setVisible(true);
    }

    private void runAdminMode() {
        setTitle("Obsługa admina");
        this.setJMenuBar(this.adminMenuBar);
        this.displayAdminOrderList();
        setVisible(true);
    }

    private void createMenus() {
        String changeMode = "Zmień tryb";
        String orders = "Zamówienie";
        String shop = "Sklep";
        String employee = "Pracownik";
        String admin = "Admin";
        String orderList = "Lista zamówień";

        this.shopMenuBar = new JMenuBar();
        this.employeeMenuBar = new JMenuBar();
        this.adminMenuBar = new JMenuBar();

        this.shopModeMenu = new JMenu(changeMode);
        this.employeeModeMenu = new JMenu(changeMode);
        this.adminModeMenu = new JMenu(changeMode);
        this.shopOrdersMenu = new JMenu(orders);
        this.adminOrdersMenu = new JMenu(orders);
        this.adminEmployeesMenu = new JMenu(employee);
        this.adminShopsMenu = new JMenu(shop);


        this.shopToEmployeeMode = new JMenuItem(employee);
        this.shopToAdminMode = new JMenuItem(admin);

        this.employeeToShopMode = new JMenuItem(shop);
        this.employeeToAdminMode = new JMenuItem(admin);

        this.adminToShopMode = new JMenuItem(shop);
        this.adminToEmployeeMode = new JMenuItem(employee);

        this.shopNewOrderMenu = new JMenuItem("Nowe zamówienie");
        this.shopOrderListMenu = new JMenuItem(orderList);
        this.adminOrderListMenu = new JMenuItem(orderList);

        this.adminShopListMenu = new JMenuItem("Lista sklepów");
        this.adminEmployeeListMenu = new JMenuItem("Lista pracowników");

        this.shopToEmployeeMode.addActionListener(this);
        this.shopToAdminMode.addActionListener(this);
        this.employeeToShopMode.addActionListener(this);
        this.employeeToAdminMode.addActionListener(this);
        this.adminToShopMode.addActionListener(this);
        this.adminToEmployeeMode.addActionListener(this);
        this.shopNewOrderMenu.addActionListener(this);
        this.shopOrderListMenu.addActionListener(this);
        this.adminOrderListMenu.addActionListener(this);
        this.adminShopListMenu.addActionListener(this);
        this.adminEmployeeListMenu.addActionListener(this);

        this.shopModeMenu.add(this.shopToEmployeeMode);
        this.shopModeMenu.add(this.shopToAdminMode);

        this.employeeModeMenu.add(this.employeeToShopMode);
        this.employeeModeMenu.add(this.employeeToAdminMode);

        this.adminModeMenu.add(this.adminToShopMode);
        this.adminModeMenu.add(this.adminToEmployeeMode);

        this.shopOrdersMenu.add(this.shopNewOrderMenu);
        this.shopOrdersMenu.add(this.shopOrderListMenu);

        this.adminOrdersMenu.add(this.adminOrderListMenu);
        this.adminEmployeesMenu.add(this.adminEmployeeListMenu);
        this.adminShopsMenu.add(this.adminShopListMenu);

        this.shopMenuBar.add(this.shopOrdersMenu);

        this.adminMenuBar.add(this.adminOrdersMenu);
        this.adminMenuBar.add(this.adminEmployeesMenu);
        this.adminMenuBar.add(this.adminShopsMenu);

        this.shopMenuBar.add(this.shopModeMenu);
        this.employeeMenuBar.add(this.employeeModeMenu);
        this.adminMenuBar.add(this.adminModeMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();
        if(source == this.shopToEmployeeMode) {
            this.employee = null;
            this.runEmployeeMode();
        }
        if(source == this.shopToAdminMode) this.runAdminMode();
        if(source == this.employeeToShopMode) {
            this.shop = null;
            this.runShopMode();
        }
        if(source == this.employeeToAdminMode) this.runAdminMode();
        if(source == this.adminToShopMode) {
            this.shop = null;
            this.runShopMode();
        }
        if(source == this.adminToEmployeeMode) {
            this.employee = null;
            this.runEmployeeMode();
        }
        if(source == this.shopNewOrderMenu) this.displayShopNewOrder();
        if(source == this.shopOrderListMenu) this.displayShopOrderList();
        if(source == this.adminOrderListMenu) this.displayAdminOrderList();
        if(source == this.adminShopListMenu) this.displayAdminShopList();
        if(source == this.adminEmployeeListMenu) this.displayAdminEmployeeList();


    }

    private void displayEmployee() {
        this.setContentPane(this.employeePanel);
        this.revalidate();
        this.repaint();
    }

    private void displayShopOrderList() {
        this.setContentPane(this.shopOrderListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayShopNewOrder() {
        this.setContentPane(this.shopNewOrderPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayAdminOrderList() {
        this.setContentPane(this.adminOrderListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayAdminShopList() {
        this.setContentPane(this.adminShopListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayAdminEmployeeList() {
        this.setContentPane(this.adminEmployeeListPanel);
        this.revalidate();
        this.repaint();
    }

}
