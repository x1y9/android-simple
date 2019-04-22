package me.i38.liquid;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;

// 标准的Preference没有处理defaultValue，而扩展的Preference都带有弹出框，所以自己写一个
public class StdPreference extends Preference {

    public StdPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index){
        return ta.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        persistString(restoreValue ? getPersistedString("") : (String) defaultValue);
    }

}
