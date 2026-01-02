package com.evandev.shutterup.compat;

import com.evandev.shutterup.Constants;
import com.evandev.shutterup.ModRegistry;
import com.evandev.shutterup.block.ShutterBlock;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ShutterUpEveryCompat extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> shutters;

    public ShutterUpEveryCompat() {
        super(Constants.MOD_ID, "shutterup", Constants.MOD_ID);

        shutters = SimpleEntrySet.builder(WoodType.class, "shutter",
                        ModRegistry.OAK_SHUTTER,
                        () -> VanillaWoodTypes.OAK,
                        w -> new ShutterBlock(
                                w.toVanillaOrOak().setType(),
                                Utils.copyPropertySafe(w.planks).noOcclusion()
                        )
                )
                .addTexture(modRes("block/oak_shutter"))
                .addTexture(modRes("item/oak_shutter"))
                .setTabKey(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.parse("minecraft:building_blocks")))
                .defaultRecipe()
                .build();

        this.addEntry(shutters);
    }
}