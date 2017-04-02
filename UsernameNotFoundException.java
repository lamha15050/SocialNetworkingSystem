/**
 * 
 * @author Lamha Goel 2015050
 * 
*/

public class UsernameNotFoundException extends Exception{
	public UsernameNotFoundException()
	{
		super("This username doesn't exist in the database");
	}
}
