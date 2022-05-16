package com.chenweibin.testcamera;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer {
    private DatagramSocket mSocket;
    private int port;
    private byte[] buf = new byte[2000];

    public UdpServer(int port) {
        this.port = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new DatagramSocket(port);
                    listen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void listen() throws Exception {
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");

        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // blocks until a packet is received
            mSocket.receive(packet);

            System.out.println("Message from " + packet.getAddress().getHostAddress() + ": " + packet.getLength());
        }
    }
}
