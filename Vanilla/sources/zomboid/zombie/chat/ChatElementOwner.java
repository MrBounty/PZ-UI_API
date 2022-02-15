package zombie.chat;

import zombie.iso.IsoGridSquare;

public interface ChatElementOwner {
   float getX();

   float getY();

   float getZ();

   IsoGridSquare getSquare();
}
