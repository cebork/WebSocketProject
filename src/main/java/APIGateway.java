import java.io.*;
import java.net.*;

// Server class
class APIGateway {
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(1000);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"+ client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock= new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            DataOutputStream out = null;
            DataInputStream in = null;
            try {

                // get the outputstream of client
                out = new DataOutputStream(clientSocket.getOutputStream());

                // get the inputstream of client
                in = new DataInputStream(clientSocket.getInputStream());

                String line, mode;
                while ((line = in.readUTF()) != null) {
                    mode = line.split("#")[0].split(":")[1];
                    switch (mode){
                        case "login":
                            Socket loginSocket = new Socket("localhost", 1001);
                            DataOutputStream loginOut = new DataOutputStream(loginSocket.getOutputStream());
                            DataInputStream loginIn = new DataInputStream(loginSocket.getInputStream());
                            loginOut.writeUTF(line);
                            out.writeUTF(loginIn.readUTF());
                            loginIn.close();
                            loginOut.close();
                            break;
                        case "registration":
                            Socket registrationSocket = new Socket("localhost", 1002);
                            DataOutputStream registrationOut = new DataOutputStream(registrationSocket.getOutputStream());
                            DataInputStream registrationIn = new DataInputStream(registrationSocket.getInputStream());
                            registrationOut.writeUTF(line);
                            out.writeUTF(registrationIn.readUTF());
                            registrationIn.close();
                            registrationOut.close();
                            break;
                        case "chat":
                            Socket chatSocket = new Socket("localhost", 1003);
                            DataOutputStream chatOut = new DataOutputStream(chatSocket.getOutputStream());
                            DataInputStream chatIn = new DataInputStream(chatSocket.getInputStream());
                            chatOut.writeUTF(line);
                            chatIn.close();
                            chatOut.close();
                            break;
                        case "table":
                            Socket tableSocket = new Socket("localhost", 1004);
                            DataOutputStream tableOut = new DataOutputStream(tableSocket.getOutputStream());
                            DataInputStream tableIn = new DataInputStream(tableSocket.getInputStream());
                            tableOut.writeUTF(line);
                            out.writeUTF(tableIn.readUTF());
                            tableIn.close();
                            tableOut.close();
                            break;
                        case "uploading":
                            Socket uploadSocket = new Socket("localhost", 1005);
                            DataOutputStream uploadOut = new DataOutputStream(uploadSocket.getOutputStream());
                            DataInputStream uploadIn = new DataInputStream(uploadSocket.getInputStream());
                            uploadOut.writeUTF(line);
                            out.writeUTF(uploadIn.readUTF());
                            uploadIn.close();
                            uploadOut.close();
                            break;
                        case "downloading":
                            Socket downloadSocket = new Socket("localhost", 1005);
                            DataOutputStream downloadOut = new DataOutputStream(downloadSocket.getOutputStream());
                            DataInputStream downloadIn = new DataInputStream(downloadSocket.getInputStream());
                            downloadOut.writeUTF(line);
                            out.writeUTF(downloadIn.readUTF());
                            downloadIn.close();
                            downloadOut.close();
                            break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

