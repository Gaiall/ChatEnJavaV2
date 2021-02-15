import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat {
    private int port = 7777;
    private String host = "localhost";
    private ServerSocket server = null;
    private boolean isRunning = true;
    private List<ClientProcessor> listeClient = null;

    public ServerChat() {
        try {
            server = new ServerSocket(port, 50, InetAddress.getByName(host));
            listeClient = new ArrayList<>();
            System.out.println("Serveur lancé sur " + server.getInetAddress() + ":" + server.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerChat(String host, int port) {
        this.port = port;
        this.host = host;
        try {
            server = new ServerSocket(port, 50, InetAddress.getByName(host));
            listeClient = new ArrayList<>();
            System.out.println("Serveur lancé sur " + server.getInetAddress() + ":" + server.getLocalPort());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void open(){
        //Il faut gérer le serveur dans un Thread a part pour gérer plusieurs requêtes
        Thread thread = new Thread(() -> {
            ClientProcessor client;
            while (isRunning){
                try {
                    //Wait pour un client
                    Socket socketClient = server.accept();

                    //On ajoute le client a la liste des client
                    client = new ClientProcessor(socketClient, this);
                    listeClient.add(client);

                    Thread thread1 = new Thread(client);
                    thread1.start();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
                server.close();
            } catch (IOException e){
                e.printStackTrace();
                server = null;
            }
        });

        thread.start();
    }

    public void close(){
        isRunning = false;
    }

    //Envoie un message a tout les clients
    public void newMsg(String msg){
        for (ClientProcessor client: listeClient) client.sendMsg(msg);
    }
}
