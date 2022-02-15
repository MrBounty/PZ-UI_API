package zombie.vehicles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.joml.Vector2f;
import org.joml.Vector3f;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.skinnedmodel.ModelManager;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.gameStates.GameState;
import zombie.gameStates.GameStateMachine;
import zombie.input.GameKeyboard;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParser;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.UIManager;
import zombie.util.list.PZArrayUtil;

public final class EditVehicleState extends GameState {
   public static EditVehicleState instance;
   private EditVehicleState.LuaEnvironment m_luaEnv;
   private boolean bExit = false;
   private String m_initialScript = null;
   private final ArrayList m_gameUI = new ArrayList();
   private final ArrayList m_selfUI = new ArrayList();
   private boolean m_bSuspendUI;
   private KahluaTable m_table = null;

   public EditVehicleState() {
      instance = this;
   }

   public void enter() {
      instance = this;
      if (this.m_luaEnv == null) {
         this.m_luaEnv = new EditVehicleState.LuaEnvironment(LuaManager.platform, LuaManager.converterManager, LuaManager.env);
      }

      this.saveGameUI();
      if (this.m_selfUI.size() == 0) {
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_luaEnv.env.rawget("EditVehicleState_InitUI"));
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

   public static EditVehicleState checkInstance() {
      if (instance != null) {
         if (instance.m_table != null && instance.m_table.getMetatable() != null) {
            if (instance.m_table.getMetatable().rawget("_LUA_RELOADED_CHECK") == null) {
               instance = null;
            }
         } else {
            instance = null;
         }
      }

      return instance == null ? new EditVehicleState() : instance;
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

   public void setScript(String var1) {
      if (this.m_table == null) {
         this.m_initialScript = var1;
      } else {
         this.m_luaEnv.caller.pcall(this.m_luaEnv.thread, this.m_table.rawget("setScript"), this.m_table, var1);
      }

   }

   public Object fromLua0(String var1) {
      byte var3 = -1;
      switch(var1.hashCode()) {
      case -1286189703:
         if (var1.equals("getInitialScript")) {
            var3 = 1;
         }
         break;
      case 3127582:
         if (var1.equals("exit")) {
            var3 = 0;
         }
      }

      switch(var3) {
      case 0:
         this.bExit = true;
         return null;
      case 1:
         return this.m_initialScript;
      default:
         throw new IllegalArgumentException("unhandled \"" + var1 + "\"");
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
            VehicleScript var5 = ScriptManager.instance.getVehicle((String)var2);
            if (var5 == null) {
               throw new NullPointerException("vehicle script \"" + var2 + "\" not found");
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

   private void updateScript(String var1, ArrayList var2, VehicleScript var3) {
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
                  if (var14.startsWith("vehicle")) {
                     var6 = var14.indexOf("{");
                     var8 = var14.substring(0, var6).trim();
                     var9 = var8.split("\\s+");
                     String var15 = var9.length > 1 ? var9[1].trim() : "";
                     if (var15.equals(var3.getName())) {
                        var14 = this.vehicleScriptToText(var3, var14).trim();
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

   private String vehicleScriptToText(VehicleScript var1, String var2) {
      float var3 = var1.getModelScale();
      ScriptParser.Block var4 = ScriptParser.parse(var2);
      var4 = (ScriptParser.Block)var4.children.get(0);
      VehicleScript.Model var5 = var1.getModel();
      ScriptParser.Block var6 = var4.getBlock("model", (String)null);
      if (var5 != null && var6 != null) {
         float var7 = var1.getModelScale();
         var6.setValue("scale", String.format(Locale.US, "%.4f", var7));
         Vector3f var8 = var1.getModel().getOffset();
         var6.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var8.x / var3, var8.y / var3, var8.z / var3));
      }

      ArrayList var12 = new ArrayList();

      int var13;
      ScriptParser.Block var14;
      for(var13 = 0; var13 < var4.children.size(); ++var13) {
         var14 = (ScriptParser.Block)var4.children.get(var13);
         if ("physics".equals(var14.type)) {
            if (var12.size() == var1.getPhysicsShapeCount()) {
               var4.elements.remove(var14);
               var4.children.remove(var13);
               --var13;
            } else {
               var12.add(var14);
            }
         }
      }

      for(var13 = 0; var13 < var1.getPhysicsShapeCount(); ++var13) {
         VehicleScript.PhysicsShape var15 = var1.getPhysicsShape(var13);
         boolean var16 = var13 < var12.size();
         ScriptParser.Block var9 = var16 ? (ScriptParser.Block)var12.get(var13) : new ScriptParser.Block();
         var9.type = "physics";
         var9.id = var15.getTypeString();
         if (var16) {
            var9.elements.clear();
            var9.children.clear();
            var9.values.clear();
         }

         var9.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var15.getOffset().x() / var3, var15.getOffset().y() / var3, var15.getOffset().z() / var3));
         if (var15.type == 1) {
            var9.setValue("extents", String.format(Locale.US, "%.4f %.4f %.4f", var15.getExtents().x() / var3, var15.getExtents().y() / var3, var15.getExtents().z() / var3));
            var9.setValue("rotate", String.format(Locale.US, "%.4f %.4f %.4f", var15.getRotate().x(), var15.getRotate().y(), var15.getRotate().z()));
         }

         if (var15.type == 2) {
            var9.setValue("radius", String.format(Locale.US, "%.4f", var15.getRadius() / var3));
         }

         if (!var16) {
            var4.elements.add(var9);
            var4.children.add(var9);
         }
      }

      for(var13 = var4.children.size() - 1; var13 >= 0; --var13) {
         var14 = (ScriptParser.Block)var4.children.get(var13);
         if ("attachment".equals(var14.type)) {
            var4.elements.remove(var14);
            var4.children.remove(var13);
         }
      }

      ScriptParser.Block var19;
      for(var13 = 0; var13 < var1.getAttachmentCount(); ++var13) {
         ModelAttachment var17 = var1.getAttachment(var13);
         var19 = var4.getBlock("attachment", var17.getId());
         if (var19 == null) {
            var19 = new ScriptParser.Block();
            var19.type = "attachment";
            var19.id = var17.getId();
            var4.elements.add(var19);
            var4.children.add(var19);
         }

         var19.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var17.getOffset().x() / var3, var17.getOffset().y() / var3, var17.getOffset().z() / var3));
         var19.setValue("rotate", String.format(Locale.US, "%.4f %.4f %.4f", var17.getRotate().x(), var17.getRotate().y(), var17.getRotate().z()));
         if (var17.getBone() != null) {
            var19.setValue("bone", var17.getBone());
         }

         if (var17.getCanAttach() != null) {
            var19.setValue("canAttach", PZArrayUtil.arrayToString((Iterable)var17.getCanAttach(), "", "", ","));
         }

         if (var17.getZOffset() != 0.0F) {
            var19.setValue("zoffset", String.format(Locale.US, "%.4f", var17.getZOffset()));
         }

         if (!var17.isUpdateConstraint()) {
            var19.setValue("updateconstraint", "false");
         }
      }

      Vector3f var22 = var1.getExtents();
      var4.setValue("extents", String.format(Locale.US, "%.4f %.4f %.4f", var22.x / var3, var22.y / var3, var22.z / var3));
      var22 = var1.getPhysicsChassisShape();
      var4.setValue("physicsChassisShape", String.format(Locale.US, "%.4f %.4f %.4f", var22.x / var3, var22.y / var3, var22.z / var3));
      var22 = var1.getCenterOfMassOffset();
      var4.setValue("centerOfMassOffset", String.format(Locale.US, "%.4f %.4f %.4f", var22.x / var3, var22.y / var3, var22.z / var3));
      Vector2f var26 = var1.getShadowExtents();
      boolean var18 = var4.getValue("shadowExtents") != null;
      var4.setValue("shadowExtents", String.format(Locale.US, "%.4f %.4f", var26.x / var3, var26.y / var3));
      if (!var18) {
         var4.moveValueAfter("shadowExtents", "centerOfMassOffset");
      }

      var26 = var1.getShadowOffset();
      var18 = var4.getValue("shadowOffset") != null;
      var4.setValue("shadowOffset", String.format(Locale.US, "%.4f %.4f", var26.x / var3, var26.y / var3));
      if (!var18) {
         var4.moveValueAfter("shadowOffset", "shadowExtents");
      }

      for(var13 = 0; var13 < var1.getAreaCount(); ++var13) {
         VehicleScript.Area var20 = var1.getArea(var13);
         var19 = var4.getBlock("area", var20.getId());
         if (var19 != null) {
            var19.setValue("xywh", String.format(Locale.US, "%.4f %.4f %.4f %.4f", var20.getX() / (double)var3, var20.getY() / (double)var3, var20.getW() / (double)var3, var20.getH() / (double)var3));
         }
      }

      for(var13 = 0; var13 < var1.getPassengerCount(); ++var13) {
         VehicleScript.Passenger var23 = var1.getPassenger(var13);
         var19 = var4.getBlock("passenger", var23.getId());
         if (var19 != null) {
            Iterator var21 = var23.positions.iterator();

            while(var21.hasNext()) {
               VehicleScript.Position var10 = (VehicleScript.Position)var21.next();
               ScriptParser.Block var11 = var19.getBlock("position", var10.id);
               if (var11 != null) {
                  var11.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var10.offset.x / var3, var10.offset.y / var3, var10.offset.z / var3));
                  var11.setValue("rotate", String.format(Locale.US, "%.4f %.4f %.4f", var10.rotate.x / var3, var10.rotate.y / var3, var10.rotate.z / var3));
               }
            }
         }
      }

      for(var13 = 0; var13 < var1.getWheelCount(); ++var13) {
         VehicleScript.Wheel var24 = var1.getWheel(var13);
         var19 = var4.getBlock("wheel", var24.getId());
         if (var19 != null) {
            var19.setValue("offset", String.format(Locale.US, "%.4f %.4f %.4f", var24.offset.x / var3, var24.offset.y / var3, var24.offset.z / var3));
         }
      }

      StringBuilder var27 = new StringBuilder();
      String var25 = System.lineSeparator();
      var4.prettyPrint(1, var27, var25);
      return var27.toString();
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

   public static final class LuaEnvironment {
      public J2SEPlatform platform;
      public KahluaTable env;
      public KahluaThread thread;
      public LuaCaller caller;

      public LuaEnvironment(J2SEPlatform var1, KahluaConverterManager var2, KahluaTable var3) {
         this.platform = var1;
         this.env = var3;
         this.thread = LuaManager.thread;
         this.caller = LuaManager.caller;
      }
   }
}
