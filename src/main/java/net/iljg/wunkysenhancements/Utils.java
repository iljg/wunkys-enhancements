package net.iljg.wunkysenhancements;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static net.iljg.wunkysenhancements.Grind.player;

public class Utils {
    public static final String SHARP_NBT = WunkysEnhancements.MOD_ID + "_sharpness";

    public static void sendMessage(Player player, String m, ChatFormatting colour) {
        sendMessage(player, m, colour);
    }

    @SubscribeEvent
    public static void onClick(PlayerInteractEvent.RightClickBlock e) {
        Grind.onClick(e.getLevel(), e.getEntity(), e.getHand(), e.getPos(), e.getHitVec());
        PlayerChatMessage chatMessage1 = PlayerChatMessage.unsigned(player.getUUID(), "SUB EVENT WORKING");
        player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage1), false, ChatType.bind(ChatType.CHAT, player));
    }

    public static void updateName(ItemStack itemstack, int uses) {
        PlayerChatMessage chatMessage4 = PlayerChatMessage.unsigned(player.getUUID(), "updateName called");
        player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage4), false, ChatType.bind(ChatType.CHAT, player));
        String prefix = "Sharpened uses: ";
        Component hoverName = itemstack.getHoverName();
        String name = hoverName.getString();
        List<Component> flatList = hoverName.toFlatList();
        CompoundTag tag = itemstack.getOrCreateTag();
        flatList.removeIf(component -> component.toString().contains(prefix));
        if (uses > 0) {
            Style last = Style.EMPTY;
            if (!flatList.isEmpty()) {
                last = flatList.get(flatList.lastIndexOf(flatList)).getStyle();
            }
            flatList.add(Component.literal(" " + prefix + uses + ")").withStyle(last));
        }

        MutableComponent mutableComponent = Component.empty();
        for (Component component : flatList) {
            mutableComponent.append(component);
        }

        tag.(SHARP_NBT, mutableComponent);
        itemstack.;

        if (uses == 0) {
            if ((new ItemStack(itemstack.getItem()).getHoverName().getString()).equals(itemstack.getHoverName().getString())) {
                itemstack.removeTagKey(SHARP_NBT);
            }
        }
    }
}
