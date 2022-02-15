package zombie.core.physics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.GameWindow;
import zombie.characters.IsoPlayer;
import zombie.debug.DebugLog;
import zombie.iso.IsoChunk;
import zombie.network.GameServer;
import zombie.network.MPStatistic;

public class Bullet {
   public static ByteBuffer cmdBuf;
   public static final byte TO_ADD_VEHICLE = 4;
   public static final byte TO_SCROLL_CHUNKMAP = 5;
   public static final byte TO_ACTIVATE_CHUNKMAP = 6;
   public static final byte TO_INIT_WORLD = 7;
   public static final byte TO_UPDATE_CHUNK = 8;
   public static final byte TO_DEBUG_DRAW_WORLD = 9;
   public static final byte TO_STEP_SIMULATION = 10;
   public static final byte TO_UPDATE_PLAYER_LIST = 12;
   public static final byte TO_END = -1;

   public static void init() {
      String var0 = "";
      if ("1".equals(System.getProperty("zomboid.debuglibs.bullet"))) {
         DebugLog.log("***** Loading debug version of PZBullet");
         var0 = "d";
      }

      String var1 = "";
      if (GameServer.bServer && GameWindow.OSValidator.isUnix()) {
         var1 = "NoOpenGL";
      }

      if (System.getProperty("os.name").contains("OS X")) {
         System.loadLibrary("PZBullet");
      } else if (System.getProperty("sun.arch.data.model").equals("64")) {
         System.loadLibrary("PZBullet" + var1 + "64" + var0);
      } else {
         System.loadLibrary("PZBullet" + var1 + "32" + var0);
      }

      cmdBuf = ByteBuffer.allocateDirect(4096);
      cmdBuf.order(ByteOrder.LITTLE_ENDIAN);
   }

   private static native void ToBullet(ByteBuffer var0);

   public static void CatchToBullet(ByteBuffer var0) {
      try {
         MPStatistic.getInstance().Bullet.Start();
         ToBullet(var0);
         MPStatistic.getInstance().Bullet.End();
      } catch (RuntimeException var2) {
         var2.printStackTrace();
      }

   }

   public static native void initWorld(int var0, int var1, boolean var2);

   public static native void destroyWorld();

   public static native void activateChunkMap(int var0, int var1, int var2, int var3);

   public static native void deactivateChunkMap(int var0);

   public static void initWorld(int var0, int var1, int var2, int var3, int var4) {
      MPStatistic.getInstance().Bullet.Start();
      initWorld(var0, var1, GameServer.bServer);
      activateChunkMap(0, var2, var3, var4);
      MPStatistic.getInstance().Bullet.End();
   }

   public static void updatePlayerList(ArrayList var0) {
      cmdBuf.clear();
      cmdBuf.put((byte)12);
      cmdBuf.putShort((short)var0.size());
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         IsoPlayer var2 = (IsoPlayer)var1.next();
         cmdBuf.putInt(var2.OnlineID);
         cmdBuf.putInt((int)var2.getX());
         cmdBuf.putInt((int)var2.getY());
      }

      cmdBuf.put((byte)-1);
      cmdBuf.put((byte)-1);
      CatchToBullet(cmdBuf);
   }

   public static void beginUpdateChunk(IsoChunk var0) {
      cmdBuf.clear();
      cmdBuf.put((byte)8);
      cmdBuf.putShort((short)var0.wx);
      cmdBuf.putShort((short)var0.wy);
   }

   public static void updateChunk(int var0, int var1, int var2, int var3, byte[] var4) {
      cmdBuf.put((byte)var0);
      cmdBuf.put((byte)var1);
      cmdBuf.put((byte)var2);
      cmdBuf.put((byte)var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         cmdBuf.put(var4[var5]);
      }

   }

   public static void endUpdateChunk() {
      if (cmdBuf.position() != 5) {
         cmdBuf.put((byte)-1);
         cmdBuf.put((byte)-1);
         CatchToBullet(cmdBuf);
      }
   }

   public static native void scrollChunkMap(int var0, int var1);

   public static void scrollChunkMapLeft(int var0) {
      MPStatistic.getInstance().Bullet.Start();
      scrollChunkMap(var0, 0);
      MPStatistic.getInstance().Bullet.End();
   }

   public static void scrollChunkMapRight(int var0) {
      MPStatistic.getInstance().Bullet.Start();
      scrollChunkMap(var0, 1);
      MPStatistic.getInstance().Bullet.End();
   }

   public static void scrollChunkMapUp(int var0) {
      MPStatistic.getInstance().Bullet.Start();
      scrollChunkMap(var0, 2);
      MPStatistic.getInstance().Bullet.End();
   }

   public static void scrollChunkMapDown(int var0) {
      MPStatistic.getInstance().Bullet.Start();
      scrollChunkMap(var0, 3);
      MPStatistic.getInstance().Bullet.End();
   }

   public static native void addVehicle(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, String var8);

   public static native void removeVehicle(int var0);

   public static native void controlVehicle(int var0, float var1, float var2, float var3);

   public static native void setVehicleActive(int var0, boolean var1);

   public static native void applyCentralForceToVehicle(int var0, float var1, float var2, float var3);

   public static native void applyTorqueToVehicle(int var0, float var1, float var2, float var3);

   public static native void teleportVehicle(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7);

   public static native void setTireInflation(int var0, int var1, float var2);

   public static native void setTireRemoved(int var0, int var1, boolean var2);

   public static native void stepSimulation(float var0, int var1, float var2);

   public static native int getVehicleCount();

   public static native int getVehiclePhysics(int var0, float[] var1);

   public static native int getOwnVehiclePhysics(int var0, float[] var1);

   public static native int setOwnVehiclePhysics(int var0, float[] var1);

   public static native int setVehicleParams(int var0, float[] var1);

   public static native int setVehicleMass(int var0, float var1);

   public static native int getObjectPhysics(float[] var0);

   public static native void createServerCell(int var0, int var1);

   public static native void removeServerCell(int var0, int var1);

   public static native int addPhysicsObject(float var0, float var1);

   public static native void defineVehicleScript(String var0, float[] var1);

   public static native void setVehicleVelocityMultiplier(int var0, float var1, float var2);

   public static native int getCollisions(int var0, float[] var1);

   public static native int setVehicleStatic(int var0, boolean var1);

   public static native int addHingeConstraint(int var0, int var1, float var2, float var3, float var4, float var5, float var6, float var7);

   public static native int addPointConstraint(int var0, int var1, float var2, float var3, float var4, float var5, float var6, float var7);

   public static native void removeConstraint(int var0);
}
