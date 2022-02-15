package org.joml;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface Vector2ic {
   int x();

   int y();

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   IntBuffer get(IntBuffer var1);

   IntBuffer get(int var1, IntBuffer var2);

   Vector2ic getToAddress(long var1);

   Vector2i sub(Vector2ic var1, Vector2i var2);

   Vector2i sub(int var1, int var2, Vector2i var3);

   long lengthSquared();

   double length();

   double distance(Vector2ic var1);

   double distance(int var1, int var2);

   long distanceSquared(Vector2ic var1);

   long distanceSquared(int var1, int var2);

   long gridDistance(Vector2ic var1);

   long gridDistance(int var1, int var2);

   Vector2i add(Vector2ic var1, Vector2i var2);

   Vector2i add(int var1, int var2, Vector2i var3);

   Vector2i mul(int var1, Vector2i var2);

   Vector2i mul(Vector2ic var1, Vector2i var2);

   Vector2i mul(int var1, int var2, Vector2i var3);

   Vector2i div(float var1, Vector2i var2);

   Vector2i div(int var1, Vector2i var2);

   Vector2i negate(Vector2i var1);

   Vector2i min(Vector2ic var1, Vector2i var2);

   Vector2i max(Vector2ic var1, Vector2i var2);

   int maxComponent();

   int minComponent();

   Vector2i absolute(Vector2i var1);

   int get(int var1) throws IllegalArgumentException;

   boolean equals(int var1, int var2);
}
