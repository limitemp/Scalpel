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
    private static final String KEY_SHOW_STACK = "stack";
    private static final String KEY_HOOK_HIJACK = "hijack";
    private static final String KEY_HOOK_RESULT_TYPE ="type";
    private static final String KEY_HOOK_RESULT = "result";

    private static final String KEY_ACTION_FIELD = "field";
    private static final String KEY_FIELD_STATIC = "static";



    public static void parser(String cmd) {
        try {
            KLog.output(cmd);
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
                entity.showStack = jsonObject.optBoolean(KEY_SHOW_STACK);
                entity.hijack = jsonObject.optBoolean(KEY_HOOK_HIJACK);
                if (entity.hijack) {
                    String type = jsonObject.optString(KEY_HOOK_RESULT_TYPE);
                    if (TextUtils.equals(type, "int")) {
                        entity.result = jsonObject.optInt(KEY_HOOK_RESULT, 0);
                    } else if (TextUtils.equals(type, "boolean")) {
                        entity.result = jsonObject.optBoolean(KEY_HOOK_RESULT, false);
                    }else if (TextUtils.equals(type, "String")) {
                        entity.result = jsonObject.optString(KEY_HOOK_RESULT, "");
                    }
                }
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
