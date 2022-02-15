package zombie.core.raknet;

import fmod.FMODSoundBuffer;
import fmod.SoundBuffer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VoiceDebug extends JPanel {
   private static final int PREF_W = 400;
   private static final int PREF_H = 200;
   private static final int BORDER_GAP = 30;
   private static final Color LINE_CURRENT_COLOR;
   private static final Color LINE_LAST_COLOR;
   private static final Color GRAPH_COLOR;
   private static final Color GRAPH_POINT_COLOR;
   private static final Stroke GRAPH_STROKE;
   private static final int GRAPH_POINT_WIDTH = 12;
   private static final int Y_HATCH_CNT = 10;
   public List scores;
   public int scores_max;
   public String title;
   public int psize;
   public int last;
   public int current;
   private static VoiceDebug mainPanel;
   private static VoiceDebug mainPanel2;
   private static VoiceDebug mainPanel3;
   private static VoiceDebug mainPanel4;
   private static JFrame frame;

   public VoiceDebug(List var1, String var2) {
      this.scores = var1;
      this.title = var2;
      this.psize = var1.size();
      this.last = 5;
      this.current = 8;
      this.scores_max = 100;
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      Graphics2D var2 = (Graphics2D)var1;
      var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      double var3 = ((double)this.getWidth() - 60.0D) / (double)(this.scores.size() - 1);
      double var5 = ((double)this.getHeight() - 60.0D) / (double)(this.scores_max - 1);
      int var7 = (int)(((double)this.getHeight() - 60.0D) / 2.0D);
      int var8 = (int)(1.0D / var3);
      if (var8 == 0) {
         var8 = 1;
      }

      ArrayList var9 = new ArrayList();

      int var10;
      int var11;
      int var12;
      for(var10 = 0; var10 < this.scores.size(); var10 += var8) {
         var11 = (int)((double)var10 * var3 + 30.0D);
         var12 = (int)((double)(this.scores_max - (Integer)this.scores.get(var10)) * var5 + 30.0D - (double)var7);
         var9.add(new Point(var11, var12));
      }

      var2.setColor(Color.black);
      var2.drawLine(30, this.getHeight() - 30, 30, 30);
      var2.drawLine(30, this.getHeight() - 30, this.getWidth() - 30, this.getHeight() - 30);

      int var13;
      for(var10 = 0; var10 < 10; ++var10) {
         byte var17 = 30;
         byte var18 = 42;
         var13 = this.getHeight() - ((var10 + 1) * (this.getHeight() - 60) / 10 + 30);
         var2.drawLine(var17, var13, var18, var13);
      }

      Stroke var16 = var2.getStroke();
      var2.setColor(GRAPH_COLOR);
      var2.setStroke(GRAPH_STROKE);

      int var14;
      for(var11 = 0; var11 < var9.size() - 1; ++var11) {
         var12 = ((Point)var9.get(var11)).x;
         var13 = ((Point)var9.get(var11)).y;
         var14 = ((Point)var9.get(var11 + 1)).x;
         int var15 = ((Point)var9.get(var11 + 1)).y;
         var2.drawLine(var12, var13, var14, var15);
      }

      double var19 = ((double)this.getWidth() - 60.0D) / (double)(this.psize - 1);
      var2.setColor(LINE_CURRENT_COLOR);
      var13 = (int)((double)this.current * var19 + 30.0D);
      var2.drawLine(var13, this.getHeight() - 30, var13, 30);
      var2.drawString("Current", var13, this.getHeight() - 30);
      var2.setColor(LINE_LAST_COLOR);
      var14 = (int)((double)this.last * var19 + 30.0D);
      var2.drawLine(var14, this.getHeight() - 30, var14, 30);
      var2.drawString("Last", var14, this.getHeight() - 30);
      var2.setColor(Color.black);
      var2.drawString(this.title, this.getWidth() / 2, 15);
      var2.drawString("Size: " + this.scores.size(), 30, 15);
      var2.drawString("Current/Write: " + this.current, 30, 30);
      var2.drawString("Last/Read: " + this.last, 30, 45);
   }

   public Dimension getPreferredSize() {
      return new Dimension(400, 200);
   }

   public static void createAndShowGui() {
      ArrayList var0 = new ArrayList();
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      mainPanel = new VoiceDebug(var0, "SoundBuffer");
      mainPanel.scores_max = 32000;
      mainPanel2 = new VoiceDebug(var1, "SoundBuffer - first 100 sample");
      mainPanel2.scores_max = 32000;
      mainPanel3 = new VoiceDebug(var2, "FMODSoundBuffer");
      mainPanel3.scores_max = 32000;
      mainPanel4 = new VoiceDebug(var3, "FMODSoundBuffer - first 100 sample");
      mainPanel4.scores_max = 32000;
      frame = new JFrame("DrawGraph");
      frame.setDefaultCloseOperation(3);
      frame.setLayout(new GridLayout(2, 2));
      frame.getContentPane().add(mainPanel);
      frame.getContentPane().add(mainPanel2);
      frame.getContentPane().add(mainPanel3);
      frame.getContentPane().add(mainPanel4);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void updateGui(SoundBuffer var0, FMODSoundBuffer var1) {
      mainPanel.scores.clear();
      int var2;
      if (var0 != null) {
         for(var2 = 0; var2 < var0.buf().length; ++var2) {
            mainPanel.scores.add(Integer.valueOf(var0.buf()[var2]));
         }

         mainPanel.current = var0.Buf_Write;
         mainPanel.last = var0.Buf_Read;
         mainPanel.psize = var0.Buf_Size;
         mainPanel2.scores.clear();

         for(var2 = 0; var2 < 100; ++var2) {
            mainPanel2.scores.add(Integer.valueOf(var0.buf()[var2]));
         }
      }

      mainPanel3.scores.clear();
      mainPanel4.scores.clear();

      for(var2 = 0; var2 < var1.buf().length / 2; var2 += 2) {
         mainPanel4.scores.add(var1.buf()[var2 + 1] * 256 + var1.buf()[var2]);
      }

      frame.repaint();
   }

   static {
      LINE_CURRENT_COLOR = Color.blue;
      LINE_LAST_COLOR = Color.red;
      GRAPH_COLOR = Color.green;
      GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
      GRAPH_STROKE = new BasicStroke(3.0F);
   }
}
