/**
 * 
 * @author Lamha Goel 2015050
 * 
*/

import java.util.ArrayList;

public class Person {
	private String username;
	private String pass;
	private String display_name;
	private int no_of_friends;
	private ArrayList<String> friends;
	private int no_of_pending_requests;
	private ArrayList<String> requests;
	private String status;
	/**********************
	 * Override equals to compare username
	 * Override toString to return the required csv string
	 ***********************/
	public Person(String x)
	{
		//Make this
		String[] temp=x.split(",");
		username=temp[0];
		pass=temp[1];
		display_name=temp[2];
		no_of_friends=Integer.parseInt(temp[3]);
		int i,j=4;
		friends = new ArrayList<String>();
		for(i=0;i<no_of_friends;i++,j++)
		{
			friends.add(temp[j]);
		}
		no_of_pending_requests=Integer.parseInt(temp[j]);
		requests = new ArrayList<String>();
		for(i=0,j++;i<no_of_pending_requests;i++,j++)
		{
			requests.add(temp[j]);
		}
		status=temp[j];
		
	}
	public String getStatus()
	{
		return status;
	}
	public String getDisplayName()
	{
		return display_name;
	}
	public void setStatus(String x)
	{
		status=x;
	}
	public void incrementrequests()
	{
		no_of_pending_requests++;
	}
	public void decrementrequests()
	{
		no_of_pending_requests--;
	}
	public void incrementfriends()
	{
		no_of_friends++;
	}
	public String getPassword()
	{
		return pass;
	}
	public ArrayList<String> getFriends()
	{
		return friends;
	}
	public String getUsername()
	{
		return username;
	}
	public ArrayList<String> getRequests()
	{
		return requests;
	}
	@Override
	public String toString()
	{
		String temp;
		temp = username + "," + pass + "," + display_name + "," + no_of_friends + ",";
		for(String x : friends)
		{
			temp = temp + x + ","; 
		}
		temp += no_of_pending_requests + ",";
		for(String x : requests)
		{
			temp += x + ",";
		}
		temp += status;
		return temp;
	}
	@Override
	public boolean equals(Object other)
	{
		Person x = (Person)other;
		if(this.username.equals(x.getUsername()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
