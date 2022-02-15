package zombie.iso;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;
import org.lwjglx.BufferUtils;
import zombie.core.Rand;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.textures.Texture;
import zombie.interfaces.ITexture;
import zombie.iso.weather.ClimateManager;

public final class ParticlesFire extends Particles {
   int MaxParticles = 1000000;
   int MaxVortices = 4;
   int particles_data_buffer;
   ByteBuffer particule_data;
   private Texture texFireSmoke;
   private Texture texFlameFire;
   public FireShader EffectFire;
   public SmokeShader EffectSmoke;
   public Shader EffectVape;
   float windX;
   float windY;
   private static ParticlesFire instance;
   private ParticlesArray particles = new ParticlesArray();
   private ArrayList zones = new ArrayList();
   private int intensityFire = 0;
   private int intensitySmoke = 0;
   private int intensitySteam = 0;
   private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

   public static synchronized ParticlesFire getInstance() {
      if (instance == null) {
         instance = new ParticlesFire();
      }

      return instance;
   }

   public ParticlesFire() {
      this.particule_data = BufferUtils.createByteBuffer(this.MaxParticles * 4 * 4);
      this.texFireSmoke = Texture.getSharedTexture("media/textures/FireSmokes.png");
      this.texFlameFire = Texture.getSharedTexture("media/textures/FireFlame.png");
      this.zones.clear();
      float var1 = (float)((int)(IsoCamera.frameState.OffX + (float)(IsoCamera.frameState.OffscreenWidth / 2)));
      float var2 = (float)((int)(IsoCamera.frameState.OffY + (float)(IsoCamera.frameState.OffscreenHeight / 2)));
      this.zones.add(new ParticlesFire.Zone(10, var1 - 30.0F, var2 - 10.0F, var1 + 30.0F, var2 + 10.0F));
      this.zones.add(new ParticlesFire.Zone(10, var1 - 200.0F, var2, 50.0F));
      this.zones.add(new ParticlesFire.Zone(40, var1 + 200.0F, var2, 100.0F));
      this.zones.add(new ParticlesFire.Zone(60, var1 - 150.0F, var2 - 300.0F, var1 + 250.0F, var2 - 300.0F, 10.0F));
      this.zones.add(new ParticlesFire.Zone(10, var1 - 350.0F, var2 - 200.0F, var1 - 350.0F, var2 - 300.0F, 10.0F));
   }

   private void ParticlesProcess() {
      for(int var1 = 0; var1 < this.zones.size(); ++var1) {
         ParticlesFire.Zone var2 = (ParticlesFire.Zone)this.zones.get(var1);
         int var3 = (int)Math.ceil((double)((float)(var2.intensity - var2.currentParticles) * 0.1F));
         int var4;
         ParticlesFire.Particle var5;
         if (var2.type == ParticlesFire.ZoneType.Rectangle) {
            for(var4 = 0; var4 < var3; ++var4) {
               var5 = new ParticlesFire.Particle();
               var5.x = Rand.Next(var2.x0, var2.x1);
               var5.y = Rand.Next(var2.y0, var2.y1);
               var5.vx = Rand.Next(-3.0F, 3.0F);
               var5.vy = Rand.Next(1.0F, 5.0F);
               var5.tShift = 0.0F;
               var5.id = Rand.Next(-1000000.0F, 1000000.0F);
               var5.zone = var2;
               ++var2.currentParticles;
               this.particles.addParticle(var5);
            }
         }

         float var6;
         float var7;
         if (var2.type == ParticlesFire.ZoneType.Circle) {
            for(var4 = 0; var4 < var3; ++var4) {
               var5 = new ParticlesFire.Particle();
               var6 = Rand.Next(0.0F, 6.2831855F);
               var7 = Rand.Next(0.0F, var2.r);
               var5.x = (float)((double)var2.x0 + (double)var7 * Math.cos((double)var6));
               var5.y = (float)((double)var2.y0 + (double)var7 * Math.sin((double)var6));
               var5.vx = Rand.Next(-3.0F, 3.0F);
               var5.vy = Rand.Next(1.0F, 5.0F);
               var5.tShift = 0.0F;
               var5.id = Rand.Next(-1000000.0F, 1000000.0F);
               var5.zone = var2;
               ++var2.currentParticles;
               this.particles.addParticle(var5);
            }
         }

         if (var2.type == ParticlesFire.ZoneType.Line) {
            for(var4 = 0; var4 < var3; ++var4) {
               var5 = new ParticlesFire.Particle();
               var6 = Rand.Next(0.0F, 6.2831855F);
               var7 = Rand.Next(0.0F, var2.r);
               float var8 = Rand.Next(0.0F, 1.0F);
               var5.x = (float)((double)(var2.x0 * var8 + var2.x1 * (1.0F - var8)) + (double)var7 * Math.cos((double)var6));
               var5.y = (float)((double)(var2.y0 * var8 + var2.y1 * (1.0F - var8)) + (double)var7 * Math.sin((double)var6));
               var5.vx = Rand.Next(-3.0F, 3.0F);
               var5.vy = Rand.Next(1.0F, 5.0F);
               var5.tShift = 0.0F;
               var5.id = Rand.Next(-1000000.0F, 1000000.0F);
               var5.zone = var2;
               ++var2.currentParticles;
               this.particles.addParticle(var5);
            }
         }

         if (var3 < 0) {
            for(var4 = 0; var4 < -var3; ++var4) {
               --var2.currentParticles;
               this.particles.deleteParticle(Rand.Next(0, this.particles.getCount() + 1));
            }
         }
      }

   }

   public FloatBuffer getParametersFire() {
      this.floatBuffer.clear();
      this.floatBuffer.put(this.windX);
      this.floatBuffer.put((float)this.intensityFire);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(this.windY);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.flip();
      return this.floatBuffer;
   }

   public int getFireShaderID() {
      return this.EffectFire.getID();
   }

   public int getSmokeShaderID() {
      return this.EffectSmoke.getID();
   }

   public int getVapeShaderID() {
      return this.EffectVape.getID();
   }

   public ITexture getFireFlameTexture() {
      return this.texFlameFire;
   }

   public ITexture getFireSmokeTexture() {
      return this.texFireSmoke;
   }

   public void reloadShader() {
      RenderThread.invokeOnRenderContext(() -> {
         this.EffectFire = new FireShader("fire");
         this.EffectSmoke = new SmokeShader("smoke");
         this.EffectVape = new Shader("vape");
      });
   }

   void createParticleBuffers() {
      this.particles_data_buffer = funcs.glGenBuffers();
      funcs.glBindBuffer(34962, this.particles_data_buffer);
      funcs.glBufferData(34962, (long)(this.MaxParticles * 4 * 4), 35044);
   }

   void destroyParticleBuffers() {
      funcs.glDeleteBuffers(this.particles_data_buffer);
   }

   void updateParticleParams() {
      float var1 = ClimateManager.getInstance().getWindAngleIntensity();
      float var2 = ClimateManager.getInstance().getWindIntensity();
      this.windX = (float)Math.sin((double)(var1 * 6.0F)) * var2;
      this.windY = (float)Math.cos((double)(var1 * 6.0F)) * var2;
      this.ParticlesProcess();
      if (this.particles.getNeedToUpdate()) {
         this.particles.defragmentParticle();
         this.particule_data.clear();

         for(int var3 = 0; var3 < this.particles.size(); ++var3) {
            ParticlesFire.Particle var4 = (ParticlesFire.Particle)this.particles.get(var3);
            if (var4 != null) {
               this.particule_data.putFloat(var4.x);
               this.particule_data.putFloat(var4.y);
               this.particule_data.putFloat(var4.id);
               this.particule_data.putFloat((float)var3 / (float)this.particles.size());
            }
         }

         this.particule_data.flip();
      }

      funcs.glBindBuffer(34962, this.particles_data_buffer);
      funcs.glBufferData(34962, this.particule_data, 35040);
      GL20.glEnableVertexAttribArray(1);
      funcs.glBindBuffer(34962, this.particles_data_buffer);
      GL20.glVertexAttribPointer(1, 4, 5126, false, 0, 0L);
      GL33.glVertexAttribDivisor(1, 1);
   }

   int getParticleCount() {
      return this.particles.getCount();
   }

   public class Zone {
      ParticlesFire.ZoneType type;
      int intensity;
      int currentParticles;
      float x0;
      float y0;
      float x1;
      float y1;
      float r;
      float fireIntensity;
      float smokeIntensity;
      float sparksIntensity;
      float vortices;
      float vorticeSpeed;
      float area;
      float temperature;
      float centerX;
      float centerY;
      float centerRp2;
      float currentVorticesCount;

      Zone(int var2, float var3, float var4, float var5) {
         this.type = ParticlesFire.ZoneType.Circle;
         this.intensity = var2;
         this.currentParticles = 0;
         this.x0 = var3;
         this.y0 = var4;
         this.r = var5;
         this.area = (float)(3.141592653589793D * (double)var5 * (double)var5);
         this.vortices = (float)this.intensity * 0.3F;
         this.vorticeSpeed = 0.5F;
         this.temperature = 2000.0F;
         this.centerX = var3;
         this.centerY = var4;
         this.centerRp2 = var5 * var5;
      }

      Zone(int var2, float var3, float var4, float var5, float var6) {
         this.type = ParticlesFire.ZoneType.Rectangle;
         this.intensity = var2;
         this.currentParticles = 0;
         if (var3 < var5) {
            this.x0 = var3;
            this.x1 = var5;
         } else {
            this.x1 = var3;
            this.x0 = var5;
         }

         if (var4 < var6) {
            this.y0 = var4;
            this.y1 = var6;
         } else {
            this.y1 = var4;
            this.y0 = var6;
         }

         this.area = (this.x1 - this.x0) * (this.y1 - this.y0);
         this.vortices = (float)this.intensity * 0.3F;
         this.vorticeSpeed = 0.5F;
         this.temperature = 2000.0F;
         this.centerX = (this.x0 + this.x1) * 0.5F;
         this.centerY = (this.y0 + this.y1) * 0.5F;
         this.centerRp2 = (this.x1 - this.x0) * (this.x1 - this.x0);
      }

      Zone(int var2, float var3, float var4, float var5, float var6, float var7) {
         this.type = ParticlesFire.ZoneType.Line;
         this.intensity = var2;
         this.currentParticles = 0;
         if (var3 < var5) {
            this.x0 = var3;
            this.x1 = var5;
            this.y0 = var4;
            this.y1 = var6;
         } else {
            this.x1 = var3;
            this.x0 = var5;
            this.y1 = var4;
            this.y0 = var6;
         }

         this.r = var7;
         this.area = (float)((double)this.r * Math.sqrt(Math.pow((double)(var3 - var5), 2.0D) + Math.pow((double)(var4 - var6), 2.0D)));
         this.vortices = (float)this.intensity * 0.3F;
         this.vorticeSpeed = 0.5F;
         this.temperature = 2000.0F;
         this.centerX = (this.x0 + this.x1) * 0.5F;
         this.centerY = (this.y0 + this.y1) * 0.5F;
         this.centerRp2 = (this.x1 - this.x0 + var7) * (this.x1 - this.x0 + var7) * 100.0F;
      }
   }

   static enum ZoneType {
      Rectangle,
      Circle,
      Line;

      // $FF: synthetic method
      private static ParticlesFire.ZoneType[] $values() {
         return new ParticlesFire.ZoneType[]{Rectangle, Circle, Line};
      }
   }

   public class Particle {
      float id;
      float x;
      float y;
      float tShift;
      float vx;
      float vy;
      ParticlesFire.Zone zone;
   }

   public class Vortice {
      float x;
      float y;
      float z;
      float size;
      float vx;
      float vy;
      float speed;
      int life;
      int lifeTime;
      ParticlesFire.Zone zone;
   }
}
