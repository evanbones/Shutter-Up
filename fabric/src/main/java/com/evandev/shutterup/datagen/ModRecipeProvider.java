package com.evandev.shutterup.datagen;

import com.evandev.shutterup.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ModRegistry.ITEMS.forEach((name, itemSupplier) -> {
            String plankName = name.replace("_shutter", "_planks");
            Item plankItem = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(plankName));

            if (plankItem != Items.AIR) {
                var builder = ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemSupplier.get(), 2)
                        .pattern("P P")
                        .pattern("P P")
                        .define('P', plankItem)
                        .unlockedBy("has_planks", has(plankItem));

                builder.save(exporter);
            }
        });
    }
}