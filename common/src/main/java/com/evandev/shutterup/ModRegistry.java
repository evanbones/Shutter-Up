package com.evandev.shutterup;

import com.evandev.shutterup.block.ShutterBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModRegistry {
    public static final Map<String, Supplier<Block>> BLOCKS = new LinkedHashMap<>();
    public static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();

    // Define all your shutters here
    public static final Supplier<Block> ACACIA_SHUTTER = registerBlock("acacia_shutter", BlockSetType.ACACIA);
    public static final Supplier<Block> BAMBOO_SHUTTER = registerBlock("bamboo_shutter", BlockSetType.BAMBOO);
    public static final Supplier<Block> BIRCH_SHUTTER = registerBlock("birch_shutter", BlockSetType.BIRCH);
    public static final Supplier<Block> CHERRY_SHUTTER = registerBlock("cherry_shutter", BlockSetType.CHERRY);
    public static final Supplier<Block> CRIMSON_SHUTTER = registerBlock("crimson_shutter", BlockSetType.CRIMSON);
    public static final Supplier<Block> DARK_OAK_SHUTTER = registerBlock("dark_oak_shutter", BlockSetType.DARK_OAK);
    public static final Supplier<Block> JUNGLE_SHUTTER = registerBlock("jungle_shutter", BlockSetType.JUNGLE);
    public static final Supplier<Block> MANGROVE_SHUTTER = registerBlock("mangrove_shutter", BlockSetType.MANGROVE);
    public static final Supplier<Block> OAK_SHUTTER = registerBlock("oak_shutter", BlockSetType.OAK);
    public static final Supplier<Block> PALE_OAK_SHUTTER = registerBlock("pale_oak_shutter", BlockSetType.OAK);
    public static final Supplier<Block> SPRUCE_SHUTTER = registerBlock("spruce_shutter", BlockSetType.SPRUCE);
    public static final Supplier<Block> WARPED_SHUTTER = registerBlock("warped_shutter", BlockSetType.WARPED);

    private static Supplier<Block> registerBlock(String name, BlockSetType type) {
        Supplier<Block> blockSupplier = () -> new ShutterBlock(type, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).noOcclusion());

        BLOCKS.put(name, blockSupplier);

        ITEMS.put(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties()));

        return blockSupplier;
    }

    public static void init() {
    }
}