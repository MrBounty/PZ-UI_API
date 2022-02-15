package zombie.vehicles;

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

public class PhysicsDataDebugWindow extends JPanel {
   private static final int PREF_W = 400;
   private static final int PREF_H = 200;
   private static final int BORDER_GAP = 30;
   private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
   private static final Stroke GRAPH_STROKE = new BasicStroke(3.0F);
   private static final int GRAPH_POINT_WIDTH = 12;
   private static final int Y_HATCH_CNT = 10;
   private static int time_divider = 1000;
   private List graphPoints_x = new ArrayList();
   private List graphPoints_y = new ArrayList();
   private static PhysicsDataDebugWindow mainPanel;
   private static JFrame frame;

   public void addCurrentData(long var1, float var3, float var4) {
      if (this.graphPoints_x.size() > 100) {
         this.graphPoints_x.clear();
         this.graphPoints_y.clear();
      }

      double var5 = ((double)this.getWidth() - 60.0D) / (double)(time_divider - 1);
      double var7 = ((double)this.getHeight() - 60.0D) / 99.0D;
      int var9 = (int)((double)(var1 % (long)time_divider) * var5 + 30.0D);
      int var10 = (int)((double)(var3 * 10.0F % 100.0F) * var7 + 30.0D);
      this.graphPoints_x.add(new Point(var9, var10));
      var10 = (int)((double)(var4 * 10.0F % 100.0F) * var7 + 30.0D);
      this.graphPoints_y.add(new Point(var9, var10));
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      Graphics2D var2 = (Graphics2D)var1;
      var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var2.setColor(Color.black);
      var2.drawLine(30, this.getHeight() - 30, 30, 30);
      var2.drawLine(30, this.getHeight() - 30, this.getWidth() - 30, this.getHeight() - 30);

      int var6;
      for(int var3 = 0; var3 < 10; ++var3) {
         byte var4 = 30;
         byte var5 = 42;
         var6 = this.getHeight() - ((var3 + 1) * (this.getHeight() - 60) / 10 + 30);
         var2.drawLine(var4, var6, var5, var6);
      }

      Stroke var9 = var2.getStroke();
      var2.setColor(Color.red);
      var2.setStroke(GRAPH_STROKE);

      int var7;
      int var8;
      int var10;
      int var11;
      for(var10 = 0; var10 < this.graphPoints_x.size() - 1; ++var10) {
         var11 = ((Point)this.graphPoints_x.get(var10)).x;
         var6 = ((Point)this.graphPoints_x.get(var10)).y;
         var7 = ((Point)this.graphPoints_x.get(var10 + 1)).x;
         var8 = ((Point)this.graphPoints_x.get(var10 + 1)).y;
         var2.drawLine(var11, var6, var7, var8);
      }

      var2.setColor(Color.green);
      var2.setStroke(GRAPH_STROKE);

      for(var10 = 0; var10 < this.graphPoints_y.size() - 1; ++var10) {
         var11 = ((Point)this.graphPoints_y.get(var10)).x;
         var6 = ((Point)this.graphPoints_y.get(var10)).y;
         var7 = ((Point)this.graphPoints_y.get(var10 + 1)).x;
         var8 = ((Point)this.graphPoints_y.get(var10 + 1)).y;
         var2.drawLine(var11, var6, var7, var8);
      }

      var2.setColor(Color.black);
   }

   public Dimension getPreferredSize() {
      return new Dimension(400, 200);
   }

   public static void createAndShowGui() {
      mainPanel = new PhysicsDataDebugWindow();
      frame = new JFrame("PhysicsData");
      frame.setDefaultCloseOperation(3);
      frame.setLayout(new GridLayout(1, 1));
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void updateGui() {
      frame.repaint();
   }

   public static void addCurrentDataS(long var0, float var2, float var3) {
      mainPanel.addCurrentData(var0, var2, var3);
      updateGui();
   }
}
