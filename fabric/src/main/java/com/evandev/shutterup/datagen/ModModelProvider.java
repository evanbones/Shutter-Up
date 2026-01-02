package com.evandev.shutterup.datagen;

import com.evandev.shutterup.Constants;
import com.evandev.shutterup.ModRegistry;
import com.evandev.shutterup.block.ShutterBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        ModelTemplate TEMPLATE_CLOSED = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_shutter_closed")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        ModelTemplate TEMPLATE_OPEN = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_shutter_open")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );

        ModRegistry.BLOCKS.forEach((name, blockSupplier) -> {
            Block block = blockSupplier.get();
            if (block instanceof ShutterBlock) {
                generateShutter(generator, block, name, TEMPLATE_CLOSED, TEMPLATE_OPEN);
            }
        });
    }

    private void generateShutter(BlockModelGenerators generator, Block block, String name, ModelTemplate closedTemplate, ModelTemplate openTemplate) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(block);

        ResourceLocation closedModel = closedTemplate.create(block, textureMapping, generator.modelOutput);
        ResourceLocation openModel = openTemplate.createWithSuffix(block, "_open", textureMapping, generator.modelOutput);

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(
                        // Logic for CLOSED state
                        net.minecraft.data.models.blockstates.PropertyDispatch.property(ShutterBlock.OPEN)
                                .select(false, Variant.variant().with(VariantProperties.MODEL, closedModel))
                                .select(true, Variant.variant().with(VariantProperties.MODEL, openModel))
                )
                .with(
                        // Logic for FACING (rotation)
                        net.minecraft.data.models.blockstates.PropertyDispatch.property(ShutterBlock.FACING)
                                .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        ModRegistry.ITEMS.forEach((name, itemSupplier) -> {
            generator.generateFlatItem(itemSupplier.get(), ModelTemplates.FLAT_ITEM);
            // 3D block model
            // generator.itemModelOutput.accept(itemSupplier.get(), new ItemModel.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name)));
        });

        ModRegistry.BLOCKS.forEach((name, block) -> {
            ResourceLocation itemModelPath = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name);
            ResourceLocation parentBlockModel = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name);
        });
    }
}