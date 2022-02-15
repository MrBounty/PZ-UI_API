package zombie.iso;

import org.joml.Vector2i;

public class DiamondMatrixIterator {
   private int size;
   private int lineSize;
   private int line;
   private int column;

   public DiamondMatrixIterator(int var1) {
      this.size = var1;
      this.lineSize = 1;
      this.line = 0;
      this.column = 0;
   }

   public DiamondMatrixIterator reset(int var1) {
      this.size = var1;
      this.lineSize = 1;
      this.line = 0;
      this.column = 0;
      return this;
   }

   public void reset() {
      this.lineSize = 1;
      this.line = 0;
      this.column = 0;
   }

   public boolean next(Vector2i var1) {
      if (this.lineSize == 0) {
         var1.x = 0;
         var1.y = 0;
         return false;
      } else if (this.line == 0 && this.column == 0) {
         var1.set(0, 0);
         ++this.column;
         return true;
      } else {
         if (this.column < this.lineSize) {
            ++var1.x;
            --var1.y;
            ++this.column;
         } else {
            this.column = 1;
            ++this.line;
            if (this.line < this.size) {
               ++this.lineSize;
               var1.x = 0;
               var1.y = this.line;
            } else {
               --this.lineSize;
               var1.x = this.line - this.size + 1;
               var1.y = this.size - 1;
            }
         }

         if (this.lineSize == 0) {
            var1.x = 0;
            var1.y = 0;
            return false;
         } else {
            return true;
         }
      }
   }

   public Vector2i i2line(int var1) {
      int var2 = 0;

      int var3;
      for(var3 = 1; var3 < this.size + 1; ++var3) {
         var2 += var3;
         if (var1 + 1 <= var2) {
            return new Vector2i(var1 - var2 + var3, var3 - 1);
         }
      }

      for(var3 = this.size + 1; var3 < this.size * 2; ++var3) {
         var2 += this.size * 2 - var3;
         if (var1 + 1 <= var2) {
            return new Vector2i(var1 - var2 + this.size * 2 - var3, var3 - 1);
         }
      }

      return null;
   }

   public Vector2i line2coord(Vector2i var1) {
      if (var1 == null) {
         return null;
      } else {
         Vector2i var2;
         int var3;
         if (var1.y < this.size) {
            var2 = new Vector2i(0, var1.y);

            for(var3 = 0; var3 < var1.x; ++var3) {
               ++var2.x;
               --var2.y;
            }

            return var2;
         } else {
            var2 = new Vector2i(var1.y - this.size + 1, this.size - 1);

            for(var3 = 0; var3 < var1.x; ++var3) {
               ++var2.x;
               --var2.y;
            }

            return var2;
         }
      }
   }
}
