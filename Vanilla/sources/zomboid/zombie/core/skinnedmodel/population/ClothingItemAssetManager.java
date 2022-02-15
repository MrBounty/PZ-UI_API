package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import java.util.List;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.asset.FileTask_ParseXML;
import zombie.fileSystem.FileSystem;
import zombie.util.list.PZArrayUtil;

public class ClothingItemAssetManager extends AssetManager {
   public static final ClothingItemAssetManager instance = new ClothingItemAssetManager();

   protected void startLoading(Asset var1) {
      FileSystem var2 = var1.getAssetManager().getOwner().getFileSystem();
      FileTask_ParseXML var3 = new FileTask_ParseXML(ClothingItemXML.class, var1.getPath().getPath(), (var2x) -> {
         this.onFileTaskFinished((ClothingItem)var1, var2x);
      }, var2);
      AssetTask_RunFileTask var4 = new AssetTask_RunFileTask(var3, var1);
      this.setTask(var1, var4);
      var4.execute();
   }

   private void onFileTaskFinished(ClothingItem var1, Object var2) {
      if (var2 instanceof ClothingItemXML) {
         ClothingItemXML var4 = (ClothingItemXML)var2;
         var1.m_MaleModel = this.fixPath(var4.m_MaleModel);
         var1.m_FemaleModel = this.fixPath(var4.m_FemaleModel);
         var1.m_Static = var4.m_Static;
         PZArrayUtil.arrayCopy((List)var1.m_BaseTextures, (List)this.fixPaths(var4.m_BaseTextures));
         var1.m_AttachBone = var4.m_AttachBone;
         PZArrayUtil.arrayCopy((List)var1.m_Masks, (List)var4.m_Masks);
         var1.m_MasksFolder = this.fixPath(var4.m_MasksFolder);
         var1.m_UnderlayMasksFolder = this.fixPath(var4.m_UnderlayMasksFolder);
         PZArrayUtil.arrayCopy((List)var1.textureChoices, (List)this.fixPaths(var4.textureChoices));
         var1.m_AllowRandomHue = var4.m_AllowRandomHue;
         var1.m_AllowRandomTint = var4.m_AllowRandomTint;
         var1.m_DecalGroup = var4.m_DecalGroup;
         var1.m_Shader = var4.m_Shader;
         var1.m_HatCategory = var4.m_HatCategory;
         this.onLoadingSucceeded(var1);
      } else {
         this.onLoadingFailed(var1);
      }

   }

   private String fixPath(String var1) {
      return var1 == null ? null : var1.replaceAll("\\\\", "/");
   }

   private ArrayList fixPaths(ArrayList var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            var1.set(var2, this.fixPath((String)var1.get(var2)));
         }

         return var1;
      }
   }

   public void onStateChanged(Asset.State var1, Asset.State var2, Asset var3) {
      super.onStateChanged(var1, var2, var3);
      if (var2 == Asset.State.READY) {
         OutfitManager.instance.onClothingItemStateChanged((ClothingItem)var3);
      }

   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new ClothingItem(var1, this);
   }

   protected void destroyAsset(Asset var1) {
   }
}
