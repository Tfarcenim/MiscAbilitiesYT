package inf1425.magicstick.server;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class GrabbedEntity {
    private LivingEntity grabbed;
    private LivingEntity grabber;
    private int ticksLeft = 20 * 12;

    public GrabbedEntity(LivingEntity grabbed, LivingEntity grabber) {
        this.grabbed = grabbed;
        this.grabber = grabber;
    }

    public LivingEntity getGrabber() {
        return this.grabber;
    }

    public LivingEntity getGrabbed() {
        return this.grabbed;
    }

    public void tick() {
        if ((--ticksLeft) < 1) {
            this.disable(true);
        }
    }

    public void disable(boolean removeFromHashmap) {
        if (removeFromHashmap) {
            ServerEvents.GRABBED_ENTITIES.remove(grabber.getUUID());
        }
        try {
            grabbed.removeEffect(MobEffects.GLOWING);
            grabber.level.getServer().getScoreboard().removePlayerFromTeam(grabbed.getScoreboardName(), ServerEvents.HELD_MOBS_TEAM);
        } catch (Exception e) { }
    }
}
