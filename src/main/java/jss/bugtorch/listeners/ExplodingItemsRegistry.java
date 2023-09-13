package jss.bugtorch.listeners;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ExplodingItemsRegistry {
    public static List<ItemWithMeta> explodingItems = new ArrayList<>();

    public static class ItemWithMeta {
        public final Item item;
        public final int metadata;
        public final float explosionPower;

        public ItemWithMeta(Item item, int metadata, float explosionPower) {
            this.item = item;
            this.metadata = metadata;
            this.explosionPower = explosionPower;
        }
    }

}
