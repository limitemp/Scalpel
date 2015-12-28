package com.scalpel.cmd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.text.TextUtils;

import com.scalpel.log.KLog;

/**
 * Author: Mrchen on 15/12/26.
 */
public class CommandReceiver extends BroadcastReceiver{
    private static final String ACTION_COMMAND = "com.scalpel.command";
    private static final String KEY_TARGET = "target";
    private static final String KEY_CMD = "cmd";

    private static boolean sInited = false;

    private static class SingletonHolder {
        private static final CommandReceiver INSTANCE = new CommandReceiver();
    }
    private CommandReceiver (){}

    private static CommandReceiver getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化命令模块
     * @param context app context
     */
    public static void initCommand(Context context) {
        if (!sInited) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_COMMAND);
            context.registerReceiver(getInstance(), filter);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int pid = intent.getIntExtra(KEY_TARGET, 0);
        if (pid == Process.myPid()) {
            String cmd = intent.getStringExtra(KEY_CMD);
            if (!TextUtils.isEmpty(cmd)) {
                KLog.output("the cmd: "+cmd);
                CommandParser.parser(cmd);
            }
        }
    }
}
