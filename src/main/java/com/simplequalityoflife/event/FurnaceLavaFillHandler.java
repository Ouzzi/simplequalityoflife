package com.simplequalityoflife.event;

import com.simplequalityoflife.Simplequalityoflife;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FurnaceLavaFillHandler {

    // Öfen/Schmelzöfen/Räucheröfen nutzen Slot 1 als Brennstoff-Slot.
    // AbstractFurnaceBlockEntity.FUEL_SLOT_INDEX ist 'protected', daher hier als Konstante gespiegelt.
    private static final int FUEL_SLOT = 1;

    public static void register() {
        UseBlockCallback.EVENT.register(FurnaceLavaFillHandler::onRightClickBlock);
    }

    private static ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (!Simplequalityoflife.getConfig().qOL.enableFurnaceLavaFill) return ActionResult.PASS;

        // Item der genutzten Hand prüfen (funktioniert für Haupt- und Nebenhand).
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() != Items.LAVA_BUCKET) return ActionResult.PASS;

        // Schleichen lässt das normale Item-Verhalten zu (z.B. Lava bewusst neben dem Ofen platzieren).
        if (player.isSneaking()) return ActionResult.PASS;

        BlockPos pos = hitResult.getBlockPos();
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof AbstractFurnaceBlockEntity furnace)) return ActionResult.PASS;

        // Verschlossene Öfen (Lock-Component) nicht umgehen -> normales Verhalten (Vanilla prüft den Schlüssel).
        if (furnace.isLocked()) return ActionResult.PASS;

        // Behandelt werden zwei Fälle: der Brennstoff-Slot ist leer ODER er enthält genau einen
        // übrig gebliebenen leeren Eimer (Rest vom letzten Lava-Verbrennen). Dann wird getauscht.
        // Alles andere (Kohle, bereits Lava, gestapelte Eimer) -> normales Verhalten (GUI öffnen).
        ItemStack fuel = furnace.getStack(FUEL_SLOT);
        boolean emptySlot = fuel.isEmpty();
        boolean leftoverBucket = fuel.getItem() == Items.BUCKET && fuel.getCount() == 1;
        if (!emptySlot && !leftoverBucket) return ActionResult.PASS;

        // Server-autoritativ: Auf dem Client nur die Interaktion beanspruchen,
        // damit sich die Ofen-GUI nicht öffnet. Die eigentliche Logik läuft serverseitig.
        if (world.isClient()) return ActionResult.SUCCESS;

        // Vorherigen Inhalt sichern (leer oder genau 1 leerer Eimer), dann den Lava-Eimer einsetzen.
        ItemStack previousFuel = fuel.copy();
        furnace.setStack(FUEL_SLOT, new ItemStack(Items.LAVA_BUCKET));
        furnace.markDirty();

        if (!player.isCreative()) {
            // Gehaltenen Lava-Eimer verbrauchen ...
            stack.decrement(1);
            // ... und einen evtl. im Ofen liegenden leeren Eimer zurückgeben (Swap, kein Dupe).
            // Bei leerem Slot kommt nichts zurück – der leere Eimer erscheint nach dem Verbrennen im Ofen.
            if (!previousFuel.isEmpty()) {
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, previousFuel);
                } else if (!player.getInventory().insertStack(previousFuel)) {
                    player.dropItem(previousFuel, false);
                }
            }
        }

        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0f, 1.0f);
        player.swingHand(hand, true);

        return ActionResult.SUCCESS;
    }
}
