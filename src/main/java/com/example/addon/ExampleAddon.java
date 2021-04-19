package com.example.addon;

import java.util.List;

import com.example.addon.compression.CompressionType;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;

public class ExampleAddon extends LabyModAddon {

  public static CompressionType COMPRESSION_TYPE = CompressionType.ZSTD;
  public static Integer LEVEL = -1;

  @Override
  public void onEnable() {
  }

  @Override
  public void loadConfig() {

  }

  @Override
  protected void fillSettings(List<SettingsElement> list) {
//    list.add(new BooleanElement("test", new IconData(Material.ACACIA_FENCE)));
  }
}
