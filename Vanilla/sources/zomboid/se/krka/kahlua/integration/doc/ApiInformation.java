package se.krka.kahlua.integration.doc;

import java.util.List;

public interface ApiInformation {
   List getAllClasses();

   List getRootClasses();

   List getChildrenForClass(Class var1);

   List getMethodsForClass(Class var1);

   List getFunctionsForClass(Class var1);
}
