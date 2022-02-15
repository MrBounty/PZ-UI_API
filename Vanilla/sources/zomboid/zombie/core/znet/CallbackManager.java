package zombie.core.znet;

import zombie.Lua.LuaEventManager;

public class CallbackManager implements IJoinRequestCallback {
   public CallbackManager() {
      SteamUtils.addJoinRequestCallback(this);
   }

   public void onJoinRequest(long var1, String var3) {
      LuaEventManager.triggerEvent("OnAcceptInvite", var3);
   }
}
