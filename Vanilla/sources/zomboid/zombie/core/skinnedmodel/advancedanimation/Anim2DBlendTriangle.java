package zombie.core.skinnedmodel.advancedanimation;

import javax.xml.bind.annotation.XmlIDREF;

public final class Anim2DBlendTriangle {
   @XmlIDREF
   public Anim2DBlend node1;
   @XmlIDREF
   public Anim2DBlend node2;
   @XmlIDREF
   public Anim2DBlend node3;

   public static double sign(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (double)((var0 - var4) * (var3 - var5) - (var2 - var4) * (var1 - var5));
   }

   static boolean PointInTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      boolean var8 = sign(var0, var1, var2, var3, var4, var5) < 0.0D;
      boolean var9 = sign(var0, var1, var4, var5, var6, var7) < 0.0D;
      boolean var10 = sign(var0, var1, var6, var7, var2, var3) < 0.0D;
      return var8 == var9 && var9 == var10;
   }

   public boolean Contains(float var1, float var2) {
      return PointInTriangle(var1, var2, this.node1.m_XPos, this.node1.m_YPos, this.node2.m_XPos, this.node2.m_YPos, this.node3.m_XPos, this.node3.m_YPos);
   }
}
