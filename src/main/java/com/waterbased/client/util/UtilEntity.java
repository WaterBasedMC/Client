package com.waterbased.client.util;

import com.waterbased.client.Client;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;

import java.util.List;

public abstract class UtilEntity {
    public static void setEntityGlow(LivingEntity entity, boolean glow) {
        if (entity.getDataTracker().getAllEntries() == null) {
            Client.LOGGER.severe("DataTracker is null");
            return;
        }

        for (DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
            if (entry.getData().getId() != 0) continue;
            DataTracker.Entry<Byte> entry1 = (DataTracker.Entry<Byte>) entry;
            byte value = (Byte) entry.get();

            if (glow) {
                entry1.set((byte) (value | 0x40));
            } else {
                entry1.set((byte) (value & (~0x40)));
            }

            entity.getDataTracker().writeUpdatedEntries(List.of(entry1));
        }
    }
}
