import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class App extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JMenuBar shopMenuBar;
    private JMenuBar employeeMenuBar;
    private JMenuBar managerMenuBar;
    private JMenu shopOrdersMenu;
    private JMenu managerOrdersMenu;
    private JMenu managerEmployeesMenu;
    private JMenu managerShopsMenu;
    private JMenu shopModeMenu;
    private JMenu employeeModeMenu;
    private JMenu managerModeMenu;
    private JMenuItem shopOrderListMenu;
    private JMenuItem managerOrderListMenu;
    private JMenuItem shopNewOrderMenu;
    private JMenuItem shopToEmployeeMode;
    private JMenuItem shopToManagerMode;
    private JMenuItem employeeToShopMode;
    private JMenuItem employeeToManagerMode;
    private JMenuItem managerToShopMode;
    private JMenuItem managerToEmployeeMode;
    private JMenuItem managerEmployeeListMenu;
    private JMenuItem managerShopListMenu;
    private JPanel shopOrderListPanel;
    private JPanel shopNewOrderPanel;
    private JPanel managerOrderListPanel;
    private JPanel managerEmployeeListPanel;
    private JPanel managerShopListPanel;
    private JPanel employeePanel;

    private EmployeeApp employee;
    private ShopApp shop;
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
        this.managerOrderListPanel = new JPanel();
        this.managerOrderListPanel.add(new JLabel("Lista zamówień"));
        this.managerEmployeeListPanel = new JPanel();
        this.managerEmployeeListPanel.add(new JLabel("Lista pracowników"));
        this.managerShopListPanel = new JPanel();
        this.managerShopListPanel.add(new JLabel("Lista sklepów"));

        this.shop = null;
        this.employee = null;
        this.server = new WarehouseServer();

        String[] choices = { "sklep", "pracownik", "manager" };
        String input = (String) JOptionPane.showInputDialog(this, "Kim jesteś:",
                "Wybór roli", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        if(input == choices[0]) {
            this.runShopMode();
        } else if (input == choices[1]){
            this.runEmployeeMode();
        } else if (input == choices[2]) {
            this.runManagerMode();
        }
    }

    private void runShopMode() {
        if(this.shop != null) {
            String[] choices = { "Nazwa Firmy 1", "Nazwa Firmy 2", "Nazwa Firmy 3" };
            String input = (String) JOptionPane.showInputDialog(this, "Wybierz swoją firmę:",
                    "Wybór sklepu", JOptionPane.QUESTION_MESSAGE, null,
                    choices,
                    choices[0]);

            for(int i = 0; i < choices.length; i++) {
                if(choices[i] == input) this.shop = new ShopApp(this.server, 1);
            }
        }
        this.setTitle("Obsługa sklepu");
        this.setJMenuBar(this.shopMenuBar);
        this.displayShopNewOrder();
        this.setVisible(true);
    }

    private void runEmployeeMode() {
        if(this.employee == null) {
            String[] choices = { "Adam Nowak", "Arutur śliwa", "Sracz 2" };
            String input = (String) JOptionPane.showInputDialog(null, "Wybierz siebie z listy:",
                    "Wybór pracownika", JOptionPane.QUESTION_MESSAGE, null,
                    choices,
                    choices[0]);

            for(int i = 0; i < choices.length; i++) {
                if(choices[i] == input) this.employee = new EmployeeApp(this.server, 1);
            }
        }
        setTitle("Obsługa pracownika");
        this.setJMenuBar(this.employeeMenuBar);
        this.displayEmployee();
        setVisible(true);
    }

    private void runManagerMode() {
        setTitle("Obsługa managera");
        this.setJMenuBar(this.managerMenuBar);
        this.displayManagerOrderList();
        setVisible(true);
    }

    private void createMenus() {
        String changeMode = "Zmień tryb";
        String orders = "Zamówienie";
        String shop = "Sklep";
        String employee = "Pracownik";
        String manager = "Manager";
        String orderList = "Lista zamówień";

        this.shopMenuBar = new JMenuBar();
        this.employeeMenuBar = new JMenuBar();
        this.managerMenuBar = new JMenuBar();

        this.shopModeMenu = new JMenu(changeMode);
        this.employeeModeMenu = new JMenu(changeMode);
        this.managerModeMenu = new JMenu(changeMode);
        this.shopOrdersMenu = new JMenu(orders);
        this.managerOrdersMenu = new JMenu(orders);
        this.managerEmployeesMenu = new JMenu(employee);
        this.managerShopsMenu = new JMenu(shop);


        this.shopToEmployeeMode = new JMenuItem(employee);
        this.shopToManagerMode = new JMenuItem(manager);

        this.employeeToShopMode = new JMenuItem(shop);
        this.employeeToManagerMode = new JMenuItem(manager);

        this.managerToShopMode = new JMenuItem(shop);
        this.managerToEmployeeMode = new JMenuItem(employee);

        this.shopNewOrderMenu = new JMenuItem("Nowe zamówienie");
        this.shopOrderListMenu = new JMenuItem(orderList);
        this.managerOrderListMenu = new JMenuItem(orderList);

        this.managerShopListMenu = new JMenuItem("Lista sklepów");
        this.managerEmployeeListMenu = new JMenuItem("Lista pracowników");

        this.shopToEmployeeMode.addActionListener(this);
        this.shopToManagerMode.addActionListener(this);
        this.employeeToShopMode.addActionListener(this);
        this.employeeToManagerMode.addActionListener(this);
        this.managerToShopMode.addActionListener(this);
        this.managerToEmployeeMode.addActionListener(this);
        this.shopNewOrderMenu.addActionListener(this);
        this.shopOrderListMenu.addActionListener(this);
        this.managerOrderListMenu.addActionListener(this);
        this.managerShopListMenu.addActionListener(this);
        this.managerEmployeeListMenu.addActionListener(this);

        this.shopModeMenu.add(this.shopToEmployeeMode);
        this.shopModeMenu.add(this.shopToManagerMode);

        this.employeeModeMenu.add(this.employeeToShopMode);
        this.employeeModeMenu.add(this.employeeToManagerMode);

        this.managerModeMenu.add(this.managerToShopMode);
        this.managerModeMenu.add(this.managerToEmployeeMode);

        this.shopOrdersMenu.add(this.shopNewOrderMenu);
        this.shopOrdersMenu.add(this.shopOrderListMenu);

        this.managerOrdersMenu.add(this.managerOrderListMenu);
        this.managerEmployeesMenu.add(this.managerEmployeeListMenu);
        this.managerShopsMenu.add(this.managerShopListMenu);

        this.shopMenuBar.add(this.shopOrdersMenu);

        this.managerMenuBar.add(this.managerOrdersMenu);
        this.managerMenuBar.add(this.managerEmployeesMenu);
        this.managerMenuBar.add(this.managerShopsMenu);

        this.shopMenuBar.add(this.shopModeMenu);
        this.employeeMenuBar.add(this.employeeModeMenu);
        this.managerMenuBar.add(this.managerModeMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();
        if(source == this.shopToEmployeeMode) {
            this.employee = null;
            this.runEmployeeMode();
        }
        if(source == this.shopToManagerMode) this.runManagerMode();
        if(source == this.employeeToShopMode) {
            this.shop = null;
            this.runShopMode();
        }
        if(source == this.employeeToManagerMode) this.runManagerMode();
        if(source == this.managerToShopMode) {
            this.shop = null;
            this.runShopMode();
        }
        if(source == this.managerToEmployeeMode) {
            this.employee = null;
            this.runEmployeeMode();
        }
        if(source == this.shopNewOrderMenu) this.displayShopNewOrder();
        if(source == this.shopOrderListMenu) this.displayShopOrderList();
        if(source == this.managerOrderListMenu) this.displayManagerOrderList();
        if(source == this.managerShopListMenu) this.displayManagerShopList();
        if(source == this.managerEmployeeListMenu) this.displayManagerEmployeeList();


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

    private void displayManagerOrderList() {
        this.setContentPane(this.managerOrderListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayManagerShopList() {
        this.setContentPane(this.managerShopListPanel);
        this.revalidate();
        this.repaint();
    }

    private void displayManagerEmployeeList() {
        this.setContentPane(this.managerEmployeeListPanel);
        this.revalidate();
        this.repaint();
    }

}
