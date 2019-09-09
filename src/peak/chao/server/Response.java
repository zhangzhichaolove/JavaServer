package peak.chao.server;

import java.io.*;


public class Response {
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[1024];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.isFile() && file.exists()) {
                fis = new FileInputStream(file);
                int len = -1;//表示已经读取了多少个字节，如果是 -1，表示已经读取到文件的末尾
                String content = "HTTP/1.1 200 ok\r\n" +
                        "Content-Type:text/html\r\n" +
                        "Content-Length:" + file.length() + "\r\n" +
                        "\r\n";
                output.write(content.getBytes());
                while ((len = fis.read(bytes)) != -1) {
                    //打印读取的数据
                    System.out.println(new String(bytes, 0, len));
                    output.write(bytes, 0, len);
                }
            } else {
                String content = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>JavaServer</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1 align=\"center\">文件未找到</h1>\n" +
                        "</body>\n" +
                        "</html>".getBytes("UTF-8");
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type:text/html\r\n" +
                        //"Content-Type: application/json; charset=utf-8\r\n" +
                        "Content-Length:" + (content.length() - 1) + "\r\n" +
                        "\r\n" +
                        content;
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
                output.close();
            }
        }
    }
}