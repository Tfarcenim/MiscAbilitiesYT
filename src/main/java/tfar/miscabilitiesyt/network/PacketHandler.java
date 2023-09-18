package tfar.miscabilitiesyt.network;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tfar.miscabilitiesyt.MiscAbilitiesYT;
import tfar.miscabilitiesyt.network.client.S2CSyncModAbilitiesPacket;

public class PacketHandler {

    public static SimpleChannel INSTANCE;

    public static void registerMessages() {

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MiscAbilitiesYT.MODID, MiscAbilitiesYT.MODID), () -> "1.0", s -> true, s -> true);

        int i = 0;

        INSTANCE.registerMessage(i++,
                S2CSyncModAbilitiesPacket.class,
                S2CSyncModAbilitiesPacket::encode,
                S2CSyncModAbilitiesPacket::new,
                S2CSyncModAbilitiesPacket::handle);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }
}
