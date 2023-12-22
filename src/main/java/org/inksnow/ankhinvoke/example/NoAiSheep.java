package org.inksnow.ankhinvoke.example;

import pers.neige.neigeitems.ref.entity.animal.RefEntitySheep;
import pers.neige.neigeitems.ref.world.RefWorld;

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
