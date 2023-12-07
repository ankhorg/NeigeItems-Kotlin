package org.inksnow.ankhinvoke.example;

import org.inksnow.ankhinvoke.example.ref.nms.RefEntitySheep;
import org.inksnow.ankhinvoke.example.ref.nms.RefWorld;

public class NoAiSheep extends RefEntitySheep {
  public NoAiSheep(RefWorld world) {
    super(world);
  }

  @Override
  protected void setupAi() {
    //
  }

  @Override
  protected void updateAi() {
    //
  }
}
