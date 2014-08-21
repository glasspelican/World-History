package k4unl.minecraft.blockLog.lib;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySQL {
    private Connection con = null;
    private Statement st = null;

    public MySQL(String hostName, String userName, String password, String dbName) throws SQLException {
        String url = "jdbc:mysql://" + hostName + ":3306/" + dbName;
        try {
            con = DriverManager.getConnection(url, userName, password);

            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;

        }
    }

    public ResultSet getQuery(String SQLQuery) throws SQLException {
        return st.executeQuery(SQLQuery);
    }

    public boolean query(String SQLQuery) throws SQLException {
        return st.execute(SQLQuery);
    }

    public boolean insert(String table, List<Object> values){
        try {
            String q = "INSERT INTO " + table + " VALUES (?";

            for(int i = 1; i < values.size(); i++){
                q += ",?";
            }
            PreparedStatement ps = con.prepareStatement(q + ")");

            int count = 1;
            for(Object value : values){
                ps.setObject(count, value);
                count++;
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
