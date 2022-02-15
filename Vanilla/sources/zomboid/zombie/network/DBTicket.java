package zombie.network;

public class DBTicket {
   private String author = null;
   private String message = "";
   private int ticketID = 0;
   private boolean viewed = false;
   private DBTicket answer = null;
   private boolean isAnswer = false;

   public DBTicket(String var1, String var2, int var3) {
      this.author = var1;
      this.message = var2;
      this.ticketID = var3;
      this.viewed = this.viewed;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String var1) {
      this.author = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public int getTicketID() {
      return this.ticketID;
   }

   public void setTicketID(int var1) {
      this.ticketID = var1;
   }

   public boolean isViewed() {
      return this.viewed;
   }

   public void setViewed(boolean var1) {
      this.viewed = var1;
   }

   public DBTicket getAnswer() {
      return this.answer;
   }

   public void setAnswer(DBTicket var1) {
      this.answer = var1;
   }

   public boolean isAnswer() {
      return this.isAnswer;
   }

   public void setIsAnswer(boolean var1) {
      this.isAnswer = var1;
   }
}
