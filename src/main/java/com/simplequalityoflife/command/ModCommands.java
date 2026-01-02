package com.simplequalityoflife.command;

import com.mojang.brigadier.arguments.*;
import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.config.SimplequalityoflifeConfig;
import com.simplequalityoflife.util.CrawlAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            dispatcher.register(CommandManager.literal("crawl")
                    .executes(ctx -> {
                        if (ctx.getSource().getEntity() instanceof PlayerEntity player) {
                            CrawlAccessor crawler = (CrawlAccessor) player;
                            boolean newState = !crawler.simpleQualityOfLife$isCrawling();
                            crawler.simpleQualityOfLife$setCrawling(newState);
                            // Kein Feedback im Chat, um Spam beim Drücken der Taste zu vermeiden
                        }
                        return 1;
                    }));


            // --- CONFIG COMMANDS (simplequalityoflife) ---
            dispatcher.register(CommandManager.literal("simplequalityoflife")
                    // Level 4 für Admin-Befehle (Config Änderungen)
                    .requires(source -> checkPermission(source, 4))
                    // 2. Vaults
                    .then(CommandManager.literal("vaults")
                            .then(CommandManager.literal("cooldown")
                                    .then(CommandManager.argument("days", IntegerArgumentType.integer(0))
                                            .executes(ctx -> {
                                                int val = IntegerArgumentType.getInteger(ctx, "days");
                                                Simplequalityoflife.getConfig().qOL.vaultCooldownDays = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Vault Cooldown set to: " + val + " days"), true);
                                                return 1;
                                            })))
                    )

                    // 8. Tweaks (Alle Features)
                    .then(CommandManager.literal("tweaks")
                            .then(CommandManager.literal("fullDurabilityBonus")
                                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                boolean val = BoolArgumentType.getBool(ctx, "enabled");
                                                Simplequalityoflife.getConfig().qOL.enableFullDurabilityBonus = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Full Durability Bonus enabled: " + val), true);
                                                return 1;
                                            })))
                            // AutoWalk
                            .then(CommandManager.literal("autowalk")
                                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                boolean val = BoolArgumentType.getBool(ctx, "enabled");
                                                Simplequalityoflife.getConfig().qOL.enableAutowalk = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Auto-Walk enabled: " + val), true);
                                                return 1;
                                            })))
                            // Farmland Protect
                            .then(CommandManager.literal("farmlandProtect")
                                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                boolean val = BoolArgumentType.getBool(ctx, "enabled");
                                                Simplequalityoflife.getConfig().qOL.preventFarmlandTrampleWithFeatherFalling = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Farmland Feather Falling Protection: " + val), true);
                                                return 1;
                                            })))
                            // Ladder Speed
                            .then(CommandManager.literal("ladderSpeed")
                                    .then(CommandManager.argument("speed", DoubleArgumentType.doubleArg(0.0))
                                            .executes(ctx -> {
                                                double val = DoubleArgumentType.getDouble(ctx, "speed");
                                                Simplequalityoflife.getConfig().qOL.ladderClimbingSpeed = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Ladder Climbing Speed set to: " + val), true);
                                                return 1;
                                            })))
                            // Sharpness Cut
                            .then(CommandManager.literal("sharpnessCut")
                                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                boolean val = BoolArgumentType.getBool(ctx, "enabled");
                                                Simplequalityoflife.getConfig().qOL.sharpnessCutsGrass = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Sharpness Cuts Grass: " + val), true);
                                                return 1;
                                            })))
                            // Mute Suffixes
                            .then(CommandManager.literal("muteSuffixes")
                                    .then(CommandManager.literal("list")
                                            .executes(ctx -> {
                                                List<String> list = Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes;
                                                ctx.getSource().sendFeedback(() -> Text.literal("Mute Suffixes: " + list.toString()).formatted(Formatting.YELLOW), false);
                                                return list.size();
                                            }))
                                    .then(CommandManager.literal("add")
                                            .then(CommandManager.argument("suffix", StringArgumentType.string())
                                                    .executes(ctx -> {
                                                        String suffix = StringArgumentType.getString(ctx, "suffix");
                                                        List<String> list = Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes;
                                                        if (!list.contains(suffix)) {
                                                            list.add(suffix);
                                                            saveConfig();
                                                            ctx.getSource().sendFeedback(() -> Text.literal("Added suffix: " + suffix).formatted(Formatting.GREEN), true);
                                                        } else {
                                                            ctx.getSource().sendError(Text.literal("Suffix already exists."));
                                                        }
                                                        return 1;
                                                    })))
                                    .then(CommandManager.literal("remove")
                                            .then(CommandManager.argument("suffix", StringArgumentType.string())
                                                    .suggests((context, builder) -> CommandSource.suggestMatching(Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes, builder))
                                                    .executes(ctx -> {
                                                        String suffix = StringArgumentType.getString(ctx, "suffix");
                                                        List<String> list = Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes;
                                                        if (list.remove(suffix)) {
                                                            saveConfig();
                                                            ctx.getSource().sendFeedback(() -> Text.literal("Removed suffix: " + suffix).formatted(Formatting.GREEN), true);
                                                        } else {
                                                            ctx.getSource().sendError(Text.literal("Suffix not found."));
                                                        }
                                                        return 1;
                                                    })))
                                    .then(CommandManager.literal("clear")
                                            .executes(ctx -> {
                                                Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes.clear();
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Cleared all mute suffixes.").formatted(Formatting.RED), true);
                                                return 1;
                                            }))
                                    .then(CommandManager.literal("hoeHarvest")
                                            .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                                    .executes(ctx -> {
                                                        boolean val = BoolArgumentType.getBool(ctx, "enabled");
                                                        Simplequalityoflife.getConfig().qOL.enableHoeHarvest = val;
                                                        saveConfig();
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Hoe Harvest & Replant: " + val), true);
                                                        return 1;
                                                    })))
                            )
                            .then(CommandManager.literal("babySuffixes")
                                    .then(CommandManager.literal("list")
                                            .executes(ctx -> {
                                                List<String> list = Simplequalityoflife.getConfig().qOL.nametagBabySuffixes;
                                                ctx.getSource().sendFeedback(() -> Text.literal("Baby Suffixes: " + list.toString()).formatted(Formatting.YELLOW), false);
                                                return list.size();
                                            }))
                                    .then(CommandManager.literal("add")
                                            .then(CommandManager.argument("suffix", StringArgumentType.string())
                                                    .executes(ctx -> {
                                                        String suffix = StringArgumentType.getString(ctx, "suffix");
                                                        List<String> list = Simplequalityoflife.getConfig().qOL.nametagBabySuffixes;
                                                        if (!list.contains(suffix)) {
                                                            list.add(suffix);
                                                            saveConfig();
                                                            ctx.getSource().sendFeedback(() -> Text.literal("Added baby suffix: " + suffix).formatted(Formatting.GREEN), true);
                                                        } else {
                                                            ctx.getSource().sendError(Text.literal("Suffix already exists."));
                                                        }
                                                        return 1;
                                                    })))
                                    .then(CommandManager.literal("remove")
                                            .then(CommandManager.literal("remove")
                                                    .then(CommandManager.argument("suffix", StringArgumentType.string())
                                                            .suggests((context, builder) -> CommandSource.suggestMatching(Simplequalityoflife.getConfig().qOL.nametagBabySuffixes, builder))
                                                            .executes(ctx -> {
                                                                String suffix = StringArgumentType.getString(ctx, "suffix");
                                                                List<String> list = Simplequalityoflife.getConfig().qOL.nametagBabySuffixes;
                                                                if (list.remove(suffix)) {
                                                                    saveConfig();
                                                                    ctx.getSource().sendFeedback(() -> Text.literal("Removed baby suffix: " + suffix).formatted(Formatting.GREEN), true);
                                                                } else {
                                                                    ctx.getSource().sendError(Text.literal("Suffix not found."));
                                                                }
                                                                return 1;
                                                            })))
                                    )
                            )

                    )

            );
        });
    }

    private static void saveConfig() {
        AutoConfig.getConfigHolder(SimplequalityoflifeConfig.class).save();
    }

    private static boolean checkPermission(ServerCommandSource source, int level) {
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            return source.getServer().getPlayerManager().isOperator(player.getPlayerConfigEntry());
        }
        return true;
    }
}