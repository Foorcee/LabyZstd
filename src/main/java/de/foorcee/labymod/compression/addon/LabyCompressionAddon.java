package de.foorcee.labymod.compression.addon;

import java.util.List;

import de.foorcee.labymod.compression.addon.compression.CompressionType;
import de.foorcee.labymod.compression.addon.module.CompressionRateDebugModule;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;

public class LabyCompressionAddon extends LabyModAddon {

    public static boolean ENABLED;
    public static CompressionType COMPRESSION_TYPE = CompressionType.ZSTD;
    public static Integer LEVEL = -1;
    public static boolean DISABLE_ENCODE_COMPRESSION = false;
    public static Integer THRESHOLD = 256;

    @Override
    public void onEnable() {
        getApi().registerModule(new CompressionRateDebugModule());
    }

    @Override
    public void loadConfig() {
        if (getConfig().has("enabled")) {
            ENABLED = getConfig().get("enabled").getAsBoolean();
        } else {
            ENABLED = true;
        }
        if (getConfig().has("compressionType")) {
            try {
                COMPRESSION_TYPE = CompressionType.valueOf(getConfig().get("compressionType").getAsString());
            } catch (Exception ex) {
                COMPRESSION_TYPE = CompressionType.ZLIB;
            }
        } else {
            COMPRESSION_TYPE = CompressionType.ZLIB;
        }
        if (getConfig().has("compressionLevel")) {
            LEVEL = getConfig().get("compressionLevel").getAsInt();
        } else {
            LEVEL = -1;
        }

        if (getConfig().has("disableEncodeCompression")) {
            DISABLE_ENCODE_COMPRESSION = getConfig().get("disableEncodeCompression").getAsBoolean();
        }

        if (getConfig().has("threshold")) {
            THRESHOLD = getConfig().get("threshold").getAsInt();
        }

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new BooleanElement("Enabled", this, new ControlElement.IconData(Material.LEVER), "enabled", ENABLED));

        DropDownMenu<CompressionType> alignmentDropDownMenu = new DropDownMenu<CompressionType>("Compression Type", 0, 0, 0, 0)
                .fill(CompressionType.values());
        DropDownElement<CompressionType> alignmentDropDown = new DropDownElement<>("Compression Type", alignmentDropDownMenu);

        alignmentDropDownMenu.setSelected(COMPRESSION_TYPE);

        alignmentDropDown.setChangeListener(compressionType -> {
            COMPRESSION_TYPE = compressionType;
            getConfig().addProperty("compressionType", COMPRESSION_TYPE.name());
            saveConfig();
        });
        list.add(alignmentDropDown);


        NumberElement numberElement = new NumberElement("Compression Level", new ControlElement.IconData(Material.WATCH), LEVEL);
        numberElement.setMinValue(-1);
        numberElement.addCallback(integer -> {
            LEVEL = integer;
            getConfig().addProperty("compressionLevel", LEVEL);
            saveConfig();
        });

        list.add(numberElement);

        NumberElement numberElement2 = new NumberElement("Compression threshold", new ControlElement.IconData(Material.REDSTONE), THRESHOLD);

        numberElement2.addCallback(integer -> {
            THRESHOLD = integer;
            getConfig().addProperty("threshold", THRESHOLD);
            saveConfig();
        });

        list.add(numberElement2);

        list.add(new BooleanElement("Disable Encode Compression", this, new ControlElement.IconData(Material.LEVER), "disableEncodeCompression", DISABLE_ENCODE_COMPRESSION));
    }
}
