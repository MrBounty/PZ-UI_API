package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleManager;

public class MovingObject implements INetworkPacket {
   public final byte objectTypeNone = 0;
   public final byte objectTypePlayer = 1;
   public final byte objectTypeZombie = 2;
   public final byte objectTypeVehicle = 3;
   public final byte objectTypeObject = 4;
   private boolean isProcessed = false;
   private byte objectType = 0;
   private short objectId;
   private int squareX;
   private int squareY;
   private byte squareZ;
   private IsoMovingObject object;

   public void setMovingObject(IsoMovingObject var1) {
      this.object = var1;
      this.isProcessed = true;
      if (this.object == null) {
         this.objectType = 0;
         this.objectId = 0;
      } else if (this.object instanceof IsoPlayer) {
         this.objectType = 1;
         this.objectId = ((IsoPlayer)this.object).getOnlineID();
      } else if (this.object instanceof IsoZombie) {
         this.objectType = 2;
         this.objectId = ((IsoZombie)this.object).getOnlineID();
      } else if (this.object instanceof BaseVehicle) {
         this.objectType = 3;
         this.objectId = ((BaseVehicle)this.object).VehicleID;
      } else {
         IsoGridSquare var2 = this.object.getCurrentSquare();
         this.objectType = 4;
         this.objectId = (short)var2.getMovingObjects().indexOf(this.object);
         this.squareX = var2.getX();
         this.squareY = var2.getY();
         this.squareZ = (byte)var2.getZ();
      }
   }

   public IsoMovingObject getMovingObject() {
      if (!this.isProcessed) {
         if (this.objectType == 0) {
            this.object = null;
         }

         if (this.objectType == 1) {
            if (GameServer.bServer) {
               this.object = (IsoMovingObject)GameServer.IDToPlayerMap.get(this.objectId);
            } else if (GameClient.bClient) {
               this.object = (IsoMovingObject)GameClient.IDToPlayerMap.get(this.objectId);
            }
         }

         if (this.objectType == 2) {
            if (GameServer.bServer) {
               this.object = ServerMap.instance.ZombieMap.get(this.objectId);
            } else if (GameClient.bClient) {
               this.object = (IsoMovingObject)GameClient.IDToZombieMap.get(this.objectId);
            }
         }

         if (this.objectType == 3) {
            this.object = VehicleManager.instance.getVehicleByID(this.objectId);
         }

         if (this.objectType == 4) {
            IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.squareX, this.squareY, this.squareZ);
            if (var1 == null) {
               this.object = null;
            } else {
               this.object = (IsoMovingObject)var1.getMovingObjects().get(this.objectId);
            }
         }

         this.isProcessed = true;
      }

      return this.object;
   }

   public void parse(ByteBuffer var1) {
      this.objectType = var1.get();
      this.objectId = var1.getShort();
      if (this.objectType == 4) {
         this.squareX = var1.getInt();
         this.squareY = var1.getInt();
         this.squareZ = var1.get();
      }

      this.isProcessed = false;
   }

   public void write(ByteBufferWriter var1) {
      var1.putByte(this.objectType);
      var1.putShort(this.objectId);
      if (this.objectType == 4) {
         var1.putInt(this.squareX);
         var1.putInt(this.squareY);
         var1.putByte(this.squareZ);
      }

   }

   public int getPacketSizeBytes() {
      return this.objectType == 4 ? 12 : 3;
   }

   public String getDescription() {
      String var1 = "";
      switch(this.objectType) {
      case 0:
         var1 = "None";
         break;
      case 1:
         var1 = "Player";
         break;
      case 2:
         var1 = "Zombie";
         break;
      case 3:
         var1 = "Vehicle";
         break;
      case 4:
         var1 = "NetObject";
      }

      return this.objectType == 4 ? "\n\tMovingObject [type=" + var1 + "(" + this.objectType + ") | id=" + this.objectId + " | position=(" + this.squareX + ", " + this.squareY + ", " + this.squareZ + ") ]" : "\n\tMovingObject [type=" + var1 + "(" + this.objectType + ") | id=" + this.objectId + "]";
   }
}
