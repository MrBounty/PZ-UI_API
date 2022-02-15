package zombie.scripting.objects;

import gnu.trove.list.array.TFloatArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import se.krka.kahlua.vm.KahluaTable;
import zombie.SystemDisabler;
import zombie.Lua.LuaManager;
import zombie.core.BoxedStaticValues;
import zombie.core.ImmutableColor;
import zombie.core.math.PZMath;
import zombie.core.physics.Bullet;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;
import zombie.vehicles.BaseVehicle;

public final class VehicleScript extends BaseScriptObject {
   private String fileName;
   private String name;
   private final ArrayList models = new ArrayList();
   public final ArrayList m_attachments = new ArrayList();
   private float mass = 800.0F;
   private final Vector3f centerOfMassOffset = new Vector3f();
   private float engineForce = 3000.0F;
   private float engineIdleSpeed = 750.0F;
   private float steeringIncrement = 0.04F;
   private float steeringClamp = 0.4F;
   private float steeringClampMax = 0.9F;
   private float wheelFriction = 800.0F;
   private float stoppingMovementForce = 1.0F;
   private float suspensionStiffness = 20.0F;
   private float suspensionDamping = 2.3F;
   private float suspensionCompression = 4.4F;
   private float suspensionRestLength = 0.6F;
   private float maxSuspensionTravelCm = 500.0F;
   private float rollInfluence = 0.1F;
   private final Vector3f extents = new Vector3f(0.75F, 0.5F, 2.0F);
   private final Vector2f shadowExtents = new Vector2f(0.0F, 0.0F);
   private final Vector2f shadowOffset = new Vector2f(0.0F, 0.0F);
   private boolean bHadShadowOExtents = false;
   private boolean bHadShadowOffset = false;
   private final Vector2f extentsOffset = new Vector2f(0.5F, 0.5F);
   private final Vector3f physicsChassisShape = new Vector3f(0.75F, 0.5F, 1.0F);
   private final ArrayList m_physicsShapes = new ArrayList();
   private final ArrayList wheels = new ArrayList();
   private final ArrayList passengers = new ArrayList();
   public float maxSpeed = 20.0F;
   public boolean isSmallVehicle = true;
   public float spawnOffsetY = 0.0F;
   private int frontEndHealth = 100;
   private int rearEndHealth = 100;
   private int storageCapacity = 100;
   private int engineLoudness = 100;
   private int engineQuality = 100;
   private int seats = 2;
   private int mechanicType;
   private int engineRepairLevel;
   private float playerDamageProtection;
   private float forcedHue = -1.0F;
   private float forcedSat = -1.0F;
   private float forcedVal = -1.0F;
   public ImmutableColor leftSirenCol;
   public ImmutableColor rightSirenCol;
   private String engineRPMType = "jeep";
   private float offroadEfficiency = 1.0F;
   private final TFloatArrayList crawlOffsets = new TFloatArrayList();
   public int gearRatioCount = 0;
   public final float[] gearRatio = new float[9];
   private final VehicleScript.Skin textures = new VehicleScript.Skin();
   private final ArrayList skins = new ArrayList();
   private final ArrayList areas = new ArrayList();
   private final ArrayList parts = new ArrayList();
   private boolean hasSiren = false;
   private final VehicleScript.LightBar lightbar = new VehicleScript.LightBar();
   private final VehicleScript.Sounds sound = new VehicleScript.Sounds();
   public boolean textureMaskEnable = false;
   private static final int PHYSICS_SHAPE_BOX = 1;
   private static final int PHYSICS_SHAPE_SPHERE = 2;

   public VehicleScript() {
      this.gearRatioCount = 4;
      this.gearRatio[0] = 7.09F;
      this.gearRatio[1] = 6.44F;
      this.gearRatio[2] = 4.1F;
      this.gearRatio[3] = 2.29F;
      this.gearRatio[4] = 1.47F;
      this.gearRatio[5] = 1.0F;
   }

   public void Load(String var1, String var2) {
      ScriptManager var3 = ScriptManager.instance;
      this.fileName = var3.currentFileName;
      if (!var3.scriptsWithVehicles.contains(this.fileName)) {
         var3.scriptsWithVehicles.add(this.fileName);
      }

      this.name = var1;
      ScriptParser.Block var4 = ScriptParser.parse(var2);
      var4 = (ScriptParser.Block)var4.children.get(0);
      Iterator var5 = var4.elements.iterator();

      while(true) {
         while(var5.hasNext()) {
            ScriptParser.BlockElement var6 = (ScriptParser.BlockElement)var5.next();
            String var16;
            if (var6.asValue() != null) {
               String[] var13 = var6.asValue().string.split("=");
               var16 = var13[0].trim();
               String var18 = var13[1].trim();
               if ("extents".equals(var16)) {
                  this.LoadVector3f(var18, this.extents);
               } else if ("shadowExtents".equals(var16)) {
                  this.LoadVector2f(var18, this.shadowExtents);
                  this.bHadShadowOExtents = true;
               } else if ("shadowOffset".equals(var16)) {
                  this.LoadVector2f(var18, this.shadowOffset);
                  this.bHadShadowOffset = true;
               } else if ("physicsChassisShape".equals(var16)) {
                  this.LoadVector3f(var18, this.physicsChassisShape);
               } else if ("extentsOffset".equals(var16)) {
                  this.LoadVector2f(var18, this.extentsOffset);
               } else if ("mass".equals(var16)) {
                  this.mass = Float.parseFloat(var18);
               } else if ("offRoadEfficiency".equalsIgnoreCase(var16)) {
                  this.offroadEfficiency = Float.parseFloat(var18);
               } else if ("centerOfMassOffset".equals(var16)) {
                  this.LoadVector3f(var18, this.centerOfMassOffset);
               } else if ("engineForce".equals(var16)) {
                  this.engineForce = Float.parseFloat(var18);
               } else if ("engineIdleSpeed".equals(var16)) {
                  this.engineIdleSpeed = Float.parseFloat(var18);
               } else if ("gearRatioCount".equals(var16)) {
                  this.gearRatioCount = Integer.parseInt(var18);
               } else if ("gearRatioR".equals(var16)) {
                  this.gearRatio[0] = Float.parseFloat(var18);
               } else if ("gearRatio1".equals(var16)) {
                  this.gearRatio[1] = Float.parseFloat(var18);
               } else if ("gearRatio2".equals(var16)) {
                  this.gearRatio[2] = Float.parseFloat(var18);
               } else if ("gearRatio3".equals(var16)) {
                  this.gearRatio[3] = Float.parseFloat(var18);
               } else if ("gearRatio4".equals(var16)) {
                  this.gearRatio[4] = Float.parseFloat(var18);
               } else if ("gearRatio5".equals(var16)) {
                  this.gearRatio[5] = Float.parseFloat(var18);
               } else if ("gearRatio6".equals(var16)) {
                  this.gearRatio[6] = Float.parseFloat(var18);
               } else if ("gearRatio7".equals(var16)) {
                  this.gearRatio[7] = Float.parseFloat(var18);
               } else if ("gearRatio8".equals(var16)) {
                  this.gearRatio[8] = Float.parseFloat(var18);
               } else if ("textureMaskEnable".equals(var16)) {
                  this.textureMaskEnable = Boolean.parseBoolean(var18);
               } else if ("textureRust".equals(var16)) {
                  this.textures.textureRust = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureMask".equals(var16)) {
                  this.textures.textureMask = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureLights".equals(var16)) {
                  this.textures.textureLights = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureDamage1Overlay".equals(var16)) {
                  this.textures.textureDamage1Overlay = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureDamage1Shell".equals(var16)) {
                  this.textures.textureDamage1Shell = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureDamage2Overlay".equals(var16)) {
                  this.textures.textureDamage2Overlay = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureDamage2Shell".equals(var16)) {
                  this.textures.textureDamage2Shell = StringUtils.discardNullOrWhitespace(var18);
               } else if ("textureShadow".equals(var16)) {
                  this.textures.textureShadow = StringUtils.discardNullOrWhitespace(var18);
               } else if ("rollInfluence".equals(var16)) {
                  this.rollInfluence = Float.parseFloat(var18);
               } else if ("steeringIncrement".equals(var16)) {
                  this.steeringIncrement = Float.parseFloat(var18);
               } else if ("steeringClamp".equals(var16)) {
                  this.steeringClamp = Float.parseFloat(var18);
               } else if ("suspensionStiffness".equals(var16)) {
                  this.suspensionStiffness = Float.parseFloat(var18);
               } else if ("suspensionDamping".equals(var16)) {
                  this.suspensionDamping = Float.parseFloat(var18);
               } else if ("suspensionCompression".equals(var16)) {
                  this.suspensionCompression = Float.parseFloat(var18);
               } else if ("suspensionRestLength".equals(var16)) {
                  this.suspensionRestLength = Float.parseFloat(var18);
               } else if ("maxSuspensionTravelCm".equals(var16)) {
                  this.maxSuspensionTravelCm = Float.parseFloat(var18);
               } else if ("wheelFriction".equals(var16)) {
                  this.wheelFriction = Float.parseFloat(var18);
               } else if ("stoppingMovementForce".equals(var16)) {
                  this.stoppingMovementForce = Float.parseFloat(var18);
               } else if ("maxSpeed".equals(var16)) {
                  this.maxSpeed = Float.parseFloat(var18);
               } else if ("isSmallVehicle".equals(var16)) {
                  this.isSmallVehicle = Boolean.parseBoolean(var18);
               } else if ("spawnOffsetY".equals(var16)) {
                  this.spawnOffsetY = Float.parseFloat(var18) - 0.995F;
               } else if ("frontEndDurability".equals(var16)) {
                  this.frontEndHealth = Integer.parseInt(var18);
               } else if ("rearEndDurability".equals(var16)) {
                  this.rearEndHealth = Integer.parseInt(var18);
               } else if ("storageCapacity".equals(var16)) {
                  this.storageCapacity = Integer.parseInt(var18);
               } else if ("engineLoudness".equals(var16)) {
                  this.engineLoudness = Integer.parseInt(var18);
               } else if ("engineQuality".equals(var16)) {
                  this.engineQuality = Integer.parseInt(var18);
               } else if ("seats".equals(var16)) {
                  this.seats = Integer.parseInt(var18);
               } else if ("hasSiren".equals(var16)) {
                  this.hasSiren = Boolean.parseBoolean(var18);
               } else if ("mechanicType".equals(var16)) {
                  this.mechanicType = Integer.parseInt(var18);
               } else if ("forcedColor".equals(var16)) {
                  String[] var21 = var18.split(" ");
                  this.setForcedHue(Float.parseFloat(var21[0]));
                  this.setForcedSat(Float.parseFloat(var21[1]));
                  this.setForcedVal(Float.parseFloat(var21[2]));
               } else if ("engineRPMType".equals(var16)) {
                  this.engineRPMType = var18.trim();
               } else if ("template".equals(var16)) {
                  this.LoadTemplate(var18);
               } else if ("template!".equals(var16)) {
                  VehicleTemplate var22 = ScriptManager.instance.getVehicleTemplate(var18);
                  if (var22 == null) {
                     DebugLog.log("ERROR: template \"" + var18 + "\" not found");
                  } else {
                     this.Load(var1, var22.body);
                  }
               } else if ("engineRepairLevel".equals(var16)) {
                  this.engineRepairLevel = Integer.parseInt(var18);
               } else if ("playerDamageProtection".equals(var16)) {
                  this.setPlayerDamageProtection(Float.parseFloat(var18));
               }
            } else {
               ScriptParser.Block var7 = var6.asBlock();
               if ("area".equals(var7.type)) {
                  this.LoadArea(var7);
               } else if ("attachment".equals(var7.type)) {
                  this.LoadAttachment(var7);
               } else if ("model".equals(var7.type)) {
                  this.LoadModel(var7, this.models);
               } else {
                  Iterator var17;
                  if ("part".equals(var7.type)) {
                     if (var7.id != null && var7.id.contains("*")) {
                        var16 = var7.id;
                        var17 = this.parts.iterator();

                        while(var17.hasNext()) {
                           VehicleScript.Part var20 = (VehicleScript.Part)var17.next();
                           if (this.globMatch(var16, var20.id)) {
                              var7.id = var20.id;
                              this.LoadPart(var7);
                           }
                        }
                     } else {
                        this.LoadPart(var7);
                     }
                  } else if ("passenger".equals(var7.type)) {
                     if (var7.id != null && var7.id.contains("*")) {
                        var16 = var7.id;
                        var17 = this.passengers.iterator();

                        while(var17.hasNext()) {
                           VehicleScript.Passenger var19 = (VehicleScript.Passenger)var17.next();
                           if (this.globMatch(var16, var19.id)) {
                              var7.id = var19.id;
                              this.LoadPassenger(var7);
                           }
                        }
                     } else {
                        this.LoadPassenger(var7);
                     }
                  } else if ("physics".equals(var7.type)) {
                     VehicleScript.PhysicsShape var15 = this.LoadPhysicsShape(var7);
                     if (var15 != null && this.m_physicsShapes.size() < 10) {
                        this.m_physicsShapes.add(var15);
                     }
                  } else if ("skin".equals(var7.type)) {
                     VehicleScript.Skin var14 = this.LoadSkin(var7);
                     if (!StringUtils.isNullOrWhitespace(var14.texture)) {
                        this.skins.add(var14);
                     }
                  } else if ("wheel".equals(var7.type)) {
                     this.LoadWheel(var7);
                  } else {
                     Iterator var8;
                     ScriptParser.Value var9;
                     String var10;
                     String var11;
                     if ("lightbar".equals(var7.type)) {
                        var8 = var7.values.iterator();

                        while(var8.hasNext()) {
                           var9 = (ScriptParser.Value)var8.next();
                           var10 = var9.getKey().trim();
                           var11 = var9.getValue().trim();
                           if ("soundSiren".equals(var10)) {
                              this.lightbar.soundSiren0 = var11 + "Yelp";
                              this.lightbar.soundSiren1 = var11 + "Wall";
                              this.lightbar.soundSiren2 = var11 + "Alarm";
                           }

                           if ("soundSiren0".equals(var10)) {
                              this.lightbar.soundSiren0 = var11;
                           }

                           if ("soundSiren1".equals(var10)) {
                              this.lightbar.soundSiren1 = var11;
                           }

                           if ("soundSiren2".equals(var10)) {
                              this.lightbar.soundSiren2 = var11;
                           }

                           String[] var12;
                           if ("leftCol".equals(var10)) {
                              var12 = var11.split(";");
                              this.leftSirenCol = new ImmutableColor(Float.parseFloat(var12[0]), Float.parseFloat(var12[1]), Float.parseFloat(var12[2]));
                           }

                           if ("rightCol".equals(var10)) {
                              var12 = var11.split(";");
                              this.rightSirenCol = new ImmutableColor(Float.parseFloat(var12[0]), Float.parseFloat(var12[1]), Float.parseFloat(var12[2]));
                           }

                           this.lightbar.enable = true;
                           VehicleScript.Part var23 = new VehicleScript.Part();
                           var23.id = "lightbar";
                           this.parts.add(var23);
                        }
                     } else if ("sound".equals(var7.type)) {
                        var8 = var7.values.iterator();

                        while(var8.hasNext()) {
                           var9 = (ScriptParser.Value)var8.next();
                           var10 = var9.getKey().trim();
                           var11 = var9.getValue().trim();
                           if ("backSignal".equals(var10)) {
                              this.sound.backSignal = StringUtils.discardNullOrWhitespace(var11);
                              this.sound.backSignalEnable = this.sound.backSignal != null;
                           } else if ("engine".equals(var10)) {
                              this.sound.engine = StringUtils.discardNullOrWhitespace(var11);
                           } else if ("engineStart".equals(var10)) {
                              this.sound.engineStart = StringUtils.discardNullOrWhitespace(var11);
                           } else if ("engineTurnOff".equals(var10)) {
                              this.sound.engineTurnOff = StringUtils.discardNullOrWhitespace(var11);
                           } else if ("horn".equals(var10)) {
                              this.sound.horn = StringUtils.discardNullOrWhitespace(var11);
                              this.sound.hornEnable = this.sound.horn != null;
                           } else if ("ignitionFail".equals(var10)) {
                              this.sound.ignitionFail = StringUtils.discardNullOrWhitespace(var11);
                           } else if ("ignitionFailNoPower".equals(var10)) {
                              this.sound.ignitionFailNoPower = StringUtils.discardNullOrWhitespace(var11);
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   public String getFileName() {
      return this.fileName;
   }

   public void Loaded() {
      float var1 = this.getModelScale();
      this.extents.mul(var1);
      this.maxSuspensionTravelCm *= var1;
      this.suspensionRestLength *= var1;
      this.centerOfMassOffset.mul(var1);
      this.physicsChassisShape.mul(var1);
      if (this.bHadShadowOExtents) {
         this.shadowExtents.mul(var1);
      } else {
         this.shadowExtents.set(this.extents.x(), this.extents.z());
      }

      if (this.bHadShadowOffset) {
         this.shadowOffset.mul(var1);
      } else {
         this.shadowOffset.set(this.centerOfMassOffset.x(), this.centerOfMassOffset.z());
      }

      Iterator var2 = this.models.iterator();

      while(var2.hasNext()) {
         VehicleScript.Model var3 = (VehicleScript.Model)var2.next();
         var3.offset.mul(var1);
      }

      var2 = this.m_attachments.iterator();

      while(var2.hasNext()) {
         ModelAttachment var6 = (ModelAttachment)var2.next();
         var6.getOffset().mul(var1);
      }

      var2 = this.m_physicsShapes.iterator();

      while(var2.hasNext()) {
         VehicleScript.PhysicsShape var7 = (VehicleScript.PhysicsShape)var2.next();
         var7.offset.mul(var1);
         switch(var7.type) {
         case 1:
            var7.extents.mul(var1);
            break;
         case 2:
            var7.radius *= var1;
         }
      }

      var2 = this.wheels.iterator();

      while(var2.hasNext()) {
         VehicleScript.Wheel var9 = (VehicleScript.Wheel)var2.next();
         var9.radius *= var1;
         var9.offset.mul(var1);
      }

      VehicleScript.Area var10;
      for(var2 = this.areas.iterator(); var2.hasNext(); var10.h *= var1) {
         var10 = (VehicleScript.Area)var2.next();
         var10.x *= var1;
         var10.y *= var1;
         var10.w *= var1;
      }

      if (!this.extents.equals(this.physicsChassisShape)) {
         DebugLog.Script.warn("vehicle \"" + this.name + "\" extents != physicsChassisShape");
      }

      int var4;
      int var8;
      for(var8 = 0; var8 < this.passengers.size(); ++var8) {
         VehicleScript.Passenger var11 = (VehicleScript.Passenger)this.passengers.get(var8);

         for(var4 = 0; var4 < var11.getPositionCount(); ++var4) {
            VehicleScript.Position var5 = var11.getPosition(var4);
            var5.getOffset().mul(var1);
         }

         for(var4 = 0; var4 < var11.switchSeats.size(); ++var4) {
            VehicleScript.Passenger.SwitchSeat var13 = (VehicleScript.Passenger.SwitchSeat)var11.switchSeats.get(var4);
            var13.seat = this.getPassengerIndex(var13.id);

            assert var13.seat != -1;
         }
      }

      for(var8 = 0; var8 < this.parts.size(); ++var8) {
         VehicleScript.Part var12 = (VehicleScript.Part)this.parts.get(var8);
         if (var12.container != null && var12.container.seatID != null && !var12.container.seatID.isEmpty()) {
            var12.container.seat = this.getPassengerIndex(var12.container.seatID);
         }

         if (var12.specificItem && var12.itemType != null) {
            for(var4 = 0; var4 < var12.itemType.size(); ++var4) {
               ArrayList var10000 = var12.itemType;
               String var10002 = (String)var12.itemType.get(var4);
               var10000.set(var4, var10002 + this.mechanicType);
            }
         }
      }

      this.initCrawlOffsets();
      this.toBullet();
   }

   public void toBullet() {
      float[] var1 = new float[200];
      byte var2 = 0;
      int var5 = var2 + 1;
      var1[var2] = this.getModelScale();
      var1[var5++] = this.extents.x;
      var1[var5++] = this.extents.y;
      var1[var5++] = this.extents.z;
      var1[var5++] = this.physicsChassisShape.x;
      var1[var5++] = this.physicsChassisShape.y;
      var1[var5++] = this.physicsChassisShape.z;
      var1[var5++] = this.mass;
      var1[var5++] = this.centerOfMassOffset.x;
      var1[var5++] = this.centerOfMassOffset.y;
      var1[var5++] = this.centerOfMassOffset.z;
      var1[var5++] = this.rollInfluence;
      var1[var5++] = this.suspensionStiffness;
      var1[var5++] = this.suspensionCompression;
      var1[var5++] = this.suspensionDamping;
      var1[var5++] = this.maxSuspensionTravelCm;
      var1[var5++] = this.suspensionRestLength;
      if (SystemDisabler.getdoHighFriction()) {
         var1[var5++] = this.wheelFriction * 100.0F;
      } else {
         var1[var5++] = this.wheelFriction;
      }

      var1[var5++] = this.stoppingMovementForce;
      var1[var5++] = (float)this.getWheelCount();

      int var3;
      for(var3 = 0; var3 < this.getWheelCount(); ++var3) {
         VehicleScript.Wheel var4 = this.getWheel(var3);
         var1[var5++] = var4.front ? 1.0F : 0.0F;
         var1[var5++] = var4.offset.x + this.getModel().offset.x - 0.0F * this.centerOfMassOffset.x;
         var1[var5++] = var4.offset.y + this.getModel().offset.y - 0.0F * this.centerOfMassOffset.y + 1.0F * this.suspensionRestLength;
         var1[var5++] = var4.offset.z + this.getModel().offset.z - 0.0F * this.centerOfMassOffset.z;
         var1[var5++] = var4.radius;
      }

      var1[var5++] = (float)(this.m_physicsShapes.size() + 1);
      var1[var5++] = 1.0F;
      var1[var5++] = this.centerOfMassOffset.x;
      var1[var5++] = this.centerOfMassOffset.y;
      var1[var5++] = this.centerOfMassOffset.z;
      var1[var5++] = this.physicsChassisShape.x;
      var1[var5++] = this.physicsChassisShape.y;
      var1[var5++] = this.physicsChassisShape.z;
      var1[var5++] = 0.0F;
      var1[var5++] = 0.0F;
      var1[var5++] = 0.0F;

      for(var3 = 0; var3 < this.m_physicsShapes.size(); ++var3) {
         VehicleScript.PhysicsShape var6 = (VehicleScript.PhysicsShape)this.m_physicsShapes.get(var3);
         var1[var5++] = (float)var6.type;
         var1[var5++] = var6.offset.x;
         var1[var5++] = var6.offset.y;
         var1[var5++] = var6.offset.z;
         if (var6.type == 1) {
            var1[var5++] = var6.extents.x;
            var1[var5++] = var6.extents.y;
            var1[var5++] = var6.extents.z;
            var1[var5++] = var6.rotate.x;
            var1[var5++] = var6.rotate.y;
            var1[var5++] = var6.rotate.z;
         } else if (var6.type == 2) {
            var1[var5++] = var6.radius;
         }
      }

      Bullet.defineVehicleScript(this.getFullName(), var1);
   }

   private void LoadVector2f(String var1, Vector2f var2) {
      String[] var3 = var1.split(" ");
      var2.set(Float.parseFloat(var3[0]), Float.parseFloat(var3[1]));
   }

   private void LoadVector3f(String var1, Vector3f var2) {
      String[] var3 = var1.split(" ");
      var2.set(Float.parseFloat(var3[0]), Float.parseFloat(var3[1]), Float.parseFloat(var3[2]));
   }

   private void LoadVector4f(String var1, Vector4f var2) {
      String[] var3 = var1.split(" ");
      var2.set(Float.parseFloat(var3[0]), Float.parseFloat(var3[1]), Float.parseFloat(var3[2]), Float.parseFloat(var3[3]));
   }

   private void LoadVector2i(String var1, Vector2i var2) {
      String[] var3 = var1.split(" ");
      var2.set(Integer.parseInt(var3[0]), Integer.parseInt(var3[1]));
   }

   private ModelAttachment LoadAttachment(ScriptParser.Block var1) {
      ModelAttachment var2 = this.getAttachmentById(var1.id);
      if (var2 == null) {
         var2 = new ModelAttachment(var1.id);
         this.m_attachments.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("bone".equals(var5)) {
            var2.setBone(var6);
         } else if ("offset".equals(var5)) {
            this.LoadVector3f(var6, var2.getOffset());
         } else if ("rotate".equals(var5)) {
            this.LoadVector3f(var6, var2.getRotate());
         } else if ("canAttach".equals(var5)) {
            var2.setCanAttach(new ArrayList(Arrays.asList(var6.split(","))));
         } else if ("zoffset".equals(var5)) {
            var2.setZOffset(Float.parseFloat(var6));
         } else if ("updateconstraint".equals(var5)) {
            var2.setUpdateConstraint(Boolean.parseBoolean(var6));
         }
      }

      return var2;
   }

   private VehicleScript.Model LoadModel(ScriptParser.Block var1, ArrayList var2) {
      VehicleScript.Model var3 = this.getModelById(var1.id, var2);
      if (var3 == null) {
         var3 = new VehicleScript.Model();
         var3.id = var1.id;
         var2.add(var3);
      }

      Iterator var4 = var1.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         if ("file".equals(var6)) {
            var3.file = var7;
         } else if ("offset".equals(var6)) {
            this.LoadVector3f(var7, var3.offset);
         } else if ("rotate".equals(var6)) {
            this.LoadVector3f(var7, var3.rotate);
         } else if ("scale".equals(var6)) {
            var3.scale = Float.parseFloat(var7);
         }
      }

      return var3;
   }

   private VehicleScript.Skin LoadSkin(ScriptParser.Block var1) {
      VehicleScript.Skin var2 = new VehicleScript.Skin();
      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("texture".equals(var5)) {
            var2.texture = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureRust".equals(var5)) {
            var2.textureRust = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureMask".equals(var5)) {
            var2.textureMask = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureLights".equals(var5)) {
            var2.textureLights = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureDamage1Overlay".equals(var5)) {
            var2.textureDamage1Overlay = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureDamage1Shell".equals(var5)) {
            var2.textureDamage1Shell = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureDamage2Overlay".equals(var5)) {
            var2.textureDamage2Overlay = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureDamage2Shell".equals(var5)) {
            var2.textureDamage2Shell = StringUtils.discardNullOrWhitespace(var6);
         } else if ("textureShadow".equals(var5)) {
            var2.textureShadow = StringUtils.discardNullOrWhitespace(var6);
         }
      }

      return var2;
   }

   private VehicleScript.Wheel LoadWheel(ScriptParser.Block var1) {
      VehicleScript.Wheel var2 = this.getWheelById(var1.id);
      if (var2 == null) {
         var2 = new VehicleScript.Wheel();
         var2.id = var1.id;
         this.wheels.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("model".equals(var5)) {
            var2.model = var6;
         } else if ("front".equals(var5)) {
            var2.front = Boolean.parseBoolean(var6);
         } else if ("offset".equals(var5)) {
            this.LoadVector3f(var6, var2.offset);
         } else if ("radius".equals(var5)) {
            var2.radius = Float.parseFloat(var6);
         } else if ("width".equals(var5)) {
            var2.width = Float.parseFloat(var6);
         }
      }

      return var2;
   }

   private VehicleScript.Passenger LoadPassenger(ScriptParser.Block var1) {
      VehicleScript.Passenger var2 = this.getPassengerById(var1.id);
      if (var2 == null) {
         var2 = new VehicleScript.Passenger();
         var2.id = var1.id;
         this.passengers.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("area".equals(var5)) {
            var2.area = var6;
         } else if ("door".equals(var5)) {
            var2.door = var6;
         } else if ("door2".equals(var5)) {
            var2.door2 = var6;
         } else if ("hasRoof".equals(var5)) {
            var2.hasRoof = Boolean.parseBoolean(var6);
         } else if ("showPassenger".equals(var5)) {
            var2.showPassenger = Boolean.parseBoolean(var6);
         }
      }

      var3 = var1.children.iterator();

      while(var3.hasNext()) {
         ScriptParser.Block var7 = (ScriptParser.Block)var3.next();
         if ("anim".equals(var7.type)) {
            this.LoadAnim(var7, var2.anims);
         } else if ("position".equals(var7.type)) {
            this.LoadPosition(var7, var2.positions);
         } else if ("switchSeat".equals(var7.type)) {
            this.LoadPassengerSwitchSeat(var7, var2);
         }
      }

      return var2;
   }

   private VehicleScript.Anim LoadAnim(ScriptParser.Block var1, ArrayList var2) {
      VehicleScript.Anim var3 = this.getAnimationById(var1.id, var2);
      if (var3 == null) {
         var3 = new VehicleScript.Anim();
         var3.id = var1.id;
         var2.add(var3);
      }

      Iterator var4 = var1.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         if ("angle".equals(var6)) {
            this.LoadVector3f(var7, var3.angle);
         } else if ("anim".equals(var6)) {
            var3.anim = var7;
         } else if ("animate".equals(var6)) {
            var3.bAnimate = Boolean.parseBoolean(var7);
         } else if ("loop".equals(var6)) {
            var3.bLoop = Boolean.parseBoolean(var7);
         } else if ("reverse".equals(var6)) {
            var3.bReverse = Boolean.parseBoolean(var7);
         } else if ("rate".equals(var6)) {
            var3.rate = Float.parseFloat(var7);
         } else if ("offset".equals(var6)) {
            this.LoadVector3f(var7, var3.offset);
         } else if ("sound".equals(var6)) {
            var3.sound = var7;
         }
      }

      return var3;
   }

   private VehicleScript.Passenger.SwitchSeat LoadPassengerSwitchSeat(ScriptParser.Block var1, VehicleScript.Passenger var2) {
      VehicleScript.Passenger.SwitchSeat var3 = var2.getSwitchSeatById(var1.id);
      if (var1.isEmpty()) {
         if (var3 != null) {
            var2.switchSeats.remove(var3);
         }

         return null;
      } else {
         if (var3 == null) {
            var3 = new VehicleScript.Passenger.SwitchSeat();
            var3.id = var1.id;
            var2.switchSeats.add(var3);
         }

         Iterator var4 = var1.values.iterator();

         while(var4.hasNext()) {
            ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
            String var6 = var5.getKey().trim();
            String var7 = var5.getValue().trim();
            if ("anim".equals(var6)) {
               var3.anim = var7;
            } else if ("rate".equals(var6)) {
               var3.rate = Float.parseFloat(var7);
            } else if ("sound".equals(var6)) {
               var3.sound = var7.isEmpty() ? null : var7;
            }
         }

         return var3;
      }
   }

   private VehicleScript.Area LoadArea(ScriptParser.Block var1) {
      VehicleScript.Area var2 = this.getAreaById(var1.id);
      if (var2 == null) {
         var2 = new VehicleScript.Area();
         var2.id = var1.id;
         this.areas.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("xywh".equals(var5)) {
            String[] var7 = var6.split(" ");
            var2.x = Float.parseFloat(var7[0]);
            var2.y = Float.parseFloat(var7[1]);
            var2.w = Float.parseFloat(var7[2]);
            var2.h = Float.parseFloat(var7[3]);
         }
      }

      return var2;
   }

   private VehicleScript.Part LoadPart(ScriptParser.Block var1) {
      VehicleScript.Part var2 = this.getPartById(var1.id);
      if (var2 == null) {
         var2 = new VehicleScript.Part();
         var2.id = var1.id;
         this.parts.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(true) {
         while(var3.hasNext()) {
            ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
            String var5 = var4.getKey().trim();
            String var6 = var4.getValue().trim();
            if ("area".equals(var5)) {
               var2.area = var6.isEmpty() ? null : var6;
            } else if ("itemType".equals(var5)) {
               var2.itemType = new ArrayList();
               String[] var7 = var6.split(";");
               String[] var8 = var7;
               int var9 = var7.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String var11 = var8[var10];
                  var2.itemType.add(var11);
               }
            } else if ("parent".equals(var5)) {
               var2.parent = var6.isEmpty() ? null : var6;
            } else if ("mechanicRequireKey".equals(var5)) {
               var2.mechanicRequireKey = Boolean.parseBoolean(var6);
            } else if ("repairMechanic".equals(var5)) {
               var2.setRepairMechanic(Boolean.parseBoolean(var6));
            } else if ("wheel".equals(var5)) {
               var2.wheel = var6;
            } else if ("category".equals(var5)) {
               var2.category = var6;
            } else if ("specificItem".equals(var5)) {
               var2.specificItem = Boolean.parseBoolean(var6);
            } else if ("hasLightsRear".equals(var5)) {
               var2.hasLightsRear = Boolean.parseBoolean(var6);
            }
         }

         var3 = var1.children.iterator();

         while(var3.hasNext()) {
            ScriptParser.Block var12 = (ScriptParser.Block)var3.next();
            if ("anim".equals(var12.type)) {
               if (var2.anims == null) {
                  var2.anims = new ArrayList();
               }

               this.LoadAnim(var12, var2.anims);
            } else if ("container".equals(var12.type)) {
               var2.container = this.LoadContainer(var12, var2.container);
            } else if ("door".equals(var12.type)) {
               var2.door = this.LoadDoor(var12);
            } else if ("lua".equals(var12.type)) {
               var2.luaFunctions = this.LoadLuaFunctions(var12);
            } else if ("model".equals(var12.type)) {
               if (var2.models == null) {
                  var2.models = new ArrayList();
               }

               this.LoadModel(var12, var2.models);
            } else if ("table".equals(var12.type)) {
               Object var13 = var2.tables == null ? null : var2.tables.get(var12.id);
               KahluaTable var14 = this.LoadTable(var12, var13 instanceof KahluaTable ? (KahluaTable)var13 : null);
               if (var2.tables == null) {
                  var2.tables = new HashMap();
               }

               var2.tables.put(var12.id, var14);
            } else if ("window".equals(var12.type)) {
               var2.window = this.LoadWindow(var12);
            }
         }

         return var2;
      }
   }

   private VehicleScript.PhysicsShape LoadPhysicsShape(ScriptParser.Block var1) {
      boolean var2 = true;
      String var3 = var1.id;
      byte var4 = -1;
      switch(var3.hashCode()) {
      case -895981619:
         if (var3.equals("sphere")) {
            var4 = 1;
         }
         break;
      case 97739:
         if (var3.equals("box")) {
            var4 = 0;
         }
      }

      byte var8;
      switch(var4) {
      case 0:
         var8 = 1;
         break;
      case 1:
         var8 = 2;
         break;
      default:
         return null;
      }

      VehicleScript.PhysicsShape var9 = new VehicleScript.PhysicsShape();
      var9.type = var8;
      Iterator var10 = var1.values.iterator();

      while(var10.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var10.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         if ("extents".equalsIgnoreCase(var6)) {
            this.LoadVector3f(var7, var9.extents);
         } else if ("offset".equalsIgnoreCase(var6)) {
            this.LoadVector3f(var7, var9.offset);
         } else if ("radius".equalsIgnoreCase(var6)) {
            var9.radius = Float.parseFloat(var7);
         } else if ("rotate".equalsIgnoreCase(var6)) {
            this.LoadVector3f(var7, var9.rotate);
         }
      }

      switch(var9.type) {
      case 1:
         if (var9.extents.x() <= 0.0F || var9.extents.y() <= 0.0F || var9.extents.z() <= 0.0F) {
            return null;
         }
         break;
      case 2:
         if (var9.radius <= 0.0F) {
            return null;
         }
      }

      return var9;
   }

   private VehicleScript.Door LoadDoor(ScriptParser.Block var1) {
      VehicleScript.Door var2 = new VehicleScript.Door();

      ScriptParser.Value var4;
      String var6;
      for(Iterator var3 = var1.values.iterator(); var3.hasNext(); var6 = var4.getValue().trim()) {
         var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
      }

      return var2;
   }

   private VehicleScript.Window LoadWindow(ScriptParser.Block var1) {
      VehicleScript.Window var2 = new VehicleScript.Window();
      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("openable".equals(var5)) {
            var2.openable = Boolean.parseBoolean(var6);
         }
      }

      return var2;
   }

   private VehicleScript.Container LoadContainer(ScriptParser.Block var1, VehicleScript.Container var2) {
      VehicleScript.Container var3 = var2 == null ? new VehicleScript.Container() : var2;
      Iterator var4 = var1.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String var6 = var5.getKey().trim();
         String var7 = var5.getValue().trim();
         if ("capacity".equals(var6)) {
            var3.capacity = Integer.parseInt(var7);
         } else if ("conditionAffectsCapacity".equals(var6)) {
            var3.conditionAffectsCapacity = Boolean.parseBoolean(var7);
         } else if ("contentType".equals(var6)) {
            var3.contentType = var7;
         } else if ("seat".equals(var6)) {
            var3.seatID = var7;
         } else if ("test".equals(var6)) {
            var3.luaTest = var7;
         }
      }

      return var3;
   }

   private HashMap LoadLuaFunctions(ScriptParser.Block var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         if (var4.string.indexOf(61) == -1) {
            String var10002 = var4.string.trim();
            throw new RuntimeException("expected \"key = value\", got \"" + var10002 + "\" in " + this.getFullName());
         }

         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         var2.put(var5, var6);
      }

      return var2;
   }

   private Object checkIntegerKey(Object var1) {
      if (!(var1 instanceof String)) {
         return var1;
      } else {
         String var2 = (String)var1;

         for(int var3 = 0; var3 < var2.length(); ++var3) {
            if (!Character.isDigit(var2.charAt(var3))) {
               return var1;
            }
         }

         return Double.valueOf(var2);
      }
   }

   private KahluaTable LoadTable(ScriptParser.Block var1, KahluaTable var2) {
      KahluaTable var3 = var2 == null ? LuaManager.platform.newTable() : var2;

      Iterator var4;
      String var6;
      String var7;
      for(var4 = var1.values.iterator(); var4.hasNext(); var3.rawset(this.checkIntegerKey(var6), var7)) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         var6 = var5.getKey().trim();
         var7 = var5.getValue().trim();
         if (var7.isEmpty()) {
            var7 = null;
         }
      }

      var4 = var1.children.iterator();

      while(var4.hasNext()) {
         ScriptParser.Block var8 = (ScriptParser.Block)var4.next();
         Object var9 = var3.rawget(var8.type);
         KahluaTable var10 = this.LoadTable(var8, var9 instanceof KahluaTable ? (KahluaTable)var9 : null);
         var3.rawset(this.checkIntegerKey(var8.type), var10);
      }

      return var3;
   }

   private void LoadTemplate(String var1) {
      if (var1.contains("/")) {
         String[] var2 = var1.split("/");
         if (var2.length == 0 || var2.length > 3) {
            DebugLog.log("ERROR: template \"" + var1 + "\"");
            return;
         }

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = var2[var3].trim();
            if (var2[var3].isEmpty()) {
               DebugLog.log("ERROR: template \"" + var1 + "\"");
               return;
            }
         }

         String var9 = var2[0];
         VehicleTemplate var4 = ScriptManager.instance.getVehicleTemplate(var9);
         if (var4 == null) {
            DebugLog.log("ERROR: template \"" + var1 + "\" not found");
            return;
         }

         VehicleScript var5 = var4.getScript();
         String var6 = var2[1];
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -944810854:
            if (var6.equals("passenger")) {
               var7 = 2;
            }
            break;
         case 3002509:
            if (var6.equals("area")) {
               var7 = 0;
            }
            break;
         case 3433459:
            if (var6.equals("part")) {
               var7 = 1;
            }
            break;
         case 113097563:
            if (var6.equals("wheel")) {
               var7 = 3;
            }
         }

         switch(var7) {
         case 0:
            if (var2.length == 2) {
               DebugLog.log("ERROR: template \"" + var1 + "\"");
               return;
            }

            this.copyAreasFrom(var5, var2[2]);
            break;
         case 1:
            if (var2.length == 2) {
               DebugLog.log("ERROR: template \"" + var1 + "\"");
               return;
            }

            this.copyPartsFrom(var5, var2[2]);
            break;
         case 2:
            if (var2.length == 2) {
               DebugLog.log("ERROR: template \"" + var1 + "\"");
               return;
            }

            this.copyPassengersFrom(var5, var2[2]);
            break;
         case 3:
            if (var2.length == 2) {
               DebugLog.log("ERROR: template \"" + var1 + "\"");
               return;
            }

            this.copyWheelsFrom(var5, var2[2]);
            break;
         default:
            DebugLog.log("ERROR: template \"" + var1 + "\"");
            return;
         }
      } else {
         String var8 = var1.trim();
         VehicleTemplate var10 = ScriptManager.instance.getVehicleTemplate(var8);
         if (var10 == null) {
            DebugLog.log("ERROR: template \"" + var1 + "\" not found");
            return;
         }

         VehicleScript var11 = var10.getScript();
         this.copyAreasFrom(var11, "*");
         this.copyPartsFrom(var11, "*");
         this.copyPassengersFrom(var11, "*");
         this.copyWheelsFrom(var11, "*");
      }

   }

   public void copyAreasFrom(VehicleScript var1, String var2) {
      if ("*".equals(var2)) {
         for(int var3 = 0; var3 < var1.getAreaCount(); ++var3) {
            VehicleScript.Area var4 = var1.getArea(var3);
            int var5 = this.getIndexOfAreaById(var4.id);
            if (var5 == -1) {
               this.areas.add(var4.makeCopy());
            } else {
               this.areas.set(var5, var4.makeCopy());
            }
         }
      } else {
         VehicleScript.Area var6 = var1.getAreaById(var2);
         if (var6 == null) {
            DebugLog.log("ERROR: area \"" + var2 + "\" not found");
            return;
         }

         int var7 = this.getIndexOfAreaById(var6.id);
         if (var7 == -1) {
            this.areas.add(var6.makeCopy());
         } else {
            this.areas.set(var7, var6.makeCopy());
         }
      }

   }

   public void copyPartsFrom(VehicleScript var1, String var2) {
      if ("*".equals(var2)) {
         for(int var3 = 0; var3 < var1.getPartCount(); ++var3) {
            VehicleScript.Part var4 = var1.getPart(var3);
            int var5 = this.getIndexOfPartById(var4.id);
            if (var5 == -1) {
               this.parts.add(var4.makeCopy());
            } else {
               this.parts.set(var5, var4.makeCopy());
            }
         }
      } else {
         VehicleScript.Part var6 = var1.getPartById(var2);
         if (var6 == null) {
            DebugLog.log("ERROR: part \"" + var2 + "\" not found");
            return;
         }

         int var7 = this.getIndexOfPartById(var6.id);
         if (var7 == -1) {
            this.parts.add(var6.makeCopy());
         } else {
            this.parts.set(var7, var6.makeCopy());
         }
      }

   }

   public void copyPassengersFrom(VehicleScript var1, String var2) {
      if ("*".equals(var2)) {
         for(int var3 = 0; var3 < var1.getPassengerCount(); ++var3) {
            VehicleScript.Passenger var4 = var1.getPassenger(var3);
            int var5 = this.getPassengerIndex(var4.id);
            if (var5 == -1) {
               this.passengers.add(var4.makeCopy());
            } else {
               this.passengers.set(var5, var4.makeCopy());
            }
         }
      } else {
         VehicleScript.Passenger var6 = var1.getPassengerById(var2);
         if (var6 == null) {
            DebugLog.log("ERROR: passenger \"" + var2 + "\" not found");
            return;
         }

         int var7 = this.getPassengerIndex(var6.id);
         if (var7 == -1) {
            this.passengers.add(var6.makeCopy());
         } else {
            this.passengers.set(var7, var6.makeCopy());
         }
      }

   }

   public void copyWheelsFrom(VehicleScript var1, String var2) {
      if ("*".equals(var2)) {
         for(int var3 = 0; var3 < var1.getWheelCount(); ++var3) {
            VehicleScript.Wheel var4 = var1.getWheel(var3);
            int var5 = this.getIndexOfWheelById(var4.id);
            if (var5 == -1) {
               this.wheels.add(var4.makeCopy());
            } else {
               this.wheels.set(var5, var4.makeCopy());
            }
         }
      } else {
         VehicleScript.Wheel var6 = var1.getWheelById(var2);
         if (var6 == null) {
            DebugLog.log("ERROR: wheel \"" + var2 + "\" not found");
            return;
         }

         int var7 = this.getIndexOfWheelById(var6.id);
         if (var7 == -1) {
            this.wheels.add(var6.makeCopy());
         } else {
            this.wheels.set(var7, var6.makeCopy());
         }
      }

   }

   private VehicleScript.Position LoadPosition(ScriptParser.Block var1, ArrayList var2) {
      VehicleScript.Position var3 = this.getPositionById(var1.id, var2);
      if (var1.isEmpty()) {
         if (var3 != null) {
            var2.remove(var3);
         }

         return null;
      } else {
         if (var3 == null) {
            var3 = new VehicleScript.Position();
            var3.id = var1.id;
            var2.add(var3);
         }

         Iterator var4 = var1.values.iterator();

         while(var4.hasNext()) {
            ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
            String var6 = var5.getKey().trim();
            String var7 = var5.getValue().trim();
            if ("rotate".equals(var6)) {
               this.LoadVector3f(var7, var3.rotate);
            } else if ("offset".equals(var6)) {
               this.LoadVector3f(var7, var3.offset);
            } else if ("area".equals(var6)) {
               var3.area = var7.isEmpty() ? null : var7;
            }
         }

         return var3;
      }
   }

   private void initCrawlOffsets() {
      for(int var1 = 0; var1 < this.getWheelCount(); ++var1) {
         VehicleScript.Wheel var2 = this.getWheel(var1);
         if (var2.id.contains("Left")) {
            this.initCrawlOffsets(var2);
         }
      }

      float var6 = this.extents.z + BaseVehicle.PLUS_RADIUS * 2.0F;

      int var7;
      for(var7 = 0; var7 < this.crawlOffsets.size(); ++var7) {
         this.crawlOffsets.set(var7, (this.extents.z / 2.0F + BaseVehicle.PLUS_RADIUS + this.crawlOffsets.get(var7) - this.centerOfMassOffset.z) / var6);
      }

      this.crawlOffsets.sort();

      for(var7 = 0; var7 < this.crawlOffsets.size(); ++var7) {
         float var3 = this.crawlOffsets.get(var7);

         for(int var4 = var7 + 1; var4 < this.crawlOffsets.size(); ++var4) {
            float var5 = this.crawlOffsets.get(var4);
            if ((var5 - var3) * var6 < 0.15F) {
               this.crawlOffsets.removeAt(var4--);
            }
         }
      }

   }

   private void initCrawlOffsets(VehicleScript.Wheel var1) {
      float var2 = 0.3F;
      float var3 = this.getModel() == null ? 0.0F : this.getModel().getOffset().z;
      float var4 = this.centerOfMassOffset.z + this.extents.z / 2.0F;
      float var5 = this.centerOfMassOffset.z - this.extents.z / 2.0F;

      for(int var6 = 0; var6 < 10; ++var6) {
         float var7 = var3 + var1.offset.z + var1.radius + var2 + var2 * (float)var6;
         if (var7 + var2 <= var4 && !this.isOverlappingWheel(var7)) {
            this.crawlOffsets.add(var7);
         }

         var7 = var3 + var1.offset.z - var1.radius - var2 - var2 * (float)var6;
         if (var7 - var2 >= var5 && !this.isOverlappingWheel(var7)) {
            this.crawlOffsets.add(var7);
         }
      }

   }

   private boolean isOverlappingWheel(float var1) {
      float var2 = 0.3F;
      float var3 = this.getModel() == null ? 0.0F : this.getModel().getOffset().z;

      for(int var4 = 0; var4 < this.getWheelCount(); ++var4) {
         VehicleScript.Wheel var5 = this.getWheel(var4);
         if (var5.id.contains("Left") && Math.abs(var3 + var5.offset.z - var1) < (var5.radius + var2) * 0.99F) {
            return true;
         }
      }

      return false;
   }

   public String getName() {
      return this.name;
   }

   public String getFullName() {
      String var10000 = this.getModule().getName();
      return var10000 + "." + this.getName();
   }

   public VehicleScript.Model getModel() {
      return this.models.isEmpty() ? null : (VehicleScript.Model)this.models.get(0);
   }

   public Vector3f getModelOffset() {
      return this.getModel() == null ? null : this.getModel().getOffset();
   }

   public float getModelScale() {
      return this.getModel() == null ? 1.0F : this.getModel().scale;
   }

   public void setModelScale(float var1) {
      VehicleScript.Model var2 = this.getModel();
      if (var2 != null) {
         float var3 = var2.scale;
         var2.scale = 1.0F / var3;
         this.Loaded();
         var2.scale = PZMath.clamp(var1, 0.01F, 100.0F);
         this.Loaded();
      }

   }

   public int getModelCount() {
      return this.models.size();
   }

   public VehicleScript.Model getModelByIndex(int var1) {
      return (VehicleScript.Model)this.models.get(var1);
   }

   public VehicleScript.Model getModelById(String var1, ArrayList var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         VehicleScript.Model var4 = (VehicleScript.Model)var2.get(var3);
         if (StringUtils.isNullOrWhitespace(var4.id) && StringUtils.isNullOrWhitespace(var1)) {
            return var4;
         }

         if (var4.id != null && var4.id.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public VehicleScript.Model getModelById(String var1) {
      return this.getModelById(var1, this.models);
   }

   public int getAttachmentCount() {
      return this.m_attachments.size();
   }

   public ModelAttachment getAttachment(int var1) {
      return (ModelAttachment)this.m_attachments.get(var1);
   }

   public ModelAttachment getAttachmentById(String var1) {
      for(int var2 = 0; var2 < this.m_attachments.size(); ++var2) {
         ModelAttachment var3 = (ModelAttachment)this.m_attachments.get(var2);
         if (var3.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public ModelAttachment addAttachment(ModelAttachment var1) {
      this.m_attachments.add(var1);
      return var1;
   }

   public ModelAttachment removeAttachment(ModelAttachment var1) {
      this.m_attachments.remove(var1);
      return var1;
   }

   public ModelAttachment addAttachmentAt(int var1, ModelAttachment var2) {
      this.m_attachments.add(var1, var2);
      return var2;
   }

   public ModelAttachment removeAttachment(int var1) {
      return (ModelAttachment)this.m_attachments.remove(var1);
   }

   public VehicleScript.LightBar getLightbar() {
      return this.lightbar;
   }

   public VehicleScript.Sounds getSounds() {
      return this.sound;
   }

   public boolean getHasSiren() {
      return this.hasSiren;
   }

   public Vector3f getExtents() {
      return this.extents;
   }

   public Vector3f getPhysicsChassisShape() {
      return this.physicsChassisShape;
   }

   public Vector2f getShadowExtents() {
      return this.shadowExtents;
   }

   public Vector2f getShadowOffset() {
      return this.shadowOffset;
   }

   public Vector2f getExtentsOffset() {
      return this.extentsOffset;
   }

   public float getMass() {
      return this.mass;
   }

   public Vector3f getCenterOfMassOffset() {
      return this.centerOfMassOffset;
   }

   public float getEngineForce() {
      return this.engineForce;
   }

   public float getEngineIdleSpeed() {
      return this.engineIdleSpeed;
   }

   public int getEngineQuality() {
      return this.engineQuality;
   }

   public int getEngineLoudness() {
      return this.engineLoudness;
   }

   public float getRollInfluence() {
      return this.rollInfluence;
   }

   public float getSteeringIncrement() {
      return this.steeringIncrement;
   }

   public float getSteeringClamp(float var1) {
      var1 = Math.abs(var1);
      float var2 = var1 / this.maxSpeed;
      if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      var2 = 1.0F - var2;
      return (this.steeringClampMax - this.steeringClamp) * var2 + this.steeringClamp;
   }

   public float getSuspensionStiffness() {
      return this.suspensionStiffness;
   }

   public float getSuspensionDamping() {
      return this.suspensionDamping;
   }

   public float getSuspensionCompression() {
      return this.suspensionCompression;
   }

   public float getSuspensionRestLength() {
      return this.suspensionRestLength;
   }

   public float getSuspensionTravel() {
      return this.maxSuspensionTravelCm;
   }

   public float getWheelFriction() {
      return this.wheelFriction;
   }

   public int getWheelCount() {
      return this.wheels.size();
   }

   public VehicleScript.Wheel getWheel(int var1) {
      return (VehicleScript.Wheel)this.wheels.get(var1);
   }

   public VehicleScript.Wheel getWheelById(String var1) {
      for(int var2 = 0; var2 < this.wheels.size(); ++var2) {
         VehicleScript.Wheel var3 = (VehicleScript.Wheel)this.wheels.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getIndexOfWheelById(String var1) {
      for(int var2 = 0; var2 < this.wheels.size(); ++var2) {
         VehicleScript.Wheel var3 = (VehicleScript.Wheel)this.wheels.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public int getPassengerCount() {
      return this.passengers.size();
   }

   public VehicleScript.Passenger getPassenger(int var1) {
      return (VehicleScript.Passenger)this.passengers.get(var1);
   }

   public VehicleScript.Passenger getPassengerById(String var1) {
      for(int var2 = 0; var2 < this.passengers.size(); ++var2) {
         VehicleScript.Passenger var3 = (VehicleScript.Passenger)this.passengers.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getPassengerIndex(String var1) {
      for(int var2 = 0; var2 < this.passengers.size(); ++var2) {
         VehicleScript.Passenger var3 = (VehicleScript.Passenger)this.passengers.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public int getPhysicsShapeCount() {
      return this.m_physicsShapes.size();
   }

   public VehicleScript.PhysicsShape getPhysicsShape(int var1) {
      return var1 >= 0 && var1 < this.m_physicsShapes.size() ? (VehicleScript.PhysicsShape)this.m_physicsShapes.get(var1) : null;
   }

   public int getFrontEndHealth() {
      return this.frontEndHealth;
   }

   public int getRearEndHealth() {
      return this.rearEndHealth;
   }

   public int getStorageCapacity() {
      return this.storageCapacity;
   }

   public VehicleScript.Skin getTextures() {
      return this.textures;
   }

   public int getSkinCount() {
      return this.skins.size();
   }

   public VehicleScript.Skin getSkin(int var1) {
      return (VehicleScript.Skin)this.skins.get(var1);
   }

   public int getAreaCount() {
      return this.areas.size();
   }

   public VehicleScript.Area getArea(int var1) {
      return (VehicleScript.Area)this.areas.get(var1);
   }

   public VehicleScript.Area getAreaById(String var1) {
      for(int var2 = 0; var2 < this.areas.size(); ++var2) {
         VehicleScript.Area var3 = (VehicleScript.Area)this.areas.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getIndexOfAreaById(String var1) {
      for(int var2 = 0; var2 < this.areas.size(); ++var2) {
         VehicleScript.Area var3 = (VehicleScript.Area)this.areas.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public int getPartCount() {
      return this.parts.size();
   }

   public VehicleScript.Part getPart(int var1) {
      return (VehicleScript.Part)this.parts.get(var1);
   }

   public VehicleScript.Part getPartById(String var1) {
      for(int var2 = 0; var2 < this.parts.size(); ++var2) {
         VehicleScript.Part var3 = (VehicleScript.Part)this.parts.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getIndexOfPartById(String var1) {
      for(int var2 = 0; var2 < this.parts.size(); ++var2) {
         VehicleScript.Part var3 = (VehicleScript.Part)this.parts.get(var2);
         if (var3.id != null && var3.id.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private VehicleScript.Anim getAnimationById(String var1, ArrayList var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         VehicleScript.Anim var4 = (VehicleScript.Anim)var2.get(var3);
         if (var4.id != null && var4.id.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   private VehicleScript.Position getPositionById(String var1, ArrayList var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         VehicleScript.Position var4 = (VehicleScript.Position)var2.get(var3);
         if (var4.id != null && var4.id.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public boolean globMatch(String var1, String var2) {
      Pattern var3 = Pattern.compile(var1.replaceAll("\\*", ".*"));
      return var3.matcher(var2).matches();
   }

   public int getGearRatioCount() {
      return this.gearRatioCount;
   }

   public int getSeats() {
      return this.seats;
   }

   public void setSeats(int var1) {
      this.seats = var1;
   }

   public int getMechanicType() {
      return this.mechanicType;
   }

   public void setMechanicType(int var1) {
      this.mechanicType = var1;
   }

   public int getEngineRepairLevel() {
      return this.engineRepairLevel;
   }

   public int getHeadlightConfigLevel() {
      return 2;
   }

   public void setEngineRepairLevel(int var1) {
      this.engineRepairLevel = var1;
   }

   public float getPlayerDamageProtection() {
      return this.playerDamageProtection;
   }

   public void setPlayerDamageProtection(float var1) {
      this.playerDamageProtection = var1;
   }

   public float getForcedHue() {
      return this.forcedHue;
   }

   public void setForcedHue(float var1) {
      this.forcedHue = var1;
   }

   public float getForcedSat() {
      return this.forcedSat;
   }

   public void setForcedSat(float var1) {
      this.forcedSat = var1;
   }

   public float getForcedVal() {
      return this.forcedVal;
   }

   public void setForcedVal(float var1) {
      this.forcedVal = var1;
   }

   public String getEngineRPMType() {
      return this.engineRPMType;
   }

   public void setEngineRPMType(String var1) {
      this.engineRPMType = var1;
   }

   public float getOffroadEfficiency() {
      return this.offroadEfficiency;
   }

   public void setOffroadEfficiency(float var1) {
      this.offroadEfficiency = var1;
   }

   public TFloatArrayList getCrawlOffsets() {
      return this.crawlOffsets;
   }

   public static final class Skin {
      public String texture;
      public String textureRust = null;
      public String textureMask = null;
      public String textureLights = null;
      public String textureDamage1Overlay = null;
      public String textureDamage1Shell = null;
      public String textureDamage2Overlay = null;
      public String textureDamage2Shell = null;
      public String textureShadow = null;
      public Texture textureData;
      public Texture textureDataRust;
      public Texture textureDataMask;
      public Texture textureDataLights;
      public Texture textureDataDamage1Overlay;
      public Texture textureDataDamage1Shell;
      public Texture textureDataDamage2Overlay;
      public Texture textureDataDamage2Shell;
      public Texture textureDataShadow;

      public void copyMissingFrom(VehicleScript.Skin var1) {
         if (this.textureRust == null) {
            this.textureRust = var1.textureRust;
         }

         if (this.textureMask == null) {
            this.textureMask = var1.textureMask;
         }

         if (this.textureLights == null) {
            this.textureLights = var1.textureLights;
         }

         if (this.textureDamage1Overlay == null) {
            this.textureDamage1Overlay = var1.textureDamage1Overlay;
         }

         if (this.textureDamage1Shell == null) {
            this.textureDamage1Shell = var1.textureDamage1Shell;
         }

         if (this.textureDamage2Overlay == null) {
            this.textureDamage2Overlay = var1.textureDamage2Overlay;
         }

         if (this.textureDamage2Shell == null) {
            this.textureDamage2Shell = var1.textureDamage2Shell;
         }

         if (this.textureShadow == null) {
            this.textureShadow = var1.textureShadow;
         }

      }
   }

   public static final class LightBar {
      public boolean enable = false;
      public String soundSiren0 = "";
      public String soundSiren1 = "";
      public String soundSiren2 = "";
   }

   public static final class Sounds {
      public boolean hornEnable = false;
      public String horn = "";
      public boolean backSignalEnable = false;
      public String backSignal = "";
      public String engine = null;
      public String engineStart = null;
      public String engineTurnOff = null;
      public String ignitionFail = null;
      public String ignitionFailNoPower = null;
   }

   public static final class Area {
      public String id;
      public float x;
      public float y;
      public float w;
      public float h;

      public String getId() {
         return this.id;
      }

      public Double getX() {
         return BoxedStaticValues.toDouble((double)this.x);
      }

      public Double getY() {
         return BoxedStaticValues.toDouble((double)this.y);
      }

      public Double getW() {
         return BoxedStaticValues.toDouble((double)this.w);
      }

      public Double getH() {
         return BoxedStaticValues.toDouble((double)this.h);
      }

      public void setX(Double var1) {
         this.x = var1.floatValue();
      }

      public void setY(Double var1) {
         this.y = var1.floatValue();
      }

      public void setW(Double var1) {
         this.w = var1.floatValue();
      }

      public void setH(Double var1) {
         this.h = var1.floatValue();
      }

      private VehicleScript.Area makeCopy() {
         VehicleScript.Area var1 = new VehicleScript.Area();
         var1.id = this.id;
         var1.x = this.x;
         var1.y = this.y;
         var1.w = this.w;
         var1.h = this.h;
         return var1;
      }
   }

   public static final class Model {
      public String id;
      public String file;
      public float scale = 1.0F;
      public final Vector3f offset = new Vector3f();
      public final Vector3f rotate = new Vector3f();

      public String getId() {
         return this.id;
      }

      public Vector3f getOffset() {
         return this.offset;
      }

      public Vector3f getRotate() {
         return this.rotate;
      }

      VehicleScript.Model makeCopy() {
         VehicleScript.Model var1 = new VehicleScript.Model();
         var1.id = this.id;
         var1.file = this.file;
         var1.scale = this.scale;
         var1.offset.set((Vector3fc)this.offset);
         var1.rotate.set((Vector3fc)this.rotate);
         return var1;
      }
   }

   public static final class Part {
      public String id = "Unknown";
      public String parent;
      public ArrayList itemType;
      public VehicleScript.Container container;
      public String area;
      public String wheel;
      public HashMap tables;
      public HashMap luaFunctions;
      public ArrayList models;
      public VehicleScript.Door door;
      public VehicleScript.Window window;
      public ArrayList anims;
      public String category;
      public boolean specificItem = true;
      public boolean mechanicRequireKey = false;
      public boolean repairMechanic = false;
      public boolean hasLightsRear = false;

      public boolean isMechanicRequireKey() {
         return this.mechanicRequireKey;
      }

      public void setMechanicRequireKey(boolean var1) {
         this.mechanicRequireKey = var1;
      }

      public boolean isRepairMechanic() {
         return this.repairMechanic;
      }

      public void setRepairMechanic(boolean var1) {
         this.repairMechanic = var1;
      }

      VehicleScript.Part makeCopy() {
         VehicleScript.Part var1 = new VehicleScript.Part();
         var1.id = this.id;
         var1.parent = this.parent;
         if (this.itemType != null) {
            var1.itemType = new ArrayList();
            var1.itemType.addAll(this.itemType);
         }

         if (this.container != null) {
            var1.container = this.container.makeCopy();
         }

         var1.area = this.area;
         var1.wheel = this.wheel;
         if (this.tables != null) {
            var1.tables = new HashMap();
            Iterator var2 = this.tables.entrySet().iterator();

            while(var2.hasNext()) {
               Entry var3 = (Entry)var2.next();
               KahluaTable var4 = LuaManager.copyTable((KahluaTable)var3.getValue());
               var1.tables.put((String)var3.getKey(), var4);
            }
         }

         if (this.luaFunctions != null) {
            var1.luaFunctions = new HashMap();
            var1.luaFunctions.putAll(this.luaFunctions);
         }

         int var5;
         if (this.models != null) {
            var1.models = new ArrayList();

            for(var5 = 0; var5 < this.models.size(); ++var5) {
               var1.models.add(((VehicleScript.Model)this.models.get(var5)).makeCopy());
            }
         }

         if (this.door != null) {
            var1.door = this.door.makeCopy();
         }

         if (this.window != null) {
            var1.window = this.window.makeCopy();
         }

         if (this.anims != null) {
            var1.anims = new ArrayList();

            for(var5 = 0; var5 < this.anims.size(); ++var5) {
               var1.anims.add(((VehicleScript.Anim)this.anims.get(var5)).makeCopy());
            }
         }

         var1.category = this.category;
         var1.specificItem = this.specificItem;
         var1.mechanicRequireKey = this.mechanicRequireKey;
         var1.repairMechanic = this.repairMechanic;
         var1.hasLightsRear = this.hasLightsRear;
         return var1;
      }
   }

   public static final class Passenger {
      public String id;
      public final ArrayList anims = new ArrayList();
      public final ArrayList switchSeats = new ArrayList();
      public boolean hasRoof = true;
      public boolean showPassenger = false;
      public String door;
      public String door2;
      public String area;
      public final ArrayList positions = new ArrayList();

      public String getId() {
         return this.id;
      }

      public VehicleScript.Passenger makeCopy() {
         VehicleScript.Passenger var1 = new VehicleScript.Passenger();
         var1.id = this.id;

         int var2;
         for(var2 = 0; var2 < this.anims.size(); ++var2) {
            var1.anims.add(((VehicleScript.Anim)this.anims.get(var2)).makeCopy());
         }

         for(var2 = 0; var2 < this.switchSeats.size(); ++var2) {
            var1.switchSeats.add(((VehicleScript.Passenger.SwitchSeat)this.switchSeats.get(var2)).makeCopy());
         }

         var1.hasRoof = this.hasRoof;
         var1.showPassenger = this.showPassenger;
         var1.door = this.door;
         var1.door2 = this.door2;
         var1.area = this.area;

         for(var2 = 0; var2 < this.positions.size(); ++var2) {
            var1.positions.add(((VehicleScript.Position)this.positions.get(var2)).makeCopy());
         }

         return var1;
      }

      public int getPositionCount() {
         return this.positions.size();
      }

      public VehicleScript.Position getPosition(int var1) {
         return (VehicleScript.Position)this.positions.get(var1);
      }

      public VehicleScript.Position getPositionById(String var1) {
         for(int var2 = 0; var2 < this.positions.size(); ++var2) {
            VehicleScript.Position var3 = (VehicleScript.Position)this.positions.get(var2);
            if (var3.id != null && var3.id.equals(var1)) {
               return var3;
            }
         }

         return null;
      }

      public VehicleScript.Passenger.SwitchSeat getSwitchSeatById(String var1) {
         for(int var2 = 0; var2 < this.switchSeats.size(); ++var2) {
            VehicleScript.Passenger.SwitchSeat var3 = (VehicleScript.Passenger.SwitchSeat)this.switchSeats.get(var2);
            if (var3.id != null && var3.id.equals(var1)) {
               return var3;
            }
         }

         return null;
      }

      public static final class SwitchSeat {
         public String id;
         public int seat;
         public String anim;
         public float rate = 1.0F;
         public String sound;

         public String getId() {
            return this.id;
         }

         public VehicleScript.Passenger.SwitchSeat makeCopy() {
            VehicleScript.Passenger.SwitchSeat var1 = new VehicleScript.Passenger.SwitchSeat();
            var1.id = this.id;
            var1.seat = this.seat;
            var1.anim = this.anim;
            var1.rate = this.rate;
            var1.sound = this.sound;
            return var1;
         }
      }
   }

   public static final class PhysicsShape {
      public int type;
      public final Vector3f offset = new Vector3f();
      public final Vector3f rotate = new Vector3f();
      public final Vector3f extents = new Vector3f();
      public float radius;

      public String getTypeString() {
         switch(this.type) {
         case 1:
            return "box";
         case 2:
            return "sphere";
         default:
            throw new RuntimeException("unhandled VehicleScript.PhysicsShape");
         }
      }

      public Vector3f getOffset() {
         return this.offset;
      }

      public Vector3f getExtents() {
         return this.extents;
      }

      public Vector3f getRotate() {
         return this.rotate;
      }

      public float getRadius() {
         return this.radius;
      }

      public void setRadius(float var1) {
         this.radius = PZMath.clamp(var1, 0.05F, 5.0F);
      }
   }

   public static final class Wheel {
      public String id;
      public String model;
      public boolean front;
      public final Vector3f offset = new Vector3f();
      public float radius = 0.5F;
      public float width = 0.4F;

      public String getId() {
         return this.id;
      }

      public Vector3f getOffset() {
         return this.offset;
      }

      VehicleScript.Wheel makeCopy() {
         VehicleScript.Wheel var1 = new VehicleScript.Wheel();
         var1.id = this.id;
         var1.model = this.model;
         var1.front = this.front;
         var1.offset.set((Vector3fc)this.offset);
         var1.radius = this.radius;
         var1.width = this.width;
         return var1;
      }
   }

   public static final class Position {
      public String id;
      public final Vector3f offset = new Vector3f();
      public final Vector3f rotate = new Vector3f();
      public String area = null;

      public String getId() {
         return this.id;
      }

      public Vector3f getOffset() {
         return this.offset;
      }

      public Vector3f getRotate() {
         return this.rotate;
      }

      public String getArea() {
         return this.area;
      }

      VehicleScript.Position makeCopy() {
         VehicleScript.Position var1 = new VehicleScript.Position();
         var1.id = this.id;
         var1.offset.set((Vector3fc)this.offset);
         var1.rotate.set((Vector3fc)this.rotate);
         return var1;
      }
   }

   public static final class Container {
      public int capacity;
      public int seat = -1;
      public String seatID;
      public String luaTest;
      public String contentType;
      public boolean conditionAffectsCapacity = false;

      VehicleScript.Container makeCopy() {
         VehicleScript.Container var1 = new VehicleScript.Container();
         var1.capacity = this.capacity;
         var1.seat = this.seat;
         var1.seatID = this.seatID;
         var1.luaTest = this.luaTest;
         var1.contentType = this.contentType;
         var1.conditionAffectsCapacity = this.conditionAffectsCapacity;
         return var1;
      }
   }

   public static final class Anim {
      public String id;
      public String anim;
      public float rate = 1.0F;
      public boolean bAnimate = true;
      public boolean bLoop = false;
      public boolean bReverse = false;
      public final Vector3f offset = new Vector3f();
      public final Vector3f angle = new Vector3f();
      public String sound;

      VehicleScript.Anim makeCopy() {
         VehicleScript.Anim var1 = new VehicleScript.Anim();
         var1.id = this.id;
         var1.anim = this.anim;
         var1.rate = this.rate;
         var1.bAnimate = this.bAnimate;
         var1.bLoop = this.bLoop;
         var1.bReverse = this.bReverse;
         var1.offset.set((Vector3fc)this.offset);
         var1.angle.set((Vector3fc)this.angle);
         var1.sound = this.sound;
         return var1;
      }
   }

   public static final class Door {
      VehicleScript.Door makeCopy() {
         VehicleScript.Door var1 = new VehicleScript.Door();
         return var1;
      }
   }

   public static final class Window {
      public boolean openable;

      VehicleScript.Window makeCopy() {
         VehicleScript.Window var1 = new VehicleScript.Window();
         var1.openable = this.openable;
         return var1;
      }
   }
}
