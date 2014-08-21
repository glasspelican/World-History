package k4unl.minecraft.pvpToggle.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHelper {


	public static void init(){
		MinecraftForge.EVENT_BUS.register(new EventHelper());
		FMLCommonHandler.instance().bus().register(new EventHelper());
	}

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event){

    }


    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event){

    }

    @SubscribeEvent
    public void OnPlayerRespawn(PlayerEvent.Clone event){

    }

    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {

    }
}
