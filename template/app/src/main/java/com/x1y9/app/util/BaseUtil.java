package com.x1y9.app.util;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by x1y9
 */

public class BaseUtil {

        public static void hideInRecents(Context context, boolean hide) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.AppTask> tasks = null;
            tasks = am.getAppTasks();
            if (tasks != null && tasks.size() > 0) {
                tasks.get(0).setExcludeFromRecents(hide);
            }
        }
    }

    public static void msgBox(Context context, String title) {
        new AlertDialog.Builder(context).setMessage(title)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public static Map<String, Object> jsonToMap(String json, Map<String, Object> defaultValue) {
        try {
            return toMap(new JSONObject(json));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    //高版本的org.json已经可以支持复杂Map转换为JSONObject了，但如果里面某key有其他Object，会被转为key:null
    public static String jsonFromMap(Map<String, Object> map, String defaultValue) {
        try {
            return new JSONObject(map).toString();
        } catch (Exception e) {
            //如果map为空，或者其中有key值为null(value为null没关系），会有异常
            return defaultValue;
        }
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            else if (value == JSONObject.NULL) {
                value = null;
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
