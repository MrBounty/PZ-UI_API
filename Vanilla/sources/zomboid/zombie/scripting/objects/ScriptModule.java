package zombie.scripting.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.runtime.RuntimeAnimationScript;
import zombie.debug.DebugLog;
import zombie.iso.MultiStageBuilding;
import zombie.scripting.IScriptObjectStore;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParser;
import zombie.vehicles.VehicleEngineRPM;

public final class ScriptModule extends BaseScriptObject implements IScriptObjectStore {
   public String name;
   public String value;
   public final HashMap ItemMap = new HashMap();
   public final HashMap GameSoundMap = new HashMap();
   public final ArrayList GameSoundList = new ArrayList();
   public final TreeMap ModelScriptMap;
   public final HashMap RuntimeAnimationScriptMap;
   public final HashMap SoundTimelineMap;
   public final HashMap VehicleMap;
   public final HashMap VehicleTemplateMap;
   public final HashMap VehicleEngineRPMMap;
   public final ArrayList RecipeMap;
   public final HashMap RecipeByName;
   public final HashMap RecipesWithDotInName;
   public final ArrayList EvolvedRecipeMap;
   public final ArrayList UniqueRecipeMap;
   public final HashMap FixingMap;
   public final ArrayList Imports;
   public boolean disabled;

   public ScriptModule() {
      this.ModelScriptMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.RuntimeAnimationScriptMap = new HashMap();
      this.SoundTimelineMap = new HashMap();
      this.VehicleMap = new HashMap();
      this.VehicleTemplateMap = new HashMap();
      this.VehicleEngineRPMMap = new HashMap();
      this.RecipeMap = new ArrayList();
      this.RecipeByName = new HashMap();
      this.RecipesWithDotInName = new HashMap();
      this.EvolvedRecipeMap = new ArrayList();
      this.UniqueRecipeMap = new ArrayList();
      this.FixingMap = new HashMap();
      this.Imports = new ArrayList();
      this.disabled = false;
   }

   public void Load(String var1, String var2) {
      this.name = var1;
      this.value = var2.trim();
      ScriptManager.instance.CurrentLoadingModule = this;
      this.ParseScriptPP(this.value);
      this.ParseScript(this.value);
      this.value = "";
   }

   private String GetTokenType(String var1) {
      int var2 = var1.indexOf(123);
      if (var2 == -1) {
         return null;
      } else {
         String var3 = var1.substring(0, var2).trim();
         int var4 = var3.indexOf(32);
         int var5 = var3.indexOf(9);
         if (var4 != -1 && var5 != -1) {
            return var3.substring(0, PZMath.min(var4, var5));
         } else if (var4 != -1) {
            return var3.substring(0, var4);
         } else {
            return var5 != -1 ? var3.substring(0, var5) : var3;
         }
      }
   }

   private void CreateFromTokenPP(String var1) {
      var1 = var1.trim();
      String var2 = this.GetTokenType(var1);
      if (var2 != null) {
         String[] var3;
         String var4;
         if ("item".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("item", "");
            var4 = var4.trim();
            Item var5 = new Item();
            this.ItemMap.put(var4, var5);
         } else if ("model".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("model", "");
            var4 = var4.trim();
            ModelScript var9;
            if (this.ModelScriptMap.containsKey(var4)) {
               var9 = (ModelScript)this.ModelScriptMap.get(var4);
               var9.reset();
            } else {
               var9 = new ModelScript();
               this.ModelScriptMap.put(var4, var9);
            }
         } else if ("sound".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("sound", "");
            var4 = var4.trim();
            GameSoundScript var10;
            if (this.GameSoundMap.containsKey(var4)) {
               var10 = (GameSoundScript)this.GameSoundMap.get(var4);
               var10.reset();
            } else {
               var10 = new GameSoundScript();
               this.GameSoundMap.put(var4, var10);
               this.GameSoundList.add(var10);
            }
         } else if ("soundTimeline".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("soundTimeline", "");
            var4 = var4.trim();
            SoundTimelineScript var11;
            if (this.SoundTimelineMap.containsKey(var4)) {
               var11 = (SoundTimelineScript)this.SoundTimelineMap.get(var4);
               var11.reset();
            } else {
               var11 = new SoundTimelineScript();
               this.SoundTimelineMap.put(var4, var11);
            }
         } else if ("vehicle".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("vehicle", "");
            var4 = var4.trim();
            VehicleScript var12 = new VehicleScript();
            this.VehicleMap.put(var4, var12);
         } else if ("template".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("template", "");
            String[] var13 = var4.trim().split("\\s+");
            if (var13.length == 2) {
               String var6 = var13[0].trim();
               String var7 = var13[1].trim();
               if ("vehicle".equals(var6)) {
                  VehicleTemplate var8 = new VehicleTemplate(this, var7, var1);
                  var8.module = this;
                  this.VehicleTemplateMap.put(var7, var8);
               }
            }
         } else if ("animation".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("animation", "");
            var4 = var4.trim();
            RuntimeAnimationScript var14;
            if (this.RuntimeAnimationScriptMap.containsKey(var4)) {
               var14 = (RuntimeAnimationScript)this.RuntimeAnimationScriptMap.get(var4);
               var14.reset();
            } else {
               var14 = new RuntimeAnimationScript();
               this.RuntimeAnimationScriptMap.put(var4, var14);
            }
         } else if ("vehicleEngineRPM".equals(var2)) {
            var3 = var1.split("[{}]");
            var4 = var3[0];
            var4 = var4.replace("vehicleEngineRPM", "");
            var4 = var4.trim();
            VehicleEngineRPM var15;
            if (this.VehicleEngineRPMMap.containsKey(var4)) {
               var15 = (VehicleEngineRPM)this.VehicleEngineRPMMap.get(var4);
               var15.reset();
            } else {
               var15 = new VehicleEngineRPM();
               this.VehicleEngineRPMMap.put(var4, var15);
            }
         }

      }
   }

   private void CreateFromToken(String var1) {
      var1 = var1.trim();
      String var2 = this.GetTokenType(var1);
      if (var2 != null) {
         String[] var3;
         if ("imports".equals(var2)) {
            var3 = var1.split("[{}]");
            String[] var4 = var3[1].split(",");

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].trim().length() > 0) {
                  String var6 = var4[var5].trim();
                  if (var6.equals(this.getName())) {
                     DebugLog.log("ERROR: module \"" + this.getName() + "\" imports itself");
                  } else {
                     this.Imports.add(var6);
                  }
               }
            }
         } else {
            String var16;
            String[] var17;
            if ("item".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("item", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               Item var18 = (Item)this.ItemMap.get(var16);
               var18.module = this;

               try {
                  var18.Load(var16, var17);
               } catch (Exception var15) {
                  DebugLog.log((Object)var15);
               }
            } else if ("recipe".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("recipe", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               Recipe var19 = new Recipe();
               this.RecipeMap.add(var19);
               if (!this.RecipeByName.containsKey(var16)) {
                  this.RecipeByName.put(var16, var19);
               }

               if (var16.contains(".")) {
                  this.RecipesWithDotInName.put(var16, var19);
               }

               var19.module = this;
               var19.Load(var16, var17);
            } else if ("uniquerecipe".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("uniquerecipe", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               UniqueRecipe var20 = new UniqueRecipe(var16);
               this.UniqueRecipeMap.add(var20);
               var20.module = this;
               var20.Load(var16, var17);
            } else if ("evolvedrecipe".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("evolvedrecipe", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               boolean var21 = false;
               Iterator var7 = this.EvolvedRecipeMap.iterator();

               while(var7.hasNext()) {
                  EvolvedRecipe var8 = (EvolvedRecipe)var7.next();
                  if (var8.name.equals(var16)) {
                     var8.Load(var16, var17);
                     var8.module = this;
                     var21 = true;
                  }
               }

               if (!var21) {
                  EvolvedRecipe var22 = new EvolvedRecipe(var16);
                  this.EvolvedRecipeMap.add(var22);
                  var22.module = this;
                  var22.Load(var16, var17);
               }
            } else if ("fixing".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("fixing", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               Fixing var24 = new Fixing();
               var24.module = this;
               this.FixingMap.put(var16, var24);
               var24.Load(var16, var17);
            } else if ("multistagebuild".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("multistagebuild", "");
               var16 = var16.trim();
               var17 = var3[1].split(",");
               MultiStageBuilding.Stage var26 = new MultiStageBuilding().new Stage();
               var26.Load(var16, var17);
               MultiStageBuilding.addStage(var26);
            } else if ("model".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("model", "");
               var16 = var16.trim();
               ModelScript var23 = (ModelScript)this.ModelScriptMap.get(var16);
               var23.module = this;

               try {
                  var23.Load(var16, var1);
               } catch (Throwable var14) {
                  ExceptionLogger.logException(var14);
               }
            } else if ("sound".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("sound", "");
               var16 = var16.trim();
               GameSoundScript var25 = (GameSoundScript)this.GameSoundMap.get(var16);
               var25.module = this;

               try {
                  var25.Load(var16, var1);
               } catch (Throwable var13) {
                  ExceptionLogger.logException(var13);
               }
            } else if ("soundTimeline".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("soundTimeline", "");
               var16 = var16.trim();
               SoundTimelineScript var27 = (SoundTimelineScript)this.SoundTimelineMap.get(var16);
               var27.module = this;

               try {
                  var27.Load(var16, var1);
               } catch (Throwable var12) {
                  ExceptionLogger.logException(var12);
               }
            } else if ("vehicle".equals(var2)) {
               var3 = var1.split("[{}]");
               var16 = var3[0];
               var16 = var16.replace("vehicle", "");
               var16 = var16.trim();
               VehicleScript var28 = (VehicleScript)this.VehicleMap.get(var16);
               var28.module = this;

               try {
                  var28.Load(var16, var1);
                  var28.Loaded();
               } catch (Exception var11) {
                  ExceptionLogger.logException(var11);
               }
            } else if (!"template".equals(var2)) {
               if ("animation".equals(var2)) {
                  var3 = var1.split("[{}]");
                  var16 = var3[0];
                  var16 = var16.replace("animation", "");
                  var16 = var16.trim();
                  RuntimeAnimationScript var29 = (RuntimeAnimationScript)this.RuntimeAnimationScriptMap.get(var16);
                  var29.module = this;

                  try {
                     var29.Load(var16, var1);
                  } catch (Throwable var10) {
                     ExceptionLogger.logException(var10);
                  }
               } else if ("vehicleEngineRPM".equals(var2)) {
                  var3 = var1.split("[{}]");
                  var16 = var3[0];
                  var16 = var16.replace("vehicleEngineRPM", "");
                  var16 = var16.trim();
                  VehicleEngineRPM var30 = (VehicleEngineRPM)this.VehicleEngineRPMMap.get(var16);
                  var30.module = this;

                  try {
                     var30.Load(var16, var1);
                  } catch (Throwable var9) {
                     this.VehicleEngineRPMMap.remove(var16);
                     ExceptionLogger.logException(var9);
                  }
               } else {
                  DebugLog.Script.warn("unknown script object \"%s\"", var2);
               }
            }
         }

      }
   }

   public void ParseScript(String var1) {
      ArrayList var2 = ScriptParser.parseTokens(var1);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var4 = (String)var2.get(var3);
         this.CreateFromToken(var4);
      }

   }

   public void ParseScriptPP(String var1) {
      ArrayList var2 = ScriptParser.parseTokens(var1);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var4 = (String)var2.get(var3);
         this.CreateFromTokenPP(var4);
      }

   }

   public Item getItem(String var1) {
      if (var1.contains(".")) {
         return ScriptManager.instance.getItem(var1);
      } else if (!this.ItemMap.containsKey(var1)) {
         for(int var2 = 0; var2 < this.Imports.size(); ++var2) {
            String var3 = (String)this.Imports.get(var2);
            ScriptModule var4 = ScriptManager.instance.getModule(var3);
            Item var5 = var4.getItem(var1);
            if (var5 != null) {
               return var5;
            }
         }

         return null;
      } else {
         return (Item)this.ItemMap.get(var1);
      }
   }

   public Recipe getRecipe(String var1) {
      if (var1.contains(".") && !this.RecipesWithDotInName.containsKey(var1)) {
         return ScriptManager.instance.getRecipe(var1);
      } else {
         Recipe var2 = (Recipe)this.RecipeByName.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            for(int var3 = 0; var3 < this.Imports.size(); ++var3) {
               ScriptModule var4 = ScriptManager.instance.getModule((String)this.Imports.get(var3));
               if (var4 != null) {
                  var2 = var4.getRecipe(var1);
                  if (var2 != null) {
                     return var2;
                  }
               }
            }

            return null;
         }
      }
   }

   public VehicleScript getVehicle(String var1) {
      if (var1.contains(".")) {
         return ScriptManager.instance.getVehicle(var1);
      } else if (!this.VehicleMap.containsKey(var1)) {
         for(int var2 = 0; var2 < this.Imports.size(); ++var2) {
            VehicleScript var3 = ScriptManager.instance.getModule((String)this.Imports.get(var2)).getVehicle(var1);
            if (var3 != null) {
               return var3;
            }
         }

         return null;
      } else {
         return (VehicleScript)this.VehicleMap.get(var1);
      }
   }

   public VehicleTemplate getVehicleTemplate(String var1) {
      if (var1.contains(".")) {
         return ScriptManager.instance.getVehicleTemplate(var1);
      } else if (!this.VehicleTemplateMap.containsKey(var1)) {
         for(int var2 = 0; var2 < this.Imports.size(); ++var2) {
            VehicleTemplate var3 = ScriptManager.instance.getModule((String)this.Imports.get(var2)).getVehicleTemplate(var1);
            if (var3 != null) {
               return var3;
            }
         }

         return null;
      } else {
         return (VehicleTemplate)this.VehicleTemplateMap.get(var1);
      }
   }

   public VehicleEngineRPM getVehicleEngineRPM(String var1) {
      return var1.contains(".") ? ScriptManager.instance.getVehicleEngineRPM(var1) : (VehicleEngineRPM)this.VehicleEngineRPMMap.get(var1);
   }

   public boolean CheckExitPoints() {
      return false;
   }

   public String getName() {
      return this.name;
   }

   public void Reset() {
      this.ItemMap.clear();
      this.GameSoundMap.clear();
      this.GameSoundList.clear();
      this.ModelScriptMap.clear();
      this.RuntimeAnimationScriptMap.clear();
      this.SoundTimelineMap.clear();
      this.VehicleMap.clear();
      this.VehicleTemplateMap.clear();
      this.VehicleEngineRPMMap.clear();
      this.RecipeMap.clear();
      this.RecipeByName.clear();
      this.RecipesWithDotInName.clear();
      this.EvolvedRecipeMap.clear();
      this.UniqueRecipeMap.clear();
      this.FixingMap.clear();
      this.Imports.clear();
   }
}
