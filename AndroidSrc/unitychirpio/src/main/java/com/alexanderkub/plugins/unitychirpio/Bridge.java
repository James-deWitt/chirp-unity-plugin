package com.alexanderkub.plugins.unitychirpio;

import android.content.Context;
import android.os.Handler;

import io.chirp.connect.models.ChirpConnectState;

public final class Bridge {
    private static ChirpPluginJavaMessageHandler javaMessageHandler;
    private static Handler unityMainThreadHandler;
    private ChirpConnector connector;

    public void registerPluginHandlers(final Context ctx, ChirpPluginJavaMessageHandler handler) {
        javaMessageHandler = handler;
        if(unityMainThreadHandler == null) {
            unityMainThreadHandler = new Handler();
        }
        this.connector = new ChirpConnector(ctx);
    }

    public void ChirpInitSDK(String key, String secret, String config) {
        this.connector.InitSDK(key, secret, config);
    }

    public int ChirpStartSDK() {
        return this.connector.StartSDK();
    }

    public int ChirpStopSDK() {
        return this.connector.StopSDK();
    }

    public int ChirpSendData(int length, String payload) {
        return this.connector.sendPayload(length, payload);
    }

    private static void runOnUnityThread(Runnable runnable) {
        if(unityMainThreadHandler != null && runnable != null) {
            unityMainThreadHandler.post(runnable);
        }
    }

    protected static void SendReceiveEventToUnity(final String data) {
        runOnUnityThread(new Runnable() {
            @Override
            public void run() {
                if(javaMessageHandler != null) {
                    javaMessageHandler.OnReceiveDataHandler(data);
                }
            }
        });
    }

    protected static void SendSentEventToUnity(final String data) {
        runOnUnityThread(new Runnable() {
            @Override
            public void run() {
                if(javaMessageHandler != null) {
                    javaMessageHandler.OnSentDataHandler(data);
                }
            }
        });
    }

    protected static void SendChangeStateEventToUnity(int state) {
        final int tmpState = state;
        runOnUnityThread(new Runnable() {
            @Override
            public void run() {
                if(javaMessageHandler != null) {
                    javaMessageHandler.OnChangeStateHandler(tmpState);
                }
            }
        });
    }
}
