package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;

public class PlayerVariables implements INetworkPacket {
   byte count = 0;
   PlayerVariables.NetworkPlayerVariable[] variables = new PlayerVariables.NetworkPlayerVariable[2];

   public PlayerVariables() {
      for(byte var1 = 0; var1 < this.variables.length; ++var1) {
         this.variables[var1] = new PlayerVariables.NetworkPlayerVariable();
      }

   }

   public void set(IsoPlayer var1) {
      String var2 = var1.getActionStateName();
      if (var2.equals("idle")) {
         this.variables[0].set(var1, PlayerVariables.NetworkPlayerVariableIDs.IdleSpeed);
         this.count = 1;
      } else if (var2.equals("maskingleft") || var2.equals("maskingright") || var2.equals("movement") || var2.equals("run") || var2.equals("sprint")) {
         this.variables[0].set(var1, PlayerVariables.NetworkPlayerVariableIDs.WalkInjury);
         this.variables[1].set(var1, PlayerVariables.NetworkPlayerVariableIDs.WalkSpeed);
         this.count = 2;
      }

   }

   public void apply(IsoPlayer var1) {
      for(byte var2 = 0; var2 < this.count; ++var2) {
         var1.setVariable(this.variables[var2].id.name(), this.variables[var2].value);
      }

   }

   public void parse(ByteBuffer var1) {
      this.count = var1.get();

      for(byte var2 = 0; var2 < this.count; ++var2) {
         this.variables[var2].id = PlayerVariables.NetworkPlayerVariableIDs.values()[var1.get()];
         this.variables[var2].value = var1.getFloat();
      }

   }

   public void write(ByteBufferWriter var1) {
      var1.putByte(this.count);

      for(byte var2 = 0; var2 < this.count; ++var2) {
         var1.putByte((byte)this.variables[var2].id.ordinal());
         var1.putFloat(this.variables[var2].value);
      }

   }

   public int getPacketSizeBytes() {
      return 1 + this.count * 5;
   }

   public String getDescription() {
      String var1 = "PlayerVariables: ";
      var1 = var1 + "count=" + this.count + " | ";

      for(byte var2 = 0; var2 < this.count; ++var2) {
         var1 = var1 + "id=" + this.variables[var2].id.name() + ", ";
         var1 = var1 + "value=" + this.variables[var2].value + " | ";
      }

      return var1;
   }

   public void copy(PlayerVariables var1) {
      this.count = var1.count;

      for(byte var2 = 0; var2 < this.count; ++var2) {
         this.variables[var2].id = var1.variables[var2].id;
         this.variables[var2].value = var1.variables[var2].value;
      }

   }

   private class NetworkPlayerVariable {
      PlayerVariables.NetworkPlayerVariableIDs id;
      float value;

      public void set(IsoPlayer var1, PlayerVariables.NetworkPlayerVariableIDs var2) {
         this.id = var2;
         this.value = var1.getVariableFloat(var2.name(), 0.0F);
      }
   }

   private static enum NetworkPlayerVariableIDs {
      IdleSpeed,
      WalkInjury,
      WalkSpeed,
      DeltaX,
      DeltaY,
      AttackVariationX,
      AttackVariationY,
      targetDist,
      autoShootVarX,
      autoShootVarY,
      recoilVarX,
      recoilVarY,
      ShoveAimX,
      ShoveAimY;

      // $FF: synthetic method
      private static PlayerVariables.NetworkPlayerVariableIDs[] $values() {
         return new PlayerVariables.NetworkPlayerVariableIDs[]{IdleSpeed, WalkInjury, WalkSpeed, DeltaX, DeltaY, AttackVariationX, AttackVariationY, targetDist, autoShootVarX, autoShootVarY, recoilVarX, recoilVarY, ShoveAimX, ShoveAimY};
      }
   }
}
