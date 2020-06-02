package com.oss.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SocketService {

	public static final int DEFAULT_BUFFER_SIZE = 100000;
    public void sendFile(String clientIP, int clientPort, byte[] tts_speech) { //String fileName) {
        String serverIP = clientIP;
        int serverPort = clientPort;
        byte[] ttsText = tts_speech;
        
        double startTime = 0;
         
        try {
            Socket socket = new Socket(serverIP, serverPort);
            
            if(!socket.isConnected()){
                log.info("Socket Connect Error.");
                System.exit(0);
            }
             
            startTime = System.currentTimeMillis();
            OutputStream os = socket.getOutputStream();
            
            os.write(ttsText);
             
            log.info("File transfer completed.");
            
            os.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        double endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime)/ 1000;
        double transferSpeed = (ttsText.length / 1000)/ diffTime;
        
         
        log.info("time: " + diffTime+ " second(s)");
        log.info("Average transfer speed: " + transferSpeed + " KB/s");
    }
}
