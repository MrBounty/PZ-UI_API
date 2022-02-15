package zombie.core.skinnedmodel.Texture;

import java.util.HashMap;
import zombie.core.textures.Texture;

public class TextureManager {
   public static TextureManager Instance = new TextureManager();
   public HashMap Textures = new HashMap();

   public boolean AddTexture(String var1) {
      Texture var2 = Texture.getSharedTexture(var1);
      if (var2 == null) {
         return false;
      } else {
         this.Textures.put(var1, new Texture2D(var2));
         return true;
      }
   }

   public void AddTexture(String var1, Texture var2) {
      if (!this.Textures.containsKey(var1)) {
         this.Textures.put(var1, new Texture2D(var2));
      }

   }
}
