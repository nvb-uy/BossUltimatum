package elocindev.bossultimatum.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.faux.customentitydata.api.CustomDataHelper;

import elocindev.bossultimatum.config.Configs;
import elocindev.bossultimatum.config.entries.UltimatumConfig;
import elocindev.bossultimatum.config.entries.UltimatumConfig.Ultimatum;
import elocindev.necronomicon.api.NecUtilsAPI;
import elocindev.bossultimatum.config.entries.UltimatumConfig.InnerHealingConfig;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        UltimatumConfig cfg = Configs.MAIN;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld world = player.getServerWorld();

        for (Ultimatum ultimatum : cfg.ultimatums) {
            if (ultimatum.entity_regex != null && damageSource.getAttacker() instanceof LivingEntity attacker) {
                if (ultimatum.direct_kill && NecUtilsAPI.getEntityIdentifier(attacker).toString().matches(ultimatum.entity_regex)) {
                    applyUltimatumLogic(attacker, ultimatum, world);
                } else {
                    world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(ultimatum.indirect_radius), entity -> entity.getHealth() >= ultimatum.minimum_hp)
                    .forEach(entity -> {
                        if (NecUtilsAPI.getEntityIdentifier(entity).toString().matches(ultimatum.entity_regex)) {
                            applyUltimatumLogic(entity, ultimatum, world);
                        }
                    });
                }
            }
        }
    }

    private void applyUltimatumLogic(LivingEntity boss, Ultimatum ultimatum, ServerWorld world) {
        setDeathCount(boss, getDeathCount(boss)+1);

        for (InnerHealingConfig healingConfig : ultimatum.healing_events) {            
            if (healingConfig.require_difficulty && !healingConfig.difficulty.contains(world.getDifficulty().getName())) {
                continue;
            }

            //?if fabric {
            if (healingConfig.ultimatum_death_count > 0 && getDeathCount(boss) < healingConfig.ultimatum_death_count) {
                continue;
            }
            //?}

            if (healingConfig.ultimatum_heals_maxhp) {
                boss.setHealth(boss.getMaxHealth() * healingConfig.ultimatum_death_healing);
            } else {
                boss.heal(healingConfig.ultimatum_death_healing);
            }

            if (healingConfig.remove_boss_instead_of_healing) {
                boss.remove(RemovalReason.DISCARDED);
            }
        }
    }

    //?if fabric {
    private void setDeathCount(LivingEntity boss, int deaths) {
        var data = CustomDataHelper.getCustomData(boss);

        data.putInt("bossultimatum_deaths", deaths);

        CustomDataHelper.setCustomData(boss, data);
    }

    private int getDeathCount(LivingEntity boss) {
        var data = CustomDataHelper.getCustomData(boss);

        if (data.contains("bossultimatum_deaths")) {
            return data.getInt("bossultimatum_deaths");
        }

        return 0;
    }
    //?}
}
