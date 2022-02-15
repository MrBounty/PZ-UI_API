package se.krka.kahlua.vm;

public interface Platform {
   double pow(double var1, double var3);

   KahluaTable newTable();

   KahluaTable newEnvironment();

   void setupEnvironment(KahluaTable var1);
}
