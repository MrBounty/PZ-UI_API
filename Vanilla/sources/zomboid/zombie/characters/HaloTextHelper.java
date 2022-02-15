package zombie.characters;

import zombie.GameTime;

public class HaloTextHelper {
   public static final HaloTextHelper.ColorRGB COLOR_WHITE = new HaloTextHelper.ColorRGB(255, 255, 255);
   public static final HaloTextHelper.ColorRGB COLOR_GREEN = new HaloTextHelper.ColorRGB(137, 232, 148);
   public static final HaloTextHelper.ColorRGB COLOR_RED = new HaloTextHelper.ColorRGB(255, 105, 97);
   private static String[] queuedLines = new String[4];
   private static String[] currentLines = new String[4];
   private static boolean ignoreOverheadCheckOnce = false;

   public static HaloTextHelper.ColorRGB getColorWhite() {
      return COLOR_WHITE;
   }

   public static HaloTextHelper.ColorRGB getColorGreen() {
      return COLOR_GREEN;
   }

   public static HaloTextHelper.ColorRGB getColorRed() {
      return COLOR_RED;
   }

   public static void forceNextAddText() {
      ignoreOverheadCheckOnce = true;
   }

   public static void addTextWithArrow(IsoPlayer var0, String var1, boolean var2, HaloTextHelper.ColorRGB var3) {
      addTextWithArrow(var0, var1, var2, var3.r, var3.g, var3.b, var3.r, var3.g, var3.b);
   }

   public static void addTextWithArrow(IsoPlayer var0, String var1, boolean var2, int var3, int var4, int var5) {
      addTextWithArrow(var0, var1, var2, var3, var4, var5, var3, var4, var5);
   }

   public static void addTextWithArrow(IsoPlayer var0, String var1, boolean var2, HaloTextHelper.ColorRGB var3, HaloTextHelper.ColorRGB var4) {
      addTextWithArrow(var0, var1, var2, var3.r, var3.g, var3.b, var4.r, var4.g, var4.b);
   }

   public static void addTextWithArrow(IsoPlayer var0, String var1, boolean var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      addText(var0, "[col=" + var3 + "," + var4 + "," + var5 + "]" + var1 + "[/] [img=media/ui/" + (var2 ? "ArrowUp.png" : "ArrowDown.png") + "," + var6 + "," + var7 + "," + var8 + "]");
   }

   public static void addText(IsoPlayer var0, String var1, HaloTextHelper.ColorRGB var2) {
      addText(var0, var1, var2.r, var2.g, var2.b);
   }

   public static void addText(IsoPlayer var0, String var1, int var2, int var3, int var4) {
      addText(var0, "[col=" + var2 + "," + var3 + "," + var4 + "]" + var1 + "[/]");
   }

   public static void addText(IsoPlayer var0, String var1) {
      int var2 = var0.getPlayerNum();
      if (!overheadContains(var2, var1)) {
         String var3 = queuedLines[var2];
         if (var3 == null) {
            var3 = var1;
         } else {
            if (var3.contains(var1)) {
               return;
            }

            var3 = var3 + "[col=175,175,175], [/]" + var1;
         }

         queuedLines[var2] = var3;
      }
   }

   private static boolean overheadContains(int var0, String var1) {
      if (ignoreOverheadCheckOnce) {
         ignoreOverheadCheckOnce = false;
         return false;
      } else {
         return currentLines[var0] != null && currentLines[var0].contains(var1);
      }
   }

   public static void update() {
      for(int var0 = 0; var0 < 4; ++var0) {
         IsoPlayer var1 = IsoPlayer.players[var0];
         if (var1 != null) {
            if (currentLines[var0] != null && var1.getHaloTimerCount() <= 0.2F * GameTime.getInstance().getMultiplier()) {
               currentLines[var0] = null;
            }

            if (queuedLines[var0] != null && var1.getHaloTimerCount() <= 0.2F * GameTime.getInstance().getMultiplier()) {
               var1.setHaloNote(queuedLines[var0]);
               currentLines[var0] = queuedLines[var0];
               queuedLines[var0] = null;
            }
         } else {
            if (queuedLines[var0] != null) {
               queuedLines[var0] = null;
            }

            if (currentLines[var0] != null) {
               currentLines[var0] = null;
            }
         }
      }

   }

   public static class ColorRGB {
      public int r;
      public int g;
      public int b;
      public int a = 255;

      public ColorRGB(int var1, int var2, int var3) {
         this.r = var1;
         this.g = var2;
         this.b = var3;
      }
   }
}
