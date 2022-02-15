package zombie.core.textures;

import java.io.Serializable;

class AlphaColorIndex implements Serializable {
   byte alpha;
   byte blue;
   byte green;
   byte red;

   AlphaColorIndex(int var1, int var2, int var3, int var4) {
      this.red = (byte)var1;
      this.green = (byte)var2;
      this.blue = (byte)var3;
      this.alpha = (byte)var4;
   }
}
