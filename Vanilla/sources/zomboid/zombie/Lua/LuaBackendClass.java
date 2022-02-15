package zombie.Lua;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;

public class LuaBackendClass implements KahluaTable {
   KahluaTable table;
   KahluaTable typeTable;

   public String getString(String var1) {
      return (String)this.rawget(var1);
   }

   public LuaBackendClass(String var1) {
      this.typeTable = (KahluaTable)LuaManager.env.rawget(var1);
   }

   public void callVoid(String var1) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), (Object)this.table);
   }

   public void callVoid(String var1, Object var2) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), new Object[]{this.table, var2});
   }

   public void callVoid(String var1, Object var2, Object var3) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), new Object[]{this.table, var2, var3});
   }

   public void callVoid(String var1, Object var2, Object var3, Object var4) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), new Object[]{this.table, var2, var3, var4});
   }

   public void callVoid(String var1, Object var2, Object var3, Object var4, Object var5) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), new Object[]{this.table, var2, var3, var4, var5});
   }

   public void callVoid(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      LuaManager.caller.pcallvoid(LuaManager.thread, this.typeTable.rawget(var1), new Object[]{this.table, var2, var3, var4, var5, var6});
   }

   public Object call(String var1) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), (Object)this.table)[1];
   }

   public Object call(String var1, Object var2) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2)[1];
   }

   public Object call(String var1, Object var2, Object var3) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3)[1];
   }

   public Object call(String var1, Object var2, Object var3, Object var4) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4)[1];
   }

   public Object call(String var1, Object var2, Object var3, Object var4, Object var5) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5)[1];
   }

   public Object call(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5, var6)[1];
   }

   public int callInt(String var1) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), (Object)this.table)[1]).intValue();
   }

   public int callInt(String var1, Object var2) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2)[1]).intValue();
   }

   public int callInt(String var1, Object var2, Object var3) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3)[1]).intValue();
   }

   public int callInt(String var1, Object var2, Object var3, Object var4) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4)[1]).intValue();
   }

   public int callInt(String var1, Object var2, Object var3, Object var4, Object var5) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5)[1]).intValue();
   }

   public int callInt(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5, var6)[1]).intValue();
   }

   public float callFloat(String var1) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), (Object)this.table)[1]).floatValue();
   }

   public float callFloat(String var1, Object var2) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2)[1]).floatValue();
   }

   public float callFloat(String var1, Object var2, Object var3) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3)[1]).floatValue();
   }

   public float callFloat(String var1, Object var2, Object var3, Object var4) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4)[1]).floatValue();
   }

   public float callFloat(String var1, Object var2, Object var3, Object var4, Object var5) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5)[1]).floatValue();
   }

   public float callFloat(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return ((Double)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5, var6)[1]).floatValue();
   }

   public boolean callBool(String var1) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), (Object)this.table)[1];
   }

   public boolean callBool(String var1, Object var2) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2)[1];
   }

   public boolean callBool(String var1, Object var2, Object var3) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3)[1];
   }

   public boolean callBool(String var1, Object var2, Object var3, Object var4) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4)[1];
   }

   public boolean callBool(String var1, Object var2, Object var3, Object var4, Object var5) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5)[1];
   }

   public boolean callBool(String var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return (Boolean)LuaManager.caller.pcall(LuaManager.thread, this.typeTable.rawget(var1), this.table, var2, var3, var4, var5, var6)[1];
   }

   public void setMetatable(KahluaTable var1) {
      this.table.setMetatable(var1);
   }

   public KahluaTable getMetatable() {
      return this.table.getMetatable();
   }

   public void rawset(Object var1, Object var2) {
      this.table.rawset(var1, var2);
   }

   public Object rawget(Object var1) {
      return this.table.rawget(var1);
   }

   public void rawset(int var1, Object var2) {
      this.table.rawset(var1, var2);
   }

   public Object rawget(int var1) {
      return this.table.rawget(var1);
   }

   public int len() {
      return this.table.len();
   }

   public int size() {
      return this.table.len();
   }

   public KahluaTableIterator iterator() {
      return this.table.iterator();
   }

   public boolean isEmpty() {
      return this.table.isEmpty();
   }

   public void wipe() {
      this.table.wipe();
   }

   public void save(ByteBuffer var1) throws IOException {
      this.table.save(var1);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.table.load(var1, var2);
   }

   public void save(DataOutputStream var1) throws IOException {
      this.table.save(var1);
   }

   public void load(DataInputStream var1, int var2) throws IOException {
      this.table.load(var1, var2);
   }
}
