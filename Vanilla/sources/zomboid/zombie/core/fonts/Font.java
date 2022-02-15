package zombie.core.fonts;

import zombie.core.Color;

public interface Font {
   void drawString(float var1, float var2, String var3);

   void drawString(float var1, float var2, String var3, Color var4);

   void drawString(float var1, float var2, String var3, Color var4, int var5, int var6);

   int getHeight(String var1);

   int getWidth(String var1);

   int getWidth(String var1, boolean var2);

   int getWidth(String var1, int var2, int var3);

   int getWidth(String var1, int var2, int var3, boolean var4);

   int getLineHeight();
}
