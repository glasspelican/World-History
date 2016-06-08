package k4unl.minecraft.blockLog.events;

import k4unl.minecraft.blockLog.BlockLog;
import k4unl.minecraft.blockLog.lib.config.Config;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventHelper {


	public static void init(){
		MinecraftForge.EVENT_BUS.register(new EventHelper());
		FMLCommonHandler.instance().bus().register(new EventHelper());
	}



    @SubscribeEvent
    public void PlayerInteractEvent(PlayerInteractEvent.RightClickBlock event){
        //4
        if(!event.getWorld().isRemote){
            if(BlockLog.instance.isUserOnNotLogList(event.getEntityPlayer().getGameProfile().getName()) > 0){
                try {
                    ResultSet rs = BlockLog.sqlConn.getQuery("SELECT * FROM `events` WHERE `x`='" + event.getPos().getX() + "' AND `y`='" + event.getPos().getY() + "' AND `z`='"
                      + event.getPos().getZ() + "' " + "AND `dimensionID`='" + event.getWorld().provider.getDimension() + "' ORDER BY `id` DESC LIMIT " + BlockLog.instance.isUserOnNotLogList(event.getEntityPlayer().getGameProfile().getName()));

                    while(rs.next()){
                        String chat = "";
                        chat += BlockLog.sqlConn.getActionType(rs.getInt("eventType")); //TODO: Make a method to translate this.
                        chat += ": ";
                        chat += rs.getString("time");
                        chat += " ";
                        chat += rs.getString("user");

                        event.getEntityPlayer().addChatMessage(new TextComponentString(chat));
                        event.setCanceled(true);
                    }
                    BlockLog.instance.removeUserFromNotLogList(event.getEntityPlayer().getGameProfile().getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                List<Object> values = new ArrayList<Object>();
                values.add(null);
                if(event.getWorld().getTileEntity(event.getPos()) instanceof IInventory){
                    values.add(3);
                }else{
                    values.add(4);
                }
                values.add(event.getPos().getX());
                values.add(event.getPos().getY());
                values.add(event.getPos().getZ());
                values.add(event.getWorld().provider.getDimension());
                values.add(event.getEntityPlayer().getGameProfile().getName());
                values.add(currentTimestamp);
                values.add(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName());

                BlockLog.sqlConn.insert("events", values);
            }
        }
    }

    @SubscribeEvent
    public void PlayerOpenContainerEvent(PlayerOpenContainerEvent event){
        //3

    }

    @SubscribeEvent
    public void BreakEvent(BlockEvent.BreakEvent event){
        //2 and 1
        if(Config.getBool("logBlockBreak")){
            if(!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof ITileEntityProvider)){
                Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                List<Object> values = new ArrayList<Object>();
                values.add(null);
                values.add(1);
                values.add(event.getPos().getX());
                values.add(event.getPos().getY());
                values.add(event.getPos().getZ());
                values.add(event.getWorld().provider.getDimension());
                values.add(event.getPlayer().getGameProfile().getName());
                values.add(currentTimestamp);
                values.add(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName());

                BlockLog.sqlConn.insert("events", values);
            }
        }
        if(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof ITileEntityProvider){
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            List<Object> values = new ArrayList<Object>();
            values.add(null);
            values.add(2);
            values.add(event.getPos().getX());
            values.add(event.getPos().getY());
            values.add(event.getPos().getZ());
            values.add(event.getWorld().provider.getDimension());
            values.add(event.getPlayer().getGameProfile().getName());
            values.add(currentTimestamp);
            values.add(event.getWorld().getBlockState(event.getPos()).getBlock().getUnlocalizedName());

            BlockLog.sqlConn.insert("events", values);
        }
    }


}
