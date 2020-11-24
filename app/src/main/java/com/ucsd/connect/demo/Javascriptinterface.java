package com.ucsd.connect.demo;

import android.webkit.JavascriptInterface;

import org.jetbrains.annotations.NotNull;

public final class Javascriptinterface {

    @NotNull
    private final CallActivity callActivity = new CallActivity();

    @JavascriptInterface
    public final void onPeerConnected() {
        this.callActivity.onPeerConnected();
    }


}
