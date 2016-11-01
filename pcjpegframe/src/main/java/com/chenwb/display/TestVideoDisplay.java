package com.chenwb.display;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TestVideoDisplay extends JFrame {
    private BufferedImage image;


    private TestVideoDisplay() {
        super("视频显示");

        setSize(480, 320);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(30, 30, 450, 450);

        setVisible(true);

        Graphics graphics = getGraphics();

        try {
//            createSocketInputStream(graphics);
            createSocket(graphics);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        new TestVideoDisplay();
    }

    private void createSocketInputStream(Graphics graphics) throws Exception {
        //为了简单起见，所有的异常信息都往外抛
        int port = 8899;
        //定义一个ServerSocket监听在端口8899上
        ServerSocket server = new ServerSocket(port);
        //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的



        ImageObserver observer = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return true;
            }
        };

        while (true) {
            Socket socket = server.accept();
            InputStream inputStream = socket.getInputStream();


            while (true) {
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
//                byte[] buf = new byte[1024];
//                int len;
//                while ((len = inputStream.read(buf)) != -1) {
//                    output.write(buf, 0, len);
//                }
//                byte[] data = output.toByteArray();
//
//
//                System.out.println("image = " + data.length);
//                if (data.length <= 0) {
//                    break;
//                }
//
//                byte[] rgbBuffer = new byte[480 * 320 * 3];
//
//                YuvToRGB.decodeYUV420SP(rgbBuffer, data, 480, 320);
//
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rgbBuffer);
                System.out.println("image = " + inputStream.available());
                BufferedImage image = ImageIO.read(inputStream);
                System.out.println("image = " + image + " available = " + inputStream.available());
                if (image == null)
                    break;
                graphics.drawImage(image, 0, 0, observer);
            }
        }
    }

    private void createSocket(Graphics graphics) throws Exception {
        DatagramSocket socket = new DatagramSocket(10086);
        int maxImageSize = 1024 * 100;
        byte[] tempBuff = new byte[maxImageSize];
        DatagramPacket packet = new DatagramPacket(tempBuff, maxImageSize);

        ImageObserver observer = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return true;
            }
        };
        System.out.println("starting ...");
        while (true) {
            socket.receive(packet);

            ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData(),
                    packet.getOffset(), packet.getLength());
            BufferedImage image = ImageIO.read(stream);
            System.out.println("image with = " + image.getWidth() + " height = " + image.getHeight()
                    + " packet = " + packet.getAddress() + " length = " + packet.getLength());
            graphics.drawImage(image, 0, 0, getWidth(), (int) (getWidth() * (image.getHeight() * 1.0f / image.getWidth())), observer);
            stream.close();
        }
    }
}
