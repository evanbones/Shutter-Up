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
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
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
                generateShutter(generator, block, TEMPLATE_CLOSED, TEMPLATE_OPEN);

                ResourceLocation itemModelLocation = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name);
                ResourceLocation blockModelLocation = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name);

                new ModelTemplate(Optional.of(blockModelLocation), Optional.empty())
                        .create(itemModelLocation, new TextureMapping(), generator.modelOutput);
            }
        });
    }

    private void generateShutter(BlockModelGenerators generator, Block block, ModelTemplate closedTemplate, ModelTemplate openTemplate) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(block);

        ResourceLocation closedModel = closedTemplate.create(block, textureMapping, generator.modelOutput);
        ResourceLocation openModel = openTemplate.createWithSuffix(block, "_open", textureMapping, generator.modelOutput);

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(
                        PropertyDispatch.property(ShutterBlock.OPEN)
                                .select(false, Variant.variant().with(VariantProperties.MODEL, closedModel))
                                .select(true, Variant.variant().with(VariantProperties.MODEL, openModel))
                )
                .with(
                        PropertyDispatch.property(ShutterBlock.FACING)
                                .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {

    }
}