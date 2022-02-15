package zombie.world;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import zombie.GameWindow;
import zombie.core.utils.Bits;
import zombie.debug.DebugLog;
import zombie.scripting.objects.Item;

public class ItemInfo {
   protected String itemName;
   protected String moduleName;
   protected String fullType;
   protected short registryID;
   protected boolean existsAsVanilla = false;
   protected boolean isModded = false;
   protected String modID;
   protected boolean obsolete = false;
   protected boolean removed = false;
   protected boolean isLoaded = false;
   protected List modOverrides;
   protected Item scriptItem;

   public String getFullType() {
      return this.fullType;
   }

   public short getRegistryID() {
      return this.registryID;
   }

   public boolean isExistsAsVanilla() {
      return this.existsAsVanilla;
   }

   public boolean isModded() {
      return this.isModded;
   }

   public String getModID() {
      return this.modID;
   }

   public boolean isObsolete() {
      return this.obsolete;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public boolean isLoaded() {
      return this.isLoaded;
   }

   public Item getScriptItem() {
      return this.scriptItem;
   }

   public List getModOverrides() {
      return this.modOverrides;
   }

   public ItemInfo copy() {
      ItemInfo var1 = new ItemInfo();
      var1.fullType = this.fullType;
      var1.registryID = this.registryID;
      var1.existsAsVanilla = this.existsAsVanilla;
      var1.isModded = this.isModded;
      var1.modID = this.modID;
      var1.obsolete = this.obsolete;
      var1.removed = this.removed;
      var1.isLoaded = this.isLoaded;
      var1.scriptItem = this.scriptItem;
      if (this.modOverrides != null) {
         var1.modOverrides = new ArrayList();
         var1.modOverrides.addAll(this.modOverrides);
      }

      return var1;
   }

   public boolean isValid() {
      return !this.obsolete && !this.removed && this.isLoaded;
   }

   public void DebugPrint() {
      DebugLog.log(this.GetDebugString());
   }

   public String GetDebugString() {
      short var10000 = this.registryID;
      String var1 = "=== Dictionary Item Debug Print ===\nregistryID = " + var10000 + ",\nfulltype = \"" + this.fullType + "\",\nmodID = \"" + this.modID + "\",\nexistsAsVanilla = " + this.existsAsVanilla + ",\nisModded = " + this.isModded + ",\nobsolete = " + this.obsolete + ",\nremoved = " + this.removed + ",\nisModdedOverride = " + (this.modOverrides != null ? this.modOverrides.size() : 0) + ",\n";
      if (this.modOverrides != null) {
         var1 = var1 + "modOverrides = { ";
         if (this.existsAsVanilla) {
            var1 = var1 + "PZ-Vanilla, ";
         }

         for(int var2 = 0; var2 < this.modOverrides.size(); ++var2) {
            var1 = var1 + "\"" + (String)this.modOverrides.get(var2) + "\"";
            if (var2 < this.modOverrides.size() - 1) {
               var1 = var1 + ", ";
            }
         }

         var1 = var1 + " },\n";
      }

      var1 = "===================================\n";
      return var1;
   }

   public String ToString() {
      short var10000 = this.registryID;
      return "registryID = " + var10000 + ",fulltype = \"" + this.fullType + "\",modID = \"" + this.modID + "\",existsAsVanilla = " + this.existsAsVanilla + ",isModded = " + this.isModded + ",obsolete = " + this.obsolete + ",removed = " + this.removed + ",modOverrides = " + (this.modOverrides != null ? this.modOverrides.size() : 0) + ",";
   }

   protected void saveAsText(FileWriter var1, String var2) throws IOException {
      var1.write(var2 + "registryID = " + this.registryID + "," + System.lineSeparator());
      var1.write(var2 + "fulltype = \"" + this.fullType + "\"," + System.lineSeparator());
      var1.write(var2 + "modID = \"" + this.modID + "\"," + System.lineSeparator());
      var1.write(var2 + "existsAsVanilla = " + this.existsAsVanilla + "," + System.lineSeparator());
      var1.write(var2 + "isModded = " + this.isModded + "," + System.lineSeparator());
      var1.write(var2 + "obsolete = " + this.obsolete + "," + System.lineSeparator());
      var1.write(var2 + "removed = " + this.removed + "," + System.lineSeparator());
      if (this.modOverrides != null) {
         String var3 = "modOverrides = { ";

         for(int var4 = 0; var4 < this.modOverrides.size(); ++var4) {
            var3 = var3 + "\"" + (String)this.modOverrides.get(var4) + "\"";
            if (var4 < this.modOverrides.size() - 1) {
               var3 = var3 + ", ";
            }
         }

         var3 = var3 + " },";
         var1.write(var2 + var3 + System.lineSeparator());
      }

   }

   protected void save(ByteBuffer var1, List var2, List var3) {
      var1.putShort(this.registryID);
      if (var3.size() > 127) {
         var1.putShort((short)var3.indexOf(this.moduleName));
      } else {
         var1.put((byte)var3.indexOf(this.moduleName));
      }

      GameWindow.WriteString(var1, this.itemName);
      byte var4 = 0;
      int var5 = var1.position();
      var1.put((byte)0);
      if (this.isModded) {
         var4 = Bits.addFlags((byte)var4, 1);
         if (var2.size() > 127) {
            var1.putShort((short)var2.indexOf(this.modID));
         } else {
            var1.put((byte)var2.indexOf(this.modID));
         }
      }

      if (this.existsAsVanilla) {
         var4 = Bits.addFlags((byte)var4, 2);
      }

      if (this.obsolete) {
         var4 = Bits.addFlags((byte)var4, 4);
      }

      if (this.removed) {
         var4 = Bits.addFlags((byte)var4, 8);
      }

      int var6;
      if (this.modOverrides != null) {
         var4 = Bits.addFlags((byte)var4, 16);
         if (this.modOverrides.size() == 1) {
            if (var2.size() > 127) {
               var1.putShort((short)var2.indexOf(this.modOverrides.get(0)));
            } else {
               var1.put((byte)var2.indexOf(this.modOverrides.get(0)));
            }
         } else {
            var4 = Bits.addFlags((byte)var4, 32);
            var1.put((byte)this.modOverrides.size());

            for(var6 = 0; var6 < this.modOverrides.size(); ++var6) {
               if (var2.size() > 127) {
                  var1.putShort((short)var2.indexOf(this.modOverrides.get(var6)));
               } else {
                  var1.put((byte)var2.indexOf(this.modOverrides.get(var6)));
               }
            }
         }
      }

      var6 = var1.position();
      var1.position(var5);
      var1.put(var4);
      var1.position(var6);
   }

   protected void load(ByteBuffer var1, int var2, List var3, List var4) {
      this.registryID = var1.getShort();
      this.moduleName = (String)var4.get(var4.size() > 127 ? var1.getShort() : var1.get());
      this.itemName = GameWindow.ReadString(var1);
      this.fullType = this.moduleName + "." + this.itemName;
      byte var5 = var1.get();
      if (Bits.hasFlags((byte)var5, 1)) {
         this.modID = (String)var3.get(var3.size() > 127 ? var1.getShort() : var1.get());
         this.isModded = true;
      } else {
         this.modID = "pz-vanilla";
         this.isModded = false;
      }

      this.existsAsVanilla = Bits.hasFlags((byte)var5, 2);
      this.obsolete = Bits.hasFlags((byte)var5, 4);
      this.removed = Bits.hasFlags((byte)var5, 8);
      if (Bits.hasFlags((byte)var5, 16)) {
         if (this.modOverrides == null) {
            this.modOverrides = new ArrayList();
         }

         this.modOverrides.clear();
         if (!Bits.hasFlags((byte)var5, 32)) {
            this.modOverrides.add((String)var3.get(var3.size() > 127 ? var1.getShort() : var1.get()));
         } else {
            byte var6 = var1.get();

            for(int var7 = 0; var7 < var6; ++var7) {
               this.modOverrides.add((String)var3.get(var3.size() > 127 ? var1.getShort() : var1.get()));
            }
         }
      }

   }
}
