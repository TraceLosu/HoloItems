package xyz.holocons.mc.holoitemsrevamp.collection.gen2;

import java.util.List;

import com.strangeone101.holoitemsapi.CustomItem;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.holocons.mc.holoitemsrevamp.collection.Idol;

public class MurasakiShion extends Idol {

    private static final String name = "murasakishion";
    private static final String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkxZDE3NTJjMjVmNzVjYTIzZmE4MmFmYTUyNzEwMDEzYjBhMjNmNTdmNmI5ZjYyMjYyYmU2YzI1Yjc3MDI4YiJ9fX0=";

    public MurasakiShion(CustomItem... items) {
        super(name, base64, items);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Murasaki Shion")
                .color(TextColor.color(0x8A55CB))
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public List<Component> getLore() {
        return null;
    }
}
