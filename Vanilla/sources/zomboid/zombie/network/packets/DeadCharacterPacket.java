package zombie.network.packets;

import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoDeadBody;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public abstract class DeadCharacterPacket implements INetworkPacket {
   protected short id;
   protected float x;
   protected float y;
   protected float z;
   protected float angle;
   protected IsoDirections direction;
   protected byte characterFlags;
   protected IsoGameCharacter killer;
   protected IsoGameCharacter character;

   public void set(IsoGameCharacter var1) {
      this.character = var1;
      this.id = var1.getOnlineID();
      this.killer = var1.getAttackedBy();
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
      this.angle = var1.getAnimAngleRadians();
      this.direction = var1.getDir();
      this.characterFlags = (byte)(var1.isFallOnFront() ? 1 : 0);
   }

   public void process() {
      if (this.character != null) {
         IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)this.y, (double)this.z);
         if (this.character.getCurrentSquare() != var1) {
            DebugLog.Multiplayer.warn(String.format("Corpse %s(%d) teleport: position (%f ; %f) => (%f ; %f)", this.character.getClass().getSimpleName(), this.id, this.character.x, this.character.y, this.x, this.y));
            this.character.setX(this.x);
            this.character.setY(this.y);
            this.character.setZ(this.z);
         }

         if (this.character.getAnimAngleRadians() - this.angle > 1.0E-4F) {
            DebugLog.Multiplayer.warn(String.format("Corpse %s(%d) teleport: direction (%f) => (%f)", this.character.getClass().getSimpleName(), this.id, this.character.getAnimAngleRadians(), this.angle));
            if (this.character.hasAnimationPlayer() && this.character.getAnimationPlayer().isReady() && !this.character.getAnimationPlayer().isBoneTransformsNeedFirstFrame()) {
               this.character.getAnimationPlayer().setAngle(this.angle);
            } else {
               this.character.getForwardDirection().setDirection(this.angle);
            }
         }

         boolean var2 = (this.characterFlags & 1) != 0;
         if (var2 != this.character.isFallOnFront()) {
            DebugLog.Multiplayer.warn(String.format("Corpse %s(%d) teleport: pose (%s) => (%s)", this.character.getClass().getSimpleName(), this.id, this.character.isFallOnFront() ? "front" : "back", var2 ? "front" : "back"));
            this.character.setFallOnFront(var2);
         }

         this.character.setCurrent(var1);
         this.character.dir = this.direction;
         this.character.setAttackedBy(this.killer);
         this.character.becomeCorpse();
      }

   }

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.getFloat();
      this.angle = var1.getFloat();
      this.direction = IsoDirections.fromIndex(var1.get());
      this.characterFlags = var1.get();
      byte var2 = var1.get();
      boolean var3 = true;
      Exception var4;
      short var5;
      if (GameServer.bServer) {
         switch(var2) {
         case 0:
            this.killer = null;
            break;
         case 1:
            var5 = var1.getShort();
            this.killer = ServerMap.instance.ZombieMap.get(var5);
            break;
         case 2:
            var5 = var1.getShort();
            this.killer = (IsoGameCharacter)GameServer.IDToPlayerMap.get(var5);
            break;
         default:
            var4 = new Exception("killerIdType:" + var2);
            var4.printStackTrace();
         }
      } else {
         switch(var2) {
         case 0:
            this.killer = null;
            break;
         case 1:
            var5 = var1.getShort();
            this.killer = (IsoGameCharacter)GameClient.IDToZombieMap.get(var5);
            break;
         case 2:
            var5 = var1.getShort();
            this.killer = (IsoGameCharacter)GameClient.IDToPlayerMap.get(var5);
            break;
         default:
            var4 = new Exception("killerIdType:" + var2);
            var4.printStackTrace();
         }
      }

   }

   public void parseDeadBodyInventory(ByteBuffer var1) {
      IsoGridSquare var2 = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)this.y, (double)this.z);
      if (var2 != null) {
         Iterator var3 = var2.getStaticMovingObjects().iterator();

         while(var3.hasNext()) {
            IsoMovingObject var4 = (IsoMovingObject)var3.next();
            if (var4 instanceof IsoDeadBody && ((IsoDeadBody)var4).getOnlineID() == this.id) {
               String var5 = ((IsoDeadBody)var4).readInventory(var1);
               var4.getContainer().setType(var5);
               break;
            }
         }
      }

   }

   protected void parseCharacterInventory(ByteBuffer var1) {
      if (this.character != null) {
         if (this.character.getContainer() != null) {
            this.character.getContainer().clear();
         }

         if (this.character.getInventory() != null) {
            this.character.getInventory().clear();
         }

         if (this.character.getWornItems() != null) {
            this.character.getWornItems().clear();
         }

         if (this.character.getAttachedItems() != null) {
            this.character.getAttachedItems().clear();
         }

         this.character.getInventory().setSourceGrid(this.character.getCurrentSquare());
         String var2 = this.character.readInventory(var1);
         this.character.getInventory().setType(var2);
         this.character.resetModelNextFrame();
      }

   }

   public void writeCharacterInventory(ByteBufferWriter var1) {
      if (this.character != null) {
         this.character.writeInventory(var1.bb);
      }

   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putFloat(this.z);
      var1.putFloat(this.angle);
      var1.putByte((byte)this.direction.index());
      var1.putByte(this.characterFlags);
      if (this.killer == null) {
         var1.putByte((byte)0);
      } else {
         if (this.killer instanceof IsoZombie) {
            var1.putByte((byte)1);
         } else {
            var1.putByte((byte)2);
         }

         var1.putShort(this.killer.getOnlineID());
      }

   }

   public String getDescription() {
      String var1 = this.getDeathDescription() + "\n\t";
      if (this.character != null) {
         var1 = var1 + " isDead=" + this.character.isDead();
         var1 = var1 + " isOnDeathDone=" + this.character.isOnDeathDone();
         var1 = var1 + " isOnKillDone=" + this.character.isOnKillDone();
         var1 = var1 + " | health=" + this.character.getHealth();
         if (this.character.getBodyDamage() != null) {
            var1 = var1 + " | bodyDamage=" + this.character.getBodyDamage().getOverallBodyHealth();
         }

         var1 = var1 + " | states=( " + this.character.getPreviousActionContextStateName() + " > " + this.character.getCurrentActionContextStateName() + " )";
      }

      return var1;
   }

   public String getDeathDescription() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + " id(" + this.id + ") | killer=" + (this.killer == null ? "Null" : this.killer.getClass().getSimpleName() + "(" + this.killer.getOnlineID() + ")") + " | pos=(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ";a=" + this.angle + ") | dir=" + this.direction.name() + " | isFallOnFront=" + ((this.characterFlags & 1) != 0);
   }

   public boolean isConsistent() {
      return this.character != null;
   }
}
