package tfar.miscabilitiesyt.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.miscabilitiesyt.ClientModAbilties;
import tfar.miscabilitiesyt.ModAbilities;
import tfar.miscabilitiesyt.network.util.S2CPacketHelper;


public class S2CSyncModAbilitiesPacket implements S2CPacketHelper {

    private final ModAbilities modAbilities;

    public S2CSyncModAbilitiesPacket(ModAbilities modAbilities) {
        this.modAbilities = modAbilities;
    }

    public S2CSyncModAbilitiesPacket(FriendlyByteBuf buf) {
        modAbilities = ModAbilities.readFromNetwork(buf);
    }

    @Override
    public void handleClient() {
        ClientModAbilties.clientModAbilities = modAbilities;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        modAbilities.writeToNetwork(buf);
    }
}