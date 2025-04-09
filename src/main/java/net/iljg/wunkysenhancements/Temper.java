package net.iljg.wunkysenhancements;

import net.iljg.wunkysenhancements.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static net.iljg.wunkysenhancements.Grind.chatPlayer;

public class Temper {

    public static float onTakeDamage(Level world, Entity entity, LivingHurtEvent livingHurtEvent, float damageAmount) {
        Entity source = livingHurtEvent.getEntity();
        if (source == null) {
            return damageAmount;
        }

        if (world.isClientSide) {
            return damageAmount;
        }

        if (!(source instanceof Player)) {
            return damageAmount;
        }
//        PlayerChatMessage takeDamageTriggered = PlayerChatMessage.unsigned(chatPlayer.getUUID(), "onTakeDamage triggered");
//        chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(takeDamageTriggered), false, ChatType.bind(ChatType.CHAT, chatPlayer));

        Player player = (Player) source;
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        Integer tempered = 0;
        double modifier = Config.PROTECTION_AMOUNT.get();
        if (entity instanceof Player) {
            CompoundTag nbtc_head = head.getOrCreateTag();
            CompoundTag nbtc_chest = chest.getOrCreateTag();
            CompoundTag nbtc_legs = legs.getOrCreateTag();
            CompoundTag nbtc_feet = feet.getOrCreateTag();
            if (nbtc_head.contains("temper")) {
                int headTemperLeft = nbtc_head.getInt("temper");
                if (!player.isCreative()) {
                    headTemperLeft--;
                }
                if (headTemperLeft > 0) {
                    nbtc_head.putInt("temper", headTemperLeft);
                    tempered++;

                }
                else {
                    nbtc_head.remove("temper");
                }
                head.setTag(nbtc_head);
                Utils.updateArmorName(head, headTemperLeft);

            }
            if (nbtc_chest.contains("temper")) {
                int chestTemperLeft = nbtc_chest.getInt("temper");
                if (!player.isCreative()) {
                    chestTemperLeft--;
                }
                if (chestTemperLeft > 0) {
                    nbtc_chest.putInt("temper", chestTemperLeft);
                    tempered++;

                }
                else {
                    nbtc_chest.remove("temper");
                }
                chest.setTag(nbtc_chest);
                Utils.updateArmorName(chest, chestTemperLeft);

            }
            if (nbtc_legs.contains("temper")) {
                int legsTemperLeft = nbtc_legs.getInt("temper");
                if (!player.isCreative()) {
                    legsTemperLeft--;
                }
                if (legsTemperLeft > 0) {
                    nbtc_legs.putInt("temper", legsTemperLeft);
                    tempered++;

                }
                else {
                    nbtc_legs.remove("temper");
                }
                legs.setTag(nbtc_legs);
                Utils.updateArmorName(legs, legsTemperLeft);

            }
            if (nbtc_feet.contains("temper")) {
                int feetTemperLeft = nbtc_feet.getInt("temper");
                if (!player.isCreative()) {
                    feetTemperLeft--;
                }
                if (feetTemperLeft > 0) {
                    nbtc_feet.putInt("temper", feetTemperLeft);
                    tempered++;

                }
                else {
                    nbtc_feet.remove("temper");
                }
                feet.setTag(nbtc_chest);
                Utils.updateArmorName(feet, feetTemperLeft);

            }
        }

        damageAmount = damageAmount - (tempered*(damageAmount*(float) modifier));
        return damageAmount;
    }

    public static boolean onClickArmor(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
        if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
            return true;
        }
        int xpCost = Config.XP_REQUIREMENT.get();
        Block block = world.getBlockState(pos).getBlock();
        if (block.equals(Blocks.ANVIL)) {
            if (player.isCrouching()) {
                if (player.totalExperience > xpCost) {
                    ItemStack itemstack = player.getItemInHand(hand);
                    if (itemstack.getItem() instanceof ArmorItem) {
                        CompoundTag nbtc = itemstack.getOrCreateTag();
                        int protectionuses = Config.PROTECTION_CHARGES.get();

                        nbtc.putInt("temper", protectionuses);
                        itemstack.setTag(nbtc);
                        Utils.updateArmorName(itemstack, protectionuses);
                        player.giveExperiencePoints(-xpCost);
//                        PlayerChatMessage sharp = PlayerChatMessage.unsigned(chatPlayer.getUUID(), "Armor reinforced with  " + protectionuses + " uses.");
//                        chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(sharp), false, ChatType.bind(ChatType.CHAT, chatPlayer));
                        return false;
                    }
                } else {
//                    PlayerChatMessage xplow = PlayerChatMessage.unsigned(chatPlayer.getUUID(), xpCost - player.totalExperience + " more xp required.");
//                    chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(xplow), false, ChatType.bind(ChatType.CHAT, chatPlayer));
                }
            }
        }

        return true;
    }
}
