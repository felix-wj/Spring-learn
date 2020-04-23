package www.felix.cn.tcp.nat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2020-04-23 17:42
 **/
public class ConnectDemo {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket = new Socket();
        // 设置reuseAddress为true
        socket.setReuseAddress(true);

        //TODO在此输入外网地址和端口
        String ip = "118.25.87.131";
        int port = 3306;
        socket.connect(new InetSocketAddress(ip, port));
        System.out.println(socket.getLocalPort());
    }

}
