package com.chenwb.display;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TestVideoDisplay extends JFrame {
    private Graphics mGraphics;

    private TestVideoDisplay() {
        super("视频显示");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(30, 30, 450, 450);

        setVisible(true);

        mGraphics = getGraphics();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mGraphics = getGraphics();
            }
        });

        try {
            createSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        System.out.println("paintComponents ...");
        mGraphics = g;
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

    private void createSocket() throws Exception {
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
            System.out.println("image width = " + image.getWidth() + " height = " + image.getHeight()
                    + " packet = " + packet.getAddress() + " length = " + packet.getLength());
            int width = getContentPane().getWidth();
            mGraphics.drawImage(image, 0, 0, width, (int) (width * (image.getHeight() * 1.0f / image.getWidth())), observer);
            stream.close();
        }
    }
}
