package ca.glasspelican.worldhistory.util;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.Chat;
import ca.glasspelican.worldhistory.lib.Log;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Investigation {
    BlockPos start;
    BlockPos end;

    public Investigation(BlockPos blockPos) {
        this.start = blockPos;
    }

    public void setEnd(BlockPos blockPos) {
        this.end = blockPos;
    }

    public void getUsers(Entity admin) {
        try {
            ResultSet resultSet = WorldHistory.getSqlConn().getQuery("SELECT * FROM `events` WHERE "
                    + "`x` BETWEEN " + start.getX() + " AND " + end.getX()
                    + " AND `z` BETWEEN " + start.getZ() + " AND " + end.getZ()
                    + " AND `dimensionID`= " + admin.getEntityWorld().provider.getDimension()
            );

            while (resultSet.next()) {
                String chat = "";
                chat += resultSet.getString("user");
                Chat.showMessage(admin, chat);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
    }
}
