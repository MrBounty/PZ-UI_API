package zombie.core.skinnedmodel.model;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import zombie.ZomboidFileSystem;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiScene;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

public abstract class FileTask_AbstractLoadModel extends FileTask {
   protected String m_fileName;
   private final String m_mediaFilePath;
   private final String m_mediaFileXPath;

   protected FileTask_AbstractLoadModel(FileSystem var1, IFileTaskCallback var2, String var3, String var4) {
      super(var1, var2);
      this.m_mediaFilePath = var3;
      this.m_mediaFileXPath = var4;
   }

   public Object call() throws Exception {
      this.checkSlowLoad();
      ModelFileExtensionType var1 = this.checkExtensionType();
      switch(var1) {
      case X:
         return this.loadX();
      case Fbx:
         return this.loadFBX();
      case Txt:
         return this.loadTxt();
      case None:
      default:
         return null;
      }
   }

   private void checkSlowLoad() {
      if (DebugOptions.instance.AssetSlowLoad.getValue()) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
         }
      }

   }

   private ModelFileExtensionType checkExtensionType() {
      String var1 = this.getRawFileName();
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      if (var2.endsWith(".txt")) {
         return ModelFileExtensionType.Txt;
      } else {
         boolean var3 = var1.startsWith("x:");
         if (var3) {
            DebugLog.Animation.warn("Note: The 'x:' prefix is not required. name=\"" + var1 + "\"");
            var2 = var1.substring(2);
         }

         if (var1.contains("media/") || var1.contains(".")) {
            this.m_fileName = var1;
            this.m_fileName = ZomboidFileSystem.instance.getString(this.m_fileName);
            if ((new File(this.m_fileName)).exists()) {
               if (this.m_fileName.endsWith(".fbx")) {
                  return ModelFileExtensionType.Fbx;
               }

               if (this.m_fileName.endsWith(".x")) {
                  return ModelFileExtensionType.X;
               }

               return ModelFileExtensionType.X;
            }
         }

         this.m_fileName = this.m_mediaFileXPath + "/" + var2 + ".fbx";
         this.m_fileName = ZomboidFileSystem.instance.getString(this.m_fileName);
         if ((new File(this.m_fileName)).exists()) {
            return ModelFileExtensionType.Fbx;
         } else {
            this.m_fileName = this.m_mediaFileXPath + "/" + var2 + ".x";
            this.m_fileName = ZomboidFileSystem.instance.getString(this.m_fileName);
            if ((new File(this.m_fileName)).exists()) {
               return ModelFileExtensionType.X;
            } else if (var3) {
               return ModelFileExtensionType.None;
            } else {
               if (!var2.endsWith(".x")) {
                  this.m_fileName = this.m_mediaFilePath + "/" + var2 + ".txt";
                  if (var1.contains("media/")) {
                     this.m_fileName = var1;
                  }

                  this.m_fileName = ZomboidFileSystem.instance.getString(this.m_fileName);
                  if ((new File(this.m_fileName)).exists()) {
                     return ModelFileExtensionType.Txt;
                  }
               }

               return ModelFileExtensionType.None;
            }
         }
      }
   }

   public abstract String getRawFileName();

   public abstract ProcessedAiScene loadX() throws IOException;

   public abstract ProcessedAiScene loadFBX() throws IOException;

   public abstract ModelTxt loadTxt() throws IOException;
}
