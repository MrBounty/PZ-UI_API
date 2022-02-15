package zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import zombie.core.profiling.TriggerGameProfilerFile;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.iso.IsoCamera;
import zombie.ui.TextManager;
import zombie.util.IPooledObject;
import zombie.util.Lambda;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.lambda.Invokers;

public final class GameProfiler {
   private static final String s_currentSessionUUID = UUID.randomUUID().toString();
   private static final ThreadLocal s_instance = ThreadLocal.withInitial(GameProfiler::new);
   private final Stack m_stack = new Stack();
   private final GameProfiler.RecordingFrame m_currentFrame = new GameProfiler.RecordingFrame();
   private final GameProfiler.RecordingFrame m_previousFrame = new GameProfiler.RecordingFrame();
   private boolean m_isInFrame;
   private final GameProfileRecording m_recorder;
   private static final Object m_gameProfilerRecordingTriggerLock = "Game Profiler Recording Watcher, synchronization lock";
   private static PredicatedFileWatcher m_gameProfilerRecordingTriggerWatcher;

   private GameProfiler() {
      String var1 = Thread.currentThread().getName();
      String var2 = var1.replace("-", "").replace(" ", "");
      String var3 = String.format("%s_GameProfiler_%s", this.getCurrentSessionUUID(), var2);
      this.m_recorder = new GameProfileRecording(var3);
   }

   private static void onTrigger_setAnimationRecorderTriggerFile(TriggerGameProfilerFile var0) {
      DebugOptions.instance.GameProfilerEnabled.setValue(var0.isRecording);
   }

   private String getCurrentSessionUUID() {
      return s_currentSessionUUID;
   }

   public static GameProfiler getInstance() {
      return (GameProfiler)s_instance.get();
   }

   public void startFrame(String var1) {
      if (this.m_isInFrame) {
         throw new RuntimeException("Already inside a frame.");
      } else {
         this.m_isInFrame = true;
         if (!this.m_stack.empty()) {
            throw new RuntimeException("Recording stack should be empty.");
         } else {
            int var2 = IsoCamera.frameState.frameCount;
            if (this.m_currentFrame.FrameNo != var2) {
               this.m_previousFrame.transferFrom(this.m_currentFrame);
               if (this.m_previousFrame.FrameNo != -1) {
                  this.m_recorder.writeLine();
               }

               long var3 = getTimeNs();
               this.m_currentFrame.FrameNo = var2;
               this.m_currentFrame.m_frameInvokerKey = var1;
               this.m_currentFrame.m_startTime = var3;
               this.m_recorder.reset();
               this.m_recorder.setFrameNumber(this.m_currentFrame.FrameNo);
               this.m_recorder.setStartTime(this.m_currentFrame.m_startTime);
            }

         }
      }
   }

   public void endFrame() {
      this.m_currentFrame.m_endTime = getTimeNs();
      this.m_currentFrame.m_totalTime = this.m_currentFrame.m_endTime - this.m_currentFrame.m_startTime;
      this.m_isInFrame = false;
   }

   public void invokeAndMeasureFrame(String var1, Runnable var2) {
      if (!isRunning()) {
         var2.run();
      } else {
         this.startFrame(var1);

         try {
            this.invokeAndMeasure(var1, var2);
         } finally {
            this.endFrame();
         }

      }
   }

   public void invokeAndMeasure(String var1, Runnable var2) {
      if (!isRunning()) {
         var2.run();
      } else if (!this.m_isInFrame) {
         DebugLog.General.warn("Not inside in a frame. Find the root caller function for this thread, and add call to invokeAndMeasureFrame.");
      } else {
         GameProfiler.ProfileArea var3 = this.start(var1);

         try {
            var2.run();
         } finally {
            this.end(var3);
         }

      }
   }

   public static boolean isRunning() {
      return DebugOptions.instance.GameProfilerEnabled.getValue();
   }

   public void invokeAndMeasure(String var1, Object var2, Invokers.Params1.ICallback var3) {
      if (!isRunning()) {
         var3.accept(var2);
      } else {
         Lambda.capture(this, var1, var2, var3, (var0, var1x, var2x, var3x, var4) -> {
            var1x.invokeAndMeasure(var2x, var0.invoker(var3x, var4));
         });
      }
   }

   public void invokeAndMeasure(String var1, Object var2, Object var3, Invokers.Params2.ICallback var4) {
      if (!isRunning()) {
         var4.accept(var2, var3);
      } else {
         Lambda.capture(this, var1, var2, var3, var4, (var0, var1x, var2x, var3x, var4x, var5) -> {
            var1x.invokeAndMeasure(var2x, var0.invoker(var3x, var4x, var5));
         });
      }
   }

   public void invokeAndMeasure(String var1, Object var2, Object var3, Object var4, Invokers.Params3.ICallback var5) {
      if (!isRunning()) {
         var5.accept(var2, var3, var4);
      } else {
         Lambda.capture(this, var1, var2, var3, var4, var5, (var0, var1x, var2x, var3x, var4x, var5x, var6) -> {
            var1x.invokeAndMeasure(var2x, var0.invoker(var3x, var4x, var5x, var6));
         });
      }
   }

   public GameProfiler.ProfileArea start(String var1) {
      long var2 = getTimeNs();
      GameProfiler.ProfileArea var4 = GameProfiler.ProfileArea.alloc();
      var4.Key = var1;
      return this.start(var4, var2);
   }

   public GameProfiler.ProfileArea start(GameProfiler.ProfileArea var1) {
      long var2 = getTimeNs();
      return this.start(var1, var2);
   }

   public GameProfiler.ProfileArea start(GameProfiler.ProfileArea var1, long var2) {
      var1.StartTime = var2;
      var1.Depth = this.m_stack.size();
      if (!this.m_stack.isEmpty()) {
         GameProfiler.ProfileArea var4 = (GameProfiler.ProfileArea)this.m_stack.peek();
         var4.Children.add(var1);
      }

      this.m_stack.push(var1);
      return var1;
   }

   public void end(GameProfiler.ProfileArea var1) {
      var1.EndTime = getTimeNs();
      var1.Total = var1.EndTime - var1.StartTime;
      if (this.m_stack.peek() != var1) {
         throw new RuntimeException("Incorrect exit. ProfileArea " + var1 + " is not at the top of the stack: " + this.m_stack.peek());
      } else {
         this.m_stack.pop();
         if (this.m_stack.isEmpty()) {
            this.m_recorder.logTimeSpan(var1);
            var1.release();
         }

      }
   }

   private void renderPercent(String var1, long var2, int var4, int var5, float var6, float var7, float var8) {
      float var9 = (float)var2 / (float)this.m_previousFrame.m_totalTime;
      var9 *= 100.0F;
      var9 = (float)((int)(var9 * 10.0F)) / 10.0F;
      TextManager.instance.DrawString((double)var4, (double)var5, var1, (double)var6, (double)var7, (double)var8, 1.0D);
      TextManager.instance.DrawString((double)(var4 + 300), (double)var5, String.valueOf(var9) + "%", (double)var6, (double)var7, (double)var8, 1.0D);
   }

   public void render(int var1, int var2) {
      this.renderPercent(this.m_previousFrame.m_frameInvokerKey, this.m_previousFrame.m_totalTime, var1, var2, 1.0F, 1.0F, 1.0F);
   }

   public static long getTimeNs() {
      return System.nanoTime();
   }

   public static void init() {
      initTriggerWatcher();
   }

   private static void initTriggerWatcher() {
      if (m_gameProfilerRecordingTriggerWatcher == null) {
         synchronized(m_gameProfilerRecordingTriggerLock) {
            if (m_gameProfilerRecordingTriggerWatcher == null) {
               m_gameProfilerRecordingTriggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_PerformanceProfiler.xml"), TriggerGameProfilerFile.class, GameProfiler::onTrigger_setAnimationRecorderTriggerFile);
               DebugFileWatcher.instance.add(m_gameProfilerRecordingTriggerWatcher);
            }
         }
      }

   }

   public static class RecordingFrame {
      private String m_frameInvokerKey = "";
      private int FrameNo = -1;
      private long m_startTime = 0L;
      private long m_endTime = 0L;
      private long m_totalTime = 0L;

      public void transferFrom(GameProfiler.RecordingFrame var1) {
         this.clear();
         this.FrameNo = var1.FrameNo;
         this.m_frameInvokerKey = var1.m_frameInvokerKey;
         this.m_startTime = var1.m_startTime;
         this.m_endTime = var1.m_endTime;
         this.m_totalTime = var1.m_totalTime;
         var1.clear();
      }

      public void clear() {
         this.FrameNo = -1;
         this.m_frameInvokerKey = "";
         this.m_startTime = 0L;
         this.m_endTime = 0L;
         this.m_totalTime = 0L;
      }
   }

   public static class ProfileArea extends PooledObject {
      public String Key;
      public long StartTime;
      public long EndTime;
      public long Total;
      public int Depth;
      public float r = 1.0F;
      public float g = 1.0F;
      public float b = 1.0F;
      public final List Children = new ArrayList();
      private static final Pool s_pool = new Pool(GameProfiler.ProfileArea::new);

      public void onReleased() {
         super.onReleased();
         this.clear();
      }

      public void clear() {
         this.StartTime = 0L;
         this.EndTime = 0L;
         this.Total = 0L;
         this.Depth = 0;
         IPooledObject.release(this.Children);
      }

      public static GameProfiler.ProfileArea alloc() {
         return (GameProfiler.ProfileArea)s_pool.alloc();
      }
   }
}
