package zombie;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;

public final class ZomboidBitFlag {
   final EnumSet isoFlagTypeES = EnumSet.noneOf(IsoFlagType.class);

   public ZomboidBitFlag(int var1) {
   }

   public ZomboidBitFlag(ZomboidBitFlag var1) {
      if (var1 != null) {
         this.isoFlagTypeES.addAll(var1.isoFlagTypeES);
      }
   }

   public void set(int var1, boolean var2) {
      if (var1 < IsoFlagType.MAX.index()) {
         if (var2) {
            this.isoFlagTypeES.add(IsoFlagType.fromIndex(var1));
         } else {
            this.isoFlagTypeES.remove(IsoFlagType.fromIndex(var1));
         }

      }
   }

   public void clear() {
      this.isoFlagTypeES.clear();
   }

   public boolean isSet(int var1) {
      return this.isoFlagTypeES.contains(IsoFlagType.fromIndex(var1));
   }

   public boolean isSet(IsoFlagType var1) {
      return this.isoFlagTypeES.contains(var1);
   }

   public void set(IsoFlagType var1, boolean var2) {
      if (var2) {
         this.isoFlagTypeES.add(var1);
      } else {
         this.isoFlagTypeES.remove(var1);
      }

   }

   public boolean isSet(IsoObjectType var1) {
      return this.isSet(var1.index());
   }

   public void set(IsoObjectType var1, boolean var2) {
      this.set(var1.index(), var2);
   }

   public void Or(ZomboidBitFlag var1) {
      this.isoFlagTypeES.addAll(var1.isoFlagTypeES);
   }

   public void save(DataOutputStream var1) throws IOException {
   }

   public void load(DataInputStream var1) throws IOException {
   }

   public void getFromLong(long var1) {
   }
}
