package com.evandev.shutterup;

import com.evandev.shutterup.block.ShutterBlock;
import com.evandev.shutterup.block.WeatheringShutterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModRegistry {
    public static final Map<String, Supplier<Block>> BLOCKS = new LinkedHashMap<>();
    public static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();

    public static final Supplier<Block> ACACIA_SHUTTER = registerBlock("acacia_shutter", BlockSetType.ACACIA);
    public static final Supplier<Block> BAMBOO_SHUTTER = registerBlock("bamboo_shutter", BlockSetType.BAMBOO);
    public static final Supplier<Block> BIRCH_SHUTTER = registerBlock("birch_shutter", BlockSetType.BIRCH);
    public static final Supplier<Block> CHERRY_SHUTTER = registerBlock("cherry_shutter", BlockSetType.CHERRY);
    public static final Supplier<Block> CRIMSON_SHUTTER = registerBlock("crimson_shutter", BlockSetType.CRIMSON);
    public static final Supplier<Block> DARK_OAK_SHUTTER = registerBlock("dark_oak_shutter", BlockSetType.DARK_OAK);
    public static final Supplier<Block> JUNGLE_SHUTTER = registerBlock("jungle_shutter", BlockSetType.JUNGLE);
    public static final Supplier<Block> MANGROVE_SHUTTER = registerBlock("mangrove_shutter", BlockSetType.MANGROVE);
    public static final Supplier<Block> OAK_SHUTTER = registerBlock("oak_shutter", BlockSetType.OAK);
    public static final Supplier<Block> SPRUCE_SHUTTER = registerBlock("spruce_shutter", BlockSetType.SPRUCE);
    public static final Supplier<Block> WARPED_SHUTTER = registerBlock("warped_shutter", BlockSetType.WARPED);

    public static final Supplier<Block> IRON_SHUTTER = registerBlock("iron_shutter", BlockSetType.IRON);

    public static final Supplier<Block> COPPER_SHUTTER = registerCopperBlock("copper_shutter", WeatheringCopper.WeatherState.UNAFFECTED, false);
    public static final Supplier<Block> EXPOSED_COPPER_SHUTTER = registerCopperBlock("exposed_copper_shutter", WeatheringCopper.WeatherState.EXPOSED, false);
    public static final Supplier<Block> WEATHERED_COPPER_SHUTTER = registerCopperBlock("weathered_copper_shutter", WeatheringCopper.WeatherState.WEATHERED, false);
    public static final Supplier<Block> OXIDIZED_COPPER_SHUTTER = registerCopperBlock("oxidized_copper_shutter", WeatheringCopper.WeatherState.OXIDIZED, false);

    public static final Supplier<Block> WAXED_COPPER_SHUTTER = registerCopperBlock("waxed_copper_shutter", WeatheringCopper.WeatherState.UNAFFECTED, true);
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_SHUTTER = registerCopperBlock("waxed_exposed_copper_shutter", WeatheringCopper.WeatherState.EXPOSED, true);
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_SHUTTER = registerCopperBlock("waxed_weathered_copper_shutter", WeatheringCopper.WeatherState.WEATHERED, true);
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_SHUTTER = registerCopperBlock("waxed_oxidized_copper_shutter", WeatheringCopper.WeatherState.OXIDIZED, true);

    private static Supplier<Block> registerCopperBlock(String name, WeatheringCopper.WeatherState state, boolean waxed) {
        Supplier<Block> blockSupplier = new Supplier<>() {
            private Block instance;

            @Override
            public Block get() {
                if (instance == null) {
                    BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion();
                    instance = waxed ? new ShutterBlock(BlockSetType.COPPER, props) : new WeatheringShutterBlock(state, BlockSetType.COPPER, props);
                }
                return instance;
            }
        };

        Supplier<Item> itemSupplier = new Supplier<>() {
            private Item instance;

            @Override
            public Item get() {
                if (instance == null) {
                    instance = new BlockItem(blockSupplier.get(), new Item.Properties());
                }
                return instance;
            }
        };

        BLOCKS.put(name, blockSupplier);
        ITEMS.put(name, itemSupplier);

        return blockSupplier;
    }

    private static Supplier<Block> registerBlock(String name, BlockSetType type) {
        Supplier<Block> blockSupplier = new Supplier<>() {
            private Block instance;

            @Override
            public Block get() {
                if (instance == null) {
                    instance = new ShutterBlock(type, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).noOcclusion());
                }
                return instance;
            }
        };

        Supplier<Item> itemSupplier = new Supplier<>() {
            private Item instance;

            @Override
            public Item get() {
                if (instance == null) {
                    instance = new BlockItem(blockSupplier.get(), new Item.Properties());
                }
                return instance;
            }
        };

        BLOCKS.put(name, blockSupplier);
        ITEMS.put(name, itemSupplier);

        return blockSupplier;
    }

    public static void init() {
    }
}