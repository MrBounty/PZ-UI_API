package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.network.NetworkVariables;

public class ZombiePacket implements INetworkPacket {
   private static final int PACKET_SIZE_BYTES = 55;
   public short id;
   public float x;
   public float y;
   public byte z;
   public int descriptorID;
   public NetworkVariables.PredictionTypes moveType;
   public short booleanVariables;
   public short target;
   public int timeSinceSeenFlesh;
   public int smParamTargetAngle;
   public short speedMod;
   public NetworkVariables.WalkType walkType;
   public float realX;
   public float realY;
   public byte realZ;
   public short realHealth;
   public NetworkVariables.ZombieState realState;
   public byte pfbType;
   public short pfbTarget;
   public float pfbTargetX;
   public float pfbTargetY;
   public byte pfbTargetZ;

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.get();
      this.descriptorID = var1.getInt();
      this.moveType = NetworkVariables.PredictionTypes.fromByte(var1.get());
      this.booleanVariables = var1.getShort();
      this.target = var1.getShort();
      this.timeSinceSeenFlesh = var1.getInt();
      this.smParamTargetAngle = var1.getInt();
      this.speedMod = var1.getShort();
      this.walkType = NetworkVariables.WalkType.fromByte(var1.get());
      this.realX = var1.getFloat();
      this.realY = var1.getFloat();
      this.realZ = var1.get();
      this.realHealth = var1.getShort();
      this.realState = NetworkVariables.ZombieState.fromByte(var1.get());
      this.pfbType = var1.get();
      if (this.pfbType == 1) {
         this.pfbTarget = var1.getShort();
      } else if (this.pfbType > 1) {
         this.pfbTargetX = var1.getFloat();
         this.pfbTargetY = var1.getFloat();
         this.pfbTargetZ = var1.get();
      }

   }

   public void write(ByteBuffer var1) {
      var1.putShort(this.id);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.put(this.z);
      var1.putInt(this.descriptorID);
      var1.put((byte)this.moveType.ordinal());
      var1.putShort(this.booleanVariables);
      var1.putShort(this.target);
      var1.putInt(this.timeSinceSeenFlesh);
      var1.putInt(this.smParamTargetAngle);
      var1.putShort(this.speedMod);
      var1.put((byte)this.walkType.ordinal());
      var1.putFloat(this.realX);
      var1.putFloat(this.realY);
      var1.put(this.realZ);
      var1.putShort(this.realHealth);
      var1.put((byte)this.realState.ordinal());
      var1.put(this.pfbType);
      if (this.pfbType == 1) {
         var1.putShort(this.pfbTarget);
      } else if (this.pfbType > 1) {
         var1.putFloat(this.pfbTargetX);
         var1.putFloat(this.pfbTargetY);
         var1.put(this.pfbTargetZ);
      }

   }

   public void write(ByteBufferWriter var1) {
      this.write(var1.bb);
   }

   public int getPacketSizeBytes() {
      return 55;
   }

   public void copy(ZombiePacket var1) {
      this.id = var1.id;
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.descriptorID = var1.descriptorID;
      this.moveType = var1.moveType;
      this.booleanVariables = var1.booleanVariables;
      this.target = var1.target;
      this.timeSinceSeenFlesh = var1.timeSinceSeenFlesh;
      this.smParamTargetAngle = var1.smParamTargetAngle;
      this.speedMod = var1.speedMod;
      this.walkType = var1.walkType;
      this.realX = var1.realX;
      this.realY = var1.realY;
      this.realZ = var1.realZ;
      this.realHealth = var1.realHealth;
      this.realState = var1.realState;
      this.pfbType = var1.pfbType;
      this.pfbTarget = var1.pfbTarget;
      this.pfbTargetX = var1.pfbTargetX;
      this.pfbTargetY = var1.pfbTargetY;
      this.pfbTargetZ = var1.pfbTargetZ;
   }

   public void set(IsoZombie var1) {
      this.id = var1.OnlineID;
      this.descriptorID = var1.getPersistentOutfitID();
      var1.networkAI.set(this);
      var1.networkAI.mindSync.set(this);
      var1.thumpSent = true;
   }
}
