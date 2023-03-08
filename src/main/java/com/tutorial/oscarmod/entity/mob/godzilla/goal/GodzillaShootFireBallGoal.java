package com.tutorial.oscarmod.entity.mob.godzilla.goal;

import com.tutorial.oscarmod.entity.mob.godzilla.GodzillaEntity;
import com.tutorial.oscarmod.entity.mob.godzilla.entity.GodzillaFireBall;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GodzillaShootFireBallGoal extends Goal{
    
    private final GodzillaEntity godzilla;
    public int chargeTime;

    public GodzillaShootFireBallGoal(GodzillaEntity p_32776_) {
        this.godzilla = p_32776_;
    }

    public boolean canUse() {
        return this.godzilla.getTarget() != null
                && !this.godzilla.isOnMeleeAttackAnimation(); // 且目前沒有播放普通攻擊動畫
    }

    public void start() {
        this.chargeTime = 0;
    }

    public void stop() {
        this.godzilla.setCharging(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.godzilla.getTarget();
        if (livingentity != null) {
            double d0 = 64.0D;
            if (livingentity.distanceToSqr(this.godzilla) < 4096.0D && this.godzilla.hasLineOfSight(livingentity)) {
                Level level = this.godzilla.level;
                ++this.chargeTime;
                if (this.chargeTime == 10 && !this.godzilla.isSilent()) {
                    level.levelEvent((Player)null, 1015, this.godzilla.blockPosition(), 0);
                }

                if (this.chargeTime == 20) {
                    double d1 = 4.0D;
                    Vec3 vec3 = this.godzilla.getViewVector(1.0F);
                    double d2 = livingentity.getX() - (this.godzilla.getX() + vec3.x * 4.0D);
                    double d3 = livingentity.getY(0.5D) - (0.5D + this.godzilla.getY(0.5D));
                    double d4 = livingentity.getZ() - (this.godzilla.getZ() + vec3.z * 4.0D);
                    if (!this.godzilla.isSilent()) {
                        level.levelEvent((Player)null, 1016, this.godzilla.blockPosition(), 0);
                    }

                    int explosionPower = 1;
                    GodzillaFireBall godzillaFireBall = new GodzillaFireBall(level, this.godzilla, d2, d3, d4, explosionPower);
                    godzillaFireBall.setPos(this.godzilla.getX() + vec3.x * 4.0D, this.godzilla.getY(0.5D) + 0.5D, godzillaFireBall.getZ() + vec3.z * 4.0D);
                    level.addFreshEntity(godzillaFireBall);
                    this.chargeTime = -120; // 延長 充能時間
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }

            this.godzilla.setCharging(this.chargeTime > 10);
        }
    }
}
