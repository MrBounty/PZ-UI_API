package zombie.ui;

import java.util.ArrayList;
import java.util.Objects;
import zombie.core.Translator;
import zombie.core.znet.SteamUtils;
import zombie.network.CoopMaster;
import zombie.network.ICoopServerMessageListener;

public final class UIServerToolbox extends NewWindow implements ICoopServerMessageListener, UIEventHandler {
   public static UIServerToolbox instance;
   ScrollBar ScrollBarV;
   UITextBox2 OutputLog;
   private final ArrayList incomingConnections = new ArrayList();
   DialogButton buttonAccept;
   DialogButton buttonReject;
   private String externalAddress = null;
   private String steamID = null;
   public boolean autoAccept = false;

   public UIServerToolbox(int var1, int var2) {
      super(var1, var2, 10, 10, true);
      this.ResizeToFitY = false;
      this.visible = true;
      if (instance != null) {
         instance.shutdown();
      }

      instance = this;
      this.width = 340.0F;
      this.height = 325.0F;
      boolean var3 = true;
      boolean var4 = true;
      this.OutputLog = new UITextBox2(UIFont.Small, 5, 33, 330, 260, Translator.getText("IGUI_ServerToolBox_Status"), true);
      this.OutputLog.multipleLine = true;
      this.ScrollBarV = new ScrollBar("ServerToolboxScrollbar", this, (int)(this.OutputLog.getX() + this.OutputLog.getWidth()) - 14, this.OutputLog.getY().intValue() + 4, this.OutputLog.getHeight().intValue() - 8, true);
      this.ScrollBarV.SetParentTextBox(this.OutputLog);
      this.AddChild(this.OutputLog);
      this.AddChild(this.ScrollBarV);
      this.buttonAccept = new DialogButton(this, 30, 225, Translator.getText("IGUI_ServerToolBox_acccept"), "accept");
      this.buttonReject = new DialogButton(this, 80, 225, Translator.getText("IGUI_ServerToolBox_reject"), "reject");
      this.AddChild(this.buttonAccept);
      this.AddChild(this.buttonReject);
      this.buttonAccept.setVisible(false);
      this.buttonReject.setVisible(false);
      this.PrintLine("\n");
      if (CoopMaster.instance != null && CoopMaster.instance.isRunning()) {
         CoopMaster.instance.addListener(this);
         CoopMaster.instance.invokeServer("get-parameter", "external-ip", new ICoopServerMessageListener() {
            public void OnCoopServerMessage(String var1, String var2, String var3) {
               UIServerToolbox.this.externalAddress = var3;
               String var4 = "null".equals(UIServerToolbox.this.externalAddress) ? Translator.getText("IGUI_ServerToolBox_IPUnknown") : UIServerToolbox.this.externalAddress;
               UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_ServerAddress", var4));
               UIServerToolbox.this.PrintLine("");
               UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_AdminPanel"));
               UIServerToolbox.this.PrintLine("");
            }
         });
         if (SteamUtils.isSteamModeEnabled()) {
            CoopMaster.instance.invokeServer("get-parameter", "steam-id", new ICoopServerMessageListener() {
               public void OnCoopServerMessage(String var1, String var2, String var3) {
                  UIServerToolbox.this.steamID = var3;
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_SteamID", UIServerToolbox.this.steamID));
                  UIServerToolbox.this.PrintLine("");
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_Invite1"));
                  UIServerToolbox.this.PrintLine("");
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_Invite2"));
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_Invite3"));
                  UIServerToolbox.this.PrintLine("");
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_Invite4"));
                  UIServerToolbox.this.PrintLine("");
                  UIServerToolbox.this.PrintLine(Translator.getText("IGUI_ServerToolBox_Invite5"));
               }
            });
         }
      }

   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         this.DrawTextCentre(Translator.getText("IGUI_ServerToolBox_Title"), this.getWidth() / 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         String var1 = "null".equals(this.externalAddress) ? Translator.getText("IGUI_ServerToolBox_IPUnknown") : this.externalAddress;
         this.DrawText(Translator.getText("IGUI_ServerToolBox_ExternalIP", var1), 7.0D, 19.0D, 0.699999988079071D, 0.699999988079071D, 1.0D, 1.0D);
         if (!this.incomingConnections.isEmpty()) {
            String var2 = (String)this.incomingConnections.get(0);
            if (var2 != null) {
               this.DrawText(Translator.getText("IGUI_ServerToolBox_UserConnecting", var2), 10.0D, 205.0D, 0.699999988079071D, 0.699999988079071D, 1.0D, 1.0D);
            }
         }

      }
   }

   public void update() {
      if (this.isVisible()) {
         if (this.incomingConnections.isEmpty()) {
            this.buttonReject.setVisible(false);
            this.buttonAccept.setVisible(false);
         } else {
            this.buttonReject.setVisible(true);
            this.buttonAccept.setVisible(true);
         }

         super.update();
      }
   }

   void UpdateViewPos() {
      this.OutputLog.TopLineIndex = this.OutputLog.Lines.size() - this.OutputLog.NumVisibleLines;
      if (this.OutputLog.TopLineIndex < 0) {
         this.OutputLog.TopLineIndex = 0;
      }

      this.ScrollBarV.scrollToBottom();
   }

   public synchronized void OnCoopServerMessage(String var1, String var2, String var3) {
      if (Objects.equals(var1, "login-attempt")) {
         this.PrintLine(var3 + " is connecting");
         if (this.autoAccept) {
            this.PrintLine("Accepted connection from " + var3);
            CoopMaster.instance.sendMessage("approve-login-attempt", var3);
         } else {
            this.incomingConnections.add(var3);
            this.setVisible(true);
         }
      }

   }

   void PrintLine(String var1) {
      this.OutputLog.SetText(this.OutputLog.Text + var1 + "\n");
      this.UpdateViewPos();
   }

   public void shutdown() {
      if (CoopMaster.instance != null) {
         CoopMaster.instance.removeListener(this);
      }

   }

   public void DoubleClick(String var1, int var2, int var3) {
   }

   public void ModalClick(String var1, String var2) {
   }

   public void Selected(String var1, int var2, int var3) {
      String var4;
      if (Objects.equals(var1, "accept")) {
         var4 = (String)this.incomingConnections.get(0);
         this.incomingConnections.remove(0);
         this.PrintLine("Accepted connection from " + var4);
         CoopMaster.instance.sendMessage("approve-login-attempt", var4);
      }

      if (Objects.equals(var1, "reject")) {
         var4 = (String)this.incomingConnections.get(0);
         this.incomingConnections.remove(0);
         this.PrintLine("Rejected connection from " + var4);
         CoopMaster.instance.sendMessage("reject-login-attempt", var4);
      }

   }
}
