package zombie.core.skinnedmodel.model;

public final class SoftwareModelMeshInstance {
   public SoftwareModelMesh softwareMesh;
   public VertexBufferObject vb;
   public String name;

   public SoftwareModelMeshInstance(String var1, SoftwareModelMesh var2) {
      this.name = var1;
      this.softwareMesh = var2;
      this.vb = new VertexBufferObject();
      this.vb.elements = var2.indicesUnskinned;
   }
}
