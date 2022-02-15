package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.network.NetworkVariables;

public class PlayerPacket implements INetworkPacket {
   public static final int PACKET_SIZE_BYTES = 43;
   public short id;
   public float x;
   public float y;
   public byte z;
   public float direction;
   public boolean usePathFinder;
   public NetworkVariables.PredictionTypes moveType;
   public short VehicleID;
   public short VehicleSeat;
   public int booleanVariables;
   public byte footstepSoundRadius;
   public byte bleedingLevel;
   public float realx;
   public float realy;
   public byte realz;
   public byte realdir;
   public int realt;
   public float collidePointX;
   public float collidePointY;
   public PlayerVariables variables = new PlayerVariables();

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.get();
      this.direction = var1.getFloat();
      this.usePathFinder = var1.get() == 1;
      this.moveType = NetworkVariables.PredictionTypes.fromByte(var1.get());
      this.VehicleID = var1.getShort();
      this.VehicleSeat = var1.getShort();
      this.booleanVariables = var1.getInt();
      this.footstepSoundRadius = var1.get();
      this.bleedingLevel = var1.get();
      this.realx = var1.getFloat();
      this.realy = var1.getFloat();
      this.realz = var1.get();
      this.realdir = var1.get();
      this.realt = var1.getInt();
      this.collidePointX = var1.getFloat();
      this.collidePointY = var1.getFloat();
      this.variables.parse(var1);
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putByte(this.z);
      var1.putFloat(this.direction);
      var1.putBoolean(this.usePathFinder);
      var1.putByte((byte)this.moveType.ordinal());
      var1.putShort(this.VehicleID);
      var1.putShort(this.VehicleSeat);
      var1.putInt(this.booleanVariables);
      var1.putByte(this.footstepSoundRadius);
      var1.putByte(this.bleedingLevel);
      var1.putFloat(this.realx);
      var1.putFloat(this.realy);
      var1.putByte(this.realz);
      var1.putByte(this.realdir);
      var1.putInt(this.realt);
      var1.putFloat(this.collidePointX);
      var1.putFloat(this.collidePointY);
      this.variables.write(var1);
   }

   public int getPacketSizeBytes() {
      return 43;
   }

   public boolean set(IsoPlayer var1) {
      this.id = var1.OnlineID;
      this.bleedingLevel = var1.bleedingLevel;
      this.variables.set(var1);
      return var1.networkAI.set(this);
   }

   public void copy(PlayerPacket var1) {
      this.id = var1.id;
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.direction = var1.direction;
      this.usePathFinder = var1.usePathFinder;
      this.moveType = var1.moveType;
      this.VehicleID = var1.VehicleID;
      this.VehicleSeat = var1.VehicleSeat;
      this.booleanVariables = var1.booleanVariables;
      this.footstepSoundRadius = var1.footstepSoundRadius;
      this.bleedingLevel = var1.bleedingLevel;
      this.realx = var1.realx;
      this.realy = var1.realy;
      this.realz = var1.realz;
      this.realdir = var1.realdir;
      this.realt = var1.realt;
      this.collidePointX = var1.collidePointX;
      this.collidePointY = var1.collidePointY;
      this.variables.copy(var1.variables);
   }

   public static class l_send {
      public static PlayerPacket playerPacket = new PlayerPacket();
   }

   public static class l_receive {
      public static PlayerPacket playerPacket = new PlayerPacket();
   }
}
