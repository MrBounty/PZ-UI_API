package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

class Floor1 extends Floor implements Cloneable {
   private int[] partitionClassList;
   private int maximumClass;
   private int multiplier;
   private int rangeBits;
   private int[] classDimensions;
   private int[] classSubclasses;
   private int[] classMasterbooks;
   private int[][] subclassBooks;
   private int[] xList;
   private int[] yList;
   private int[] lowNeighbours;
   private int[] highNeighbours;
   private static final int[] RANGES = new int[]{256, 128, 86, 64};

   private Floor1() {
   }

   protected Floor1(BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      this.maximumClass = -1;
      int var3 = var1.getInt(5);
      this.partitionClassList = new int[var3];

      int var4;
      for(var4 = 0; var4 < this.partitionClassList.length; ++var4) {
         this.partitionClassList[var4] = var1.getInt(4);
         if (this.partitionClassList[var4] > this.maximumClass) {
            this.maximumClass = this.partitionClassList[var4];
         }
      }

      this.classDimensions = new int[this.maximumClass + 1];
      this.classSubclasses = new int[this.maximumClass + 1];
      this.classMasterbooks = new int[this.maximumClass + 1];
      this.subclassBooks = new int[this.maximumClass + 1][];
      var4 = 2;

      for(int var5 = 0; var5 <= this.maximumClass; ++var5) {
         this.classDimensions[var5] = var1.getInt(3) + 1;
         var4 += this.classDimensions[var5];
         this.classSubclasses[var5] = var1.getInt(2);
         if (this.classDimensions[var5] > var2.getCodeBooks().length || this.classSubclasses[var5] > var2.getCodeBooks().length) {
            throw new VorbisFormatException("There is a class dimension or class subclasses entry higher than the number of codebooks in the setup header.");
         }

         if (this.classSubclasses[var5] != 0) {
            this.classMasterbooks[var5] = var1.getInt(8);
         }

         this.subclassBooks[var5] = new int[1 << this.classSubclasses[var5]];

         for(int var6 = 0; var6 < this.subclassBooks[var5].length; ++var6) {
            this.subclassBooks[var5][var6] = var1.getInt(8) - 1;
         }
      }

      this.multiplier = var1.getInt(2) + 1;
      this.rangeBits = var1.getInt(4);
      boolean var9 = false;
      ArrayList var10 = new ArrayList();
      var10.add(new Integer(0));
      var10.add(new Integer(1 << this.rangeBits));

      int var8;
      for(int var7 = 0; var7 < var3; ++var7) {
         for(var8 = 0; var8 < this.classDimensions[this.partitionClassList[var7]]; ++var8) {
            var10.add(new Integer(var1.getInt(this.rangeBits)));
         }
      }

      this.xList = new int[var10.size()];
      this.lowNeighbours = new int[this.xList.length];
      this.highNeighbours = new int[this.xList.length];
      Iterator var11 = var10.iterator();

      for(var8 = 0; var8 < this.xList.length; ++var8) {
         this.xList[var8] = (Integer)var11.next();
      }

      for(var8 = 0; var8 < this.xList.length; ++var8) {
         this.lowNeighbours[var8] = Util.lowNeighbour(this.xList, var8);
         this.highNeighbours[var8] = Util.highNeighbour(this.xList, var8);
      }

   }

   protected int getType() {
      return 1;
   }

   protected Floor decodeFloor(VorbisStream var1, BitInputStream var2) throws VorbisFormatException, IOException {
      if (!var2.getBit()) {
         return null;
      } else {
         Floor1 var3 = (Floor1)this.clone();
         var3.yList = new int[this.xList.length];
         int var4 = RANGES[this.multiplier - 1];
         var3.yList[0] = var2.getInt(Util.ilog(var4 - 1));
         var3.yList[1] = var2.getInt(Util.ilog(var4 - 1));
         int var5 = 2;

         for(int var6 = 0; var6 < this.partitionClassList.length; ++var6) {
            int var7 = this.partitionClassList[var6];
            int var8 = this.classDimensions[var7];
            int var9 = this.classSubclasses[var7];
            int var10 = (1 << var9) - 1;
            int var11 = 0;
            if (var9 > 0) {
               var11 = var2.getInt(var1.getSetupHeader().getCodeBooks()[this.classMasterbooks[var7]].getHuffmanRoot());
            }

            for(int var12 = 0; var12 < var8; ++var12) {
               int var13 = this.subclassBooks[var7][var11 & var10];
               var11 >>>= var9;
               if (var13 >= 0) {
                  var3.yList[var12 + var5] = var2.getInt(var1.getSetupHeader().getCodeBooks()[var13].getHuffmanRoot());
               } else {
                  var3.yList[var12 + var5] = 0;
               }
            }

            var5 += var8;
         }

         return var3;
      }
   }

   protected void computeFloor(float[] var1) {
      int var2 = var1.length;
      int var3 = this.xList.length;
      boolean[] var4 = new boolean[var3];
      int var5 = RANGES[this.multiplier - 1];

      int var7;
      int var8;
      int var9;
      int var10;
      int var13;
      for(int var6 = 2; var6 < var3; ++var6) {
         var7 = this.lowNeighbours[var6];
         var8 = this.highNeighbours[var6];
         var9 = Util.renderPoint(this.xList[var7], this.xList[var8], this.yList[var7], this.yList[var8], this.xList[var6]);
         var10 = this.yList[var6];
         int var11 = var5 - var9;
         var13 = var11 < var9 ? var11 * 2 : var9 * 2;
         if (var10 != 0) {
            var4[var7] = true;
            var4[var8] = true;
            var4[var6] = true;
            if (var10 >= var13) {
               this.yList[var6] = var11 > var9 ? var10 - var9 + var9 : -var10 + var11 + var9 - 1;
            } else {
               this.yList[var6] = (var10 & 1) == 1 ? var9 - (var10 + 1 >> 1) : var9 + (var10 >> 1);
            }
         } else {
            var4[var6] = false;
            this.yList[var6] = var9;
         }
      }

      int[] var14 = new int[var3];
      System.arraycopy(this.xList, 0, var14, 0, var3);
      sort(var14, this.yList, var4);
      var7 = 0;
      var8 = 0;
      var9 = 0;
      var10 = this.yList[0] * this.multiplier;
      float[] var15 = new float[var1.length];
      float[] var12 = new float[var1.length];
      Arrays.fill(var15, 1.0F);
      System.arraycopy(var1, 0, var12, 0, var1.length);

      for(var13 = 1; var13 < var3; ++var13) {
         if (var4[var13]) {
            var8 = this.yList[var13] * this.multiplier;
            var7 = var14[var13];
            Util.renderLine(var9, var10, var7, var8, var1);
            Util.renderLine(var9, var10, var7, var8, var15);
            var9 = var7;
            var10 = var8;
         }
      }

      for(float var16 = DB_STATIC_TABLE[var8]; var7 < var2 / 2; var1[var7++] = var16) {
      }

   }

   public Object clone() {
      Floor1 var1 = new Floor1();
      var1.classDimensions = this.classDimensions;
      var1.classMasterbooks = this.classMasterbooks;
      var1.classSubclasses = this.classSubclasses;
      var1.maximumClass = this.maximumClass;
      var1.multiplier = this.multiplier;
      var1.partitionClassList = this.partitionClassList;
      var1.rangeBits = this.rangeBits;
      var1.subclassBooks = this.subclassBooks;
      var1.xList = this.xList;
      var1.yList = this.yList;
      var1.lowNeighbours = this.lowNeighbours;
      var1.highNeighbours = this.highNeighbours;
      return var1;
   }

   private static final void sort(int[] var0, int[] var1, boolean[] var2) {
      byte var3 = 0;
      int var4 = var0.length;
      int var5 = var4 + var3;

      for(int var8 = var3; var8 < var5; ++var8) {
         for(int var9 = var8; var9 > var3 && var0[var9 - 1] > var0[var9]; --var9) {
            int var6 = var0[var9];
            var0[var9] = var0[var9 - 1];
            var0[var9 - 1] = var6;
            var6 = var1[var9];
            var1[var9] = var1[var9 - 1];
            var1[var9 - 1] = var6;
            boolean var7 = var2[var9];
            var2[var9] = var2[var9 - 1];
            var2[var9 - 1] = var7;
         }
      }

   }

   private static final void swap(int[] var0, int var1, int var2) {
      int var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }

   private static final void swap(boolean[] var0, int var1, int var2) {
      boolean var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }
}
