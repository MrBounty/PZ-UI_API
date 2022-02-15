package zombie.chat;

import java.util.ArrayList;
import java.util.HashSet;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.characters.Talker;
import zombie.iso.objects.IsoRadio;
import zombie.iso.objects.IsoTelevision;
import zombie.network.GameServer;
import zombie.radio.ZomboidRadio;
import zombie.ui.TextDrawObject;
import zombie.ui.UIFont;
import zombie.vehicles.VehiclePart;

public class ChatElement implements Talker {
   protected ChatElement.PlayerLines[] playerLines = new ChatElement.PlayerLines[4];
   protected ChatElementOwner owner;
   protected float historyVal = 1.0F;
   protected boolean historyInRange = false;
   protected float historyRange = 15.0F;
   protected boolean useEuclidean = true;
   protected boolean hasChatToDisplay = false;
   protected int maxChatLines = -1;
   protected int maxCharsPerLine = -1;
   protected String sayLine = null;
   protected String sayLineTag = null;
   protected TextDrawObject sayLineObject = null;
   protected boolean Speaking = false;
   protected String talkerType = "unknown";
   public static boolean doBackDrop = true;
   public static NineGridTexture backdropTexture;
   private int bufferX = 0;
   private int bufferY = 0;
   private static ChatElement.PlayerLinesList[] renderBatch = new ChatElement.PlayerLinesList[4];
   private static HashSet noLogText = new HashSet();

   public ChatElement(ChatElementOwner var1, int var2, String var3) {
      this.owner = var1;
      this.setMaxChatLines(var2);
      this.setMaxCharsPerLine(75);
      this.talkerType = var3 != null ? var3 : this.talkerType;
      if (backdropTexture == null) {
         backdropTexture = new NineGridTexture("NineGridBlack", 5);
      }

   }

   public void setMaxChatLines(int var1) {
      var1 = var1 < 1 ? 1 : (var1 > 10 ? 10 : var1);
      if (var1 != this.maxChatLines) {
         this.maxChatLines = var1;

         for(int var2 = 0; var2 < this.playerLines.length; ++var2) {
            this.playerLines[var2] = new ChatElement.PlayerLines(this.maxChatLines);
         }
      }

   }

   public int getMaxChatLines() {
      return this.maxChatLines;
   }

   public void setMaxCharsPerLine(int var1) {
      for(int var2 = 0; var2 < this.playerLines.length; ++var2) {
         this.playerLines[var2].setMaxCharsPerLine(var1);
      }

      this.maxCharsPerLine = var1;
   }

   public boolean IsSpeaking() {
      return this.Speaking;
   }

   public String getTalkerType() {
      return this.talkerType;
   }

   public void setTalkerType(String var1) {
      this.talkerType = var1 == null ? "" : var1;
   }

   public String getSayLine() {
      return this.sayLine;
   }

   public String getSayLineTag() {
      return this.Speaking && this.sayLineTag != null ? this.sayLineTag : "";
   }

   public void setHistoryRange(float var1) {
      this.historyRange = var1;
   }

   public void setUseEuclidean(boolean var1) {
      this.useEuclidean = var1;
   }

   public boolean getHasChatToDisplay() {
      return this.hasChatToDisplay;
   }

   protected float getDistance(IsoPlayer var1) {
      if (var1 == null) {
         return -1.0F;
      } else {
         return this.useEuclidean ? (float)Math.sqrt(Math.pow((double)(this.owner.getX() - var1.x), 2.0D) + Math.pow((double)(this.owner.getY() - var1.y), 2.0D)) : Math.abs(this.owner.getX() - var1.x) + Math.abs(this.owner.getY() - var1.y);
      }
   }

   protected boolean playerWithinBounds(IsoPlayer var1, float var2) {
      if (var1 == null) {
         return false;
      } else {
         return var1.getX() > this.owner.getX() - var2 && var1.getX() < this.owner.getX() + var2 && var1.getY() > this.owner.getY() - var2 && var1.getY() < this.owner.getY() + var2;
      }
   }

   public void SayDebug(int var1, String var2) {
      if (!GameServer.bServer && var1 >= 0 && var1 < this.maxChatLines) {
         for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            IsoPlayer var4 = IsoPlayer.players[var3];
            if (var4 != null) {
               ChatElement.PlayerLines var5 = this.playerLines[var3];
               if (var1 < var5.chatLines.length) {
                  if (var5.chatLines[var1].getOriginal() != null && var5.chatLines[var1].getOriginal().equals(var2)) {
                     var5.chatLines[var1].setInternalTickClock((float)var5.lineDisplayTime);
                  } else {
                     var5.chatLines[var1].setSettings(true, true, true, true, true, true);
                     var5.chatLines[var1].setInternalTickClock((float)var5.lineDisplayTime);
                     var5.chatLines[var1].setCustomTag("default");
                     var5.chatLines[var1].setDefaultColors(1.0F, 1.0F, 1.0F, 1.0F);
                     var5.chatLines[var1].ReadString(UIFont.Medium, var2, this.maxCharsPerLine);
                  }
               }
            }
         }

         this.sayLine = var2;
         this.sayLineTag = "default";
         this.hasChatToDisplay = true;
      }

   }

   public void Say(String var1) {
      this.addChatLine(var1, 1.0F, 1.0F, 1.0F, UIFont.Dialogue, 25.0F, "default", false, false, false, false, false, true);
   }

   public void addChatLine(String var1, float var2, float var3, float var4, float var5) {
      this.addChatLine(var1, var2, var3, var4, UIFont.Dialogue, var5, "default", false, false, false, false, false, true);
   }

   public void addChatLine(String var1, float var2, float var3, float var4) {
      this.addChatLine(var1, var2, var3, var4, UIFont.Dialogue, 25.0F, "default", false, false, false, false, false, true);
   }

   public void addChatLine(String var1, float var2, float var3, float var4, UIFont var5, float var6, String var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13) {
      if (!GameServer.bServer) {
         for(int var14 = 0; var14 < IsoPlayer.numPlayers; ++var14) {
            IsoPlayer var15 = IsoPlayer.players[var14];
            if (var15 != null) {
               if (var15.Traits.Deaf.isSet()) {
                  if (this.owner instanceof IsoTelevision) {
                     if (!((IsoTelevision)this.owner).isFacing(var15)) {
                        continue;
                     }
                  } else if (this.owner instanceof IsoRadio || this.owner instanceof VehiclePart) {
                     continue;
                  }
               }

               float var16 = this.getScrambleValue(var15, var6);
               if (var16 < 1.0F) {
                  ChatElement.PlayerLines var17 = this.playerLines[var14];
                  TextDrawObject var18 = var17.getNewLineObject();
                  if (var18 != null) {
                     var18.setSettings(var8, var9, var10, var11, var12, var13);
                     var18.setInternalTickClock((float)var17.lineDisplayTime);
                     var18.setCustomTag(var7);
                     String var19;
                     if (var16 > 0.0F) {
                        var19 = ZomboidRadio.getInstance().scrambleString(var1, (int)(100.0F * var16), true, "...");
                        var18.setDefaultColors(0.5F, 0.5F, 0.5F, 1.0F);
                     } else {
                        var19 = var1;
                        var18.setDefaultColors(var2, var3, var4, 1.0F);
                     }

                     var18.ReadString(var5, var19, this.maxCharsPerLine);
                     this.sayLine = var1;
                     this.sayLineTag = var7;
                     this.hasChatToDisplay = true;
                  }
               }
            }
         }
      }

   }

   protected float getScrambleValue(IsoPlayer var1, float var2) {
      if (this.owner == var1) {
         return 0.0F;
      } else {
         float var3 = 1.0F;
         boolean var4 = false;
         boolean var5 = false;
         if (this.owner.getSquare() != null && var1.getSquare() != null) {
            if (var1.getBuilding() != null && this.owner.getSquare().getBuilding() != null && var1.getBuilding() == this.owner.getSquare().getBuilding()) {
               if (var1.getSquare().getRoom() == this.owner.getSquare().getRoom()) {
                  var3 = (float)((double)var3 * 2.0D);
                  var5 = true;
               } else if (Math.abs(var1.getZ() - this.owner.getZ()) < 1.0F) {
                  var3 = (float)((double)var3 * 2.0D);
               }
            } else if (var1.getBuilding() != null || this.owner.getSquare().getBuilding() != null) {
               var3 = (float)((double)var3 * 0.5D);
               var4 = true;
            }

            if (Math.abs(var1.getZ() - this.owner.getZ()) >= 1.0F) {
               var3 = (float)((double)var3 - (double)var3 * (double)Math.abs(var1.getZ() - this.owner.getZ()) * 0.25D);
               var4 = true;
            }
         }

         float var6 = var2 * var3;
         float var7 = 1.0F;
         if (var3 > 0.0F && this.playerWithinBounds(var1, var6)) {
            float var8 = this.getDistance(var1);
            if (var8 >= 0.0F && var8 < var6) {
               float var9 = var6 * 0.6F;
               if (var5 || !var4 && var8 < var9) {
                  var7 = 0.0F;
               } else if (var6 - var9 != 0.0F) {
                  var7 = (var8 - var9) / (var6 - var9);
                  if (var7 < 0.2F) {
                     var7 = 0.2F;
                  }
               }
            }
         }

         return var7;
      }
   }

   protected void updateChatLines() {
      this.Speaking = false;
      boolean var1 = false;
      if (this.hasChatToDisplay) {
         this.hasChatToDisplay = false;

         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            float var3 = 1.25F * GameTime.getInstance().getMultiplier();
            int var6 = this.playerLines[var2].lineDisplayTime;
            TextDrawObject[] var7 = this.playerLines[var2].chatLines;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               TextDrawObject var10 = var7[var9];
               float var4 = var10.updateInternalTickClock(var3);
               if (!(var4 <= 0.0F)) {
                  this.hasChatToDisplay = true;
                  if (!var1 && !var10.getCustomTag().equals("radio")) {
                     float var5 = var4 / ((float)var6 / 2.0F);
                     if (var5 >= 1.0F) {
                        this.Speaking = true;
                     }

                     var1 = true;
                  }

                  var3 *= 1.2F;
               }
            }
         }
      }

      if (!this.Speaking) {
         this.sayLine = null;
         this.sayLineTag = null;
      }

   }

   protected void updateHistory() {
      if (this.hasChatToDisplay) {
         this.historyInRange = false;
         IsoPlayer var1 = IsoPlayer.getInstance();
         if (var1 != null) {
            if (var1 == this.owner) {
               this.historyVal = 1.0F;
            } else {
               if (this.playerWithinBounds(var1, this.historyRange)) {
                  this.historyInRange = true;
               } else {
                  this.historyInRange = false;
               }

               if (this.historyInRange && this.historyVal != 1.0F) {
                  this.historyVal += 0.04F;
                  if (this.historyVal > 1.0F) {
                     this.historyVal = 1.0F;
                  }
               }

               if (!this.historyInRange && this.historyVal != 0.0F) {
                  this.historyVal -= 0.04F;
                  if (this.historyVal < 0.0F) {
                     this.historyVal = 0.0F;
                  }
               }
            }
         }
      } else if (this.historyVal != 0.0F) {
         this.historyVal = 0.0F;
      }

   }

   public void update() {
      if (!GameServer.bServer) {
         this.updateChatLines();
         this.updateHistory();
      }
   }

   public void renderBatched(int var1, int var2, int var3) {
      this.renderBatched(var1, var2, var3, false);
   }

   public void renderBatched(int var1, int var2, int var3, boolean var4) {
      if (var1 < this.playerLines.length && this.hasChatToDisplay && !GameServer.bServer) {
         this.playerLines[var1].renderX = var2;
         this.playerLines[var1].renderY = var3;
         this.playerLines[var1].ignoreRadioLines = var4;
         if (renderBatch[var1] == null) {
            renderBatch[var1] = new ChatElement.PlayerLinesList();
         }

         renderBatch[var1].add(this.playerLines[var1]);
      }

   }

   public void clear(int var1) {
      this.playerLines[var1].clear();
   }

   public static void RenderBatch(int var0) {
      if (renderBatch[var0] != null && renderBatch[var0].size() > 0) {
         for(int var1 = 0; var1 < renderBatch[var0].size(); ++var1) {
            ChatElement.PlayerLines var2 = (ChatElement.PlayerLines)renderBatch[var0].get(var1);
            var2.render();
         }

         renderBatch[var0].clear();
      }

   }

   public static void NoRender(int var0) {
      if (renderBatch[var0] != null) {
         renderBatch[var0].clear();
      }

   }

   public static void addNoLogText(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         noLogText.add(var0);
      }
   }

   class PlayerLines {
      protected int lineDisplayTime = 314;
      protected int renderX = 0;
      protected int renderY = 0;
      protected boolean ignoreRadioLines = false;
      protected TextDrawObject[] chatLines;

      public PlayerLines(int var2) {
         this.chatLines = new TextDrawObject[var2];

         for(int var3 = 0; var3 < this.chatLines.length; ++var3) {
            this.chatLines[var3] = new TextDrawObject(0, 0, 0, true, true, true, true, true, true);
            this.chatLines[var3].setDefaultFont(UIFont.Medium);
         }

      }

      public void setMaxCharsPerLine(int var1) {
         for(int var2 = 0; var2 < this.chatLines.length; ++var2) {
            this.chatLines[var2].setMaxCharsPerLine(var1);
         }

      }

      public TextDrawObject getNewLineObject() {
         if (this.chatLines != null && this.chatLines.length > 0) {
            TextDrawObject var1 = this.chatLines[this.chatLines.length - 1];
            var1.Clear();

            for(int var2 = this.chatLines.length - 1; var2 > 0; --var2) {
               this.chatLines[var2] = this.chatLines[var2 - 1];
            }

            this.chatLines[0] = var1;
            return this.chatLines[0];
         } else {
            return null;
         }
      }

      public void render() {
         if (!GameServer.bServer) {
            if (ChatElement.this.hasChatToDisplay) {
               int var3 = 0;
               TextDrawObject[] var4 = this.chatLines;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TextDrawObject var7 = var4[var6];
                  if (var7.getEnabled()) {
                     if (var7.getWidth() > 0 && var7.getHeight() > 0) {
                        float var1 = var7.getInternalClock();
                        if (!(var1 <= 0.0F) && (!var7.getCustomTag().equals("radio") || !this.ignoreRadioLines)) {
                           float var2 = var1 / ((float)this.lineDisplayTime / 4.0F);
                           if (var2 > 1.0F) {
                              var2 = 1.0F;
                           }

                           this.renderY -= var7.getHeight() + 1;
                           boolean var8 = var7.getDefaultFontEnum() != UIFont.Dialogue;
                           if (ChatElement.doBackDrop && ChatElement.backdropTexture != null) {
                              ChatElement.backdropTexture.renderInnerBased(this.renderX - var7.getWidth() / 2, this.renderY, var7.getWidth(), var7.getHeight(), 0.0F, 0.0F, 0.0F, 0.4F);
                           }

                           if (var3 == 0) {
                              var7.Draw((double)this.renderX, (double)this.renderY, var8, var2);
                           } else if (ChatElement.this.historyVal > 0.0F) {
                              var7.Draw((double)this.renderX, (double)this.renderY, var8, var2 * ChatElement.this.historyVal);
                           }

                           ++var3;
                        }
                     } else {
                        ++var3;
                     }
                  }
               }
            }

         }
      }

      void clear() {
         if (ChatElement.this.hasChatToDisplay) {
            ChatElement.this.hasChatToDisplay = false;

            for(int var1 = 0; var1 < this.chatLines.length; ++var1) {
               if (!(this.chatLines[var1].getInternalClock() <= 0.0F)) {
                  this.chatLines[var1].Clear();
                  this.chatLines[var1].updateInternalTickClock(this.chatLines[var1].getInternalClock());
               }
            }

            ChatElement.this.historyInRange = false;
            ChatElement.this.historyVal = 0.0F;
         }
      }
   }

   class PlayerLinesList extends ArrayList {
   }
}
