package org.joml;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Vector3ic {
   int x();

   int y();

   int z();

   IntBuffer get(IntBuffer var1);

   IntBuffer get(int var1, IntBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   Vector3ic getToAddress(long var1);

   Vector3i sub(Vector3ic var1, Vector3i var2);

   Vector3i sub(int var1, int var2, int var3, Vector3i var4);

   Vector3i add(Vector3ic var1, Vector3i var2);

   Vector3i add(int var1, int var2, int var3, Vector3i var4);

   Vector3i mul(int var1, Vector3i var2);

   Vector3i mul(Vector3ic var1, Vector3i var2);

   Vector3i mul(int var1, int var2, int var3, Vector3i var4);

   Vector3i div(float var1, Vector3i var2);

   Vector3i div(int var1, Vector3i var2);

   long lengthSquared();

   double length();

   double distance(Vector3ic var1);

   double distance(int var1, int var2, int var3);

   long gridDistance(Vector3ic var1);

   long gridDistance(int var1, int var2, int var3);

   long distanceSquared(Vector3ic var1);

   long distanceSquared(int var1, int var2, int var3);

   Vector3i negate(Vector3i var1);

   Vector3i min(Vector3ic var1, Vector3i var2);

   Vector3i max(Vector3ic var1, Vector3i var2);

   int get(int var1) throws IllegalArgumentException;

   int maxComponent();

   int minComponent();

   Vector3i absolute(Vector3i var1);

   boolean equals(int var1, int var2, int var3);
}
