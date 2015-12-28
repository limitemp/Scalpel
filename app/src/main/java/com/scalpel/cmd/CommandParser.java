package com.scalpel.cmd;

import android.text.TextUtils;

import com.scalpel.entity.FieldEntity;
import com.scalpel.entity.MethodEntity;
import com.scalpel.log.KLog;

import org.json.JSONObject;

/**
 * Author: Mrchen on 15/12/26.
 */
public class CommandParser {
    private static final String KEY_ACTION = "action";

    private static final String KEY_ACTION_METHOD = "method";
    private static final String KEY_CLASS_NAME = "class";
    private static final String KEY_METHOD_NAME = "method";
    private static final String KEY_HOOK_CONSTRUCTOR = "constructor";

    private static final String KEY_ACTION_FIELD = "field";
    private static final String KEY_FIELD_STATIC = "static";



    public static void parser(String cmd) {
        try {
            JSONObject jsonObject = new JSONObject(cmd);
            String action = jsonObject.optString(KEY_ACTION);
            if (TextUtils.equals(action, KEY_ACTION_METHOD)) {
                String className = jsonObject.optString(KEY_CLASS_NAME);
                if (TextUtils.isEmpty(className)) {
                    KLog.output("the class name is null! you need specific point class name.");
                    return;
                }
                MethodEntity entity = new MethodEntity(className);
                entity.methodName = jsonObject.optString(KEY_METHOD_NAME);
                entity.hookConstructor = jsonObject.optBoolean(KEY_HOOK_CONSTRUCTOR);
                MethodCommandHandler.handleCommand(entity);
            } else if (TextUtils.equals(action, KEY_ACTION_FIELD)) {
                String className = jsonObject.optString(KEY_CLASS_NAME);
                String fieldName = jsonObject.optString(KEY_ACTION_FIELD);
                if (TextUtils.isEmpty(className) && TextUtils.isEmpty(fieldName)) {
                    KLog.output("the class or field name is null! you need specific point class or field name.");
                    return;
                }
                FieldEntity entity = new FieldEntity(className, fieldName);
                entity.isStatic = jsonObject.optBoolean(KEY_FIELD_STATIC);
                FieldCommandHandler.handleCommand(entity);
            }
        } catch (Throwable throwable) {
            KLog.output(throwable);
        }
    }
}
