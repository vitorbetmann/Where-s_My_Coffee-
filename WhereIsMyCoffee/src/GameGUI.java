import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A class for handling the GUI components of the game, including user
 * interaction via ActionListener and MouseListener
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */

public class GameGUI extends JFrame implements ActionListener, MouseListener {
	/**
	 * width of GUI screen
	 */
	private static final int WIDTH = 1200;
	/**
	 * height of GUI screen
	 */
	private static final int HEIGHT = 780;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 150;
	/**
	 * deck of ingredient cards as an array
	 */
	private static ArrayList<Ingredient> deckArray;
	/**
	 * ingredient cards trash pile as an array
	 */
	private static ArrayList<Ingredient> trashArray;
	/**
	 * line of customers as an array
	 */
	private static ArrayList<Customer> lineArray;
	private static ArrayList<String> tempChoiceArray;
	/**
	 * player's current turn starting from 0
	 */
	private static int turn;
	/**
	 * number of customers lost starting from 0
	 */
	private static int customersLost;
	/**
	 * list of playable barista characters (only 4 available in this version)
	 */
	private ArrayList<Barista> baristas;
	/**
	 * player's chosen barista
	 */
	private Barista player;

	private JPanel mainScreen;
	private JPanel charSelectScreen;
	private JPanel gameScreen;
	private JPanel line;
	private JPanel cards;
	private JPanel decks;
	private JPanel southPanel;

	private JLabel messageToPlayer;
	private JLabel turnLabel;
	private JLabel tipsLabel;
	private JLabel customersLostLabel;

	private JButton confirm;
	private JButton cancel;
	private JButton deck;
	private JButton trash;

	/**
	 * 
	 * @param title is the name of the game
	 * @throws FileNotFoundException for opening file
	 */
	public GameGUI(String title) throws FileNotFoundException {
		super(title);

		// TITLE SCREEN
		this.mainScreen = new JPanel();

		// maybe
		this.mainScreen.setLayout(new FlowLayout());

		JLabel titleLabel = new JLabel("WHERE IS MY COFFEE?");
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 100));
		titleLabel.setBackground(Color.DARK_GRAY);

		JButton playButton = new JButton("New Game");
		playButton.setFocusable(false);
		playButton.setFont(new Font("Monospaced", Font.PLAIN, 80));
		playButton.setPreferredSize(new Dimension(500, 120));
		playButton.addActionListener(this);

		mainScreen.add(titleLabel);
		mainScreen.add(playButton);

		// CHARACTER SELECTION SCREEN
		this.charSelectScreen = new JPanel(new BorderLayout());
		this.messageToPlayer = new JLabel("Pick a Barista:");
		this.messageToPlayer.setFont(new Font("Monospaced", Font.BOLD, 40));

		this.baristas = new ArrayList<Barista>();
		GridLayout charGrid = new GridLayout(1, 4, 20, 10);

		JPanel charPanel = new JPanel();
		charPanel.setBounds(10, 10, 4 * BUTTON_WIDTH, BUTTON_HEIGHT);
		charPanel.setLayout(charGrid);

		for (int i = 0; i < 4; i++) {
			String name = "";

			switch (i) {
			case 0:
				name = "Betmann";
				break;
			case 1:
				name = "Jessica";
				break;
			case 2:
				name = "Subin";
				break;
			case 3:
				name = "Nathan";
				break;
			}

			String source = "src/" + name.toLowerCase() + ".png";
			Barista b = new Barista(name, source);
			b.getButton().setActionCommand(b.getName());
			b.getButton().addActionListener(this);
			this.baristas.add(b);
			charPanel.add(b.getButton());
		}

		JPanel option = new JPanel(new FlowLayout());

		this.confirm = new JButton("Confirm");
		this.confirm.addActionListener(this);
		this.confirm.setVisible(false);

		this.cancel = new JButton("Actually...");
		this.cancel.addActionListener(this);
		this.cancel.setVisible(false);

		this.charSelectScreen.add(this.messageToPlayer, BorderLayout.NORTH);
		this.charSelectScreen.add(charPanel, BorderLayout.CENTER);
		option.add(this.confirm);
		option.add(this.cancel);
		this.charSelectScreen.add(option, BorderLayout.SOUTH);

		// for game screen
		GameGUI.tempChoiceArray = new ArrayList<String>();
		GameGUI.deckArray = new ArrayList<Ingredient>();

		GameGUI.trashArray = new ArrayList<Ingredient>();
		GameGUI.lineArray = new ArrayList<Customer>();

		this.gameScreen = new JPanel();
		this.gameScreen.setLayout(new BorderLayout());

		GameGUI.turn = 1;
		GameGUI.customersLost = 0;

		customersLostLabel = new JLabel("Customers Lost: " + GameGUI.customersLost);
		turnLabel = new JLabel("Turn: " + GameGUI.turn);
		tipsLabel = new JLabel();

		this.line = new JPanel();
		GridLayout lineGrid = new GridLayout(1, 0);
		this.line.setPreferredSize(new Dimension(600, 380));
		this.line.setLayout(lineGrid);

		this.cards = new JPanel();
		GridLayout cardsGrid = new GridLayout(1, 0);
		this.cards.setLayout(cardsGrid);

		this.decks = new JPanel();
		GridLayout decksGrid = new GridLayout(1, 0);
		this.decks.setLayout(decksGrid);

		ImageIcon deckImage = new ImageIcon("src/deck.jpg");
		Image resizedDeckImage = deckImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
		deckImage.setImage(resizedDeckImage);
		this.deck = new JButton(deckImage);
		this.deck.addMouseListener(this);
		this.deck.addActionListener(this);
		this.deck.setActionCommand("deck");
		this.decks.add(this.deck);

		ImageIcon trashImage = new ImageIcon("src/trash_empty.jpg");
		Image resizedTrashImage = trashImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
		trashImage.setImage(resizedTrashImage);
		this.trash = new JButton(trashImage);
		this.trash.addMouseListener(this);
		this.trash.addActionListener(this);
		this.trash.setActionCommand("trash");
		this.decks.add(this.trash);

		JPanel northPanel = new JPanel();
		GridLayout northGrid = new GridLayout(1, 0);
		northPanel.setPreferredSize(new Dimension(1000, 100));
		northPanel.setLayout(northGrid);
		northPanel.add(this.customersLostLabel);
		northPanel.add(this.turnLabel);
		northPanel.add(this.tipsLabel);

		this.southPanel = new JPanel();
		GridLayout southGrid = new GridLayout(0, 3);
		this.southPanel.setPreferredSize(new Dimension(1000, 250));
		this.southPanel.setLayout(southGrid);

		JButton nextTurnButton = new JButton("Next Turn");
		nextTurnButton.setActionCommand("Next Turn");
		nextTurnButton.addActionListener(this);

		this.gameScreen.add(northPanel, BorderLayout.NORTH);
		this.gameScreen.add(this.line, BorderLayout.WEST);
		this.gameScreen.add(nextTurnButton, BorderLayout.EAST);
		this.gameScreen.add(this.southPanel, BorderLayout.SOUTH);

		this.add(mainScreen);

		// the rest
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JButton b = (JButton) e.getSource();
		String command = b.getActionCommand();
		switch (command) {
		case "deck":
			ImageIcon deckImage = new ImageIcon("src/deck.jpg");
			Image resizedDeckImage = deckImage.getImage().getScaledInstance((int) (BUTTON_WIDTH * 1.5),
					(int) (BUTTON_HEIGHT * 1.5), 0);
			deckImage.setImage(resizedDeckImage);
			this.deck.setIcon(deckImage);
			break;
		case "trash":
			String imageString = GameGUI.trashArray.isEmpty()? "src/trash_empty.jpg" : "src/trash_full.jpg";

			ImageIcon trashImage = new ImageIcon(imageString);
			Image resizedTrashImage = trashImage.getImage().getScaledInstance((int) (BUTTON_WIDTH * 1.5),
					(int) (BUTTON_HEIGHT * 1.5), 0);
			trashImage.setImage(resizedTrashImage);
			this.trash.setIcon(trashImage);
			break;
		}
		this.mainScreen.revalidate();
		this.mainScreen.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JButton b = (JButton) e.getSource();
		String command = b.getActionCommand();
		switch (command) {
		case "deck":
			ImageIcon deckImage = new ImageIcon("src/deck.jpg");
			Image resizedDeckImage = deckImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
			deckImage.setImage(resizedDeckImage);
			this.deck.setIcon(deckImage);
			break;
		case "trash":
			String imageString = GameGUI.trashArray.isEmpty()? "src/trash_empty.jpg" : "src/trash_full.jpg";
			ImageIcon trashImage = new ImageIcon(imageString);
			Image resizedTrashImage = trashImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
			trashImage.setImage(resizedTrashImage);
			this.trash.setIcon(trashImage);
			break;
		}
		this.mainScreen.revalidate();
		this.mainScreen.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "New Game":
			this.mainScreen.removeAll();

			ImageIcon tutorial = new ImageIcon("src/tutorial1.jpg");
			JOptionPane.showMessageDialog(this, tutorial);
			tutorial = new ImageIcon("src/tutorial2.jpg");
			JOptionPane.showMessageDialog(this, tutorial);
			tutorial = new ImageIcon("src/tutorial3.jpg");
			JOptionPane.showMessageDialog(this, tutorial);

			this.mainScreen.add(charSelectScreen);

			break;
		case "Betmann":
		case "Jessica":
		case "Subin":
		case "Nathan":
			this.messageToPlayer.setText("You have chosen " + command);
			for (Barista barista : this.baristas) {
				if (barista.getName().equals(command)) {
					this.player = barista;
					continue;
				}
				barista.getButton().setVisible(false);
			}

			this.confirm.setVisible(true);
			this.cancel.setVisible(true);
			break;
		case "Confirm":
			for (Barista barista : baristas) {
				barista.getButton().setVisible(true);
			}
			this.confirm.setVisible(false);
			this.cancel.setVisible(false);

			GameGUI.deckArray.clear();
			GameGUI.trashArray.clear();
			String filename = "src/ingredients.txt";
			File ingredients = new File(filename);
			for (byte j = 0; j < 3; j++) {
				Scanner input = null;
				try {
					input = new Scanner(ingredients);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				for (byte i = 0; i < ingredients.length() && input.hasNext(); i++) {
					GameGUI.deckArray.add(new Ingredient(input.next()));
				}
				input.close();
			}
			Collections.shuffle(GameGUI.deckArray);

			String name = this.player.getName();
			String source = "src/" + name + ".png";
			Barista tempBarista = new Barista(name, source);

			this.gameScreen.add(tempBarista.getButton());
			tipsLabel.setText("Tip: " + player.getRevenue());
			this.southPanel.removeAll();

			this.southPanel.add(tempBarista.getButton());
			this.southPanel.add(this.cards);
			this.southPanel.add(this.decks);

			GameGUI.turn = 1;
			this.turnLabel.setText("Turn: " + GameGUI.turn);
			GameGUI.customersLost = 0;
			this.customersLostLabel.setText("Customers Lost: " + GameGUI.customersLost);
			this.player.resetRevenue();
			tipsLabel.setText("Tip: " + player.getRevenue());
			this.line.removeAll();
			this.cards.removeAll();

			ImageIcon trashImage = new ImageIcon("src/trash_empty.jpg");
			Image resizedTrashImage = trashImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
			trashImage.setImage(resizedTrashImage);
			this.trash.setIcon(trashImage);

			try {
				gameStart();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			this.mainScreen.removeAll();
			this.mainScreen.add(this.gameScreen);

			break;
		case "Actually...":
			this.messageToPlayer.setText("Pick a Barista:");

			for (Barista barista : baristas) {
				barista.getButton().setVisible(true);
			}
			this.confirm.setVisible(false);
			this.cancel.setVisible(false);
			break;
		case "deck":
			String deckText = "There are " + GameGUI.deckArray.size() + " cards left in deck.";

			byte coffeeAmount = 0, milkAmount = 0, matchaAmount = 0, chaiAmount = 0, teaAmount = 0;

			for (int i = 0; i < GameGUI.deckArray.size(); i++) {
				String itemName = GameGUI.deckArray.get(i).getName();
				switch (itemName) {
				case "Coffee":
					coffeeAmount++;
					break;
				case "Milk":
					milkAmount++;
					break;
				case "Matcha":
					matchaAmount++;
					break;
				case "Chai":
					chaiAmount++;
					break;
				case "Tea":
					teaAmount++;
					break;
				}
			}

			deckText += String.format("\n\t Coffee: %d\n\t Milk: %d\n\t Chai: %d\n\t Tea: %d\n\t Matcha: %d\n",
					coffeeAmount, milkAmount, chaiAmount, teaAmount, matchaAmount);
			deckText = deckText.substring(0, deckText.length() - 1);
			JOptionPane.showMessageDialog(this, deckText);

			break;
		case "trash":
			JButton[] options;

			String trashText = "Ingredients in trash:";
			if (GameGUI.trashArray.isEmpty()) {
				options = new JButton[] { new JButton("Go Back") };
				trashText = "Trash is empty.";
			} else {
				options = new JButton[] { new JButton("Shuffle"), new JButton("Go Back") };

				coffeeAmount = 0;
				milkAmount = 0;
				matchaAmount = 0;
				chaiAmount = 0;
				teaAmount = 0;

				for (int i = 0; i < GameGUI.trashArray.size(); i++) {
					String itemName = GameGUI.trashArray.get(i).getName();
					switch (itemName) {
					case "Coffee":
						coffeeAmount++;
						break;
					case "Milk":
						milkAmount++;
						break;
					case "Matcha":
						matchaAmount++;
						break;
					case "Chai":
						chaiAmount++;
						break;
					case "Tea":
						teaAmount++;
						break;
					}
				}
				trashText += String.format("\n\t Coffee: %d\n\t Milk: %d\n\t Chai: %d\n\t Tea: %d\n\t Matcha: %d\n",
						coffeeAmount, milkAmount, chaiAmount, teaAmount, matchaAmount);
			}
			trashText = trashText.substring(0, trashText.length() - 1);

			for (JButton option : options) {
				option.addActionListener(this);
				option.setActionCommand(option.getName());
			}

			JOptionPane.showOptionDialog(null, trashText, "Trash", JOptionPane.DEFAULT_OPTION,
					JOptionPane.DEFAULT_OPTION, null, options, options[0]);
			break;
		case "Shuffle":
			restock();
			GameGUI.turn++;
			this.turnLabel.setText("Turn: " + GameGUI.turn);
			try {
				newRandomCustomer();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			draw();

			Window w = SwingUtilities.getWindowAncestor((JButton) e.getSource());
			if (w != null) {
				w.setVisible(false);
			}

			trashImage = new ImageIcon("src/trash_empty.jpg");
			resizedTrashImage = trashImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
			trashImage.setImage(resizedTrashImage);
			this.trash.setIcon(trashImage);
			break;
		case "Go Back":
			w = SwingUtilities.getWindowAncestor((JButton) e.getSource());
			if (w != null) {
				w.setVisible(false);
			}
			break;
		case "Next Turn":
			GameGUI.turn++;
			this.turnLabel.setText("Turn: " + GameGUI.turn);

			ArrayList<Customer> found = new ArrayList<Customer>();
			int count = 0;
			for (Customer customer : GameGUI.lineArray) {
				customer.decreasePatience();

				if (customer.getPatience() == 0) {
					found.add(customer);
					this.line.remove(customer.getButton());
					count++;
				}
				customer.decreaseTip();

				String cardText = customer.name + "\n(" + customer.getQuirk() + ")" + "\n\n\n\n\n\n\n\n\n\n"
						+ customer.orderToString() + "\nPatience: " + customer.getPatience() + "\nTips: "
						+ customer.getTip();
				cardText = cardText.replaceAll("\n", " <br>");
				cardText = cardText.replaceAll("\t", "<&emsp>");
				customer.getButton().setText("<html>" + cardText + "</html>");

			}

			GameGUI.lineArray.removeAll(found);
			if (count > 0) {
				GameGUI.customersLost += count;
				this.customersLostLabel.setText("Customers Lost: " + GameGUI.customersLost);
				String message = count + " customer";
				if (count > 1) {
					message += "s";
				}
				message += " left the line. You will randomly discard " + count + " card";
				if (count > 1) {
					message += "s";
				}
				message += ".";

				JOptionPane.showMessageDialog(this, message);
				for (int i = 0; i < count; i++) {
					int randomIndex = (int) Math.random() * this.player.getHand().size();
					try {

						this.cards.remove(this.player.getHand().get(randomIndex).getButton());
						GameGUI.trashArray.add(this.player.getHand().remove(randomIndex));

					} catch (IndexOutOfBoundsException e1) {
						JOptionPane.showMessageDialog(this, "looks like you have no more cards in hand.");
						this.cards.removeAll();
						break;
					}
				}
			}

			try {
				newRandomCustomer();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				draw();
			} catch (IndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(this, "No more ingredients in deck. You must restock.");
			}

			if (hasLost()) {
				JOptionPane.showMessageDialog(this,
						"Oh no, customers got angry and reported you. You're jobless now, but something will come up.");
				this.mainScreen.removeAll();
				this.mainScreen.add(charSelectScreen);
			}

			break;
		default:
			JButton b = (JButton) e.getSource();
			b.setBorderPainted(!b.isBorderPainted());
			b.setSelected(!b.isSelected());
			String buttonName = b.getName();

			switch (buttonName) {
			case "Coffee":
			case "Milk":
			case "Matcha":
			case "Chai":
			case "Tea":
				GameGUI.tempChoiceArray.add(b.getName());
				break;
			default:
				String[] order = b.getActionCommand().split(",");
				boolean canMakeOrder = true;
				for (int i = 0; i < order.length - 2; i++) {
					if (GameGUI.tempChoiceArray.contains(order[i])) {
						GameGUI.tempChoiceArray.remove(order[i]);
					} else {
						Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red);
						b.setBorder(border);
						// create animation here for the red to go away after a few seconds
						canMakeOrder = false;
						break;
					}
				}

				if (canMakeOrder) {
					int reward = Integer.parseInt(order[order.length - 1]);
					if (reward <= 0) {
						reward = 1;
					}

					int revenue = Integer.parseInt(order[order.length - 2]);
					this.player.increaseRevenue(revenue);
					this.tipsLabel.setText("Tip: " + this.player.getRevenue());
					b.setText("served");

					ArrayList<Customer> foundCustomer = new ArrayList<Customer>();
					for (Customer customer : GameGUI.lineArray) {
						if (customer.getButton().getText().equals("served")) {
							foundCustomer.add(customer);
						}
					}
					GameGUI.lineArray.removeAll(foundCustomer);
					this.line.remove(b);

					ArrayList<Ingredient> foundCard = new ArrayList<Ingredient>();
					for (Ingredient card : this.player.getHand()) {
						if (card.getButton().isSelected()) {
							foundCard.add(card);
							this.cards.remove(card.getButton());
						}
					}
					this.player.getHand().removeAll(foundCard);
					GameGUI.trashArray.addAll(foundCard);
					try {
						draw(reward);
					} catch (IndexOutOfBoundsException e1) {
						JOptionPane.showMessageDialog(this, "No more ingredients in deck. You must restock.");
					}
					trashImage = new ImageIcon("src/trash_full.jpg");
					resizedTrashImage = trashImage.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
					trashImage.setImage(resizedTrashImage);
					this.trash.setIcon(trashImage);
				}

				for (Ingredient card : this.player.getHand()) {
					card.getButton().setSelected(false);
					card.getButton().setBorderPainted(false);
				}
			}
			if (hasWon()) {
				JOptionPane.showMessageDialog(this,
						"Yeah!!! You beat the game!/nCongratulations!! You're the best barista there is!");
				this.mainScreen.removeAll();
				this.mainScreen.add(charSelectScreen);
			}
			break;
		}
		this.mainScreen.revalidate();
		this.mainScreen.repaint();
	}

	/**
	 * This method starts the game by creating 3 random customers and drawing the
	 * player's hand
	 * 
	 * @throws IOException
	 */
	public void gameStart() throws IOException {
		for (int i = 0; i < 3; i++) {
			newRandomCustomer();
		}
		draw(4);
		if (this.player.getName().equals("Betmann")) {
			draw();
		}
	}

	/**
	 * This method determines if the player has earned enough tips to win the game
	 * 
	 * @return true if the player has earned $30.00+ in tips; otherwise, false
	 */
	public boolean hasWon() {
		if (this.player.getRevenue() >= 30) {
			return true;
		}
		return false;
	}

	/**
	 * This method determines if the customer has lost the game by losing too many
	 * customers
	 * 
	 * @return true if the player has lost 5+ customers; otherwise, false
	 */
	public boolean hasLost() {
		if (GameGUI.customersLost >= 5) {
			return true;
		}
		return false;
	}

	/**
	 * This method generates a new random customer to join the line
	 * 
	 * @throws IOException
	 */
	public void newRandomCustomer() throws IOException {
		Customer c = new Customer();
		String name = this.player.getName();

		switch (name) {
		case "Jessica":
			if (Math.random() >= 0.5) {
				c.setQuirk("Kind");
				c.increaseReward();
			}
			break;
		case "Subin":
			c.increasePatience();
			break;
		case "Nathan":
			c.increaseReward();
			break;
		}
		c.getButton().addActionListener(this);
		c.getButton().setBorderPainted(false);
		String command = "";
		for (Ingredient ingredient : c.getOrder().getOrderArray()) {
			command += ingredient.getName().trim() + ",";
		}
		command += c.getTip() + ",";
		command += c.getReward();
		c.getButton().setActionCommand(command);

		// c.getButton().addMouseListener(this);

		GameGUI.lineArray.add(c);
		this.line.add(c.getButton());
	}

	/**
	 * re-shuffles the game deck once the draw pile is out of cards
	 */
	public static void restock() {
		GameGUI.deckArray.addAll(GameGUI.trashArray);
		GameGUI.trashArray.clear();
		Collections.shuffle(GameGUI.deckArray);
	}

	/*
	 * default draw method calls draw(1)
	 */
	public void draw() {
		draw(1);
	}

	/**
	 * Draws a new ingredient card
	 * 
	 * @param amount is the amount of cards to draw
	 */
	public void draw(int amount) {
		for (int i = 0; i < amount; i++) {
			this.player.draw(GameGUI.deckArray);
			Ingredient ingredient = this.player.getLast();
			ingredient.getButton().addActionListener(this);
			Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GREEN, Color.GREEN);
			ingredient.getButton().setBorder(border);
			ingredient.getButton().setBorderPainted(false);
			// ingredient.getButton().addMouseListener(this);
			this.cards.add(this.player.getLast().getButton());
		}
	}

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		GameGUI game = new GameGUI("Where's My Coffee?");
	}
}