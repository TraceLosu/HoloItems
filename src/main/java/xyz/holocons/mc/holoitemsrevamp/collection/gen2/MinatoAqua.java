package xyz.holocons.mc.holoitemsrevamp.collection.gen2;

import java.util.List;

import com.strangeone101.holoitemsapi.CustomItem;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.holocons.mc.holoitemsrevamp.collection.Idol;

public class MinatoAqua extends Idol {

    private static final String name = "minatoaqua";
    private static final String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1M2Y1NGVlNGE4ODY0YjdkNTIyZDVjYTkyNTE2Y2U5ZTlmOWE0YWU4YmYzZmRkMmQzOWJmYjc0MzY0ZDgyOCJ9fX0=";

    public MinatoAqua(CustomItem... items) {
        super(name, base64, items);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Minato Aqua")
                .color(TextColor.color(0xFE5DD5))
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public List<Component> getLore() {
        return null;
    }
}
