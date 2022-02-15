package se.krka.kahlua.vm;

public interface KahluaTableIterator extends JavaFunction {
   boolean advance();

   Object getKey();

   Object getValue();
}
