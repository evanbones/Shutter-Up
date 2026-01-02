package com.evandev.shutterup.datagen;

import com.evandev.shutterup.Constants;
import com.evandev.shutterup.ModRegistry;
import com.evandev.shutterup.block.ShutterBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
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
        ModelTemplate TEMPLATE_OPEN_LEFT_BLOCKED = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_shutter_open_left_blocked")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        ModelTemplate TEMPLATE_OPEN_RIGHT_BLOCKED = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/template_shutter_open_right_blocked")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );

        ModRegistry.BLOCKS.forEach((name, blockSupplier) -> {
            Block block = blockSupplier.get();
            if (block instanceof ShutterBlock) {
                generateShutter(generator, block, TEMPLATE_CLOSED, TEMPLATE_OPEN, TEMPLATE_OPEN_LEFT_BLOCKED, TEMPLATE_OPEN_RIGHT_BLOCKED);
            }
        });
    }

    private void generateShutter(BlockModelGenerators generator, Block block, ModelTemplate closedTemplate, ModelTemplate openTemplate, ModelTemplate leftBlockedTemplate, ModelTemplate rightBlockedTemplate) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(block);

        ResourceLocation closedModel = closedTemplate.create(block, textureMapping, generator.modelOutput);
        ResourceLocation openModel = openTemplate.createWithSuffix(block, "_open", textureMapping, generator.modelOutput);
        ResourceLocation leftBlockedModel = leftBlockedTemplate.createWithSuffix(block, "_open_left_blocked", textureMapping, generator.modelOutput);
        ResourceLocation rightBlockedModel = rightBlockedTemplate.createWithSuffix(block, "_open_right_blocked", textureMapping, generator.modelOutput);

        MultipartGenerator multipart = MultipartGenerator.multiPart(block);

        // 1. Closed State
        multipart.with(Condition.condition().term(ShutterBlock.OPEN, false), Variant.variant().with(VariantProperties.MODEL, closedModel));

        // 2. Open State - Normal
        multipart.with(Condition.condition()
                        .term(ShutterBlock.OPEN, true)
                        .term(ShutterBlock.BLOCKED_LEFT, false)
                        .term(ShutterBlock.BLOCKED_RIGHT, false),
                Variant.variant().with(VariantProperties.MODEL, openModel));

        // 3. Open State - Left Blocked
        multipart.with(Condition.condition()
                        .term(ShutterBlock.OPEN, true)
                        .term(ShutterBlock.BLOCKED_LEFT, true)
                        .term(ShutterBlock.BLOCKED_RIGHT, false),
                Variant.variant().with(VariantProperties.MODEL, leftBlockedModel));

        // 4. Open State - Right Blocked
        multipart.with(Condition.condition()
                        .term(ShutterBlock.OPEN, true)
                        .term(ShutterBlock.BLOCKED_LEFT, false)
                        .term(ShutterBlock.BLOCKED_RIGHT, true),
                Variant.variant().with(VariantProperties.MODEL, rightBlockedModel));

        // 5. Open State - Both Blocked
        multipart.with(Condition.condition()
                        .term(ShutterBlock.OPEN, true)
                        .term(ShutterBlock.BLOCKED_LEFT, true)
                        .term(ShutterBlock.BLOCKED_RIGHT, true),
                Variant.variant().with(VariantProperties.MODEL, leftBlockedModel));

        generator.blockStateOutput.accept(multipart.with(
                PropertyDispatch.property(ShutterBlock.FACING)
                        .select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                        .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
        ));
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        ModRegistry.ITEMS.forEach((name, itemSupplier) -> {
            ResourceLocation blockTexture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + name);

            ResourceLocation parent = ResourceLocation.parse("item/generated");

            new ModelTemplate(Optional.of(parent), Optional.empty(), TextureSlot.LAYER0)
                    .create(ModelLocationUtils.getModelLocation(itemSupplier.get()), TextureMapping.layer0(blockTexture), generator.output);
        });
    }
}