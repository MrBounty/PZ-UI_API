package zombie.core.skinnedmodel.model;

import zombie.core.Color;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.core.skinnedmodel.Vector3;
import zombie.iso.Vector2;

public class VertexDefinitions {
   class VertexPositionNormalTangentTexture {
      public Vector3 Position;
      public Vector3 Normal;
      public Vector3 Tangent;
      public Vector2 TextureCoordinates;

      public VertexPositionNormalTangentTexture(Vector3 var2, Vector3 var3, Vector3 var4, Vector2 var5) {
         this.Position = var2;
         this.Normal = var3;
         this.Tangent = var4;
         this.TextureCoordinates = var5;
      }

      public VertexPositionNormalTangentTexture(float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
         this.Position = new Vector3(var2, var3, var4);
         this.Normal = new Vector3(var5, var6, var7);
         this.Tangent = new Vector3(var8, var9, var10);
         this.TextureCoordinates = new Vector2(var11, var12);
      }
   }

   class VertexPositionNormalTexture {
      public Vector3 Position;
      public Vector3 Normal;
      public Vector2 TextureCoordinates;

      public VertexPositionNormalTexture(Vector3 var2, Vector3 var3, Vector2 var4) {
         this.Position = var2;
         this.Normal = var3;
         this.TextureCoordinates = var4;
      }

      public VertexPositionNormalTexture(float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
         this.Position = new Vector3(var2, var3, var4);
         this.Normal = new Vector3(var5, var6, var7);
         this.TextureCoordinates = new Vector2(var8, var9);
      }
   }

   class VertexPositionNormal {
      public Vector3 Position;
      public Vector3 Normal;

      public VertexPositionNormal(Vector3 var2, Vector3 var3, Vector2 var4) {
         this.Position = var2;
         this.Normal = var3;
      }

      public VertexPositionNormal(float var2, float var3, float var4, float var5, float var6, float var7) {
         this.Position = new Vector3(var2, var3, var4);
         this.Normal = new Vector3(var5, var6, var7);
      }
   }

   class VertexPositionColour {
      public Vector3 Position;
      public int Colour;

      public VertexPositionColour(Vector3 var2, Color var3) {
         this.Position = var2;
         this.Colour = HelperFunctions.ToRgba(var3);
      }

      public VertexPositionColour(float var2, float var3, float var4, Color var5) {
         this.Position = new Vector3(var2, var3, var4);
         this.Colour = HelperFunctions.ToRgba(var5);
      }
   }
}
