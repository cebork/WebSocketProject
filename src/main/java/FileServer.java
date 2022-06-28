import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Base64;

import static java.lang.Integer.parseInt;

public class FileServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket server = new ServerSocket(1005);
        while (true) {
            Socket client = server.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            Base64.Decoder decoder = Base64.getDecoder();
            String message = in.readUTF();
            String mode = message.split("#")[0].split(":")[1];

            if(mode.equals("uploading")) {
                String fileName = message.split("#")[1].split(":")[1];
                int fileLength = parseInt(message.split("#")[2].split(":")[1]) ;
                File file = new File("C:\\Users\\Czarek\\IdeaProjects\\LastChangeTS\\FileServer\\" + fileName);
                if(!file.exists()){
                    FileOutputStream fos = new FileOutputStream(file, true);
                    byte[] buffer;
                    int i = 3;
                    while (fileLength > 0) {
                        buffer = decoder.decode(message.split("#")[i].split(":")[1]);
                        fos.write(buffer);
                        fileLength -= 2024;
                        i++;
                    }
                    out.writeUTF("Status:0");
                    fos.close();
                }else{
                    out.writeUTF("Status:1");
                }
            }else if(mode.equals("downloading")){
                Base64.Encoder encoder = Base64.getEncoder();
                String fileName = message.split("#")[1].split(":")[1];
                String response = "";
                File file = new File("C:\\Users\\Czarek\\IdeaProjects\\LastChangeTS\\FileServer\\" + fileName);
                if (file.exists()) {
                    response += "Status:0#fileName:" + fileName;
                    String bufferString = "";
                    int bytes = 0;
                    FileInputStream fis = new FileInputStream(file);
                    long FileLength = file.length();
                    response = response + "#fileLength:" + FileLength;
                    byte[] buffer = new byte[2024];
                    int i = 0;
                    while ((bytes = fis.read(buffer)) != -1){
                        bufferString = bufferString + "#buffer" + i + ":" + encoder.encodeToString(buffer);
                        i++;
                    }
                    response += bufferString;
                    out.writeUTF(response);
                    out.flush();
                    fis.close();
                } else {
                    out.writeUTF("Status:1");
                }

            }
            out.close();
            in.close();
        }
    }
}

