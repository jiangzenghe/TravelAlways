package com.imyuu.travel.network;

public interface DownloadProgressListener {
    public void onDownloadSize(int size);

    public void onFinish();
}
