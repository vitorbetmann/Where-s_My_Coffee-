import javax.swing.*;
import java.awt.Image;
import java.util.Random;
/**
 * A class for handling the ingredient's components
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */
public class Ingredient extends Card {
    
    private static final int BUTTON_WIDTH = 125;
    private static final int BUTTON_HEIGHT = 175;
    
    public Ingredient() {
        super("", "");
        this.setName(getRandomIngredient());

        ImageIcon image = new ImageIcon(getFilename());
        Image resizedImage = image.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
        image.setImage(resizedImage);
        this.getButton().setIcon(image);
    }

    public Ingredient(String name) {
        super(name, "");
        this.getButton().setText("");

        ImageIcon image = new ImageIcon(getFilename());
        Image resizedImage = image.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
        image.setImage(resizedImage);
        this.getButton().setIcon(image);
    }
    /**
     * Method to randomly get an ingredient
     * @return random ingredient
     */
    public String getRandomIngredient() {
        Random rand = new Random();
        int choice = rand.nextInt(5);

        switch (choice) {
        case 0:
            this.setName("Coffee");
            break;
        case 1:
            this.setName("Milk");
            break;
        case 2:
            this.setName("Matcha");
            break;
        case 3:
            this.setName("Chai");
            break;
        case 4:
            this.setName("Tea");
            break;
        }

        return this.getName();
    }
    /**
     * 
     * @return ingredient's image file name
     */
    public String getFilename() {
        return "src/" + this.getName().toLowerCase() + ".png";
    }
}