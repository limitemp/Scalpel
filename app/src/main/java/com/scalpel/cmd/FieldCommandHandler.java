package com.scalpel.cmd;

import android.text.TextUtils;

import com.scalpel.entity.FieldEntity;
import com.scalpel.java.JavaHooker;
import com.scalpel.log.KLog;
import com.scalpel.log.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XposedHelpers;

/**
 * Author: Mrchen on 15/12/27.
 */
public class FieldCommandHandler {
    public static Set<FieldEntity> sFiledSet = new HashSet<FieldEntity>();

    public static void handleCommand(FieldEntity entity) {
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
        Field field = null;
        try {
            field = hookClass.getDeclaredField(entity.fieldName);
        } catch (NoSuchFieldException e) {
            KLog.output(e);
        }
        if (field == null) {
            KLog.output("can't find the "+entity.fieldName+" field in "+entity.className+" class");
            return;
        }
        if (!sFiledSet.contains(entity)) {
            sFiledSet.add(entity);
        }
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                field.setAccessible(true);
                Object object = field.get(null);
                if (object != null) {
                    KLog.output(" ===========>>> " + entity.fieldName +" type: "+object.getClass().getName()
                            +" value: "+ object.toString());
                }
            } catch (IllegalAccessException e) {
                KLog.output(e);
            }
        }else {
            KLog.output("just add list, wait for hook method combines");
        }
    }

    public static void combineMethod(Object obj) {
        if (obj == null) {
            return;
        }

        KLog.output("combineMethod: "+ obj.getClass().getName());
        String className = obj.getClass().getName();
        for (FieldEntity entity : sFiledSet) {
            if (TextUtils.equals(entity.className, className)) {
                Object fieldObj = XposedHelpers.getObjectField(obj, entity.fieldName);
                if (fieldObj != null) {
                    KLog.output(entity.fieldName + " ------------->>>>>");
                    KLog.output("type: "+fieldObj.getClass().getName());
                    KLog.output("value: " + StrUtil.toString(fieldObj));
                    KLog.output(entity.fieldName + " <<<<<<-------------");
                }
            }
        }
    }
}
