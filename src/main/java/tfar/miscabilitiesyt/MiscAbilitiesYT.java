package tfar.miscabilitiesyt;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import tfar.miscabilitiesyt.network.PacketHandler;
import tfar.miscabilitiesyt.network.client.S2CSyncModAbilitiesPacket;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MiscAbilitiesYT.MODID)
public class MiscAbilitiesYT {

    public static final String MODID = "miscabilitiesyt";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MiscAbilitiesYT() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
        MinecraftForge.EVENT_BUS.addListener(this::target);
        if (FMLEnvironment.dist.isClient()) {
            ClientModAbilties.setup(bus);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
    }

    private void playerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.START && !e.player.level.isClientSide) {
            PacketHandler.sendToClient(new S2CSyncModAbilitiesPacket(ModAbilities.getFromPlayerSideSafe(e.player)),(ServerPlayer) e.player);
        }
    }

    private void target(LivingSetAttackTargetEvent e) {
        LivingEntity livingEntity = e.getTarget();
        if (livingEntity instanceof Player player) {
            ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
            if (modAbilities.disableHostiles) {
                ((Mob)e.getEntity()).setTarget(null);
            }
        }
    }

    private void commands(RegisterCommandsEvent e) {
        ModCommands.register(e.getDispatcher());
    }

    public static boolean waterWalk(BlockBehaviour.BlockStateBase state, BlockGetter world, BlockPos pos, CollisionContext context){
        Entity entity = ((EntityCollisionContext)context).getEntity();
        if (entity instanceof Player player && state.getBlock() instanceof LiquidBlock && entity.getPose() != Pose.CROUCHING) {
            ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
            boolean wet = player.isInWater() || player.isInLava();
            boolean applyCollision = !wet && modAbilities.waterwalk;
            applyCollision &= world.getBlockState(pos.above()).isAir();
            applyCollision &= state.getMaterial() == Material.WATER;
            return applyCollision;
        }
        return false;
    }

}
