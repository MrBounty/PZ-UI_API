package zombie.input;

import org.lwjglx.input.KeyEventQueue;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.opengl.RenderThread;
import zombie.ui.UIManager;

public final class GameKeyboard {
   private static boolean[] bDown;
   private static boolean[] bLastDown;
   private static boolean[] bEatKey;
   public static boolean bNoEventsWhileLoading = false;
   public static boolean doLuaKeyPressed = true;
   private static final KeyboardStateCache s_keyboardStateCache = new KeyboardStateCache();

   public static void update() {
      if (!s_keyboardStateCache.getState().isCreated()) {
         s_keyboardStateCache.swap();
      } else {
         int var0 = s_keyboardStateCache.getState().getKeyCount();
         if (bDown == null) {
            bDown = new boolean[var0];
            bLastDown = new boolean[var0];
            bEatKey = new boolean[var0];
         }

         boolean var1 = Core.CurrentTextEntryBox != null && Core.CurrentTextEntryBox.DoingTextEntry;

         for(int var2 = 1; var2 < var0; ++var2) {
            bLastDown[var2] = bDown[var2];
            bDown[var2] = s_keyboardStateCache.getState().isKeyDown(var2);
            if (!bDown[var2] && bLastDown[var2]) {
               if (bEatKey[var2]) {
                  bEatKey[var2] = false;
                  continue;
               }

               if (bNoEventsWhileLoading || var1 || LuaManager.thread == UIManager.defaultthread && UIManager.onKeyRelease(var2)) {
                  continue;
               }

               if (Core.bDebug && !doLuaKeyPressed) {
                  System.out.println("KEY RELEASED " + var2 + " doLuaKeyPressed=false");
               }

               if (LuaManager.thread == UIManager.defaultthread && doLuaKeyPressed) {
                  LuaEventManager.triggerEvent("OnKeyPressed", var2);
               }

               if (LuaManager.thread == UIManager.defaultthread) {
                  LuaEventManager.triggerEvent("OnCustomUIKey", var2);
                  LuaEventManager.triggerEvent("OnCustomUIKeyReleased", var2);
               }
            }

            if (bDown[var2] && bLastDown[var2]) {
               if (bNoEventsWhileLoading || var1 || LuaManager.thread == UIManager.defaultthread && UIManager.onKeyRepeat(var2)) {
                  continue;
               }

               if (LuaManager.thread == UIManager.defaultthread && doLuaKeyPressed) {
                  LuaEventManager.triggerEvent("OnKeyKeepPressed", var2);
               }
            }

            if (bDown[var2] && !bLastDown[var2] && !bNoEventsWhileLoading && !var1 && !bEatKey[var2] && (LuaManager.thread != UIManager.defaultthread || !UIManager.onKeyPress(var2)) && !bEatKey[var2]) {
               if (LuaManager.thread == UIManager.defaultthread && doLuaKeyPressed) {
                  LuaEventManager.triggerEvent("OnKeyStartPressed", var2);
               }

               if (LuaManager.thread == UIManager.defaultthread) {
                  LuaEventManager.triggerEvent("OnCustomUIKeyPressed", var2);
               }
            }
         }

         s_keyboardStateCache.swap();
      }
   }

   public static void poll() {
      s_keyboardStateCache.poll();
   }

   public static boolean isKeyPressed(int var0) {
      return isKeyDown(var0) && !wasKeyDown(var0);
   }

   public static boolean isKeyDown(int var0) {
      if (Core.CurrentTextEntryBox != null && Core.CurrentTextEntryBox.DoingTextEntry) {
         return false;
      } else {
         return bDown == null ? false : bDown[var0];
      }
   }

   public static boolean wasKeyDown(int var0) {
      if (Core.CurrentTextEntryBox != null && Core.CurrentTextEntryBox.DoingTextEntry) {
         return false;
      } else {
         return bLastDown == null ? false : bLastDown[var0];
      }
   }

   public static void eatKeyPress(int var0) {
      if (var0 >= 0 && var0 < bEatKey.length) {
         bEatKey[var0] = true;
      }
   }

   public static void setDoLuaKeyPressed(boolean var0) {
      doLuaKeyPressed = var0;
   }

   public static KeyEventQueue getEventQueue() {
      assert Thread.currentThread() == GameWindow.GameThread;

      return s_keyboardStateCache.getState().getEventQueue();
   }

   public static KeyEventQueue getEventQueuePolling() {
      assert Thread.currentThread() == RenderThread.RenderThread;

      return s_keyboardStateCache.getStatePolling().getEventQueue();
   }
}
