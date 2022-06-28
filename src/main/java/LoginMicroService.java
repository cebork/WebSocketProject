import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class LoginMicroService {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        ServerSocket server = new ServerSocket(1001);
        while (true) {
            Socket client = server.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            String message = in.readUTF();
            String login = message.split("#")[1].split(":")[1];
            String password = message.split("#")[2].split(":")[1];

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/users?user=root&password=");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE login='" + login + "' AND password='" + password + "'" );

            if (rs.next()) {
                out.writeUTF("Status:0");
                out.flush();
            }else{
                out.writeUTF("Status:1");
                out.flush();
            }

            out.close();
            in.close();
        }
    }
}

