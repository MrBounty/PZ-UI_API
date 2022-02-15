package zombie.iso;

import java.util.ArrayList;

public class IsoGridStack {
   public ArrayList Squares;

   public IsoGridStack(int var1) {
      this.Squares = new ArrayList(var1);

      for(int var2 = 0; var2 < var1; ++var2) {
         this.Squares.add(new ArrayList(5000));
      }

   }
}
