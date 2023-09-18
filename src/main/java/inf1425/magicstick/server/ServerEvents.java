package inf1425.magicstick.server;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfar.miscabilitiesyt.MiscAbilitiesYT;
import tfar.miscabilitiesyt.ModAbilities;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ServerEvents {
    public static final String HELD_MOBS_TEAM_NAME = MiscAbilitiesYT.MODID + "_team_held_mobs";
    public static PlayerTeam HELD_MOBS_TEAM;
    public static HashMap<UUID, GrabbedEntity> GRABBED_ENTITIES = new HashMap<>();

    @SubscribeEvent
    public static void onLevelLoaded(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        GRABBED_ENTITIES.clear();

        HELD_MOBS_TEAM = event.getLevel().getServer().getScoreboard().getPlayerTeam(HELD_MOBS_TEAM_NAME);
        if (HELD_MOBS_TEAM == null) {
            HELD_MOBS_TEAM = event.getLevel().getServer().getScoreboard().addPlayerTeam(HELD_MOBS_TEAM_NAME);
            HELD_MOBS_TEAM.setColor(ChatFormatting.RED);
        }
    }

    @SubscribeEvent
    public static void tickServer(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

        GRABBED_ENTITIES.values().forEach(entity -> {
            LivingEntity grabbed = entity.getGrabbed();
            LivingEntity user = entity.getGrabber();

            Vec3 userEyePos = user.getEyePosition();
            Vec3 userLook = user.getLookAngle();
            BlockHitResult hitResult = user.level.clip(new ClipContext(userEyePos, userEyePos.add(user.getLookAngle().scale(7)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));

            Vec3 placePos = userEyePos;
            if (hitResult.getType() == HitResult.Type.MISS) {
                placePos = placePos.add(userLook.multiply(4, 4, 4)).subtract(0, grabbed.getBbHeight() / 2.0, 0);
            } else {
                Vec3 fromUserEye = hitResult.getLocation().subtract(userEyePos);
                placePos = placePos.add(fromUserEye.multiply(0.75, 1, 0.75));
            }

            grabbed.teleportTo(placePos.x, placePos.y, placePos.z);
            grabbed.resetFallDistance();
            grabbed.setDeltaMovement(0, 0, 0);

            ((ServerLevel) grabbed.level).sendParticles(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0F), grabbed.getX(), grabbed.getY() + (grabbed.getBbHeight() / 2.0F), grabbed.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        });
    }

    @SubscribeEvent
    public static void shutDownServer(ServerStoppingEvent event) {
        GRABBED_ENTITIES.values().forEach(entity -> entity.disable(false));
    }

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }

        GrabbedEntity grabbedEntity = GRABBED_ENTITIES.getOrDefault(event.player.getUUID(), null);

        if (grabbedEntity != null) {
            if (!ModAbilities.getFromPlayerSideSafe(event.player).telekinesis) {
                grabbedEntity.disable(true);
            }
            grabbedEntity.tick();
        }
    }
}
