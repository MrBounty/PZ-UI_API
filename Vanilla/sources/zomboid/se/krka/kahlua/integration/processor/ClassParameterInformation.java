package se.krka.kahlua.integration.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassParameterInformation implements Serializable {
   private static final long serialVersionUID = 7634190901254143200L;
   private final String packageName;
   private final String simpleClassName;
   public Map methods = new HashMap();

   private ClassParameterInformation() {
      this.packageName = null;
      this.simpleClassName = null;
   }

   public ClassParameterInformation(String var1, String var2) {
      this.packageName = var1;
      this.simpleClassName = var2;
   }

   public ClassParameterInformation(Class var1) {
      Package var2 = var1.getPackage();
      this.packageName = var2 == null ? null : var2.getName();
      this.simpleClassName = var1.getSimpleName();
      Constructor[] var3 = var1.getConstructors();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         Constructor var6 = var3[var5];
         this.methods.put(DescriptorUtil.getDescriptor(var6), MethodParameterInformation.EMPTY);
      }

      Method[] var7 = var1.getMethods();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         Method var8 = var7[var5];
         this.methods.put(DescriptorUtil.getDescriptor(var8), MethodParameterInformation.EMPTY);
      }

   }

   public String getPackageName() {
      return this.packageName;
   }

   public String getSimpleClassName() {
      return this.simpleClassName;
   }

   public String getFullClassName() {
      return this.packageName != null && !this.packageName.equals("") ? this.packageName + "." + this.simpleClassName : this.simpleClassName;
   }

   public static ClassParameterInformation getFromStream(Class var0) throws IOException, ClassNotFoundException {
      String var1 = getFileName(var0);
      InputStream var2 = var0.getResourceAsStream(var1);
      if (var2 == null) {
         return null;
      } else {
         ObjectInputStream var3 = new ObjectInputStream(var2);
         return (ClassParameterInformation)var3.readObject();
      }
   }

   private static String getFileName(Class var0) {
      String var10000 = var0.getPackage().getName().replace('.', '/');
      return "/" + var10000 + "/" + getSimpleName(var0) + ".luadebugdata";
   }

   private static String getSimpleName(Class var0) {
      if (var0.getEnclosingClass() != null) {
         String var10000 = getSimpleName(var0.getEnclosingClass());
         return var10000 + "_" + var0.getSimpleName();
      } else {
         return var0.getSimpleName();
      }
   }

   public void saveToStream(OutputStream var1) throws IOException {
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(this);
   }

   public String getFileName() {
      return getFileName(this.getClass());
   }
}
