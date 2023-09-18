package tfar.miscabilitiesyt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tfar.miscabilitiesyt.network.NetworkHelper;

import java.lang.reflect.Field;

public class ModAbilities {

    public boolean waterwalk;
    public boolean lavawalk;
    public boolean vision;
    public boolean disableHostiles;

    public static ModAbilities readFromNetwork(FriendlyByteBuf buf) {
        ModAbilities modAbilities = NetworkHelper.deserialize(buf, ModAbilities.class);
        return modAbilities;
    }

    public void writeToNetwork(FriendlyByteBuf buf) {
        NetworkHelper.serialize(buf,this);
    }

    public static ModAbilities loadNBT(CompoundTag tag) {
        ModAbilities modAbilities = new ModAbilities();
        modAbilities.waterwalk = tag.getBoolean("waterwalk");
        modAbilities.lavawalk = tag.getBoolean("lavawalk");
        modAbilities.vision = tag.getBoolean("vision");
        modAbilities.disableHostiles = tag.getBoolean("disableHostiles");
        return modAbilities;
    }

    public CompoundTag saveNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("waterwalk",waterwalk);
        tag.putBoolean("lavawalk",lavawalk);
        tag.putBoolean("vision",vision);
        tag.putBoolean("disableHostiles",disableHostiles);
        return tag;
    }

    public static ModAbilities getFromPlayerServer(Player player) {
        if (player.level.isClientSide) {
            MiscAbilitiesYT.LOGGER.warn("Can't get data on the client");
            return null;
        } else {
            CompoundTag data = player.getPersistentData();
            if (data.contains(MiscAbilitiesYT.MODID)) {
                return loadNBT(data.getCompound(MiscAbilitiesYT.MODID));
            } else {
                return new ModAbilities();
            }
        }
    }

    public void saveToPlayer(Player player) {
        if (player.level.isClientSide) {
            MiscAbilitiesYT.LOGGER.warn("Can't save data on the client");
        } else {
            CompoundTag data = player.getPersistentData();
            data.put(MiscAbilitiesYT.MODID,saveNBT());
        }
    }

    public static ModAbilities getFromPlayerSideSafe(Player player) {
        ModAbilities modAbilities;
        if (player.level.isClientSide) {
            modAbilities = ClientModAbilties.clientModAbilities;
        } else {
            modAbilities = getFromPlayerServer(player);
        }
        return modAbilities;
    }

    public static boolean canWaterWalk(Player player) {
        ModAbilities modAbilities = getFromPlayerSideSafe(player);
        return modAbilities.waterwalk;
    }
}
