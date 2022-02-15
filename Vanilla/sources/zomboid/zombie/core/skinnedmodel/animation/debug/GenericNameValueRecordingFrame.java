package zombie.core.skinnedmodel.animation.debug;

import java.io.PrintStream;
import java.util.HashMap;
import zombie.ZomboidFileSystem;
import zombie.util.list.PZArrayUtil;

public abstract class GenericNameValueRecordingFrame {
   protected String[] m_columnNames = new String[0];
   protected final HashMap m_nameIndices = new HashMap();
   protected boolean m_headerDirty = false;
   protected final String m_fileKey;
   protected PrintStream m_outHeader = null;
   protected PrintStream m_outValues = null;
   private String m_headerFilePath = null;
   private String m_valuesFilePath = null;
   protected int m_frameNumber = -1;
   protected static final String delim = ",";
   protected final String m_valuesFileNameSuffix;
   private String m_previousLine = null;
   private int m_previousFrameNo = -1;
   protected final StringBuilder m_lineBuffer = new StringBuilder();

   public GenericNameValueRecordingFrame(String var1, String var2) {
      this.m_fileKey = var1;
      this.m_valuesFileNameSuffix = var2;
   }

   protected int addColumnInternal(String var1) {
      int var2 = this.m_columnNames.length;
      this.m_columnNames = (String[])PZArrayUtil.add(this.m_columnNames, var1);
      this.m_nameIndices.put(var1, var2);
      this.m_headerDirty = true;
      this.onColumnAdded();
      return var2;
   }

   public int getOrCreateColumn(String var1) {
      return this.m_nameIndices.containsKey(var1) ? (Integer)this.m_nameIndices.get(var1) : this.addColumnInternal(var1);
   }

   public void setFrameNumber(int var1) {
      this.m_frameNumber = var1;
   }

   public int getColumnCount() {
      return this.m_columnNames.length;
   }

   public String getNameAt(int var1) {
      return this.m_columnNames[var1];
   }

   public abstract String getValueAt(int var1);

   protected void openHeader(boolean var1) {
      this.m_outHeader = AnimationPlayerRecorder.openFileStream(this.m_fileKey + "_header", var1, (var1x) -> {
         this.m_headerFilePath = var1x;
      });
   }

   protected void openValuesFile(boolean var1) {
      this.m_outValues = AnimationPlayerRecorder.openFileStream(this.m_fileKey + this.m_valuesFileNameSuffix, var1, (var1x) -> {
         this.m_valuesFilePath = var1x;
      });
   }

   public void writeLine() {
      if (this.m_headerDirty || this.m_outHeader == null) {
         this.m_headerDirty = false;
         this.writeHeader();
      }

      this.writeData();
   }

   public void close() {
      if (this.m_outHeader != null) {
         this.m_outHeader.close();
         this.m_outHeader = null;
      }

      if (this.m_outValues != null) {
         this.m_outValues.close();
         this.m_outValues = null;
      }

   }

   public void closeAndDiscard() {
      this.close();
      ZomboidFileSystem.instance.tryDeleteFile(this.m_headerFilePath);
      this.m_headerFilePath = null;
      ZomboidFileSystem.instance.tryDeleteFile(this.m_valuesFilePath);
      this.m_valuesFilePath = null;
   }

   protected abstract void onColumnAdded();

   public abstract void reset();

   protected void writeHeader() {
      StringBuilder var1 = new StringBuilder();
      var1.append("frameNo");
      this.writeHeader(var1);
      this.openHeader(false);
      this.m_outHeader.println(var1);
   }

   protected void writeHeader(StringBuilder var1) {
      int var2 = 0;

      for(int var3 = this.getColumnCount(); var2 < var3; ++var2) {
         appendCell(var1, this.getNameAt(var2));
      }

   }

   protected void writeData() {
      if (this.m_outValues == null) {
         this.openValuesFile(false);
      }

      StringBuilder var1 = this.m_lineBuffer;
      var1.setLength(0);
      this.writeData(var1);
      if (this.m_previousLine == null || !this.m_previousLine.contentEquals(var1)) {
         this.m_outValues.print(this.m_frameNumber);
         this.m_outValues.println(var1);
         this.m_previousLine = var1.toString();
         this.m_previousFrameNo = this.m_frameNumber;
      }
   }

   protected void writeData(StringBuilder var1) {
      int var2 = 0;

      for(int var3 = this.getColumnCount(); var2 < var3; ++var2) {
         appendCell(var1, this.getValueAt(var2));
      }

   }

   public static StringBuilder appendCell(StringBuilder var0) {
      return var0.append(",");
   }

   public static StringBuilder appendCell(StringBuilder var0, String var1) {
      return var0.append(",").append(var1);
   }

   public static StringBuilder appendCell(StringBuilder var0, float var1) {
      return var0.append(",").append(var1);
   }

   public static StringBuilder appendCell(StringBuilder var0, int var1) {
      return var0.append(",").append(var1);
   }

   public static StringBuilder appendCell(StringBuilder var0, long var1) {
      return var0.append(",").append(var1);
   }

   public static StringBuilder appendCellQuot(StringBuilder var0, String var1) {
      return var0.append(",").append('"').append(var1).append('"');
   }
}
