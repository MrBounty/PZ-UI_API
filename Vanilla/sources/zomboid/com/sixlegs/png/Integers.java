package com.sixlegs.png;

class Integers {
   private static final Integer INT_0 = new Integer(0);
   private static final Integer INT_1 = new Integer(1);
   private static final Integer INT_2 = new Integer(2);
   private static final Integer INT_3 = new Integer(3);
   private static final Integer INT_4 = new Integer(4);
   private static final Integer INT_5 = new Integer(5);
   private static final Integer INT_6 = new Integer(6);
   private static final Integer INT_7 = new Integer(7);
   private static final Integer INT_8 = new Integer(8);

   public static Integer valueOf(int var0) {
      switch(var0) {
      case 0:
         return INT_0;
      case 1:
         return INT_1;
      case 2:
         return INT_2;
      case 3:
         return INT_3;
      case 4:
         return INT_4;
      case 5:
         return INT_5;
      case 6:
         return INT_6;
      case 7:
         return INT_7;
      case 8:
         return INT_8;
      default:
         return new Integer(var0);
      }
   }
}
