package zombie.ui;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.KahluaException;
import se.krka.kahlua.vm.LuaClosure;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.core.Core;
import zombie.debug.DebugOptions;

public final class UIDebugConsole extends NewWindow {
   public static UIDebugConsole instance;
   IsoGameCharacter ParentChar;
   ScrollBar ScrollBarV;
   UITextBox2 OutputLog;
   public UITextBox2 CommandLine;
   UITextBox2 autosuggest;
   String ConsoleVersion = "v1.1.0";
   int inputlength = 0;
   private final ArrayList Previous = new ArrayList();
   private final ArrayList globalLuaMethods = new ArrayList();
   public int PreviousIndex = 0;
   Method prevSuggestion = null;
   String[] AvailableCommands = new String[]{"?", "help", "commands", "clr", "AddInvItem", "SpawnZombie"};
   String[] AvailableCommandsHelp = new String[]{"'?' - Shows available commands", "'help' - Shows available commands", "'commands' - Shows available commands", "'clr' - Clears the command log", "'AddInvItem' - Adds an item to player inventory. USAGE - AddInvItem 'ItemName' [ammount]", "'SpawnZombie' - Spawn a zombie at a map location. USAGE - SpawnZombie X,Y,Z (integers)"};
   public boolean bDebounceUp = false;
   public boolean bDebounceDown = false;
   private static final Object outputLock = "DebugConsole Output Lock";
   private static final ByteBuffer outputBB = ByteBuffer.allocate(8192);
   private static boolean outputChanged = false;
   private static CharsetDecoder outputDecoder;
   private static char[] outputChars;
   private static CharBuffer outputCharBuf = null;

   public UIDebugConsole(int var1, int var2) {
      super(var1, var2, 10, 10, true);
      this.ResizeToFitY = false;
      this.visible = true;
      instance = this;
      this.width = 640.0F;
      int var3 = TextManager.instance.getFontHeight(UIFont.DebugConsole);
      byte var4 = 11;
      byte var5 = 5;
      this.OutputLog = new UITextBox2(UIFont.DebugConsole, 5, 33, 630, var3 * var4 + var5 * 2, "Project Zomboid - " + Core.getInstance().getVersionNumber() + "\nDebug Console - " + this.ConsoleVersion + "\n(C) Indie Stone Studios 2021\n---------------------------------------------------------------------------------------------------------------------------\n\n", true);
      this.OutputLog.multipleLine = true;
      this.OutputLog.bAlwaysPaginate = false;
      this.CommandLine = new UIDebugConsole.CommandEntry(UIFont.DebugConsole, 5, (int)(this.OutputLog.getY() + this.OutputLog.getHeight()) + 15, 630, 24, "", true);
      this.CommandLine.IsEditable = true;
      this.CommandLine.TextEntryMaxLength = 256;
      this.autosuggest = new UITextBox2(UIFont.DebugConsole, 5, 180, 15, 25, "", true);
      this.height = (float)((int)(this.CommandLine.getY() + this.CommandLine.getHeight()) + 6);
      this.ScrollBarV = new ScrollBar("UIDebugConsoleScrollbar", (UIEventHandler)null, (int)(this.OutputLog.getX() + this.OutputLog.getWidth()) - 14, this.OutputLog.getY().intValue() + 4, this.OutputLog.getHeight().intValue() - 8, true);
      this.ScrollBarV.SetParentTextBox(this.OutputLog);
      this.AddChild(this.OutputLog);
      this.AddChild(this.ScrollBarV);
      this.AddChild(this.CommandLine);
      this.AddChild(this.autosuggest);
      this.InitSuggestionEngine();
      if (Core.bDebug) {
         BaseLib.setPrintCallback(this::SpoolText);
      }

   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         this.DrawTextCentre(UIFont.DebugConsole, "Command Console", this.getWidth() / 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         this.DrawText(UIFont.DebugConsole, "Output Log", 7.0D, 19.0D, 0.699999988079071D, 0.699999988079071D, 1.0D, 1.0D);
         this.DrawText(UIFont.DebugConsole, "Lua Command Line", 7.0D, this.OutputLog.getY() + this.OutputLog.getHeight() + 1.0D, 0.699999988079071D, 0.699999988079071D, 1.0D, 1.0D);
      }
   }

   public void update() {
      if (this.isVisible()) {
         this.handleOutput();
         super.update();
         if (this.CommandLine.getText().length() != this.inputlength && this.CommandLine.getText().length() != 0) {
            this.inputlength = this.CommandLine.getText().length();
            String[] var1 = this.CommandLine.getText().split(":");
            String var2 = "";
            if (var1.length > 0) {
               var2 = var1[var1.length - 1];
               if (var1[var1.length - 1].isEmpty() && this.autosuggest.isVisible()) {
                  this.autosuggest.setVisible(false);
                  return;
               }
            }

            Method var3 = null;
            if (var1.length > 1 && var1[0].indexOf(")") > 0 && !var1[var1.length - 1].contains("(")) {
               ArrayList var5 = new ArrayList(this.globalLuaMethods);
               int var6 = 0;

               while(true) {
                  if (var6 >= var1.length) {
                     var3 = this.SuggestionEngine(var2, var5);
                     break;
                  }

                  String var7 = var1[var6];
                  if (var7.indexOf(")") > 0) {
                     var7 = var7.split("\\(", 0)[0];
                     Iterator var8 = var5.iterator();

                     label78:
                     while(var8.hasNext()) {
                        Method var9 = (Method)var8.next();
                        if (var9.getName().equals(var7)) {
                           var5.clear();
                           Class var10 = var9.getReturnType();

                           while(true) {
                              if (var10 == null) {
                                 break label78;
                              }

                              Method[] var11 = var10.getDeclaredMethods();
                              int var12 = var11.length;

                              for(int var13 = 0; var13 < var12; ++var13) {
                                 Method var14 = var11[var13];
                                 if (Modifier.isPublic(var14.getModifiers())) {
                                    var5.add(var14);
                                 }
                              }

                              var10 = var10.getSuperclass();
                           }
                        }
                     }
                  }

                  ++var6;
               }
            } else if (var1.length == 1) {
               var3 = this.SuggestionEngine(var2);
            }

            String var4 = "void";
            if (var3 != null) {
               if (!var3.getReturnType().toString().equals("void")) {
                  String[] var15 = var3.getReturnType().toString().split("\\.");
                  var4 = var15[var15.length - 1];
               }

               if (!this.autosuggest.isVisible()) {
                  this.autosuggest.setVisible(true);
               }

               this.autosuggest.SetText("<" + var4 + "> " + var3.getName());
               this.autosuggest.setX((double)(5 * this.CommandLine.getText().length()));
               this.autosuggest.setWidth((double)(15 * (var4.length() + var3.getName().length())));
               this.autosuggest.Frame.width = (float)(10 * (var4.length() + var3.getName().length()));
            }
         } else if (this.CommandLine.getText().length() == 0 && this.autosuggest.isVisible()) {
            this.autosuggest.setVisible(false);
         }

      }
   }

   public void ProcessCommand() {
      if (this.CommandLine.internalText != null) {
         String var1 = this.CommandLine.internalText;
         this.CommandLine.internalText = "";
         var1 = var1.trim();
         String[] var2 = var1.split(" ");
         var2[0] = var2[0].trim();
         if (this.Previous.isEmpty() || !var1.equals(this.Previous.get(this.Previous.size() - 1))) {
            this.Previous.add(var1);
         }

         this.PreviousIndex = this.Previous.size();
         this.CommandLine.DoingTextEntry = true;
         Core.CurrentTextEntryBox = this.CommandLine;
         if ("clear".equals(var1)) {
            this.OutputLog.bTextChanged = true;
            this.OutputLog.clearInput();
         } else {
            if (DebugOptions.instance.UIDebugConsoleEchoCommand.getValue()) {
               this.SpoolText("[USER] - \"" + var1 + "\".");
            }

            try {
               LuaClosure var3 = LuaCompiler.loadstring(var1, "console", LuaManager.env);
               LuaReturn var4 = LuaManager.caller.protectedCall(LuaManager.thread, var3);
            } catch (KahluaException var5) {
               this.SpoolText(var5.getMessage());
            } catch (Exception var6) {
               Logger.getLogger(UIDebugConsole.class.getName()).log(Level.SEVERE, (String)null, var6);
            }

         }
      }
   }

   void historyPrev() {
      --this.PreviousIndex;
      if (this.PreviousIndex < 0) {
         this.PreviousIndex = 0;
      }

      if (this.PreviousIndex >= 0 && this.PreviousIndex < this.Previous.size()) {
         this.CommandLine.SetText((String)this.Previous.get(this.PreviousIndex));
      }

   }

   void historyNext() {
      ++this.PreviousIndex;
      if (this.PreviousIndex >= this.Previous.size()) {
         this.PreviousIndex = this.Previous.size() - 1;
      }

      if (this.PreviousIndex >= 0 && this.PreviousIndex < this.Previous.size()) {
         this.CommandLine.SetText((String)this.Previous.get(this.PreviousIndex));
      }

   }

   public void onOtherKey(int var1) {
      switch(var1) {
      case 15:
         if (this.prevSuggestion != null) {
            String[] var2 = this.CommandLine.getText().split(":");
            StringBuilder var3 = new StringBuilder();
            if (var2.length > 0) {
               var2[var2.length - 1] = this.prevSuggestion.getName();

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  var3.append(var2[var4]);
                  if (var4 != var2.length - 1) {
                     var3.append(":");
                  }
               }
            }

            if (this.prevSuggestion.getParameterTypes().length == 0) {
               this.CommandLine.SetText(var3 + "()");
            } else {
               this.CommandLine.SetText(var3 + "(");
            }
         }
      default:
      }
   }

   void ClearConsole() {
      this.OutputLog.bTextChanged = true;
      this.OutputLog.SetText("");
      this.UpdateViewPos();
   }

   void UpdateViewPos() {
      this.OutputLog.TopLineIndex = this.OutputLog.Lines.size() - this.OutputLog.NumVisibleLines;
      if (this.OutputLog.TopLineIndex < 0) {
         this.OutputLog.TopLineIndex = 0;
      }

      this.ScrollBarV.scrollToBottom();
   }

   void SpoolText(String var1) {
      this.OutputLog.bTextChanged = true;
      this.OutputLog.SetText(this.OutputLog.Text + var1 + "\n");
      this.UpdateViewPos();
   }

   Method SuggestionEngine(String var1) {
      return this.SuggestionEngine(var1, this.globalLuaMethods);
   }

   Method SuggestionEngine(String var1, ArrayList var2) {
      int var3 = 0;
      boolean var4 = false;
      Method var5 = null;
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         Method var7 = (Method)var6.next();
         if (var5 == null) {
            var5 = var7;
            var3 = this.levenshteinDistance(var1, var7.getName());
         } else {
            int var8 = this.levenshteinDistance(var1, var7.getName());
            if (var8 < var3) {
               var3 = var8;
               var5 = var7;
            }
         }
      }

      this.prevSuggestion = var5;
      return var5;
   }

   void InitSuggestionEngine() {
      Class var1 = LuaManager.GlobalObject.class;
      this.globalLuaMethods.addAll(Arrays.asList(var1.getDeclaredMethods()));
   }

   public int levenshteinDistance(CharSequence var1, CharSequence var2) {
      int var3 = var1.length() + 1;
      int var4 = var2.length() + 1;
      int[] var5 = new int[var3];
      int[] var6 = new int[var3];

      int var7;
      for(var7 = 0; var7 < var3; var5[var7] = var7++) {
      }

      for(var7 = 1; var7 < var4; ++var7) {
         var6[0] = var7;

         for(int var8 = 1; var8 < var3; ++var8) {
            int var9 = var1.charAt(var8 - 1) == var2.charAt(var7 - 1) ? 0 : 1;
            int var10 = var5[var8 - 1] + var9;
            int var11 = var5[var8] + 1;
            int var12 = var6[var8 - 1] + 1;
            var6[var8] = Math.min(Math.min(var11, var12), var10);
         }

         int[] var13 = var5;
         var5 = var6;
         var6 = var13;
      }

      return var5[var3 - 1];
   }

   void setSuggestWidth(int var1) {
      this.autosuggest.setWidth((double)var1);
      this.autosuggest.Frame.width = (float)var1;
   }

   public void addOutput(byte[] var1, int var2, int var3) {
      if (var3 >= 1) {
         synchronized(outputLock) {
            int var5 = var3 - outputBB.capacity();
            if (var5 > 0) {
               var2 += var5;
               var3 -= var5;
            }

            if (outputBB.position() + var3 > outputBB.capacity()) {
               outputBB.clear();
            }

            outputBB.put(var1, var2, var3);
            if (var1[var2 + var3 - 1] == 10) {
               outputChanged = true;
            }

         }
      }
   }

   private void handleOutput() {
      synchronized(outputLock) {
         if (outputChanged) {
            outputChanged = false;

            try {
               if (outputDecoder == null) {
                  outputDecoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
               }

               outputDecoder.reset();
               int var2 = outputBB.position();
               outputBB.flip();
               int var3 = (int)((double)var2 * (double)outputDecoder.maxCharsPerByte());
               if (outputChars == null || outputChars.length < var3) {
                  int var4 = (var3 + 128 - 1) / 128 * 128;
                  outputChars = new char[var4];
                  outputCharBuf = CharBuffer.wrap(outputChars);
               }

               outputCharBuf.clear();
               CoderResult var11 = outputDecoder.decode(outputBB, outputCharBuf, true);
               outputBB.clear();
               String var5 = new String(outputChars, 0, outputCharBuf.position());
               this.OutputLog.bTextChanged = true;
               this.OutputLog.SetText(this.OutputLog.Text + var5);
               short var6 = 8192;
               if (this.OutputLog.Text.length() > var6) {
                  int var7;
                  for(var7 = this.OutputLog.Text.length() - var6; var7 < this.OutputLog.Text.length() && this.OutputLog.Text.charAt(var7) != '\n'; ++var7) {
                  }

                  this.OutputLog.bTextChanged = true;
                  this.OutputLog.SetText(this.OutputLog.Text.substring(var7 + 1));
               }
            } catch (Exception var9) {
            }

            this.UpdateViewPos();
         }

      }
   }

   private class CommandEntry extends UITextBox2 {
      public CommandEntry(UIFont var2, int var3, int var4, int var5, int var6, String var7, boolean var8) {
         super(var2, var3, var4, var5, var6, var7, var8);
      }

      public void onPressUp() {
         UIDebugConsole.this.historyPrev();
      }

      public void onPressDown() {
         UIDebugConsole.this.historyNext();
      }

      public void onOtherKey(int var1) {
         UIDebugConsole.this.onOtherKey(var1);
      }
   }
}
