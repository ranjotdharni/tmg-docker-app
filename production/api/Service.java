//package production.api;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

class Service {

    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Context for the route "/hello"
        server.createContext("/hello", new HelloHandler());

        // Start the server
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Handler for "/hello" route
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            // Get the output stream and write response
            OutputStream outputStream = exchange.getResponseBody();
            String response = "Hello, World!";
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}