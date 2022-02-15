package zombie.util.util;

import java.util.NoSuchElementException;
import zombie.util.map.NoSuchMappingException;

public class Exceptions {
   public static void indexOutOfBounds(int var0, int var1, int var2) throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException("Index out of bounds: " + var0 + ", valid range is " + var1 + " to " + var2);
   }

   public static void nullArgument(String var0) throws NullPointerException {
      throw new NullPointerException("The specified " + var0 + " is null");
   }

   public static void negativeArgument(String var0, Object var1) throws IllegalArgumentException {
      throw new IllegalArgumentException(var0 + " cannot be negative: " + String.valueOf(var1));
   }

   public static void negativeOrZeroArgument(String var0, Object var1) throws IllegalArgumentException {
      throw new IllegalArgumentException(var0 + " must be a positive value: " + String.valueOf(var1));
   }

   public static void endOfIterator() throws NoSuchElementException {
      throw new NoSuchElementException("Attempt to iterate past iterator's last element.");
   }

   public static void startOfIterator() throws NoSuchElementException {
      throw new NoSuchElementException("Attempt to iterate past iterator's first element.");
   }

   public static void noElementToRemove() throws IllegalStateException {
      throw new IllegalStateException("Attempt to remove element from iterator that has no current element.");
   }

   public static void noElementToGet() throws IllegalStateException {
      throw new IllegalStateException("Attempt to get element from iterator that has no current element. Call next() first.");
   }

   public static void noElementToSet() throws IllegalStateException {
      throw new IllegalStateException("Attempt to set element in iterator that has no current element.");
   }

   public static void noLastElement() throws IllegalStateException {
      throw new IllegalStateException("No value to return. Call containsKey() first.");
   }

   public static void noSuchMapping(Object var0) throws NoSuchMappingException {
      throw new NoSuchMappingException("No such key in map: " + String.valueOf(var0));
   }

   public static void dequeNoFirst() throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException("Attempt to get first element of empty deque");
   }

   public static void dequeNoLast() throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException("Attempt to get last element of empty deque");
   }

   public static void dequeNoFirstToRemove() throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException("Attempt to remove last element of empty deque");
   }

   public static void dequeNoLastToRemove() throws IndexOutOfBoundsException {
      throw new IndexOutOfBoundsException("Attempt to remove last element of empty deque");
   }

   public static void nullElementNotAllowed() throws IllegalArgumentException {
      throw new IllegalArgumentException("Attempt to add a null value to an adapted primitive set.");
   }

   public static void cannotAdapt(String var0) throws IllegalStateException {
      throw new IllegalStateException("The " + var0 + " contains values preventing it from being adapted to a primitive " + var0);
   }

   public static void unsupported(String var0) throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Attempt to invoke unsupported operation: " + var0);
   }

   public static void unmodifiable(String var0) throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Attempt to modify unmodifiable " + var0);
   }

   public static void cloning() throws RuntimeException {
      throw new RuntimeException("Clone is not supported");
   }

   public static void invalidRangeBounds(Object var0, Object var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("First (" + var0 + ") cannot be greater than last (" + var1 + ")");
   }

   public static void cannotMergeRanges(Object var0, Object var1) throws IllegalArgumentException {
      String var10002 = var0.toString();
      throw new IllegalArgumentException("Ranges cannot be merged: " + var10002 + " and " + var1.toString());
   }

   public static void setNoFirst() throws NoSuchElementException {
      throw new NoSuchElementException("Attempt to get first element of empty set");
   }

   public static void setNoLast() throws NoSuchElementException {
      throw new NoSuchElementException("Attempt to get last element of empty set");
   }

   public static void invalidSetBounds(Object var0, Object var1) throws IllegalArgumentException {
      throw new IllegalArgumentException("Lower bound (" + var0 + ") cannot be greater than upper bound (" + var1 + ")");
   }

   public static void valueNotInSubRange(Object var0) throws IllegalArgumentException {
      throw new IllegalArgumentException("Attempt to add a value outside valid range: " + var0);
   }

   public static void invalidUpperBound(Object var0) throws IllegalArgumentException {
      throw new IllegalArgumentException("Upper bound is not in valid sub-range: " + var0);
   }

   public static void invalidLowerBound(Object var0) throws IllegalArgumentException {
      throw new IllegalArgumentException("Lower bound is not in valid sub-range: " + var0);
   }
}
