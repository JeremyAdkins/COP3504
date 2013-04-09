package project.tui;


import java.math.BigDecimal;
import java.util.Scanner;

import project.model.OverdraftException;


public class Console {

	/**
	 * @param args
	 * @throws OverdraftException 
	 */
	public static void main(String[] args) throws OverdraftException {
		printTellerSavingsMenu();
	}
	
	private static Scanner input = new Scanner(System.in);
	
	public static void printTellerSavingsMenu() throws OverdraftException {

		display: while(true) {

			System.out.println(
							"--------------------------------------------\n" +
							" Teller Savings Menu\n" +
							"--------------------------------------------\n" +
					        " Select an option: \n" +
							"  1) Deposit\n" +
							"  2) Withdraw\n" +
							"  3) Configure automatic transactions\n" +
							"  4) Waive service charge for this set of transactions\n" +
							"  0) Exit to previous menu\n\n" +
							"--------------------------------------------\n"
					);
//			Account acc = new SavingsAccount();

			System.out.println("Enter number for your selection: ");
			int selection = input.nextInt();
			input.nextLine();

			switch (selection) {
			case 1:
				System.out.println("Enter amount of deposit:");
				BigDecimal dep = input.nextBigDecimal();
//				acc.deposit(dep);
				break;
			case 2:
				System.out.println("Enter amount of withdrawal:");
				BigDecimal wd = input.nextBigDecimal();
//				acc.withdraw(wd);
				break;
			case 3:
				displayAutoMenu: while(true) {
					System.out.println(
							"Do you want to setup auto deposits or withdrawals?\n" +
									" 1) Automatic deposits\n" +
									" 2) Automatic withdrawals\n" +
									" 0) Exit to previous menu\n"
							);
					int auto = input.nextInt();
					switch (auto) {
					case 1:
						System.out.println("Enter amount of the monthly automatic deposit\n");
						BigDecimal ad = input.nextBigDecimal();
//						acc.addRepeatingPayment(new Transaction(Transaction.Type.DEPOSIT,ad));
						break;
					case 2:
						System.out.println("Enter amount of the monthly automatic withdrawal\n");
						BigDecimal aw = input.nextBigDecimal();
//						acc.addRepeatingPayment(new Transaction(Transaction.Type.WITHDRAWAL,aw));
						break;					
					case 0:
						break displayAutoMenu;
					default:
						System.out.println("Invalid selection.\n");
						break;
					} // End switch (auto)
				} // End displayAutoMenu
				
			case 4:
				// Waive service charge for this set of transactions
				break;
			case 0:
				break display;
			
			default:
				System.out.println("Invalid selection.\n");
				break;
			} // End switch (selection)


		} // End display
	} // End printTellerSavingsMenu()

} // End console class