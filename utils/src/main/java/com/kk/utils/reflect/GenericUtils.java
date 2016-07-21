package com.kk.utils.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 实现工具类，检查参数化类型的参数类型。
 * <p/>
 * 1.返回类型List<E>、Map<K,V>中的E、K、V
 * <p/>
 * 2.收集类的所有常量
 */
@SuppressWarnings({"rawtypes"})
public class GenericUtils {

    private static final Class[] EMPTY_CLASSES = new Class[0];


    /**
     * 从参数, 返回值, 基类的: Generic 类型信息获取传入的实际类信息。 获取list，map里面的泛型
     * <p/>
     * 调用方式：getActualClass(method.getGenericReturnType())
     *
     * @param genericType - Generic 类型信息
     * @return 实际类信息
     */
    public static Class<?>[] getActualClass(Type genericType) {

        if (genericType instanceof ParameterizedType) {

            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class<?>[] actualClasses = new Class<?>[actualTypes.length];

            for (int i = 0; i < actualTypes.length; i++) {
                Type actualType = actualTypes[i];
                if (actualType instanceof Class<?>) {
                    actualClasses[i] = (Class<?>) actualType;
                } else if (actualType instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
                    actualClasses[i] = Array.newInstance((Class<?>) componentType, 0).getClass();
                }
            }

            return actualClasses;
        }

        return EMPTY_CLASSES;
    }

    /**
     * 返回类型List<E>、Map<K,V>中的E、K、V
     * <p/>
     * 调用样例
     * method.getDeclaringClass(), method.getGenericReturnType()
     *
     * @param invocationClass
     * @param targetType
     */
    public static Class[] resolveTypeParameters(Class invocationClass, Type targetType) {
        if (targetType instanceof ParameterizedType) {

            Type[] actualTypes = ((ParameterizedType) targetType).getActualTypeArguments();
            Class[] actualClasses = new Class[actualTypes.length];
            for (int i = 0; i < actualTypes.length; i++) {
                actualClasses[i] = resolveTypeVariable(invocationClass, actualTypes[i]);
            }
            return actualClasses;
        }

        return EMPTY_CLASSES;
    }

    /**
     * 求declaringClass类中声明的泛型类型变量在invocationClass中真正的值
     *
     * @param invocationClass 编程时使用的类
     * @param declaringClass  声明类型变量typeVarName的类
     * @param typeVarName     泛型变量名
     * @return
     */
    public static final Class resolveTypeVariable(Class invocationClass, Class declaringClass,
                                                  String typeVarName) {
        TypeVariable typeVariable = null;
        for (TypeVariable typeParemeter : declaringClass.getTypeParameters()) {
            if (typeParemeter.getName().equals(typeVarName)) {
                typeVariable = typeParemeter;
                break;
            }
        }
        if (typeVariable == null) {
            throw new NullPointerException("not found TypeVariable name " + typeVarName);
        }
        return resolveTypeVariable(invocationClass, typeVariable);
    }

    /**
     * 求给定的invocationClass类中，目标类型type的值
     * <p/>
     * 如果type已是类型，则直接返回它；
     * 如果type是类型变量( {@link TypeVariable})，则求他的值；
     *
     * @param invocationClass
     * @param targetType
     * @return
     */
    public static final Class resolveTypeVariable(Class invocationClass, Type targetType) {
        if (targetType == null) {
            throw new NullPointerException("TypeVariable is null");
        }
        // Class类型
        if (targetType instanceof Class) {
            return (Class) targetType;
        }
        // 参数容器类型（注意：返回的是容器类型，而非具体的参数类型。不要混淆！）
        if (targetType instanceof ParameterizedType) {
            return resolveTypeVariable(invocationClass,
                    (Type) ((ParameterizedType) targetType).getRawType());
        }
        // 数组类型
        if (targetType instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) targetType).getGenericComponentType();
            componentType = resolveTypeVariable(invocationClass, componentType);
            return Array.newInstance((Class) componentType, 0).getClass();
        }

        Map<TypeVariable, Type> refs = new HashMap<TypeVariable, Type>();

        // 
        List<Type> allSuperTypes = new LinkedList<Type>();
        allSuperTypes.addAll(Arrays.asList(invocationClass.getGenericInterfaces()));
        for (int i = 0; i < allSuperTypes.size(); i++) {
            Type type = allSuperTypes.get(i);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = ((ParameterizedType) type);
                Class interfaceClass = (Class) parameterizedType.getRawType();
                int j = 0;
                for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                    TypeVariable v = interfaceClass.getTypeParameters()[j++];
                    refs.put(v, actualTypeArgument);
                    // System.out.println("put " + v + "  --->  " + actualTypeArgument);
                }

                for (Type t : interfaceClass.getGenericInterfaces()) {
                    if (!allSuperTypes.contains(t)) {
                        allSuperTypes.add(t);
                    }
                }
            } else {
                for (Type t : ((Class) type).getGenericInterfaces()) {
                    if (!allSuperTypes.contains(t)) {
                        allSuperTypes.add(t);
                    }
                }
            }
        }

        Type returnType = targetType;
        while (true) {
            Type old = returnType;
            returnType = refs.get(returnType);
            if (returnType instanceof Class) {
                return (Class) returnType;
            }
            if (returnType == null) {
                returnType = old;
                if (returnType instanceof WildcardType) {
                    return (Class) ((WildcardType) returnType).getUpperBounds()[0];
                }
                return (Class) ((TypeVariable) returnType).getBounds()[0];
            }
        }
    }

    /**
     * 收集类的所有常量
     *
     * @param clazz          - 收集目标
     * @param findAncestor   - 是否查找父类
     * @param findInterfaces - 是否查找接口
     * @return {@link Map} 包含类的所有常量
     */
    public static Map<String, Object> getConstantFrom(Class clazz, // NL
                                                      boolean findAncestor, boolean findInterfaces) {

        HashMap<String, Object> map = new HashMap<String, Object>();

        if (findInterfaces) {
            for (Class interfaceClass : clazz.getInterfaces()) {
                fillConstantFrom(interfaceClass, map);
            }
        }

        if (findAncestor) {
            Class superClass = clazz;
            while (superClass != null) {
                fillConstantFrom(superClass, map);
                superClass = superClass.getSuperclass();
            }
        }

        fillConstantFrom(clazz, map);

        return map;
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

    // 填充静态常量
    protected static void fillConstantFrom(Class clazz, HashMap<String, Object> map) {

        Field fields[] = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isSynthetic()) {
                continue; // 忽略系统常量
            }

            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                continue; // 忽略非静态常量
            }

            try {
                makeAccessible(field);

                map.put(field.getName(), field.get(null));
            } catch (SecurityException e) {
            } catch (IllegalAccessException e) {
                // Do nothing
            }
        }
    }

    // 测试代码
    public static void main(String... args) {

        // 输出所有常量
        System.out.println("输出所有常量");
        Map<String, ?> constants = getConstantFrom(GenericUtils.class, true, true);
        System.out.println(constants);

        // 获取方法 返回值 list，map 里面的类型
        for (Method method : GenericUtils.class.getMethods()) {
            System.out.println(method.getName() + " " + Arrays.asList(resolveTypeParameters(method.getDeclaringClass(), method.getGenericReturnType())));
        }
    }
}
