/**
 * 
 * @author Lamha Goel 2015050
 * 
*/

public class UsernameAlreadyInUseException extends Exception{
	public UsernameAlreadyInUseException()
	{
		super("Sorry, this username is already in use.");
	}
}
