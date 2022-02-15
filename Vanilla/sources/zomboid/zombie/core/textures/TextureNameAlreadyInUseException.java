package zombie.core.textures;

public final class TextureNameAlreadyInUseException extends RuntimeException {
   public TextureNameAlreadyInUseException(String var1) {
      super("Texture Name " + var1 + " is already in use");
   }
}
