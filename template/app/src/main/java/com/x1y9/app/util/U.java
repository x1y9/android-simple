package com.x1y9.app.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U {
    public static String noNull(String s) {
        return s == null ? "" : s;
    }

    public static <T> T or(T... vars) {
        for (int i = 0; i < vars.length; i++) {
            if (vars[i] != null)
                return vars[i];
        }
        return null;
    }

    public static String symbol(boolean b) {
        return b ?  "✓" : "X";
    }

    public static <T> List<T> noNull(List<T> src) {
        return src == null ? new ArrayList<>() : src;
    }

    public static <K,V> Map<K,V> noNull(Map<K,V> src) {
        return src == null ? new HashMap<K,V>() : src;
    }

    public static String toNull(String str) {
        return (str == null || str.trim().length() == 0) ? null : str;
    }

    public static String trim(Object o, String def) {
        String s = s(o);
        return s == null ? def : s.trim();
    }

    public static String s(Object o) {
        try {
            return o.toString();
        }catch (Exception e) {
            return null;
        }
    }

    public static <T> T get(T[] array, int index, T defValue) {
        return index >= 0 && index < array.length  ? array[index] : defValue;
    }

    public static <T> T get(List<T> list, int index, T defVaule) {
        return index >= 0 && index < list.size() ? list.get(index) : defVaule;
    }

    public static Object get(Map map, String...keys) {
        try {
            for (int i = 0; i < keys.length - 1; i++) {
                map = map == null ? null : (Map) map.get(keys[i]);
            }
            return map.get(keys[keys.length - 1]);
        }catch (Exception e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> put(Map<K, V> map, K key, V value) {
        if(value != null)
            map.put(key, value);

        return map;
    }

    public static Map<String, String> newMap(String... kv) {
        Map<String,String> map = new HashMap<>();
        for (int i=0 ; i < kv.length / 2; i++) {
            map.put(kv[i*2], kv[i*2 +1]);
        }
        return map;
    }

    public static <T> List<T> add(List<T> list, T value) {
        if(value != null)
            list.add(value);

        return list;
    }

    public static <T> boolean in(T src, T... tgts) {
        for (int i = 0; i< tgts.length; i++) {
            if (src.equals(tgts[i]))
                return true;
        }
        return false;
    }

    public static boolean isEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    public static String append(String old, String value, String split) {
        return isEmpty(old) ? value : isEmpty(value) ? old : (old + split + value);
    }

    public static <T> Map<T, String> append(Map<T, String> map, T key, String value, String split) {
        if(value != null && key != null) {
            String old = map.get(key);
            map.put(key, (old == null ? "" : (old + split)) + value);
        }
        return map;
    }

    public static String join(List<String> list, String split) {
        return TextUtils.join(split, list);
    }

    public static <T> String join(Map<String, T> map) {
        return join(map, ":", "\n");
    }

    public static <T> String join(String split, String... keys) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : keys) {
            if (key != null) {
                if (!first) {
                    sb.append(split);
                }
                sb.append(key);
                first = false;
            }
        }
        return isEmpty(sb.toString()) ? null : sb.toString();
    }

    public static <T> String join(Map<String, T> map, String kv, String vv) {
        if (map != null) {
            List<String> items = new ArrayList();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                items.add(U.append(entry.getKey(), U.s(entry.getValue()), kv));
            }
            return TextUtils.join(vv, items);
        }
        return "";
    }

    public static Matcher matcher(String value, String reg) {
        return Pattern.compile(reg).matcher(value);
    }

    public static boolean match(String value, String reg) {
        return matcher(value, reg).matches();
    }

    public static boolean find(String value, String reg) {
        return matcher(value, reg).find();
    }

    public static String group1(String value, String reg) {
        try {
            Matcher matcher = Pattern.compile(reg).matcher(value);
            //注意用find和matches的区别
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
        }
        return null;
    }
    public static String formatSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "", "K", "M", "G", "T" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
