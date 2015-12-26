package com.scalpel.java;

import android.content.Context;
import android.text.TextUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Author: Mrchen on 15/12/26.
 */
public class JavaHooker implements IXposedHookLoadPackage{
    private boolean mIsSystemServer;
    private Context mContext;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (TextUtils.equals("system_server", loadPackageParam.processName)) {
            mIsSystemServer = true;
        }

        Class<?> activityThreadClass = loadPackageParam.classLoader.loadClass("android.app.ActivityThread");
        if (mIsSystemServer) {
            // TODO: 15/12/26 system context
            mContext = (Context) XposedHelpers.getStaticObjectField(activityThreadClass, "mSystemContext");
        }


        XposedHelpers.findAndHookConstructor(activityThreadClass, "attach", boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }

}
