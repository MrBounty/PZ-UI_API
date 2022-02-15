package se.krka.kahlua.j2se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.ui.UIManager;

public final class KahluaTableImpl implements KahluaTable {
   public final Map delegate;
   private KahluaTable metatable;
   private KahluaTable reloadReplace;
   private static final byte SBYT_NO_SAVE = -1;
   private static final byte SBYT_STRING = 0;
   private static final byte SBYT_DOUBLE = 1;
   private static final byte SBYT_TABLE = 2;
   private static final byte SBYT_BOOLEAN = 3;

   public KahluaTableImpl(Map var1) {
      this.delegate = var1;
   }

   public void setMetatable(KahluaTable var1) {
      this.metatable = var1;
   }

   public KahluaTable getMetatable() {
      return this.metatable;
   }

   public int size() {
      return this.delegate.size();
   }

   public void rawset(Object var1, Object var2) {
      if (this.reloadReplace != null) {
         this.reloadReplace.rawset(var1, var2);
      }

      Object var3 = null;
      if (Core.bDebug && LuaManager.thread != null && LuaManager.thread.hasDataBreakpoint(this, var1)) {
         var3 = this.rawget(var1);
      }

      if (var2 == null) {
         if (Core.bDebug && LuaManager.thread != null && LuaManager.thread.hasDataBreakpoint(this, var1) && var3 != null) {
            UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)LuaManager.thread.lastLine);
         }

         this.delegate.remove(var1);
      } else {
         if (Core.bDebug && LuaManager.thread != null && LuaManager.thread.hasDataBreakpoint(this, var1) && !var2.equals(var3)) {
            int var4 = LuaManager.GlobalObject.getCurrentCoroutine().currentCallFrame().pc;
            if (var4 < 0) {
               var4 = 0;
            }

            UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.GlobalObject.getCurrentCoroutine().currentCallFrame().closure.prototype.lines[var4] - 1));
         }

         this.delegate.put(var1, var2);
      }
   }

   public Object rawget(Object var1) {
      if (this.reloadReplace != null) {
         return this.reloadReplace.rawget(var1);
      } else if (var1 == null) {
         return null;
      } else {
         if (Core.bDebug && LuaManager.thread != null && LuaManager.thread.hasReadDataBreakpoint(this, var1)) {
            int var2 = LuaManager.GlobalObject.getCurrentCoroutine().currentCallFrame().pc;
            if (var2 < 0) {
               var2 = 0;
            }

            UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.GlobalObject.getCurrentCoroutine().currentCallFrame().closure.prototype.lines[var2] - 1));
         }

         return !this.delegate.containsKey(var1) && this.metatable != null ? this.metatable.rawget(var1) : this.delegate.get(var1);
      }
   }

   public void rawset(int var1, Object var2) {
      this.rawset(KahluaUtil.toDouble((long)var1), var2);
   }

   public String rawgetStr(Object var1) {
      return (String)this.rawget(var1);
   }

   public int rawgetInt(Object var1) {
      return this.rawget(var1) instanceof Double ? ((Double)this.rawget(var1)).intValue() : -1;
   }

   public boolean rawgetBool(Object var1) {
      return this.rawget(var1) instanceof Boolean ? (Boolean)this.rawget(var1) : false;
   }

   public float rawgetFloat(Object var1) {
      return this.rawget(var1) instanceof Double ? ((Double)this.rawget(var1)).floatValue() : -1.0F;
   }

   public Object rawget(int var1) {
      return this.rawget(KahluaUtil.toDouble((long)var1));
   }

   public int len() {
      return KahluaUtil.len(this, 0, 2 * this.delegate.size());
   }

   public KahluaTableIterator iterator() {
      final Object[] var1 = this.delegate.isEmpty() ? null : this.delegate.keySet().toArray();
      return new KahluaTableIterator() {
         private Object curKey;
         private Object curValue;
         private int keyIndex;

         public int call(LuaCallFrame var1x, int var2) {
            return this.advance() ? var1x.push(this.getKey(), this.getValue()) : 0;
         }

         public boolean advance() {
            if (var1 != null && this.keyIndex < var1.length) {
               this.curKey = var1[this.keyIndex];
               this.curValue = KahluaTableImpl.this.delegate.get(this.curKey);
               ++this.keyIndex;
               return true;
            } else {
               this.curKey = null;
               this.curValue = null;
               return false;
            }
         }

         public Object getKey() {
            return this.curKey;
         }

         public Object getValue() {
            return this.curValue;
         }
      };
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public void wipe() {
      this.delegate.clear();
   }

   public String toString() {
      return "table 0x" + System.identityHashCode(this);
   }

   public void save(ByteBuffer var1) {
      KahluaTableIterator var2 = this.iterator();
      int var3 = 0;

      while(var2.advance()) {
         if (canSave(var2.getKey(), var2.getValue())) {
            ++var3;
         }
      }

      var2 = this.iterator();
      var1.putInt(var3);

      while(var2.advance()) {
         byte var4 = getKeyByte(var2.getKey());
         byte var5 = getValueByte(var2.getValue());
         if (var4 != -1 && var5 != -1) {
            this.save(var1, var4, var2.getKey());
            this.save(var1, var5, var2.getValue());
         }
      }

   }

   private void save(ByteBuffer var1, byte var2, Object var3) throws RuntimeException {
      var1.put(var2);
      if (var2 == 0) {
         GameWindow.WriteString(var1, (String)var3);
      } else if (var2 == 1) {
         var1.putDouble((Double)var3);
      } else if (var2 == 3) {
         var1.put((byte)((Boolean)var3 ? 1 : 0));
      } else {
         if (var2 != 2) {
            throw new RuntimeException("invalid lua table type " + var2);
         }

         ((KahluaTableImpl)var3).save(var1);
      }

   }

   public void save(DataOutputStream var1) throws IOException {
      KahluaTableIterator var2 = this.iterator();
      int var3 = 0;

      while(var2.advance()) {
         if (canSave(var2.getKey(), var2.getValue())) {
            ++var3;
         }
      }

      var2 = this.iterator();
      var1.writeInt(var3);

      while(var2.advance()) {
         byte var4 = getKeyByte(var2.getKey());
         byte var5 = getValueByte(var2.getValue());
         if (var4 != -1 && var5 != -1) {
            this.save(var1, var4, var2.getKey());
            this.save(var1, var5, var2.getValue());
         }
      }

   }

   private void save(DataOutputStream var1, byte var2, Object var3) throws IOException, RuntimeException {
      var1.writeByte(var2);
      if (var2 == 0) {
         GameWindow.WriteString(var1, (String)var3);
      } else if (var2 == 1) {
         var1.writeDouble((Double)var3);
      } else if (var2 == 3) {
         var1.writeByte((Boolean)var3 ? 1 : 0);
      } else {
         if (var2 != 2) {
            throw new RuntimeException("invalid lua table type " + var2);
         }

         ((KahluaTableImpl)var3).save(var1);
      }

   }

   public void load(ByteBuffer var1, int var2) {
      int var3 = var1.getInt();
      this.wipe();
      int var4;
      byte var5;
      if (var2 >= 25) {
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var1.get();
            Object var6 = this.load(var1, var2, var5);
            byte var7 = var1.get();
            Object var8 = this.load(var1, var2, var7);
            this.rawset(var6, var8);
         }
      } else {
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var1.get();
            String var9 = GameWindow.ReadString(var1);
            Object var10 = this.load(var1, var2, var5);
            this.rawset(var9, var10);
         }
      }

   }

   public Object load(ByteBuffer var1, int var2, byte var3) throws RuntimeException {
      if (var3 == 0) {
         return GameWindow.ReadString(var1);
      } else if (var3 == 1) {
         return var1.getDouble();
      } else if (var3 == 3) {
         return var1.get() == 1;
      } else if (var3 == 2) {
         KahluaTableImpl var4 = (KahluaTableImpl)LuaManager.platform.newTable();
         var4.load(var1, var2);
         return var4;
      } else {
         throw new RuntimeException("invalid lua table type " + var3);
      }
   }

   public void load(DataInputStream var1, int var2) throws IOException {
      int var3 = var1.readInt();
      int var4;
      byte var5;
      if (var2 >= 25) {
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var1.readByte();
            Object var6 = this.load(var1, var2, var5);
            byte var7 = var1.readByte();
            Object var8 = this.load(var1, var2, var7);
            this.rawset(var6, var8);
         }
      } else {
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var1.readByte();
            String var9 = GameWindow.ReadString(var1);
            Object var10 = this.load(var1, var2, var5);
            this.rawset(var9, var10);
         }
      }

   }

   public Object load(DataInputStream var1, int var2, byte var3) throws IOException, RuntimeException {
      if (var3 == 0) {
         return GameWindow.ReadString(var1);
      } else if (var3 == 1) {
         return var1.readDouble();
      } else if (var3 == 3) {
         return var1.readByte() == 1;
      } else if (var3 == 2) {
         KahluaTableImpl var4 = (KahluaTableImpl)LuaManager.platform.newTable();
         var4.load(var1, var2);
         return var4;
      } else {
         throw new RuntimeException("invalid lua table type " + var3);
      }
   }

   public String getString(String var1) {
      return (String)this.rawget(var1);
   }

   public KahluaTableImpl getRewriteTable() {
      return (KahluaTableImpl)this.reloadReplace;
   }

   public void setRewriteTable(Object var1) {
      this.reloadReplace = (KahluaTableImpl)var1;
   }

   private static byte getKeyByte(Object var0) {
      if (var0 instanceof String) {
         return 0;
      } else {
         return (byte)(var0 instanceof Double ? 1 : -1);
      }
   }

   private static byte getValueByte(Object var0) {
      if (var0 instanceof String) {
         return 0;
      } else if (var0 instanceof Double) {
         return 1;
      } else if (var0 instanceof Boolean) {
         return 3;
      } else {
         return (byte)(var0 instanceof KahluaTableImpl ? 2 : -1);
      }
   }

   public static boolean canSave(Object var0, Object var1) {
      return getKeyByte(var0) != -1 && getValueByte(var1) != -1;
   }
}
