package zombie.characters.action;

public final class ActionContextEvents {
   private ActionContextEvents.Event m_firstEvent;
   private ActionContextEvents.Event m_eventPool;

   public void add(String var1, int var2) {
      if (!this.contains(var1, var2, false)) {
         ActionContextEvents.Event var3 = this.allocEvent();
         var3.name = var1;
         var3.layer = var2;
         var3.next = this.m_firstEvent;
         this.m_firstEvent = var3;
      }
   }

   public boolean contains(String var1, int var2) {
      return this.contains(var1, var2, true);
   }

   public boolean contains(String var1, int var2, boolean var3) {
      for(ActionContextEvents.Event var4 = this.m_firstEvent; var4 != null; var4 = var4.next) {
         if (var4.name.equalsIgnoreCase(var1)) {
            if (var2 == -1) {
               return true;
            }

            if (var4.layer == var2) {
               return true;
            }

            if (var3 && var4.layer == -1) {
               return true;
            }
         }
      }

      return false;
   }

   public void clear() {
      if (this.m_firstEvent != null) {
         ActionContextEvents.Event var1;
         for(var1 = this.m_firstEvent; var1.next != null; var1 = var1.next) {
         }

         var1.next = this.m_eventPool;
         this.m_eventPool = this.m_firstEvent;
         this.m_firstEvent = null;
      }
   }

   public void clearEvent(String var1) {
      ActionContextEvents.Event var2 = null;

      ActionContextEvents.Event var4;
      for(ActionContextEvents.Event var3 = this.m_firstEvent; var3 != null; var3 = var4) {
         var4 = var3.next;
         if (var3.name.equalsIgnoreCase(var1)) {
            this.releaseEvent(var3, var2);
         } else {
            var2 = var3;
         }
      }

   }

   private ActionContextEvents.Event allocEvent() {
      if (this.m_eventPool == null) {
         return new ActionContextEvents.Event();
      } else {
         ActionContextEvents.Event var1 = this.m_eventPool;
         this.m_eventPool = var1.next;
         return var1;
      }
   }

   private void releaseEvent(ActionContextEvents.Event var1, ActionContextEvents.Event var2) {
      if (var2 == null) {
         assert var1 == this.m_firstEvent;

         this.m_firstEvent = var1.next;
      } else {
         assert var1 != this.m_firstEvent;

         assert var2.next == var1;

         var2.next = var1.next;
      }

      var1.next = this.m_eventPool;
      this.m_eventPool = var1;
   }

   private static final class Event {
      int layer;
      String name;
      ActionContextEvents.Event next;
   }
}
