package de.foorcee.labymod.compression.addon.module;

import de.foorcee.labymod.compression.addon.SessionSettings;
import de.foorcee.labymod.compression.addon.compression.CompressionType;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleTextModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class CompressionRateDebugModule extends SimpleTextModule {
    @Override
    public String[] getValues() {
        return new String[]{
                SessionSettings.SESSION_COMPRESSION_TYPE.name() + " " + SessionSettings.SESSION_COMPRESSION_LEVEL,
                SessionSettings.COMPRESSION_RATE.getMean() + "",
                SessionSettings.DECOMPRESSION_RATE.getMean() + ""
        };
    }

    @Override
    public String[] getDefaultValues() {
        return new String[]{
                CompressionType.ZLIB.name() + " " + -1, "0", "0"
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                "Algorithmus",
                "Kompressionsrate",
                "Dekompressionsrate"
        };
    }

    @Override
    public String[] getDefaultKeys() {
        return getKeys();
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.PAPER);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "compression_debug";
    }

    @Override
    public String getDescription() {
        return "Compression Debug Module";
    }

    @Override
    public int getSortingId() {
        return 1;
    }

    @Override
    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_INFO;
    }
}
