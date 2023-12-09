package org.inksnow.ankhinvoke.example;

import pers.neige.neigeitems.ref.entity.animal.RefEntitySheep;
import pers.neige.neigeitems.ref.entity.RefNewEntityTypes;
import pers.neige.neigeitems.ref.world.RefWorld;

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
