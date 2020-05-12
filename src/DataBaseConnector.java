import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataBaseConnector {
    private String driverString = "jdbc:oracle:thin:@";
    private String host = "localhost";//"156.17.43.90";
    private String port = "1521";
    private String serviceName = "xe";
    private String user = "Nyatan";//"pwr_19_20_L_018248844";
    private String password = "dupadupa";

    private Connection connection;

    public DataBaseConnector connect(){
        System.out.println("Connecting to database as " + user);
        try {
            connection = DriverManager.getConnection(
                    driverString+host+":"+port+":"+serviceName, user, password);
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public ResultSet query(String query){
        Statement st = null;
        ResultSet rs = null;
        connect();
        try {
            st = connection.createStatement();

            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<Map<String, String>> fetch(String query){
        try {
            List<Map<String, String>> result = new ArrayList<>();

            ResultSet rs = query(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> cols = new ArrayList<>();

            for(int i=1;i<=rsmd.getColumnCount(); ++i) cols.add(rsmd.getColumnName(i));


            while(rs.next()){
                Map<String, String> row = new HashMap<>();
                for(String column : cols){
                    try {
                        row.put(column, rs.getString(column));
                    } catch (Exception e){
                        row.put(column, Integer.toString(rs.getInt(column)));
                    }
                }
                result.add(row);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
