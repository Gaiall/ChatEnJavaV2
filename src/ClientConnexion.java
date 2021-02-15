import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnexion implements Runnable{
    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private final String name;
    private final ChatClient chat;
    public boolean connected;

    public ClientConnexion(String host, int port, String name, ChatClient chat){
        this.name = name;
        this.chat = chat;
        try {
            connexion = new Socket(host, port);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                writer = new PrintWriter(connexion.getOutputStream(), true);
                reader = new BufferedInputStream(connexion.getInputStream());

                String response = read();
                chat.updateChat(response);
                //On essaie en continue de recevoir un message du serveur
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //Envoie du message au serveur
    public void sendMsg(String msg){
        writer.write(this.name + ": " + msg);
        writer.flush();
    }

    //Reçoit un message du serveur
    private String read() throws IOException{
        byte[] b = new byte[4096];
        int stream = reader.read(b);
        return new String(b, 0, stream);
    }

    public void interrupt(){
        Thread.currentThread().interrupt();
    }
}
