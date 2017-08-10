package ca.glasspelican.worldhistory.util;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.AsyncQueObject;
import ca.glasspelican.worldhistory.lib.Database;
import ca.glasspelican.worldhistory.lib.Log;

public class AsyncDatabase implements Runnable {

    public void run() {
        Log.info("Starting Database Thread");
        AsyncQueObject queObject;
        while (Database.getRunAsyncThread().get()) {
            try {
                queObject = Database.getAsyncInsertQue().take();
                WorldHistory.getSqlConn().insert(queObject.getString(), queObject.getList());
            } catch (InterruptedException e) {
                Log.error(e);
                break;
            }
        }
        Log.info("Stopping Database Thread");
    }


}
