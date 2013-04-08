
public class TestMenuBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MenuBuilder menu = new MenuBuilder();
		String[] options = {"1", "2", "3", "4", "5", "0"};
		String[] descriptions = {"option 1", "option 2", "option 3", "option 4", "option 5", "Exit to previous menu"};
		String title = "This is a sample title";
		menu.getOptions(options);
		menu.getDesriptions(descriptions);
		menu.generate(title);
	}

}
