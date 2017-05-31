package com.mobile.utils;

import java.net.ServerSocket;

public class AvailablePorts {

    /**
     * Generates Random ports
     * Used during starting appium server
     */
    public int getPort() {

        try {

            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            socket.close();
            return port;

        } catch (Exception e) {
            return 0;
        }
    }
}
