package serial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BG
 */
import gnu.io.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serial {
    
    public Serial(){
        super();
    }
    
    private OutputStream out;
    private InputStream in;
    
    public void connect (String portName) throws Exception{
        
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned()){
            System.out.println("Error: Port is currently in use");
        }
        else{
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            if ( commPort instanceof SerialPort){
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();

                //(new Thread(new SerialWriter(out))).start();
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
            }
            else {
                System.out.println("Error: Only serial ports are handled by this example.");
                
            }
        }
    
    }
    
    public OutputStream getOutputStream(){
        return out;
    }
    
    public InputStream getInputStream(){
        return in;
    }
    
    
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                System.out.println(new String(buffer,0,len));
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
    
    public static class SerialWriter{

        /**
         * @return the comm
         */
        public String getComm() {
            return comm;
        }

        /**
         * @param comm the comm to set
         */
        public void setComm(String comm) {
            this.comm = comm;
        }
    
        OutputStream out;
        
        private byte[] msg = {0x02,0x30,0x31,0x52,0x50,0x56,0x31,0x03};
        private String comm;
        
        public SerialWriter (OutputStream out){
            this.out = out;
        }
        
        public void writeMSG (String msg){
        
            byte[] byteArray = toBytes(msg.toCharArray());
            try {
                //this.out.write(byteArray);
                this.out.write(byteArray);
            } catch (IOException ex) {
                Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        public void writeMSG(){
            try {
                this.out.write(msg);
            } catch (IOException ex) {
                Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        
        
        /*public void run(){
            byte[] szoveg = {0x02,0x30,0x31,0x52,0x50,0x56,0x31,0x03};
            String msg = "Hello!!!";
            //char[] charArray = msg.toCharArray();
            byte[] byteArray = toBytes(msg.toCharArray());
                
            try {
                //while (true){
                    Thread.sleep(3000);
                    this.out.write(byteArray);
                    Thread.sleep(3000);
                    this.out.write(szoveg);
                    Thread.sleep(500);
                //}
            } catch (Exception ex) {
                Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }*/
        private byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
            byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
}
        
    }
    
}