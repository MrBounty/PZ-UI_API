package com.sixlegs.png;

public interface SuggestedPalette {
   String getName();

   int getSampleCount();

   int getSampleDepth();

   void getSample(int var1, short[] var2);

   int getFrequency(int var1);
}
