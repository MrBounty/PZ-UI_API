package zombie.vehicles;

import java.nio.ByteBuffer;
import zombie.debug.DebugLog;

public class Clipper {
   private long address;
   final ByteBuffer bb = ByteBuffer.allocateDirect(64);

   public static void init() {
      String var0 = "";
      if ("1".equals(System.getProperty("zomboid.debuglibs.clipper"))) {
         DebugLog.log("***** Loading debug version of PZClipper");
         var0 = "d";
      }

      if (System.getProperty("os.name").contains("OS X")) {
         System.loadLibrary("PZClipper");
      } else if (System.getProperty("sun.arch.data.model").equals("64")) {
         System.loadLibrary("PZClipper64" + var0);
      } else {
         System.loadLibrary("PZClipper32" + var0);
      }

      n_init();
   }

   public Clipper() {
      this.newInstance();
   }

   private native void newInstance();

   public native void clear();

   public native void addPath(int var1, ByteBuffer var2, boolean var3);

   public native void addLine(float var1, float var2, float var3, float var4);

   public native void addAABB(float var1, float var2, float var3, float var4);

   public void addAABBBevel(float var1, float var2, float var3, float var4, float var5) {
      this.bb.clear();
      this.bb.putFloat(var1 + var5);
      this.bb.putFloat(var2);
      this.bb.putFloat(var3 - var5);
      this.bb.putFloat(var2);
      this.bb.putFloat(var3);
      this.bb.putFloat(var2 + var5);
      this.bb.putFloat(var3);
      this.bb.putFloat(var4 - var5);
      this.bb.putFloat(var3 - var5);
      this.bb.putFloat(var4);
      this.bb.putFloat(var1 + var5);
      this.bb.putFloat(var4);
      this.bb.putFloat(var1);
      this.bb.putFloat(var4 - var5);
      this.bb.putFloat(var1);
      this.bb.putFloat(var2 + var5);
      this.addPath(this.bb.position() / 4 / 2, this.bb, false);
   }

   public native void addPolygon(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

   public native void clipAABB(float var1, float var2, float var3, float var4);

   public int generatePolygons() {
      return this.generatePolygons(0.0D);
   }

   public native int generatePolygons(double var1);

   public native int getPolygon(int var1, ByteBuffer var2);

   public native int generateTriangulatePolygons(int var1, int var2);

   public native int triangulate(int var1, ByteBuffer var2);

   public static native void n_init();

   private static void writeToStdErr(String var0) {
      System.err.println(var0);
   }
}
