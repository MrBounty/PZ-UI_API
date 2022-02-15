package zombie.radio.globals;

import java.util.HashMap;
import java.util.Map;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;

public final class RadioGlobalsManager {
   private final Map globals = new HashMap();
   private final RadioGlobalInt bufferInt = new RadioGlobalInt("bufferInt", 0);
   private final RadioGlobalString bufferString = new RadioGlobalString("bufferString", "");
   private final RadioGlobalBool bufferBoolean = new RadioGlobalBool("bufferBoolean", false);
   private final RadioGlobalFloat bufferFloat = new RadioGlobalFloat("bufferFloat", 0.0F);
   private static RadioGlobalsManager instance;

   public static RadioGlobalsManager getInstance() {
      if (instance == null) {
         instance = new RadioGlobalsManager();
      }

      return instance;
   }

   private RadioGlobalsManager() {
   }

   public void reset() {
      instance = null;
   }

   public boolean exists(String var1) {
      return this.globals.containsKey(var1);
   }

   public RadioGlobalType getType(String var1) {
      return this.globals.containsKey(var1) ? ((RadioGlobal)this.globals.get(var1)).getType() : RadioGlobalType.Invalid;
   }

   public String getString(String var1) {
      RadioGlobal var2 = this.getGlobal(var1);
      return var2 != null ? var2.getString() : null;
   }

   public boolean addGlobal(String var1, RadioGlobal var2) {
      if (!this.exists(var1) && var2 != null) {
         this.globals.put(var1, var2);
         return true;
      } else {
         DebugLog.log(DebugType.Radio, "Error adding global: " + var1 + " to globals (already exists or global==null)");
         return false;
      }
   }

   public boolean addGlobalString(String var1, String var2) {
      return this.addGlobal(var1, new RadioGlobalString(var1, var2));
   }

   public boolean addGlobalBool(String var1, boolean var2) {
      return this.addGlobal(var1, new RadioGlobalBool(var1, var2));
   }

   public boolean addGlobalInt(String var1, int var2) {
      return this.addGlobal(var1, new RadioGlobalInt(var1, var2));
   }

   public boolean addGlobalFloat(String var1, float var2) {
      return this.addGlobal(var1, new RadioGlobalFloat(var1, var2));
   }

   public RadioGlobal getGlobal(String var1) {
      return this.exists(var1) ? (RadioGlobal)this.globals.get(var1) : null;
   }

   public RadioGlobalString getGlobalString(String var1) {
      RadioGlobal var2 = this.getGlobal(var1);
      return var2 != null && var2 instanceof RadioGlobalString ? (RadioGlobalString)var2 : null;
   }

   public RadioGlobalInt getGlobalInt(String var1) {
      RadioGlobal var2 = this.getGlobal(var1);
      return var2 != null && var2 instanceof RadioGlobalInt ? (RadioGlobalInt)var2 : null;
   }

   public RadioGlobalFloat getGlobalFloat(String var1) {
      RadioGlobal var2 = this.getGlobal(var1);
      return var2 != null && var2 instanceof RadioGlobalFloat ? (RadioGlobalFloat)var2 : null;
   }

   public RadioGlobalBool getGlobalBool(String var1) {
      RadioGlobal var2 = this.getGlobal(var1);
      return var2 != null && var2 instanceof RadioGlobalBool ? (RadioGlobalBool)var2 : null;
   }

   public boolean setGlobal(String var1, RadioGlobal var2, EditGlobalOps var3) {
      RadioGlobal var4 = this.getGlobal(var1);
      return var4 != null && var2 != null ? var4.setValue(var2, var3) : false;
   }

   public boolean setGlobal(String var1, String var2) {
      this.bufferString.setValue(var2);
      return this.setGlobal(var1, this.bufferString, EditGlobalOps.set);
   }

   public boolean setGlobal(String var1, int var2) {
      this.bufferInt.setValue(var2);
      return this.setGlobal(var1, this.bufferInt, EditGlobalOps.set);
   }

   public boolean setGlobal(String var1, float var2) {
      this.bufferFloat.setValue(var2);
      return this.setGlobal(var1, this.bufferFloat, EditGlobalOps.set);
   }

   public boolean setGlobal(String var1, boolean var2) {
      this.bufferBoolean.setValue(var2);
      return this.setGlobal(var1, this.bufferBoolean, EditGlobalOps.set);
   }

   public CompareResult compare(RadioGlobal var1, RadioGlobal var2, CompareMethod var3) {
      return var1 != null && var2 != null && var1.getType().equals(var2.getType()) ? var1.compare(var2, var3) : CompareResult.Invalid;
   }

   public CompareResult compare(String var1, String var2, CompareMethod var3) {
      return this.compare(this.getGlobal(var1), this.getGlobal(var2), var3);
   }
}
