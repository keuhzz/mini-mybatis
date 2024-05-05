package com.zyuhoo.mini.mybatis.type;

import com.zyuhoo.mini.mybatis.io.Resources;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 类型别名注册器: 为 Java 类型设置缩写, 仅用于 XML 配置, 旨在降低冗余的全限定类名.
 *
 * @since 0.0.1
 */
public class TypeAliasRegistry {

    private final Map<String, Class<?>> typeAliases = new HashMap<>();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("_byte", byte.class);
        registerAlias("_short", short.class);
        registerAlias("_int", int.class);
        registerAlias("_long", long.class);
        registerAlias("_float", float.class);
        registerAlias("_double", double.class);
        registerAlias("_char", char.class);
        registerAlias("_boolean", boolean.class);

        registerAlias("_byte[]", byte[].class);
        registerAlias("_short[]", short[].class);
        registerAlias("_int[]", int[].class);
        registerAlias("_long[]", long[].class);
        registerAlias("_float[]", float[].class);
        registerAlias("_double[]", double[].class);
        registerAlias("_char[]", char[].class);
        registerAlias("_boolean[]", boolean[].class);

        registerAlias("byte", Byte.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("long", Long.class);
        registerAlias("float", Float.class);
        registerAlias("double", Double.class);
        registerAlias("char", Character.class);
        registerAlias("boolean", Boolean.class);

        registerAlias("byte[]", Byte[].class);
        registerAlias("short[]", Short[].class);
        registerAlias("int[]", Integer[].class);
        registerAlias("long[]", Long[].class);
        registerAlias("float[]", Float[].class);
        registerAlias("double[]", Double[].class);
        registerAlias("char[]", Character[].class);
        registerAlias("boolean[]", Boolean[].class);

    }

    /**
     * 注册 Java 类型对应的别名.
     *
     * @param alias 别名
     * @param value Java 类型
     */
    public void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            return;
        }
        String key = alias.toLowerCase(Locale.ENGLISH);
        if (typeAliases.containsKey(key) && typeAliases.get(key) != null && !typeAliases.get(key).equals(value)) {
            return;
        }
        typeAliases.put(key, value);
    }

    /**
     * 根据传入的字符串获取对应的类型.
     *
     * @param string 别名或类型
     * @return Class类型
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> resolveAlias(String string) {
        if (string == null) {
            return null;
        }
        String key = string.toLowerCase(Locale.ENGLISH);
        if (typeAliases.containsKey(key)) {
            return (Class<T>) typeAliases.get(key);
        }

        try {
            return (Class<T>) Resources.classForName(string);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not resolve type alias " + string + ". Cause: " + e, e);
        }
    }
}
