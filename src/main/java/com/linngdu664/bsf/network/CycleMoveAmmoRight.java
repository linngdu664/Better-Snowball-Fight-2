package com.linngdu664.bsf.network;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf.item.tank.AbstractSnowballTankItem;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Vector;
import java.util.function.Supplier;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)

public class CycleMoveAmmoRight {
    int type, pressedms;

    public CycleMoveAmmoRight(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public CycleMoveAmmoRight(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(CycleMoveAmmoRight message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(CycleMoveAmmoRight message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(context.getSender(), message.type, message.pressedms);
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type, int pressedms) {
        Level level = player.level;
        if (!level.hasChunkAt(player.blockPosition()))
            return;
        if (type == 0) {
            if(!level.isClientSide){
                execute(player);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        Main.addNetworkMessage(CycleMoveAmmoRight.class, CycleMoveAmmoRight::buffer, CycleMoveAmmoRight::new, CycleMoveAmmoRight::handler);
    }

    private static void execute(Player player){
        Inventory inventory = player.getInventory();
        ItemStack itemStack;
        Vector<Integer> list=new Vector<Integer>();
        boolean haveTank=false;
        for (int j = 0; j < inventory.getContainerSize(); j++) {
            itemStack = inventory.getItem(j);
            if (itemStack.getItem() instanceof AbstractSnowballTankItem tank && (tank.getSnowball().canBeLaunchedByNormalWeapon()||tank.getSnowball().canBeLaunchedByMachineGun()) && !listHaveItem(inventory,list,itemStack)) {
                list.add(j);
                haveTank=true;
            }
        }
        if(!haveTank){
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                itemStack = inventory.getItem(i);
                if (itemStack.getItem() instanceof AbstractBSFSnowballItem snowball && snowball.canBeLaunchedByNormalWeapon() && !listHaveItem(inventory,list,itemStack)) {
                    list.add(i);
                }
            }
        }
        for (int i = 0; i < list.size()-1; i++) {
            exchangeItem(inventory,list.get(i),list.get(i+1));
            int c=list.get(i);
            list.set(i,list.get(i+1));
            list.set(i+1,c);
        }
    }
    private static void exchangeItem(Inventory inventory,int a,int b){
        ItemStack ia = inventory.getItem(a);
        ItemStack ib = inventory.getItem(b);
        if(ia!=ItemStack.EMPTY&&ib!=ItemStack.EMPTY){
            inventory.setItem(a,ib);
            inventory.setItem(b,ia);
        }

    }
    private static boolean listHaveItem(Inventory inventory, Vector<Integer> list,ItemStack itemStack){
        for (Integer integer : list) {
            if(contrastItem(inventory,integer,itemStack)){
                return true;
            }
        }
        return false;
    }
    private static boolean contrastItem(Inventory inventory,int a,ItemStack ib){
        ItemStack ia = inventory.getItem(a);
        if(ia!=ItemStack.EMPTY&&ib!=ItemStack.EMPTY){
            return ia.getItem().getDescriptionId().equals(ib.getItem().getDescriptionId());
        }
        return false;
    }

}
