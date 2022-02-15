package zombie.iso;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjglx.BufferUtils;
import zombie.GameTime;
import zombie.core.SpriteRenderer;
import zombie.core.VBO.GLBufferObject15;
import zombie.core.VBO.GLBufferObjectARB;
import zombie.core.VBO.IGLBufferObject;
import zombie.core.opengl.RenderThread;
import zombie.debug.DebugLog;

public abstract class Particles {
   private float ParticlesTime;
   public static int ParticleSystemsCount = 0;
   public static int ParticleSystemsLast = 0;
   public static final ArrayList ParticleSystems = new ArrayList();
   private int id;
   int particle_vertex_buffer;
   public static IGLBufferObject funcs = null;
   private Matrix4f projectionMatrix;
   private Matrix4f mvpMatrix;
   private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

   public static synchronized int addParticle(Particles var0) {
      if (ParticleSystems.size() == ParticleSystemsCount) {
         ParticleSystems.add(var0);
         ++ParticleSystemsCount;
         return ParticleSystems.size() - 1;
      } else {
         int var1 = ParticleSystemsLast;
         if (var1 < ParticleSystems.size()) {
            if (ParticleSystems.get(var1) == null) {
               ParticleSystemsLast = var1;
               ParticleSystems.set(var1, var0);
               ++ParticleSystemsCount;
            }

            return var1;
         } else {
            byte var2 = 0;
            if (var2 < ParticleSystemsLast) {
               if (ParticleSystems.get(var2) == null) {
                  ParticleSystemsLast = var2;
                  ParticleSystems.set(var2, var0);
                  ++ParticleSystemsCount;
               }

               return var2;
            } else {
               DebugLog.log("ERROR: addParticle has unknown error");
               return -1;
            }
         }
      }
   }

   public static synchronized void deleteParticle(int var0) {
      ParticleSystems.set(var0, (Object)null);
      --ParticleSystemsCount;
   }

   public static void init() {
      if (funcs == null) {
         if (!GL.getCapabilities().OpenGL33) {
            System.out.println("OpenGL 3.3 don't supported");
         }

         if (GL.getCapabilities().OpenGL15) {
            System.out.println("OpenGL 1.5 buffer objects supported");
            funcs = new GLBufferObject15();
         } else {
            if (!GL.getCapabilities().GL_ARB_vertex_buffer_object) {
               throw new RuntimeException("Neither OpenGL 1.5 nor GL_ARB_vertex_buffer_object supported");
            }

            System.out.println("GL_ARB_vertex_buffer_object supported");
            funcs = new GLBufferObjectARB();
         }

      }
   }

   public void initBuffers() {
      ByteBuffer var1 = MemoryUtil.memAlloc(48);
      var1.clear();
      var1.putFloat(-1.0F);
      var1.putFloat(-1.0F);
      var1.putFloat(0.0F);
      var1.putFloat(1.0F);
      var1.putFloat(-1.0F);
      var1.putFloat(0.0F);
      var1.putFloat(-1.0F);
      var1.putFloat(1.0F);
      var1.putFloat(0.0F);
      var1.putFloat(1.0F);
      var1.putFloat(1.0F);
      var1.putFloat(0.0F);
      var1.flip();
      this.particle_vertex_buffer = funcs.glGenBuffers();
      funcs.glBindBuffer(34962, this.particle_vertex_buffer);
      funcs.glBufferData(34962, var1, 35044);
      MemoryUtil.memFree(var1);
      this.createParticleBuffers();
   }

   public void destroy() {
      deleteParticle(this.id);
      funcs.glDeleteBuffers(this.particle_vertex_buffer);
      this.destroyParticleBuffers();
   }

   public abstract void reloadShader();

   public Particles() {
      RenderThread.invokeOnRenderContext(() -> {
         init();
         this.initBuffers();
         this.projectionMatrix = new Matrix4f();
      });
      this.reloadShader();
      this.id = addParticle(this);
   }

   private static Matrix4f orthogonal(float var0, float var1, float var2, float var3, float var4, float var5) {
      Matrix4f var6 = new Matrix4f();
      var6.setIdentity();
      var6.m00 = 2.0F / (var1 - var0);
      var6.m11 = 2.0F / (var3 - var2);
      var6.m22 = -2.0F / (var5 - var4);
      var6.m32 = (-var5 - var4) / (var5 - var4);
      var6.m30 = (-var1 - var0) / (var1 - var0);
      var6.m31 = (-var3 - var2) / (var3 - var2);
      return var6;
   }

   public void render() {
      int var1 = IsoCamera.frameState.playerIndex;
      this.ParticlesTime += 0.0166F * GameTime.getInstance().getMultiplier();
      this.updateMVPMatrix();
      SpriteRenderer.instance.drawParticles(var1, 0, 0);
   }

   private void updateMVPMatrix() {
      this.projectionMatrix = orthogonal(IsoCamera.frameState.OffX, IsoCamera.frameState.OffX + (float)IsoCamera.frameState.OffscreenWidth, IsoCamera.frameState.OffY + (float)IsoCamera.frameState.OffscreenHeight, IsoCamera.frameState.OffY, -1.0F, 1.0F);
      this.mvpMatrix = this.projectionMatrix;
   }

   public FloatBuffer getMVPMatrix() {
      this.floatBuffer.clear();
      this.floatBuffer.put(this.mvpMatrix.m00);
      this.floatBuffer.put(this.mvpMatrix.m10);
      this.floatBuffer.put(this.mvpMatrix.m20);
      this.floatBuffer.put(this.mvpMatrix.m30);
      this.floatBuffer.put(this.mvpMatrix.m01);
      this.floatBuffer.put(this.mvpMatrix.m11);
      this.floatBuffer.put(this.mvpMatrix.m21);
      this.floatBuffer.put(this.mvpMatrix.m31);
      this.floatBuffer.put(this.mvpMatrix.m02);
      this.floatBuffer.put(this.mvpMatrix.m12);
      this.floatBuffer.put(this.mvpMatrix.m22);
      this.floatBuffer.put(this.mvpMatrix.m32);
      this.floatBuffer.put(this.mvpMatrix.m03);
      this.floatBuffer.put(this.mvpMatrix.m13);
      this.floatBuffer.put(this.mvpMatrix.m23);
      this.floatBuffer.put(this.mvpMatrix.m33);
      this.floatBuffer.flip();
      return this.floatBuffer;
   }

   public void getGeometry(int var1) {
      this.updateParticleParams();
      GL20.glEnableVertexAttribArray(0);
      funcs.glBindBuffer(34962, this.particle_vertex_buffer);
      GL20.glVertexAttribPointer(0, 3, 5126, false, 0, 0L);
      GL33.glVertexAttribDivisor(0, 0);
      GL31.glDrawArraysInstanced(5, 0, 4, this.getParticleCount());
   }

   public void getGeometryFire(int var1) {
      this.updateParticleParams();
      GL20.glEnableVertexAttribArray(0);
      funcs.glBindBuffer(34962, this.particle_vertex_buffer);
      GL20.glVertexAttribPointer(0, 3, 5126, false, 0, 0L);
      GL33.glVertexAttribDivisor(0, 0);
      GL31.glDrawArraysInstanced(5, 0, 4, this.getParticleCount());
   }

   public float getShaderTime() {
      return this.ParticlesTime;
   }

   abstract void createParticleBuffers();

   abstract void destroyParticleBuffers();

   abstract void updateParticleParams();

   abstract int getParticleCount();
}
