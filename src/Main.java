
/*==============================================  ==============================================*/
public class Main{
    public static void main(String[] args){
       ServerChat serveur = new ServerChat();
       serveur.open();
       ChatClient chat1 = new ChatClient();
       ChatClient chat2 = new ChatClient();
    }
}
