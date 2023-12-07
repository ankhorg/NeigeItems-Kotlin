package org.inksnow.ankhinvoke.example;

import org.inksnow.ankhinvoke.example.ref.nms.RefEntitySheep;
import org.inksnow.ankhinvoke.example.ref.nms.RefNewEntityTypes;
import org.inksnow.ankhinvoke.example.ref.nms.RefWorld;

public class NewNoAiSheep extends RefEntitySheep {
  public NewNoAiSheep(RefNewEntityTypes<? extends RefEntitySheep> type, RefWorld world) {
    super(type, world);
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
