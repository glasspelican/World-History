package ca.glasspelican.worldhistory.lib;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.tables.EnumEventTypes;
import ca.glasspelican.worldhistory.lib.tables.EventLog;
import ca.glasspelican.worldhistory.util.AsyncDatabase;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class Database {

    private static final LinkedBlockingDeque<AsyncQueObject> asyncInsertQue = new LinkedBlockingDeque();
    private static final AtomicBoolean runAsyncThread = new AtomicBoolean(true);
    private Connection connection = null;
    private Statement statement = null;
    private Thread asyncThread;
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
            connection = DriverManager.getConnection(url, userName, password);

            statement = connection.createStatement();

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
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            this.init();
        } catch (SQLException e) {
            Log.error(e);

            throw new ReportedException(new CrashReport("Unable to Connect to embedded database", e));
        }
    }

    public static LinkedBlockingDeque<AsyncQueObject> getAsyncInsertQue() {
        return asyncInsertQue;
    }

    public static AtomicBoolean getRunAsyncThread() {
        return runAsyncThread;
    }

    private void init() throws SQLException {
        this.query(EnumEventTypes.getTableStructure());
        try {
            this.query(EnumEventTypes.getTable());
        } catch (SQLException e) {
            //we already have this
        }
        this.query(EventLog.getInitQuery());

        asyncThread = new Thread(new AsyncDatabase());

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
        Log.info(query);
        return statement.executeQuery(query);
    }

    public boolean query(String query) throws SQLException {
        Log.info(query);
        return statement.execute(query);
    }

    /**
     * @param table
     * @param values
     * @return
     */
    public void insert(String table, List<Object> values) {
        try {
            String q = String.format("INSERT INTO %s VALUES (?", table);

            for (int i = 1; i < values.size(); i++) {
                q += ",?";
            }
            PreparedStatement ps = connection.prepareStatement(q + ")");

            int count = 1;
            for (Object value : values) {
                ps.setObject(count, value);
                count++;
            }
            ps.executeUpdate();
            Log.info(ps.toString());
            ps.close();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    public boolean asyncInsert(String table, List<Object> values) {
        Log.info("AsyncInsert");
        return asyncInsertQue.offer(new AsyncQueObject(table, values));
    }

    /**
     * close the database connection
     */
    public void close() {
        //cleanup worker thread
        Database.getRunAsyncThread().set(false);
        //write out que
        for (AsyncQueObject queObject : Database.getAsyncInsertQue()) {
            WorldHistory.getSqlConn().insert(queObject.getString(), queObject.getList());
        }

        try {
            asyncThread.join();
        } catch (InterruptedException e) {
            Log.error(e);
        }

        //close database
        Log.info("Closing Database connection");
        try {
            connection.close();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

}

