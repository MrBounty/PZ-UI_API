package zombie.util.io;

public interface BitHeaderRead {
   int getStartPosition();

   void read();

   boolean hasFlags(int var1);

   boolean hasFlags(long var1);

   boolean equals(int var1);

   boolean equals(long var1);

   int getLen();

   void release();
}
