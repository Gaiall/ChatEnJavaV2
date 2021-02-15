import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatClient {
    private final JTextArea leChat = new JTextArea();

    public ChatClient() {
        final boolean[] connected = new boolean[]{false};
        /*============================================== Le stockage de la Connexion ==============================================*/
        final ClientConnexion[] client = new ClientConnexion[1];
        /*============================================== Creation des boites ==============================================*/
        int longueur = 500;
        int largeur = 600;
        FenetreChat fenetre = new FenetreChat("Squaïp", longueur, largeur);
        Box boiteDeNom = Box.createHorizontalBox();
        Box boiteDeIP = Box.createHorizontalBox();
        Box boiteDePort = Box.createHorizontalBox();
        Box boiteDeAdresse = Box.createHorizontalBox();
        Box boiteDeBoutonConnecter = Box.createHorizontalBox();
        Box boiteInfoConnexion = Box.createVerticalBox();
        Box boiteLeChat = Box.createVerticalBox();
        Box boiteLesBoites = Box.createVerticalBox();

        /*============================================== Creation des zones de texte ==============================================*/
        JTextField zoneDeNom = new JTextField(" nom");
        JTextField zoneDeIP = new JTextField(" adresse");
        JTextField zoneDePort = new JTextField(" port");
        JLabel labelDeNom = new JLabel("Nom :");
        JLabel labelDeIP = new JLabel("Adresse IP :");
        JLabel labelDePort = new JLabel("Port :");
        JButton boutonConnecter = new JButton("Connexion");
        JTextArea listeDesConnectes = new JTextArea();
        listeDesConnectes.setText("");
        /* On rempli le chat avec du vide pour l'affichage */
        for(int i = 0; i < 23; i++){
            leChat.append("\n");
        }
        JTextField leMessage = new JTextField("");
        JButton boutonEnvoyer = new JButton("Envoyer");

        JPanel zoneDeChat = new JPanel(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();

        /*============================================= Assemblage de la zone de chat ==============================================*/
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.weightx = 1;
        contrainte.weighty = 1;
        contrainte.gridx = 0;
        contrainte.gridy = 0;
        contrainte.gridheight = 5;
        contrainte.gridwidth = 1;
        listeDesConnectes.setPreferredSize(new Dimension(175, 450));
        zoneDeChat.add(listeDesConnectes, contrainte);

        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.gridx = 1;
        contrainte.gridy = 0;
        contrainte.gridwidth = 2;
        contrainte.gridheight = 3;
        leChat.setPreferredSize(new Dimension(275, 400));
        zoneDeChat.add(leChat, contrainte);

        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.gridx = 1;
        contrainte.gridy = 3;
        contrainte.gridwidth = 2;
        contrainte.gridheight = 1;
        leMessage.setPreferredSize(new Dimension(275, 25));
        zoneDeChat.add(leMessage, contrainte);

        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.gridx = 1;
        contrainte.gridy = 4;
        contrainte.gridwidth = 2;
        contrainte.gridheight = 1;
        boutonEnvoyer.setPreferredSize(new Dimension(275, 25));
        zoneDeChat.add(boutonEnvoyer, contrainte);

        /*============================================== Comportements ==============================================*/
        /* Le bouton pour se connecter au serveur du chat */
        boutonConnecter.addActionListener(event -> {
            if(!connected[0]){
                String adresseIP = zoneDeIP.getText();
                String port = zoneDePort.getText();
                Boolean adresseOK = adresseIP.matches("\\s*\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}\\s*") || adresseIP.equals("localhost");
                Boolean portOK = port.matches("\\s*\\d{1,4}\\s*");
                adresseIP = adresseIP.trim();
                port = port.trim();
                if(adresseOK && portOK){
                    System.out.println(adresseIP + ":" + port);
                    client[0] = new ClientConnexion(adresseIP, Integer.parseInt(port), zoneDeNom.getText(), this);
                    if(client[0].connected){
                        Thread threadClient = new Thread(client[0]);
                        threadClient.start();

                        /* une fois connecté on empêche les infos de connection d'etre modifiées */
                        zoneDeNom.setEditable(false);
                        zoneDeIP.setEditable(false);
                        zoneDePort.setEditable(false);
                        /* On permet l'utilisation du chat */
                        boutonEnvoyer.setEnabled(true);
                        leMessage.setEditable(true);
                        /* On en fait un bouton de déconnexion */
                        boutonConnecter.setText("Déconnexion");
                        leMessage.requestFocus();
                        connected[0] = true;
                    } else {
                        updateChat("IMPOSSIBLE DE SE CONNECTER (IP et/ou port incorrect");
                    }
                } else {
                    updateChat("IMPOSSIBLE DE SE CONNECTER (IP et/ou port incorrect");
                }
            } else {
                client[0].interrupt();
                /* on déconnecte donc on autorise de nouveau de tout changer */
                zoneDeNom.setEditable(true);
                zoneDeIP.setEditable(true);
                zoneDePort.setEditable(true);
                boutonEnvoyer.setEnabled(false);
                leMessage.setEditable(false);
                //On en fait un bouton de déconnexion
                boutonConnecter.setText("Connexion");
                connected[0] = false;
            }
        });

        /* Changements esthétiques pour les zones de saisie  des infos de connexion*/
        zoneDeNom.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                if(zoneDeNom.getText().equals(" nom")){
                    zoneDeNom.setText("");
                }
            }

            public void focusLost(FocusEvent e){
                if(zoneDeNom.getText().equals("")){
                    zoneDeNom.setText(" nom");
                }
            }
        });
        zoneDeIP.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                if(zoneDeIP.getText().equals(" adresse")){
                    zoneDeIP.setText("");
                }
            }

            public void focusLost(FocusEvent e){
                if(zoneDeIP.getText().equals("")){
                    zoneDeIP.setText(" adresse");
                }
            }
        });
        zoneDePort.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                if(zoneDePort.getText().equals(" port")){
                    zoneDePort.setText("");
                }
            }

            public void focusLost(FocusEvent e){
                if(zoneDePort.getText().equals("")){
                    zoneDePort.setText(" port");
                }
            }
        });
        /* Qol pour les zones de saisie des infos de connexion*/
        KeyAdapter connexionAvecEnter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    boutonConnecter.doClick();
            }
        };
        zoneDeNom.addKeyListener(connexionAvecEnter);
        zoneDeIP.addKeyListener(connexionAvecEnter);
        zoneDePort.addKeyListener(connexionAvecEnter);

        /* Envoie du message quand on appuie sur entrée */
        leMessage.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent event){
                if(event.getKeyCode()==KeyEvent.VK_ENTER){
                    boutonEnvoyer.doClick();
                }
            }
        });
        /* Envoie du message quand on appuie sur le bouton envoyer */
        boutonEnvoyer.addActionListener(e -> {
            String message = leMessage.getText();
            leMessage.setText("");
            if(!(message.trim().equals(""))){
                client[0].sendMsg(message);
            }
        });

        /* On empêche d'utiliser le chat tant qu'on est pas connecté */
        boutonEnvoyer.setEnabled(false);
        leMessage.setEditable(false);

        zoneDeNom.setMaximumSize(new Dimension(100, 20));
        zoneDeIP.setPreferredSize(new Dimension(200, 20));
        zoneDePort.setPreferredSize(new Dimension(70, 20));

        listeDesConnectes.setEditable(false);
        listeDesConnectes.setBorder(new LineBorder(Color.black, 1));
        leChat.setEditable(false);
        leChat.setBorder(new LineBorder(Color.black, 1));

        /*============================================== Ajout des éléments aux boites ==============================================*/
        boiteDeNom.add(labelDeNom);
        boiteDeNom.add(Box.createRigidArea(new Dimension(10, 0)));
        boiteDeNom.add(zoneDeNom);

        boiteDeIP.add(labelDeIP);
        boiteDeIP.add(Box.createRigidArea(new Dimension(10, 0)));
        boiteDeIP.add(zoneDeIP);

        boiteDePort.add(labelDePort);
        boiteDePort.add(Box.createRigidArea(new Dimension(10, 0)));
        boiteDePort.add(zoneDePort);

        boiteDeAdresse.add(boiteDeIP);
        boiteDeAdresse.add(Box.createRigidArea(new Dimension(10, 0)));
        boiteDeAdresse.add(boiteDePort);

        boiteDeBoutonConnecter.add(boutonConnecter);

        boiteInfoConnexion.add(boiteDeNom);
        boiteInfoConnexion.add(Box.createRigidArea(new Dimension(0, 10)));
        boiteInfoConnexion.add(boiteDeAdresse);
        boiteInfoConnexion.add(Box.createRigidArea(new Dimension(0, 10)));
        boiteInfoConnexion.add(boiteDeBoutonConnecter);

        boiteLeChat.add(zoneDeChat);

        boiteLesBoites.add(boiteInfoConnexion);
        boiteLesBoites.add(Box.createRigidArea(new Dimension(0, 10)));
        boiteLesBoites.add(boiteLeChat);

        /*============================================== Affichage de la fenetre ==============================================*/
        fenetre.getContentPane().add(boiteLesBoites);
        fenetre.setResizable(false);
        fenetre.setVisible(true);
    }

    public void updateChat(String msg) {
        int finDeLigne;
        try{
            finDeLigne = leChat.getLineEndOffset(0);
            leChat.replaceRange("", 0, finDeLigne);
        } catch(Exception exception){
            exception.printStackTrace();
        }
        leChat.append("\n " + msg);
    }
}
