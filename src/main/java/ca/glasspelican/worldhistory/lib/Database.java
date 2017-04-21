package ca.glasspelican.worldhistory.lib;

import ca.glasspelican.worldhistory.lib.tables.EnumEventTypes;
import ca.glasspelican.worldhistory.lib.tables.EventLog;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private Connection con = null;
    private Statement st = null;

    private Map<Integer, String> actionTypes = new HashMap<>();

    /**
     * create the mysql database connection
     *
     * @param hostName
     * @param userName
     * @param password
     * @param dbName
     * @throws SQLException
     */
    //
    public Database(String hostName, String userName, String password, String dbName) throws SQLException {

        String url = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        Log.info("Connecting to mysql database: " + url);
        try {
            con = DriverManager.getConnection(url, userName, password);

            st = con.createStatement();

            this.init();
        } catch (SQLException e) {
            Log.error(e);
            throw e;
        }
    }


    /**
     * create and connect to the embedded h2database
     *
     * @param worldPath
     * @param dbName
     * @throws SQLException
     */
    public Database(String worldPath, String dbName) throws SQLException {

        String url = "jdbc:h2:" + worldPath + dbName;

        Log.info("Connecting to embedded database: " + url);
        try {
            con = DriverManager.getConnection(url);

            st = con.createStatement();

            this.init();
        } catch (SQLException e) {
            Log.error(e);
            throw e;
        }
    }

    private void init() throws SQLException {
        this.query(EnumEventTypes.getTableStructure());
        try {
            this.query(EnumEventTypes.getTable());
        } catch (SQLException e) {
            //we already have this
        }
        this.query(EventLog.getInitQuery());
    }

    /**
     * read the list of available events from the database
     */
    public void updateActionTypes() {
        actionTypes.clear();
        try {
            ResultSet rs = getQuery("SELECT * FROM `eventtypes`");

            while (rs.next()) {
                actionTypes.put(rs.getInt("id"), rs.getString("name"));
            }
            rs.close();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    /**
     * convert event id to event name
     *
     * @param id
     * @return
     */
    public String getActionType(int id) {
        if (actionTypes.size() == 0) {
            updateActionTypes();
        }
        return actionTypes.get(id);
    }


    public ResultSet getQuery(String query) throws SQLException {
        return st.executeQuery(query);
    }

    public boolean query(String query) throws SQLException {
        Log.info(query);
        return st.execute(query);
    }

    /**
     * @param table
     * @param values
     * @return
     */
    public void insert(String table, List<Object> values) {
        try {
            String q = "INSERT INTO " + table + " VALUES (?";

            for (int i = 1; i < values.size(); i++) {
                q += ",?";
            }
            PreparedStatement ps = con.prepareStatement(q + ")");

            int count = 1;
            for (Object value : values) {
                ps.setObject(count, value);
                count++;
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    /**
     * close the database connection
     */
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            Log.error(e);
        }
    }
}
