package org.joml;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Vector4ic {
   int x();

   int y();

   int z();

   int w();

   IntBuffer get(IntBuffer var1);

   IntBuffer get(int var1, IntBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   Vector4ic getToAddress(long var1);

   Vector4i sub(Vector4ic var1, Vector4i var2);

   Vector4i sub(int var1, int var2, int var3, int var4, Vector4i var5);

   Vector4i add(Vector4ic var1, Vector4i var2);

   Vector4i add(int var1, int var2, int var3, int var4, Vector4i var5);

   Vector4i mul(Vector4ic var1, Vector4i var2);

   Vector4i div(Vector4ic var1, Vector4i var2);

   Vector4i mul(int var1, Vector4i var2);

   Vector4i div(float var1, Vector4i var2);

   Vector4i div(int var1, Vector4i var2);

   long lengthSquared();

   double length();

   double distance(Vector4ic var1);

   double distance(int var1, int var2, int var3, int var4);

   long gridDistance(Vector4ic var1);

   long gridDistance(int var1, int var2, int var3, int var4);

   int distanceSquared(Vector4ic var1);

   int distanceSquared(int var1, int var2, int var3, int var4);

   int dot(Vector4ic var1);

   Vector4i negate(Vector4i var1);

   Vector4i min(Vector4ic var1, Vector4i var2);

   Vector4i max(Vector4ic var1, Vector4i var2);

   int get(int var1) throws IllegalArgumentException;

   int maxComponent();

   int minComponent();

   Vector4i absolute(Vector4i var1);

   boolean equals(int var1, int var2, int var3, int var4);
}
