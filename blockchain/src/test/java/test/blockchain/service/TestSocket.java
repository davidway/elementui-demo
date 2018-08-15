package test.blockchain.service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TestSocket {
  String host = "http://localhost/YAChatManage";
  int port = 8080;
  Socket socket;

  public void createSocket() throws Exception {
    socket = new Socket(this.host, this.port);
  }

  public void communicate() throws Exception {
    StringBuffer sb = new StringBuffer("GET / HTTP/1.1/r/n");
    sb.append("Host: <A href=\"www.163.com\" mce_href=\"www.163.com\" target=_blank>www.163.com</A>/r/n");
    sb.append("Connection: Keep-Alive/r/n");
    sb.append("Accept: */*/r/n/r/n");

    // 发出HTTP请求
    OutputStream socketOut = socket.getOutputStream();
    socketOut.write(sb.toString().getBytes());
    socket.shutdownOutput(); // 关闭输出流

    // 接收响应结果
    System.out.println(socket);

    InputStream socketIn = socket.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(socketIn));
    String data;
    while ((data = br.readLine()) != null) {
      System.out.println(data);
    }
    socket.close();
  }

  public static void main(String args[]) throws Exception {
    TestSocket client = new TestSocket();
    client.createSocket();
    client.communicate();
  }

}