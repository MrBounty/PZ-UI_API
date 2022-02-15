package zombie.iso.areas.isoregion.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class DataCell {
   public final DataRoot dataRoot;
   protected final Map dataChunks = new HashMap();

   protected DataCell(DataRoot var1, int var2, int var3, int var4) {
      this.dataRoot = var1;
   }

   protected DataRoot getDataRoot() {
      return this.dataRoot;
   }

   protected DataChunk getChunk(int var1) {
      return (DataChunk)this.dataChunks.get(var1);
   }

   protected DataChunk addChunk(int var1, int var2, int var3) {
      DataChunk var4 = new DataChunk(var1, var2, this, var3);
      this.dataChunks.put(var3, var4);
      return var4;
   }

   protected void setChunk(DataChunk var1) {
      this.dataChunks.put(var1.getHashId(), var1);
   }

   protected void getAllChunks(List var1) {
      Iterator var2 = this.dataChunks.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.add((DataChunk)var3.getValue());
      }

   }
}
