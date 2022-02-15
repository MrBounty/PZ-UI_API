package zombie.util.io;

public interface BitHeaderWrite {
   int getStartPosition();

   void create();

   void write();

   void addFlags(int var1);

   void addFlags(long var1);

   boolean hasFlags(int var1);

   boolean hasFlags(long var1);

   boolean equals(int var1);

   boolean equals(long var1);

   int getLen();

   void release();
}
