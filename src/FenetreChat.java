import javax.swing.*;
import java.awt.*;

public class FenetreChat extends JFrame{

    public FenetreChat(String titre, int longueur, int largeur) {
        this.setTitle(titre);
        this.setSize(longueur, largeur);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jpanel = new JPanel();
        jpanel.setBackground(Color.WHITE);
        this.setContentPane(jpanel);
    }
}
