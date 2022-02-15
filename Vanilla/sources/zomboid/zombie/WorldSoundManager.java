package zombie;

import java.util.ArrayList;
import java.util.Stack;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.popman.MPDebugInfo;
import zombie.popman.ZombiePopulationManager;

public final class WorldSoundManager {
   public static final WorldSoundManager instance = new WorldSoundManager();
   public final ArrayList SoundList = new ArrayList();
   private final Stack freeSounds = new Stack();
   private static final WorldSoundManager.ResultBiggestSound resultBiggestSound = new WorldSoundManager.ResultBiggestSound();

   public void init(IsoCell var1) {
   }

   public void initFrame() {
   }

   public void KillCell() {
      this.freeSounds.addAll(this.SoundList);
      this.SoundList.clear();
   }

   public WorldSoundManager.WorldSound getNew() {
      return this.freeSounds.isEmpty() ? new WorldSoundManager.WorldSound() : (WorldSoundManager.WorldSound)this.freeSounds.pop();
   }

   public WorldSoundManager.WorldSound addSound(Object var1, int var2, int var3, int var4, int var5, int var6) {
      return this.addSound(var1, var2, var3, var4, var5, var6, false, 0.0F, 1.0F);
   }

   public WorldSoundManager.WorldSound addSound(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      return this.addSound(var1, var2, var3, var4, var5, var6, var7, 0.0F, 1.0F);
   }

   public WorldSoundManager.WorldSound addSound(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7, float var8, float var9) {
      return this.addSound(var1, var2, var3, var4, var5, var6, var7, var8, var9, false, true, false);
   }

   public WorldSoundManager.WorldSound addSound(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7, float var8, float var9, boolean var10, boolean var11, boolean var12) {
      if (var5 <= 0) {
         return null;
      } else {
         if (!var12) {
            if (SandboxOptions.instance.Lore.Hearing.getValue() == 1) {
               var5 = (int)((float)var5 * 3.0F);
            }

            if (SandboxOptions.instance.Lore.Hearing.getValue() == 3) {
               var5 = (int)((float)var5 * 0.45F);
            }
         }

         WorldSoundManager.WorldSound var13;
         synchronized(this.SoundList) {
            var13 = this.getNew().init(var1, var2, var3, var4, var5, var6, var7, var8, var9);
            if (var1 == null) {
               var13.sourceIsZombie = var10;
            }

            if (!GameServer.bServer) {
               int var15 = (var2 - var5) / 10;
               int var16 = (var3 - var5) / 10;
               int var17 = (int)Math.ceil((double)(((float)var2 + (float)var5) / 10.0F));
               int var18 = (int)Math.ceil((double)(((float)var3 + (float)var5) / 10.0F));

               for(int var19 = var15; var19 < var17; ++var19) {
                  for(int var20 = var16; var20 < var18; ++var20) {
                     IsoChunk var21 = IsoWorld.instance.CurrentCell.getChunk(var19, var20);
                     if (var21 != null) {
                        var21.SoundList.add(var13);
                     }
                  }
               }
            }

            this.SoundList.add(var13);
            ZombiePopulationManager.instance.addWorldSound(var13, var11);
         }

         if (var11) {
            if (GameClient.bClient) {
               GameClient.instance.sendWorldSound(var13);
            } else if (GameServer.bServer) {
               GameServer.sendWorldSound((WorldSoundManager.WorldSound)var13, (UdpConnection)null);
            }
         }

         if (Core.bDebug && GameClient.bClient) {
            MPDebugInfo.AddDebugSound(var13);
         }

         return var13;
      }
   }

   public WorldSoundManager.WorldSound addSoundRepeating(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      WorldSoundManager.WorldSound var8 = this.addSound(var1, var2, var3, var4, var5, var6, var7, 0.0F, 1.0F);
      if (var8 != null) {
         var8.bRepeating = true;
      }

      return var8;
   }

   public WorldSoundManager.WorldSound getSoundZomb(IsoZombie var1) {
      IsoChunk var2 = null;
      if (var1.soundSourceTarget == null) {
         return null;
      } else if (var1.getCurrentSquare() == null) {
         return null;
      } else {
         var2 = var1.getCurrentSquare().chunk;
         ArrayList var3 = null;
         if (var2 != null && !GameServer.bServer) {
            var3 = var2.SoundList;
         } else {
            var3 = this.SoundList;
         }

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            WorldSoundManager.WorldSound var5 = (WorldSoundManager.WorldSound)var3.get(var4);
            if (var1.soundSourceTarget == var5.source) {
               return var5;
            }
         }

         return null;
      }
   }

   public WorldSoundManager.ResultBiggestSound getBiggestSoundZomb(int var1, int var2, int var3, boolean var4, IsoZombie var5) {
      float var6 = -1000000.0F;
      WorldSoundManager.WorldSound var7 = null;
      IsoChunk var8 = null;
      if (var5 != null) {
         if (var5.getCurrentSquare() == null) {
            return resultBiggestSound.init((WorldSoundManager.WorldSound)null, 0.0F);
         }

         var8 = var5.getCurrentSquare().chunk;
      }

      ArrayList var9 = null;
      if (var8 != null && !GameServer.bServer) {
         var9 = var8.SoundList;
      } else {
         var9 = this.SoundList;
      }

      for(int var10 = 0; var10 < var9.size(); ++var10) {
         WorldSoundManager.WorldSound var11 = (WorldSoundManager.WorldSound)var9.get(var10);
         if (var11 != null && var11.radius != 0) {
            float var12 = IsoUtils.DistanceToSquared((float)var1, (float)var2, (float)var11.x, (float)var11.y);
            if (!(var12 > (float)(var11.radius * var11.radius)) && (!(var12 < var11.zombieIgnoreDist * var11.zombieIgnoreDist) || var3 != var11.z) && (!var4 || !var11.sourceIsZombie)) {
               IsoGridSquare var13 = IsoWorld.instance.CurrentCell.getGridSquare(var11.x, var11.y, var11.z);
               IsoGridSquare var14 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
               float var15 = var12 / (float)(var11.radius * var11.radius);
               if (var13 != null && var14 != null && var13.getRoom() != var14.getRoom()) {
                  var15 *= 1.2F;
                  if (var14.getRoom() == null || var13.getRoom() == null) {
                     var15 *= 1.4F;
                  }
               }

               var15 = 1.0F - var15;
               if (!(var15 <= 0.0F)) {
                  if (var15 > 1.0F) {
                     var15 = 1.0F;
                  }

                  float var16 = (float)var11.volume * var15;
                  if (var16 > var6) {
                     var6 = var16;
                     var7 = var11;
                  }
               }
            }
         }
      }

      return resultBiggestSound.init(var7, var6);
   }

   public float getSoundAttract(WorldSoundManager.WorldSound var1, IsoZombie var2) {
      if (var1 == null) {
         return 0.0F;
      } else if (var1.radius == 0) {
         return 0.0F;
      } else {
         float var3 = IsoUtils.DistanceToSquared(var2.x, var2.y, (float)var1.x, (float)var1.y);
         if (var3 > (float)(var1.radius * var1.radius)) {
            return 0.0F;
         } else if (var3 < var1.zombieIgnoreDist * var1.zombieIgnoreDist && var2.z == (float)var1.z) {
            return 0.0F;
         } else if (var1.sourceIsZombie) {
            return 0.0F;
         } else {
            IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1.x, var1.y, var1.z);
            IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare((double)var2.x, (double)var2.y, (double)var2.z);
            float var6 = var3 / (float)(var1.radius * var1.radius);
            if (var4 != null && var5 != null && var4.getRoom() != var5.getRoom()) {
               var6 *= 1.2F;
               if (var5.getRoom() == null || var4.getRoom() == null) {
                  var6 *= 1.4F;
               }
            }

            var6 = 1.0F - var6;
            if (var6 <= 0.0F) {
               return 0.0F;
            } else {
               if (var6 > 1.0F) {
                  var6 = 1.0F;
               }

               float var7 = (float)var1.volume * var6;
               return var7;
            }
         }
      }
   }

   public float getStressFromSounds(int var1, int var2, int var3) {
      float var4 = 0.0F;

      for(int var5 = 0; var5 < this.SoundList.size(); ++var5) {
         WorldSoundManager.WorldSound var6 = (WorldSoundManager.WorldSound)this.SoundList.get(var5);
         if (var6.stresshumans && var6.radius != 0) {
            float var7 = IsoUtils.DistanceManhatten((float)var1, (float)var2, (float)var6.x, (float)var6.y);
            float var8 = var7 / (float)var6.radius;
            var8 = 1.0F - var8;
            if (!(var8 <= 0.0F)) {
               if (var8 > 1.0F) {
                  var8 = 1.0F;
               }

               float var9 = var8 * var6.stressMod;
               var4 += var9;
            }
         }
      }

      return var4;
   }

   public void update() {
      int var1;
      if (!GameServer.bServer) {
         for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoChunkMap var2 = IsoWorld.instance.CurrentCell.ChunkMap[var1];
            if (!var2.ignore) {
               for(int var3 = 0; var3 < IsoChunkMap.ChunkGridWidth; ++var3) {
                  for(int var4 = 0; var4 < IsoChunkMap.ChunkGridWidth; ++var4) {
                     IsoChunk var5 = var2.getChunk(var4, var3);
                     if (var5 != null) {
                        var5.updateSounds();
                     }
                  }
               }
            }
         }
      }

      var1 = this.SoundList.size();

      for(int var6 = 0; var6 < var1; ++var6) {
         WorldSoundManager.WorldSound var7 = (WorldSoundManager.WorldSound)this.SoundList.get(var6);
         if (var7 != null && var7.life > 0) {
            --var7.life;
         } else {
            this.SoundList.remove(var6);
            this.freeSounds.push(var7);
            --var6;
            --var1;
         }
      }

   }

   public void render() {
      if (Core.bDebug && DebugOptions.instance.WorldSoundRender.getValue()) {
         if (!GameClient.bClient) {
            if (!GameServer.bServer || ServerGUI.isCreated()) {
               for(int var1 = 0; var1 < this.SoundList.size(); ++var1) {
                  WorldSoundManager.WorldSound var2 = (WorldSoundManager.WorldSound)this.SoundList.get(var1);

                  for(double var3 = 0.0D; var3 < 6.283185307179586D; var3 += 0.15707963267948966D) {
                     this.DrawIsoLine((float)var2.x + (float)var2.radius * (float)Math.cos(var3), (float)var2.y + (float)var2.radius * (float)Math.sin(var3), (float)var2.x + (float)var2.radius * (float)Math.cos(var3 + 0.15707963267948966D), (float)var2.y + (float)var2.radius * (float)Math.sin(var3 + 0.15707963267948966D), (float)var2.z, 1.0F, 1.0F, 1.0F, 1.0F, 1);
                  }
               }

               if (!GameServer.bServer) {
                  IsoChunkMap var13 = IsoWorld.instance.CurrentCell.getChunkMap(0);
                  if (var13 != null && !var13.ignore) {
                     for(int var14 = 0; var14 < IsoChunkMap.ChunkGridWidth; ++var14) {
                        for(int var15 = 0; var15 < IsoChunkMap.ChunkGridWidth; ++var15) {
                           IsoChunk var4 = var13.getChunk(var15, var14);
                           if (var4 != null) {
                              for(int var5 = 0; var5 < var4.SoundList.size(); ++var5) {
                                 WorldSoundManager.WorldSound var6 = (WorldSoundManager.WorldSound)var4.SoundList.get(var5);

                                 for(double var7 = 0.0D; var7 < 6.283185307179586D; var7 += 0.15707963267948966D) {
                                    this.DrawIsoLine((float)var6.x + (float)var6.radius * (float)Math.cos(var7), (float)var6.y + (float)var6.radius * (float)Math.sin(var7), (float)var6.x + (float)var6.radius * (float)Math.cos(var7 + 0.15707963267948966D), (float)var6.y + (float)var6.radius * (float)Math.sin(var7 + 0.15707963267948966D), (float)var6.z, 0.0F, 1.0F, 1.0F, 1.0F, 1);
                                    float var9 = (float)(var4.wx * 10) + 0.1F;
                                    float var10 = (float)(var4.wy * 10) + 0.1F;
                                    float var11 = (float)((var4.wx + 1) * 10) - 0.1F;
                                    float var12 = (float)((var4.wy + 1) * 10) - 0.1F;
                                    this.DrawIsoLine(var9, var10, var11, var10, (float)var6.z, 0.0F, 1.0F, 1.0F, 1.0F, 1);
                                    this.DrawIsoLine(var11, var10, var11, var12, (float)var6.z, 0.0F, 1.0F, 1.0F, 1.0F, 1);
                                    this.DrawIsoLine(var11, var12, var9, var12, (float)var6.z, 0.0F, 1.0F, 1.0F, 1.0F, 1);
                                    this.DrawIsoLine(var9, var12, var9, var10, (float)var6.z, 0.0F, 1.0F, 1.0F, 1.0F, 1);
                                 }
                              }
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }

   private void DrawIsoLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      float var11 = IsoUtils.XToScreenExact(var1, var2, var5, 0);
      float var12 = IsoUtils.YToScreenExact(var1, var2, var5, 0);
      float var13 = IsoUtils.XToScreenExact(var3, var4, var5, 0);
      float var14 = IsoUtils.YToScreenExact(var3, var4, var5, 0);
      LineDrawer.drawLine(var11, var12, var13, var14, var6, var7, var8, var9, var10);
   }

   public class WorldSound {
      public Object source = null;
      public int life = 1;
      public int radius;
      public boolean stresshumans;
      public int volume;
      public int x;
      public int y;
      public int z;
      public float zombieIgnoreDist = 0.0F;
      public boolean sourceIsZombie;
      public float stressMod = 1.0F;
      public boolean bRepeating;

      public WorldSoundManager.WorldSound init(Object var1, int var2, int var3, int var4, int var5, int var6) {
         return this.init(var1, var2, var3, var4, var5, var6, false, 0.0F, 1.0F);
      }

      public WorldSoundManager.WorldSound init(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
         return this.init(var1, var2, var3, var4, var5, var6, var7, 0.0F, 1.0F);
      }

      public WorldSoundManager.WorldSound init(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7, float var8, float var9) {
         this.source = var1;
         this.life = 1;
         this.x = var2;
         this.y = var3;
         this.z = var4;
         this.radius = var5;
         this.volume = var6;
         this.stresshumans = var7;
         this.zombieIgnoreDist = var8;
         this.stressMod = var9;
         this.sourceIsZombie = var1 instanceof IsoZombie;
         this.bRepeating = false;
         LuaEventManager.triggerEvent("OnWorldSound", var2, var3, var4, var5, var6, var1);
         return this;
      }

      public WorldSoundManager.WorldSound init(boolean var1, int var2, int var3, int var4, int var5, int var6, boolean var7, float var8, float var9) {
         WorldSoundManager.WorldSound var10 = this.init((Object)null, var2, var3, var4, var5, var6, var7, var8, var9);
         var10.sourceIsZombie = var1;
         return var10;
      }
   }

   public static final class ResultBiggestSound {
      public WorldSoundManager.WorldSound sound;
      public float attract;

      public WorldSoundManager.ResultBiggestSound init(WorldSoundManager.WorldSound var1, float var2) {
         this.sound = var1;
         this.attract = var2;
         return this;
      }
   }
}
