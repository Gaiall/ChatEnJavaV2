import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable {
    private final Socket socket;
    private final ServerChat serveur;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;

    ClientProcessor(Socket client, ServerChat serveur) {
        this.socket = client;
        this.serveur = serveur;
    }

    @Override
    public void run() {

        while(!socket.isClosed()){
            try {
                writer = new PrintWriter(socket.getOutputStream());
                reader = new BufferedInputStream(socket.getInputStream());

                //Attente du client
                String response = read();
                InetSocketAddress remote = (InetSocketAddress) socket.getRemoteSocketAddress();

                //Du debug
                String debug;
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Reçue : " + response + "\n";
                System.err.println("\n" + debug);

                //On dit au serveur d'envoyer le message reçu de la part du client
                serveur.newMsg(response);
            } catch (SocketException e){
                //Connexion interrompue, on s'en fout
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //Methode pour lire les réponses
    //(partie complètement pillée dans l'archive openclassroom, à voir si ça marche)
    private @NotNull String read() throws IOException {
        String response;
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

    //Envoie un message de chat au client
    public void sendMsg(String msg){
        writer.write(msg);
        writer.flush(); //ne pas oublier, c'est important
    }
}
