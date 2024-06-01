import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class for handling the player's operations
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */
public class Barista extends Card {

	private String name;
	/**
	 * states barista's special skill
	 */
	private String skill;
	/**
	 * barista's (player's) current revenue of tips
	 */
	private int revenue;
	/**
	 * player's hand of ingredients for game play
	 */
	private ArrayList<Ingredient> hand;
	/**
	 * 
	 * @param name is the name of the barista
	 * @param filename is the file name of the barista's image
	 */
	public Barista(String name, String filename) {
		super(name, filename);
		this.revenue = 0;

		if (name.equals("Subin")) {
			this.skill = "Each customer waits \nfor ONE MORE TURN";
		}
		if (name.equals("Nathan")) {
			this.skill = "Each customer pays \n+1.00 DOLLAR in tip";
		}
		if (name.equals("Jessica")) {
			this.skill = "KIND customers \nappear more often";
		}
		if (name.equals("Betmann")) {
			this.skill = "Starting hand \nhas +1 INGREDIENT";
		}

		this.hand = new ArrayList<Ingredient>();

		String cardText = this.getButton().getText() + "\n\n\n\n\n\n\n" + this.skill;
		this.getButton().setText("<html>" + cardText.replaceAll("\n", " <br>") + "</html>");
	}

	public String getSkill() {
		return skill;
	}

	public ArrayList<Ingredient> getHand() {
		return this.hand;
	}
	
	public Ingredient getLast() {
		return this.hand.get(this.hand.size()-1);
	}

	public void draw(ArrayList<Ingredient> deck) {
		this.draw(1, deck);
	}
	
	public int getRevenue() {
		return this.revenue;
	}
	
	public void increaseRevenue(int amount) {
		this.revenue += amount;
	}
	
	public void resetRevenue() {
		this.revenue = 0;
	}

	public void draw(int x, ArrayList<Ingredient> deck) {
		for (int i = 0; i < x; i++) {
			Ingredient ingredient = deck.remove(0);
			this.hand.add(ingredient);
		}
	}
}