package server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.Server.receiveServer;

/**
 *
 * @author whiwf
 */
public class ReceiveServer extends Thread {

    public InetAddress clientIP;
    public int clientPort;

    ReceiveServer() {
    }

    // receive object
    public <T> T receiveObjectData(DatagramSocket server){
        try {
            byte[] buff = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buff, buff.length);
            
            server.receive(dp);
            
            ByteArrayInputStream bin = new ByteArrayInputStream(dp.getData());
            ObjectInputStream oin = new ObjectInputStream(bin);
            
            return (T) oin.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    // receive string
    public String receiveData(DatagramSocket server) throws IOException {
        byte[] buff = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buff, buff.length);

        server.receive(dp);

        clientIP = dp.getAddress();
        clientPort = dp.getPort();
        
        if(!checkExistSK(dp)){
            Server.listSK.add(dp);
        }
        
        return new String(dp.getData());
    }
    
    private boolean checkExistSK(DatagramPacket dp){
        for(DatagramPacket item : Server.listSK){
            if(item.getAddress().equals(dp.getAddress()) && item.getPort() == dp.getPort()){
                return true;
            }
        }
        return false;
    }
}
