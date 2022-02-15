package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.core.Rand;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoObject;
import zombie.iso.sprite.IsoSpriteManager;

public class IsoBrokenGlass extends IsoObject {
   public IsoBrokenGlass(IsoCell var1) {
      super(var1);
      int var2 = Rand.Next(4);
      this.sprite = IsoSpriteManager.instance.getSprite("brokenglass_1_" + var2);
   }

   public String getObjectName() {
      return "IsoBrokenGlass";
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
   }

   public void addToWorld() {
      super.addToWorld();
   }

   public void removeFromWorld() {
      super.removeFromWorld();
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      super.render(var1, var2, var3, var4, var5, var6, var7);
   }

   public void renderObjectPicker(float var1, float var2, float var3, ColorInfo var4) {
   }
}
