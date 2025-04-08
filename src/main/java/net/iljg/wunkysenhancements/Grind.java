package net.iljg.wunkysenhancements;

import net.iljg.wunkysenhancements.config.Config;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.net.ServerSocket;

public class Grind {

    public static Player player = Minecraft.getInstance().player;

    public static float onDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
        Entity source = damageSource.getEntity();
        if (source == null) {
            return damageAmount;
        }

        if (world.isClientSide) {
            return damageAmount;
        }

        if (!(source instanceof Player player)) {
            return damageAmount;
        }

        ItemStack hand = player.getMainHandItem();

        if (hand.getItem() instanceof SwordItem || hand.getItem() instanceof AxeItem) {

        }

        return damageAmount;
    }

    public static boolean onClick(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
        if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
            return true;
        }

        Block block = world.getBlockState(pos).getBlock();
        if (block.equals(Blocks.GRINDSTONE)) {
            if (player.isCrouching()) {
                ItemStack itemstack = player.getItemInHand(hand);
                if (itemstack.getItem() instanceof SwordItem || itemstack.getItem() instanceof AxeItem) {
                    PlayerChatMessage chatMessage2 = PlayerChatMessage.unsigned(player.getUUID(), "PASSED CHECKS");
                    player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage2), false, ChatType.bind(ChatType.CHAT, player));
                    CompoundTag nbtc = new CompoundTag();

                    CompoundTag customData = itemstack.getTag();
                    if (customData != null) {
                        nbtc = customData.getCompound("sharpened");
                    }

                    int sharpeneduses = Config.SHARPNESS_CHARGES.get();

                    nbtc.putInt("sharpened", sharpeneduses);
                    PlayerChatMessage chatMessage3 = PlayerChatMessage.unsigned(player.getUUID(), nbtc.toString());
                    player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage3), false, ChatType.bind(ChatType.CHAT, player));
                    Utils.updateName(itemstack, sharpeneduses);
                    Utils.sendMessage(player, "Your tool has been sharpened with " + sharpeneduses + " uses.", ChatFormatting.DARK_GREEN);
                    return false;
                }
            }
        }

        return true;
    }


}
