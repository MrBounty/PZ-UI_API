---@class Field : java.lang.reflect.Field
Field = {}

---@return boolean
function Field:isTrustedFinal() end

---@private
---@param arg0 Class|Unknown
---@param arg1 Object
---@return void
function Field:checkAccess(arg0, arg1) end

---@public
---@return Type
function Field:getGenericType() end

---@return Field
function Field:copy() end

---@public
---@param arg0 Object
---@param arg1 float
---@return void
function Field:setFloat(arg0, arg1) end

---@public
---@return String
function Field:toString() end

---@public
---@param arg0 Object
---@return short
function Field:getShort(arg0) end

---@private
---@return Map|Unknown|Unknown
function Field:declaredAnnotations() end

---@public
---@param arg0 Class|Unknown
---@return Annotation[]
function Field:getAnnotationsByType(arg0) end

---@public
---@param arg0 Object
---@return boolean
function Field:equals(arg0) end

---@public
---@param arg0 Object
---@param arg1 double
---@return void
function Field:setDouble(arg0, arg1) end

---@private
---@return byte[]
function Field:getTypeAnnotationBytes0() end

---@public
---@param arg0 Object
---@return char
function Field:getChar(arg0) end

---@return Field
function Field:getRoot() end

---@public
---@param arg0 Object
---@param arg1 long
---@return void
function Field:setLong(arg0, arg1) end

---@return String
function Field:toShortString() end

---@public
---@return boolean
function Field:isEnumConstant() end

---@public
---@param arg0 Class|Unknown
---@return Annotation
function Field:getAnnotation(arg0) end

---@public
---@param arg0 Object
---@param arg1 int
---@return void
function Field:setInt(arg0, arg1) end

---@private
---@param arg0 FieldAccessor
---@param arg1 boolean
---@return void
function Field:setFieldAccessor(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 short
---@return void
function Field:setShort(arg0, arg1) end

---@public
---@param arg0 Object
---@return long
function Field:getLong(arg0) end

---@public
---@return Class|Unknown
function Field:getDeclaringClass() end

---@public
---@return String
function Field:toGenericString() end

---@public
---@param arg0 Object
---@param arg1 byte
---@return void
function Field:setByte(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 boolean
---@return void
function Field:setBoolean(arg0, arg1) end

---@public
---@return AnnotatedType
function Field:getAnnotatedType() end

---@private
---@param arg0 boolean
---@return FieldAccessor
---@overload fun(arg0:Object)
function Field:getFieldAccessor(arg0) end

---@private
---@param arg0 Object
---@return FieldAccessor
function Field:getFieldAccessor(arg0) end

---@public
---@param arg0 Object
---@return Object
function Field:get(arg0) end

---@public
---@return String
function Field:getName() end

---@private
---@return String
function Field:getGenericSignature() end

---@public
---@return Annotation[]
function Field:getDeclaredAnnotations() end

---@param arg0 Class|Unknown
---@return void
function Field:checkCanSetAccessible(arg0) end

---@private
---@return FieldRepository
function Field:getGenericInfo() end

---@public
---@param arg0 Object
---@param arg1 char
---@return void
function Field:setChar(arg0, arg1) end

---@public
---@return Class|Unknown
function Field:getType() end

---@public
---@param arg0 boolean
---@return void
function Field:setAccessible(arg0) end

---@public
---@param arg0 Object
---@return int
function Field:getInt(arg0) end

---@public
---@param arg0 Object
---@return double
function Field:getDouble(arg0) end

---@public
---@return int
function Field:getModifiers() end

---@public
---@return boolean
function Field:isSynthetic() end

---@public
---@param arg0 Object
---@param arg1 Object
---@return void
function Field:set(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
function Field:getBoolean(arg0) end

---@private
---@param arg0 boolean
---@return FieldAccessor
function Field:acquireFieldAccessor(arg0) end

---@private
---@return GenericsFactory
function Field:getFactory() end

---@public
---@return int
function Field:hashCode() end

---@public
---@param arg0 Object
---@return float
function Field:getFloat(arg0) end

---@public
---@param arg0 Object
---@return byte
function Field:getByte(arg0) end
