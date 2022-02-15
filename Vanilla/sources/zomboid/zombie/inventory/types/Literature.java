package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import zombie.GameWindow;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.traits.TraitCollection;
import zombie.characters.traits.TraitFactory;
import zombie.core.Translator;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemType;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;
import zombie.util.io.BitHeader;
import zombie.util.io.BitHeaderRead;
import zombie.util.io.BitHeaderWrite;

public final class Literature extends InventoryItem {
   public boolean bAlreadyRead = false;
   public String requireInHandOrInventory = null;
   public String useOnConsume = null;
   private int numberOfPages = -1;
   private String bookName = "";
   private int LvlSkillTrained = -1;
   private int NumLevelsTrained;
   private String SkillTrained = "None";
   private int alreadyReadPages = 0;
   private boolean canBeWrite = false;
   private HashMap customPages = null;
   private String lockedBy = null;
   private int pageToWrite;
   private List teachedRecipes = null;

   public Literature(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.setBookName(var2);
      this.cat = ItemType.Literature;
      if (this.staticModel == null) {
         this.staticModel = "Book";
      }

   }

   public Literature(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
      this.setBookName(var2);
      this.cat = ItemType.Literature;
      if (this.staticModel == null) {
         this.staticModel = "Book";
      }

   }

   public boolean IsLiterature() {
      return true;
   }

   public int getSaveType() {
      return Item.Type.Literature.ordinal();
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Literature";
   }

   public void update() {
      if (this.container != null) {
      }

   }

   public boolean finishupdate() {
      return true;
   }

   public void DoTooltip(ObjectTooltip var1, ObjectTooltip.Layout var2) {
      ObjectTooltip.LayoutItem var3;
      int var4;
      if (this.getBoredomChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)this.getBoredomChange();
         var3.setLabel(Translator.getText("Tooltip_literature_Boredom_Reduction") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getStressChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)(this.getStressChange() * 100.0F);
         var3.setLabel(Translator.getText("Tooltip_literature_Stress_Reduction") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getUnhappyChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)this.getUnhappyChange();
         var3.setLabel(Translator.getText("Tooltip_food_Unhappiness") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getNumberOfPages() != -1) {
         var3 = var2.addItem();
         var4 = this.getAlreadyReadPages();
         if (var1.getCharacter() != null) {
            var4 = var1.getCharacter().getAlreadyReadPages(this.getFullType());
         }

         var3.setLabel(Translator.getText("Tooltip_literature_Number_of_Pages") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValue(var4 + " / " + this.getNumberOfPages(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      String var14;
      if (this.getLvlSkillTrained() != -1) {
         var3 = var2.addItem();
         var14 = this.getLvlSkillTrained().makeConcatWithConstants<invokedynamic>(this.getLvlSkillTrained());
         if (this.getLvlSkillTrained() != this.getMaxLevelTrained()) {
            var14 = var14 + "-" + this.getMaxLevelTrained();
         }

         var3.setLabel(Translator.getText("Tooltip_Literature_XpMultiplier", var14), 1.0F, 1.0F, 0.8F, 1.0F);
      }

      if (this.getTeachedRecipes() != null) {
         Iterator var16 = this.getTeachedRecipes().iterator();

         while(var16.hasNext()) {
            String var5 = (String)var16.next();
            var3 = var2.addItem();
            String var6 = Translator.getRecipeName(var5);
            var3.setLabel(Translator.getText("Tooltip_Literature_TeachedRecipes", var6), 1.0F, 1.0F, 0.8F, 1.0F);
         }

         if (var1.getCharacter() != null) {
            var3 = var2.addItem();
            var14 = Translator.getText("Tooltip_literature_NotBeenRead");
            if (var1.getCharacter().getKnownRecipes().containsAll(this.getTeachedRecipes())) {
               var14 = Translator.getText("Tooltip_literature_HasBeenRead");
            }

            var3.setLabel(var14, 1.0F, 1.0F, 0.8F, 1.0F);
            if (var1.getCharacter().getKnownRecipes().containsAll(this.getTeachedRecipes())) {
               ProfessionFactory.Profession var13 = ProfessionFactory.getProfession(var1.getCharacter().getDescriptor().getProfession());
               TraitCollection var15 = var1.getCharacter().getTraits();
               int var7 = 0;
               int var8 = 0;

               for(int var9 = 0; var9 < this.getTeachedRecipes().size(); ++var9) {
                  String var10 = (String)this.getTeachedRecipes().get(var9);
                  if (var13 != null && var13.getFreeRecipes().contains(var10)) {
                     ++var7;
                  }

                  for(int var11 = 0; var11 < var15.size(); ++var11) {
                     TraitFactory.Trait var12 = TraitFactory.getTrait(var15.get(var11));
                     if (var12 != null && var12.getFreeRecipes().contains(var10)) {
                        ++var8;
                     }
                  }
               }

               if (var7 > 0 || var8 > 0) {
                  var3 = var2.addItem();
                  var3.setLabel(Translator.getText("Tooltip_literature_AlreadyKnown"), 0.0F, 1.0F, 0.8F, 1.0F);
               }
            }
         }
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      BitHeaderWrite var3 = BitHeader.allocWrite(BitHeader.HeaderSize.Byte, var1);
      byte var4 = 0;
      if (this.numberOfPages >= 127 && this.numberOfPages < 32767) {
         var4 = 1;
      } else if (this.numberOfPages >= 32767) {
         var4 = 2;
      }

      if (this.numberOfPages != -1) {
         var3.addFlags(1);
         if (var4 == 1) {
            var3.addFlags(2);
            var1.putShort((short)this.numberOfPages);
         } else if (var4 == 2) {
            var3.addFlags(4);
            var1.putInt(this.numberOfPages);
         } else {
            var1.put((byte)this.numberOfPages);
         }
      }

      if (this.alreadyReadPages != 0) {
         var3.addFlags(8);
         if (var4 == 1) {
            var1.putShort((short)this.alreadyReadPages);
         } else if (var4 == 2) {
            var1.putInt(this.alreadyReadPages);
         } else {
            var1.put((byte)this.alreadyReadPages);
         }
      }

      if (this.canBeWrite) {
         var3.addFlags(16);
      }

      if (this.customPages != null && this.customPages.size() > 0) {
         var3.addFlags(32);
         var1.putInt(this.customPages.size());
         Iterator var5 = this.customPages.values().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            GameWindow.WriteString(var1, var6);
         }
      }

      if (this.lockedBy != null) {
         var3.addFlags(64);
         GameWindow.WriteString(var1, this.getLockedBy());
      }

      var3.write();
      var3.release();
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      BitHeaderRead var3 = BitHeader.allocRead(BitHeader.HeaderSize.Byte, var1);
      if (!var3.equals(0)) {
         byte var4 = 0;
         if (var3.hasFlags(1)) {
            if (var3.hasFlags(2)) {
               var4 = 1;
               this.numberOfPages = var1.getShort();
            } else if (var3.hasFlags(4)) {
               var4 = 2;
               this.numberOfPages = var1.getInt();
            } else {
               this.numberOfPages = var1.get();
            }
         }

         if (var3.hasFlags(8)) {
            if (var4 == 1) {
               this.alreadyReadPages = var1.getShort();
            } else if (var4 == 2) {
               this.alreadyReadPages = var1.getInt();
            } else {
               this.alreadyReadPages = var1.get();
            }
         }

         this.canBeWrite = var3.hasFlags(16);
         if (var3.hasFlags(32)) {
            int var5 = var1.getInt();
            if (var5 > 0) {
               this.customPages = new HashMap();

               for(int var6 = 0; var6 < var5; ++var6) {
                  this.customPages.put(var6 + 1, GameWindow.ReadString(var1));
               }
            }
         }

         if (var3.hasFlags(64)) {
            this.setLockedBy(GameWindow.ReadString(var1));
         }
      }

      var3.release();
   }

   public float getBoredomChange() {
      return !this.bAlreadyRead ? this.boredomChange : 0.0F;
   }

   public float getUnhappyChange() {
      return !this.bAlreadyRead ? this.unhappyChange : 0.0F;
   }

   public float getStressChange() {
      return !this.bAlreadyRead ? this.stressChange : 0.0F;
   }

   public int getNumberOfPages() {
      return this.numberOfPages;
   }

   public void setNumberOfPages(int var1) {
      this.numberOfPages = var1;
   }

   public String getBookName() {
      return this.bookName;
   }

   public void setBookName(String var1) {
      this.bookName = var1;
   }

   public int getLvlSkillTrained() {
      return this.LvlSkillTrained;
   }

   public void setLvlSkillTrained(int var1) {
      this.LvlSkillTrained = var1;
   }

   public int getNumLevelsTrained() {
      return this.NumLevelsTrained;
   }

   public void setNumLevelsTrained(int var1) {
      this.NumLevelsTrained = var1;
   }

   public int getMaxLevelTrained() {
      return this.getLvlSkillTrained() + this.getNumLevelsTrained() - 1;
   }

   public String getSkillTrained() {
      return this.SkillTrained;
   }

   public void setSkillTrained(String var1) {
      this.SkillTrained = var1;
   }

   public int getAlreadyReadPages() {
      return this.alreadyReadPages;
   }

   public void setAlreadyReadPages(int var1) {
      this.alreadyReadPages = var1;
   }

   public boolean canBeWrite() {
      return this.canBeWrite;
   }

   public void setCanBeWrite(boolean var1) {
      this.canBeWrite = var1;
   }

   public HashMap getCustomPages() {
      if (this.customPages == null) {
         this.customPages = new HashMap();
         this.customPages.put(1, "");
      }

      return this.customPages;
   }

   public void setCustomPages(HashMap var1) {
      this.customPages = var1;
   }

   public void addPage(Integer var1, String var2) {
      if (this.customPages == null) {
         this.customPages = new HashMap();
      }

      this.customPages.put(var1, var2);
   }

   public String seePage(Integer var1) {
      if (this.customPages == null) {
         this.customPages = new HashMap();
         this.customPages.put(1, "");
      }

      return (String)this.customPages.get(var1);
   }

   public String getLockedBy() {
      return this.lockedBy;
   }

   public void setLockedBy(String var1) {
      this.lockedBy = var1;
   }

   public int getPageToWrite() {
      return this.pageToWrite;
   }

   public void setPageToWrite(int var1) {
      this.pageToWrite = var1;
   }

   public List getTeachedRecipes() {
      return this.teachedRecipes;
   }

   public void setTeachedRecipes(List var1) {
      this.teachedRecipes = var1;
   }
}
