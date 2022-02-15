package zombie.gameStates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import se.krka.kahlua.vm.KahluaTable;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.skinnedmodel.ModelManager;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParser;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.ui.UIManager;
import zombie.vehicles.EditVehicleState;

public final class AttachmentEditorState extends GameState {
   public static AttachmentEditorState instance;
   private EditVehicleState.LuaEnvironment m_luaEnv;
   private boolean bExit = false;
   private final ArrayList m_gameUI = new ArrayList();
   private final ArrayList m_selfUI = new ArrayList();
   private boolean m_bSuspendUI;
   private KahluaTable m_table = null;

   public void enter() {
      instance = this;
      if (this.m_luaEnv == null) {
         this.m_luaEnv = new EditVehicleState.LuaEnvironment(LuaManager.platform, LuaManager.converterManager, LuaManager.env);
      }

      this.saveGameUI();
      if (this.m_selfUI.size() == 0) {
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_luaEnv.env.rawget("AttachmentEditorState_InitUI"));
         if (this.m_table != null && this.m_table.getMetatable() != null) {
            this.m_table.getMetatable().rawset("_LUA_RELOADED_CHECK", Boolean.FALSE);
         }
      } else {
         UIManager.UI.addAll(this.m_selfUI);
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_table.rawget("showUI"), (Object)this.m_table);
      }

      this.bExit = false;
   }

   public void yield() {
      this.restoreGameUI();
   }

   public void reenter() {
      this.saveGameUI();
   }

   public void exit() {
      this.restoreGameUI();
   }

   public void render() {
      byte var1 = 0;
      Core.getInstance().StartFrame(var1, true);
      this.renderScene();
      Core.getInstance().EndFrame(var1);
      Core.getInstance().RenderOffScreenBuffer();
      if (Core.getInstance().StartFrameUI()) {
         this.renderUI();
      }

      Core.getInstance().EndFrameUI();
   }

   public GameStateMachine.StateAction update() {
      if (!this.bExit && !GameKeyboard.isKeyPressed(65)) {
         this.updateScene();
         return GameStateMachine.StateAction.Remain;
      } else {
         return GameStateMachine.StateAction.Continue;
      }
   }

   public static AttachmentEditorState checkInstance() {
      if (instance != null) {
         if (instance.m_table != null && instance.m_table.getMetatable() != null) {
            if (instance.m_table.getMetatable().rawget("_LUA_RELOADED_CHECK") == null) {
               instance = null;
            }
         } else {
            instance = null;
         }
      }

      return instance == null ? new AttachmentEditorState() : instance;
   }

   private void saveGameUI() {
      this.m_gameUI.clear();
      this.m_gameUI.addAll(UIManager.UI);
      UIManager.UI.clear();
      this.m_bSuspendUI = UIManager.bSuspend;
      UIManager.bSuspend = false;
      UIManager.setShowPausedMessage(false);
      UIManager.defaultthread = this.m_luaEnv.thread;
   }

   private void restoreGameUI() {
      this.m_selfUI.clear();
      this.m_selfUI.addAll(UIManager.UI);
      UIManager.UI.clear();
      UIManager.UI.addAll(this.m_gameUI);
      UIManager.bSuspend = this.m_bSuspendUI;
      UIManager.setShowPausedMessage(true);
      UIManager.defaultthread = LuaManager.thread;
   }

   private void updateScene() {
      ModelManager.instance.update();
      if (GameKeyboard.isKeyPressed(17)) {
         DebugOptions.instance.ModelRenderWireframe.setValue(!DebugOptions.instance.ModelRenderWireframe.getValue());
      }

   }

   private void renderScene() {
   }

   private void renderUI() {
      UIManager.render();
   }

   public void setTable(KahluaTable var1) {
      this.m_table = var1;
   }

   public Object fromLua0(String var1) {
      byte var3 = -1;
      switch(var1.hashCode()) {
      case 3127582:
         if (var1.equals("exit")) {
            var3 = 0;
         }
      default:
         switch(var3) {
         case 0:
            this.bExit = true;
            return null;
         default:
            throw new IllegalArgumentException("unhandled \"" + var1 + "\"");
         }
      }
   }

   public Object fromLua1(String var1, Object var2) {
      byte var4 = -1;
      switch(var1.hashCode()) {
      case 1396535690:
         if (var1.equals("writeScript")) {
            var4 = 0;
         }
      default:
         switch(var4) {
         case 0:
            ModelScript var5 = ScriptManager.instance.getModelScript((String)var2);
            if (var5 == null) {
               throw new NullPointerException("model script \"" + var2 + "\" not found");
            }

            ArrayList var6 = this.readScript(var5.getFileName());
            if (var6 != null) {
               this.updateScript(var5.getFileName(), var6, var5);
            }

            return null;
         default:
            throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\"", var1, var2));
         }
      }
   }

   private ArrayList readScript(String var1) {
      StringBuilder var2 = new StringBuilder();
      var1 = ZomboidFileSystem.instance.getString(var1);
      File var3 = new File(var1);

      try {
         FileReader var4 = new FileReader(var3);

         try {
            BufferedReader var5 = new BufferedReader(var4);

            try {
               String var6 = System.lineSeparator();

               String var7;
               while((var7 = var5.readLine()) != null) {
                  var2.append(var7);
                  var2.append(var6);
               }
            } catch (Throwable var10) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var5.close();
         } catch (Throwable var11) {
            try {
               var4.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }

            throw var11;
         }

         var4.close();
      } catch (Throwable var12) {
         ExceptionLogger.logException(var12);
         return null;
      }

      String var13 = ScriptParser.stripComments(var2.toString());
      return ScriptParser.parseTokens(var13);
   }

   private void updateScript(String var1, ArrayList var2, ModelScript var3) {
      var1 = ZomboidFileSystem.instance.getString(var1);

      for(int var4 = var2.size() - 1; var4 >= 0; --var4) {
         String var5 = ((String)var2.get(var4)).trim();
         int var6 = var5.indexOf("{");
         int var7 = var5.lastIndexOf("}");
         String var8 = var5.substring(0, var6);
         if (var8.startsWith("module")) {
            var8 = var5.substring(0, var6).trim();
            String[] var9 = var8.split("\\s+");
            String var10 = var9.length > 1 ? var9[1].trim() : "";
            if (var10.equals(var3.getModule().getName())) {
               String var11 = var5.substring(var6 + 1, var7).trim();
               ArrayList var12 = ScriptParser.parseTokens(var11);

               for(int var13 = var12.size() - 1; var13 >= 0; --var13) {
                  String var14 = ((String)var12.get(var13)).trim();
                  if (var14.startsWith("model")) {
                     var6 = var14.indexOf("{");
                     var8 = var14.substring(0, var6).trim();
                     var9 = var8.split("\\s+");
                     String var15 = var9.length > 1 ? var9[1].trim() : "";
                     if (var15.equals(var3.getName())) {
                        var14 = this.modelScriptToText(var3, var14).trim();
                        var12.set(var13, var14);
                        String var16 = System.lineSeparator();
                        String var17 = String.join(var16 + "\t", var12);
                        var17 = "module " + var10 + var16 + "{" + var16 + "\t" + var17 + var16 + "}" + var16;
                        var2.set(var4, var17);
                        this.writeScript(var1, var2);
                        return;
                     }
                  }
               }
            }
         }
      }

   }

   private String modelScriptToText(ModelScript var1, String var2) {
      ScriptParser.Block var3 = ScriptParser.parse(var2);
      var3 = (ScriptParser.Block)var3.children.get(0);

      int var4;
      for(var4 = var3.children.size() - 1; var4 >= 0; --var4) {
         ScriptParser.Block var5 = (ScriptParser.Block)var3.children.get(var4);
         if ("attachment".equals(var5.type)) {
            var3.elements.remove(var5);
            var3.children.remove(var4);
         }
      }

      for(var4 = 0; var4 < var1.getAttachmentCount(); ++var4) {
         ModelAttachment var8 = var1.getAttachment(var4);
         ScriptParser.Block var6 = var3.getBlock("attachment", var8.getId());
         if (var6 == null) {
            var6 = new ScriptParser.Block();
            var6.type = "attachment";
            var6.id = var8.getId();
            var6.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var8.getOffset().x(), var8.getOffset().y(), var8.getOffset().z()));
            var6.setValue("rotate", String.format(Locale.US, "%.4f %.4f %.4f", var8.getRotate().x(), var8.getRotate().y(), var8.getRotate().z()));
            if (var8.getBone() != null) {
               var6.setValue("bone", var8.getBone());
            }

            var3.elements.add(var6);
            var3.children.add(var6);
         } else {
            var6.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var8.getOffset().x(), var8.getOffset().y(), var8.getOffset().z()));
            var6.setValue("rotate", String.format(Locale.US, "%.4f %.4f %.4f", var8.getRotate().x(), var8.getRotate().y(), var8.getRotate().z()));
         }
      }

      StringBuilder var7 = new StringBuilder();
      String var9 = System.lineSeparator();
      var3.prettyPrint(1, var7, var9);
      return var7.toString();
   }

   private void writeScript(String var1, ArrayList var2) {
      String var3 = ZomboidFileSystem.instance.getString(var1);
      File var4 = new File(var3);

      try {
         FileWriter var5 = new FileWriter(var4);

         try {
            BufferedWriter var6 = new BufferedWriter(var5);

            try {
               DebugLog.General.printf("writing %s\n", var1);
               Iterator var7 = var2.iterator();

               while(true) {
                  if (!var7.hasNext()) {
                     this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_table.rawget("wroteScript"), this.m_table, var3);
                     break;
                  }

                  String var8 = (String)var7.next();
                  var6.write(var8);
               }
            } catch (Throwable var11) {
               try {
                  var6.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            var6.close();
         } catch (Throwable var12) {
            try {
               var5.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         var5.close();
      } catch (Throwable var13) {
         ExceptionLogger.logException(var13);
      }

   }
}
