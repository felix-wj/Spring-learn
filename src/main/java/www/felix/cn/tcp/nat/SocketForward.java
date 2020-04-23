package www.felix.cn.tcp.nat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2020-04-23 20:55
 **/
@Slf4j
public class SocketForward {

    private Socket server;
    private Socket client;
    private static final int SIZE=1024*1024;
    public SocketForward(Socket server,String ip,Integer port) {
        this.server = server;

        client = new Socket();
        client = new Socket();
        // 设置reuseAddress为true
        try {
            client.setReuseAddress(true);
            client.connect(new InetSocketAddress(ip, port));
        } catch (Exception e) {
            throw new RuntimeException("创建socket失败");
        }
    }

    public void run() throws IOException {
        ReadAndWriteThread realClientSend = new ReadAndWriteThread(server.getInputStream(),client.getOutputStream());
        realClientSend.start();

        ReadAndWriteThread realServerSend = new ReadAndWriteThread(client.getInputStream(),server.getOutputStream());
        realServerSend.start();
    }
}
