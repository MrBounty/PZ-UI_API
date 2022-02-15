package zombie.world.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import zombie.world.ItemInfo;

public class Log {
   public static class ModIDChangedItem extends Log.BaseItemLog {
      protected final String oldModID;
      protected final String newModID;

      public ModIDChangedItem(ItemInfo var1, String var2, String var3) {
         super(var1);
         this.oldModID = var2;
         this.newModID = var3;
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"modchange_item\", oldModID = \"" + this.oldModID + "\", " + this.getItemString() + " }" + System.lineSeparator());
      }
   }

   public static class RemovedItem extends Log.BaseItemLog {
      protected final boolean isScriptMissing;

      public RemovedItem(ItemInfo var1, boolean var2) {
         super(var1);
         this.isScriptMissing = var2;
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"removed_item\", scriptMissing = " + this.isScriptMissing + ", " + this.getItemString() + " }" + System.lineSeparator());
      }
   }

   public static class ObsoleteItem extends Log.BaseItemLog {
      public ObsoleteItem(ItemInfo var1) {
         super(var1);
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"obsolete_item\", " + this.getItemString() + " }" + System.lineSeparator());
      }
   }

   public static class ReinstateItem extends Log.BaseItemLog {
      public ReinstateItem(ItemInfo var1) {
         super(var1);
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"reinstate_item\", " + this.getItemString() + " }" + System.lineSeparator());
      }
   }

   public static class RegisterItem extends Log.BaseItemLog {
      public RegisterItem(ItemInfo var1) {
         super(var1);
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"reg_item\", " + this.getItemString() + " }" + System.lineSeparator());
      }
   }

   public abstract static class BaseItemLog extends Log.BaseLog {
      protected final ItemInfo itemInfo;

      public BaseItemLog(ItemInfo var1) {
         this.itemInfo = var1;
      }

      abstract void saveAsText(FileWriter var1, String var2) throws IOException;

      protected String getItemString() {
         String var10000 = this.itemInfo.getFullType();
         return "fulltype = \"" + var10000 + "\", registeryID = " + this.itemInfo.getRegistryID() + ", existsVanilla = " + this.itemInfo.isExistsAsVanilla() + ", isModded = " + this.itemInfo.isModded() + ", modID = \"" + this.itemInfo.getModID() + "\", obsolete = " + this.itemInfo.isObsolete() + ", removed = " + this.itemInfo.isRemoved() + ", isLoaded = " + this.itemInfo.isLoaded();
      }
   }

   public static class RegisterObject extends Log.BaseLog {
      protected final String objectName;
      protected final int ID;

      public RegisterObject(String var1, int var2) {
         this.objectName = var1;
         this.ID = var2;
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{ type = \"reg_obj\", id = " + this.ID + ", obj = \"" + this.objectName + "\" }" + System.lineSeparator());
      }
   }

   public static class Comment extends Log.BaseLog {
      protected String txt;

      public Comment(String var1) {
         this.ignoreSaveCheck = true;
         this.txt = var1;
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "-- " + this.txt + System.lineSeparator());
      }
   }

   public static class Info extends Log.BaseLog {
      protected final List mods;
      protected final String timeStamp;
      protected final String saveWorld;
      protected final int worldVersion;
      public boolean HasErrored = false;

      public Info(String var1, String var2, int var3, List var4) {
         this.ignoreSaveCheck = true;
         this.timeStamp = var1;
         this.saveWorld = var2;
         this.worldVersion = var3;
         this.mods = var4;
      }

      public void saveAsText(FileWriter var1, String var2) throws IOException {
         var1.write(var2 + "{" + System.lineSeparator());
         var1.write(var2 + "\ttype = \"info\"," + System.lineSeparator());
         var1.write(var2 + "\ttimeStamp = \"" + this.timeStamp + "\"," + System.lineSeparator());
         var1.write(var2 + "\tsaveWorld = \"" + this.saveWorld + "\"," + System.lineSeparator());
         var1.write(var2 + "\tworldVersion = " + this.worldVersion + "," + System.lineSeparator());
         var1.write(var2 + "\thasErrored = " + this.HasErrored + "," + System.lineSeparator());
         var1.write(var2 + "\titemMods = {" + System.lineSeparator());

         for(int var3 = 0; var3 < this.mods.size(); ++var3) {
            var1.write(var2 + "\t\t\"" + (String)this.mods.get(var3) + "\"," + System.lineSeparator());
         }

         var1.write(var2 + "\t}," + System.lineSeparator());
         var1.write(var2 + "}," + System.lineSeparator());
      }
   }

   public abstract static class BaseLog {
      protected boolean ignoreSaveCheck = false;

      public boolean isIgnoreSaveCheck() {
         return this.ignoreSaveCheck;
      }

      abstract void saveAsText(FileWriter var1, String var2) throws IOException;
   }
}
