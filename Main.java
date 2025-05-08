import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // 替换为实际的服务器地址
        int port = 60001; // SocketTool使用的端口号

        // 定义要发送的16进制数组
        byte[] dataToSend = new byte[]{(byte) 0xA3, 0x00, 0x00, 0x00};

        try {
            // 创建Socket对象并连接到服务器
            Socket socket = new Socket(serverAddress, port);
            System.out.println("已连接到服务器：" + serverAddress + ":" + port);

            // 获取输出流以发送数据
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(dataToSend);
            System.out.println("已发送数据：" + bytesToHex(dataToSend));

            // 获取输入流以接收返回值
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead > 0) {
                byte[] response = new byte[bytesRead];
                System.arraycopy(buffer, 0, response, 0, bytesRead);
                System.out.println("接收到的数据：" + bytesToHex(response));

                // 判断返回值是否为启用交通灯识别
                if (response.length >= 6 &&
                        response[0] == (byte) 0x55 &&
                        response[1] == (byte) 0xAA &&
                        response[2] == (byte) 0xA3 &&
                        response[3] == 0x00 &&
                        response[4] == 0x00 &&
                        response[5] == 0x00) {
                    System.out.println("服务端返回值匹配：启用交通灯识别");
                } else {
                    System.out.println("服务端返回值未匹配任何已定义的指令");
                }
            } else {
                System.out.println("未接收到数据");
            }

            // 关闭连接
            socket.close();
            System.out.println("连接已关闭");

        } catch (Exception e) {
            System.err.println("客户端发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 辅助函数：将字节数组转换为16进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder aux  = new StringBuilder();
        for (byte b : bytes) {
            aux.append(String.format("%02X ", b));
        }
        return aux.toString().trim();
    }
}