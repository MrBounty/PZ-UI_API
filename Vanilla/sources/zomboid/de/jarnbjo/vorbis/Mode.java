package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class Mode {
   private boolean blockFlag;
   private int windowType;
   private int transformType;
   private int mapping;

   protected Mode(BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      this.blockFlag = var1.getBit();
      this.windowType = var1.getInt(16);
      this.transformType = var1.getInt(16);
      this.mapping = var1.getInt(8);
      if (this.windowType != 0) {
         throw new VorbisFormatException("Window type = " + this.windowType + ", != 0");
      } else if (this.transformType != 0) {
         throw new VorbisFormatException("Transform type = " + this.transformType + ", != 0");
      } else if (this.mapping > var2.getMappings().length) {
         throw new VorbisFormatException("Mode mapping number is higher than total number of mappings.");
      }
   }

   protected boolean getBlockFlag() {
      return this.blockFlag;
   }

   protected int getWindowType() {
      return this.windowType;
   }

   protected int getTransformType() {
      return this.transformType;
   }

   protected int getMapping() {
      return this.mapping;
   }
}
