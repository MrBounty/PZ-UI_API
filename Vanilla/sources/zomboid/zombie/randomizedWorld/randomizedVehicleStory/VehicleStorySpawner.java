package zombie.randomizedWorld.randomizedVehicleStory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import zombie.core.math.PZMath;
import zombie.debug.LineDrawer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.popman.ObjectPool;
import zombie.util.Type;

public class VehicleStorySpawner {
   private static final VehicleStorySpawner instance = new VehicleStorySpawner();
   private static final Vector2 s_vector2_1 = new Vector2();
   private static final Vector2 s_vector2_2 = new Vector2();
   private static final ObjectPool s_elementPool = new ObjectPool(VehicleStorySpawner.Element::new);
   private static final int[] s_AABB = new int[4];
   public final ArrayList m_elements = new ArrayList();
   public final HashMap m_storyParams = new HashMap();

   public static VehicleStorySpawner getInstance() {
      return instance;
   }

   public void clear() {
      s_elementPool.release((List)this.m_elements);
      this.m_elements.clear();
      this.m_storyParams.clear();
   }

   public VehicleStorySpawner.Element addElement(String var1, float var2, float var3, float var4, float var5, float var6) {
      VehicleStorySpawner.Element var7 = ((VehicleStorySpawner.Element)s_elementPool.alloc()).init(var1, var2, var3, var4, var5, var6);
      this.m_elements.add(var7);
      return var7;
   }

   public void setParameter(String var1, boolean var2) {
      this.m_storyParams.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
   }

   public void setParameter(String var1, float var2) {
      this.m_storyParams.put(var1, var2);
   }

   public void setParameter(String var1, int var2) {
      this.m_storyParams.put(var1, var2);
   }

   public void setParameter(String var1, Object var2) {
      this.m_storyParams.put(var1, var2);
   }

   public boolean getParameterBoolean(String var1) {
      return (Boolean)this.getParameter(var1, Boolean.class);
   }

   public float getParameterFloat(String var1) {
      return (Float)this.getParameter(var1, Float.class);
   }

   public int getParameterInteger(String var1) {
      return (Integer)this.getParameter(var1, Integer.class);
   }

   public String getParameterString(String var1) {
      return (String)this.getParameter(var1, String.class);
   }

   public Object getParameter(String var1, Class var2) {
      return Type.tryCastTo(this.m_storyParams.get(var1), var2);
   }

   public void spawn(float var1, float var2, float var3, float var4, VehicleStorySpawner.IElementSpawner var5) {
      for(int var6 = 0; var6 < this.m_elements.size(); ++var6) {
         VehicleStorySpawner.Element var7 = (VehicleStorySpawner.Element)this.m_elements.get(var6);
         Vector2 var8 = s_vector2_1.setLengthAndDirection(var7.direction, 1.0F);
         var8.add(var7.position);
         this.rotate(var1, var2, var8, var4);
         this.rotate(var1, var2, var7.position, var4);
         var7.direction = Vector2.getDirection(var8.x - var7.position.x, var8.y - var7.position.y);
         var7.z = var3;
         var7.square = IsoWorld.instance.CurrentCell.getGridSquare((double)var7.position.x, (double)var7.position.y, (double)var3);
         var5.spawn(this, var7);
      }

   }

   public Vector2 rotate(float var1, float var2, Vector2 var3, float var4) {
      float var5 = var3.x;
      float var6 = var3.y;
      var3.x = var1 + (float)((double)var5 * Math.cos((double)var4) - (double)var6 * Math.sin((double)var4));
      var3.y = var2 + (float)((double)var5 * Math.sin((double)var4) + (double)var6 * Math.cos((double)var4));
      return var3;
   }

   public void getAABB(float var1, float var2, float var3, float var4, float var5, int[] var6) {
      Vector2 var7 = s_vector2_1.setLengthAndDirection(var5, 1.0F);
      Vector2 var8 = s_vector2_2.set(var7);
      var8.tangent();
      var7.x *= var4 / 2.0F;
      var7.y *= var4 / 2.0F;
      var8.x *= var3 / 2.0F;
      var8.y *= var3 / 2.0F;
      float var9 = var1 + var7.x;
      float var10 = var2 + var7.y;
      float var11 = var1 - var7.x;
      float var12 = var2 - var7.y;
      float var13 = var9 - var8.x;
      float var14 = var10 - var8.y;
      float var15 = var9 + var8.x;
      float var16 = var10 + var8.y;
      float var17 = var11 - var8.x;
      float var18 = var12 - var8.y;
      float var19 = var11 + var8.x;
      float var20 = var12 + var8.y;
      float var21 = PZMath.min(var13, PZMath.min(var15, PZMath.min(var17, var19)));
      float var22 = PZMath.max(var13, PZMath.max(var15, PZMath.max(var17, var19)));
      float var23 = PZMath.min(var14, PZMath.min(var16, PZMath.min(var18, var20)));
      float var24 = PZMath.max(var14, PZMath.max(var16, PZMath.max(var18, var20)));
      var6[0] = (int)PZMath.floor(var21);
      var6[1] = (int)PZMath.floor(var23);
      var6[2] = (int)PZMath.ceil(var22);
      var6[3] = (int)PZMath.ceil(var24);
   }

   public void render(float var1, float var2, float var3, float var4, float var5, float var6) {
      LineDrawer.DrawIsoRectRotated(var1, var2, var3, var4, var5, var6, 0.0F, 0.0F, 1.0F, 1.0F);
      float var7 = 1.0F;
      float var8 = 1.0F;
      float var9 = 1.0F;
      float var10 = 1.0F;
      float var11 = Float.MAX_VALUE;
      float var12 = Float.MAX_VALUE;
      float var13 = -3.4028235E38F;
      float var14 = -3.4028235E38F;

      for(Iterator var15 = this.m_elements.iterator(); var15.hasNext(); var14 = PZMath.max(var14, (float)s_AABB[3])) {
         VehicleStorySpawner.Element var16 = (VehicleStorySpawner.Element)var15.next();
         Vector2 var17 = s_vector2_1.setLengthAndDirection(var16.direction, 1.0F);
         LineDrawer.DrawIsoLine(var16.position.x, var16.position.y, var3, var16.position.x + var17.x, var16.position.y + var17.y, var3, var7, var8, var9, var10, 1);
         LineDrawer.DrawIsoRectRotated(var16.position.x, var16.position.y, var3, var16.width, var16.height, var16.direction, var7, var8, var9, var10);
         this.getAABB(var16.position.x, var16.position.y, var16.width, var16.height, var16.direction, s_AABB);
         var11 = PZMath.min(var11, (float)s_AABB[0]);
         var12 = PZMath.min(var12, (float)s_AABB[1]);
         var13 = PZMath.max(var13, (float)s_AABB[2]);
      }

   }

   public static final class Element {
      String id;
      final Vector2 position = new Vector2();
      float direction;
      float width;
      float height;
      float z;
      IsoGridSquare square;

      VehicleStorySpawner.Element init(String var1, float var2, float var3, float var4, float var5, float var6) {
         this.id = var1;
         this.position.set(var2, var3);
         this.direction = var4;
         this.width = var5;
         this.height = var6;
         this.z = 0.0F;
         this.square = null;
         return this;
      }
   }

   public interface IElementSpawner {
      void spawn(VehicleStorySpawner var1, VehicleStorySpawner.Element var2);
   }
}
