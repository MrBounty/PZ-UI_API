package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.network.packets.INetworkPacket;

public class NetObject implements INetworkPacket {
   public final byte objectTypeNone = 0;
   public final byte objectTypeObject = 1;
   private boolean isProcessed = false;
   private byte objectType = 0;
   private short objectId;
   private int squareX;
   private int squareY;
   private byte squareZ;
   private IsoObject object;

   public void setObject(IsoObject var1) {
      this.object = var1;
      this.isProcessed = true;
      if (this.object == null) {
         this.objectType = 0;
         this.objectId = 0;
      } else {
         IsoGridSquare var2 = this.object.square;
         this.objectType = 1;
         this.objectId = (short)var2.getObjects().indexOf(this.object);
         this.squareX = var2.getX();
         this.squareY = var2.getY();
         this.squareZ = (byte)var2.getZ();
      }
   }

   public IsoObject getObject() {
      if (!this.isProcessed) {
         if (this.objectType == 0) {
            this.object = null;
         }

         if (this.objectType == 1) {
            IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.squareX, this.squareY, this.squareZ);
            if (var1 == null) {
               this.object = null;
            } else {
               this.object = (IsoObject)var1.getObjects().get(this.objectId);
            }
         }

         this.isProcessed = true;
      }

      return this.object;
   }

   public void parse(ByteBuffer var1) {
      this.objectType = var1.get();
      if (this.objectType == 1) {
         this.objectId = var1.getShort();
         this.squareX = var1.getInt();
         this.squareY = var1.getInt();
         this.squareZ = var1.get();
      }

      this.isProcessed = false;
   }

   public void write(ByteBufferWriter var1) {
      var1.putByte(this.objectType);
      if (this.objectType == 1) {
         var1.putShort(this.objectId);
         var1.putInt(this.squareX);
         var1.putInt(this.squareY);
         var1.putByte(this.squareZ);
      }

   }

   public int getPacketSizeBytes() {
      return this.objectType == 1 ? 12 : 1;
   }

   public String getDescription() {
      String var1 = "";
      switch(this.objectType) {
      case 0:
         var1 = "None";
         break;
      case 1:
         var1 = "NetObject";
      }

      return "\n\tNetObject [type=" + var1 + "(" + this.objectType + ") | id=" + this.objectId + " | pos=(" + this.squareX + ", " + this.squareY + ", " + this.squareZ + ") ]";
   }
}
