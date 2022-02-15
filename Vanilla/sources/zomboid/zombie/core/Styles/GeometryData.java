package zombie.core.Styles;

public class GeometryData {
   private final FloatList vertexData;
   private final ShortList indexData;

   public GeometryData(FloatList var1, ShortList var2) {
      this.vertexData = var1;
      this.indexData = var2;
   }

   public void clear() {
      this.vertexData.clear();
      this.indexData.clear();
   }

   public FloatList getVertexData() {
      return this.vertexData;
   }

   public ShortList getIndexData() {
      return this.indexData;
   }
}
