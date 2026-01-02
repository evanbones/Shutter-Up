package com.evandev.shutterup.datagen;

import com.evandev.shutterup.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {

    public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.shutterup", "Shutter Up");
        translationBuilder.add("block_type.shutterup.shutter", "%s Shutter");

        ModRegistry.BLOCKS.forEach((name, blockSupplier) -> {
            String readableName = name.replace('_', ' ');
            readableName = java.util.regex.Pattern.compile("\\b([a-z])").matcher(readableName)
                    .replaceAll(m -> m.group(1).toUpperCase());

            translationBuilder.add(blockSupplier.get(), readableName);
        });
    }
}