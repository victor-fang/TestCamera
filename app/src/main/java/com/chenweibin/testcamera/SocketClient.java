package com.chenweibin.testcamera;

import android.os.Handler;

import java.io.OutputStream;
import java.net.Socket;

public class SocketClient extends Handler {
    private Socket socket;
//    private Thread thread;

    public SocketClient() {
//       thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    socket = new Socket("192.168.1.107", 8899);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    socket = null;
//                }
//            }
//        });
//        thread.start();
    }

    public void sendData(byte[] data) {
        try {
            if (socket == null)
                return;

            OutputStream stream = socket.getOutputStream();
            stream.write(data);

//            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
