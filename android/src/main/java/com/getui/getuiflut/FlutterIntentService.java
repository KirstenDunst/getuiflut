package com.getui.getuiflut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import java.util.HashMap;
import java.util.Map;

public class FlutterIntentService extends GTIntentService {
   private String TAG = "intentService";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        if (intent != null) {
            processOnHandleIntent(this, intent);
        }
        return START_NOT_STICKY;
    }

    public void processOnHandleIntent(Context context, Intent intent) {
        if (intent == null || context == null) {
            return;
        }

        try {
            Bundle bundle = intent.getExtras();
            if (bundle == null || bundle.get("action") == null || !(bundle.get("action") instanceof Integer)) {
                return;
            }

            int action = bundle.getInt(PushConsts.CMD_ACTION);

            context = context.getApplicationContext();

            switch (action) {
                case PushConsts.GET_MSG_DATA:
                    onReceiveMessageData(context, (GTTransmitMessage) intent.getSerializableExtra(PushConsts.KEY_MESSAGE_DATA));
                    break;

                case PushConsts.GET_CLIENTID:
                    onReceiveClientId(context, bundle.getString(PushConsts.KEY_CLIENT_ID));
                    break;

                case PushConsts.GET_DEVICETOKEN:
                    onReceiveDeviceToken(context, bundle.getString(PushConsts.KEY_DEVICE_TOKEN));
                    break;

                case PushConsts.GET_SDKONLINESTATE:
                    onReceiveOnlineState(context, bundle.getBoolean(PushConsts.KEY_ONLINE_STATE));
                    break;

                case PushConsts.GET_SDKSERVICEPID:
                    onReceiveServicePid(context, bundle.getInt(PushConsts.KEY_SERVICE_PIT));
                    break;

                case PushConsts.KEY_CMD_RESULT:
                    onReceiveCommandResult(context, (GTCmdMessage) intent.getSerializableExtra(PushConsts.KEY_CMD_MSG));
                    break;

                case PushConsts.ACTION_NOTIFICATION_ARRIVED:
                    onNotificationMessageArrived(context, (GTNotificationMessage) intent.getSerializableExtra(PushConsts.KEY_NOTIFICATION_ARRIVED));
                    break;

                case PushConsts.ACTION_NOTIFICATION_CLICKED:
                    onNotificationMessageClicked(context, (GTNotificationMessage) intent.getSerializableExtra(PushConsts.KEY_NOTIFICATION_CLICKED));
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
       
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        GetuiflutPlugin.transmitMessageReceive(clientid, "onReceiveClientId");
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("messageId", msg.getMessageId());
        payload.put("payload", new String(msg.getPayload()));
        payload.put("payloadId", msg.getPayloadId());
        payload.put("taskId", msg.getTaskId());
        GetuiflutPlugin.transmitMessageReceive(payload, "onReceiveMessageData");
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        GetuiflutPlugin.transmitMessageReceive(String.valueOf(b), "onReceiveOnlineState");
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        GetuiflutPlugin.transmitMessageReceive(gtCmdMessage.toString(), "onReceiveCommandResult");
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage message) {
        Map<String, Object> notification = new HashMap<String, Object>();
        notification.put("messageId",message.getMessageId());
        notification.put("taskId",message.getTaskId());
        notification.put("title",message.getTitle());
        notification.put("content",message.getContent());
        GetuiflutPlugin.transmitMessageReceive(notification, "onNotificationMessageArrived");
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage message) {
        Map<String, Object> notification = new HashMap<String, Object>();
        notification.put("messageId",message.getMessageId());
        notification.put("taskId",message.getTaskId());
        notification.put("title",message.getTitle());
        notification.put("content",message.getContent());
        GetuiflutPlugin.transmitMessageReceive(notification, "onNotificationMessageClicked");
    }
}
