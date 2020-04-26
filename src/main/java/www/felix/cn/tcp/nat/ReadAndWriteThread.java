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
    private String name;
    private InputStream in ;
    private OutputStream out ;
    private static final int SIZE=1024*1024;

    public ReadAndWriteThread(String name ,InputStream in, OutputStream out) {
        this.name = name;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        byte[] b = new byte[SIZE];
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw = new PrintWriter(out);
        try {
            // 获取输入流，并读取客户端信息
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.print(name);
                System.out.println(new String(info.getBytes(),"utf-8"));
                pw.write(info);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
