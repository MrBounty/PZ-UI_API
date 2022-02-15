package zombie.iso.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import zombie.core.textures.Texture;
import zombie.network.GameServer;
import zombie.network.ServerGUI;

public final class IsoAnim {
   public static final HashMap GlobalAnimMap = new HashMap();
   public short FinishUnloopedOnFrame = 0;
   public short FrameDelay = 0;
   public short LastFrame = 0;
   public final ArrayList Frames = new ArrayList(8);
   public String name;
   boolean looped = true;
   public int ID = 0;
   private static final ThreadLocal tlsStrBuf = new ThreadLocal() {
      protected StringBuilder initialValue() {
         return new StringBuilder();
      }
   };
   public IsoDirectionFrame[] FramesArray = new IsoDirectionFrame[0];

   public static void DisposeAll() {
      GlobalAnimMap.clear();
   }

   void LoadExtraFrame(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = var1 + "_";
      String var5 = "_" + var2 + "_";
      Integer var6 = new Integer(var3);
      IsoDirectionFrame var7 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + "8" + var5 + var6.toString() + ".png"), Texture.getSharedTexture(var4 + "9" + var5 + var6.toString() + ".png"), Texture.getSharedTexture(var4 + "6" + var5 + var6.toString() + ".png"), Texture.getSharedTexture(var4 + "3" + var5 + var6.toString() + ".png"), Texture.getSharedTexture(var4 + "2" + var5 + var6.toString() + ".png"));
      this.Frames.add(var7);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesReverseAltName(String var1, String var2, String var3, int var4) {
      this.name = var3;
      StringBuilder var5 = (StringBuilder)tlsStrBuf.get();
      var5.setLength(0);
      var5.append(var1);
      var5.append("_%_");
      var5.append(var2);
      var5.append("_^");
      int var6 = var5.lastIndexOf("^");
      int var7 = var5.indexOf("_%_") + 1;
      var5.setCharAt(var7, '9');
      var5.setCharAt(var6, '0');
      if (GameServer.bServer && !ServerGUI.isCreated()) {
         for(int var8 = 0; var8 < var4; ++var8) {
            this.Frames.add(new IsoDirectionFrame((Texture)null));
         }

         this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
         this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
      }

      Texture var22 = Texture.getSharedTexture(var5.toString());
      if (var22 != null) {
         for(int var9 = 0; var9 < var4; ++var9) {
            if (var9 == 10) {
               var5.setLength(0);
               var5.append(var1);
               var5.append("_1_");
               var5.append(var2);
               var5.append("_10");
            }

            Integer var10 = var9;
            IsoDirectionFrame var11 = null;
            String var12 = var10.toString();
            String var14;
            String var15;
            String var16;
            String var17;
            String var23;
            if (var22 == null) {
               var5.setCharAt(var7, '8');

               try {
                  var5.setCharAt(var6, var10.toString().charAt(0));
               } catch (Exception var21) {
                  this.LoadFramesReverseAltName(var1, var2, var3, var4);
               }

               var23 = var5.toString();
               var5.setCharAt(var7, '9');
               var14 = var5.toString();
               var5.setCharAt(var7, '6');
               var15 = var5.toString();
               var5.setCharAt(var7, '3');
               var16 = var5.toString();
               var5.setCharAt(var7, '2');
               var17 = var5.toString();
               var11 = new IsoDirectionFrame(Texture.getSharedTexture(var23), Texture.getSharedTexture(var14), Texture.getSharedTexture(var15), Texture.getSharedTexture(var16), Texture.getSharedTexture(var17));
            } else {
               var5.setCharAt(var7, '9');

               for(int var13 = 0; var13 < var12.length(); ++var13) {
                  var5.setCharAt(var6 + var13, var12.charAt(var13));
               }

               var23 = var5.toString();
               var5.setCharAt(var7, '6');
               var14 = var5.toString();
               var5.setCharAt(var7, '3');
               var15 = var5.toString();
               var5.setCharAt(var7, '2');
               var16 = var5.toString();
               var5.setCharAt(var7, '1');
               var17 = var5.toString();
               var5.setCharAt(var7, '4');
               String var18 = var5.toString();
               var5.setCharAt(var7, '7');
               String var19 = var5.toString();
               var5.setCharAt(var7, '8');
               String var20 = var5.toString();
               var11 = new IsoDirectionFrame(Texture.getSharedTexture(var23), Texture.getSharedTexture(var14), Texture.getSharedTexture(var15), Texture.getSharedTexture(var16), Texture.getSharedTexture(var17), Texture.getSharedTexture(var18), Texture.getSharedTexture(var19), Texture.getSharedTexture(var20));
            }

            this.Frames.add(0, var11);
         }

         this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
         this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
      }
   }

   public void LoadFrames(String var1, String var2, int var3) {
      this.name = var2;
      StringBuilder var4 = (StringBuilder)tlsStrBuf.get();
      var4.setLength(0);
      var4.append(var1);
      var4.append("_%_");
      var4.append(var2);
      var4.append("_^");
      int var5 = var4.indexOf("_%_") + 1;
      int var6 = var4.lastIndexOf("^");
      var4.setCharAt(var5, '9');
      var4.setCharAt(var6, '0');
      if (GameServer.bServer && !ServerGUI.isCreated()) {
         for(int var7 = 0; var7 < var3; ++var7) {
            this.Frames.add(new IsoDirectionFrame((Texture)null));
         }

         this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      }

      Texture var22 = Texture.getSharedTexture(var4.toString());
      if (var22 != null) {
         for(int var8 = 0; var8 < var3; ++var8) {
            if (var8 % 10 == 0 && var8 > 0) {
               var4.setLength(0);
               var4.append(var1);
               var4.append("_%_");
               var4.append(var2);
               var4.append("_^_");
               var5 = var4.indexOf("_%_") + 1;
               var6 = var4.lastIndexOf("^");
            }

            Integer var9 = var8;
            IsoDirectionFrame var10 = null;
            String var11 = var9.toString();
            int var12;
            String var13;
            String var14;
            String var15;
            String var16;
            String var23;
            if (var22 != null) {
               var4.setCharAt(var5, '9');

               for(var12 = 0; var12 < var11.length(); ++var12) {
                  var4.setCharAt(var6 + var12, var11.charAt(var12));
               }

               var23 = var4.toString();
               var4.setCharAt(var5, '6');
               var13 = var4.toString();
               var4.setCharAt(var5, '3');
               var14 = var4.toString();
               var4.setCharAt(var5, '2');
               var15 = var4.toString();
               var4.setCharAt(var5, '1');
               var16 = var4.toString();
               var4.setCharAt(var5, '4');
               String var17 = var4.toString();
               var4.setCharAt(var5, '7');
               String var18 = var4.toString();
               var4.setCharAt(var5, '8');
               String var19 = var4.toString();
               var10 = new IsoDirectionFrame(Texture.getSharedTexture(var23), Texture.getSharedTexture(var13), Texture.getSharedTexture(var14), Texture.getSharedTexture(var15), Texture.getSharedTexture(var16), Texture.getSharedTexture(var17), Texture.getSharedTexture(var18), Texture.getSharedTexture(var19));
            } else {
               try {
                  var4.setCharAt(var5, '8');
               } catch (Exception var21) {
                  this.LoadFrames(var1, var2, var3);
               }

               for(var12 = 0; var12 < var11.length(); ++var12) {
                  try {
                     var4.setCharAt(var6 + var12, var9.toString().charAt(var12));
                  } catch (Exception var20) {
                     this.LoadFrames(var1, var2, var3);
                  }
               }

               var23 = var4.toString();
               var4.setCharAt(var5, '9');
               var13 = var4.toString();
               var4.setCharAt(var5, '6');
               var14 = var4.toString();
               var4.setCharAt(var5, '3');
               var15 = var4.toString();
               var4.setCharAt(var5, '2');
               var16 = var4.toString();
               var10 = new IsoDirectionFrame(Texture.getSharedTexture(var23), Texture.getSharedTexture(var13), Texture.getSharedTexture(var14), Texture.getSharedTexture(var15), Texture.getSharedTexture(var16));
            }

            this.Frames.add(var10);
         }

         this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
         this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
      }
   }

   public void LoadFramesUseOtherFrame(String var1, String var2, String var3, String var4, int var5, String var6) {
      this.name = var3;
      String var7 = var4 + "_" + var2 + "_";
      String var8 = "_";
      String var9 = "";
      if (var6 != null) {
         var9 = "_" + var6;
      }

      for(int var10 = 0; var10 < 1; ++var10) {
         Integer var11 = new Integer(var5);
         IsoDirectionFrame var12 = new IsoDirectionFrame(Texture.getSharedTexture(var7 + "8" + var8 + var11.toString() + var9 + ".png"), Texture.getSharedTexture(var7 + "9" + var8 + var11.toString() + var9 + ".png"), Texture.getSharedTexture(var7 + "6" + var8 + var11.toString() + var9 + ".png"), Texture.getSharedTexture(var7 + "3" + var8 + var11.toString() + var9 + ".png"), Texture.getSharedTexture(var7 + "2" + var8 + var11.toString() + var9 + ".png"));
         this.Frames.add(var12);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesBits(String var1, String var2, String var3, int var4) {
      this.name = var3;
      String var5 = var3 + "_" + var2 + "_";
      String var6 = "_";

      for(int var7 = 0; var7 < var4; ++var7) {
         Integer var8 = new Integer(var7);
         IsoDirectionFrame var9 = new IsoDirectionFrame(Texture.getSharedTexture(var5 + "8" + var6 + var8.toString() + ".png"), Texture.getSharedTexture(var5 + "9" + var6 + var8.toString() + ".png"), Texture.getSharedTexture(var5 + "6" + var6 + var8.toString() + ".png"), Texture.getSharedTexture(var5 + "3" + var6 + var8.toString() + ".png"), Texture.getSharedTexture(var5 + "2" + var6 + var8.toString() + ".png"));
         this.Frames.add(var9);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesBits(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = var1 + "_" + var2 + "_";
      String var5 = "_";

      for(int var6 = 0; var6 < var3; ++var6) {
         Integer var7 = new Integer(var6);
         IsoDirectionFrame var8 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + "8" + var5 + var7.toString() + ".png"), Texture.getSharedTexture(var4 + "9" + var5 + var7.toString() + ".png"), Texture.getSharedTexture(var4 + "6" + var5 + var7.toString() + ".png"), Texture.getSharedTexture(var4 + "3" + var5 + var7.toString() + ".png"), Texture.getSharedTexture(var4 + "2" + var5 + var7.toString() + ".png"));
         this.Frames.add(var8);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesBitRepeatFrame(String var1, String var2, int var3) {
      this.name = var2;
      String var5 = "_";
      String var6 = "";
      Integer var8 = new Integer(var3);
      IsoDirectionFrame var9 = new IsoDirectionFrame(Texture.getSharedTexture(var2 + "8" + var5 + var8.toString() + var6 + ".png"), Texture.getSharedTexture(var2 + "9" + var5 + var8.toString() + var6 + ".png"), Texture.getSharedTexture(var2 + "6" + var5 + var8.toString() + var6 + ".png"), Texture.getSharedTexture(var2 + "3" + var5 + var8.toString() + var6 + ".png"), Texture.getSharedTexture(var2 + "2" + var5 + var8.toString() + var6 + ".png"));
      this.Frames.add(var9);
      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesBitRepeatFrame(String var1, String var2, String var3, int var4, String var5) {
      this.name = var3;
      String var6 = var3 + "_" + var2 + "_";
      String var7 = "_";
      String var8 = "";
      if (var5 != null) {
         var8 = "_" + var5;
      }

      Integer var10 = new Integer(var4);
      IsoDirectionFrame var11 = new IsoDirectionFrame(Texture.getSharedTexture(var6 + "8" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "9" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "6" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "3" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "2" + var7 + var10.toString() + var8 + ".png"));
      this.Frames.add(var11);
      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesBits(String var1, String var2, String var3, int var4, String var5) {
      this.name = var3;
      String var6 = var3 + "_" + var2 + "_";
      String var7 = "_";
      String var8 = "";
      if (var5 != null) {
         var8 = "_" + var5;
      }

      for(int var9 = 0; var9 < var4; ++var9) {
         Integer var10 = new Integer(var9);
         IsoDirectionFrame var11 = new IsoDirectionFrame(Texture.getSharedTexture(var6 + "8" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "9" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "6" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "3" + var7 + var10.toString() + var8 + ".png"), Texture.getSharedTexture(var6 + "2" + var7 + var10.toString() + var8 + ".png"));
         this.Frames.add(var11);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesPcx(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = var1 + "_";
      String var5 = "_" + var2 + "_";

      for(int var6 = 0; var6 < var3; ++var6) {
         Integer var7 = new Integer(var6);
         IsoDirectionFrame var8 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + "8" + var5 + var7.toString() + ".pcx"), Texture.getSharedTexture(var4 + "9" + var5 + var7.toString() + ".pcx"), Texture.getSharedTexture(var4 + "6" + var5 + var7.toString() + ".pcx"), Texture.getSharedTexture(var4 + "3" + var5 + var7.toString() + ".pcx"), Texture.getSharedTexture(var4 + "2" + var5 + var7.toString() + ".pcx"));
         this.Frames.add(var8);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void Dispose() {
      for(int var1 = 0; var1 < this.Frames.size(); ++var1) {
         IsoDirectionFrame var2 = (IsoDirectionFrame)this.Frames.get(var1);
         var2.SetAllDirections((Texture)null);
      }

   }

   Texture LoadFrameExplicit(String var1) {
      Texture var2 = Texture.getSharedTexture(var1);
      IsoDirectionFrame var3 = new IsoDirectionFrame(var2);
      this.Frames.add(var3);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
      return var2;
   }

   void LoadFramesNoDir(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = "media/" + var1;
      String var5 = "_" + var2 + "_";

      for(int var6 = 0; var6 < var3; ++var6) {
         Integer var7 = new Integer(var6);
         IsoDirectionFrame var8 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + var5 + var7.toString() + ".png"));
         this.Frames.add(var8);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void LoadFramesNoDirPage(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = var1;
      String var5 = "_" + var2 + "_";

      for(int var6 = 0; var6 < var3; ++var6) {
         Integer var7 = new Integer(var6);
         IsoDirectionFrame var8 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + var5 + var7.toString()));
         this.Frames.add(var8);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void LoadFramesNoDirPageDirect(String var1, String var2, int var3) {
      this.name = var2;
      String var4 = var1;
      String var5 = "_" + var2 + "_";

      for(int var6 = 0; var6 < var3; ++var6) {
         Integer var7 = new Integer(var6);
         IsoDirectionFrame var8 = new IsoDirectionFrame(Texture.getSharedTexture(var4 + var5 + var7.toString() + ".png"));
         this.Frames.add(var8);
      }

      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
   }

   void LoadFramesNoDirPage(String var1) {
      this.name = "default";
      String var2 = var1;

      for(int var3 = 0; var3 < 1; ++var3) {
         IsoDirectionFrame var4 = new IsoDirectionFrame(Texture.getSharedTexture(var2));
         this.Frames.add(var4);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   public void LoadFramesPageSimple(String var1, String var2, String var3, String var4) {
      this.name = "default";

      for(int var5 = 0; var5 < 1; ++var5) {
         new Integer(var5);
         IsoDirectionFrame var7 = new IsoDirectionFrame(Texture.getSharedTexture(var1), Texture.getSharedTexture(var2), Texture.getSharedTexture(var3), Texture.getSharedTexture(var4));
         this.Frames.add(var7);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void LoadFramesNoDirPalette(String var1, String var2, int var3, String var4) {
      this.name = var2;
      String var5 = "media/characters/" + var1;
      String var6 = "_" + var2 + "_";

      for(int var7 = 0; var7 < var3; ++var7) {
         Integer var8 = new Integer(var7);
         IsoDirectionFrame var9 = new IsoDirectionFrame(Texture.getSharedTexture(var5 + var6 + var8.toString() + ".pcx", var4));
         this.Frames.add(var9);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void LoadFramesPalette(String var1, String var2, int var3, String var4) {
      this.name = var2;
      String var5 = var1 + "_";
      String var6 = "_" + var2 + "_";

      for(int var7 = 0; var7 < var3; ++var7) {
         Integer var8 = new Integer(var7);
         IsoDirectionFrame var9 = new IsoDirectionFrame(Texture.getSharedTexture(var5 + "8" + var6 + var8.toString() + "_" + var4), Texture.getSharedTexture(var5 + "9" + var6 + var8.toString() + "_" + var4), Texture.getSharedTexture(var5 + "6" + var6 + var8.toString() + "_" + var4), Texture.getSharedTexture(var5 + "3" + var6 + var8.toString() + "_" + var4), Texture.getSharedTexture(var5 + "2" + var6 + var8.toString() + "_" + var4));
         this.Frames.add(var9);
      }

      this.FinishUnloopedOnFrame = (short)(this.Frames.size() - 1);
      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }

   void DupeFrame() {
      for(int var1 = 0; var1 < 8; ++var1) {
         IsoDirectionFrame var2 = new IsoDirectionFrame();
         var2.directions[var1] = ((IsoDirectionFrame)this.Frames.get(0)).directions[var1];
         var2.bDoFlip = ((IsoDirectionFrame)this.Frames.get(0)).bDoFlip;
         this.Frames.add(var2);
      }

      this.FramesArray = (IsoDirectionFrame[])this.Frames.toArray(this.FramesArray);
   }
}
