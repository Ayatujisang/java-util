package com.kk.utils.reflect;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 反射工具类，method,field的封装
 * <p/>
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * <p/>
 * <p/>
 * 推荐使用 BeanUtils，PropertyUtils（commons-beanutils）
 *
 * FieldUtils.getField(model.getClass(), "fieldName", true)，getField 会找父类field，getDeclaredField不会找父类field。
 *
 * <p/>
 * 推荐使用   BeanWrapper beanWrapper = new BeanWrapperImpl(bean) （spring-beans）
 *
 * Reflections,FieldUtils 设置fieldValue时候，value类型需要和field类型一致，否则抛出异常
 * BeanWrapper 可以自动识别类型。
 * @author calvin
 */
public class Reflections {
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Log logger = LogFactory.getLog(Reflections.class);

    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    //  自动识别value和field类型
    public static void setFieldValueAutoDetectType(Object obj, String fieldName, Object value) {
        Field field = getAccessibleField(obj, fieldName);
        Class<?> typeClass = field.getType();
        if (String.class.isAssignableFrom(typeClass)) {
            setFieldValue(obj, fieldName, value.toString());
        } else if (Long.class.isAssignableFrom(typeClass) || long.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Long.valueOf(value.toString()));
            }
        } else if (Integer.class.isAssignableFrom(typeClass) || int.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Integer.valueOf(value.toString()));
            }
        } else if (Boolean.class.isAssignableFrom(typeClass) || boolean.class.isAssignableFrom(typeClass)) {
            if (value != null) {
                setFieldValue(obj, fieldName, Boolean.valueOf(value.toString()));
            }
        } else if (Double.class.isAssignableFrom(typeClass) || double.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Double.valueOf(value.toString()));
            }
        } else if (Float.class.isAssignableFrom(typeClass) || float.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Float.valueOf(value.toString()));
            }
        } else if (Short.class.isAssignableFrom(typeClass) || short.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Short.valueOf(value.toString()));
            }
        } else if (Byte.class.isAssignableFrom(typeClass) || byte.class.isAssignableFrom(typeClass)) {
            if (StringUtils.isNumeric(value.toString())) {
                setFieldValue(obj, fieldName, Byte.valueOf(value.toString()));
            }
        } else {
            setFieldValue(obj, fieldName, value);
        }
    }

    /**
     * 调用Getter方法.
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = GETTER_PREFIX + capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{},
                new Object[]{});
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     */
    public static void invokeSetter(Object obj, String propertyName,
                                    Object value) {
        String setterMethodName = SETTER_PREFIX + capitalize(propertyName);
        invokeMethodByName(obj, setterMethodName, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName,
                                     final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:" + e.getMessage(), e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj,
                                      final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method ["
                    + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj,
                                            final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method ["
                    + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj,
                                           final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p/>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethod(final Object obj,
                                             final String methodName, final Class<?>... parameterTypes) {

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName,
                        parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
     * <p/>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethodByName(final Object obj,
                                                   final String methodName) {

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
                .getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier
                .isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p/>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName()
                    + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            logger.warn("Index: " + index + ", Size of "
                    + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName()
                    + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    public static Class<?> getUserClass(Object instance) {
        Class clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(
            Exception e) {
        if ((e instanceof IllegalAccessException)
                || (e instanceof IllegalArgumentException)
                || (e instanceof NoSuchMethodException)) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(
                    ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    // java 内省方法，  代替get，set

    /**
     * 　Introspector类:
     * 　　将JavaBean中的属性封装起来进行操作。在程序把一个类当做JavaBean来看，
     * 就是调用Introspector.getBeanInfo()方法，得到的BeanInfo对象封装了把这个类当做JavaBean看的结果信息，即属性的信息。
     * 　　getPropertyDescriptors()，获得属性的描述，可以采用遍历BeanInfo的方法，来查找、设置类的属性。
     */
    public static void setPropertyByIntrospector(Object object, String prop, Object value) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        // 所有的 内省方法
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(prop)) {
                    Method method = propDesc.getWriteMethod();
                    method.invoke(object, value);
                    break;
                }
            }
        }
    }

    //如果prop找不到则返回null
    public static Object getPropertyByIntrospector(Object object, String prop) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(prop)) {
                    Method method = propDesc.getReadMethod();
                    return method.invoke(object);
                }
            }
        }
        return null;
    }

    /**
     * PropertyDescriptor类:
     * 　　PropertyDescriptor类表示JavaBean类通过存储器导出一个属性。主要方法：
     * 　　1. getPropertyType()，获得属性的Class对象;
     * 　　2. getReadMethod()，获得用于读取属性值的方法；getWriteMethod()，获得用于写入属性值的方法;
     * 　　3. hashCode()，获取对象的哈希值;
     * 　　4. setReadMethod(Method readMethod)，设置用于读取属性值的方法;
     * 　　5. setWriteMethod(Method writeMethod)，设置用于写入属性值的方法。
     * 　　6. getName(Method writeMethod)，属性名称
     */
    public static void setProperty(Object object, String prop, Object value) throws Exception {
        //生成一个 内省方法
        PropertyDescriptor propDesc = new PropertyDescriptor(prop, object.getClass());
        Method method = propDesc.getWriteMethod();
        method.invoke(object, value);
    }

    // 如果prop找不到则抛出异常
    public static Object getProperty(Object object, String prop) throws Exception {
        PropertyDescriptor proDescriptor = new PropertyDescriptor(prop, object.getClass());
        Method method = proDescriptor.getReadMethod();
        Object value = method.invoke(object);
        return value;
    }
}
