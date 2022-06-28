import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class TableMicroService {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket server = new ServerSocket(1004);
        while (true) {
            Socket client = server.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/msg?user=root&password=");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM (select * from msg ORDER BY id DESC LIMIT 10) as lastTen ORDER BY lastTen.id ASC;");

            String msgToResponse;
            String usernameToResponse;
            String response = "";
            while(rs.next()){
                msgToResponse = rs.getString("msg");
                usernameToResponse = rs.getString("nick");
                response += usernameToResponse + ": " + msgToResponse + "#";
            }
            out.writeUTF(response);
            out.flush();
            out.close();
            in.close();
        }
    }
}

