package cintlex.mvd.mixin;

import cintlex.mvd.MaxedVanillaDifficulty;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalDifficulty.class)
public class MaxedVanillaDifficultyMixin {

    @Shadow @Final private Difficulty globalDifficulty;
    private static boolean loggedeasy = false;
    private static boolean loggednormal = false;
    private static boolean loggedhard = false;
    private static boolean loggedclamped = false;

    @Inject(method = "getLocalDifficulty", at = @At("RETURN"), cancellable = true)
    private void setmaxregional(CallbackInfoReturnable<Float> cir) {
        if (!MaxedVanillaDifficulty.CONFIG.configregional) {
            return;
        }

        float originalregional = cir.getReturnValue();
        float maxregional;
        boolean log = false;
        switch (this.globalDifficulty) {
            case EASY:
                maxregional = 1.5f;
                log = !loggedeasy && originalregional != maxregional;
                if (log) loggedeasy = true;
                break;
            case NORMAL:
                maxregional = 4.0f;
                log = !loggednormal && originalregional != maxregional;
                if (log) loggednormal = true;
                break;
            case HARD:
                maxregional = 6.75f;
                log = !loggedhard && originalregional != maxregional;
                if (log) loggedhard = true;
                break;
            default:
                return;
        }

        if (log) {
            MaxedVanillaDifficulty.LOGGER.info("Regional Difficulty set to {}", maxregional);
        }

        cir.setReturnValue(maxregional);
    }

    @Inject(method = "getClampedLocalDifficulty", at = @At("RETURN"), cancellable = true)
    private void setmaxclampedregional(CallbackInfoReturnable<Float> cir) {
        if (!MaxedVanillaDifficulty.CONFIG.configclampedregional) {
            return;
        }

        float originalclamped = cir.getReturnValue();
        float maxclamped;
        if (this.globalDifficulty == Difficulty.EASY) {
            maxclamped = 0.0f;
        } else {
            maxclamped = 1.0f;
        }
        if (!loggedclamped && originalclamped != maxclamped) {
            MaxedVanillaDifficulty.LOGGER.info("Clamped Regional Difficulty set to {}", maxclamped);
            loggedclamped = true;
        }

        cir.setReturnValue(maxclamped);
    }
}