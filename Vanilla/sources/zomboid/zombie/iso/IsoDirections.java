package zombie.iso;

import zombie.core.Rand;

public enum IsoDirections {
   N(0),
   NW(1),
   W(2),
   SW(3),
   S(4),
   SE(5),
   E(6),
   NE(7),
   Max(8);

   private static final IsoDirections[] VALUES = values();
   private static IsoDirections[][] directionLookup;
   private static final Vector2 temp = new Vector2();
   private final int index;

   private IsoDirections(int var3) {
      this.index = var3;
   }

   public static IsoDirections fromIndex(int var0) {
      while(var0 < 0) {
         var0 += 8;
      }

      var0 %= 8;
      return VALUES[var0];
   }

   public IsoDirections RotLeft(int var1) {
      IsoDirections var2 = RotLeft(this);

      for(int var3 = 0; var3 < var1 - 1; ++var3) {
         var2 = RotLeft(var2);
      }

      return var2;
   }

   public IsoDirections RotRight(int var1) {
      IsoDirections var2 = RotRight(this);

      for(int var3 = 0; var3 < var1 - 1; ++var3) {
         var2 = RotRight(var2);
      }

      return var2;
   }

   public IsoDirections RotLeft() {
      return RotLeft(this);
   }

   public IsoDirections RotRight() {
      return RotRight(this);
   }

   public static IsoDirections RotLeft(IsoDirections var0) {
      switch(var0) {
      case NE:
         return N;
      case N:
         return NW;
      case NW:
         return W;
      case W:
         return SW;
      case SW:
         return S;
      case S:
         return SE;
      case SE:
         return E;
      case E:
         return NE;
      default:
         return Max;
      }
   }

   public static IsoDirections RotRight(IsoDirections var0) {
      switch(var0) {
      case NE:
         return E;
      case N:
         return NE;
      case NW:
         return N;
      case W:
         return NW;
      case SW:
         return W;
      case S:
         return SW;
      case SE:
         return S;
      case E:
         return SE;
      default:
         return Max;
      }
   }

   public static void generateTables() {
      directionLookup = new IsoDirections[200][200];

      for(int var0 = 0; var0 < 200; ++var0) {
         for(int var1 = 0; var1 < 200; ++var1) {
            int var2 = var0 - 100;
            int var3 = var1 - 100;
            float var4 = (float)var2 / 100.0F;
            float var5 = (float)var3 / 100.0F;
            Vector2 var6 = new Vector2(var4, var5);
            var6.normalize();
            directionLookup[var0][var1] = fromAngleActual(var6);
         }
      }

   }

   public static IsoDirections fromAngleActual(Vector2 var0) {
      temp.x = var0.x;
      temp.y = var0.y;
      temp.normalize();
      float var1 = temp.getDirectionNeg();
      float var2 = 0.7853982F;
      float var3 = 6.2831855F;
      var3 = (float)((double)var3 + Math.toRadians(112.5D));

      for(int var4 = 0; var4 < 8; ++var4) {
         var3 += var2;
         if (var1 >= var3 && var1 <= var3 + var2 || var1 + 6.2831855F >= var3 && var1 + 6.2831855F <= var3 + var2 || var1 - 6.2831855F >= var3 && var1 - 6.2831855F <= var3 + var2) {
            return fromIndex(var4);
         }

         if ((double)var3 > 6.283185307179586D) {
            var3 = (float)((double)var3 - 6.283185307179586D);
         }
      }

      if (temp.x > 0.5F) {
         if (temp.y < -0.5F) {
            return NE;
         } else if (temp.y > 0.5F) {
            return SE;
         } else {
            return E;
         }
      } else if (temp.x < -0.5F) {
         if (temp.y < -0.5F) {
            return NW;
         } else if (temp.y > 0.5F) {
            return SW;
         } else {
            return W;
         }
      } else if (temp.y < -0.5F) {
         return N;
      } else if (temp.y > 0.5F) {
         return S;
      } else {
         return N;
      }
   }

   public static IsoDirections fromAngle(float var0) {
      float var1 = (float)Math.cos((double)var0);
      float var2 = (float)Math.sin((double)var0);
      return fromAngle(var1, var2);
   }

   public static IsoDirections fromAngle(Vector2 var0) {
      return fromAngle(var0.x, var0.y);
   }

   public static IsoDirections fromAngle(float var0, float var1) {
      temp.x = var0;
      temp.y = var1;
      if (temp.getLengthSquared() != 1.0F) {
         temp.normalize();
      }

      if (directionLookup == null) {
         generateTables();
      }

      int var2 = (int)((temp.x + 1.0F) * 100.0F);
      int var3 = (int)((temp.y + 1.0F) * 100.0F);
      if (var2 >= 200) {
         var2 = 199;
      }

      if (var3 >= 200) {
         var3 = 199;
      }

      if (var2 < 0) {
         var2 = 0;
      }

      if (var3 < 0) {
         var3 = 0;
      }

      return directionLookup[var2][var3];
   }

   public static IsoDirections cardinalFromAngle(Vector2 var0) {
      boolean var1 = var0.getX() >= var0.getY();
      boolean var2 = var0.getX() > -var0.getY();
      if (var1) {
         return var2 ? E : N;
      } else {
         return var2 ? S : W;
      }
   }

   public static IsoDirections reverse(IsoDirections var0) {
      switch(var0) {
      case NE:
         return SW;
      case N:
         return S;
      case NW:
         return SE;
      case W:
         return E;
      case SW:
         return NE;
      case S:
         return N;
      case SE:
         return NW;
      case E:
         return W;
      default:
         return Max;
      }
   }

   public int index() {
      return this.index % 8;
   }

   public String toCompassString() {
      switch(this.index) {
      case 0:
         return "9";
      case 1:
         return "8";
      case 2:
         return "7";
      case 3:
         return "4";
      case 4:
         return "1";
      case 5:
         return "2";
      case 6:
         return "3";
      case 7:
         return "6";
      default:
         return "";
      }
   }

   public Vector2 ToVector() {
      switch(this) {
      case NE:
         temp.x = 1.0F;
         temp.y = -1.0F;
         break;
      case N:
         temp.x = 0.0F;
         temp.y = -1.0F;
         break;
      case NW:
         temp.x = -1.0F;
         temp.y = -1.0F;
         break;
      case W:
         temp.x = -1.0F;
         temp.y = 0.0F;
         break;
      case SW:
         temp.x = -1.0F;
         temp.y = 1.0F;
         break;
      case S:
         temp.x = 0.0F;
         temp.y = 1.0F;
         break;
      case SE:
         temp.x = 1.0F;
         temp.y = 1.0F;
         break;
      case E:
         temp.x = 1.0F;
         temp.y = 0.0F;
      }

      temp.normalize();
      return temp;
   }

   public float toAngle() {
      float var1 = 0.7853982F;
      switch(this) {
      case NE:
         return var1 * 7.0F;
      case N:
         return var1 * 0.0F;
      case NW:
         return var1 * 1.0F;
      case W:
         return var1 * 2.0F;
      case SW:
         return var1 * 3.0F;
      case S:
         return var1 * 4.0F;
      case SE:
         return var1 * 5.0F;
      case E:
         return var1 * 6.0F;
      default:
         return 0.0F;
      }
   }

   public static IsoDirections getRandom() {
      return fromIndex(Rand.Next(0, Max.index));
   }

   // $FF: synthetic method
   private static IsoDirections[] $values() {
      return new IsoDirections[]{N, NW, W, SW, S, SE, E, NE, Max};
   }
}
