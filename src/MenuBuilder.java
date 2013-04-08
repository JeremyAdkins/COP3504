public class MenuBuilder {

    //variables for the options side of the menu
    int optionsLength;
    int optionsLargestLength = 0;
    String[] options;

    //variables for the descriptions side of the menu
    String[] descriptions;
    int descriptionsLength;
    int descriptionsLargestLength = 0;

    /*
     * Gets the options and other variables
     * length of array
     * and the last length
     */
    public void getOptions(String[] options) {
        this.options = options;
        optionsLength = options.length - 1;

        for(int i = 0; i < options.length; i++) {
            if (optionsLargestLength < options[i].length()) {
                optionsLargestLength = options[i].length();
            }
        }
    }

    /*
     * Gets the descriptions and other variables
     * length of array
     * and the last length
     */
    public void getDesriptions(String[] descriptions) {

        this.descriptions = descriptions;
        descriptionsLength = descriptions.length - 1;

        for(int i = 0; i < descriptions.length; i++) {
            if (descriptionsLargestLength < descriptions[i].length()) {
                descriptionsLargestLength = descriptions[i].length();
            }
        }
    }

    public void generate(String title) {
    	int titleCellLength = optionsLargestLength + descriptionsLargestLength + 6;
    	if (title.length() + 2 > optionsLargestLength + descriptionsLargestLength + 6) {
    		descriptionsLargestLength += (title.length() + 2 - (optionsLargestLength + descriptionsLargestLength + 6));
    		titleCellLength = title.length() + 2;
    	}
        /*
         * Generates the top line based of the length of the options
         */
        System.out.print("|");

        for (int i = 0; i < optionsLargestLength +3; i++) {
            System.out.print("-");
        }
        /*
         * Finished generation of the top line based of the length of the descriptions
         */
        for (int i = 0; i < descriptionsLargestLength +3; i++) {
            System.out.print("-");
        }

        System.out.println("|");

        /*
         * Generates the ammount of space needed after the options label
         */
        System.out.print("|");
        for (int i = 0; i < ((titleCellLength-title.length())/2); i++) {
            System.out.print(" ");
        }
        System.out.print(title);
        /*
         * Generates the ammount of space needed after the description label
         */
        for (int i = 0; i < ((titleCellLength-title.length())/2); i++) {
            System.out.print(" ");
        }
        if (((titleCellLength-title.length())%2) > 0) System.out.print(" ");

        System.out.println("|");
        System.out.print("|");

        /*
         * Generates a break line bewteen the labels and actual content
         */
        for (int i = 0; i < titleCellLength; i++) {
            System.out.print("-");
        }

        System.out.println("|");

        /*
         * Generates the rows of options and descriptions row by row
         */
        //options content
        for (int i = 0; i < optionsLength +1; i++) {
            System.out.print("| " + options[i]);

            for(int k = 0; k < optionsLargestLength-options[i].length() +2; k++) {
                System.out.print(" ");
            }
            //description content
            System.out.print("| " + descriptions[i]);

            for(int k = 0; k < descriptionsLargestLength-descriptions[i].length() +1; k++) {
                System.out.print(" ");
            }

            System.out.println("|");
        }
        System.out.print("|");
        /*
         * Generates a bottom line to close the menu
         */

        for (int i = 0; i < titleCellLength; i++) {
            System.out.print("-");
        }
        
        /*
         * Finishes generation of a bottom line to close the menu
         */
//        for (int k = 0; k < descriptionsLargestLength +3; k++) {
//            System.out.print("-");
//        }

        System.out.println("|");
        
    }

}