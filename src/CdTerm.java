
public enum CdTerm {
SIX_MONTHS, ONE_YEAR, TWO_YEARS, THREE_YEARS, FOUR_YEARS, FIVE_YEARS; 

public Time getLength()
{ 
	//TODO How are we going to determine how many days are in six months? 
	if (this.equals(SIX_MONTHS))
	{
		return new Time(30*6,0,0,0); 
	}
	else if(this.equals(ONE_YEAR))
	{
		return new Time(365,0,0,0); 
	}
	else if(this.equals(TWO_YEARS))
	{
		return new Time(2*365,0,0,0); 
	}
	else if(this.equals(THREE_YEARS))
	{
		return new Time(3*365,0,0,0); 
	}
	else if(this.equals(FOUR_YEARS))
	{
		return new Time(4*365,0,0,0); 
	}
	return null; 
}
}
