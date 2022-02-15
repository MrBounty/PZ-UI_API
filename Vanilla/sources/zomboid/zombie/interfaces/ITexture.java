package zombie.interfaces;

import java.nio.ByteBuffer;
import zombie.core.textures.Mask;
import zombie.core.utils.WrappedBuffer;

public interface ITexture extends IDestroyable, IMaskerable {
   void bind();

   void bind(int var1);

   WrappedBuffer getData();

   int getHeight();

   int getHeightHW();

   int getID();

   int getWidth();

   int getWidthHW();

   float getXEnd();

   float getXStart();

   float getYEnd();

   float getYStart();

   boolean isSolid();

   void makeTransp(int var1, int var2, int var3);

   void setAlphaForeach(int var1, int var2, int var3, int var4);

   void setData(ByteBuffer var1);

   void setMask(Mask var1);

   void setRegion(int var1, int var2, int var3, int var4);
}
