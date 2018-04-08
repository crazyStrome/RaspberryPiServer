package RaspberryPi;

import com.pi4j.io.gpio.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class Client {
    private SocketChannel client;
    private Charset charset;

    private void init() {
        try {
            client = SocketChannel.open();
            client.connect(new InetSocketAddress("192.168.137.66", 9999));
            charset = Charset.forName("utf-8");
        } catch (IOException e) {
            System.out.println("服务器连接失败: " + e);
        }
        try {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                client.write(ByteBuffer.wrap(scanner.nextLine().getBytes(charset)));
            }
        } catch (IOException e) {
            System.out.println("信息发送失败: " + e);
        }
    }
    public static void main(String[] args) {
        //new Client().init();
        System.out.println("<--pi4j--> GPIO Blink...");
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(
                RaspiPin.GPIO_08
        );
        while (true) {
            pin.high();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pin.low();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}