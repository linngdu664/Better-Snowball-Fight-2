package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.*;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.*;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballShotgunItem extends Item {
    private double pushRank;
    public SnowballShotgunItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1).durability(256));
    }

    public LaunchFunc getLaunchFunc(){
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.SHOTGUN;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = 1.51F;
            }
        };
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        pushRank=0.24;
        int i;
        for (i = 0; i < 4; i++) {
            ItemStack itemStack = BSFUtil.findAmmo(player, false, true);
            if (itemStack != null) {
                boolean isAmmoTank = BSFUtil.isAmmoTank(itemStack.getItem(), true);
                BSFSnowballEntity snowballEntity = itemToEntity(itemStack, level, player);
                assert snowballEntity != null;
                BSFUtil.shootFromRotation(snowballEntity,player.getXRot(), player.getYRot(), 0.0F, 2.0F, 10.0F);
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                if (!player.isShiftKeyDown()){
                    level.addFreshEntity(snowballEntity);
                }
                if (!player.getAbilities().instabuild) {
                    if (isAmmoTank) {
                        itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                        if (itemStack.getDamageValue() == 96) {
                            itemStack.shrink(1);
                            player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get(), 1), true);
                        }
                    } else {
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            player.getInventory().removeItem(itemStack);
                        }
                    }
                }
            }else {
                break;
            }
        }
        if (i > 0) {
            float pitch = player.getXRot();
            float yaw = player.getYRot();
            Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
            if (!player.isShiftKeyDown()){
                if (level.isClientSide()) {
                    player.push(-0.24 * cameraVec.x, -0.24 * cameraVec.y, -0.24 * cameraVec.z);
                }
                if (!level.isClientSide()) {
                    //add particles
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y , player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.16);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_2.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }else{
                if (level.isClientSide()) {
                    player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
                }
                if (!level.isClientSide()) {
                    //add particles
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y , player.getZ() + cameraVec.z, (int) (29 * pushRank + 9.04), 0, 0, 0, 0.32);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_1.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        return super.use(level, player, usedHand);



//        if (!player.isShiftKeyDown()) {
//            int i;
//            for (i = 0; i < 4; i++) {
//                ItemStack itemStack = BSFUtil.findAmmo(player, false);
//                if (itemStack != null) {
//                    boolean k = BSFUtil.isAmmoTank(itemStack, true);
//                    AdvancedSnowballEntity snowballEntity;
//                    if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.COMPACTED);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.STONE, 3.0F, 4.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.GLASS, 4.0F, 5.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.IRON, 5.0F, 7.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.ICE, 4.0F, 6.0F);
//                        snowballEntity.frozenTicks = 60;
//                        snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.GOLD, 6.0F, 7.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.OBSIDIAN, 7.0F, 8.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get() || itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.SPECTRAL);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.EXPLOSIVE, 3.0F, 5.0F);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_MONSTER);
//                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_MONSTER_DAMAGE, 4.0F, 6.0F);
//                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
//                    } else if (itemStack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_MONSTER_EXPLOSIVE, 3.0F, 5.0F);
//                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_PLAYER);
//                        snowballEntity.setMissilesTracking(Player.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get()));
//                    } else if (itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get()) {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_PLAYER_DAMAGE, 4.0F, 6.0F);
//                        snowballEntity.setMissilesTracking(Player.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
//                    } else {
//                        snowballEntity = new AdvancedSnowballEntity(level, player, TankType.TRACKING_PLAYER_EXPLOSIVE, 3.0F, 5.0F);
//                        snowballEntity.setMissilesTracking(Player.class, 20, true);
//                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
//                    }
//                    snowballEntity.punch = 1.51F;
//
//                    BSFUtil.shootFromRotation(snowballEntity,player.getXRot(), player.getYRot(), 0.0F, 2.0F, 10.0F);
//
//                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
//                    if (!player.isShiftKeyDown()){
//                        level.addFreshEntity(snowballEntity);
//                    }
//
//                    if (!player.getAbilities().instabuild) {
//                        if (k) {
//                            itemStack.setDamageValue(itemStack.getDamageValue() + 1);
//                            if (itemStack.getDamageValue() == 96) {
//                                itemStack.shrink(1);
//                                player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get(), 1), true);
//                            }
//                        } else {
//                            itemStack.shrink(1);
//                            if (itemStack.isEmpty()) {
//                                player.getInventory().removeItem(itemStack);
//                            }
//                        }
//                    }
//                } else {
//                    break;
//                }
//            }
//            if (i > 0) {
//                float pitch = player.getXRot();
//                float yaw = player.getYRot();
//                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
//                if (!player.isShiftKeyDown()){
//                    if (level.isClientSide()) {
//                        player.push(-0.24 * cameraVec.x, -0.24 * cameraVec.y, -0.24 * cameraVec.z);
//                    }
//                    if (!level.isClientSide()) {
//                        //add particles
//                        ServerLevel serverLevel = (ServerLevel) level;
//                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y , player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.16);
//                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_2.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
//
//                    }
//                }else{
//                    if (level.isClientSide()) {
//                        player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
//                    }
//                    if (!level.isClientSide()) {
//                        //add particles
//                        ServerLevel serverLevel = (ServerLevel) level;
//                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getY() + cameraVec.y + 1.8, player.getZ() + cameraVec.z, (int) (29 * pushRank + 9.04), 0, 0, 0, 0.16);
//                    }
//                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_1.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
//                }
//            }
//        } else {
//            double pushRank = 0.24;
//            int i;
//            for (i = 0; i < 4; i++) {
//                ItemStack itemStack = BSFUtil.findAmmo(player, false);
//                if (itemStack != null) {
//                    boolean k = BSFUtil.isAmmoTank(itemStack, true);
//                    if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() ||
//                            itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
//                            itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.10;
//                    } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.12;
//                    } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.12;
//                    } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
//                            itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() ||
//                            itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get()) {
//                        pushRank += 0.16;
//                    } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.12;
//                    } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.18;
//                    } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.18;
//                    } else if (itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get() || itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
//                        pushRank += 0.10;
//                    } else {
//                        pushRank += 0.42;
//                    }
//
//                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
//                    if (!player.getAbilities().instabuild) {
//                        if (k) {
//                            itemStack.setDamageValue(itemStack.getDamageValue() + 1);
//                            if (itemStack.getDamageValue() == 96) {
//                                itemStack.shrink(1);
//                                player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get(), 1), true);
//                            }
//                        } else {
//                            itemStack.shrink(1);
//                            if (itemStack.isEmpty()) {
//                                player.getInventory().removeItem(itemStack);
//                            }
//                        }
//                    }
//                } else {
//                    break;
//                }
//            }
//            if (i > 0) {
//                float pitch = player.getXRot();
//                float yaw = player.getYRot();
//                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
//                if (level.isClientSide()) {
//                    player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
//                }
//                if (!level.isClientSide()) {
//                    //add particles
//                    ServerLevel serverLevel = (ServerLevel) level;
//                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getY() + cameraVec.y + 1.8, player.getZ() + cameraVec.z, (int) (29 * pushRank + 9.04), 0, 0, 0, 0.16);
//                }
//                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_1.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
//            }
//        }


        //
    }



    private BSFSnowballEntity itemToEntity(ItemStack itemStack, Level level, Player player){
        Item item=itemStack.getItem();
        if(item==ItemRegister.COMPACTED_SNOWBALL.get()||item==ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.10;
            return new CompactedSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.STONE_SNOWBALL.get()||item==ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.12;
            return new StoneSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.GLASS_SNOWBALL.get()||item==ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.12;
            return new GlassSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.IRON_SNOWBALL.get()||item==ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.16;
            return new IronSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.ICE_SNOWBALL.get()||item==ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.12;
            return new IceSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.GOLD_SNOWBALL.get()||item==ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new GoldSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.OBSIDIAN_SNOWBALL.get()||item==ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new ObsidianSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.EXPLOSIVE_SNOWBALL.get()||item==ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.42;
            return new ExplosiveSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.SPECTRAL_SNOWBALL.get()||item==ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.10;
            return new SpectralSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get()||item==ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new LightMonsterTrackingSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get()||item==ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new HeavyMonsterTrackingSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()||item==ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.42;
            return new ExplosiveMonsterTrackingSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get()||item==ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new LightPlayerTrackingSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get()||item==ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.18;
            return new HeavyPlayerTrackingSnowballEntity(player,level,getLaunchFunc());
        }else if(item==ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()||item==ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()){
            pushRank += 0.42;
            return new ExplosivePlayerTrackingSnowballEntity(player,level,getLaunchFunc());
        }
        return null;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_shotgun.tooltip").withStyle(ChatFormatting.GRAY));
    }

}
