package com.scalpel.cmd;

import android.text.TextUtils;
import android.util.Log;

import com.scalpel.entity.MethodEntity;
import com.scalpel.java.JavaHooker;
import com.scalpel.log.KLog;
import com.scalpel.log.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Author: Mrchen on 15/12/27.
 */
public class MethodCommandHandler {
    private static HashMap<Member, MethodEntity> sHookedMethodMap = new HashMap<Member, MethodEntity>();

    public static void handleCommand(MethodEntity entity) {
        if (TextUtils.isEmpty(entity.methodName) && !entity.hookConstructor) {
            KLog.output("MethodCommandHandle: nothing to do !");
            return;
        }
        Class<?> hookClass = null;
        try {
            hookClass = JavaHooker.getClassLoader().loadClass(entity.className);
        } catch (ClassNotFoundException e) {
            try {
                hookClass = JavaHooker.getContext().getClassLoader().loadClass(entity.className);
            } catch (ClassNotFoundException e1) {
                KLog.output(e1);
            }
        }
        if (hookClass == null) {
            KLog.output("can't find the "+ entity.className +" class, "+"make sure class name had been exist");
            return;
        }

        if (!TextUtils.isEmpty(entity.methodName)) {
            for (Method method : hookClass.getDeclaredMethods()) {
                if (TextUtils.equals(method.getName(), entity.methodName)) {
                    sHookedMethodMap.put(method, entity);
                    if (!sHookedMethodMap.containsKey(method)) {
                        XposedBridge.hookMethod(method, new MethodHook(method));
                    }
                }
            }
            KLog.output("hook "+entity.className+"#"+entity.methodName+" done");
        }

        if (entity.hookConstructor) {
            for (Constructor<?> constructor : hookClass.getDeclaredConstructors()) {
                sHookedMethodMap.put(constructor, entity);
                if (!sHookedMethodMap.containsKey(constructor)) {
                    XposedBridge.hookMethod(constructor, new MethodHook(constructor));
                }

            }
            KLog.output("hook "+entity.className+"constructor done");
        }

    }


    static class MethodHook extends XC_MethodHook {
        private static final String TAG = "_MethodHook";
        private Member mMember;
        public MethodHook(Member member) {
            super();
            this.mMember = member;
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            MethodEntity entity = sHookedMethodMap.get(mMember);
            if (entity.showStack) {
                KLog.i(TAG + " stack: ", Log.getStackTraceString(new Throwable()));
            }
            StringBuilder builder = new StringBuilder();
            builder.append(entity.className);
            builder.append('#');
            builder.append(entity.methodName);
            builder.append('\n');
            if (param.args != null && param.args.length != 0) {
                builder.append("+++++++++++beforeHookedMethod+++++++++++");
                builder.append('\n');
                for (Object arg : param.args) {
                    if (arg == null) {
                        builder.append("null");
                        builder.append('\n');
                        continue;
                    }
                    FieldCommandHandler.combineMethod(arg);
                    builder.append(StrUtil.toString(arg));
                    builder.append('\n');
                }
                builder.append("-----------beforeHookedMethod-----------");
            }
            KLog.i(TAG, "[method: beforeHookedMethod ] " + builder.toString());
            KLog.output(builder.toString());

            FieldCommandHandler.combineMethod(param.thisObject);
            super.beforeHookedMethod(param);
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            MethodEntity entity = sHookedMethodMap.get(mMember);
            StringBuilder builder = new StringBuilder();
            builder.append("+++++++++++afterHookedMethod+++++++++++");
            builder.append('\n');
            builder.append(entity.className);
            builder.append('#');
            builder.append(entity.methodName);
            builder.append('\n');
            builder.append("result is ").append(StrUtil.toString(param.getResult()));
            builder.append('\n');

            if (param.args != null && param.args.length != 0) {
                for (Object arg : param.args) {
                    if (arg == null) {
                        builder.append("null");
                        builder.append('\n');
                        continue;
                    }
                    FieldCommandHandler.combineMethod(arg);
                    builder.append(StrUtil.toString(arg));
                    builder.append('\n');
                }
            }
            builder.append("-----------afterHookedMethod-----------");
            KLog.i(TAG, "[method: afterHookedMethod ] " + builder.toString());
            KLog.output(builder.toString());
            FieldCommandHandler.combineMethod(param.thisObject);
            super.afterHookedMethod(param);
        }

    }
}

