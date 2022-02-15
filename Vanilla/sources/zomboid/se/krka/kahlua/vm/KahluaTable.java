package se.krka.kahlua.vm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface KahluaTable {
   void setMetatable(KahluaTable var1);

   KahluaTable getMetatable();

   void rawset(Object var1, Object var2);

   Object rawget(Object var1);

   void rawset(int var1, Object var2);

   Object rawget(int var1);

   int len();

   KahluaTableIterator iterator();

   boolean isEmpty();

   void wipe();

   int size();

   void save(ByteBuffer var1) throws IOException;

   void load(ByteBuffer var1, int var2) throws IOException;

   void save(DataOutputStream var1) throws IOException;

   void load(DataInputStream var1, int var2) throws IOException;

   String getString(String var1);
}
