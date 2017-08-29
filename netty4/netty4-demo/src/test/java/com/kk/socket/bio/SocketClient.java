package com.kk.socket.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 10000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            String msg = reader.readLine();
            out.println(msg);
            out.flush();

            if (msg.equals("bye")) {
                break;
            }
            logger.info(in.readLine());
        }
        socket.close();
    }

}