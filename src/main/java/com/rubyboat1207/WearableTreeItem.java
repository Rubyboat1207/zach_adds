package com.rubyboat1207;

import net.minecraft.block.WearableCarvedPumpkinBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WearableTreeItem extends Item implements Equipment {
    public WearableTreeItem(Settings settings) {
        super(settings);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    public void onAttack(ItemStack stack, PlayerEntity player, Entity entity) {
        // Get the player's rotation yaw and pitch
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        // Convert yaw and pitch to radians for trigonometric functions
        double radYaw = Math.toRadians(yaw);
        double radPitch = Math.toRadians(pitch);

        // Calculate the direction vector components in the horizontal plane
        double x = -Math.sin(radYaw) * Math.cos(radPitch);
        double z = Math.cos(radYaw) * Math.cos(radPitch);
        double y = -Math.sin(radPitch);

        // Normalize the direction (optional, depends on how strong you want the effect to be)
        double magnitude = Math.sqrt(x * x + y * y + z * z);
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;

        // Set the velocity of the entity; you can scale these values to control the speed
        entity.addVelocity(x* 10, 5, z * 10);
    }
}
