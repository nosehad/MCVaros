package dev.nosehad.varos.packet;

import java.util.UUID;

interface F3Name {

    void send(UUID uuid, String brand);
    String BRAND_CHANNEL = "minecraft:brand";
}
