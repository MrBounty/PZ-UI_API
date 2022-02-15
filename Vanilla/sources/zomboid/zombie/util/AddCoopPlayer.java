package zombie.util;

import zombie.SoundManager;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.physics.WorldSimulation;
import zombie.debug.DebugLog;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LosUtil;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.popman.ZombiePopulationManager;
import zombie.ui.UIManager;

public final class AddCoopPlayer {
   private AddCoopPlayer.Stage stage;
   private IsoPlayer player;

   public AddCoopPlayer(IsoPlayer var1) {
      this.stage = AddCoopPlayer.Stage.Init;
      this.player = var1;
   }

   public void update() {
      IsoCell var1;
      int var4;
      ByteBufferWriter var18;
      switch(this.stage) {
      case Init:
         if (GameClient.bClient) {
            var18 = GameClient.connection.startPacket();
            PacketTypes.PacketType.AddCoopPlayer.doPacket(var18);
            var18.putByte((byte)1);
            var18.putByte((byte)this.player.PlayerIndex);
            var18.putUTF(this.player.username != null ? this.player.username : "");
            var18.putFloat(this.player.x);
            var18.putFloat(this.player.y);
            PacketTypes.PacketType.AddCoopPlayer.send(GameClient.connection);
            this.stage = AddCoopPlayer.Stage.ReceiveClientConnect;
         } else {
            this.stage = AddCoopPlayer.Stage.StartMapLoading;
         }
      case ReceiveClientConnect:
      case ReceivePlayerConnect:
      case Finished:
      default:
         break;
      case StartMapLoading:
         var1 = IsoWorld.instance.CurrentCell;
         int var19 = this.player.PlayerIndex;
         IsoChunkMap var21 = var1.ChunkMap[var19];
         IsoChunkMap.bSettingChunk.lock();

         try {
            var21.Unload();
            var21.ignore = false;
            var4 = (int)(this.player.x / 10.0F);
            int var5 = (int)(this.player.y / 10.0F);

            try {
               if (LightingJNI.init) {
                  LightingJNI.teleport(var19, var4 - IsoChunkMap.ChunkGridWidth / 2, var5 - IsoChunkMap.ChunkGridWidth / 2);
               }
            } catch (Exception var16) {
            }

            if (!GameServer.bServer && !GameClient.bClient) {
               ZombiePopulationManager.instance.playerSpawnedAt((int)this.player.x, (int)this.player.y, (int)this.player.z);
            }

            var21.WorldX = var4;
            var21.WorldY = var5;
            WorldSimulation.instance.activateChunkMap(var19);
            int var6 = var4 - IsoChunkMap.ChunkGridWidth / 2;
            int var7 = var5 - IsoChunkMap.ChunkGridWidth / 2;
            int var8 = var4 + IsoChunkMap.ChunkGridWidth / 2 + 1;
            int var9 = var5 + IsoChunkMap.ChunkGridWidth / 2 + 1;
            int var10 = var6;

            while(true) {
               if (var10 >= var8) {
                  var21.SwapChunkBuffers();
                  break;
               }

               for(int var11 = var7; var11 < var9; ++var11) {
                  if (IsoWorld.instance.getMetaGrid().isValidChunk(var10, var11)) {
                     IsoChunk var12 = var21.LoadChunkForLater(var10, var11, var10 - var6, var11 - var7);
                     if (var12 != null && var12.bLoaded) {
                        var1.setCacheChunk(var12, var19);
                     }
                  }
               }

               ++var10;
            }
         } finally {
            IsoChunkMap.bSettingChunk.unlock();
         }

         this.stage = AddCoopPlayer.Stage.CheckMapLoading;
         break;
      case CheckMapLoading:
         var1 = IsoWorld.instance.CurrentCell;
         IsoChunkMap var2 = var1.ChunkMap[this.player.PlayerIndex];
         var2.update();

         for(int var3 = 0; var3 < IsoChunkMap.ChunkGridWidth; ++var3) {
            for(var4 = 0; var4 < IsoChunkMap.ChunkGridWidth; ++var4) {
               if (IsoWorld.instance.getMetaGrid().isValidChunk(var2.getWorldXMin() + var4, var2.getWorldYMin() + var3) && var2.getChunk(var4, var3) == null) {
                  return;
               }
            }
         }

         IsoGridSquare var20 = var1.getGridSquare((int)this.player.x, (int)this.player.y, (int)this.player.z);
         if (var20 != null && var20.getRoom() != null) {
            var20.getRoom().def.setExplored(true);
            var20.getRoom().building.setAllExplored(true);
         }

         this.stage = GameClient.bClient ? AddCoopPlayer.Stage.SendPlayerConnect : AddCoopPlayer.Stage.AddToWorld;
         break;
      case SendPlayerConnect:
         var18 = GameClient.connection.startPacket();
         PacketTypes.PacketType.AddCoopPlayer.doPacket(var18);
         var18.putByte((byte)2);
         var18.putByte((byte)this.player.PlayerIndex);
         GameClient.instance.writePlayerConnectData(var18, this.player);
         PacketTypes.PacketType.AddCoopPlayer.send(GameClient.connection);
         this.stage = AddCoopPlayer.Stage.ReceivePlayerConnect;
         break;
      case AddToWorld:
         IsoPlayer.players[this.player.PlayerIndex] = this.player;
         LosUtil.cachecleared[this.player.PlayerIndex] = true;
         this.player.updateLightInfo();
         var1 = IsoWorld.instance.CurrentCell;
         this.player.setCurrent(var1.getGridSquare((int)this.player.x, (int)this.player.y, (int)this.player.z));
         this.player.updateUsername();
         this.player.setSceneCulled(false);
         if (var1.isSafeToAdd()) {
            var1.getObjectList().add(this.player);
         } else {
            var1.getAddList().add(this.player);
         }

         this.player.getInventory().addItemsToProcessItems();
         LuaEventManager.triggerEvent("OnCreatePlayer", this.player.PlayerIndex, this.player);
         if (this.player.isAsleep()) {
            UIManager.setFadeBeforeUI(this.player.PlayerIndex, true);
            UIManager.FadeOut((double)this.player.PlayerIndex, 2.0D);
            UIManager.setFadeTime((double)this.player.PlayerIndex, 0.0D);
         }

         this.stage = AddCoopPlayer.Stage.Finished;
         SoundManager.instance.stopMusic(IsoPlayer.DEATH_MUSIC_NAME);
      }

   }

   public boolean isFinished() {
      return this.stage == AddCoopPlayer.Stage.Finished;
   }

   public void accessGranted(int var1) {
      if (this.player.PlayerIndex == var1) {
         DebugLog.log("coop player=" + (var1 + 1) + "/4 access granted");
         this.stage = AddCoopPlayer.Stage.StartMapLoading;
      }

   }

   public void accessDenied(int var1, String var2) {
      if (this.player.PlayerIndex == var1) {
         DebugLog.log("coop player=" + (var1 + 1) + "/4 access denied: " + var2);
         IsoCell var3 = IsoWorld.instance.CurrentCell;
         int var4 = this.player.PlayerIndex;
         IsoChunkMap var5 = var3.ChunkMap[var4];
         var5.Unload();
         var5.ignore = true;
         this.stage = AddCoopPlayer.Stage.Finished;
         LuaEventManager.triggerEvent("OnCoopJoinFailed", var1);
      }

   }

   public void receivePlayerConnect(int var1) {
      if (this.player.PlayerIndex == var1) {
         this.stage = AddCoopPlayer.Stage.AddToWorld;
         this.update();
      }

   }

   public boolean isLoadingThisSquare(int var1, int var2) {
      int var3 = (int)(this.player.x / 10.0F);
      int var4 = (int)(this.player.y / 10.0F);
      int var5 = var3 - IsoChunkMap.ChunkGridWidth / 2;
      int var6 = var4 - IsoChunkMap.ChunkGridWidth / 2;
      int var7 = var5 + IsoChunkMap.ChunkGridWidth;
      int var8 = var6 + IsoChunkMap.ChunkGridWidth;
      var1 /= 10;
      var2 /= 10;
      return var1 >= var5 && var1 < var7 && var2 >= var6 && var2 < var8;
   }

   public static enum Stage {
      Init,
      ReceiveClientConnect,
      StartMapLoading,
      CheckMapLoading,
      SendPlayerConnect,
      ReceivePlayerConnect,
      AddToWorld,
      Finished;

      // $FF: synthetic method
      private static AddCoopPlayer.Stage[] $values() {
         return new AddCoopPlayer.Stage[]{Init, ReceiveClientConnect, StartMapLoading, CheckMapLoading, SendPlayerConnect, ReceivePlayerConnect, AddToWorld, Finished};
      }
   }
}
