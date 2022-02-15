package zombie.network;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoGridSquare;
import zombie.iso.LosUtil;

public class ServerLOS {
   public static ServerLOS instance;
   private ServerLOS.LOSThread thread;
   private ArrayList playersMain = new ArrayList();
   private ArrayList playersLOS = new ArrayList();
   private boolean bMapLoading = false;
   private boolean bSuspended = false;
   boolean bWasSuspended;

   private void noise(String var1) {
   }

   public static void init() {
      instance = new ServerLOS();
      instance.start();
   }

   public void start() {
      this.thread = new ServerLOS.LOSThread();
      this.thread.setName("LOS");
      this.thread.setDaemon(true);
      this.thread.start();
   }

   public void addPlayer(IsoPlayer var1) {
      synchronized(this.playersMain) {
         if (this.findData(var1) == null) {
            ServerLOS.PlayerData var3 = new ServerLOS.PlayerData(var1);
            this.playersMain.add(var3);
            synchronized(this.thread.notifier) {
               this.thread.notifier.notify();
            }

         }
      }
   }

   public void removePlayer(IsoPlayer var1) {
      synchronized(this.playersMain) {
         ServerLOS.PlayerData var3 = this.findData(var1);
         this.playersMain.remove(var3);
         synchronized(this.thread.notifier) {
            this.thread.notifier.notify();
         }

      }
   }

   public boolean isCouldSee(IsoPlayer var1, IsoGridSquare var2) {
      ServerLOS.PlayerData var3 = this.findData(var1);
      if (var3 != null) {
         int var4 = var2.x - var3.px + 50;
         int var5 = var2.y - var3.py + 50;
         if (var4 >= 0 && var4 < 100 && var5 >= 0 && var5 < 100) {
            return var3.visible[var4][var5][var2.z];
         }
      }

      return false;
   }

   public void doServerZombieLOS(IsoPlayer var1) {
      if (ServerMap.instance.bUpdateLOSThisFrame) {
         ServerLOS.PlayerData var2 = this.findData(var1);
         if (var2 != null) {
            if (var2.status == ServerLOS.UpdateStatus.NeverDone) {
               var2.status = ServerLOS.UpdateStatus.ReadyInMain;
            }

            if (var2.status == ServerLOS.UpdateStatus.ReadyInMain) {
               var2.status = ServerLOS.UpdateStatus.WaitingInLOS;
               this.noise("WaitingInLOS playerID=" + var1.OnlineID);
               synchronized(this.thread.notifier) {
                  this.thread.notifier.notify();
               }
            }

         }
      }
   }

   public void updateLOS(IsoPlayer var1) {
      ServerLOS.PlayerData var2 = this.findData(var1);
      if (var2 != null) {
         if (var2.status == ServerLOS.UpdateStatus.ReadyInLOS || var2.status == ServerLOS.UpdateStatus.ReadyInMain) {
            if (var2.status == ServerLOS.UpdateStatus.ReadyInLOS) {
               this.noise("BusyInMain playerID=" + var1.OnlineID);
            }

            var2.status = ServerLOS.UpdateStatus.BusyInMain;
            var1.updateLOS();
            var2.status = ServerLOS.UpdateStatus.ReadyInMain;
            synchronized(this.thread.notifier) {
               this.thread.notifier.notify();
            }
         }

      }
   }

   private ServerLOS.PlayerData findData(IsoPlayer var1) {
      for(int var2 = 0; var2 < this.playersMain.size(); ++var2) {
         if (((ServerLOS.PlayerData)this.playersMain.get(var2)).player == var1) {
            return (ServerLOS.PlayerData)this.playersMain.get(var2);
         }
      }

      return null;
   }

   public void suspend() {
      this.bMapLoading = true;
      this.bWasSuspended = this.bSuspended;

      while(!this.bSuspended) {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
         }
      }

      if (!this.bWasSuspended) {
         this.noise("suspend **********");
      }

   }

   public void resume() {
      this.bMapLoading = false;
      synchronized(this.thread.notifier) {
         this.thread.notifier.notify();
      }

      if (!this.bWasSuspended) {
         this.noise("resume **********");
      }

   }

   private class LOSThread extends Thread {
      public Object notifier = new Object();

      public void run() {
         while(true) {
            try {
               this.runInner();
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }
      }

      private void runInner() {
         MPStatistic.getInstance().ServerLOS.Start();
         synchronized(ServerLOS.this.playersMain) {
            ServerLOS.this.playersLOS.clear();
            ServerLOS.this.playersLOS.addAll(ServerLOS.this.playersMain);
         }

         for(int var1 = 0; var1 < ServerLOS.this.playersLOS.size(); ++var1) {
            ServerLOS.PlayerData var2 = (ServerLOS.PlayerData)ServerLOS.this.playersLOS.get(var1);
            if (var2.status == ServerLOS.UpdateStatus.WaitingInLOS) {
               var2.status = ServerLOS.UpdateStatus.BusyInLOS;
               ServerLOS.this.noise("BusyInLOS playerID=" + var2.player.OnlineID);
               this.calcLOS(var2);
               var2.status = ServerLOS.UpdateStatus.ReadyInLOS;
            }

            if (ServerLOS.this.bMapLoading) {
               break;
            }
         }

         MPStatistic.getInstance().ServerLOS.End();

         while(this.shouldWait()) {
            ServerLOS.this.bSuspended = true;
            synchronized(this.notifier) {
               try {
                  this.notifier.wait();
               } catch (InterruptedException var4) {
               }
            }
         }

         ServerLOS.this.bSuspended = false;
      }

      private void calcLOS(ServerLOS.PlayerData var1) {
         boolean var2 = false;
         if (var1.px == (int)var1.player.getX() && var1.py == (int)var1.player.getY() && var1.pz == (int)var1.player.getZ()) {
            var2 = true;
         }

         var1.px = (int)var1.player.getX();
         var1.py = (int)var1.player.getY();
         var1.pz = (int)var1.player.getZ();
         var1.player.initLightInfo2();
         if (!var2) {
            byte var3 = 0;

            int var4;
            int var5;
            int var6;
            for(var4 = 0; var4 < LosUtil.XSIZE; ++var4) {
               for(var5 = 0; var5 < LosUtil.YSIZE; ++var5) {
                  for(var6 = 0; var6 < LosUtil.ZSIZE; ++var6) {
                     LosUtil.cachedresults[var4][var5][var6][var3] = 0;
                  }
               }
            }

            try {
               IsoPlayer.players[var3] = var1.player;
               var4 = var1.px;
               var5 = var1.py;

               for(var6 = -50; var6 < 50; ++var6) {
                  for(int var7 = -50; var7 < 50; ++var7) {
                     for(int var8 = 0; var8 < 8; ++var8) {
                        IsoGridSquare var9 = ServerMap.instance.getGridSquare(var6 + var4, var7 + var5, var8);
                        if (var9 != null) {
                           var9.CalcVisibility(var3);
                           var1.visible[var6 + 50][var7 + 50][var8] = var9.isCouldSee(var3);
                           var9.checkRoomSeen(var3);
                        }
                     }
                  }
               }
            } finally {
               IsoPlayer.players[var3] = null;
            }

         }
      }

      private boolean shouldWait() {
         if (ServerLOS.this.bMapLoading) {
            return true;
         } else {
            for(int var1 = 0; var1 < ServerLOS.this.playersLOS.size(); ++var1) {
               ServerLOS.PlayerData var2 = (ServerLOS.PlayerData)ServerLOS.this.playersLOS.get(var1);
               if (var2.status == ServerLOS.UpdateStatus.WaitingInLOS) {
                  return false;
               }
            }

            synchronized(ServerLOS.this.playersMain) {
               if (ServerLOS.this.playersLOS.size() != ServerLOS.this.playersMain.size()) {
                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

   private class PlayerData {
      public IsoPlayer player;
      public ServerLOS.UpdateStatus status;
      public int px;
      public int py;
      public int pz;
      public boolean[][][] visible;

      public PlayerData(IsoPlayer var2) {
         this.status = ServerLOS.UpdateStatus.NeverDone;
         this.visible = new boolean[100][100][8];
         this.player = var2;
      }
   }

   static enum UpdateStatus {
      NeverDone,
      WaitingInLOS,
      BusyInLOS,
      ReadyInLOS,
      BusyInMain,
      ReadyInMain;

      // $FF: synthetic method
      private static ServerLOS.UpdateStatus[] $values() {
         return new ServerLOS.UpdateStatus[]{NeverDone, WaitingInLOS, BusyInLOS, ReadyInLOS, BusyInMain, ReadyInMain};
      }
   }

   public static final class ServerLighting implements IsoGridSquare.ILighting {
      private static final byte LOS_SEEN = 1;
      private static final byte LOS_COULD_SEE = 2;
      private static final byte LOS_CAN_SEE = 4;
      private static ColorInfo lightInfo = new ColorInfo();
      private byte los;

      public int lightverts(int var1) {
         return 0;
      }

      public float lampostTotalR() {
         return 0.0F;
      }

      public float lampostTotalG() {
         return 0.0F;
      }

      public float lampostTotalB() {
         return 0.0F;
      }

      public boolean bSeen() {
         return (this.los & 1) != 0;
      }

      public boolean bCanSee() {
         return (this.los & 4) != 0;
      }

      public boolean bCouldSee() {
         return (this.los & 2) != 0;
      }

      public float darkMulti() {
         return 0.0F;
      }

      public float targetDarkMulti() {
         return 0.0F;
      }

      public ColorInfo lightInfo() {
         lightInfo.r = 1.0F;
         lightInfo.g = 1.0F;
         lightInfo.b = 1.0F;
         return lightInfo;
      }

      public void lightverts(int var1, int var2) {
      }

      public void lampostTotalR(float var1) {
      }

      public void lampostTotalG(float var1) {
      }

      public void lampostTotalB(float var1) {
      }

      public void bSeen(boolean var1) {
         if (var1) {
            this.los = (byte)(this.los | 1);
         } else {
            this.los &= -2;
         }

      }

      public void bCanSee(boolean var1) {
         if (var1) {
            this.los = (byte)(this.los | 4);
         } else {
            this.los &= -5;
         }

      }

      public void bCouldSee(boolean var1) {
         if (var1) {
            this.los = (byte)(this.los | 2);
         } else {
            this.los &= -3;
         }

      }

      public void darkMulti(float var1) {
      }

      public void targetDarkMulti(float var1) {
      }

      public int resultLightCount() {
         return 0;
      }

      public IsoGridSquare.ResultLight getResultLight(int var1) {
         return null;
      }

      public void reset() {
         this.los = 0;
      }
   }
}
