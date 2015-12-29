package com.scalpel.java;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.scalpel.cmd.CommandReceiver;
import com.scalpel.log.KLog;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Author: Mrchen on 15/12/26.
 */
public class JavaHooker implements IXposedHookLoadPackage{
    private static final String TAG = "_JavaHooker";
    private boolean mIsSystemServer;
    private static Context sContext;
    private static ClassLoader sClassLoader;

    static {
        KLog.i(TAG, "[method: static initializer ] " + "this is scalpel !");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        KLog.sProcessName = loadPackageParam.processName;
        KLog.i(TAG, "[method: handleLoadPackage ] " + ": "+loadPackageParam.packageName);
        if (TextUtils.equals("system_server", loadPackageParam.processName)) {
            mIsSystemServer = true;
        }
        sClassLoader = loadPackageParam.classLoader;
        if (mIsSystemServer) {
            Class<?> activityThreadClass = loadPackageParam.classLoader.loadClass("android.app.ActivityThread");
            sContext = (Context) XposedHelpers.getStaticObjectField(activityThreadClass, "mSystemContext");
            CommandReceiver.initCommand(sContext);
        }

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args != null && param.args.length != 0) {
                    sContext = (Context) param.args[0];
                    CommandReceiver.initCommand(sContext);
                }
                super.afterHookedMethod(param);
            }
        });
    }

    public static Context getContext() {
        return sContext;
    }

    public static ClassLoader getClassLoader() {
        return sClassLoader;
    }
}
