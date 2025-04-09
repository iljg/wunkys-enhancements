package net.iljg.wunkysenhancements;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class Utils {
    public static final String SHARP_NBT = WunkysEnhancements.MOD_ID + "_sharpness";
    //public static Player chatPlayer = Minecraft.getInstance().player;

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        LivingEntity livingEntity = e.getEntity();
        float originalDamage = e.getAmount();
        if (e.getEntity() instanceof Player) {
//            PlayerChatMessage miti = PlayerChatMessage.unsigned(chatPlayer.getUUID(), "Original damage " + originalDamage);
//            chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(miti), false, ChatType.bind(ChatType.CHAT, chatPlayer));
            float mitigatedDamage = Temper.onTakeDamage(livingEntity.level(), livingEntity, e, originalDamage);
//            PlayerChatMessage miti2 = PlayerChatMessage.unsigned(chatPlayer.getUUID(), "Mitigation damage " + mitigatedDamage);
//            chatPlayer.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(miti2), false, ChatType.bind(ChatType.CHAT, chatPlayer));
            if (originalDamage > mitigatedDamage) {
                e.setAmount(mitigatedDamage);
            }
        }
        float newDamage = Grind.onDamage(livingEntity.level(), livingEntity, e.getSource(), originalDamage);

        if (originalDamage != newDamage) {
            e.setAmount(newDamage);
        }
    }

    @SubscribeEvent
    public static void onClick(PlayerInteractEvent.RightClickBlock e) {
        Grind.onClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
        Temper.onClickArmor(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
    }

    public static void updateName(ItemStack itemstack, int uses) {
        String prefix = "(Sharpened uses: ";
        Component hoverName = itemstack.getHoverName();
        List<Component> flatList = hoverName.toFlatList();

        flatList.removeIf(component -> component.toString().contains(prefix));
        if (uses > 0) {
            Style last = Style.EMPTY;
            if (flatList.size() > 0) {
                last = flatList.get(flatList.size() - 1).getStyle();
            }

            flatList.add(Component.literal(" " + prefix + uses + ")").withStyle(last));
        }

        MutableComponent mutableComponent = MutableComponent.create(ComponentContents.EMPTY);
        for (Component component : flatList) {
            mutableComponent.append(component);
        }

        itemstack.setHoverName(mutableComponent);

        if (uses == 0) {
            if ((new ItemStack(itemstack.getItem()).getHoverName().getString()).equals(itemstack.getHoverName().getString())) {
                itemstack.resetHoverName();
            }
        }
    }

    public static void updateArmorName(ItemStack itemstack, int uses) {
        String prefix = "(Tempered uses: ";
        Component hoverName = itemstack.getHoverName();
        List<Component> flatList = hoverName.toFlatList();

        flatList.removeIf(component -> component.toString().contains(prefix));
        if (uses > 0) {
            Style last = Style.EMPTY;
            if (flatList.size() > 0) {
                last = flatList.get(flatList.size() - 1).getStyle();
            }

            flatList.add(Component.literal(" " + prefix + uses + ")").withStyle(last));
        }

        MutableComponent mutableComponent = MutableComponent.create(ComponentContents.EMPTY);
        for (Component component : flatList) {
            mutableComponent.append(component);
        }

        itemstack.setHoverName(mutableComponent);

        if (uses == 0) {
            if ((new ItemStack(itemstack.getItem()).getHoverName().getString()).equals(itemstack.getHoverName().getString())) {
                itemstack.resetHoverName();
            }
        }
    }
}
