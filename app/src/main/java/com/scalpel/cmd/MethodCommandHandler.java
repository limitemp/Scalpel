package com.scalpel.cmd;

import android.text.TextUtils;

import com.scalpel.entity.MethodEntity;
import com.scalpel.java.JavaHooker;
import com.scalpel.log.KLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Author: Mrchen on 15/12/27.
 */
public class MethodCommandHandler {
    private static Set<Member> sHookedMethodSet = new HashSet<Member>();

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
                    if (!sHookedMethodSet.contains(method)) {
                        sHookedMethodSet.add(method);
                    }
                }
            }
            //xposed内部已经有了防止重复hook的逻辑，这里不需要担心
            XposedBridge.hookAllMethods(hookClass, entity.methodName, new MethodHook(entity.className));
            KLog.output("hook "+entity.className+"#"+entity.methodName+" done");
        }

        if (entity.hookConstructor) {
            for (Constructor<?> constructor : hookClass.getDeclaredConstructors()) {
                if (!sHookedMethodSet.contains(constructor)) {
                    sHookedMethodSet.add(constructor);
                }
            }
            XposedBridge.hookAllConstructors(hookClass, new MethodHook(entity.className));
            KLog.output("hook "+entity.className+"constructor done");
        }

    }


    static class MethodHook extends XC_MethodHook {
        private static final String TAG = "_MethodHook";
        String className;
        String methodName;

        public MethodHook(String className) {
            super();
            this.className = className;
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            methodName = param.method.getName();
            StringBuilder builder = new StringBuilder();
            builder.append(className);
            builder.append('#');
            builder.append(methodName);
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
                    builder.append(arg.toString());
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
            StringBuilder builder = new StringBuilder();
            builder.append(className);
            builder.append('#');
            builder.append(methodName);
            builder.append('\n');
            if (param.args != null && param.args.length != 0) {
                builder.append("+++++++++++afterHookedMethod+++++++++++");
                builder.append('\n');
                for (Object arg : param.args) {
                    if (arg == null) {
                        builder.append("null");
                        builder.append('\n');
                        continue;
                    }
                    FieldCommandHandler.combineMethod(arg);
                    builder.append(arg.toString());
                    builder.append('\n');
                }
                builder.append("-----------afterHookedMethod-----------");
            }
            KLog.i(TAG, "[method: afterHookedMethod ] " + builder.toString());
            KLog.output(builder.toString());
            FieldCommandHandler.combineMethod(param.thisObject);
            super.afterHookedMethod(param);
        }

    }
}

