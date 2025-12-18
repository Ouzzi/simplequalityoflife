package com.simplequalityoflife.config;

import com.simplequalityoflife.Simplequalityoflife;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = Simplequalityoflife.MOD_ID)
public class SimplequalityoflifeConfig implements ConfigData {


    @ConfigEntry.Gui.CollapsibleObject
    public QOL qOL = new QOL();
    public enum SlideActivationMode {
        CAMERA, ALWAYS
    }


    public static class QOL {
        @ConfigEntry.Gui.Tooltip
        public boolean enableAutowalk = true;

        @ConfigEntry.Gui.Tooltip
        public List<String> nametagMuteSuffixes = new ArrayList<>(Arrays.asList("_mute", "_shhh"));

        // NEU: Baby Suffixes
        @ConfigEntry.Gui.Tooltip
        public List<String> nametagBabySuffixes = new ArrayList<>(Arrays.asList("_baby", "_small"));

        @ConfigEntry.Gui.Tooltip
        public boolean preventFarmlandTrampleWithFeatherFalling = true;

        @ConfigEntry.Gui.Tooltip
        public double ladderClimbingSpeed = 0.4; // Vanilla default is ~0.2

        @ConfigEntry.Gui.Tooltip
        public boolean enableFastLadderSlide = true;

        @ConfigEntry.Gui.Tooltip
        public double ladderSlideSpeed = 0.8;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public SlideActivationMode ladderSlideActivation = SlideActivationMode.CAMERA;

        @ConfigEntry.Gui.Tooltip
        public boolean sharpnessCutsGrass = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableHoeHarvest = true;

        @ConfigEntry.Gui.Tooltip
        public int vaultCooldownDays = 1;

        @ConfigEntry.Gui.Tooltip
        public boolean enableFullDurabilityBonus = true;

        @ConfigEntry.Gui.Tooltip
        public double fullDurabilityThreshold = 0.90; // Ab 90% Haltbarkeit

        @ConfigEntry.Gui.Tooltip
        public double fullDurabilityBonusMultiplier = 1.15; // 15% Bonus (Efficiency & Damage)

    }
}