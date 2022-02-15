package zombie.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import zombie.core.Color;
import zombie.core.network.ByteBufferWriter;

public class ChatMessage implements Cloneable {
   private ChatBase chat;
   private LocalDateTime datetime;
   private String author;
   private String text;
   private boolean scramble;
   private String customTag;
   private Color textColor;
   private boolean customColor;
   private boolean overHeadSpeech;
   private boolean showInChat;
   private boolean fromDiscord;
   private boolean serverAlert;
   private int radioChannel;
   private boolean local;
   private boolean shouldAttractZombies;
   private boolean serverAuthor;

   public ChatMessage(ChatBase var1, String var2) {
      this(var1, LocalDateTime.now(), var2);
   }

   public ChatMessage(ChatBase var1, LocalDateTime var2, String var3) {
      this.scramble = false;
      this.overHeadSpeech = true;
      this.showInChat = true;
      this.fromDiscord = false;
      this.serverAlert = false;
      this.radioChannel = -1;
      this.local = false;
      this.shouldAttractZombies = false;
      this.serverAuthor = false;
      this.chat = var1;
      this.datetime = var2;
      this.text = var3;
      this.textColor = var1.getColor();
      this.customColor = false;
   }

   public boolean isShouldAttractZombies() {
      return this.shouldAttractZombies;
   }

   public void setShouldAttractZombies(boolean var1) {
      this.shouldAttractZombies = var1;
   }

   public boolean isLocal() {
      return this.local;
   }

   public void setLocal(boolean var1) {
      this.local = var1;
   }

   public String getTextWithReplacedParentheses() {
      return this.text != null ? this.text.replaceAll("<", "&lt;").replaceAll(">", "&gt;") : null;
   }

   public void setScrambledText(String var1) {
      this.scramble = true;
      this.text = var1;
   }

   public int getRadioChannel() {
      return this.radioChannel;
   }

   public void setRadioChannel(int var1) {
      this.radioChannel = var1;
   }

   public boolean isServerAuthor() {
      return this.serverAuthor;
   }

   public void setServerAuthor(boolean var1) {
      this.serverAuthor = var1;
   }

   public boolean isFromDiscord() {
      return this.fromDiscord;
   }

   public void makeFromDiscord() {
      this.fromDiscord = true;
   }

   public boolean isOverHeadSpeech() {
      return this.overHeadSpeech;
   }

   public void setOverHeadSpeech(boolean var1) {
      this.overHeadSpeech = var1;
   }

   public boolean isShowInChat() {
      return this.showInChat;
   }

   public void setShowInChat(boolean var1) {
      this.showInChat = var1;
   }

   public LocalDateTime getDatetime() {
      return this.datetime;
   }

   public String getDatetimeStr() {
      return this.datetime.format(DateTimeFormatter.ofPattern("h:m"));
   }

   public void setDatetime(LocalDateTime var1) {
      this.datetime = var1;
   }

   public boolean isShowAuthor() {
      return this.getChat().isShowAuthor();
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String var1) {
      this.author = var1;
   }

   public ChatBase getChat() {
      return this.chat;
   }

   public int getChatID() {
      return this.chat.getID();
   }

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      this.text = var1;
   }

   public String getTextWithPrefix() {
      return this.chat.getMessageTextWithPrefix(this);
   }

   public boolean isScramble() {
      return this.scramble;
   }

   public String getCustomTag() {
      return this.customTag;
   }

   public void setCustomTag(String var1) {
      this.customTag = var1;
   }

   public Color getTextColor() {
      return this.textColor;
   }

   public void setTextColor(Color var1) {
      this.customColor = true;
      this.textColor = var1;
   }

   public boolean isCustomColor() {
      return this.customColor;
   }

   public void pack(ByteBufferWriter var1) {
      this.chat.packMessage(var1, this);
   }

   public ChatMessage clone() {
      ChatMessage var1;
      try {
         var1 = (ChatMessage)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new RuntimeException();
      }

      var1.datetime = this.datetime;
      var1.chat = this.chat;
      var1.author = this.author;
      var1.text = this.text;
      var1.scramble = this.scramble;
      var1.customTag = this.customTag;
      var1.textColor = this.textColor;
      var1.customColor = this.customColor;
      var1.overHeadSpeech = this.overHeadSpeech;
      return var1;
   }

   public boolean isServerAlert() {
      return this.serverAlert;
   }

   public void setServerAlert(boolean var1) {
      this.serverAlert = var1;
   }

   public String toString() {
      String var10000 = this.chat.getTitle();
      return "ChatMessage{chat=" + var10000 + ", author='" + this.author + "', text='" + this.text + "'}";
   }
}
