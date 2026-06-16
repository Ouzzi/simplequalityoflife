package com.simplequalityoflife.command;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.config.SimplequalityoflifeConfig;
import com.simplequalityoflife.util.CrawlAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
                    // Level 4 (OWNERS) für Admin-Befehle (Config Änderungen).
                    // Die 1.21.11-Permission-API nutzt PermissionChecks statt int-Level.
                    .requires(CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK))

                    // --- Vaults ---
                    .then(CommandManager.literal("vaults")
                            .then(CommandManager.literal("cooldown")
                                    .then(CommandManager.argument("days", IntegerArgumentType.integer(0))
                                            .executes(ctx -> {
                                                int val = IntegerArgumentType.getInteger(ctx, "days");
                                                Simplequalityoflife.getConfig().qOL.vaultCooldownDays = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Vault Cooldown set to: " + val + " days"), true);
                                                return 1;
                                            }))))

                    // --- Tweaks (Alle Features) ---
                    .then(CommandManager.literal("tweaks")
                            .then(boolTweak("fullDurabilityBonus", (c, v) -> { c.qOL.enableFullDurabilityBonus = v; }, "Full Durability Bonus enabled"))
                            .then(boolTweak("autowalk", (c, v) -> { c.qOL.enableAutowalk = v; }, "Auto-Walk enabled"))
                            .then(boolTweak("farmlandProtect", (c, v) -> { c.qOL.preventFarmlandTrampleWithFeatherFalling = v; }, "Farmland Feather Falling Protection"))
                            .then(boolTweak("frostWalkerSnow", (c, v) -> { c.frostWalkerWalkOnPowderSnow = v; }, "Frost Walker on Powder Snow"))
                            .then(boolTweak("hoeHarvest", (c, v) -> { c.qOL.enableHoeHarvest = v; }, "Hoe Harvest & Replant"))
                            .then(boolTweak("furnaceLava", (c, v) -> { c.qOL.enableFurnaceLavaFill = v; }, "Furnace Lava Fill"))
                            .then(boolTweak("sharpnessCut", (c, v) -> { c.qOL.sharpnessCutsGrass = v; }, "Sharpness Cuts Grass"))
                            .then(CommandManager.literal("ladderSpeed")
                                    .then(CommandManager.argument("speed", DoubleArgumentType.doubleArg(0.0))
                                            .executes(ctx -> {
                                                double val = DoubleArgumentType.getDouble(ctx, "speed");
                                                Simplequalityoflife.getConfig().qOL.ladderClimbingSpeed = val;
                                                saveConfig();
                                                ctx.getSource().sendFeedback(() -> Text.literal("Ladder Climbing Speed set to: " + val), true);
                                                return 1;
                                            })))
                            .then(suffixCommands("muteSuffixes",
                                    () -> Simplequalityoflife.getConfig().qOL.nametagMuteSuffixes,
                                    "mute suffix", "Mute Suffixes"))
                            .then(suffixCommands("babySuffixes",
                                    () -> Simplequalityoflife.getConfig().qOL.nametagBabySuffixes,
                                    "baby suffix", "Baby Suffixes"))
                    )
            );
        });
    }

    // Baut einen einfachen An/Aus-Schalter für ein Boolean-Feature.
    private static LiteralArgumentBuilder<ServerCommandSource> boolTweak(String name,
                                                                        BiConsumer<SimplequalityoflifeConfig, Boolean> setter,
                                                                        String label) {
        return CommandManager.literal(name)
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(ctx -> {
                            boolean val = BoolArgumentType.getBool(ctx, "enabled");
                            setter.accept(Simplequalityoflife.getConfig(), val);
                            saveConfig();
                            ctx.getSource().sendFeedback(() -> Text.literal(label + ": " + val), true);
                            return 1;
                        }));
    }

    // Baut den list/add/remove/clear-Unterbaum für eine Suffix-Liste.
    private static LiteralArgumentBuilder<ServerCommandSource> suffixCommands(String name,
                                                                             Supplier<List<String>> listSupplier,
                                                                             String labelSingular,
                                                                             String labelPlural) {
        return CommandManager.literal(name)
                .then(CommandManager.literal("list")
                        .executes(ctx -> {
                            List<String> list = listSupplier.get();
                            ctx.getSource().sendFeedback(() -> Text.literal(labelPlural + ": " + list).formatted(Formatting.YELLOW), false);
                            return 1;
                        }))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("suffix", StringArgumentType.string())
                                .executes(ctx -> {
                                    String suffix = StringArgumentType.getString(ctx, "suffix");
                                    List<String> list = listSupplier.get();
                                    if (list.contains(suffix)) {
                                        ctx.getSource().sendError(Text.literal("Suffix already exists."));
                                        return 0;
                                    }
                                    list.add(suffix);
                                    saveConfig();
                                    ctx.getSource().sendFeedback(() -> Text.literal("Added " + labelSingular + ": " + suffix).formatted(Formatting.GREEN), true);
                                    return 1;
                                })))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("suffix", StringArgumentType.string())
                                .suggests((context, builder) -> CommandSource.suggestMatching(listSupplier.get(), builder))
                                .executes(ctx -> {
                                    String suffix = StringArgumentType.getString(ctx, "suffix");
                                    if (listSupplier.get().remove(suffix)) {
                                        saveConfig();
                                        ctx.getSource().sendFeedback(() -> Text.literal("Removed " + labelSingular + ": " + suffix).formatted(Formatting.GREEN), true);
                                        return 1;
                                    }
                                    ctx.getSource().sendError(Text.literal("Suffix not found."));
                                    return 0;
                                })))
                .then(CommandManager.literal("clear")
                        .executes(ctx -> {
                            listSupplier.get().clear();
                            saveConfig();
                            ctx.getSource().sendFeedback(() -> Text.literal("Cleared all " + labelPlural.toLowerCase() + ".").formatted(Formatting.RED), true);
                            return 1;
                        }));
    }

    private static void saveConfig() {
        AutoConfig.getConfigHolder(SimplequalityoflifeConfig.class).save();
    }
}
