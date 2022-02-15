package zombie.core.skinnedmodel.model;

import org.joml.Matrix4f;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;
import zombie.core.skinnedmodel.model.jassimp.JAssImpImporter;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiScene;
import zombie.core.skinnedmodel.shader.Shader;

public final class ModelMesh extends Asset {
   public VertexBufferObject vb;
   public SkinningData skinningData;
   public SoftwareModelMesh softwareMesh;
   public ModelMesh.MeshAssetParams assetParams;
   public Matrix4f m_transform;
   protected boolean bStatic;
   public ModelMesh m_animationsMesh;
   public String m_fullPath;
   public static final AssetType ASSET_TYPE = new AssetType("Mesh");

   public ModelMesh(AssetPath var1, AssetManager var2, ModelMesh.MeshAssetParams var3) {
      super(var1, var2);
      this.assetParams = var3;
      this.bStatic = this.assetParams != null && this.assetParams.bStatic;
      this.m_animationsMesh = this.assetParams == null ? null : this.assetParams.animationsMesh;
   }

   protected void onLoadedX(ProcessedAiScene var1) {
      JAssImpImporter.LoadMode var2 = this.assetParams.bStatic ? JAssImpImporter.LoadMode.StaticMesh : JAssImpImporter.LoadMode.Normal;
      SkinningData var3 = this.assetParams.animationsMesh == null ? null : this.assetParams.animationsMesh.skinningData;
      var1.applyToMesh(this, var2, false, var3);
   }

   protected void onLoadedTxt(ModelTxt var1) {
      SkinningData var2 = this.assetParams.animationsMesh == null ? null : this.assetParams.animationsMesh.skinningData;
      ModelLoader.instance.applyToMesh(var1, this, var2);
   }

   public void SetVertexBuffer(VertexBufferObject var1) {
      this.clear();
      this.vb = var1;
      this.bStatic = var1 == null || var1.bStatic;
   }

   public void Draw(Shader var1) {
      if (this.vb != null) {
         this.vb.Draw(var1);
      }

   }

   public void onBeforeReady() {
      super.onBeforeReady();
      if (this.assetParams != null) {
         this.assetParams.animationsMesh = null;
         this.assetParams = null;
      }

   }

   public void setAssetParams(AssetManager.AssetParams var1) {
      this.assetParams = (ModelMesh.MeshAssetParams)var1;
   }

   public AssetType getType() {
      return ASSET_TYPE;
   }

   public void clear() {
      if (this.vb != null) {
         this.vb.clear();
         this.vb = null;
      }
   }

   public static final class MeshAssetParams extends AssetManager.AssetParams {
      public boolean bStatic;
      public ModelMesh animationsMesh;
   }
}
