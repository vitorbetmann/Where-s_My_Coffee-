import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
/**
 * A class for handling the customer's components
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */
public class Customer extends Card {
	/**
	 * customer's order
	 */
	private Order order;
	/**
	 * customer's quirk
	 */
	private String quirk;
	/**
	 * amount of customer's tip
	 */
	private byte tip;
	/**
	 * customer's level of patience
	 */
	private byte patience;
	/**
	 * reward amount
	 */
	private byte reward;

	/**
	 * 
	 * @throws IOException
	 */
	public Customer() throws IOException {
		super("", "");
		this.order = getRandomOrder();
		this.tip = 3;
		this.patience = 3;
		this.reward = 1;

		String filename = "src/clientsNames.txt";
		File clientsNames = new File(filename);
		long randomLine = (long) (Math.random() * clientsNames.length());
		RandomAccessFile namesRAF = new RandomAccessFile(filename, "r");
		namesRAF.seek(randomLine);
		namesRAF.readLine();
		this.setName(namesRAF.readLine());

		if (this.getName() == null || this.getName().equals("")) {
			namesRAF.seek(0);
			this.setName(namesRAF.readLine());
		}
		namesRAF.close();

		String[] quirksList = { "Patient", "Hasty", "Kind", "Rude", "Tipper", "Cheap" };
		this.quirk = quirksList[(byte) (Math.random() * 6)];

		String secondQuirk = "";
		if (Math.random() >= 0.75) {
			while (secondQuirk.equals(this.quirk) || secondQuirk.equals("")) {
				secondQuirk = quirksList[(byte) (Math.random() * 6)];
			}
			secondQuirk = ", " + secondQuirk;
		}

		this.quirk += secondQuirk;

		if (this.quirk.toLowerCase().contains("tipper")) {
			increaseTip();
		} else if (this.quirk.toLowerCase().contains("cheap")) {
			decreaseTip();
		}
		if (this.quirk.toLowerCase().contains("patient")) {
			increasePatience();
		} else if (this.quirk.toLowerCase().contains("hasty")) {
			decreasePatience();
		}
		if (this.quirk.toLowerCase().contains("kind")) {
			increaseReward();
		} else if (this.quirk.toLowerCase().contains("rude")) {
			decreaseReward();
		}

		ImageIcon image = new ImageIcon("src/customer.jpg");
		Image resizedImage = image.getImage().getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, 0);
		image.setImage(resizedImage);
		this.getButton().setIcon(image);

		String cardText = this.name + "\n(" + this.getQuirk() + ")" + "\n\n\n\n\n\n\n\n\n\n" + this.orderToString()
				+ "\nPatience: " + this.getPatience() + "\nTips: " + this.getTip();
		cardText = cardText.replaceAll("\n", " <br>");
		cardText = cardText.replaceAll("\t", "<&emsp>");
		this.getButton().setText("<html>" + cardText + "</html>");
	}
	
	/**
	 * 
	 * @param newQuirk is the new quirk of the customer
	 * @return quirk as a string
	 */
	public String setQuirk(String newQuirk) {
		String[] quirkArray = this.getQuirk().split(", ");
		quirkArray[0] = newQuirk;
		String result = "";
		for (String quirk : quirkArray) {
			result += quirk + ", ";
		}
		result = result.substring(0, result.length() - 2);
		return result;
	}

	public String getQuirk() {
		return this.quirk;
	}

	public int getTip() {
		return this.tip;
	}

	public void increaseTip() {
		this.tip += 1;
	}

	public void decreaseTip() {
		if (this.tip > 0) {
			this.tip -= 1;
		}
	}

	public void increasePatience() {
		this.patience += 1;
	}

	public void decreasePatience() {
		if (this.patience > 0) {
			this.patience -= 1;
		}
	}

	public byte getPatience() {
		return this.patience;
	}

	public void increaseReward() {
		this.reward += 1;
	}

	public void decreaseReward() {
		this.reward -= 1;
	}

	public byte getReward() {
		return this.reward;
	}
	/**
	 * method to get a randomized order
	 * @return Order
	 * @throws IOException
	 */
	public Order getRandomOrder() throws IOException {
		String filename = "src/gameOrders.txt";
		File ordersFile = new File(filename);
		long randomLine = (long) (Math.random() * ordersFile.length());
		RandomAccessFile myRAF = new RandomAccessFile(filename, "r");
		myRAF.seek(randomLine);
		myRAF.readLine();
		String orderString = myRAF.readLine();

		if (orderString == null || orderString.equals("")) {
			myRAF.seek(0);
			orderString = myRAF.readLine();
		}

		myRAF.close();

		String[] orderArray = orderString.split(",");
		
		if (orderArray.length > 2) {
			this.increaseReward();
		}

		return new Order(orderArray);
	}
	
	public Order getOrder() {
		return this.order;
	}

	/**
	 * 
	 * @return order as a string
	 */
	public String orderToString() {
		String result = this.order.getName();

		for (Ingredient ingredient : this.order.getOrderArray()) {
			result += "\n\t" + ingredient.getName();
		}

		return result;
	}
}