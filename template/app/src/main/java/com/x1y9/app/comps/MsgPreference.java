package com.x1y9.app.comps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MsgPreference extends Preference implements View.OnClickListener {

    private String mTip, mUrl;

    public MsgPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreference(context, attrs);
    }

    public MsgPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreference(context, attrs);
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    private void initPreference(Context context, AttributeSet attrs) {
        mTip = getAttributeStringValue(attrs, null, "tip" ,"");
        mUrl = getAttributeStringValue(attrs, null, "url" ,"");
    }

    private String getAttributeStringValue(AttributeSet attrs, String namespace, String name, String defaultValue) {

        String value = null;
        int resId = attrs.getAttributeResourceValue(namespace, name, 0);

        if (resId == 0) {
            value = attrs.getAttributeValue(namespace, name);
            if (value == null)
                value = defaultValue;
        }
        else {
            value = getContext().getResources().getString(resId);
        }

        return value;
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        //setOnClickListener一定要放在bind这里，因为列表过长而recyle的时候view会重用，这里一定要重新set
        view.setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(android.R.id.title);
        if (textView != null) {
            textView.setSingleLine(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mTip))  {
            if (!TextUtils.isEmpty(mUrl)) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl)));
            }
        }
        else {
            new AlertDialog.Builder(this.getContext()).setMessage(mTip)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mUrl)) {
                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl)));
                        }
                    }
                }).show();
        }
    }
}