package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin extends LivingEntity {

    protected PassiveEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract int getBreedingAge();
    @Shadow public abstract void setBreedingAge(int age);

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        if (this.getEntityWorld().isClient()) return;

        if (this.age % 100 != 0) return;
        if (!this.hasCustomName()) return;

        Text customName = this.getCustomName();
        if (customName == null) return;

        String name = customName.getString();
        List<String> suffixes = Simplequalityoflife.getConfig().qOL.nametagBabySuffixes;

        // Prüfen, ob der Name mit einem der Suffixe endet
        for (String suffix : suffixes) {
            // null/leer überspringen: name.endsWith(null) wirft NPE, endsWith("") wäre immer true
            if (suffix == null || suffix.isEmpty()) continue;
            if (name.endsWith(suffix)) {
                // Auf Baby-Alter (-24000) halten. WICHTIG: NICHT auf "ist gerade ein Baby" beschränken –
                // sonst kann ein Tier, das zwischen zwei Checks (alle 100 Ticks) erwachsen wird, dauerhaft
                // entkommen. So wird es notfalls wieder auf Baby zurückgesetzt.
                if (this.getBreedingAge() > -24000) {
                    this.setBreedingAge(-24000);
                }
                return;
            }
        }
    }
}