package zombie;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.core.skinnedmodel.animation.debug.GenericNameValueRecordingFrame;
import zombie.util.IPooledObject;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.list.PZArrayUtil;

public final class GameProfileRecording extends GenericNameValueRecordingFrame {
   private long m_startTime;
   private final GameProfileRecording.Row m_rootRow = new GameProfileRecording.Row();
   private final HashMap m_keyValueTable = new HashMap();
   protected PrintStream m_outSegment = null;
   private long m_firstFrameNo = -1L;
   private final List m_segmentFilePaths = new ArrayList();
   private int m_numFramesPerFile = 60;
   private int m_currentSegmentFrameCount = 0;

   public GameProfileRecording(String var1) {
      super(var1, "_times");
      this.addColumnInternal("StartTime");
      this.addColumnInternal("EndTime");
      this.addColumnInternal("SegmentNo");
      this.addColumnInternal("Spans");
      this.addColumnInternal("key");
      this.addColumnInternal("Depth");
      this.addColumnInternal("StartTime");
      this.addColumnInternal("EndTime");
      this.addColumnInternal("Time Format");
      this.addColumnInternal("x * 100ns");
   }

   public void setNumFramesPerSegment(int var1) {
      this.m_numFramesPerFile = var1;
   }

   public void setStartTime(long var1) {
      this.m_startTime = var1;
   }

   public void logTimeSpan(GameProfiler.ProfileArea var1) {
      if (this.m_firstFrameNo == -1L) {
         this.m_firstFrameNo = (long)this.m_frameNumber;
      }

      GameProfileRecording.Span var2 = this.allocSpan(var1);
      GameProfileRecording.Row var3 = this.m_rootRow;
      if (var3.Spans.isEmpty()) {
         var3.StartTime = var2.StartTime;
      }

      var3.EndTime = var2.EndTime;
      var3.Spans.add(var2);
   }

   protected GameProfileRecording.Span allocSpan(GameProfiler.ProfileArea var1) {
      int var2 = this.getOrCreateKey(var1.Key);
      long var3 = var1.StartTime - this.m_startTime;
      long var5 = var1.EndTime - this.m_startTime;
      GameProfileRecording.Span var7 = GameProfileRecording.Span.alloc();
      var7.key = var2;
      var7.Depth = var1.Depth;
      var7.StartTime = var3;
      var7.EndTime = var5;
      int var8 = 0;

      for(int var9 = var1.Children.size(); var8 < var9; ++var8) {
         GameProfiler.ProfileArea var10 = (GameProfiler.ProfileArea)var1.Children.get(var8);
         GameProfileRecording.Span var11 = this.allocSpan(var10);
         var7.Children.add(var11);
      }

      return var7;
   }

   private int getOrCreateKey(String var1) {
      Integer var2 = (Integer)this.m_keyValueTable.get(var1);
      if (var2 == null) {
         var2 = this.m_keyValueTable.size();
         this.m_keyValueTable.put(var1, var2);
         this.m_headerDirty = true;
      }

      return var2;
   }

   public String getValueAt(int var1) {
      throw new RuntimeException("Not implemented. Use getValueAt(row, col)");
   }

   protected void onColumnAdded() {
   }

   public void reset() {
      this.m_rootRow.reset();
   }

   protected void openSegmentFile(boolean var1) {
      if (this.m_outSegment != null) {
         this.m_outSegment.flush();
         this.m_outSegment.close();
      }

      String var2 = String.format("%s%s_%04d", this.m_fileKey, this.m_valuesFileNameSuffix, this.m_segmentFilePaths.size());
      List var10003 = this.m_segmentFilePaths;
      Objects.requireNonNull(var10003);
      this.m_outSegment = AnimationPlayerRecorder.openFileStream(var2, var1, var10003::add);
      this.m_currentSegmentFrameCount = 0;
      this.m_headerDirty = true;
   }

   public void close() {
      if (this.m_outSegment != null) {
         this.m_outSegment.close();
         this.m_outSegment = null;
      }

   }

   public void closeAndDiscard() {
      super.closeAndDiscard();
      List var10000 = this.m_segmentFilePaths;
      ZomboidFileSystem var10001 = ZomboidFileSystem.instance;
      Objects.requireNonNull(var10001);
      PZArrayUtil.forEach(var10000, var10001::tryDeleteFile);
      this.m_segmentFilePaths.clear();
   }

   protected void writeData() {
      if (this.m_outValues == null) {
         this.openValuesFile(false);
      }

      StringBuilder var1 = this.m_lineBuffer;
      var1.setLength(0);
      ++this.m_currentSegmentFrameCount;
      if (this.m_outSegment == null || this.m_currentSegmentFrameCount >= this.m_numFramesPerFile) {
         this.openSegmentFile(false);
      }

      this.writeDataRow(var1, this.m_rootRow);
      this.m_outSegment.print(this.m_frameNumber);
      this.m_outSegment.println(var1);
      var1 = this.m_lineBuffer;
      var1.setLength(0);
      this.writeFrameTimeRow(var1, this.m_rootRow, this.m_segmentFilePaths.size() - 1);
      this.m_outValues.print(this.m_frameNumber);
      this.m_outValues.println(var1);
   }

   private void writeDataRow(StringBuilder var1, GameProfileRecording.Row var2) {
      int var3 = 0;

      for(int var4 = var2.Spans.size(); var3 < var4; ++var3) {
         GameProfileRecording.Span var5 = (GameProfileRecording.Span)var2.Spans.get(var3);
         this.writeSpan(var1, var2, var5);
      }

   }

   private void writeFrameTimeRow(StringBuilder var1, GameProfileRecording.Row var2, int var3) {
      appendCell(var1, var2.StartTime / 100L);
      appendCell(var1, var2.EndTime / 100L);
      appendCell(var1, var3);
   }

   private void writeSpan(StringBuilder var1, GameProfileRecording.Row var2, GameProfileRecording.Span var3) {
      long var4 = (var3.StartTime - var2.StartTime) / 100L;
      long var6 = (var3.EndTime - var3.StartTime) / 100L;
      appendCell(var1, var3.key);
      appendCell(var1, var3.Depth);
      appendCell(var1, var4);
      appendCell(var1, var6);
      int var8 = 0;

      for(int var9 = var3.Children.size(); var8 < var9; ++var8) {
         GameProfileRecording.Span var10 = (GameProfileRecording.Span)var3.Children.get(var8);
         this.writeSpan(var1, var2, var10);
      }

   }

   protected void writeHeader() {
      super.writeHeader();
      this.m_outHeader.println();
      this.m_outHeader.println("Segmentation Info");
      this.m_outHeader.println("FirstFrame," + this.m_firstFrameNo);
      this.m_outHeader.println("NumFramesPerFile," + this.m_numFramesPerFile);
      this.m_outHeader.println("NumFiles," + this.m_segmentFilePaths.size());
      this.m_outHeader.println();
      this.m_outHeader.println("KeyNamesTable");
      this.m_outHeader.println("Index,Name");
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.m_keyValueTable.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.setLength(0);
         var1.append(var3.getValue());
         var1.append(",");
         var1.append((String)var3.getKey());
         this.m_outHeader.println(var1);
      }

   }

   public static class Row {
      long StartTime;
      long EndTime;
      final List Spans = new ArrayList();

      public void reset() {
         IPooledObject.release(this.Spans);
      }
   }

   public static class Span extends PooledObject {
      int key;
      int Depth;
      long StartTime;
      long EndTime;
      final List Children = new ArrayList();
      private static final Pool s_pool = new Pool(GameProfileRecording.Span::new);

      public void onReleased() {
         super.onReleased();
         IPooledObject.release(this.Children);
      }

      public static GameProfileRecording.Span alloc() {
         return (GameProfileRecording.Span)s_pool.alloc();
      }
   }
}
