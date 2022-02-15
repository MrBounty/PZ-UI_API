package zombie.core.znet;

public class ZNet {
   public static native void init();

   public static native void setLogLevel(int var0);

   private static void logPutsCallback(String var0) {
      long var1 = System.currentTimeMillis();
      System.out.print(var1 + " " + var0);
   }
}
