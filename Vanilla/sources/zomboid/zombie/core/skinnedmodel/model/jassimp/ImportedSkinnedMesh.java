package zombie.core.skinnedmodel.model.jassimp;

import jassimp.AiBone;
import jassimp.AiBoneWeight;
import jassimp.AiMesh;
import java.util.List;
import zombie.core.skinnedmodel.model.VertexBufferObject;

public final class ImportedSkinnedMesh {
   final ImportedSkeleton skeleton;
   String name;
   VertexBufferObject.VertexArray vertices = null;
   int[] elements = null;

   public ImportedSkinnedMesh(ImportedSkeleton var1, AiMesh var2) {
      this.skeleton = var1;
      this.processAiScene(var2);
   }

   private void processAiScene(AiMesh var1) {
      this.name = var1.getName();
      int var2 = var1.getNumVertices();
      int var3 = var2 * 4;
      int[] var4 = new int[var3];
      float[] var5 = new float[var3];

      for(int var6 = 0; var6 < var3; ++var6) {
         var5[var6] = 0.0F;
      }

      List var17 = var1.getBones();
      int var7 = var17.size();

      int var8;
      int var11;
      for(var8 = 0; var8 < var7; ++var8) {
         AiBone var9 = (AiBone)var17.get(var8);
         String var10 = var9.getName();
         var11 = (Integer)this.skeleton.boneIndices.get(var10);
         List var12 = var9.getBoneWeights();

         for(int var13 = 0; var13 < var9.getNumWeights(); ++var13) {
            AiBoneWeight var14 = (AiBoneWeight)var12.get(var13);
            int var15 = var14.getVertexId() * 4;

            for(int var16 = 0; var16 < 4; ++var16) {
               if (var5[var15 + var16] == 0.0F) {
                  var5[var15 + var16] = var14.getWeight();
                  var4[var15 + var16] = var11;
                  break;
               }
            }
         }
      }

      var8 = getNumUVs(var1);
      VertexBufferObject.VertexFormat var18 = new VertexBufferObject.VertexFormat(5 + var8);
      var18.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      var18.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      var18.setElement(2, VertexBufferObject.VertexType.TangentArray, 12);
      var18.setElement(3, VertexBufferObject.VertexType.BlendWeightArray, 16);
      var18.setElement(4, VertexBufferObject.VertexType.BlendIndexArray, 16);

      int var19;
      for(var19 = 0; var19 < var8; ++var19) {
         var18.setElement(5 + var19, VertexBufferObject.VertexType.TextureCoordArray, 8);
      }

      var18.calculate();
      this.vertices = new VertexBufferObject.VertexArray(var18, var2);

      for(var19 = 0; var19 < var2; ++var19) {
         this.vertices.setElement(var19, 0, var1.getPositionX(var19), var1.getPositionY(var19), var1.getPositionZ(var19));
         if (var1.hasNormals()) {
            this.vertices.setElement(var19, 1, var1.getNormalX(var19), var1.getNormalY(var19), var1.getNormalZ(var19));
         } else {
            this.vertices.setElement(var19, 1, 0.0F, 1.0F, 0.0F);
         }

         if (var1.hasTangentsAndBitangents()) {
            this.vertices.setElement(var19, 2, var1.getTangentX(var19), var1.getTangentY(var19), var1.getTangentZ(var19));
         } else {
            this.vertices.setElement(var19, 2, 0.0F, 0.0F, 1.0F);
         }

         this.vertices.setElement(var19, 3, var5[var19 * 4], var5[var19 * 4 + 1], var5[var19 * 4 + 2], var5[var19 * 4 + 3]);
         this.vertices.setElement(var19, 4, (float)var4[var19 * 4], (float)var4[var19 * 4 + 1], (float)var4[var19 * 4 + 2], (float)var4[var19 * 4 + 3]);
         if (var8 > 0) {
            var11 = 0;

            for(int var20 = 0; var20 < 8; ++var20) {
               if (var1.hasTexCoords(var20)) {
                  this.vertices.setElement(var19, 5 + var11, var1.getTexCoordU(var19, var20), 1.0F - var1.getTexCoordV(var19, var20));
                  ++var11;
               }
            }
         }
      }

      var19 = var1.getNumFaces();
      this.elements = new int[var19 * 3];

      for(var11 = 0; var11 < var19; ++var11) {
         this.elements[var11 * 3 + 2] = var1.getFaceVertex(var11, 0);
         this.elements[var11 * 3 + 1] = var1.getFaceVertex(var11, 1);
         this.elements[var11 * 3 + 0] = var1.getFaceVertex(var11, 2);
      }

   }

   private static int getNumUVs(AiMesh var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < 8; ++var2) {
         if (var0.hasTexCoords(var2)) {
            ++var1;
         }
      }

      return var1;
   }
}
