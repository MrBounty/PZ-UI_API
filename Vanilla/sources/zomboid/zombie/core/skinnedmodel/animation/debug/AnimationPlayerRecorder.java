package zombie.core.skinnedmodel.animation.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.action.ActionState;
import zombie.core.logger.LoggerManager;
import zombie.core.skinnedmodel.advancedanimation.AnimState;
import zombie.core.skinnedmodel.advancedanimation.IAnimationVariableSource;
import zombie.core.skinnedmodel.advancedanimation.LiveAnimNode;
import zombie.debug.DebugLog;
import zombie.iso.Vector2;
import zombie.iso.Vector3;

public final class AnimationPlayerRecorder {
   private boolean m_isRecording = false;
   private final AnimationTrackRecordingFrame m_animationTrackFrame;
   private final AnimationNodeRecordingFrame m_animationNodeFrame;
   private final AnimationVariableRecordingFrame m_animationVariableFrame;
   private final IsoGameCharacter m_character;
   private static String s_startupTimeStamp = null;
   private static final SimpleDateFormat s_fileNameSdf = new SimpleDateFormat("yy-MM-dd_HH-mm");

   public AnimationPlayerRecorder(IsoGameCharacter var1) {
      this.m_character = var1;
      String var2 = this.m_character.getUID();
      String var3 = var2 + "_AnimRecorder";
      this.m_animationTrackFrame = new AnimationTrackRecordingFrame(var3 + "_Track");
      this.m_animationNodeFrame = new AnimationNodeRecordingFrame(var3 + "_Node");
      this.m_animationVariableFrame = new AnimationVariableRecordingFrame(var3 + "_Vars");
   }

   public void beginLine(int var1) {
      this.m_animationTrackFrame.reset();
      this.m_animationTrackFrame.setFrameNumber(var1);
      this.m_animationNodeFrame.reset();
      this.m_animationNodeFrame.setFrameNumber(var1);
      this.m_animationVariableFrame.reset();
      this.m_animationVariableFrame.setFrameNumber(var1);
   }

   public void endLine() {
      this.m_animationTrackFrame.writeLine();
      this.m_animationNodeFrame.writeLine();
      this.m_animationVariableFrame.writeLine();
   }

   public void discardRecording() {
      this.m_animationTrackFrame.closeAndDiscard();
      this.m_animationNodeFrame.closeAndDiscard();
      this.m_animationVariableFrame.closeAndDiscard();
   }

   public static PrintStream openFileStream(String var0, boolean var1, Consumer var2) {
      String var3 = getTimeStampedFilePath(var0);

      try {
         var2.accept(var3);
         File var4 = new File(var3);
         return new PrintStream(new FileOutputStream(var4, var1));
      } catch (FileNotFoundException var5) {
         DebugLog.General.error("Exception thrown trying to create animation player recording file.");
         DebugLog.General.error(var5);
         var5.printStackTrace();
         return null;
      }
   }

   private static String getTimeStampedFilePath(String var0) {
      String var10000 = LoggerManager.getLogsDir();
      return var10000 + File.separator + getTimeStampedFileName(var0) + ".csv";
   }

   private static String getTimeStampedFileName(String var0) {
      String var10000 = getStartupTimeStamp();
      return var10000 + "_" + var0;
   }

   private static String getStartupTimeStamp() {
      if (s_startupTimeStamp == null) {
         s_startupTimeStamp = s_fileNameSdf.format(Calendar.getInstance().getTime());
      }

      return s_startupTimeStamp;
   }

   public void logAnimWeights(List var1, int[] var2, float[] var3, Vector2 var4) {
      this.m_animationTrackFrame.logAnimWeights(var1, var2, var3, var4);
   }

   public void logAnimNode(LiveAnimNode var1) {
      if (var1.isTransitioningIn()) {
         this.m_animationNodeFrame.logWeight("transition(" + var1.getTransitionFrom() + "->" + var1.getName() + ")", var1.getTransitionLayerIdx(), var1.getTransitionInWeight());
      }

      this.m_animationNodeFrame.logWeight(var1.getName(), var1.getLayerIdx(), var1.getWeight());
   }

   public void logActionState(ActionState var1, List var2) {
      this.m_animationNodeFrame.logActionState(var1, var2);
   }

   public void logAIState(State var1, List var2) {
      this.m_animationNodeFrame.logAIState(var1, var2);
   }

   public void logAnimState(AnimState var1) {
      this.m_animationNodeFrame.logAnimState(var1);
   }

   public void logVariables(IAnimationVariableSource var1) {
      this.m_animationVariableFrame.logVariables(var1);
   }

   public void logCharacterPos() {
      IsoPlayer var1 = IsoPlayer.getInstance();
      IsoGameCharacter var2 = this.getOwner();
      Vector3 var3 = var1.getPosition(new Vector3());
      Vector3 var4 = var2.getPosition(new Vector3());
      Vector3 var5 = var3.sub(var4, new Vector3());
      this.m_animationNodeFrame.logCharacterToPlayerDiff(var5);
   }

   public IsoGameCharacter getOwner() {
      return this.m_character;
   }

   public boolean isRecording() {
      return this.m_isRecording;
   }

   public void setRecording(boolean var1) {
      if (this.m_isRecording != var1) {
         this.m_isRecording = var1;
         DebugLog.General.println("AnimationPlayerRecorder %s.", this.m_isRecording ? "recording" : "stopped");
      }

   }
}
