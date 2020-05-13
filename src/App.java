import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

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
    private JTable adminEmployeeListTable;
    private DefaultTableModel adminEmployeeListModel;
    private JTable adminShopListTable;
    private DefaultTableModel adminShopListModel;
    private JLabel employeePanelLabel;
    private JTable employeePanelProductList;
    private DefaultTableModel tableModel;
    private JButton employeeCompleteButton;
    private EmployeeApp employee;
    private ShopApp shop;
    private AdminApp admin;
    private WarehouseServer server;
    private DefaultTableModel shopOrdersTableModel;
    private JTable shopOrdersTable;
    private DefaultTableModel shopNewOrderModel;
    private JTable shopNewOrderTable;
    private JTextField shopNewOrderCountField;
    private JButton shopNewOrderSubmit;
    private JTable shopNewOrderedTable;
    private DefaultTableModel shopNewOrderedModel;
    private JButton shopNewOrderPlaceOrder;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(500, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.createMenus();

        this.shopOrderListPanel = new JPanel();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Produkty");
        columnNames.add("Status");
        this.shopOrdersTableModel = new DefaultTableModel(columnNames, 0);
        this.shopOrdersTable = new JTable(shopOrdersTableModel);
        this.shopOrdersTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.shopOrdersTable.getColumnModel().getColumn(1).setPreferredWidth(330);
        this.shopOrdersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.shopOrderListPanel.add(shopOrdersTable);


        this.shopNewOrderPanel = new JPanel();
        columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Available");
        this.shopNewOrderModel = new DefaultTableModel(columnNames, 0);
        this.shopNewOrderTable = new JTable(shopNewOrderModel);
        this.shopNewOrderTable.setPreferredScrollableViewportSize(new Dimension(460,200));
        this.shopNewOrderTable.setFillsViewportHeight(true);
        JScrollPane shopNewOrderScroll = new JScrollPane(this.shopNewOrderTable);
        shopNewOrderScroll.setVisible(true);
        this.shopNewOrderPanel.add(shopNewOrderScroll);
        this.shopNewOrderCountField = new JTextField("Ilość");
        this.shopNewOrderPanel.add(shopNewOrderCountField);
        this.shopNewOrderSubmit = new JButton("Add");
        this.shopNewOrderPanel.add(this.shopNewOrderSubmit);
        columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Count");
        this.shopNewOrderedModel = new DefaultTableModel(columnNames, 0);
        this.shopNewOrderedTable = new JTable(shopNewOrderedModel);
        this.shopNewOrderedTable.setPreferredScrollableViewportSize(new Dimension(460,200));
        this.shopNewOrderedTable.setFillsViewportHeight(true);
        JScrollPane shopNewOrderedScroll = new JScrollPane(this.shopNewOrderedTable);
        shopNewOrderedScroll.setVisible(true);
        this.shopNewOrderPanel.add(shopNewOrderedScroll);
        this.shopNewOrderPlaceOrder = new JButton("Place order");
        this.shopNewOrderPanel.add(shopNewOrderPlaceOrder);
        shopNewOrderSubmit.addActionListener(e -> {
            if(shopNewOrderTable.getSelectedRow() == -1) return;
            Vector<String> row = new Vector<>();
            row.add((String) shopNewOrderTable.getValueAt(shopNewOrderTable.getSelectedRow(),0));
            row.add((String) shopNewOrderTable.getValueAt(shopNewOrderTable.getSelectedRow(),1));
            try {
                if(Integer.parseInt(row.get(0)) - Integer.parseInt(shopNewOrderCountField.getText()) < 0) return;
            } catch(Exception a){
                return;
            }
            row.add(shopNewOrderCountField.getText());
            shopNewOrderedModel.addRow(row);
            List<Integer> product = new ArrayList<>();
            product.add(Integer.parseInt(row.get(0)));
            product.add(Integer.parseInt(row.get(2)));
            this.shop.addToOrder(product);
            this.revalidate();
            this.repaint();
        });
        shopNewOrderPlaceOrder.addActionListener(e -> {
            this.shop.makeOrder();
            displayShopNewOrder();
        });

        this.employeePanel = new JPanel();
        this.employeePanelLabel = new JLabel("Obsługiwane zamówienie: Brak");
        this.employeePanel.add(employeePanelLabel);
        columnNames = new Vector<>();
        columnNames.add("Product_id");
        columnNames.add("Count");
        this.tableModel = new DefaultTableModel(columnNames, 0);
        this.employeePanelProductList = new JTable(tableModel);
        this.employeePanelProductList.getColumnModel().getColumn(0).setPreferredWidth(230);
        this.employeePanelProductList.getColumnModel().getColumn(1).setPreferredWidth(230);
        this.employeePanel.add(this.employeePanelProductList);
        this.employeeCompleteButton = new JButton("Zamówienie gotowe");
        employeeCompleteButton.addActionListener(e -> {
            if(this.employee.is_working) this.employee.completeOrder();
            this.server.assignOrders();
            displayEmployee();
        });

        this.employeePanel.add(this.employeeCompleteButton);

        this.adminOrderListPanel = new JPanel();
        this.adminOrderListPanel.add(new JLabel("Lista zamówień"));

        this.adminEmployeeListPanel = new JPanel();
        columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Imię i nazwisko");
        columnNames.add("Wypełnione zamówienia");
        columnNames.add("Pensja");
        this.adminEmployeeListModel = new DefaultTableModel(columnNames, 0);
        this.adminEmployeeListTable = new JTable(this.adminEmployeeListModel);
        this.adminEmployeeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.adminEmployeeListTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.adminEmployeeListTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        this.adminEmployeeListTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        this.adminEmployeeListTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.adminEmployeeListPanel.add(new JLabel("Lista pracowników"));
        this.adminEmployeeListPanel.add(this.adminEmployeeListTable);

        this.adminShopListPanel = new JPanel();
        columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Nazwa");
        columnNames.add("Adres");
        this.adminShopListModel = new DefaultTableModel(columnNames, 0);
        this.adminShopListTable = new JTable(this.adminShopListModel);
        this.adminShopListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.adminShopListTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.adminShopListTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        this.adminShopListTable.getColumnModel().getColumn(2).setPreferredWidth(230);
        this.adminShopListPanel.add(new JLabel("Lista sklepów"));
        this.adminShopListPanel.add(this.adminShopListTable);

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

            if(input == null) this.shop = new ShopApp(this.server, ids.get(0));
            else {
                for (int i = 0; i < choices.size(); i++) {
                    if (choices.get(i) == input) this.shop = new ShopApp(this.server, ids.get(i));
                }
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

            if(input == null) this.employee = server.checkin(ids.get(0));
            else {
                for(int i = 0; i < choices.size(); i++) {
                    if(choices.get(i) == input) this.employee = server.checkin(ids.get(i));
                }
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
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        if(this.employee.is_working) {
            this.employeePanelLabel.setText(String.format(
                    "Obsługiwane zamówienie: %d", this.employee.current_order_id));

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Product_id");
            columnNames.add("Count");
            this.tableModel.addRow(columnNames);

            for(Map<String, String> product : this.employee.orderProducts){
                //this.employeePanelListModel.addElement(String.format("[ID] %s: %s", product.get("Product_id"), product.get("Count")));
                Vector<String> row = new Vector<>();
                row.add(product.get("Product_id"));
                row.add(product.get("Count"));
                this.tableModel.addRow(row);
            }
        }
        else{
            this.employeePanelLabel.setText("Obsługiwane zamówienie: Brak");
        }
        this.setContentPane(this.employeePanel);
        this.revalidate();
        this.repaint();
    }

    private void displayShopOrderList() {
        while(shopOrdersTableModel.getRowCount() > 0) {
            shopOrdersTableModel.removeRow(0);
        }
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Products");
        columnNames.add("Status");
        this.shopOrdersTableModel.addRow(columnNames);
        Map<Integer, List<Product>> orders = this.shop.getOrdersDetails();
        for(Integer order : orders.keySet()){
            Vector<String> row = new Vector<>();
            row.add(order.toString());
            row.add(orders.get(order).toString());
            row.add(this.shop.getOrderStatus(order));
            shopOrdersTableModel.addRow(row);
        }

        this.setContentPane(this.shopOrderListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayShopNewOrder() {
        while(shopNewOrderedModel.getRowCount() > 0) {
            shopNewOrderedModel.removeRow(0);
        }
        while(shopNewOrderModel.getRowCount() > 0) {
            shopNewOrderModel.removeRow(0);
        }
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Available");
        this.shopNewOrderModel.addRow(columnNames);

        columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Count");
        this.shopNewOrderedModel.addRow(columnNames);

        List<Map<String, String>> productsRaw = this.shop.getProducts();
        for(Map<String, String> product : productsRaw){
            Vector<String> row = new Vector<>();
            row.add(product.get("Product_id"));
            row.add(product.get("Name"));
            row.add(product.get("Count"));
            shopNewOrderModel.addRow(row);
        }

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
        List<Map<String, String>> shops = this.admin.getShops();
        this.adminShopListModel.setRowCount(0);
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Nazwa");
        columnNames.add("Adres");
        this.adminEmployeeListModel.addRow(columnNames);
        for(int i = 0; i < shops.size(); i++) {
            this.adminShopListModel.addRow(new Object[]{shops.get(i).get("Shop_id"), shops.get(i).get("Name"), shops.get(i).get("Address")});
        }

        this.setContentPane(this.adminShopListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayAdminEmployeeList() {
        List<Map<String, String>> employees = this.admin.getEmployees();
        this.adminEmployeeListModel.setRowCount(0);
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Imię i nazwisko");
        columnNames.add("Ukończone zamówienia");
        columnNames.add("Pensja");
        this.adminEmployeeListModel.addRow(columnNames);
        for(int i = 0; i < employees.size(); i++) {
            this.adminEmployeeListModel.addRow(new Object[]{employees.get(i).get("Employee_id"),
                    employees.get(i).get("First_name") + " " + employees.get(i).get("Last_name"), employees.get(i).get("Completed_orders"),
                    employees.get(i).get("Salary")});
        }

        this.setContentPane(this.adminEmployeeListPanel);
        this.revalidate();
        this.repaint();
    }

}
