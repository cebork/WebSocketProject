import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ChatMicroService {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket server = new ServerSocket(1003);
        while (true) {
            Socket client = server.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            String message = in.readUTF();
            String username = message.split("#")[1].split(":")[1];
            String msg = message.split("#")[2].split(":")[1];

            System.out.println(message);
            System.out.println(username);
            System.out.println(msg);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/msg?user=root&password=");
            Statement stmt = con.createStatement();

            stmt.executeUpdate("INSERT INTO msg VALUES (NULL, '" + username + "', '" + msg +"');");

            out.close();
            in.close();
        }
    }
}

