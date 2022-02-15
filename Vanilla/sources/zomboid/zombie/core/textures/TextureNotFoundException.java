package zombie.core.textures;

public final class TextureNotFoundException extends RuntimeException {
   public TextureNotFoundException(String var1) {
      super("Image " + var1 + " not found! ");
   }
}
