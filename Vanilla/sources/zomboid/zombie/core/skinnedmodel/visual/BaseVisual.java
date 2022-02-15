package zombie.core.skinnedmodel.visual;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.core.skinnedmodel.model.ModelInstance;

public abstract class BaseVisual {
   public abstract void save(ByteBuffer var1) throws IOException;

   public abstract void load(ByteBuffer var1, int var2) throws IOException;

   public abstract ModelInstance createModelInstance();
}
