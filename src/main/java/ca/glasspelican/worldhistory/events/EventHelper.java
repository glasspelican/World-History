package ca.glasspelican.worldhistory.events;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventHelper {


    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
    }

    @SubscribeEvent
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        //4
        if ((event instanceof PlayerInteractEvent.RightClickBlock) && !event.getWorld().isRemote) {
            if (WorldHistory.instance.isUserOnNotLogList(event.getEntityPlayer().getGameProfile().getName()) > 0) {
                try {
                    ResultSet resultSet = WorldHistory.getSqlConn().getQuery("SELECT * FROM `events` WHERE `x`='" + event.getPos().getX() + "' AND `y`='" + event.getPos().getY() + "' AND `z`='"
                            + event.getPos().getZ() + "' " + "AND `dimensionID`='" + event.getWorld().provider.getDimension() + "' ORDER BY `id` DESC LIMIT " + WorldHistory.instance.isUserOnNotLogList(event.getEntityPlayer().getName()));

                    while (resultSet.next()) {
                        String chat = "";
                        chat += WorldHistory.getSqlConn().getActionType(resultSet.getInt("eventType")); //TODO: Make a method to translate this.
                        chat += ": ";
                        chat += resultSet.getString("time");
                        chat += " ";
                        chat += resultSet.getString("user");

                        event.getEntityPlayer().addChatMessage(new TextComponentString(chat));
                        event.setCanceled(true);
                    }
                    WorldHistory.instance.removeUserFromNotLogList(event.getEntityPlayer().getGameProfile().getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                List<Object> values = new ArrayList<Object>();
                values.add(null);
                if (event.getWorld().getTileEntity(event.getPos()) instanceof IInventory) {
                    values.add(3);
                } else {
                    values.add(4);
                }
                values.add(event.getPos().getX());
                values.add(event.getPos().getY());
                values.add(event.getPos().getZ());
                values.add(event.getWorld().provider.getDimension());
                values.add(event.getEntityPlayer().getGameProfile().getName());
                values.add(currentTimestamp);
                values.add(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName());

                WorldHistory.getSqlConn().insert("events", values);
            }
        }
    }

    @SubscribeEvent
    public void PlayerContainerEvent(PlayerContainerEvent event) {
        //3

    }

    @SubscribeEvent
    public void BreakEvent(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof ITileEntityProvider) {
            BlockBreak(event,2);
        } else if (Config.getBool("logBlockBreak")){
            BlockBreak(event,1);
        }
    }

    private void BlockBreak(BlockEvent.BreakEvent event, int value){
        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        List<Object> values = new ArrayList<>();
        values.add(null);
        values.add(value);
        values.add(event.getPos().getX());
        values.add(event.getPos().getY());
        values.add(event.getPos().getZ());
        values.add(event.getWorld().provider.getDimension());
        values.add(event.getPlayer().getGameProfile().getName());
        values.add(currentTimestamp);
        values.add(event.getState().getBlock().getUnlocalizedName());

        WorldHistory.getSqlConn().insert("events", values);
    }


}
