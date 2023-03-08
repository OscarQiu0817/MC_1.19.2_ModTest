package com.tutorial.oscarmod.entity.mob.godzilla.goal;

import com.tutorial.oscarmod.entity.mob.godzilla.GodzillaEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GodzillaMeleeAttackGoal extends MeleeAttackGoal {

//    protected PathfinderMob mob;
    protected GodzillaEntity mob;
    private double speedModifier;
    private boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private final int attackInterval = 90;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 90L;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    private double[] livingEntityPostion = null;

    public GodzillaMeleeAttackGoal(GodzillaEntity p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_,p_25553_,p_25554_);
        this.mob = p_25552_;
        this.speedModifier = p_25553_;
        this.followingTargetEvenIfNotSeen = p_25554_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.mob.level.getGameTime();
        if (i - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.mob.getNavigation().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.mob.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    // 攻擊可達到的距離 是否 >= 與目標的距離
                    return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget((LivingEntity)null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
//        System.out.println("ticksUntilNextAttack-0 now is : " + ticksUntilNextAttack);
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if (this.mob.getNavigation().getPath() != null) {
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

//            System.out.println("ticksUntilNextAttack-1 now is : " + ticksUntilNextAttack);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
//            System.out.println("ticksUntilNextAttack-2 now is : " + ticksUntilNextAttack);

            this.checkAndPerformAttack(livingentity, d0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
        double d0 = this.getAttackReachSqr(p_25557_);
//        System.out.println("p_25558_ <= d0 : " + (p_25558_ <= d0));
//        System.out.println("ticksUntilNextAttack now is : " + ticksUntilNextAttack);
        if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);

            // 攻擊動畫沒開始前才紀錄位置
            if( ! this.mob.isOnMeleeAttackAnimation() ){
                System.out.println( "not on animation , save entity position");
                livingEntityPostion = new double[3];
                livingEntityPostion[0] = p_25557_.getX();
                livingEntityPostion[1] = p_25557_.getY();
                livingEntityPostion[2] = p_25557_.getZ();
                this.mob.setLivingEntityPostion(livingEntityPostion);

                // 理論上應執行，雖不確定客戶端正在播放，但這個參數可以拿來控制火球不被同時觸發
                this.mob.setOnMeleeAttackAnimation(true);
            }
        }
    }

    public void resetLivingEntityPostion(){
        this.livingEntityPostion = null;
    }

    protected void resetAttackCooldown() {
//        System.out.println("resetAttackCooldown");
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackInterval);
//        System.out.println("ticksUntilNextAttack now is : " + ticksUntilNextAttack);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(attackInterval);
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
//        System.out.println(this.mob.getBbWidth());  // 8.0
//        System.out.println(p_25556_.getBbWidth()); // 村民 0.3
//        System.out.println(p_25556_.getClass()); // class net.minecraft.world.entity.npc.Villager
        // 256.3
//        System.out.println(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth());
        // 只要寬度兩倍就夠了
//        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth());
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 1.15F + p_25556_.getBbWidth());
    }


}
