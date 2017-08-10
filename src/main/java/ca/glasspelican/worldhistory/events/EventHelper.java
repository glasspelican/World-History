package ca.glasspelican.worldhistory.events;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.Chat;
import ca.glasspelican.worldhistory.lib.Log;
import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
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
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventHelper implements Runnable {
    private static LinkedBlockingDeque<PlayerInteractEvent> eventQue = new LinkedBlockingDeque<>();
    private static AtomicBoolean run = new AtomicBoolean(false);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
        run.set(true);
        new Thread(new EventHelper()).start();
    }

    public static void stop() {
        run.set(false);
    }

    @SubscribeEvent
    public void PlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
        //4
        if (event.getSide().isServer()) {
            if (WorldHistory.instance.isUserOnNotLogList(event.getEntityPlayer().getGameProfile().getName()) > 0) {
                eventQue.offer(event);
                event.setCanceled(true);
            } else {

                BlockEvent(
                        event.getWorld().getTileEntity(event.getPos()) instanceof IInventory ? 3 : 4,
                        event.getPos(),
                        event.getEntityPlayer(),
                        event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName()
                );
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
            BlockEvent(2, event.getPos(), event.getPlayer(), event.getState().getBlock().getUnlocalizedName());
        } else if (Config.getBool("logBlockBreak")) {
            BlockEvent(1, event.getPos(), event.getPlayer(), event.getState().getBlock().getUnlocalizedName());
        }
    }

    private void BlockEvent(int eventType, BlockPos blockPos, EntityPlayer entityPlayer, String blockName) {
        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        List<Object> values = new ArrayList<>();
        values.add(null);
        values.add(eventType);
        values.add(blockPos.getX());
        values.add(blockPos.getY());
        values.add(blockPos.getZ());
        values.add(entityPlayer.getEntityWorld().provider.getDimension());
        values.add(entityPlayer.getGameProfile().getName());
        values.add(currentTimestamp);
        values.add(blockName);

        WorldHistory.getSqlConn().asyncInsert("events", values);
    }

    private void QueryBlockHistory(PlayerInteractEvent event) {
        try {
            ResultSet resultSet = WorldHistory.getSqlConn().getQuery("SELECT * FROM `events` WHERE `x`='" + event.getPos().getX() + "' AND `y`='" + event.getPos().getY() + "' AND `z`='"
                    + event.getPos().getZ() + "' " + "AND `dimensionID`='" + event.getWorld().provider.getDimension() + "' ORDER BY `id` DESC LIMIT " + WorldHistory.instance.isUserOnNotLogList(event.getEntityPlayer().getName()));

            while (resultSet.next()) {
                String chat = "";
                chat += WorldHistory.getSqlConn().getActionType(resultSet.getInt("eventType"));
                chat += ": ";
                chat += resultSet.getString("time");
                chat += " ";
                chat += resultSet.getString("user");

                Chat.showMessage(event.getEntityPlayer(), chat);
            }
            WorldHistory.instance.removeUserFromNotLogList(event.getEntityPlayer().getGameProfile().getName());
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    @Override
    public void run() {
        Log.info("Starting Event Thread");

        PlayerInteractEvent interactEvent;

        while (run.get()) {
            try {
                interactEvent = eventQue.take();
                QueryBlockHistory(interactEvent);
            } catch (Exception e) {
                Log.error(e);
                break;
            }
        }
    }


}
