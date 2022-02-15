package org.luaj.kahluafork.compiler;

public class BlockCnt {
   BlockCnt previous;
   int breaklist;
   int nactvar;
   boolean upval;
   boolean isbreakable;
}
