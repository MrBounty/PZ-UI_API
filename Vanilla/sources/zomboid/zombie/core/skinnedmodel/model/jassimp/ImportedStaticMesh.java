package zombie.core.skinnedmodel.model.jassimp;

import jassimp.AiMesh;
import zombie.core.skinnedmodel.model.VertexBufferObject;

public final class ImportedStaticMesh {
   VertexBufferObject.VertexArray verticesUnskinned = null;
   int[] elements = null;

   public ImportedStaticMesh(AiMesh var1) {
      this.processAiScene(var1);
   }

   private void processAiScene(AiMesh var1) {
      int var2 = var1.getNumVertices();
      int var3 = 0;

      for(int var4 = 0; var4 < 8; ++var4) {
         if (var1.hasTexCoords(var4)) {
            ++var3;
         }
      }

      VertexBufferObject.VertexFormat var8 = new VertexBufferObject.VertexFormat(3 + var3);
      var8.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      var8.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      var8.setElement(2, VertexBufferObject.VertexType.TangentArray, 12);

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         var8.setElement(3 + var5, VertexBufferObject.VertexType.TextureCoordArray, 8);
      }

      var8.calculate();
      this.verticesUnskinned = new VertexBufferObject.VertexArray(var8, var2);

      int var6;
      for(var5 = 0; var5 < var2; ++var5) {
         this.verticesUnskinned.setElement(var5, 0, var1.getPositionX(var5), var1.getPositionY(var5), var1.getPositionZ(var5));
         if (var1.hasNormals()) {
            this.verticesUnskinned.setElement(var5, 1, var1.getNormalX(var5), var1.getNormalY(var5), var1.getNormalZ(var5));
         } else {
            this.verticesUnskinned.setElement(var5, 1, 0.0F, 1.0F, 0.0F);
         }

         if (var1.hasTangentsAndBitangents()) {
            this.verticesUnskinned.setElement(var5, 2, var1.getTangentX(var5), var1.getTangentY(var5), var1.getTangentZ(var5));
         } else {
            this.verticesUnskinned.setElement(var5, 2, 0.0F, 0.0F, 1.0F);
         }

         if (var3 > 0) {
            var6 = 0;

            for(int var7 = 0; var7 < 8; ++var7) {
               if (var1.hasTexCoords(var7)) {
                  this.verticesUnskinned.setElement(var5, 3 + var6, var1.getTexCoordU(var5, var7), 1.0F - var1.getTexCoordV(var5, var7));
                  ++var6;
               }
            }
         }
      }

      var5 = var1.getNumFaces();
      this.elements = new int[var5 * 3];

      for(var6 = 0; var6 < var5; ++var6) {
         this.elements[var6 * 3 + 2] = var1.getFaceVertex(var6, 0);
         this.elements[var6 * 3 + 1] = var1.getFaceVertex(var6, 1);
         this.elements[var6 * 3 + 0] = var1.getFaceVertex(var6, 2);
      }

   }
}
