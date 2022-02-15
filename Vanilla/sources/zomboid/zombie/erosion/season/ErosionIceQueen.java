package zombie.erosion.season;

import java.util.ArrayList;
import zombie.core.Core;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;

public final class ErosionIceQueen {
   public static ErosionIceQueen instance;
   private final ArrayList sprites = new ArrayList();
   private final IsoSpriteManager SprMngr;
   private boolean snowState;

   public void addSprite(String var1, String var2) {
      IsoSprite var3 = this.SprMngr.getSprite(var1);
      IsoSprite var4 = this.SprMngr.getSprite(var2);
      if (var3 != null && var4 != null) {
         var3.setName(var1);
         this.sprites.add(new ErosionIceQueen.Sprite(var3, var1, var2));
      }

   }

   public void setSnow(boolean var1) {
      if (this.snowState != var1) {
         this.snowState = var1;

         for(int var2 = 0; var2 < this.sprites.size(); ++var2) {
            ErosionIceQueen.Sprite var3 = (ErosionIceQueen.Sprite)this.sprites.get(var2);
            var3.sprite.ReplaceCurrentAnimFrames(this.snowState ? var3.winter : var3.normal);
         }
      }

   }

   public ErosionIceQueen(IsoSpriteManager var1) {
      instance = this;
      this.SprMngr = var1;
      int var2;
      if (Core.TileScale == 1) {
         this.setRoofSnowOneX();

         for(var2 = 0; var2 < 10; ++var2) {
            this.addSprite("vegetation_ornamental_01_" + var2, "vegetation_ornamental_01_" + (var2 + 48));
         }
      } else {
         this.setRoofSnow();

         for(var2 = 0; var2 < 10; ++var2) {
            this.addSprite("vegetation_ornamental_01_" + var2, "f_bushes_2_" + (var2 + 10));
            this.addSprite("f_bushes_2_" + var2, "f_bushes_2_" + (var2 + 10));
         }
      }

   }

   private void setRoofSnowA() {
      for(int var1 = 0; var1 < 128; ++var1) {
         String var2 = "e_roof_snow_1_" + var1;

         for(int var3 = 1; var3 <= 5; ++var3) {
            String var4 = "roofs_0" + var3 + "_" + var1;
            this.addSprite(var4, var2);
         }
      }

   }

   private void setRoofSnow() {
      for(int var1 = 1; var1 <= 5; ++var1) {
         for(int var2 = 0; var2 < 128; ++var2) {
            int var3 = var2;
            switch(var1) {
            case 1:
               if (var2 >= 72 && var2 <= 79) {
                  var3 = var2 - 8;
               }

               if (var2 == 112 || var2 == 114) {
                  var3 = 0;
               }

               if (var2 == 113 || var2 == 115) {
                  var3 = 1;
               }

               if (var2 == 116 || var2 == 118) {
                  var3 = 4;
               }

               if (var2 == 117 || var2 == 119) {
                  var3 = 5;
               }
               break;
            case 2:
               if (var2 == 50) {
                  var3 = 106;
               }

               if (var2 == 51) {
                  var3 = 107;
               }

               if (var2 >= 72 && var2 <= 79) {
                  var3 = var2 - 8;
               }

               if (var2 == 104 || var2 == 106) {
                  var3 = 0;
               }

               if (var2 == 105 || var2 == 107) {
                  var3 = 1;
               }

               if (var2 == 108 || var2 == 110) {
                  var3 = 4;
               }

               if (var2 == 109 || var2 == 111) {
                  var3 = 5;
               }
               break;
            case 3:
               if (var2 == 72 || var2 == 74) {
                  var3 = 0;
               }

               if (var2 == 73 || var2 == 75) {
                  var3 = 1;
               }

               if (var2 == 76 || var2 == 78) {
                  var3 = 4;
               }

               if (var2 == 77 || var2 == 79) {
                  var3 = 5;
               }

               if (var2 == 102) {
                  var3 = 70;
               }

               if (var2 == 103) {
                  var3 = 71;
               }

               if (var2 == 104 || var2 == 106) {
                  var3 = 0;
               }

               if (var2 == 105 || var2 == 107) {
                  var3 = 1;
               }

               if (var2 == 108 || var2 == 110) {
                  var3 = 4;
               }

               if (var2 == 109 || var2 == 111) {
                  var3 = 5;
               }

               if (var2 >= 120 && var2 <= 127) {
                  var3 = var2 - 16;
               }
               break;
            case 4:
               if (var2 == 48) {
                  var3 = 106;
               }

               if (var2 == 49) {
                  var3 = 107;
               }

               if (var2 == 50) {
                  var3 = 108;
               }

               if (var2 == 51) {
                  var3 = 109;
               }

               if (var2 == 72 || var2 == 74) {
                  var3 = 0;
               }

               if (var2 == 73 || var2 == 75) {
                  var3 = 1;
               }

               if (var2 == 76 || var2 == 78) {
                  var3 = 4;
               }

               if (var2 == 77 || var2 == 79) {
                  var3 = 5;
               }

               if (var2 == 102) {
                  var3 = 70;
               }

               if (var2 == 103) {
                  var3 = 71;
               }

               if (var2 == 104 || var2 == 106) {
                  var3 = 0;
               }

               if (var2 == 105 || var2 == 107) {
                  var3 = 1;
               }

               if (var2 == 108 || var2 == 110) {
                  var3 = 4;
               }

               if (var2 == 109 || var2 == 111) {
                  var3 = 5;
               }
               break;
            case 5:
               if (var2 == 104 || var2 == 106) {
                  var3 = 0;
               }

               if (var2 == 105 || var2 == 107) {
                  var3 = 1;
               }

               if (var2 == 108 || var2 == 110) {
                  var3 = 4;
               }

               if (var2 == 109 || var2 == 111) {
                  var3 = 5;
               }
            }

            String var4 = "roofs_0" + var1 + "_" + var2;
            String var5 = "e_roof_snow_1_" + var3;
            this.addSprite(var4, var5);
         }
      }

   }

   private void setRoofSnowOneX() {
      for(int var1 = 1; var1 <= 5; ++var1) {
         for(int var2 = 0; var2 < 128; ++var2) {
            int var3 = var2;
            switch(var1) {
            case 1:
               if (var2 >= 96 && var2 <= 98) {
                  var3 = var2 - 16;
               }

               if (var2 == 99) {
                  var3 = var2 - 19;
               }

               if (var2 == 100) {
                  var3 = var2 - 13;
               }

               if (var2 >= 101 && var2 <= 103) {
                  var3 = var2 - 16;
               }

               if (var2 >= 112 && var2 <= 113) {
                  var3 = var2 - 112;
               }

               if (var2 >= 114 && var2 <= 115) {
                  var3 = var2 - 114;
               }

               if (var2 == 116 || var2 == 118) {
                  var3 = 5;
               }

               if (var2 == 117 || var2 == 119) {
                  var3 = 4;
               }
               break;
            case 2:
               if (var2 >= 96 && var2 <= 98) {
                  var3 = var2 - 16;
               }

               if (var2 == 99) {
                  var3 = var2 - 19;
               }

               if (var2 == 100) {
                  var3 = var2 - 13;
               }

               if (var2 >= 101 && var2 <= 103) {
                  var3 = var2 - 16;
               }

               if (var2 >= 104 && var2 <= 105) {
                  var3 = var2 - 104;
               }

               if (var2 >= 106 && var2 <= 107) {
                  var3 = var2 - 106;
               }

               if (var2 >= 108 && var2 <= 109) {
                  var3 = var2 - 104;
               }

               if (var2 >= 110 && var2 <= 111) {
                  var3 = var2 - 106;
               }
               break;
            case 3:
               if (var2 >= 18 && var2 <= 19) {
                  var3 = var2 - 12;
               }

               if (var2 >= 50 && var2 <= 51) {
                  var3 = var2 - 44;
               }

               if (var2 >= 72 && var2 <= 73) {
                  var3 = var2 - 72;
               }

               if (var2 >= 74 && var2 <= 75) {
                  var3 = var2 - 74;
               }

               if (var2 >= 76 && var2 <= 77) {
                  var3 = var2 - 72;
               }

               if (var2 >= 78 && var2 <= 79) {
                  var3 = var2 - 74;
               }

               if (var2 >= 102 && var2 <= 103) {
                  var3 = var2 - 88;
               }

               if (var2 >= 122 && var2 <= 125) {
                  var3 = var2 - 16;
               }
               break;
            case 4:
               if (var2 >= 18 && var2 <= 19) {
                  var3 = var2 - 12;
               }
               break;
            case 5:
               if (var2 >= 72 && var2 <= 74) {
                  var3 = var2 + 8;
               }

               if (var2 == 75) {
                  var3 = var2 + 7;
               }

               if (var2 == 76) {
                  var3 = var2 + 11;
               }

               if (var2 >= 77 && var2 <= 79) {
                  var3 = var2 + 8;
               }

               if (var2 >= 112 && var2 <= 113) {
                  var3 = var2 - 112;
               }

               if (var2 >= 114 && var2 <= 115) {
                  var3 = var2 - 114;
               }

               if (var2 == 116 || var2 == 118) {
                  var3 = 5;
               }

               if (var2 == 117 || var2 == 119) {
                  var3 = 4;
               }
            }

            String var4 = "roofs_0" + var1 + "_" + var2;
            String var5 = "e_roof_snow_1_" + var3;
            this.addSprite(var4, var5);
         }
      }

   }

   public static void Reset() {
      if (instance != null) {
         instance.sprites.clear();
         instance = null;
      }

   }

   private static class Sprite {
      public IsoSprite sprite;
      public String normal;
      public String winter;

      public Sprite(IsoSprite var1, String var2, String var3) {
         this.sprite = var1;
         this.normal = var2;
         this.winter = var3;
      }
   }
}
