package zombie.radio.devices;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameWindow;
import zombie.Lua.LuaManager;

public final class DevicePresets implements Cloneable {
   protected int maxPresets = 10;
   protected ArrayList presets = new ArrayList();

   protected Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public KahluaTable getPresetsLua() {
      KahluaTable var1 = LuaManager.platform.newTable();

      for(int var2 = 0; var2 < this.presets.size(); ++var2) {
         PresetEntry var3 = (PresetEntry)this.presets.get(var2);
         KahluaTable var4 = LuaManager.platform.newTable();
         var4.rawset("name", var3.name);
         var4.rawset("frequency", var3.frequency);
         var1.rawset(var2, var4);
      }

      return var1;
   }

   public ArrayList getPresets() {
      return this.presets;
   }

   public void setPresets(ArrayList var1) {
      this.presets = var1;
   }

   public int getMaxPresets() {
      return this.maxPresets;
   }

   public void setMaxPresets(int var1) {
      this.maxPresets = var1;
   }

   public void addPreset(String var1, int var2) {
      if (this.presets.size() < this.maxPresets) {
         this.presets.add(new PresetEntry(var1, var2));
      }

   }

   public void removePreset(int var1) {
      if (this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size()) {
         this.presets.remove(var1);
      }

   }

   public String getPresetName(int var1) {
      return this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size() ? ((PresetEntry)this.presets.get(var1)).name : "";
   }

   public int getPresetFreq(int var1) {
      return this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size() ? ((PresetEntry)this.presets.get(var1)).frequency : -1;
   }

   public void setPresetName(int var1, String var2) {
      if (var2 == null) {
         var2 = "name-is-null";
      }

      if (this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size()) {
         PresetEntry var3 = (PresetEntry)this.presets.get(var1);
         var3.name = var2;
      }

   }

   public void setPresetFreq(int var1, int var2) {
      if (this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size()) {
         PresetEntry var3 = (PresetEntry)this.presets.get(var1);
         var3.frequency = var2;
      }

   }

   public void setPreset(int var1, String var2, int var3) {
      if (var2 == null) {
         var2 = "name-is-null";
      }

      if (this.presets.size() != 0 && var1 >= 0 && var1 < this.presets.size()) {
         PresetEntry var4 = (PresetEntry)this.presets.get(var1);
         var4.name = var2;
         var4.frequency = var3;
      }

   }

   public void clearPresets() {
      this.presets.clear();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      var1.putInt(this.maxPresets);
      var1.putInt(this.presets.size());

      for(int var3 = 0; var3 < this.presets.size(); ++var3) {
         PresetEntry var4 = (PresetEntry)this.presets.get(var3);
         GameWindow.WriteString(var1, var4.name);
         var1.putInt(var4.frequency);
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      if (var2 >= 69) {
         this.clearPresets();
         this.maxPresets = var1.getInt();
         int var4 = var1.getInt();

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = GameWindow.ReadString(var1);
            int var7 = var1.getInt();
            if (this.presets.size() < this.maxPresets) {
               this.presets.add(new PresetEntry(var6, var7));
            }
         }
      }

   }
}
