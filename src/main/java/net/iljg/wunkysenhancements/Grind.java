package net.iljg.wunkysenhancements;

import net.iljg.wunkysenhancements.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;


public class Grind {

    public static Player chatPlayer = Minecraft.getInstance().player;

    public static float onDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
        Entity source = damageSource.getEntity();
        if (source == null) {
            return damageAmount;
        }

        if (world.isClientSide) {
            return damageAmount;
        }

        if (!(source instanceof Player)) {
            return damageAmount;
        }

        Player player = (Player) source;
        ItemStack hand = player.getMainHandItem();

        if (hand.getItem() instanceof SwordItem || hand.getItem() instanceof AxeItem) {
            CompoundTag nbtc = hand.getOrCreateTag();
            if (nbtc.contains("sharper")) {
                int sharpLeft = nbtc.getInt("sharper");
                if (!player.isCreative()) {
                    sharpLeft--;
                }
                if (sharpLeft > 0) {
                    nbtc.putInt("sharper", sharpLeft);
                    double modifier = Config.SHARPNESS_DAMAGE.get();
                    damageAmount *= (float) modifier;

                }
                else {
                    nbtc.remove("sharper");
                }
                hand.setTag(nbtc);
                Utils.updateName(hand, sharpLeft);

            }
        }

        return damageAmount;
    }

    public static boolean onClick(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
        if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
            return true;
        }
        int xpCost = Config.XP_REQUIREMENT.get();
        Block block = world.getBlockState(pos).getBlock();
        if (block.equals(Blocks.GRINDSTONE)) {
            if (player.isCrouching()) {
                if (player.totalExperience > xpCost) {
                    ItemStack itemstack = player.getItemInHand(hand);
                    if (itemstack.getItem() instanceof SwordItem || itemstack.getItem() instanceof AxeItem) {
                        CompoundTag nbtc = itemstack.getOrCreateTag();
                        int sharpeneduses = Config.SHARPNESS_CHARGES.get();

                        nbtc.putInt("sharper", sharpeneduses);
                        itemstack.setTag(nbtc);
                        Utils.updateName(itemstack, sharpeneduses);
                        player.giveExperiencePoints(-xpCost);
                        PlayerChatMessage sharp = PlayerChatMessage.unsigned(chatPlayer.getUUID(), "Weapon Sharpened with " + sharpeneduses + " uses.");
                        chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(sharp), false, ChatType.bind(ChatType.CHAT, chatPlayer));
                        return false;
                    }
                } else {
                    PlayerChatMessage xplow = PlayerChatMessage.unsigned(chatPlayer.getUUID(), xpCost - player.totalExperience + " more xp required.");
                    chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(xplow), false, ChatType.bind(ChatType.CHAT, chatPlayer));
                }
            }
        }

        return true;
    }


}
