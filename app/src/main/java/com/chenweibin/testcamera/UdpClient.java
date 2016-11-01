package com.chenweibin.testcamera;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UdpClient {

    private DatagramSocket mSocket;
    private DatagramPacket mOutPacket;
    private InetAddress remoteAddress;
    private int remotePort = 10086;

    public UdpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new DatagramSocket();
                    remoteAddress = InetAddress.getByName("192.168.1.128");
                    mSocket.connect(remoteAddress, remotePort);

                    mOutPacket = new DatagramPacket(new byte[1], 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void sentData(byte[] data, int len) {
        if (data == null || mSocket == null)
            return;
        try {
            mOutPacket.setData(data);
            mOutPacket.setLength(len);

            mOutPacket.setAddress(remoteAddress);
            mOutPacket.setPort(remotePort);

            mSocket.send(mOutPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {

    }
}
