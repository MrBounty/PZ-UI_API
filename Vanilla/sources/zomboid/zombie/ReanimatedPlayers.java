package zombie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.ai.states.ZombieIdleState;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.SliceY;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public final class ReanimatedPlayers {
   public static ReanimatedPlayers instance = new ReanimatedPlayers();
   private final ArrayList Zombies = new ArrayList();

   private static void noise(String var0) {
      DebugLog.log("reanimate: " + var0);
   }

   public void addReanimatedPlayersToChunk(IsoChunk var1) {
      int var2 = var1.wx * 10;
      int var3 = var1.wy * 10;
      int var4 = var2 + 10;
      int var5 = var3 + 10;

      for(int var6 = 0; var6 < this.Zombies.size(); ++var6) {
         IsoZombie var7 = (IsoZombie)this.Zombies.get(var6);
         if (var7.getX() >= (float)var2 && var7.getX() < (float)var4 && var7.getY() >= (float)var3 && var7.getY() < (float)var5) {
            IsoGridSquare var8 = var1.getGridSquare((int)var7.getX() - var2, (int)var7.getY() - var3, (int)var7.getZ());
            if (var8 != null) {
               if (GameServer.bServer) {
                  if (var7.OnlineID != -1) {
                     noise("ERROR? OnlineID != -1 for reanimated player zombie");
                  }

                  var7.OnlineID = ServerMap.instance.getUniqueZombieId();
                  if (var7.OnlineID == -1) {
                     continue;
                  }

                  ServerMap.instance.ZombieMap.put(var7.OnlineID, var7);
               }

               var7.setCurrent(var8);

               assert !IsoWorld.instance.CurrentCell.getObjectList().contains(var7);

               assert !IsoWorld.instance.CurrentCell.getZombieList().contains(var7);

               IsoWorld.instance.CurrentCell.getObjectList().add(var7);
               IsoWorld.instance.CurrentCell.getZombieList().add(var7);
               this.Zombies.remove(var6);
               --var6;
               SharedDescriptors.createPlayerZombieDescriptor(var7);
               noise("added to world " + var7);
            }
         }
      }

   }

   public void removeReanimatedPlayerFromWorld(IsoZombie var1) {
      if (var1.isReanimatedPlayer()) {
         if (!GameServer.bServer) {
            var1.setSceneCulled(true);
         }

         if (var1.isOnFire()) {
            IsoFireManager.RemoveBurningCharacter(var1);
            var1.setOnFire(false);
         }

         if (var1.AttachedAnimSprite != null) {
            ArrayList var2 = var1.AttachedAnimSprite;

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               IsoSpriteInstance var4 = (IsoSpriteInstance)var2.get(var3);
               IsoSpriteInstance.add(var4);
            }

            var1.AttachedAnimSprite.clear();
         }

         if (!GameServer.bServer) {
            for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
               IsoPlayer var6 = IsoPlayer.players[var5];
               if (var6 != null && var6.ReanimatedCorpse == var1) {
                  var6.ReanimatedCorpse = null;
                  var6.ReanimatedCorpseID = -1;
               }
            }
         }

         if (GameServer.bServer && var1.OnlineID != -1) {
            ServerMap.instance.ZombieMap.remove(var1.OnlineID);
            var1.OnlineID = -1;
         }

         SharedDescriptors.releasePlayerZombieDescriptor(var1);

         assert !VirtualZombieManager.instance.isReused(var1);

         if (!var1.isDead()) {
            if (!GameClient.bClient) {
               if (!this.Zombies.contains(var1)) {
                  this.Zombies.add(var1);
                  noise("added to Zombies " + var1);
                  var1.setStateMachineLocked(false);
                  var1.changeState(ZombieIdleState.instance());
               }
            }
         }
      }
   }

   public void saveReanimatedPlayers() {
      if (!GameClient.bClient) {
         ArrayList var1 = new ArrayList();

         try {
            ByteBuffer var2 = SliceY.SliceBuffer;
            var2.clear();
            var2.putInt(186);
            var1.addAll(this.Zombies);
            ArrayList var3 = IsoWorld.instance.CurrentCell.getZombieList();
            Iterator var4 = var3.iterator();

            while(true) {
               IsoZombie var5;
               if (!var4.hasNext()) {
                  var2.putInt(var1.size());
                  var4 = var1.iterator();

                  while(var4.hasNext()) {
                     var5 = (IsoZombie)var4.next();
                     var5.save(var2);
                  }

                  File var8 = ZomboidFileSystem.instance.getFileInCurrentSave("reanimated.bin");
                  FileOutputStream var9 = new FileOutputStream(var8);
                  BufferedOutputStream var6 = new BufferedOutputStream(var9);
                  var6.write(var2.array(), 0, var2.position());
                  var6.flush();
                  var6.close();
                  break;
               }

               var5 = (IsoZombie)var4.next();
               if (var5.isReanimatedPlayer() && !var5.isDead() && !var1.contains(var5)) {
                  var1.add(var5);
               }
            }
         } catch (Exception var7) {
            ExceptionLogger.logException(var7);
            return;
         }

         noise("saved " + var1.size() + " zombies");
      }
   }

   public void loadReanimatedPlayers() {
      if (!GameClient.bClient) {
         this.Zombies.clear();
         File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("reanimated.bin");

         try {
            FileInputStream var2 = new FileInputStream(var1);

            try {
               BufferedInputStream var3 = new BufferedInputStream(var2);

               try {
                  synchronized(SliceY.SliceBufferLock) {
                     ByteBuffer var5 = SliceY.SliceBuffer;
                     var5.clear();
                     int var6 = var3.read(var5.array());
                     var5.limit(var6);
                     this.loadReanimatedPlayers(var5);
                  }
               } catch (Throwable var11) {
                  try {
                     var3.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }

                  throw var11;
               }

               var3.close();
            } catch (Throwable var12) {
               try {
                  var2.close();
               } catch (Throwable var8) {
                  var12.addSuppressed(var8);
               }

               throw var12;
            }

            var2.close();
         } catch (FileNotFoundException var13) {
            return;
         } catch (Exception var14) {
            ExceptionLogger.logException(var14);
            return;
         }

         noise("loaded " + this.Zombies.size() + " zombies");
      }
   }

   private void loadReanimatedPlayers(ByteBuffer var1) throws IOException, RuntimeException {
      int var2 = var1.getInt();
      int var3 = var1.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoObject var5 = IsoObject.factoryFromFileInput(IsoWorld.instance.CurrentCell, var1);
         if (!(var5 instanceof IsoZombie)) {
            throw new RuntimeException("expected IsoZombie here");
         }

         IsoZombie var6 = (IsoZombie)var5;
         var6.load(var1, var2);
         var6.getDescriptor().setID(0);
         var6.setReanimatedPlayer(true);
         IsoWorld.instance.CurrentCell.getAddList().remove(var6);
         IsoWorld.instance.CurrentCell.getObjectList().remove(var6);
         IsoWorld.instance.CurrentCell.getZombieList().remove(var6);
         this.Zombies.add(var6);
      }

   }
}
