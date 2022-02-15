package se.krka.kahlua.integration.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import se.krka.kahlua.integration.annotations.LuaConstructor;
import se.krka.kahlua.integration.annotations.LuaMethod;

public class LuaDebugDataProcessor implements Processor, ElementVisitor {
   private HashMap classes;
   private Filer filer;

   public Iterable getCompletions(Element var1, AnnotationMirror var2, ExecutableElement var3, String var4) {
      return new HashSet();
   }

   public Set getSupportedAnnotationTypes() {
      HashSet var1 = new HashSet();
      var1.add(LuaMethod.class.getName());
      var1.add(LuaConstructor.class.getName());
      return var1;
   }

   public Set getSupportedOptions() {
      return new HashSet();
   }

   public SourceVersion getSupportedSourceVersion() {
      return SourceVersion.latest();
   }

   public void init(ProcessingEnvironment var1) {
      this.filer = var1.getFiler();
      this.classes = new HashMap();
   }

   public boolean process(Set var1, RoundEnvironment var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TypeElement var4 = (TypeElement)var3.next();
         Set var5 = var2.getElementsAnnotatedWith(var4);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            Element var7 = (Element)var6.next();
            var7.accept(this, (Object)null);
         }
      }

      if (var2.processingOver()) {
         try {
            this.store();
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }

      return true;
   }

   public Void visit(Element var1) {
      return null;
   }

   public Void visit(Element var1, Void var2) {
      return null;
   }

   public Void visitExecutable(ExecutableElement var1, Void var2) {
      String var3 = this.findClass(var1);
      String var4 = this.findPackage(var1);
      ClassParameterInformation var5 = this.getOrCreate(this.classes, var3, var4, this.findSimpleClassName(var1));
      String var6 = var1.getSimpleName().toString();
      String var7 = DescriptorUtil.getDescriptor(var6, var1.getParameters());
      ArrayList var8 = new ArrayList();
      Iterator var9 = var1.getParameters().iterator();

      while(var9.hasNext()) {
         VariableElement var10 = (VariableElement)var9.next();
         var8.add(var10.getSimpleName().toString());
      }

      MethodParameterInformation var11 = new MethodParameterInformation(var8);
      var5.methods.put(var7, var11);
      return null;
   }

   private ClassParameterInformation getOrCreate(HashMap var1, String var2, String var3, String var4) {
      ClassParameterInformation var5 = (ClassParameterInformation)var1.get(var2);
      if (var5 == null) {
         var5 = new ClassParameterInformation(var3, var4);
         var1.put(var2, var5);
      }

      return var5;
   }

   private String findClass(Element var1) {
      return var1.getKind() == ElementKind.CLASS ? var1.toString() : this.findClass(var1.getEnclosingElement());
   }

   private String findSimpleClassName(Element var1) {
      if (var1.getKind() == ElementKind.CLASS) {
         String var2 = var1.getSimpleName().toString();
         if (var1.getEnclosingElement().getKind() == ElementKind.CLASS) {
            String var10000 = this.findSimpleClassName(var1.getEnclosingElement());
            return var10000 + "_" + var2;
         } else {
            return var2;
         }
      } else {
         return this.findSimpleClassName(var1.getEnclosingElement());
      }
   }

   private String findPackage(Element var1) {
      return var1.getKind() == ElementKind.PACKAGE ? var1.toString() : this.findPackage(var1.getEnclosingElement());
   }

   public Void visitPackage(PackageElement var1, Void var2) {
      return null;
   }

   public Void visitType(TypeElement var1, Void var2) {
      return null;
   }

   public Void visitVariable(VariableElement var1, Void var2) {
      return null;
   }

   public Void visitTypeParameter(TypeParameterElement var1, Void var2) {
      return null;
   }

   public Void visitUnknown(Element var1, Void var2) {
      return null;
   }

   private void store() throws IOException {
      Iterator var1 = this.classes.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         ClassParameterInformation var3 = (ClassParameterInformation)var2.getValue();
         Object var4 = null;
         FileObject var5 = this.filer.createResource(StandardLocation.CLASS_OUTPUT, var3.getPackageName(), var3.getSimpleClassName() + ".luadebugdata", (Element[])var4);
         OutputStream var6 = var5.openOutputStream();
         var3.saveToStream(var6);
         var6.close();
      }

   }
}
