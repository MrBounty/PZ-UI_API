package se.krka.kahlua.vm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Prototype {
   public int[] code;
   public Object[] constants;
   public Prototype[] prototypes;
   public int numParams;
   public boolean isVararg;
   public String name;
   public int[] lines;
   public int numUpvalues;
   public int maxStacksize;
   public String file;
   public String filename;
   public String[] locvars;
   public int[] locvarlines;

   public Prototype() {
   }

   public Prototype(DataInputStream var1, boolean var2, String var3, int var4) throws IOException {
      this.name = readLuaString(var1, var4, var2);
      if (this.name == null) {
         this.name = var3;
      }

      var1.readInt();
      var1.readInt();
      this.numUpvalues = var1.read();
      this.numParams = var1.read();
      int var6 = var1.read();
      this.isVararg = (var6 & 2) != 0;
      this.maxStacksize = var1.read();
      int var7 = toInt(var1.readInt(), var2);
      this.code = new int[var7];

      int var8;
      int var9;
      for(var8 = 0; var8 < var7; ++var8) {
         var9 = toInt(var1.readInt(), var2);
         this.code[var8] = var9;
      }

      var8 = toInt(var1.readInt(), var2);
      this.constants = new Object[var8];

      int var11;
      for(var9 = 0; var9 < var8; ++var9) {
         Object var10 = null;
         var11 = var1.read();
         switch(var11) {
         case 0:
            break;
         case 1:
            int var12 = var1.read();
            var10 = var12 == 0 ? Boolean.FALSE : Boolean.TRUE;
            break;
         case 2:
         default:
            throw new IOException("unknown constant type: " + var11);
         case 3:
            long var13 = var1.readLong();
            if (var2) {
               var13 = rev(var13);
            }

            var10 = KahluaUtil.toDouble(Double.longBitsToDouble(var13));
            break;
         case 4:
            var10 = readLuaString(var1, var4, var2);
         }

         this.constants[var9] = var10;
      }

      var9 = toInt(var1.readInt(), var2);
      this.prototypes = new Prototype[var9];

      int var15;
      for(var15 = 0; var15 < var9; ++var15) {
         this.prototypes[var15] = new Prototype(var1, var2, this.name, var4);
      }

      int var5 = toInt(var1.readInt(), var2);
      this.lines = new int[var5];

      for(var15 = 0; var15 < var5; ++var15) {
         var11 = toInt(var1.readInt(), var2);
         this.lines[var15] = var11;
      }

      var5 = toInt(var1.readInt(), var2);

      for(var15 = 0; var15 < var5; ++var15) {
         readLuaString(var1, var4, var2);
         var1.readInt();
         var1.readInt();
      }

      var5 = toInt(var1.readInt(), var2);

      for(var15 = 0; var15 < var5; ++var15) {
         readLuaString(var1, var4, var2);
      }

   }

   public String toString() {
      return this.name;
   }

   private static String readLuaString(DataInputStream var0, int var1, boolean var2) throws IOException {
      long var3 = 0L;
      int var5;
      if (var1 == 4) {
         var5 = var0.readInt();
         var3 = (long)toInt(var5, var2);
      } else if (var1 == 8) {
         var3 = toLong(var0.readLong(), var2);
      } else {
         loadAssert(false, "Bad string size");
      }

      if (var3 == 0L) {
         return null;
      } else {
         --var3;
         loadAssert(var3 < 65536L, "Too long string:" + var3);
         var5 = (int)var3;
         byte[] var6 = new byte[3 + var5];
         var6[0] = (byte)(var5 >> 8 & 255);
         var6[1] = (byte)(var5 & 255);
         var0.readFully(var6, 2, var5 + 1);
         loadAssert(var6[2 + var5] == 0, "String loading");
         DataInputStream var7 = new DataInputStream(new ByteArrayInputStream(var6));
         String var8 = var7.readUTF();
         var7.close();
         return var8;
      }
   }

   public static int rev(int var0) {
      int var1 = var0 >>> 24 & 255;
      int var2 = var0 >>> 16 & 255;
      int var3 = var0 >>> 8 & 255;
      int var4 = var0 & 255;
      return var4 << 24 | var3 << 16 | var2 << 8 | var1;
   }

   public static long rev(long var0) {
      long var2 = var0 >>> 56 & 255L;
      long var4 = var0 >>> 48 & 255L;
      long var6 = var0 >>> 40 & 255L;
      long var8 = var0 >>> 32 & 255L;
      long var10 = var0 >>> 24 & 255L;
      long var12 = var0 >>> 16 & 255L;
      long var14 = var0 >>> 8 & 255L;
      long var16 = var0 & 255L;
      return var16 << 56 | var14 << 48 | var12 << 40 | var10 << 32 | var8 << 24 | var6 << 16 | var4 << 8 | var2;
   }

   public static int toInt(int var0, boolean var1) {
      return var1 ? rev(var0) : var0;
   }

   public static long toLong(long var0, boolean var2) {
      return var2 ? rev(var0) : var0;
   }

   public static LuaClosure loadByteCode(DataInputStream var0, KahluaTable var1) throws IOException {
      int var2 = var0.read();
      loadAssert(var2 == 27, "Signature 1");
      var2 = var0.read();
      loadAssert(var2 == 76, "Signature 2");
      var2 = var0.read();
      loadAssert(var2 == 117, "Signature 3");
      var2 = var0.read();
      loadAssert(var2 == 97, "Signature 4");
      var2 = var0.read();
      loadAssert(var2 == 81, "Version");
      var2 = var0.read();
      loadAssert(var2 == 0, "Format");
      boolean var3 = var0.read() == 1;
      var2 = var0.read();
      loadAssert(var2 == 4, "Size int");
      int var4 = var0.read();
      loadAssert(var4 == 4 || var4 == 8, "Size t");
      var2 = var0.read();
      loadAssert(var2 == 4, "Size instr");
      var2 = var0.read();
      loadAssert(var2 == 8, "Size number");
      var2 = var0.read();
      loadAssert(var2 == 0, "Integral");
      Prototype var5 = new Prototype(var0, var3, (String)null, var4);
      LuaClosure var6 = new LuaClosure(var5, var1);
      return var6;
   }

   private static void loadAssert(boolean var0, String var1) throws IOException {
      if (!var0) {
         throw new IOException("Could not load bytecode:" + var1);
      }
   }

   public static LuaClosure loadByteCode(InputStream var0, KahluaTable var1) throws IOException {
      if (!(var0 instanceof DataInputStream)) {
         var0 = new DataInputStream((InputStream)var0);
      }

      return loadByteCode((DataInputStream)var0, var1);
   }

   public void dump(OutputStream var1) throws IOException {
      DataOutputStream var2;
      if (var1 instanceof DataOutputStream) {
         var2 = (DataOutputStream)var1;
      } else {
         var2 = new DataOutputStream(var1);
      }

      var2.write(27);
      var2.write(76);
      var2.write(117);
      var2.write(97);
      var2.write(81);
      var2.write(0);
      var2.write(0);
      var2.write(4);
      var2.write(4);
      var2.write(4);
      var2.write(8);
      var2.write(0);
      this.dumpPrototype(var2);
   }

   private void dumpPrototype(DataOutputStream var1) throws IOException {
      dumpString(this.name, var1);
      var1.writeInt(0);
      var1.writeInt(0);
      var1.write(this.numUpvalues);
      var1.write(this.numParams);
      var1.write(this.isVararg ? 2 : 0);
      var1.write(this.maxStacksize);
      int var2 = this.code.length;
      var1.writeInt(var2);

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         var1.writeInt(this.code[var3]);
      }

      var3 = this.constants.length;
      var1.writeInt(var3);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Object var5 = this.constants[var4];
         if (var5 == null) {
            var1.write(0);
         } else if (var5 instanceof Boolean) {
            var1.write(1);
            var1.write((Boolean)var5 ? 1 : 0);
         } else if (var5 instanceof Double) {
            var1.write(3);
            Double var6 = (Double)var5;
            var1.writeLong(Double.doubleToLongBits(var6));
         } else {
            if (!(var5 instanceof String)) {
               throw new RuntimeException("Bad type in constant pool");
            }

            var1.write(4);
            dumpString((String)var5, var1);
         }
      }

      var4 = this.prototypes.length;
      var1.writeInt(var4);

      int var7;
      for(var7 = 0; var7 < var4; ++var7) {
         this.prototypes[var7].dumpPrototype(var1);
      }

      var7 = this.lines.length;
      var1.writeInt(var7);

      for(int var8 = 0; var8 < var7; ++var8) {
         var1.writeInt(this.lines[var8]);
      }

      var1.writeInt(0);
      var1.writeInt(0);
   }

   private static void dumpString(String var0, DataOutputStream var1) throws IOException {
      if (var0 == null) {
         var1.writeShort(0);
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         (new DataOutputStream(var2)).writeUTF(var0);
         byte[] var3 = var2.toByteArray();
         int var4 = var3.length - 2;
         var1.writeInt(var4 + 1);
         var1.write(var3, 2, var4);
         var1.write(0);
      }
   }
}
