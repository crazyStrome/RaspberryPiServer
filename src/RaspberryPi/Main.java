package RaspberryPi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import static java.lang.System.*;

public class Main {
    /**
     * 服务端口
     */
    private final static int PORT = 30000;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private Charset charset = Charset.forName("utf-8");
    /**
     * 记录每个客户端对应的用户名
     */
    public static CrazyStromeMap<String, SocketChannel> clients
            = new CrazyStromeMap<>();

    private Car car;

    private void startListening() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            out.println("Server is listening: " + getLANAddressOnWindows().getHostAddress() + "::" + PORT);
            while (selector.select() > 0) {
                Set selectedKeys = selector.selectedKeys();
                Iterator iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = (SelectionKey)iterator.next();
                    if (sk.isAcceptable()) {
                        /**
                         * 客户端链接到服务器
                         */
                        SocketChannel socket = serverChannel.accept();
                        out.println(socket.getRemoteAddress() + " connect to server");
                        socket.write(ByteBuffer.wrap(Protocol.START.getBytes(charset)));
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                        sk.interestOps(SelectionKey.OP_ACCEPT);
                    }
                    if (sk.isReadable()) {
                        SocketChannel socket = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        String content = "";
                        try {
                            while (socket.read(buffer) > 0) {
                                buffer.flip();
                                content += charset.decode(buffer);
                                buffer.clear();
                            }
                            if (content.trim().length() > 0) {
                                handleMessage(content);
                                out.println("Received from client: " + content);
                            }
                            sk.interestOps(SelectionKey.OP_READ);
                        } catch (Exception e) {
                            sk.cancel();
                            if (sk.channel() != null) {
                                sk.channel().close();
                            }
                        }

                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getLANAddressOnWindows() {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();

                if (nif.getName().startsWith("wlan")) {
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();

                    while (addresses.hasMoreElements()) {

                        InetAddress addr = addresses.nextElement();
                        if (addr.getAddress().length == 4) { // 速度快于 instanceof
                            return addr;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    private void handleMessage(String msg) {
        switch (msg) {
            case Protocol.START:
                //car = Car.getInstance();
                out.println("Start");
                break;
            case Protocol.FORWARD:
                out.println("forward");
                if (car != null) {
                    car.forward();
                }
                break;
            case Protocol.BACKWARD:
                out.println("backward");
                if (car != null) {
                    car.backward();
                }
                break;
            case Protocol.LEFT:
                out.println("left");
                if (car != null) {
                    car.left();
                }
                break;
            case Protocol.RIGHT:
                out.println("right");
                if (car != null) {
                    car.right();
                }
                break;
            case Protocol.STOP:
                out.println("stop");
                if (car != null) {
                    car.stop();
                }
                break;
            case Protocol.DESTORY:
                out.println("destory");
                if (car != null) {
                    car.destory();
                    car = null;
                }
                break;
            default:
                if (car != null) {
                    if (msg.startsWith(Protocol.SPEED)) {
                        out.println("speed up");
                        double speed = Double.parseDouble(msg.split("[" + Protocol.SPLIT_SIGN + "]")[1]);
                        car.setSpeed(speed);
                    }
                }
                break;
        }
    }
    public static void main(String[] args) {
        new Main().startListening();
    }
}