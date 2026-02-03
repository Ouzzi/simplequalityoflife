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

    // Diese Enum muss public sein, damit Cloth Config sie lesen kann
    public enum SlideActivationMode {
        CAMERA, ALWAYS
    }

    // Option außerhalb der QOL-Kategorie (Global)
    @ConfigEntry.Gui.Tooltip
    public boolean frostWalkerWalkOnPowderSnow = true;

    @ConfigEntry.Gui.CollapsibleObject
    public QOL qOL = new QOL();

    public static class QOL {
        // --- Movement & AutoWalk ---
        @ConfigEntry.Gui.Tooltip
        public boolean enableAutowalk = false;

        // --- Sound & Visuals ---

        // WICHTIG: Das fehlte! Wird für Entity-ID Muting benötigt (z.B. ["minecraft:cow"])
        @ConfigEntry.Gui.Tooltip
        public List<String> mutedEntities = new ArrayList<>();

        @ConfigEntry.Gui.Tooltip
        public List<String> nametagMuteSuffixes = new ArrayList<>(Arrays.asList("_mute", "_shhh"));

        // NEU: Baby Suffixes
        @ConfigEntry.Gui.Tooltip
        public List<String> nametagBabySuffixes = new ArrayList<>(Arrays.asList("_baby", "_small"));

        // --- Farming & World Interaction ---
        @ConfigEntry.Gui.Tooltip
        public boolean preventFarmlandTrampleWithFeatherFalling = true;

        @ConfigEntry.Gui.Tooltip
        public boolean sharpnessCutsGrass = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableHoeHarvest = true;

        // --- Ladder Mechanics ---
        @ConfigEntry.Gui.Tooltip
        public double ladderClimbingSpeed = 0.4; // Vanilla default is ~0.2

        @ConfigEntry.Gui.Tooltip
        public boolean enableFastLadderSlide = true;

        @ConfigEntry.Gui.Tooltip
        public double ladderSlideSpeed = 0.8;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public SlideActivationMode ladderSlideActivation = SlideActivationMode.CAMERA;

        // --- Vaults ---
        @ConfigEntry.Gui.Tooltip
        public int vaultCooldownDays = 100;

        // --- Durability Mechanics ---
        @ConfigEntry.Gui.Tooltip
        public boolean enableFullDurabilityBonus = true;

        @ConfigEntry.Gui.Tooltip
        public double fullDurabilityThreshold = 0.80;

        @ConfigEntry.Gui.Tooltip
        public double fullDurabilityBonusMultiplier = 1.5;

        // --- Piglins ---
        @ConfigEntry.Gui.Tooltip
        public boolean piglinsIgnoreGoldTrims = true;

        @ConfigEntry.Gui.Tooltip
        public boolean piglinsIgnoreGoldTools = true;

        // --- Weather ---
        @ConfigEntry.Gui.Tooltip
        public boolean disableWeather = false;

        @ConfigEntry.Gui.Tooltip
        public int clientRainParticleDensity = 20;

    }
}