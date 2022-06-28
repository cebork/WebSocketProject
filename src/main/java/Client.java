import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.Integer.parseInt;

// Client class
class Client {

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 1000)) {

            // writing to server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // reading from server
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null, toSend = "", username = null, password, message;
            boolean state = false;

            while (!"exit".equalsIgnoreCase(line)) {
                toSend = "";
                menu();
                line = sc.nextLine();
                switch(line){
                    case "1":
                        if(!state){
                            toSend += "mode:login";

                            System.out.print("Podaj login: ");
                            username = sc.nextLine();
                            toSend += "#login:" + username;

                            System.out.print("Podaj hasło: ");
                            password = sc.nextLine();
                            toSend += "#password:" + password;

                            out.writeUTF(toSend);
                            out.flush();

                            if(in.readUTF().split(":")[1].equals("0")){
                                state = true;
                                System.out.println("Zalogowano");
                            }else{
                                System.out.println("Login lub hasło jest niepoprawne");
                            }
                        }else{
                            System.out.println("Jesteś już zalogowany");
                        }

                        break;
                    case "2":
                        if(!state){
                            toSend += "mode:registration";

                            System.out.print("Podaj login: ");
                            String usernameR = sc.nextLine();
                            toSend += "#login:" + usernameR;

                            System.out.print("Podaj hasło: ");
                            password = sc.nextLine();
                            toSend += "#password:" + password;

                            out.writeUTF(toSend);
                            out.flush();

                            if(in.readUTF().split(":")[1].equals("0")){
                                System.out.println("Konto zostało utowrzone");
                            }else{
                                System.out.println("Login jest zajęty");
                            }
                        }else{
                            System.out.println("Jesteś już zalogowany");
                        }
                        break;
                    case "3":
                        if(state){
                            toSend += "mode:chat#username:";
                            toSend += username;

                            System.out.print("Napisz wiadomość: ");
                            message = sc.nextLine();
                            toSend += "#message:" + message;

                            System.out.println(toSend);
                            out.writeUTF(toSend);
                            out.flush();

                        }else{
                            System.out.println("Nie jesteś zalogowany");
                        }
                        break;
                    case "4":
                        toSend += "mode:table";
                        out.writeUTF(toSend);
                        out.flush();
                        String[] response = in.readUTF().split("#");
                        for (String msg : response) {
                            System.out.println(msg);
                        }
                        break;
                    case "5":
                        if(state) {
                            Base64.Encoder encoder = Base64.getEncoder();
                            toSend += "mode:uploading";
                            System.out.print("Podaj ścieżkę do pliku pliku: ");
                            String path = sc.nextLine();
                            toSend = toSend + "#fileName:" + path;
                            File file = new File(path);
                            if (file.exists()) {
                                String bufferString = "";
                                int bytes = 0;
                                FileInputStream fis = new FileInputStream(file);
                                long FileLength = file.length();
                                toSend = toSend + "#fileLength:" + FileLength;
                                byte[] buffer = new byte[2024];
                                int i = 0;
                                while ((bytes = fis.read(buffer)) != -1){
                                    bufferString = bufferString + "#buffer" + i + ":" + encoder.encodeToString(buffer);
                                    i++;
                                }
                                toSend += bufferString;
                                out.writeUTF(toSend);
                                out.flush();
                                fis.close();
                                if(in.readUTF().split(":")[1].equals("0")){
                                    System.out.println("Pomyślnie przesłano plik na serwer");
                                }else{
                                    System.out.println("Plik juz istnieje na serwerze");
                                }
                            } else {
                                System.out.println("Plik nie istnieje");
                            }
                        }else{
                            System.out.println("Nie jesteś zalogowany");
                        }
                        break;
                    case "6":
                        if(state) {
                            Base64.Decoder decoder = Base64.getDecoder();
                            toSend += "mode:downloading";
                            System.out.print("Podaj nazwę pliku: ");
                            String fileName = sc.nextLine();
                            File file = new File("C:\\Users\\Czarek\\IdeaProjects\\LastChangeTS\\FileClient\\" + fileName);
                            if(!file.exists()){
                                toSend = toSend + "#fileName:" + fileName;
                                out.writeUTF(toSend);
                                String message1 = in.readUTF();
                                String status = message1.split("#")[0].split(":")[1];
                                int fileLength = parseInt(message1.split("#")[2].split(":")[1]);
                                FileOutputStream fos = new FileOutputStream(file, true);
                                byte[] buffer;
                                int i = 3;
                                while (fileLength > 0) {
                                    buffer = decoder.decode(message1.split("#")[i].split(":")[1]);
                                    fos.write(buffer);
                                    fileLength -= 2024;
                                    i++;
                                }
                                if(status.equals("0")){
                                    System.out.println("Pomyślnie pobrano plik");
                                }else{
                                    System.out.println("Taki plik nie istnieje na serwerze");
                                }
                                fos.close();

                            }else{
                                System.out.println("Plik juz istnieje u klienta");
                            }
                        }else{
                            System.out.println("Nie jesteś zalogowany");
                        }
                        break;
                    case "7":
                        System.out.println("Wylogowałeś się");
                        state = false;
                        break;
                    case "8":
                        System.out.println("Dobranoc");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Nie ma takiej opcji");
                }
            }

            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void menu(){
        System.out.println("\nMENU\n1. Logowanie\n2. Rejestracja\n3. Chat\n4. Tablica\n5. Wysyłanie plików\n6. Pobieranie plików\n7. Wylogowywanie\n8. Exit");
    }

}
