import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
/**
 * A superclass for all game cards
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */
public abstract class Card extends JFrame {

    protected String name;
    protected String filename;
    protected JButton card;
    private boolean isSelected;

    protected static final int BUTTON_WIDTH = 100;
    protected static final int BUTTON_HEIGHT = 100;

    public Card(String name, String filename) {
        this.name = name;
        this.filename = filename;
        this.card = new JButton();
        this.card.setName(name);
        this.isSelected = false;

        ImageIcon image = new ImageIcon(this.filename);
        Image resizedImage = image.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
        image.setImage(resizedImage);
        this.card.setIcon(image);
        this.card.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.card.setText(this.name);
        this.card.setFont(new Font("Monospaced", Font.BOLD, 15));
        this.card.setHorizontalTextPosition(JButton.CENTER);
        this.card.setVerticalTextPosition(JButton.CENTER);
        this.add(card);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JButton getButton() {
        return this.card;
    }
}