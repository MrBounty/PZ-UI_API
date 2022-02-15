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

public class PhysicsInterpolationDebug extends JPanel {
   private static final int PREF_W = 400;
   private static final int PREF_H = 200;
   private static final int BORDER_GAP = 30;
   private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
   private static final Stroke GRAPH_STROKE = new BasicStroke(3.0F);
   private static final int GRAPH_POINT_WIDTH = 12;
   private static final int Y_HATCH_CNT = 10;
   public VehicleInterpolation idata;
   private static int time_divider = 1000;
   private long ci_time;
   private float ci_x;
   private float ci_y;
   private String ci_user;
   private List graphPoints_x = new ArrayList();
   private List graphPoints_y = new ArrayList();
   private List graphPoints_i = new ArrayList();
   private static PhysicsInterpolationDebug mainPanel;
   private static JFrame frame;

   public PhysicsInterpolationDebug(VehicleInterpolation var1) {
      this.idata = var1;
   }

   public void addCurrentData(long var1, float var3, float var4, String var5) {
      this.ci_time = var1;
      this.ci_x = var3;
      this.ci_y = var4;
      this.ci_user = var5;
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      if (this.idata != null) {
         Graphics2D var2 = (Graphics2D)var1;
         var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         double var3 = ((double)this.getWidth() - 60.0D) / (double)(time_divider - 1);
         double var5 = ((double)this.getHeight() - 60.0D) / 99.0D;
         this.graphPoints_x.clear();
         this.graphPoints_y.clear();
         this.graphPoints_i.clear();

         int var7;
         int var9;
         int var10;
         for(var7 = 0; var7 < this.idata.dataList.size(); ++var7) {
            VehicleInterpolationData var8 = (VehicleInterpolationData)this.idata.dataList.get(var7);
            var9 = (int)((double)(var8.time / 1000000L % (long)time_divider) * var3 + 30.0D);
            var10 = (int)((double)(var8.x * 10.0F % 100.0F) * var5 + 30.0D);
            this.graphPoints_x.add(new Point(var9, var10));
            var10 = (int)((double)(var8.y * 10.0F % 100.0F) * var5 + 30.0D);
            this.graphPoints_y.add(new Point(var9, var10));
         }

         var7 = (int)((double)(this.ci_time / 1000000L % (long)time_divider) * var3 + 30.0D);
         int var15 = (int)((double)(this.ci_x * 10.0F % 100.0F) * var5 + 30.0D);
         this.graphPoints_i.add(new Point(var7, var15));
         var15 = (int)((double)(this.ci_y * 10.0F % 100.0F) * var5 + 30.0D);
         this.graphPoints_i.add(new Point(var7, var15));
         var2.setColor(Color.black);
         var2.drawLine(30, this.getHeight() - 30, 30, 30);
         var2.drawLine(30, this.getHeight() - 30, this.getWidth() - 30, this.getHeight() - 30);

         int var12;
         for(var9 = 0; var9 < 10; ++var9) {
            byte var17 = 30;
            byte var11 = 42;
            var12 = this.getHeight() - ((var9 + 1) * (this.getHeight() - 60) / 10 + 30);
            var2.drawLine(var17, var12, var11, var12);
         }

         Stroke var16 = var2.getStroke();
         var2.setColor(Color.red);
         var2.setStroke(GRAPH_STROKE);

         int var13;
         int var14;
         int var18;
         for(var10 = 0; var10 < this.graphPoints_x.size() - 1; ++var10) {
            var18 = ((Point)this.graphPoints_x.get(var10)).x;
            var12 = ((Point)this.graphPoints_x.get(var10)).y;
            var13 = ((Point)this.graphPoints_x.get(var10 + 1)).x;
            var14 = ((Point)this.graphPoints_x.get(var10 + 1)).y;
            var2.drawLine(var18, var12, var13, var14);
         }

         var2.setColor(Color.green);
         var2.setStroke(GRAPH_STROKE);

         for(var10 = 0; var10 < this.graphPoints_y.size() - 1; ++var10) {
            var18 = ((Point)this.graphPoints_y.get(var10)).x;
            var12 = ((Point)this.graphPoints_y.get(var10)).y;
            var13 = ((Point)this.graphPoints_y.get(var10 + 1)).x;
            var14 = ((Point)this.graphPoints_y.get(var10 + 1)).y;
            var2.drawLine(var18, var12, var13, var14);
         }

         var2.setStroke(var16);
         var2.setColor(GRAPH_POINT_COLOR);

         for(var10 = 0; var10 < this.graphPoints_i.size(); ++var10) {
            var18 = ((Point)this.graphPoints_i.get(var10)).x - 6;
            var12 = ((Point)this.graphPoints_i.get(var10)).y - 6;
            byte var19 = 12;
            byte var20 = 12;
            var2.fillOval(var18, var12, var19, var20);
         }

         var2.setColor(Color.black);
         var2.drawString("dataList.size: " + this.idata.dataList.size(), 30, 15);
         var2.drawString("Current t=" + this.ci_time + " x=" + this.ci_x + " y=" + this.ci_y + " user=" + this.ci_user, 30, 30);
      }
   }

   public Dimension getPreferredSize() {
      return new Dimension(400, 200);
   }

   public static void createAndShowGui() {
      mainPanel = new PhysicsInterpolationDebug((VehicleInterpolation)null);
      frame = new JFrame("DrawGraph");
      frame.setDefaultCloseOperation(3);
      frame.setLayout(new GridLayout(1, 1));
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void updateGui() {
      ArrayList var0 = VehicleManager.instance.getVehicles();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         BaseVehicle var2 = (BaseVehicle)var0.get(var1);
         if (var2.getPassenger(0).character != null && !var2.isKeyboardControlled()) {
            mainPanel.idata = var2.interpolation;
            break;
         }
      }

      frame.repaint();
   }

   public static void addCurrentDataS(long var0, float var2, float var3, String var4) {
      mainPanel.addCurrentData(var0, var2, var3, var4);
   }
}
