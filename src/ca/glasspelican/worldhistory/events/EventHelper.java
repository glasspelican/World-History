package ca.glasspelican.worldhistory.events;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventHelper {


    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
        FMLCommonHandler.instance().bus().register(new EventHelper());
    }

    @SubscribeEvent
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        //4
        if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && !event.world.isRemote) {
            if (WorldHistory.instance.isUserOnNotLogList(event.entityPlayer.getGameProfile().getName()) > 0) {
                try {
                    ResultSet rs = WorldHistory.sqlConn.getQuery("SELECT * FROM `events` WHERE `x`='" + event.x + "' AND `y`='" + event.y + "' AND `z`='"
                            + event.z + "' " + "AND `dimensionID`='" + event.world.provider.dimensionId + "' ORDER BY `id` DESC LIMIT " + WorldHistory.instance.isUserOnNotLogList(event.entityPlayer.getGameProfile().getName()));

                    while (rs.next()) {
                        String chat = "";
                        chat += WorldHistory.sqlConn.getActionType(rs.getInt("eventType")); //TODO: Make a method to translate this.
                        chat += ": ";
                        chat += rs.getString("time");
                        chat += " ";
                        chat += rs.getString("user");

                        event.entityPlayer.addChatMessage(new ChatComponentText(chat));
                        event.setCanceled(true);
                    }
                    WorldHistory.instance.removeUserFromNotLogList(event.entityPlayer.getGameProfile().getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                List<Object> values = new ArrayList<Object>();
                values.add(null);
                if (event.world.getTileEntity(event.x, event.y, event.z) instanceof IInventory) {
                    values.add(3);
                } else {
                    values.add(4);
                }
                values.add(event.x);
                values.add(event.y);
                values.add(event.z);
                values.add(event.world.provider.dimensionId);
                values.add(event.entityPlayer.getGameProfile().getName());
                values.add(currentTimestamp);
                values.add(event.world.getBlock(event.x, event.y, event.z).getUnlocalizedName());

                WorldHistory.sqlConn.insert("events", values);
            }
        }
    }

    @SubscribeEvent
    public void PlayerOpenContainerEvent(PlayerOpenContainerEvent event) {
        //3

    }

    @SubscribeEvent
    public void BreakEvent(BlockEvent.BreakEvent event) {
        //2 and 1
        if (Config.getBool("logBlockBreak")) {
            if (!(event.block instanceof ITileEntityProvider)) {
                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                List<Object> values = new ArrayList<Object>();
                values.add(null);
                values.add(1);
                values.add(event.x);
                values.add(event.y);
                values.add(event.z);
                values.add(event.world.provider.dimensionId);
                values.add(event.getPlayer().getGameProfile().getName());
                values.add(currentTimestamp);
                values.add(event.block.getUnlocalizedName());

                WorldHistory.sqlConn.insert("events", values);
            }
        }
        if (event.block instanceof ITileEntityProvider) {
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            List<Object> values = new ArrayList<Object>();
            values.add(null);
            values.add(2);
            values.add(event.x);
            values.add(event.y);
            values.add(event.z);
            values.add(event.world.provider.dimensionId);
            values.add(event.getPlayer().getGameProfile().getName());
            values.add(currentTimestamp);
            values.add(event.block.getUnlocalizedName());

            WorldHistory.sqlConn.insert("events", values);
        }
    }


}
