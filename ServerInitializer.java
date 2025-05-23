import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;


public class ServerInitializer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private boolean serverRunning = true;
    private static String upTime;

    public static void main(String[] args) {
        new Thread(() -> {
            try{
                ServerSocket serverSocket = new ServerSocket(3000);
                upTime = LocalTime.now().toString();
                boolean serverRunning = true;
                while (serverRunning) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start();
                }

            }catch(IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    public static class ClientHandler extends Thread {

        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try{
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            String message = in.readUTF();

            switch (message) {
                case "TIME":
                    out.writeUTF(LocalTime.now().toString());
                    out.flush();
                    break;
                case "DATE":
                    out.writeUTF(LocalDate.now().toString());
                    out.flush();
                    break;
                case "UPTIME":
                    out.writeUTF(upTime);
                    out.flush();
                    break;
                case "BYE":
                    clientSocket.close();
                    break;
                case "HELP":
                    out.writeUTF("These are available Commands TIME/DATE/UPTIME/BYE");
                    out.flush();
                default:
                    out.writeUTF(message);
                    out.flush();

            }
        }catch(IOException e){
            e.printStackTrace();}
        }
    }
}
