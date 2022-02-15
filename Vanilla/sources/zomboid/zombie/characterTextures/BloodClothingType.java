package zombie.characterTextures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import zombie.SandboxOptions;
import zombie.core.Rand;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.inventory.types.Clothing;
import zombie.scripting.objects.Item;
import zombie.util.Type;

public enum BloodClothingType {
   Jacket,
   LongJacket,
   Trousers,
   ShortsShort,
   Shirt,
   ShirtLongSleeves,
   ShirtNoSleeves,
   Jumper,
   JumperNoSleeves,
   Shoes,
   FullHelmet,
   Apron,
   Bag,
   Hands,
   Head,
   Neck,
   UpperBody,
   LowerBody,
   LowerLegs,
   UpperLegs,
   LowerArms,
   UpperArms,
   Groin;

   private static HashMap coveredParts = null;
   private static final ArrayList bodyParts = new ArrayList();

   public static BloodClothingType fromString(String var0) {
      if (Jacket.toString().equals(var0)) {
         return Jacket;
      } else if (LongJacket.toString().equals(var0)) {
         return LongJacket;
      } else if (Trousers.toString().equals(var0)) {
         return Trousers;
      } else if (ShortsShort.toString().equals(var0)) {
         return ShortsShort;
      } else if (Shirt.toString().equals(var0)) {
         return Shirt;
      } else if (ShirtLongSleeves.toString().equals(var0)) {
         return ShirtLongSleeves;
      } else if (ShirtNoSleeves.toString().equals(var0)) {
         return ShirtNoSleeves;
      } else if (Jumper.toString().equals(var0)) {
         return Jumper;
      } else if (JumperNoSleeves.toString().equals(var0)) {
         return JumperNoSleeves;
      } else if (Shoes.toString().equals(var0)) {
         return Shoes;
      } else if (FullHelmet.toString().equals(var0)) {
         return FullHelmet;
      } else if (Bag.toString().equals(var0)) {
         return Bag;
      } else if (Hands.toString().equals(var0)) {
         return Hands;
      } else if (Head.toString().equals(var0)) {
         return Head;
      } else if (Neck.toString().equals(var0)) {
         return Neck;
      } else if (Apron.toString().equals(var0)) {
         return Apron;
      } else if (Bag.toString().equals(var0)) {
         return Bag;
      } else if (Hands.toString().equals(var0)) {
         return Hands;
      } else if (Head.toString().equals(var0)) {
         return Head;
      } else if (Neck.toString().equals(var0)) {
         return Neck;
      } else if (UpperBody.toString().equals(var0)) {
         return UpperBody;
      } else if (LowerBody.toString().equals(var0)) {
         return LowerBody;
      } else if (LowerLegs.toString().equals(var0)) {
         return LowerLegs;
      } else if (UpperLegs.toString().equals(var0)) {
         return UpperLegs;
      } else if (LowerArms.toString().equals(var0)) {
         return LowerArms;
      } else if (UpperArms.toString().equals(var0)) {
         return UpperArms;
      } else {
         return Groin.toString().equals(var0) ? Groin : null;
      }
   }

   private static void init() {
      if (coveredParts == null) {
         coveredParts = new HashMap();
         ArrayList var0 = new ArrayList();
         var0.add(BloodBodyPartType.Torso_Upper);
         var0.add(BloodBodyPartType.Torso_Lower);
         var0.add(BloodBodyPartType.UpperLeg_L);
         var0.add(BloodBodyPartType.UpperLeg_R);
         coveredParts.put(Apron, var0);
         ArrayList var1 = new ArrayList();
         var1.add(BloodBodyPartType.Torso_Upper);
         var1.add(BloodBodyPartType.Torso_Lower);
         var1.add(BloodBodyPartType.Back);
         coveredParts.put(ShirtNoSleeves, var1);
         coveredParts.put(JumperNoSleeves, var1);
         ArrayList var2 = new ArrayList();
         var2.addAll(var1);
         var2.add(BloodBodyPartType.UpperArm_L);
         var2.add(BloodBodyPartType.UpperArm_R);
         coveredParts.put(Shirt, var2);
         ArrayList var3 = new ArrayList();
         var3.addAll(var2);
         var3.add(BloodBodyPartType.ForeArm_L);
         var3.add(BloodBodyPartType.ForeArm_R);
         coveredParts.put(ShirtLongSleeves, var3);
         coveredParts.put(Jumper, var3);
         ArrayList var4 = new ArrayList();
         var4.addAll(var3);
         var4.add(BloodBodyPartType.Neck);
         coveredParts.put(Jacket, var4);
         ArrayList var5 = new ArrayList();
         var5.addAll(var3);
         var5.add(BloodBodyPartType.Neck);
         var5.add(BloodBodyPartType.Groin);
         var5.add(BloodBodyPartType.UpperLeg_L);
         var5.add(BloodBodyPartType.UpperLeg_R);
         coveredParts.put(LongJacket, var5);
         ArrayList var6 = new ArrayList();
         var6.add(BloodBodyPartType.Groin);
         var6.add(BloodBodyPartType.UpperLeg_L);
         var6.add(BloodBodyPartType.UpperLeg_R);
         coveredParts.put(ShortsShort, var6);
         ArrayList var7 = new ArrayList();
         var7.addAll(var6);
         var7.add(BloodBodyPartType.LowerLeg_L);
         var7.add(BloodBodyPartType.LowerLeg_R);
         coveredParts.put(Trousers, var7);
         ArrayList var8 = new ArrayList();
         var8.add(BloodBodyPartType.Foot_L);
         var8.add(BloodBodyPartType.Foot_R);
         coveredParts.put(Shoes, var8);
         ArrayList var9 = new ArrayList();
         var9.add(BloodBodyPartType.Head);
         coveredParts.put(FullHelmet, var9);
         ArrayList var10 = new ArrayList();
         var10.add(BloodBodyPartType.Back);
         coveredParts.put(Bag, var10);
         ArrayList var11 = new ArrayList();
         var11.add(BloodBodyPartType.Hand_L);
         var11.add(BloodBodyPartType.Hand_R);
         coveredParts.put(Hands, var11);
         ArrayList var12 = new ArrayList();
         var12.add(BloodBodyPartType.Head);
         coveredParts.put(Head, var12);
         ArrayList var13 = new ArrayList();
         var13.add(BloodBodyPartType.Neck);
         coveredParts.put(Neck, var13);
         ArrayList var14 = new ArrayList();
         var14.add(BloodBodyPartType.Groin);
         coveredParts.put(Groin, var14);
         ArrayList var15 = new ArrayList();
         var15.add(BloodBodyPartType.Torso_Upper);
         coveredParts.put(UpperBody, var15);
         ArrayList var16 = new ArrayList();
         var16.add(BloodBodyPartType.Torso_Lower);
         coveredParts.put(LowerBody, var16);
         ArrayList var17 = new ArrayList();
         var17.add(BloodBodyPartType.LowerLeg_L);
         var17.add(BloodBodyPartType.LowerLeg_R);
         coveredParts.put(LowerLegs, var17);
         ArrayList var18 = new ArrayList();
         var18.add(BloodBodyPartType.UpperLeg_L);
         var18.add(BloodBodyPartType.UpperLeg_R);
         coveredParts.put(UpperLegs, var18);
         ArrayList var19 = new ArrayList();
         var19.add(BloodBodyPartType.UpperArm_L);
         var19.add(BloodBodyPartType.UpperArm_R);
         coveredParts.put(UpperArms, var19);
         ArrayList var20 = new ArrayList();
         var20.add(BloodBodyPartType.ForeArm_L);
         var20.add(BloodBodyPartType.ForeArm_R);
         coveredParts.put(LowerArms, var20);
      }

   }

   public static ArrayList getCoveredParts(ArrayList var0) {
      return getCoveredParts(var0, new ArrayList());
   }

   public static ArrayList getCoveredParts(ArrayList var0, ArrayList var1) {
      if (var0 == null) {
         return var1;
      } else {
         init();

         for(int var2 = 0; var2 < var0.size(); ++var2) {
            BloodClothingType var3 = (BloodClothingType)var0.get(var2);
            var1.addAll((Collection)coveredParts.get(var3));
         }

         return var1;
      }
   }

   public static int getCoveredPartCount(ArrayList var0) {
      if (var0 == null) {
         return 0;
      } else {
         init();
         int var1 = 0;

         for(int var2 = 0; var2 < var0.size(); ++var2) {
            BloodClothingType var3 = (BloodClothingType)var0.get(var2);
            var1 += ((ArrayList)coveredParts.get(var3)).size();
         }

         return var1;
      }
   }

   public static void addBlood(int var0, HumanVisual var1, ArrayList var2, boolean var3) {
      for(int var4 = 0; var4 < var0; ++var4) {
         BloodBodyPartType var5 = BloodBodyPartType.FromIndex(Rand.Next(0, BloodBodyPartType.MAX.index()));
         addBlood(var5, var1, var2, var3);
      }

   }

   public static void addBlood(BloodBodyPartType var0, HumanVisual var1, ArrayList var2, boolean var3) {
      init();
      float var4 = 0.0F;
      if (SandboxOptions.instance.ClothingDegradation.getValue() > 1) {
         float var5 = 0.01F;
         float var6 = 0.05F;
         if (SandboxOptions.instance.ClothingDegradation.getValue() == 2) {
            var5 = 0.001F;
            var6 = 0.01F;
         }

         if (SandboxOptions.instance.ClothingDegradation.getValue() == 3) {
            var5 = 0.05F;
            var6 = 0.1F;
         }

         var4 = OutfitRNG.Next(var5, var6);
      }

      addBlood(var0, var4, var1, var2, var3);
   }

   public static void addDirt(BloodBodyPartType var0, HumanVisual var1, ArrayList var2, boolean var3) {
      init();
      float var4 = 0.0F;
      if (SandboxOptions.instance.ClothingDegradation.getValue() > 1) {
         float var5 = 0.01F;
         float var6 = 0.05F;
         if (SandboxOptions.instance.ClothingDegradation.getValue() == 2) {
            var5 = 0.001F;
            var6 = 0.01F;
         }

         if (SandboxOptions.instance.ClothingDegradation.getValue() == 3) {
            var5 = 0.05F;
            var6 = 0.1F;
         }

         var4 = OutfitRNG.Next(var5, var6);
      }

      addDirt(var0, var4, var1, var2, var3);
   }

   public static void addHole(BloodBodyPartType var0, HumanVisual var1, ArrayList var2) {
      addHole(var0, var1, var2, false);
   }

   public static void addHole(BloodBodyPartType var0, HumanVisual var1, ArrayList var2, boolean var3) {
      init();
      ItemVisual var4 = null;
      boolean var5 = true;

      for(int var6 = var2.size() - 1; var6 >= 0; --var6) {
         ItemVisual var7 = (ItemVisual)var2.get(var6);
         Item var8 = var7.getScriptItem();
         if (var8 != null && (var7.getInventoryItem() == null || !var7.getInventoryItem().isBroken())) {
            ArrayList var9 = var8.getBloodClothingType();
            if (var9 != null) {
               for(int var10 = 0; var10 < var9.size(); ++var10) {
                  BloodClothingType var11 = (BloodClothingType)var8.getBloodClothingType().get(var10);
                  if (((ArrayList)coveredParts.get(var11)).contains(var0) && var8.canHaveHoles && var7.getHole(var0) == 0.0F) {
                     var4 = var7;
                     break;
                  }
               }

               if (var4 != null) {
                  var4.setHole(var0);
                  Clothing var12 = (Clothing)Type.tryCastTo(var4.getInventoryItem(), Clothing.class);
                  if (var12 != null) {
                     var12.removePatch(var0);
                     var12.setCondition(var12.getCondition() - var12.getCondLossPerHole());
                  }

                  if (!var3) {
                     break;
                  }

                  var4 = null;
               }
            }
         }
      }

      if (var4 == null || var3) {
         var1.setHole(var0);
      }

   }

   public static void addBasicPatch(BloodBodyPartType var0, HumanVisual var1, ArrayList var2) {
      init();
      ItemVisual var3 = null;

      for(int var4 = var2.size() - 1; var4 >= 0; --var4) {
         ItemVisual var5 = (ItemVisual)var2.get(var4);
         Item var6 = var5.getScriptItem();
         if (var6 != null) {
            ArrayList var7 = var6.getBloodClothingType();
            if (var7 != null) {
               for(int var8 = 0; var8 < var7.size(); ++var8) {
                  BloodClothingType var9 = (BloodClothingType)var7.get(var8);
                  if (((ArrayList)coveredParts.get(var9)).contains(var0) && var5.getBasicPatch(var0) == 0.0F) {
                     var3 = var5;
                     break;
                  }
               }

               if (var3 != null) {
                  break;
               }
            }
         }
      }

      if (var3 != null) {
         var3.removeHole(BloodBodyPartType.ToIndex(var0));
         var3.setBasicPatch(var0);
      }

   }

   public static void addDirt(BloodBodyPartType var0, float var1, HumanVisual var2, ArrayList var3, boolean var4) {
      init();
      ItemVisual var5 = null;
      float var14;
      if (!var4) {
         for(int var6 = var3.size() - 1; var6 >= 0; --var6) {
            ItemVisual var7 = (ItemVisual)var3.get(var6);
            Item var8 = var7.getScriptItem();
            if (var8 != null) {
               ArrayList var9 = var8.getBloodClothingType();
               if (var9 != null) {
                  for(int var10 = 0; var10 < var9.size(); ++var10) {
                     BloodClothingType var11 = (BloodClothingType)var9.get(var10);
                     if (((ArrayList)coveredParts.get(var11)).contains(var0) && var7.getHole(var0) == 0.0F) {
                        var5 = var7;
                        break;
                     }
                  }

                  if (var5 != null) {
                     break;
                  }
               }
            }
         }

         if (var5 != null) {
            if (var1 > 0.0F) {
               var5.setDirt(var0, var5.getDirt(var0) + var1);
               if (var5.getInventoryItem() instanceof Clothing) {
                  calcTotalDirtLevel((Clothing)var5.getInventoryItem());
               }
            }
         } else {
            var14 = var2.getDirt(var0);
            var2.setDirt(var0, var14 + 0.05F);
         }
      } else {
         var14 = var2.getDirt(var0);
         var2.setDirt(var0, var14 + 0.05F);
         float var15 = var2.getDirt(var0);
         if (Rand.NextBool(Math.abs((new Float(var15 * 100.0F)).intValue() - 100))) {
            return;
         }

         for(int var16 = 0; var16 < var3.size(); ++var16) {
            var5 = null;
            ItemVisual var17 = (ItemVisual)var3.get(var16);
            Item var18 = var17.getScriptItem();
            if (var18 != null) {
               ArrayList var19 = var18.getBloodClothingType();
               if (var19 != null) {
                  for(int var12 = 0; var12 < var19.size(); ++var12) {
                     BloodClothingType var13 = (BloodClothingType)var19.get(var12);
                     if (((ArrayList)coveredParts.get(var13)).contains(var0) && var17.getHole(var0) == 0.0F) {
                        var5 = var17;
                        break;
                     }
                  }

                  if (var5 != null) {
                     if (var1 > 0.0F) {
                        var5.setDirt(var0, var5.getDirt(var0) + var1);
                        if (var5.getInventoryItem() instanceof Clothing) {
                           calcTotalDirtLevel((Clothing)var5.getInventoryItem());
                        }

                        var15 = var5.getDirt(var0);
                     }

                     if (Rand.NextBool(Math.abs((new Float(var15 * 100.0F)).intValue() - 100))) {
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   public static void addBlood(BloodBodyPartType var0, float var1, HumanVisual var2, ArrayList var3, boolean var4) {
      init();
      ItemVisual var5 = null;
      float var14;
      if (!var4) {
         for(int var6 = var3.size() - 1; var6 >= 0; --var6) {
            ItemVisual var7 = (ItemVisual)var3.get(var6);
            Item var8 = var7.getScriptItem();
            if (var8 != null) {
               ArrayList var9 = var8.getBloodClothingType();
               if (var9 != null) {
                  for(int var10 = 0; var10 < var9.size(); ++var10) {
                     BloodClothingType var11 = (BloodClothingType)var9.get(var10);
                     if (((ArrayList)coveredParts.get(var11)).contains(var0) && var7.getHole(var0) == 0.0F) {
                        var5 = var7;
                        break;
                     }
                  }

                  if (var5 != null) {
                     break;
                  }
               }
            }
         }

         if (var5 != null) {
            if (var1 > 0.0F) {
               var5.setBlood(var0, var5.getBlood(var0) + var1);
               if (var5.getInventoryItem() instanceof Clothing) {
                  calcTotalBloodLevel((Clothing)var5.getInventoryItem());
               }
            }
         } else {
            var14 = var2.getBlood(var0);
            var2.setBlood(var0, var14 + 0.05F);
         }
      } else {
         var14 = var2.getBlood(var0);
         var2.setBlood(var0, var14 + 0.05F);
         float var15 = var2.getBlood(var0);
         if (OutfitRNG.NextBool(Math.abs((new Float(var15 * 100.0F)).intValue() - 100))) {
            return;
         }

         for(int var16 = 0; var16 < var3.size(); ++var16) {
            var5 = null;
            ItemVisual var17 = (ItemVisual)var3.get(var16);
            Item var18 = var17.getScriptItem();
            if (var18 != null) {
               ArrayList var19 = var18.getBloodClothingType();
               if (var19 != null) {
                  for(int var12 = 0; var12 < var19.size(); ++var12) {
                     BloodClothingType var13 = (BloodClothingType)var19.get(var12);
                     if (((ArrayList)coveredParts.get(var13)).contains(var0) && var17.getHole(var0) == 0.0F) {
                        var5 = var17;
                        break;
                     }
                  }

                  if (var5 != null) {
                     if (var1 > 0.0F) {
                        var5.setBlood(var0, var5.getBlood(var0) + var1);
                        if (var5.getInventoryItem() instanceof Clothing) {
                           calcTotalBloodLevel((Clothing)var5.getInventoryItem());
                        }

                        var15 = var5.getBlood(var0);
                     }

                     if (OutfitRNG.NextBool(Math.abs((new Float(var15 * 100.0F)).intValue() - 100))) {
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   public static synchronized void calcTotalBloodLevel(Clothing var0) {
      ItemVisual var1 = var0.getVisual();
      if (var1 == null) {
         var0.setBloodLevel(0.0F);
      } else {
         ArrayList var2 = var0.getBloodClothingType();
         if (var2 == null) {
            var0.setBloodLevel(0.0F);
         } else {
            bodyParts.clear();
            getCoveredParts(var2, bodyParts);
            if (bodyParts.isEmpty()) {
               var0.setBloodLevel(0.0F);
            } else {
               float var3 = 0.0F;

               for(int var4 = 0; var4 < bodyParts.size(); ++var4) {
                  var3 += var1.getBlood((BloodBodyPartType)bodyParts.get(var4)) * 100.0F;
               }

               var0.setBloodLevel(var3 / (float)bodyParts.size());
            }
         }
      }
   }

   public static synchronized void calcTotalDirtLevel(Clothing var0) {
      ItemVisual var1 = var0.getVisual();
      if (var1 == null) {
         var0.setDirtyness(0.0F);
      } else {
         ArrayList var2 = var0.getBloodClothingType();
         if (var2 == null) {
            var0.setDirtyness(0.0F);
         } else {
            bodyParts.clear();
            getCoveredParts(var2, bodyParts);
            if (bodyParts.isEmpty()) {
               var0.setDirtyness(0.0F);
            } else {
               float var3 = 0.0F;

               for(int var4 = 0; var4 < bodyParts.size(); ++var4) {
                  var3 += var1.getDirt((BloodBodyPartType)bodyParts.get(var4)) * 100.0F;
               }

               var0.setDirtyness(var3 / (float)bodyParts.size());
            }
         }
      }
   }

   // $FF: synthetic method
   private static BloodClothingType[] $values() {
      return new BloodClothingType[]{Jacket, LongJacket, Trousers, ShortsShort, Shirt, ShirtLongSleeves, ShirtNoSleeves, Jumper, JumperNoSleeves, Shoes, FullHelmet, Apron, Bag, Hands, Head, Neck, UpperBody, LowerBody, LowerLegs, UpperLegs, LowerArms, UpperArms, Groin};
   }
}
