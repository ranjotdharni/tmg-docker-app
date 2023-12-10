import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class Service {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/upload", new ImageHandler());
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            if ("POST".equals(exchange.getRequestMethod())) 
            {
                InputStream inputStream = exchange.getRequestBody();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                String base64Image = new String(outputStream.toByteArray(), "UTF-8");
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                BufferedImage bufferedImage = ImageIO.read(bis);
                Orchestrator _o = new Orchestrator(1920, 1080, bufferedImage);
                bufferedImage = _o.getCanvas();

                exchange.sendResponseHeaders(200, 0);
                OutputStream responseStream = exchange.getResponseBody();
                String response = "{\"message\": \"" + bufferedImageToBase64DataUrl(bufferedImage) + "\"}";
                responseStream.write(response.getBytes());
                responseStream.flush();
                responseStream.close();
            } 
            else 
            {
                exchange.sendResponseHeaders(405, 0);
                OutputStream responseStream = exchange.getResponseBody();
                String response = "Method Not Allowed";
                responseStream.write(response.getBytes());
                responseStream.flush();
                responseStream.close();
            }
        }
    }

    private static String bufferedImageToBase64DataUrl(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String dataUrl = "data:image/png;base64," + base64Image;
        return dataUrl;
    }
}