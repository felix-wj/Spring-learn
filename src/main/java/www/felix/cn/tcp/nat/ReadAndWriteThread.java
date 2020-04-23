package www.felix.cn.tcp.nat;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2020-04-23 21:35
 **/
@Slf4j
public class ReadAndWriteThread extends Thread {
    private InputStream in ;
    private OutputStream out ;
    private static final int SIZE=1024*1024;

    public ReadAndWriteThread(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        byte[] b = new byte[SIZE];
        try {
            while (in.read(b)>1){
                System.out.println(b);
                out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
