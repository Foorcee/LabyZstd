package de.foorcee.labymod.compression.addon;

import net.labymod.addon.AddonTransformer;
import net.labymod.api.TransformerType;

public class CompressionAddonTransformer extends AddonTransformer {

  @Override
  public void registerTransformers() {
    this.registerTransformer(TransformerType.VANILLA, "compression.mixin.json");
  }
}
