package zombie.inventory.types;

import fmod.fmod.FMODManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoPlayer;
import zombie.characters.SurvivorDesc;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemSoundManager;
import zombie.inventory.ItemType;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoFireplace;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;
import zombie.util.StringUtils;
import zombie.util.io.BitHeader;
import zombie.util.io.BitHeaderRead;
import zombie.util.io.BitHeaderWrite;

public final class Food extends InventoryItem {
   protected boolean bBadCold = false;
   protected boolean bGoodHot = false;
   private static final float MIN_HEAT = 0.2F;
   private static final float MAX_HEAT = 3.0F;
   protected float Heat = 1.0F;
   protected float endChange = 0.0F;
   protected float hungChange = 0.0F;
   protected String useOnConsume = null;
   protected boolean rotten = false;
   protected boolean bDangerousUncooked = false;
   protected int LastCookMinute = 0;
   public float thirstChange = 0.0F;
   public boolean Poison = false;
   private List ReplaceOnCooked = null;
   private float baseHunger = 0.0F;
   public ArrayList spices = null;
   private boolean isSpice = false;
   private int poisonDetectionLevel = -1;
   private Integer PoisonLevelForRecipe = 0;
   private int UseForPoison = 0;
   private int PoisonPower = 0;
   private String FoodType = null;
   private String CustomEatSound = null;
   private boolean RemoveNegativeEffectOnCooked = false;
   private String Chef = null;
   private String OnCooked = null;
   private String WorldTextureCooked;
   private String WorldTextureRotten;
   private String WorldTextureOverdone;
   private int fluReduction = 0;
   private int ReduceFoodSickness = 0;
   private float painReduction = 0.0F;
   private String HerbalistType;
   private float carbohydrates = 0.0F;
   private float lipids = 0.0F;
   private float proteins = 0.0F;
   private float calories = 0.0F;
   private boolean packaged = false;
   private float freezingTime = 0.0F;
   private boolean frozen = false;
   private boolean canBeFrozen = true;
   protected float LastFrozenUpdate = -1.0F;
   public static final float FreezerAgeMultiplier = 0.02F;
   private String replaceOnRotten = null;
   private boolean forceFoodTypeAsName = false;
   private float rottenTime = 0.0F;
   private float compostTime = 0.0F;
   private String onEat = null;
   private boolean badInMicrowave = false;
   private boolean cookedInMicrowave = false;
   private long m_cookingSound = 0L;
   private int m_cookingParameter = -1;
   private static final int COOKING_STATE_COOKING = 0;
   private static final int COOKING_STATE_BURNING = 1;

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Food";
   }

   public Food(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      Texture.WarnFailFindTexture = false;
      this.texturerotten = Texture.trygetTexture(var4 + "Rotten");
      this.textureCooked = Texture.trygetTexture(var4 + "Cooked");
      this.textureBurnt = Texture.trygetTexture(var4 + "Overdone");
      String var5 = "Overdone.png";
      if (this.textureBurnt == null) {
         this.textureBurnt = Texture.trygetTexture(var4 + "Burnt");
         if (this.textureBurnt != null) {
            var5 = "Burnt.png";
         }
      }

      String var6 = "Rotten.png";
      if (this.texturerotten == null) {
         this.texturerotten = Texture.trygetTexture(var4 + "Spoiled");
         if (this.texturerotten != null) {
            var6 = "Spoiled.png";
         }
      }

      Texture.WarnFailFindTexture = true;
      if (this.texturerotten == null) {
         this.texturerotten = this.texture;
      }

      if (this.textureCooked == null) {
         this.textureCooked = this.texture;
      }

      if (this.textureBurnt == null) {
         this.textureBurnt = this.texture;
      }

      this.WorldTextureCooked = this.WorldTexture.replace(".png", "Cooked.png");
      this.WorldTextureOverdone = this.WorldTexture.replace(".png", var5);
      this.WorldTextureRotten = this.WorldTexture.replace(".png", var6);
      this.cat = ItemType.Food;
   }

   public Food(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
      String var5 = var4.ItemName;
      Texture.WarnFailFindTexture = false;
      this.texture = var4.NormalTexture;
      if (var4.SpecialTextures.size() == 0) {
         boolean var6 = false;
      }

      if (var4.SpecialTextures.size() > 0) {
         this.texturerotten = (Texture)var4.SpecialTextures.get(0);
      }

      if (var4.SpecialTextures.size() > 1) {
         this.textureCooked = (Texture)var4.SpecialTextures.get(1);
      }

      if (var4.SpecialTextures.size() > 2) {
         this.textureBurnt = (Texture)var4.SpecialTextures.get(2);
      }

      Texture.WarnFailFindTexture = true;
      if (this.texturerotten == null) {
         this.texturerotten = this.texture;
      }

      if (this.textureCooked == null) {
         this.textureCooked = this.texture;
      }

      if (this.textureBurnt == null) {
         this.textureBurnt = this.texture;
      }

      if (var4.SpecialWorldTextureNames.size() > 0) {
         this.WorldTextureRotten = (String)var4.SpecialWorldTextureNames.get(0);
      }

      if (var4.SpecialWorldTextureNames.size() > 1) {
         this.WorldTextureCooked = (String)var4.SpecialWorldTextureNames.get(1);
      }

      if (var4.SpecialWorldTextureNames.size() > 2) {
         this.WorldTextureOverdone = (String)var4.SpecialWorldTextureNames.get(2);
      }

      this.cat = ItemType.Food;
   }

   public boolean IsFood() {
      return true;
   }

   public int getSaveType() {
      return Item.Type.Food.ordinal();
   }

   public void update() {
      ItemContainer var1 = this.getOutermostContainer();
      if (var1 != null) {
         float var2 = var1.getTemprature();
         if (this.Heat > var2) {
            this.Heat -= 0.001F * GameTime.instance.getMultiplier();
            if (this.Heat < Math.max(0.2F, var2)) {
               this.Heat = Math.max(0.2F, var2);
            }
         }

         if (this.Heat < var2) {
            this.Heat += var2 / 1000.0F * GameTime.instance.getMultiplier();
            if (this.Heat > Math.min(3.0F, var2)) {
               this.Heat = Math.min(3.0F, var2);
            }
         }

         int var3;
         float var4;
         if (this.IsCookable && !this.isFrozen()) {
            if (this.Heat > 1.6F) {
               var3 = GameTime.getInstance().getMinutes();
               if (var3 != this.LastCookMinute) {
                  this.LastCookMinute = var3;
                  var4 = this.Heat / 1.5F;
                  if (var1.getTemprature() <= 1.6F) {
                     var4 *= 0.05F;
                  }

                  this.CookingTime += var4;
                  if (this.shouldPlayCookingSound()) {
                     ItemSoundManager.addItem(this);
                  }

                  if (this.isTaintedWater() && this.CookingTime > Math.min(this.MinutesToCook, 10.0F)) {
                     this.setTaintedWater(false);
                  }

                  if (!this.isCooked() && !this.Burnt && this.CookingTime > this.MinutesToCook) {
                     int var5;
                     if (this.getReplaceOnCooked() != null) {
                        for(var5 = 0; var5 < this.getReplaceOnCooked().size(); ++var5) {
                           InventoryItem var8 = this.container.AddItem((String)this.getReplaceOnCooked().get(var5));
                           if (var8 != null) {
                              var8.copyConditionModData(this);
                              if (var8 instanceof Food && ((Food)var8).isBadInMicrowave() && this.container.isMicrowave()) {
                                 var8.setUnhappyChange(5.0F);
                                 var8.setBoredomChange(5.0F);
                                 ((Food)var8).cookedInMicrowave = true;
                              }
                           }
                        }

                        this.container.Remove((InventoryItem)this);
                        IsoWorld.instance.CurrentCell.addToProcessItemsRemove((InventoryItem)this);
                        return;
                     }

                     this.setCooked(true);
                     if (this.getScriptItem().RemoveUnhappinessWhenCooked) {
                        this.setUnhappyChange(0.0F);
                     }

                     if (this.type.equals("RicePot") || this.type.equals("PastaPot") || this.type.equals("RicePan") || this.type.equals("PastaPan") || this.type.equals("WaterPotRice") || this.type.equals("WaterPotPasta") || this.type.equals("WaterSaucepanRice") || this.type.equals("WaterSaucepanPasta") || this.type.equals("RiceBowl") || this.type.equals("PastaBowl")) {
                        this.setAge(0.0F);
                        this.setOffAge(1);
                        this.setOffAgeMax(2);
                     }

                     if (this.isRemoveNegativeEffectOnCooked()) {
                        if (this.thirstChange > 0.0F) {
                           this.setThirstChange(0.0F);
                        }

                        if (this.unhappyChange > 0.0F) {
                           this.setUnhappyChange(0.0F);
                        }

                        if (this.boredomChange > 0.0F) {
                           this.setBoredomChange(0.0F);
                        }
                     }

                     if (this.getOnCooked() != null) {
                        LuaManager.caller.protectedCall(LuaManager.thread, LuaManager.env.rawget(this.getOnCooked()), this);
                     }

                     if (this.isBadInMicrowave() && this.container.isMicrowave()) {
                        this.setUnhappyChange(5.0F);
                        this.setBoredomChange(5.0F);
                        this.cookedInMicrowave = true;
                     }

                     if (this.Chef != null && !this.Chef.isEmpty()) {
                        for(var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
                           IsoPlayer var6 = IsoPlayer.players[var5];
                           if (var6 != null && !var6.isDead() && this.Chef.equals(var6.getFullName())) {
                              var6.getXp().AddXP(PerkFactory.Perks.Cooking, 10.0F);
                              break;
                           }
                        }
                     }
                  }

                  if (this.CookingTime > this.MinutesToBurn) {
                     this.Burnt = true;
                     this.setCooked(false);
                  }

                  if (GameTime.instance.NightsSurvived < SandboxOptions.instance.getElecShutModifier() && this.Burnt && this.CookingTime >= 50.0F && this.CookingTime >= this.MinutesToCook * 2.0F + this.MinutesToBurn / 2.0F && Rand.Next(Rand.AdjustForFramerate(200)) == 0) {
                     boolean var7 = this.container != null && this.container.getParent() != null && this.container.getParent().getName() != null && this.container.getParent().getName().equals("Campfire");
                     if (!var7 && this.container != null && this.container.getParent() != null && this.container.getParent() instanceof IsoFireplace) {
                        var7 = true;
                     }

                     if (this.container != null && this.container.SourceGrid != null && !var7) {
                        IsoFireManager.StartFire(this.container.SourceGrid.getCell(), this.container.SourceGrid, true, 500000);
                        this.IsCookable = false;
                     }
                  }
               }
            }
         } else if (this.isTaintedWater() && this.Heat > 1.6F && !this.isFrozen()) {
            var3 = GameTime.getInstance().getMinutes();
            if (var3 != this.LastCookMinute) {
               this.LastCookMinute = var3;
               var4 = 1.0F;
               if (var1.getTemprature() <= 1.6F) {
                  var4 = (float)((double)var4 * 0.2D);
               }

               this.CookingTime += var4;
               if (this.CookingTime > 10.0F) {
                  this.setTaintedWater(false);
               }
            }
         }
      }

      this.updateRotting(var1);
   }

   public void updateSound(BaseSoundEmitter var1) {
      if (this.shouldPlayCookingSound()) {
         if (var1.isPlaying(this.m_cookingSound)) {
            this.setCookingParameter(var1);
            return;
         }

         ItemContainer var2 = this.getOutermostContainer();
         IsoGridSquare var3 = var2.getParent().getSquare();
         var1.setPos((float)var3.getX() + 0.5F, (float)var3.getY() + 0.5F, (float)var3.getZ());
         this.m_cookingSound = var1.playSoundImpl(this.getCookingSound(), (IsoObject)null);
         this.setCookingParameter(var1);
      } else {
         var1.stopOrTriggerSound(this.m_cookingSound);
         this.m_cookingSound = 0L;
         this.m_cookingParameter = -1;
         ItemSoundManager.removeItem(this);
      }

   }

   private boolean shouldPlayCookingSound() {
      if (GameServer.bServer) {
         return false;
      } else if (StringUtils.isNullOrWhitespace(this.getCookingSound())) {
         return false;
      } else {
         ItemContainer var1 = this.getOutermostContainer();
         if (var1 != null && var1.getParent() != null && var1.getParent().getObjectIndex() != -1 && !(var1.getTemprature() <= 1.6F)) {
            return this.isCookable() && !this.isFrozen() && this.getHeat() > 1.6F;
         } else {
            return false;
         }
      }
   }

   private void setCookingParameter(BaseSoundEmitter var1) {
      boolean var2 = this.CookingTime > this.MinutesToCook;
      int var3 = var2 ? 1 : 0;
      if (var3 != this.m_cookingParameter) {
         this.m_cookingParameter = var3;
         var1.setParameterValue(this.m_cookingSound, FMODManager.instance.getParameterDescription("CookingState"), (float)this.m_cookingParameter);
      }

   }

   private void updateRotting(ItemContainer var1) {
      if ((double)this.OffAgeMax != 1.0E9D) {
         if (!GameClient.bClient || this.isInLocalPlayerInventory()) {
            if (!GameServer.bServer || this.container == null || this.getOutermostContainer() == this.container) {
               if (this.replaceOnRotten != null && !this.replaceOnRotten.isEmpty()) {
                  this.updateAge();
                  if (this.isRotten()) {
                     InventoryItem var2 = InventoryItemFactory.CreateItem(this.getModule() + "." + this.replaceOnRotten, this);
                     if (var2 == null) {
                        String var10001 = this.replaceOnRotten;
                        DebugLog.General.warn("ReplaceOnRotten = " + var10001 + " doesn't exist for " + this.getFullType());
                        this.destroyThisItem();
                        return;
                     }

                     var2.setAge(this.getAge());
                     IsoWorldInventoryObject var3 = this.getWorldItem();
                     if (var3 != null && var3.getSquare() != null) {
                        IsoGridSquare var4 = var3.getSquare();
                        if (!GameServer.bServer) {
                           var3.item = var2;
                           var2.setWorldItem(var3);
                           var3.updateSprite();
                           IsoWorld.instance.CurrentCell.addToProcessItemsRemove((InventoryItem)this);
                           LuaEventManager.triggerEvent("OnContainerUpdate");
                           return;
                        }

                        var4.AddWorldInventoryItem(var2, var3.xoff, var3.yoff, var3.zoff, true);
                     } else if (this.container != null) {
                        this.container.AddItem(var2);
                        if (GameServer.bServer) {
                           GameServer.sendAddItemToContainer(this.container, var2);
                        }
                     }

                     this.destroyThisItem();
                     return;
                  }
               }

               if (SandboxOptions.instance.DaysForRottenFoodRemoval.getValue() >= 0) {
                  if (var1 != null && var1.parent instanceof IsoCompost) {
                     return;
                  }

                  this.updateAge();
                  if (this.getAge() > (float)(this.getOffAgeMax() + SandboxOptions.instance.DaysForRottenFoodRemoval.getValue())) {
                     this.destroyThisItem();
                     return;
                  }
               }

            }
         }
      }
   }

   public void updateAge() {
      ItemContainer var1 = this.getOutermostContainer();
      this.updateFreezing(var1);
      boolean var2 = false;
      if (var1 != null && var1.getSourceGrid() != null && var1.getSourceGrid().haveElectricity()) {
         var2 = true;
      }

      float var3 = (float)GameTime.getInstance().getWorldAgeHours();
      float var4 = 0.2F;
      if (SandboxOptions.instance.FridgeFactor.getValue() == 1) {
         var4 = 0.4F;
      } else if (SandboxOptions.instance.FridgeFactor.getValue() == 2) {
         var4 = 0.3F;
      } else if (SandboxOptions.instance.FridgeFactor.getValue() == 4) {
         var4 = 0.1F;
      } else if (SandboxOptions.instance.FridgeFactor.getValue() == 5) {
         var4 = 0.03F;
      }

      if (this.LastAged < 0.0F) {
         this.LastAged = var3;
      } else if (this.LastAged > var3) {
         this.LastAged = var3;
      }

      if (var3 > this.LastAged) {
         double var5 = (double)(var3 - this.LastAged);
         if (var1 != null && this.Heat != var1.getTemprature()) {
            if (var5 < 0.3333333432674408D) {
               if (!IsoWorld.instance.getCell().getProcessItems().contains(this)) {
                  this.Heat = GameTime.instance.Lerp(this.Heat, var1.getTemprature(), (float)var5 / 0.33333334F);
                  IsoWorld.instance.getCell().addToProcessItems((InventoryItem)this);
               }
            } else {
               this.Heat = var1.getTemprature();
            }
         }

         float var7;
         if (this.isFrozen()) {
            var5 *= 0.019999999552965164D;
         } else if (var1 != null && (var1.getType().equals("fridge") || var1.getType().equals("freezer"))) {
            if (var2) {
               var5 *= (double)var4;
            } else if (SandboxOptions.instance.getElecShutModifier() > -1 && this.LastAged < (float)(SandboxOptions.instance.getElecShutModifier() * 24)) {
               var7 = Math.min((float)(SandboxOptions.instance.getElecShutModifier() * 24), var3);
               var5 = (double)((var7 - this.LastAged) * var4);
               if (var3 > (float)(SandboxOptions.instance.getElecShutModifier() * 24)) {
                  var5 += (double)(var3 - (float)(SandboxOptions.instance.getElecShutModifier() * 24));
               }
            }
         }

         var7 = 1.0F;
         if (SandboxOptions.instance.FoodRotSpeed.getValue() == 1) {
            var7 = 1.7F;
         } else if (SandboxOptions.instance.FoodRotSpeed.getValue() == 2) {
            var7 = 1.4F;
         } else if (SandboxOptions.instance.FoodRotSpeed.getValue() == 4) {
            var7 = 0.7F;
         } else if (SandboxOptions.instance.FoodRotSpeed.getValue() == 5) {
            var7 = 0.4F;
         }

         boolean var8 = !this.Burnt && this.OffAge < 1000000000 && this.Age < (float)this.OffAge;
         boolean var9 = !this.Burnt && this.OffAgeMax < 1000000000 && this.Age >= (float)this.OffAgeMax;
         this.Age = (float)((double)this.Age + var5 * (double)var7 / 24.0D);
         this.LastAged = var3;
         boolean var10 = !this.Burnt && this.OffAge < 1000000000 && this.Age < (float)this.OffAge;
         boolean var11 = !this.Burnt && this.OffAgeMax < 1000000000 && this.Age >= (float)this.OffAgeMax;
         if (!GameServer.bServer && (var8 != var10 || var9 != var11)) {
            LuaEventManager.triggerEvent("OnContainerUpdate", this);
         }
      }

   }

   public void setAutoAge() {
      ItemContainer var1 = this.getOutermostContainer();
      float var2 = (float)GameTime.getInstance().getWorldAgeHours() / 24.0F;
      var2 += (float)((SandboxOptions.instance.TimeSinceApo.getValue() - 1) * 30);
      float var3 = var2;
      boolean var4 = false;
      if (var1 != null && var1.getParent() != null && var1.getParent().getSprite() != null) {
         var4 = var1.getParent().getSprite().getProperties().Is("IsFridge");
      }

      int var5;
      float var6;
      if (var1 != null && (var4 || var1.getType().equals("fridge") || var1.getType().equals("freezer"))) {
         var5 = SandboxOptions.instance.ElecShutModifier.getValue();
         if (var5 > -1) {
            var6 = Math.min((float)var5, var2);
            int var7 = SandboxOptions.instance.FridgeFactor.getValue();
            float var8 = 0.2F;
            if (var7 == 1) {
               var8 = 0.4F;
            } else if (var7 == 2) {
               var8 = 0.3F;
            } else if (var7 == 4) {
               var8 = 0.1F;
            } else if (var7 == 5) {
               var8 = 0.03F;
            }

            if (!var1.getType().equals("fridge") && this.canBeFrozen() && !var4) {
               float var9 = var6;
               float var10 = 100.0F;
               if (var2 > var6) {
                  float var11 = (var2 - var6) * 24.0F;
                  float var12 = 1440.0F / GameTime.getInstance().getMinutesPerDay() * 60.0F * 5.0F;
                  float var13 = 0.0095999995F;
                  var10 -= var13 * var12 * var11;
                  if (var10 > 0.0F) {
                     var9 = var6 + var11 / 24.0F;
                  } else {
                     float var14 = 100.0F / (var13 * var12);
                     var9 = var6 + var14 / 24.0F;
                     var10 = 0.0F;
                  }
               }

               var3 = var2 - var9;
               var3 += var9 * 0.02F;
               this.setFreezingTime(var10);
            } else {
               var3 = var2 - var6;
               var3 += var6 * var8;
            }
         }
      }

      var5 = SandboxOptions.instance.FoodRotSpeed.getValue();
      var6 = 1.0F;
      if (var5 == 1) {
         var6 = 1.7F;
      } else if (var5 == 2) {
         var6 = 1.4F;
      } else if (var5 == 4) {
         var6 = 0.7F;
      } else if (var5 == 5) {
         var6 = 0.4F;
      }

      this.Age = var3 * var6;
      this.LastAged = (float)GameTime.getInstance().getWorldAgeHours();
      this.LastFrozenUpdate = this.LastAged;
      if (var1 != null) {
         this.setHeat(var1.getTemprature());
      }

   }

   public void updateFreezing(ItemContainer var1) {
      float var2 = (float)GameTime.getInstance().getWorldAgeHours();
      if (this.LastFrozenUpdate < 0.0F) {
         this.LastFrozenUpdate = var2;
      } else if (this.LastFrozenUpdate > var2) {
         this.LastFrozenUpdate = var2;
      }

      if (var2 > this.LastFrozenUpdate) {
         float var3 = var2 - this.LastFrozenUpdate;
         float var4 = 4.0F;
         float var5 = 1.5F;
         if (this.isFreezing()) {
            this.setFreezingTime(this.getFreezingTime() + var3 / var4 * 100.0F);
         }

         if (this.isThawing()) {
            float var6 = var5;
            if (var1 != null && "fridge".equals(var1.getType()) && var1.isPowered()) {
               var6 = var5 * 2.0F;
            }

            if (var1 != null && var1.getTemprature() > 1.0F) {
               var6 /= 6.0F;
            }

            this.setFreezingTime(this.getFreezingTime() - var3 / var6 * 100.0F);
         }

         this.LastFrozenUpdate = var2;
      }

   }

   public float getActualWeight() {
      float var3;
      if (this.haveExtraItems()) {
         float var7 = this.getHungChange();
         float var8 = this.getBaseHunger();
         var3 = var8 == 0.0F ? 0.0F : var7 / var8;
         float var4 = 0.0F;
         if (this.getReplaceOnUse() != null) {
            String var5 = this.getReplaceOnUseFullType();
            Item var6 = ScriptManager.instance.getItem(var5);
            if (var6 != null) {
               var4 = var6.getActualWeight();
            }
         }

         float var9 = super.getActualWeight() + this.getExtraItemsWeight();
         float var10 = (var9 - var4) * var3 + var4;
         return var10;
      } else {
         if (this.getReplaceOnUse() != null) {
            String var1 = this.getReplaceOnUseFullType();
            Item var2 = ScriptManager.instance.getItem(var1);
            if (var2 != null) {
               var3 = 1.0F;
               if (this.getScriptItem().getHungerChange() < 0.0F) {
                  var3 = this.getHungChange() * 100.0F / this.getScriptItem().getHungerChange();
               } else if (this.getScriptItem().getThirstChange() < 0.0F) {
                  var3 = this.getThirstChange() * 100.0F / this.getScriptItem().getThirstChange();
               }

               return (this.getScriptItem().getActualWeight() - var2.getActualWeight()) * var3 + var2.getActualWeight();
            }
         }

         return super.getActualWeight();
      }
   }

   public float getWeight() {
      return this.getReplaceOnUse() != null ? this.getActualWeight() : super.getWeight();
   }

   public boolean CanStack(InventoryItem var1) {
      return false;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putFloat(this.Age);
      var1.putFloat(this.LastAged);
      BitHeaderWrite var3 = BitHeader.allocWrite(BitHeader.HeaderSize.Byte, var1);
      if (this.calories != 0.0F || this.proteins != 0.0F || this.lipids != 0.0F || this.carbohydrates != 0.0F) {
         var3.addFlags(1);
         var1.putFloat(this.calories);
         var1.putFloat(this.proteins);
         var1.putFloat(this.lipids);
         var1.putFloat(this.carbohydrates);
      }

      if (this.hungChange != 0.0F) {
         var3.addFlags(2);
         var1.putFloat(this.hungChange);
      }

      if (this.baseHunger != 0.0F) {
         var3.addFlags(4);
         var1.putFloat(this.baseHunger);
      }

      if (this.unhappyChange != 0.0F) {
         var3.addFlags(8);
         var1.putFloat(this.unhappyChange);
      }

      if (this.boredomChange != 0.0F) {
         var3.addFlags(16);
         var1.putFloat(this.boredomChange);
      }

      if (this.thirstChange != 0.0F) {
         var3.addFlags(32);
         var1.putFloat(this.thirstChange);
      }

      BitHeaderWrite var4 = BitHeader.allocWrite(BitHeader.HeaderSize.Integer, var1);
      if (this.Heat != 1.0F) {
         var4.addFlags(1);
         var1.putFloat(this.Heat);
      }

      if ((float)this.LastCookMinute != 0.0F) {
         var4.addFlags(2);
         var1.putInt(this.LastCookMinute);
      }

      if (this.CookingTime != 0.0F) {
         var4.addFlags(4);
         var1.putFloat(this.CookingTime);
      }

      if (this.Cooked) {
         var4.addFlags(8);
      }

      if (this.Burnt) {
         var4.addFlags(16);
      }

      if (this.IsCookable) {
         var4.addFlags(32);
      }

      if (this.bDangerousUncooked) {
         var4.addFlags(64);
      }

      if (this.poisonDetectionLevel != -1) {
         var4.addFlags(128);
         var1.put((byte)this.poisonDetectionLevel);
      }

      if (this.spices != null) {
         var4.addFlags(256);
         var1.put((byte)this.spices.size());
         Iterator var5 = this.spices.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            GameWindow.WriteString(var1, var6);
         }
      }

      if (this.PoisonPower != 0) {
         var4.addFlags(512);
         var1.put((byte)this.PoisonPower);
      }

      if (this.Chef != null) {
         var4.addFlags(1024);
         GameWindow.WriteString(var1, this.Chef);
      }

      if ((double)this.OffAge != 1.0E9D) {
         var4.addFlags(2048);
         var1.putInt(this.OffAge);
      }

      if ((double)this.OffAgeMax != 1.0E9D) {
         var4.addFlags(4096);
         var1.putInt(this.OffAgeMax);
      }

      if (this.painReduction != 0.0F) {
         var4.addFlags(8192);
         var1.putFloat(this.painReduction);
      }

      if (this.fluReduction != 0) {
         var4.addFlags(16384);
         var1.putInt(this.fluReduction);
      }

      if (this.ReduceFoodSickness != 0) {
         var4.addFlags(32768);
         var1.putInt(this.ReduceFoodSickness);
      }

      if (this.Poison) {
         var4.addFlags(65536);
      }

      if (this.UseForPoison != 0) {
         var4.addFlags(131072);
         var1.putShort((short)this.UseForPoison);
      }

      if (this.freezingTime != 0.0F) {
         var4.addFlags(262144);
         var1.putFloat(this.freezingTime);
      }

      if (this.isFrozen()) {
         var4.addFlags(524288);
      }

      if (this.LastFrozenUpdate != 0.0F) {
         var4.addFlags(1048576);
         var1.putFloat(this.LastFrozenUpdate);
      }

      if (this.rottenTime != 0.0F) {
         var4.addFlags(2097152);
         var1.putFloat(this.rottenTime);
      }

      if (this.compostTime != 0.0F) {
         var4.addFlags(4194304);
         var1.putFloat(this.compostTime);
      }

      if (this.cookedInMicrowave) {
         var4.addFlags(8388608);
      }

      if (!var4.equals(0)) {
         var3.addFlags(64);
         var4.write();
      } else {
         var1.position(var4.getStartPosition());
      }

      var3.write();
      var3.release();
      var4.release();
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      this.Age = var1.getFloat();
      this.LastAged = var1.getFloat();
      BitHeaderRead var3 = BitHeader.allocRead(BitHeader.HeaderSize.Byte, var1);
      if (!var3.equals(0)) {
         if (var3.hasFlags(1)) {
            this.calories = var1.getFloat();
            this.proteins = var1.getFloat();
            this.lipids = var1.getFloat();
            this.carbohydrates = var1.getFloat();
         }

         if (var3.hasFlags(2)) {
            this.hungChange = var1.getFloat();
         }

         if (var3.hasFlags(4)) {
            this.baseHunger = var1.getFloat();
         }

         if (var3.hasFlags(8)) {
            this.unhappyChange = var1.getFloat();
         }

         if (var3.hasFlags(16)) {
            this.boredomChange = var1.getFloat();
         }

         if (var3.hasFlags(32)) {
            this.thirstChange = var1.getFloat();
         }

         if (var3.hasFlags(64)) {
            BitHeaderRead var4 = BitHeader.allocRead(BitHeader.HeaderSize.Integer, var1);
            if (var4.hasFlags(1)) {
               this.Heat = var1.getFloat();
            }

            if (var4.hasFlags(2)) {
               this.LastCookMinute = var1.getInt();
            }

            if (var4.hasFlags(4)) {
               this.CookingTime = var1.getFloat();
            }

            this.Cooked = var4.hasFlags(8);
            this.Burnt = var4.hasFlags(16);
            this.IsCookable = var4.hasFlags(32);
            this.bDangerousUncooked = var4.hasFlags(64);
            if (var4.hasFlags(128)) {
               this.poisonDetectionLevel = var1.get();
            }

            if (var4.hasFlags(256)) {
               this.spices = new ArrayList();
               byte var5 = var1.get();

               for(int var6 = 0; var6 < var5; ++var6) {
                  String var7 = GameWindow.ReadString(var1);
                  this.spices.add(var7);
               }
            }

            if (var4.hasFlags(512)) {
               this.PoisonPower = var1.get();
            }

            if (var4.hasFlags(1024)) {
               this.Chef = GameWindow.ReadString(var1);
            }

            if (var4.hasFlags(2048)) {
               this.OffAge = var1.getInt();
            }

            if (var4.hasFlags(4096)) {
               this.OffAgeMax = var1.getInt();
            }

            if (var4.hasFlags(8192)) {
               this.painReduction = var1.getFloat();
            }

            if (var4.hasFlags(16384)) {
               this.fluReduction = var1.getInt();
            }

            if (var4.hasFlags(32768)) {
               this.ReduceFoodSickness = var1.getInt();
            }

            this.Poison = var4.hasFlags(65536);
            if (var4.hasFlags(131072)) {
               this.UseForPoison = var1.getShort();
            }

            if (var4.hasFlags(262144)) {
               this.freezingTime = var1.getFloat();
            }

            this.setFrozen(var4.hasFlags(524288));
            if (var4.hasFlags(1048576)) {
               this.LastFrozenUpdate = var1.getFloat();
            }

            if (var4.hasFlags(2097152)) {
               this.rottenTime = var1.getFloat();
            }

            if (var4.hasFlags(4194304)) {
               this.compostTime = var1.getFloat();
            }

            this.cookedInMicrowave = var4.hasFlags(8388608);
            var4.release();
         }
      }

      var3.release();
      if (GameServer.bServer && this.LastAged == -1.0F) {
         this.LastAged = (float)GameTime.getInstance().getWorldAgeHours();
      }

   }

   public boolean finishupdate() {
      if (this.container == null && (this.getWorldItem() == null || this.getWorldItem().getSquare() == null)) {
         return true;
      } else if (this.IsCookable) {
         return false;
      } else if (this.container != null && (this.Heat != this.container.getTemprature() || this.container.isTemperatureChanging())) {
         return false;
      } else if (this.isTaintedWater() && this.container != null && this.container.getTemprature() > 1.0F) {
         return false;
      } else {
         if ((!GameClient.bClient || this.isInLocalPlayerInventory()) && (double)this.OffAgeMax != 1.0E9D) {
            if (this.replaceOnRotten != null && !this.replaceOnRotten.isEmpty()) {
               return false;
            }

            if (SandboxOptions.instance.DaysForRottenFoodRemoval.getValue() != -1) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean shouldUpdateInWorld() {
      if (!GameClient.bClient && (double)this.OffAgeMax != 1.0E9D) {
         if (this.replaceOnRotten != null && !this.replaceOnRotten.isEmpty()) {
            return true;
         }

         if (SandboxOptions.instance.DaysForRottenFoodRemoval.getValue() != -1) {
            return true;
         }
      }

      return false;
   }

   public String getName() {
      String var1 = "";
      if (this.Burnt) {
         var1 = var1 + this.BurntString + " ";
      } else if (this.OffAge < 1000000000 && this.Age < (float)this.OffAge) {
         var1 = var1 + this.FreshString + " ";
      } else if (this.OffAgeMax < 1000000000 && this.Age >= (float)this.OffAgeMax) {
         var1 = var1 + this.OffString + " ";
      }

      if (this.isCooked() && !this.Burnt) {
         var1 = var1 + this.CookedString + " ";
      } else if (this.IsCookable && !this.Burnt) {
         var1 = var1 + this.UnCookedString + " ";
      }

      if (this.isFrozen()) {
         var1 = var1 + this.FrozenString + " ";
      }

      var1 = var1.trim();
      return var1.isEmpty() ? this.name : Translator.getText("IGUI_FoodNaming", var1, this.name);
   }

   public void DoTooltip(ObjectTooltip var1, ObjectTooltip.Layout var2) {
      ObjectTooltip.LayoutItem var3;
      int var4;
      if (this.getHungerChange() != 0.0F) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Hunger") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var4 = (int)(this.getHungerChange() * 100.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getThirstChange() != 0.0F) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Thirst") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var4 = (int)(this.getThirstChange() * 100.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getEnduranceChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)(this.getEnduranceChange() * 100.0F);
         var3.setLabel(Translator.getText("Tooltip_food_Endurance") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, true);
      }

      if (this.getStressChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)(this.getStressChange() * 100.0F);
         var3.setLabel(Translator.getText("Tooltip_food_Stress") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getBoredomChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)this.getBoredomChange();
         var3.setLabel(Translator.getText("Tooltip_food_Boredom") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      if (this.getUnhappyChange() != 0.0F) {
         var3 = var2.addItem();
         var4 = (int)this.getUnhappyChange();
         var3.setLabel(Translator.getText("Tooltip_food_Unhappiness") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRight(var4, false);
      }

      float var5;
      float var6;
      float var7;
      float var8;
      float var9;
      float var10;
      float var11;
      float var16;
      if (this.isIsCookable() && !this.isFrozen() && !this.Burnt && (double)this.getHeat() > 1.6D) {
         var16 = this.getCookingTime();
         var5 = this.getMinutesToCook();
         var6 = this.getMinutesToBurn();
         var7 = var16 / var5;
         var8 = 0.0F;
         var9 = 0.6F;
         var10 = 0.0F;
         var11 = 0.7F;
         float var12 = 1.0F;
         float var13 = 1.0F;
         float var14 = 0.8F;
         String var15 = Translator.getText("IGUI_invpanel_Cooking");
         if (var16 > var5) {
            var15 = Translator.getText("IGUI_invpanel_Burning");
            var12 = 1.0F;
            var13 = 0.0F;
            var14 = 0.0F;
            var7 = (var16 - var5) / (var6 - var5);
            var8 = 0.6F;
            var9 = 0.0F;
            var10 = 0.0F;
         }

         var3 = var2.addItem();
         var3.setLabel(var15 + ": ", var12, var13, var14, 1.0F);
         var3.setProgress(var7, var8, var9, var10, var11);
      }

      if (this.getFreezingTime() < 100.0F && this.getFreezingTime() > 0.0F) {
         var16 = this.getFreezingTime() / 100.0F;
         var5 = 0.0F;
         var6 = 0.6F;
         var7 = 0.0F;
         var8 = 0.7F;
         var9 = 1.0F;
         var10 = 1.0F;
         var11 = 0.8F;
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("IGUI_invpanel_FreezingTime") + ": ", var9, var10, var11, 1.0F);
         var3.setProgress(var16, var5, var6, var7, var8);
      }

      if (Core.bDebug && DebugOptions.instance.TooltipInfo.getValue() || this.isPackaged() || var1.getCharacter() != null && (var1.getCharacter().Traits.Nutritionist.isSet() || var1.getCharacter().Traits.Nutritionist2.isSet())) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Calories") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRightNoPlus(this.getCalories());
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Carbs") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRightNoPlus(this.getCarbohydrates());
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Prots") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRightNoPlus(this.getProteins());
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Fat") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
         var3.setValueRightNoPlus(this.getLipids());
      }

      if (this.getScriptItem().RemoveUnhappinessWhenCooked && !this.isCooked()) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_CookToRemoveUnhappiness"), 1.0F, 0.0F, 0.0F, 1.0F);
      }

      if (this.isbDangerousUncooked() && !this.isCooked() && !this.isBurnt()) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_Dangerous_uncooked"), 1.0F, 0.0F, 0.0F, 1.0F);
      }

      if ((this.isGoodHot() || this.isBadCold()) && this.Heat < 1.3F) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_BetterHot"), 1.0F, 0.9F, 0.9F, 1.0F);
      }

      if (this.cookedInMicrowave) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_food_CookedInMicrowave"), 1.0F, 0.9F, 0.9F, 1.0F);
      }

      if (Core.bDebug && DebugOptions.instance.TooltipInfo.getValue()) {
         var3 = var2.addItem();
         var3.setLabel("DBG: BaseHunger", 0.0F, 1.0F, 0.0F, 1.0F);
         var3.setValueRight((int)(this.getBaseHunger() * 100.0F), false);
         var3 = var2.addItem();
         var3.setLabel("DBG: Age", 0.0F, 1.0F, 0.0F, 1.0F);
         var3.setValueRightNoPlus(this.getAge() * 24.0F);
         if ((double)this.getOffAgeMax() != 1.0E9D) {
            var3 = var2.addItem();
            var3.setLabel("DBG: Age Fresh", 0.0F, 1.0F, 0.0F, 1.0F);
            var3.setValueRightNoPlus((float)this.getOffAge() * 24.0F);
            var3 = var2.addItem();
            var3.setLabel("DBG: Age Rotten", 0.0F, 1.0F, 0.0F, 1.0F);
            var3.setValueRightNoPlus(this.getOffAgeMax() * 24);
         }

         var3 = var2.addItem();
         var3.setLabel("DBG: Heat", 0.0F, 1.0F, 0.0F, 1.0F);
         var3.setValueRightNoPlus(this.getHeat());
         var3 = var2.addItem();
         var3.setLabel("DBG: Freeze Time", 0.0F, 1.0F, 0.0F, 1.0F);
         var3.setValueRightNoPlus(this.getFreezingTime());
         var3 = var2.addItem();
         var3.setLabel("DBG: Compost Time", 0.0F, 1.0F, 0.0F, 1.0F);
         var3.setValueRightNoPlus(this.getCompostTime());
      }

   }

   public float getEnduranceChange() {
      if (this.Burnt) {
         return this.endChange / 3.0F;
      } else if (this.Age >= (float)this.OffAge && this.Age < (float)this.OffAgeMax) {
         return this.endChange / 2.0F;
      } else {
         return this.isCooked() ? this.endChange * 2.0F : this.endChange;
      }
   }

   public float getUnhappyChange() {
      float var1 = this.unhappyChange;
      if (this.isFrozen() && !"Icecream".equals(this.getType())) {
         var1 += 30.0F;
      }

      if (this.Burnt) {
         var1 += 20.0F;
      }

      if (this.Age >= (float)this.OffAge && this.Age < (float)this.OffAgeMax) {
         var1 += 10.0F;
      }

      if (this.Age >= (float)this.OffAgeMax) {
         var1 += 20.0F;
      }

      if (this.isBadCold() && this.IsCookable && this.isCooked() && this.Heat < 1.3F) {
         var1 += 2.0F;
      }

      if (this.isGoodHot() && this.IsCookable && this.isCooked() && this.Heat > 1.3F) {
         var1 -= 2.0F;
      }

      return var1;
   }

   public float getBoredomChange() {
      float var1 = this.boredomChange;
      if (this.isFrozen() && !"Icecream".equals(this.getType())) {
         var1 += 30.0F;
      }

      if (this.Burnt) {
         var1 += 20.0F;
      }

      if (this.Age >= (float)this.OffAge && this.Age < (float)this.OffAgeMax) {
         var1 += 10.0F;
      }

      if (this.Age >= (float)this.OffAgeMax) {
         var1 += 20.0F;
      }

      return var1;
   }

   public float getHungerChange() {
      float var1 = this.hungChange;
      if (this.Burnt) {
         return var1 / 3.0F;
      } else if (this.Age >= (float)this.OffAge && this.Age < (float)this.OffAgeMax) {
         return var1 / 1.3F;
      } else if (this.Age >= (float)this.OffAgeMax) {
         return var1 / 2.2F;
      } else {
         return this.isCooked() ? var1 * 1.3F : var1;
      }
   }

   public float getStressChange() {
      if (this.Burnt) {
         return this.stressChange / 4.0F;
      } else if (this.Age >= (float)this.OffAge && this.Age < (float)this.OffAgeMax) {
         return this.stressChange / 1.3F;
      } else if (this.Age >= (float)this.OffAgeMax) {
         return this.stressChange / 2.0F;
      } else {
         return this.isCooked() ? this.stressChange * 1.3F : this.stressChange;
      }
   }

   public float getScore(SurvivorDesc var1) {
      float var2 = 0.0F;
      var2 -= this.getHungerChange() * 100.0F;
      return var2;
   }

   public boolean isBadCold() {
      return this.bBadCold;
   }

   public void setBadCold(boolean var1) {
      this.bBadCold = var1;
   }

   public boolean isGoodHot() {
      return this.bGoodHot;
   }

   public void setGoodHot(boolean var1) {
      this.bGoodHot = var1;
   }

   public boolean isCookedInMicrowave() {
      return this.cookedInMicrowave;
   }

   public float getHeat() {
      return this.Heat;
   }

   public float getInvHeat() {
      return this.Heat > 1.0F ? (this.Heat - 1.0F) / 2.0F : 1.0F - (this.Heat - 0.2F) / 0.8F;
   }

   public void setHeat(float var1) {
      this.Heat = var1;
   }

   public float getEndChange() {
      return this.endChange;
   }

   public void setEndChange(float var1) {
      this.endChange = var1;
   }

   /** @deprecated */
   @Deprecated
   public float getBaseHungChange() {
      return this.getHungChange();
   }

   public float getHungChange() {
      return this.hungChange;
   }

   public void setHungChange(float var1) {
      this.hungChange = var1;
   }

   public String getUseOnConsume() {
      return this.useOnConsume;
   }

   public void setUseOnConsume(String var1) {
      this.useOnConsume = var1;
   }

   public boolean isRotten() {
      return this.Age >= (float)this.OffAgeMax;
   }

   public boolean isFresh() {
      return this.Age < (float)this.OffAge;
   }

   public void setRotten(boolean var1) {
      this.rotten = var1;
   }

   public boolean isbDangerousUncooked() {
      return this.bDangerousUncooked;
   }

   public void setbDangerousUncooked(boolean var1) {
      this.bDangerousUncooked = var1;
   }

   public int getLastCookMinute() {
      return this.LastCookMinute;
   }

   public void setLastCookMinute(int var1) {
      this.LastCookMinute = var1;
   }

   public float getThirstChange() {
      float var1 = this.thirstChange;
      if (this.Burnt) {
         return var1 / 5.0F;
      } else {
         return this.isCooked() ? var1 / 2.0F : var1;
      }
   }

   public void setThirstChange(float var1) {
      this.thirstChange = var1;
   }

   public void setReplaceOnCooked(List var1) {
      this.ReplaceOnCooked = var1;
   }

   public List getReplaceOnCooked() {
      return this.ReplaceOnCooked;
   }

   public float getBaseHunger() {
      return this.baseHunger;
   }

   public void setBaseHunger(float var1) {
      this.baseHunger = var1;
   }

   public boolean isSpice() {
      return this.isSpice;
   }

   public void setSpice(boolean var1) {
      this.isSpice = var1;
   }

   public boolean isPoison() {
      return this.Poison;
   }

   public int getPoisonDetectionLevel() {
      return this.poisonDetectionLevel;
   }

   public void setPoisonDetectionLevel(int var1) {
      this.poisonDetectionLevel = var1;
   }

   public Integer getPoisonLevelForRecipe() {
      return this.PoisonLevelForRecipe;
   }

   public void setPoisonLevelForRecipe(Integer var1) {
      this.PoisonLevelForRecipe = var1;
   }

   public int getUseForPoison() {
      return this.UseForPoison;
   }

   public void setUseForPoison(int var1) {
      this.UseForPoison = var1;
   }

   public int getPoisonPower() {
      return this.PoisonPower;
   }

   public void setPoisonPower(int var1) {
      this.PoisonPower = var1;
   }

   public String getFoodType() {
      return this.FoodType;
   }

   public void setFoodType(String var1) {
      this.FoodType = var1;
   }

   public boolean isRemoveNegativeEffectOnCooked() {
      return this.RemoveNegativeEffectOnCooked;
   }

   public void setRemoveNegativeEffectOnCooked(boolean var1) {
      this.RemoveNegativeEffectOnCooked = var1;
   }

   public String getCookingSound() {
      return this.getScriptItem().getCookingSound();
   }

   public String getCustomEatSound() {
      return this.CustomEatSound;
   }

   public void setCustomEatSound(String var1) {
      this.CustomEatSound = var1;
   }

   public String getChef() {
      return this.Chef;
   }

   public void setChef(String var1) {
      this.Chef = var1;
   }

   public String getOnCooked() {
      return this.OnCooked;
   }

   public void setOnCooked(String var1) {
      this.OnCooked = var1;
   }

   public String getHerbalistType() {
      return this.HerbalistType;
   }

   public void setHerbalistType(String var1) {
      this.HerbalistType = var1;
   }

   public ArrayList getSpices() {
      return this.spices;
   }

   public void setSpices(ArrayList var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.spices == null) {
            this.spices = new ArrayList(var1);
         } else {
            this.spices.clear();
            this.spices.addAll(var1);
         }

      } else {
         if (this.spices != null) {
            this.spices.clear();
         }

      }
   }

   public Texture getTex() {
      if (this.Burnt) {
         return this.textureBurnt;
      } else if (this.Age >= (float)this.OffAgeMax) {
         return this.texturerotten;
      } else {
         return this.isCooked() ? this.textureCooked : super.getTex();
      }
   }

   public String getWorldTexture() {
      if (this.Burnt) {
         return this.WorldTextureOverdone;
      } else if (this.Age >= (float)this.OffAgeMax) {
         return this.WorldTextureRotten;
      } else {
         return this.isCooked() ? this.WorldTextureCooked : this.WorldTexture;
      }
   }

   public int getReduceFoodSickness() {
      return this.ReduceFoodSickness;
   }

   public void setReduceFoodSickness(int var1) {
      this.ReduceFoodSickness = var1;
   }

   public int getFluReduction() {
      return this.fluReduction;
   }

   public void setFluReduction(int var1) {
      this.fluReduction = var1;
   }

   public float getPainReduction() {
      return this.painReduction;
   }

   public void setPainReduction(float var1) {
      this.painReduction = var1;
   }

   public float getCarbohydrates() {
      return this.carbohydrates;
   }

   public void setCarbohydrates(float var1) {
      this.carbohydrates = var1;
   }

   public float getLipids() {
      return this.lipids;
   }

   public void setLipids(float var1) {
      this.lipids = var1;
   }

   public float getProteins() {
      return this.proteins;
   }

   public void setProteins(float var1) {
      this.proteins = var1;
   }

   public float getCalories() {
      return this.calories;
   }

   public void setCalories(float var1) {
      this.calories = var1;
   }

   public boolean isPackaged() {
      return this.packaged;
   }

   public void setPackaged(boolean var1) {
      this.packaged = var1;
   }

   public float getFreezingTime() {
      return this.freezingTime;
   }

   public void setFreezingTime(float var1) {
      if (var1 >= 100.0F) {
         this.setFrozen(true);
         var1 = 100.0F;
      } else if (var1 <= 0.0F) {
         var1 = 0.0F;
         this.setFrozen(false);
      }

      this.freezingTime = var1;
   }

   public void freeze() {
      this.setFreezingTime(100.0F);
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   public void setFrozen(boolean var1) {
      this.frozen = var1;
   }

   public boolean canBeFrozen() {
      return this.canBeFrozen;
   }

   public void setCanBeFrozen(boolean var1) {
      this.canBeFrozen = var1;
   }

   public boolean isFreezing() {
      return this.canBeFrozen() && !(this.getFreezingTime() >= 100.0F) && this.getOutermostContainer() != null && "freezer".equals(this.getOutermostContainer().getType()) ? this.getOutermostContainer().isPowered() : false;
   }

   public boolean isThawing() {
      if (this.canBeFrozen() && !(this.getFreezingTime() <= 0.0F)) {
         if (this.getOutermostContainer() != null && "freezer".equals(this.getOutermostContainer().getType())) {
            return !this.getOutermostContainer().isPowered();
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public String getReplaceOnRotten() {
      return this.replaceOnRotten;
   }

   public void setReplaceOnRotten(String var1) {
      this.replaceOnRotten = var1;
   }

   public void multiplyFoodValues(float var1) {
      this.setBoredomChange(this.getBoredomChange() * var1);
      this.setUnhappyChange(this.getUnhappyChange() * var1);
      this.setHungChange(this.getHungChange() * var1);
      this.setFluReduction((int)((float)this.getFluReduction() * var1));
      this.setThirstChange(this.getThirstChange() * var1);
      this.setPainReduction(this.getPainReduction() * var1);
      this.setReduceFoodSickness((int)((float)this.getReduceFoodSickness() * var1));
      this.setEndChange(this.getEnduranceChange() * var1);
      this.setStressChange(this.getStressChange() * var1);
      this.setFatigueChange(this.getFatigueChange() * var1);
      this.setCalories(this.getCalories() * var1);
      this.setCarbohydrates(this.getCarbohydrates() * var1);
      this.setProteins(this.getProteins() * var1);
      this.setLipids(this.getLipids() * var1);
   }

   public float getRottenTime() {
      return this.rottenTime;
   }

   public void setRottenTime(float var1) {
      this.rottenTime = var1;
   }

   public float getCompostTime() {
      return this.compostTime;
   }

   public void setCompostTime(float var1) {
      this.compostTime = var1;
   }

   public String getOnEat() {
      return this.onEat;
   }

   public void setOnEat(String var1) {
      this.onEat = var1;
   }

   public boolean isBadInMicrowave() {
      return this.badInMicrowave;
   }

   public void setBadInMicrowave(boolean var1) {
      this.badInMicrowave = var1;
   }

   private void destroyThisItem() {
      IsoWorldInventoryObject var1 = this.getWorldItem();
      if (var1 != null && var1.getSquare() != null) {
         if (GameServer.bServer) {
            GameServer.RemoveItemFromMap(var1);
         } else {
            var1.removeFromWorld();
            var1.removeFromSquare();
         }

         this.setWorldItem((IsoWorldInventoryObject)null);
      } else if (this.container != null) {
         IsoObject var2 = this.container.getParent();
         if (GameServer.bServer) {
            if (!this.isInPlayerInventory()) {
               GameServer.sendRemoveItemFromContainer(this.container, this);
            }

            this.container.Remove((InventoryItem)this);
         } else {
            this.container.Remove((InventoryItem)this);
         }

         IsoWorld.instance.CurrentCell.addToProcessItemsRemove((InventoryItem)this);
         LuaManager.updateOverlaySprite(var2);
      }

      if (!GameServer.bServer) {
         LuaEventManager.triggerEvent("OnContainerUpdate");
      }

   }
}
