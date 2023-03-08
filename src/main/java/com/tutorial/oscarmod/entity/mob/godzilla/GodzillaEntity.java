package com.tutorial.oscarmod.entity.mob.godzilla;

import com.tutorial.oscarmod.entity.mob.godzilla.goal.GodzillaMeleeAttackGoal;
import com.tutorial.oscarmod.entity.mob.godzilla.goal.GodzillaShootFireBallGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;

public class GodzillaEntity extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation GODZILLA_ATTACK_1 = RawAnimation.begin().thenPlay("animation.godzilla.attack1");

    public GodzillaEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    // 普通攻擊動畫 4.5 秒 , 20 ticks 約 1 秒 , 這裡讓攻擊產生的爆炸延遲 4.5 秒 ( 90 ticks ) 後再觸發
    private int ticksToWaitMeleeAttackAnimationDone = 90;

    public double[] livingEntityPostion = null;

    // 預設是高度 * 0.85 如果位置不對再 override 方法自行調整
    protected float getStandingEyeHeight(Pose p_32741_, EntityDimensions p_32742_) {
        return p_32742_.height * 0.95F;
    }

    // 噴射火球的同步設定
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(GodzillaEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isCharging() {
        return this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean p_32759_) {
        this.entityData.set(DATA_IS_CHARGING, p_32759_);
    }

    // 近戰攻擊的同步設定 ( 這種方式 只能從 server 同步到 client ... 所以在 client 觸發的動畫沒辦法帶回來 )
    private static final EntityDataAccessor<Boolean> IS_ON_MELEE_ATTACK_ANIMATION = SynchedEntityData.defineId(GodzillaEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isOnMeleeAttackAnimation() {
        return this.entityData.get(IS_ON_MELEE_ATTACK_ANIMATION);
    }

    public void setOnMeleeAttackAnimation(boolean p_32759_) {
        this.entityData.set(IS_ON_MELEE_ATTACK_ANIMATION, p_32759_);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ON_MELEE_ATTACK_ANIMATION, false);
        this.entityData.define(DATA_IS_CHARGING, false);
    }

    // 不受爆炸影響
    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster
                .createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

//    private PlayState predicate(AnimationState state){
//        if(state.isMoving()){
//           return state.setAndContinue(DefaultAnimations.WALK);
//        }
//
//        return state.setAndContinue(DefaultAnimations.IDLE);
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        System.out.println("this.level.isClientSide() B " + this.level.isClientSide());

        // AnimationController 建構所需要的 第四個參數 "AnimationStateHandler" 是一個 @FunctionalInterface ( 方法可做為參數 )
        // 內建一個 方法 PlayState handle(AnimationState<A> var1);
        // 當輸入的參數類型符合此方法的時候可簡寫為 lambda 表示

        // 版本 1
//        AnimationController.AnimationStateHandler handler = (state) -> {return state.setAndContinue(DefaultAnimations.IDLE);};
//        controllerRegistrar.add(new AnimationController<>(this, "idle", 5,handler));

//        版本 2
//        controllerRegistrar.add(new AnimationController<>(this, "idle", 5,
//                (state) -> state.setAndContinue(DefaultAnimations.IDLE)));

        // 版本 3  因為 predicate方法 回傳一個 PlayState 且參數為 AnimationState, 所以也可以被當作是 AnimationStateHandler.
//        AnimationController.AnimationStateHandler handler = (state) -> {
//            return predicate(state);
//        };
        // 版本 4 再簡化成 this::predicate
//        controllerRegistrar.add(
//                new AnimationController<>(this, "idle", 5,
//                        this::predicate));

        controllerRegistrar.add(
                DefaultAnimations.genericWalkIdleController(this),
//                DefaultAnimations.genericAttackAnimation(this, GODZILLA_ATTACK_1));

                // 讓攻擊動畫可以完整做完，用預設的一脫離 swinging 狀態就會 STOP.
                new AnimationController<>(this, "Attack", 5, state -> {

                    if (this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
                        state.getController().forceAnimationReset();
                        return state.setAndContinue(GODZILLA_ATTACK_1);
                    }
                    return PlayState.CONTINUE;
                }));


    }

    @Override
    public void tick() {

        super.tick();

//        System.out.println("this.level.isClientSide : " + this.level.isClientSide);
//        System.out.println( isOnMeleeAttackAnimation() );
//        System.out.println( livingEntityPostion );
        // 如果動畫開始 且 livingEntityPosition有值
        if(isOnMeleeAttackAnimation() && livingEntityPostion != null){
            Arrays.stream(livingEntityPostion).forEach(num -> System.out.print("living - " + num + " , "));
            this.ticksToWaitMeleeAttackAnimationDone = Math.max(this.ticksToWaitMeleeAttackAnimationDone - 1, 0);
            if(this.ticksToWaitMeleeAttackAnimationDone == 0){

                float f = 1.0F;
                // 如果直接用 target 的 xyz 因為爆炸傷害的計算有一條是透過距離
                // 距離為 0 的時候 套進公式裡面讓整個都變成0 反而在爆炸中心點不會受到傷害
                // 所以做一些偏移
                if (!this.level.isClientSide) {
                    System.out.println(livingEntityPostion[0]);
                    System.out.println(livingEntityPostion[1]);
                    System.out.println(livingEntityPostion[2]);
                    this.level.explode(this,
                            livingEntityPostion[0],
                            livingEntityPostion[1],
                            livingEntityPostion[2],
                            (float)3 * f, Level.ExplosionInteraction.MOB);
                }

                // 重設相關參數
                livingEntityPostion = null;
                setOnMeleeAttackAnimation(false);
                this.ticksToWaitMeleeAttackAnimationDone = 90;

            }
        }
    }

    public void setLivingEntityPostion(double[] livingEntityPostion){
        this.livingEntityPostion = livingEntityPostion;
    }

    public double[] getLivingEntityPostion(){
        return livingEntityPostion;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        /*定義攻擊種類與效果
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
        在這行代碼中，1.25D 是實體的移動速度，20 是以滴答為單位的最大攻擊間隔（因此大約每秒一次），10.0F 是它將射擊的最大距離。
        將 20 修改為 2 秒間隔的 40 或 3 秒間隔的 60。
         */
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // 允許實體進行近戰攻擊。
        this.goalSelector.addGoal(1, new GodzillaMeleeAttackGoal(this, 1.2D, false));
        // 6.0F = lookDistance
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(7, new GodzillaShootFireBallGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        // 攻擊策略
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, GodzillaEntity.class, true));

    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return super.hurt(p_21016_, p_21017_);
    }

}
