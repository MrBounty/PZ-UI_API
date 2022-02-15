package zombie.core.skinnedmodel.advancedanimation;

import javax.xml.bind.annotation.XmlTransient;

public final class AnimEvent {
   public String m_EventName;
   public AnimEvent.AnimEventTime m_Time;
   public float m_TimePc;
   public String m_ParameterValue;
   @XmlTransient
   public String m_SetVariable1;
   @XmlTransient
   public String m_SetVariable2;

   public AnimEvent() {
      this.m_Time = AnimEvent.AnimEventTime.Percentage;
   }

   public String toString() {
      return String.format("%s { %s }", this.getClass().getName(), this.toDetailsString());
   }

   public String toDetailsString() {
      return String.format("Details: %s %s, time: %s", this.m_EventName, this.m_ParameterValue, this.m_Time == AnimEvent.AnimEventTime.Percentage ? Float.toString(this.m_TimePc) : this.m_Time.name());
   }

   public static enum AnimEventTime {
      Percentage,
      Start,
      End;

      // $FF: synthetic method
      private static AnimEvent.AnimEventTime[] $values() {
         return new AnimEvent.AnimEventTime[]{Percentage, Start, End};
      }
   }
}
