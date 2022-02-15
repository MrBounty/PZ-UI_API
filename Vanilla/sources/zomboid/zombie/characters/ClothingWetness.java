package zombie.characters;

import java.util.ArrayList;
import zombie.GameTime;
import zombie.ZomboidGlobals;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.BodyDamage.Thermoregulator;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.Clothing;

public final class ClothingWetness {
   private static final ItemVisuals itemVisuals = new ItemVisuals();
   private static final ArrayList coveredParts = new ArrayList();
   public final IsoGameCharacter character;
   public final ClothingWetness.ItemList[] clothing;
   public final int[] perspiringParts;
   public boolean changed;

   public ClothingWetness(IsoGameCharacter var1) {
      this.clothing = new ClothingWetness.ItemList[BloodBodyPartType.MAX.index()];
      this.perspiringParts = new int[BloodBodyPartType.MAX.index()];
      this.changed = true;
      this.character = var1;

      for(int var2 = 0; var2 < this.clothing.length; ++var2) {
         this.clothing[var2] = new ClothingWetness.ItemList();
      }

   }

   public void calculateExposedItems() {
      int var1;
      for(var1 = 0; var1 < this.clothing.length; ++var1) {
         this.clothing[var1].clear();
      }

      this.character.getItemVisuals(itemVisuals);

      for(var1 = itemVisuals.size() - 1; var1 >= 0; --var1) {
         ItemVisual var2 = (ItemVisual)itemVisuals.get(var1);
         InventoryItem var3 = var2.getInventoryItem();
         ArrayList var4 = var3.getBloodClothingType();
         if (var4 != null) {
            coveredParts.clear();
            BloodClothingType.getCoveredParts(var4, coveredParts);

            for(int var5 = 0; var5 < coveredParts.size(); ++var5) {
               BloodBodyPartType var6 = (BloodBodyPartType)coveredParts.get(var5);
               this.clothing[var6.index()].add(var3);
            }
         }
      }

   }

   public void updateWetness(float var1, float var2) {
      boolean var3 = false;
      InventoryItem var4 = this.character.getPrimaryHandItem();
      if (var4 != null && var4.isProtectFromRainWhileEquipped()) {
         var3 = true;
      }

      var4 = this.character.getSecondaryHandItem();
      if (var4 != null && var4.isProtectFromRainWhileEquipped()) {
         var3 = true;
      }

      if (this.changed) {
         this.changed = false;
         this.calculateExposedItems();
      }

      this.character.getItemVisuals(itemVisuals);

      for(int var5 = 0; var5 < itemVisuals.size(); ++var5) {
         InventoryItem var6 = ((ItemVisual)itemVisuals.get(var5)).getInventoryItem();
         if (var6 instanceof Clothing) {
            if (var6.getBloodClothingType() == null) {
               ((Clothing)var6).updateWetness(true);
            } else {
               ((Clothing)var6).flushWetness();
            }
         }
      }

      float var26 = (float)ZomboidGlobals.WetnessIncrease * GameTime.instance.getMultiplier();
      float var27 = (float)ZomboidGlobals.WetnessDecrease * GameTime.instance.getMultiplier();

      int var7;
      BloodBodyPartType var8;
      BodyPartType var9;
      BodyPart var10;
      Thermoregulator.ThermalNode var11;
      float var12;
      float var13;
      boolean var15;
      boolean var16;
      float var17;
      boolean var28;
      label282:
      for(var7 = 0; var7 < this.clothing.length; ++var7) {
         var8 = BloodBodyPartType.FromIndex(var7);
         var9 = BodyPartType.FromIndex(var7);
         if (var9 != BodyPartType.MAX) {
            var10 = this.character.getBodyDamage().getBodyPart(var9);
            var11 = this.character.getBodyDamage().getThermoregulator().getNodeForBloodType(var8);
            if (var10 != null && var11 != null) {
               var12 = 0.0F;
               var13 = PZMath.clamp(var11.getSecondaryDelta(), 0.0F, 1.0F);
               var13 *= var13;
               var13 *= 0.2F + 0.8F * (1.0F - var11.getDistToCore());
               float var14;
               if (var13 > 0.1F) {
                  var12 += var13;
               } else {
                  var14 = (var11.getSkinCelcius() - 20.0F) / 22.0F;
                  var14 *= var14;
                  var14 -= var1;
                  var14 = Math.max(0.0F, var14);
                  var12 -= var14;
                  if (var1 > 0.0F) {
                     var12 = 0.0F;
                  }
               }

               this.perspiringParts[var7] = var12 > 0.0F ? 1 : 0;
               if (var12 != 0.0F) {
                  if (var12 > 0.0F) {
                     var12 *= var26;
                  } else {
                     var12 *= var27;
                  }

                  var10.setWetness(var10.getWetness() + var12);
                  if ((!(var12 > 0.0F) || !(var10.getWetness() < 25.0F)) && (!(var12 < 0.0F) || !(var10.getWetness() > 50.0F))) {
                     if (var12 > 0.0F) {
                        var14 = this.character.getBodyDamage().getThermoregulator().getExternalAirTemperature();
                        var14 += 10.0F;
                        var14 = PZMath.clamp(var14, 0.0F, 20.0F) / 20.0F;
                        var12 *= 0.4F + 0.6F * var14;
                     }

                     var28 = false;
                     var15 = false;
                     var16 = false;
                     var17 = 1.0F;
                     int var18 = this.clothing[var7].size() - 1;

                     InventoryItem var19;
                     Clothing var20;
                     while(true) {
                        if (var18 < 0) {
                           continue label282;
                        }

                        int var10002;
                        if (var12 > 0.0F) {
                           var10002 = this.perspiringParts[var7]++;
                        }

                        var19 = (InventoryItem)this.clothing[var7].get(var18);
                        if (var19 instanceof Clothing) {
                           var17 = 1.0F;
                           var20 = (Clothing)var19;
                           ItemVisual var21 = var20.getVisual();
                           if (var21 == null) {
                              break;
                           }

                           if (var21.getHole(var8) > 0.0F) {
                              var28 = true;
                           } else if (var12 > 0.0F && var20.getWetness() >= 100.0F) {
                              var15 = true;
                           } else {
                              if (!(var12 < 0.0F) || !(var20.getWetness() <= 0.0F)) {
                                 if (var12 > 0.0F && var20.getWaterResistance() > 0.0F) {
                                    var17 = PZMath.max(0.0F, 1.0F - var20.getWaterResistance());
                                    if (var17 <= 0.0F) {
                                       var10002 = this.perspiringParts[var7]--;
                                       continue label282;
                                    }
                                 }
                                 break;
                              }

                              var16 = true;
                           }
                        }

                        --var18;
                     }

                     coveredParts.clear();
                     BloodClothingType.getCoveredParts(var19.getBloodClothingType(), coveredParts);
                     int var22 = coveredParts.size();
                     float var23 = var12;
                     if (var12 > 0.0F) {
                        var23 = var12 * var17;
                     }

                     if (var28 || var15 || var16) {
                        var23 /= 2.0F;
                     }

                     var20.setWetness(var20.getWetness() + var23);
                  }
               }
            }
         }
      }

      for(var7 = 0; var7 < this.clothing.length; ++var7) {
         var8 = BloodBodyPartType.FromIndex(var7);
         var9 = BodyPartType.FromIndex(var7);
         if (var9 != BodyPartType.MAX) {
            var10 = this.character.getBodyDamage().getBodyPart(var9);
            var11 = this.character.getBodyDamage().getThermoregulator().getNodeForBloodType(var8);
            if (var10 != null && var11 != null) {
               var12 = 100.0F;
               if (var3) {
                  var12 = 100.0F * BodyPartType.GetUmbrellaMod(var9);
               }

               var13 = 0.0F;
               if (var1 > 0.0F) {
                  var13 = var1 * var26;
               } else {
                  var13 -= var2 * var27;
               }

               var28 = false;
               var15 = false;
               var16 = false;
               var17 = 1.0F;
               float var29 = 2.0F;

               for(int var31 = 0; var31 < this.clothing[var7].size(); ++var31) {
                  int var30 = 1 + (this.clothing[var7].size() - var31);
                  var17 = 1.0F;
                  InventoryItem var33 = (InventoryItem)this.clothing[var7].get(var31);
                  if (var33 instanceof Clothing) {
                     Clothing var35 = (Clothing)var33;
                     ItemVisual var36 = var35.getVisual();
                     if (var36 != null) {
                        if (var36.getHole(var8) > 0.0F) {
                           var28 = true;
                           continue;
                        }

                        if (var13 > 0.0F && var35.getWetness() >= 100.0F) {
                           var15 = true;
                           continue;
                        }

                        if (var13 < 0.0F && var35.getWetness() <= 0.0F) {
                           var16 = true;
                           continue;
                        }

                        if (var13 > 0.0F && var35.getWaterResistance() > 0.0F) {
                           var17 = PZMath.max(0.0F, 1.0F - var35.getWaterResistance());
                           if (var17 <= 0.0F) {
                              break;
                           }
                        }
                     }

                     coveredParts.clear();
                     BloodClothingType.getCoveredParts(var33.getBloodClothingType(), coveredParts);
                     int var24 = coveredParts.size();
                     float var25 = var13;
                     if (var13 > 0.0F) {
                        var25 = var13 * var17;
                     }

                     var25 /= (float)var24;
                     if (var28 || var15 || var16) {
                        var25 /= var29;
                     }

                     if (var13 < 0.0F && var30 > this.perspiringParts[var7] || var13 > 0.0F && var35.getWetness() <= var12) {
                        var35.setWetness(var35.getWetness() + var25);
                     }

                     if (var13 > 0.0F) {
                        break;
                     }

                     if (var16) {
                        var29 *= 2.0F;
                     }
                  }
               }

               if (!this.clothing[var7].isEmpty()) {
                  InventoryItem var32 = (InventoryItem)this.clothing[var7].get(this.clothing[var7].size() - 1);
                  if (var32 instanceof Clothing) {
                     Clothing var34 = (Clothing)var32;
                     if (var13 > 0.0F && this.perspiringParts[var7] == 0 && var34.getWetness() >= 50.0F && var10.getWetness() <= var12) {
                        var10.setWetness(var10.getWetness() + var13 / 2.0F);
                     }

                     if (var13 < 0.0F && this.perspiringParts[var7] == 0 && var34.getWetness() <= 50.0F) {
                        var10.setWetness(var10.getWetness() + var13 / 2.0F);
                     }
                  }
               } else if (var13 < 0.0F && this.perspiringParts[var7] == 0 || var10.getWetness() <= var12) {
                  var10.setWetness(var10.getWetness() + var13);
               }
            }
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public void increaseWetness(float var1) {
      if (!(var1 <= 0.0F)) {
         if (this.changed) {
            this.changed = false;
            this.calculateExposedItems();
         }

         this.character.getItemVisuals(itemVisuals);

         int var2;
         for(var2 = 0; var2 < itemVisuals.size(); ++var2) {
            InventoryItem var3 = ((ItemVisual)itemVisuals.get(var2)).getInventoryItem();
            if (var3 instanceof Clothing) {
               ((Clothing)var3).flushWetness();
            }
         }

         var2 = 0;

         for(int var13 = 0; var13 < this.clothing.length; ++var13) {
            BloodBodyPartType var4 = BloodBodyPartType.FromIndex(var13);
            boolean var5 = false;
            boolean var6 = false;
            int var7 = 0;

            label85: {
               InventoryItem var8;
               Clothing var9;
               while(true) {
                  if (var7 >= this.clothing[var13].size()) {
                     break label85;
                  }

                  var8 = (InventoryItem)this.clothing[var13].get(var7);
                  if (var8 instanceof Clothing) {
                     var9 = (Clothing)var8;
                     ItemVisual var10 = var9.getVisual();
                     if (var10 == null) {
                        break;
                     }

                     if (var10.getHole(var4) > 0.0F) {
                        var5 = true;
                     } else {
                        if (!(var9.getWetness() >= 100.0F)) {
                           break;
                        }

                        var6 = true;
                     }
                  }

                  ++var7;
               }

               coveredParts.clear();
               BloodClothingType.getCoveredParts(var8.getBloodClothingType(), coveredParts);
               int var11 = coveredParts.size();
               float var12 = var1 / (float)var11;
               if (var5 || var6) {
                  var12 /= 2.0F;
               }

               var9.setWetness(var9.getWetness() + var12);
            }

            if (this.clothing[var13].isEmpty()) {
               ++var2;
            } else {
               InventoryItem var16 = (InventoryItem)this.clothing[var13].get(this.clothing[var13].size() - 1);
               if (var16 instanceof Clothing) {
                  Clothing var17 = (Clothing)var16;
                  if (var17.getWetness() >= 100.0F) {
                     ++var2;
                  }
               }
            }
         }

         if (var2 > 0) {
            float var14 = this.character.getBodyDamage().getWetness();
            float var15 = var1 * ((float)var2 / (float)this.clothing.length);
            this.character.getBodyDamage().setWetness(var14 + var15);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void decreaseWetness(float var1) {
      if (!(var1 <= 0.0F)) {
         if (this.changed) {
            this.changed = false;
            this.calculateExposedItems();
         }

         this.character.getItemVisuals(itemVisuals);

         for(int var2 = itemVisuals.size() - 1; var2 >= 0; --var2) {
            ItemVisual var3 = (ItemVisual)itemVisuals.get(var2);
            InventoryItem var4 = var3.getInventoryItem();
            if (var4 instanceof Clothing) {
               Clothing var5 = (Clothing)var4;
               if (var5.getWetness() > 0.0F) {
                  var5.setWetness(var5.getWetness() - var1);
               }
            }
         }

      }
   }

   private static final class ItemList extends ArrayList {
   }
}
