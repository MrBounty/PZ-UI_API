package zombie.core.skinnedmodel.model;

import java.nio.IntBuffer;
import org.lwjglx.BufferUtils;

public final class Vbo {
   public IntBuffer b = BufferUtils.createIntBuffer(4);
   public int VboID;
   public int EboID;
   public int NumElements;
   public int VertexStride;
   public boolean FaceDataOnly;
}
