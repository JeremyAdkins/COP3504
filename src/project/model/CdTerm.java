package project.model;


public enum CdTerm {
    SIX_MONTHS(6), ONE_YEAR(12), TWO_YEARS(24), THREE_YEARS(36), FOUR_YEARS(48), FIVE_YEARS(60);

    private final int length;

    private CdTerm(int length) {
        this.length = length;
    }

    public int getLength()
    {
	    return length;
    }
}
