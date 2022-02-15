package zombie.iso.objects;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.GameTime;
import zombie.SystemDisabler;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.Shader;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.LosUtil;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.util.Type;
import zombie.util.list.PZArrayList;

public class IsoCurtain extends IsoObject {
   public boolean Barricaded = false;
   public Integer BarricideMaxStrength = 0;
   public Integer BarricideStrength = 0;
   public Integer Health = 1000;
   public boolean Locked = false;
   public Integer MaxHealth = 1000;
   public Integer PushedMaxStrength = 0;
   public Integer PushedStrength = 0;
   IsoSprite closedSprite;
   public boolean north = false;
   public boolean open = false;
   IsoSprite openSprite;
   private boolean destroyed = false;

   public void removeSheet(IsoGameCharacter var1) {
      this.square.transmitRemoveItemFromSquare(this);
      if (GameServer.bServer) {
         var1.sendObjectChange("addItemOfType", new Object[]{"type", "Base.Sheet"});
      } else {
         var1.getInventory().AddItem("Base.Sheet");
      }

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         LosUtil.cachecleared[var2] = true;
      }

      GameTime.instance.lightSourceUpdate = 100.0F;
      IsoGridSquare.setRecalcLightTime(-1);
   }

   public IsoCurtain(IsoCell var1, IsoGridSquare var2, IsoSprite var3, boolean var4, boolean var5) {
      this.OutlineOnMouseover = true;
      this.PushedMaxStrength = this.PushedStrength = 2500;
      if (var5) {
         this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, (IsoSprite)var3, 4);
         this.closedSprite = var3;
      } else {
         this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, (IsoSprite)var3, -4);
         this.openSprite = var3;
      }

      this.open = true;
      this.sprite = this.openSprite;
      this.square = var2;
      this.north = var4;
      this.DirtySlice();
   }

   public IsoCurtain(IsoCell var1, IsoGridSquare var2, String var3, boolean var4) {
      this.OutlineOnMouseover = true;
      this.PushedMaxStrength = this.PushedStrength = 2500;
      this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, (String)var3, -4);
      this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, (String)var3, 0);
      this.open = true;
      this.sprite = this.openSprite;
      this.square = var2;
      this.north = var4;
      this.DirtySlice();
   }

   public IsoCurtain(IsoCell var1) {
      super(var1);
   }

   public String getObjectName() {
      return "Curtain";
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      if (this.square == null) {
         return var1.set(0.0F, 0.0F);
      } else if (this.getType() == IsoObjectType.curtainS) {
         return var1.set(this.getX() + 0.5F, this.getY() + 1.0F);
      } else if (this.getType() == IsoObjectType.curtainE) {
         return var1.set(this.getX() + 1.0F, this.getY() + 0.5F);
      } else {
         return this.north ? var1.set(this.getX() + 0.5F, this.getY()) : var1.set(this.getX(), this.getY() + 0.5F);
      }
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.open = var1.get() == 1;
      this.north = var1.get() == 1;
      this.Health = var1.getInt();
      this.BarricideStrength = var1.getInt();
      if (this.open) {
         this.closedSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         this.openSprite = this.sprite;
      } else {
         this.openSprite = IsoSprite.getSprite(IsoSpriteManager.instance, var1.getInt());
         this.closedSprite = this.sprite;
      }

      if (SystemDisabler.doObjectStateSyncEnable && GameClient.bClient) {
         GameClient.instance.objectSyncReq.putRequestLoad(this.square);
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.open ? 1 : 0));
      var1.put((byte)(this.north ? 1 : 0));
      var1.putInt(this.Health);
      var1.putInt(this.BarricideStrength);
      if (this.open) {
         var1.putInt(this.closedSprite.ID);
      } else {
         var1.putInt(this.openSprite.ID);
      }

   }

   public boolean getNorth() {
      return this.north;
   }

   public boolean IsOpen() {
      return this.open;
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      return false;
   }

   public boolean canInteractWith(IsoGameCharacter var1) {
      if (var1 != null && var1.getCurrentSquare() != null) {
         IsoGridSquare var2 = var1.getCurrentSquare();
         return (this.isAdjacentToSquare(var2) || var2 == this.getOppositeSquare()) && !this.getSquare().isBlockedTo(var2);
      } else {
         return false;
      }
   }

   public IsoGridSquare getOppositeSquare() {
      if (this.getType() == IsoObjectType.curtainN) {
         return this.getCell().getGridSquare((double)this.getX(), (double)(this.getY() - 1.0F), (double)this.getZ());
      } else if (this.getType() == IsoObjectType.curtainS) {
         return this.getCell().getGridSquare((double)this.getX(), (double)(this.getY() + 1.0F), (double)this.getZ());
      } else if (this.getType() == IsoObjectType.curtainW) {
         return this.getCell().getGridSquare((double)(this.getX() - 1.0F), (double)this.getY(), (double)this.getZ());
      } else {
         return this.getType() == IsoObjectType.curtainE ? this.getCell().getGridSquare((double)(this.getX() + 1.0F), (double)this.getY(), (double)this.getZ()) : null;
      }
   }

   public boolean isAdjacentToSquare(IsoGridSquare var1, IsoGridSquare var2) {
      if (var1 != null && var2 != null) {
         if (this.getType() != IsoObjectType.curtainN && this.getType() != IsoObjectType.curtainS) {
            return var1.x == var2.x && Math.abs(var1.y - var2.y) <= 1;
         } else {
            return var1.y == var2.y && Math.abs(var1.x - var2.x) <= 1;
         }
      } else {
         return false;
      }
   }

   public boolean isAdjacentToSquare(IsoGridSquare var1) {
      return this.isAdjacentToSquare(this.getSquare(), var1);
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      if (var2.getZ() != var1.getZ()) {
         return IsoObject.VisionResult.NoEffect;
      } else {
         if (var1 == this.square && (this.getType() == IsoObjectType.curtainW || this.getType() == IsoObjectType.curtainN) || var1 != this.square && (this.getType() == IsoObjectType.curtainE || this.getType() == IsoObjectType.curtainS)) {
            if (this.north && var2.getY() < var1.getY() && !this.open) {
               return IsoObject.VisionResult.Blocked;
            }

            if (!this.north && var2.getX() < var1.getX() && !this.open) {
               return IsoObject.VisionResult.Blocked;
            }
         } else {
            if (this.north && var2.getY() > var1.getY() && !this.open) {
               return IsoObject.VisionResult.Blocked;
            }

            if (!this.north && var2.getX() > var1.getX() && !this.open) {
               return IsoObject.VisionResult.Blocked;
            }
         }

         return IsoObject.VisionResult.NoEffect;
      }
   }

   public void ToggleDoor(IsoGameCharacter var1) {
      if (!this.Barricaded) {
         this.DirtySlice();
         if (!this.Locked || var1 == null || var1.getCurrentSquare().getRoom() != null || this.open) {
            this.open = !this.open;
            this.sprite = this.closedSprite;
            if (this.open) {
               this.sprite = this.openSprite;
               if (var1 != null) {
                  var1.playSound(this.getSoundPrefix() + "Open");
               }
            } else if (var1 != null) {
               var1.playSound(this.getSoundPrefix() + "Close");
            }

            this.syncIsoObject(false, (byte)(this.open ? 1 : 0), (UdpConnection)null);
         }
      }
   }

   public void ToggleDoorSilent() {
      if (!this.Barricaded) {
         this.DirtySlice();

         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            LosUtil.cachecleared[var1] = true;
         }

         GameTime.instance.lightSourceUpdate = 100.0F;
         IsoGridSquare.setRecalcLightTime(-1);
         this.open = !this.open;
         this.sprite = this.closedSprite;
         if (this.open) {
            this.sprite = this.openSprite;
         }

         this.syncIsoObject(false, (byte)(this.open ? 1 : 0), (UdpConnection)null);
      }
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      int var8 = IsoCamera.frameState.playerIndex;
      IsoObject var9 = this.getObjectAttachedTo();
      if (var9 != null && this.getSquare().getTargetDarkMulti(var8) <= var9.getSquare().getTargetDarkMulti(var8)) {
         var4 = var9.getSquare().lighting[var8].lightInfo();
         this.setTargetAlpha(var8, var9.getTargetAlpha(var8));
      }

      super.render(var1, var2, var3, var4, var5, var6, var7);
   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      byte var2 = (byte)this.square.getObjects().indexOf(this);
      var1.putByte(var2);
      var1.putByte((byte)1);
      var1.putByte((byte)(this.open ? 1 : 0));
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      this.syncIsoObject(var1, var2, var3);
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3) {
      if (this.square == null) {
         System.out.println("ERROR: " + this.getClass().getSimpleName() + " square is null");
      } else if (this.getObjectIndex() == -1) {
         PrintStream var10000 = System.out;
         String var10001 = this.getClass().getSimpleName();
         var10000.println("ERROR: " + var10001 + " not found on square " + this.square.getX() + "," + this.square.getY() + "," + this.square.getZ());
      } else {
         if (GameClient.bClient && !var1) {
            ByteBufferWriter var7 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SyncIsoObject.doPacket(var7);
            this.syncIsoObjectSend(var7);
            PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
         } else if (var1) {
            if (var2 == 1) {
               this.open = true;
               this.sprite = this.openSprite;
            } else {
               this.open = false;
               this.sprite = this.closedSprite;
            }

            if (GameServer.bServer) {
               Iterator var4 = GameServer.udpEngine.connections.iterator();

               while(var4.hasNext()) {
                  UdpConnection var5 = (UdpConnection)var4.next();
                  if (var3 != null && var5.getConnectedGUID() != var3.getConnectedGUID()) {
                     ByteBufferWriter var6 = var5.startPacket();
                     PacketTypes.PacketType.SyncIsoObject.doPacket(var6);
                     this.syncIsoObjectSend(var6);
                     PacketTypes.PacketType.SyncIsoObject.send(var5);
                  }
               }
            }
         }

         this.square.RecalcProperties();
         this.square.RecalcAllWithNeighbours(true);

         for(int var8 = 0; var8 < IsoPlayer.numPlayers; ++var8) {
            LosUtil.cachecleared[var8] = true;
         }

         IsoGridSquare.setRecalcLightTime(-1);
         GameTime.instance.lightSourceUpdate = 100.0F;
         LuaEventManager.triggerEvent("OnContainerUpdate");
         if (this.square != null) {
            this.square.RecalcProperties();
         }

      }
   }

   public IsoObject getObjectAttachedTo() {
      int var1 = this.getObjectIndex();
      if (var1 == -1) {
         return null;
      } else {
         PZArrayList var2 = this.getSquare().getObjects();
         if (this.getType() != IsoObjectType.curtainW && this.getType() != IsoObjectType.curtainN) {
            if (this.getType() == IsoObjectType.curtainE || this.getType() == IsoObjectType.curtainS) {
               IsoGridSquare var7 = this.getOppositeSquare();
               if (var7 != null) {
                  boolean var8 = this.getType() == IsoObjectType.curtainS;
                  var2 = var7.getObjects();

                  for(int var9 = var2.size() - 1; var9 >= 0; --var9) {
                     BarricadeAble var6 = (BarricadeAble)Type.tryCastTo((IsoObject)var2.get(var9), BarricadeAble.class);
                     if (var6 != null && var8 == var6.getNorth()) {
                        return (IsoObject)var2.get(var9);
                     }
                  }
               }
            }
         } else {
            boolean var3 = this.getType() == IsoObjectType.curtainN;

            for(int var4 = var1 - 1; var4 >= 0; --var4) {
               BarricadeAble var5 = (BarricadeAble)Type.tryCastTo((IsoObject)var2.get(var4), BarricadeAble.class);
               if (var5 != null && var3 == var5.getNorth()) {
                  return (IsoObject)var2.get(var4);
               }
            }
         }

         return null;
      }
   }

   public String getSoundPrefix() {
      if (this.closedSprite == null) {
         return "CurtainShort";
      } else {
         PropertyContainer var1 = this.closedSprite.getProperties();
         return var1.Is("CurtainSound") ? "Curtain" + var1.Val("CurtainSound") : "CurtainShort";
      }
   }

   public static boolean isSheet(IsoObject var0) {
      if (var0 instanceof IsoDoor) {
         var0 = ((IsoDoor)var0).HasCurtains();
      }

      if (var0 instanceof IsoThumpable) {
         var0 = ((IsoThumpable)var0).HasCurtains();
      }

      if (var0 instanceof IsoWindow) {
         var0 = ((IsoWindow)var0).HasCurtains();
      }

      if (var0 != null && ((IsoObject)var0).getSprite() != null) {
         IsoSprite var1 = ((IsoObject)var0).getSprite();
         return var1.getProperties().Is("CurtainSound") ? "Sheet".equals(var1.getProperties().Val("CurtainSound")) : false;
      } else {
         return false;
      }
   }
}
