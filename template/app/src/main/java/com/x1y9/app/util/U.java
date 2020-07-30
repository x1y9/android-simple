package com.x1y9.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.x1y9.app.Consts;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public static <T> List<T> noNull(List<T> src) {
        return src == null ? new ArrayList<>() : src;
    }

    public static String toNull(String str) {
        return (str == null || str.trim().length() == 0) ? null : str;
    }

    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    public static <T> T get(T[] array, int index, T defValue) {
        return index >= 0 && index < array.length  ? array[index] : defValue;
    }

    public static <T> T get(List<T> list, int index, T defVaule) {
        return index >= 0 && index < list.size() ? list.get(index) : defVaule;
    }

    public static <K, V> Map<K, V> put(Map<K, V> map, K key, V value) {
        if(value != null)
            map.put(key, value);

        return map;
    }

    public static boolean isEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    public static String append(String old, String value, String split) {
        return isEmpty(old) ? value : isEmpty(value) ? old : (old + split + value);
    }

    public static <T> Map<T, String> append(Map<T, String> map, T key, String value, String split) {
        if(value != null) {
            String old = map.get(key);
            map.put(key, value + (old == null ? "" : (split + old)));
        }
        return map;
    }

    public static <T> String format(Map<String, T> map) {
        return format(map, ":", "\n");
    }

    public static <T> String format(Map<String, T> map, String kv, String vv) {
        if (map != null) {
            List<String> items = new ArrayList();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                items.add(entry.getKey() + kv + entry.getValue().toString());
            }
            return TextUtils.join(vv, items);
        }
        return "";
    }

    public static Matcher matcher(String value, String reg) {
        return Pattern.compile(reg).matcher(value);
    }

    public static boolean isMatch(String value, String reg) {
        return matcher(value, reg).matches();
    }

    public static List<String> run(String cmd) {
        List<String> result = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                for (String line; (line = bufferedReader.readLine()) != null; result.add(line));
            }
            process.waitFor();
            process.destroy();
            return result;
        }catch (Exception e) {
            Log.d(Consts.LOG, cmd, e);
        }
        return null;
    }
}
