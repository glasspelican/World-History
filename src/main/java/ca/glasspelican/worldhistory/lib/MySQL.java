package ca.glasspelican.worldhistory.lib;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQL {

    private Connection con = null;
    private Statement st = null;

    private Map<Integer, String> actionTypes = new HashMap<Integer, String>();

    public MySQL(String hostName, String userName, String password, String dbName) throws SQLException {

        String url = "jdbc:mysql://" + hostName + ":3306/" + dbName;
        try {
            con = DriverManager.getConnection(url, userName, password);

            st = con.createStatement();
        } catch (SQLException e) {
            Log.error(e.getLocalizedMessage());
            throw e;

        }
    }

    public void updateActionTypes() {
        actionTypes.clear();
        try {
            ResultSet rs = getQuery("SELECT * FROM `eventtypes`");

            while (rs.next()) {
                actionTypes.put(rs.getInt("id"), rs.getString("name"));
            }
            rs.close();
        } catch (SQLException e) {
            Log.error(e.getLocalizedMessage());
        }
    }

    public String getActionType(int index) {
        if (actionTypes.size() == 0) {
            updateActionTypes();
        }
        return actionTypes.get(index);
    }


    public ResultSet getQuery(String SQLQuery) throws SQLException {
        return st.executeQuery(SQLQuery);
    }

    public boolean query(String SQLQuery) throws SQLException {
        return st.execute(SQLQuery);
    }

    public boolean insert(String table, List<Object> values) {

        StringBuilder q = new StringBuilder("INSERT INTO " + table + " VALUES (?");

        for (Object value : values) q.append(",?");

        q.append(")");

        try (PreparedStatement ps = con.prepareStatement(q.toString())) {
            int count = 1;
            for (Object value : values) {
                ps.setObject(count, value);
                count++;
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            Log.error(e.getLocalizedMessage());
        }
        return false;
    }
}
