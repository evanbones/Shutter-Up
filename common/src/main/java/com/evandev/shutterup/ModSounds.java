package com.evandev.shutterup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModSounds {
    public static final Map<String, SoundEvent> SOUNDS = new LinkedHashMap<>();

    public static final SoundEvent SHUTTER_OPEN = create("shutter_open");
    public static final SoundEvent SHUTTER_CLOSE = create("shutter_close");
    public static final SoundEvent METAL_SHUTTER_OPEN = create("metal_shutter_open");
    public static final SoundEvent METAL_SHUTTER_CLOSE = create("metal_shutter_close");

    private static SoundEvent create(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
        SOUNDS.put(name, sound);
        return sound;
    }

    public static void init() {
    }
}