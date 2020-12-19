package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

public class Server {

    public Server() throws IOException {
        ConfigHelper.initValues();
//        manager.updateDataBase();
    }

    public void startServer() throws IOException {
        final String ip = InetAddress.getByName(ConfigHelper.HOST_NAME).getHostAddress();
        final int port = Integer.parseInt(ConfigHelper.HOST_PORT);

        final InetSocketAddress socket = new InetSocketAddress(ip, port);
        final HttpServer server = HttpServer.create(socket, 1);

        System.out.println("Server is ready.");
        server.createContext("/get", httpExchange -> {
            try (httpExchange) {
                final String userInput = getBody(httpExchange);
                final Headers headers = httpExchange.getResponseHeaders();
                headers.set("Content-Type", String.format("application/json; charset=%s", ConfigHelper.CHARSET));

                final Option userOption = ResponseHandler.analyzeRequest(userInput);
                final String responseBody = ResponseHandler.createResponse(userInput, userOption);
                final byte[] rawResponseBody = responseBody.getBytes(ConfigHelper.CHARSET);

                httpExchange.sendResponseHeaders(200, rawResponseBody.length);
                httpExchange.getResponseBody().write(rawResponseBody);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        server.start();
    }

    private String getBody(HttpExchange httpExchange) {
        final URI requestURI = httpExchange.getRequestURI();
        return requestURI.toString().split("\\?")[1].split("=")[1];
    }
}
