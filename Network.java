/**
 * 
 * @author Lamha Goel 2015050
 * 
*/

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Stack;

public class Network {
	private ArrayList<Person> registered_users= new ArrayList<Person>();	//network of all users in database
	private Person current_user = null;	//user currently logged in
	private final String database = "input.txt";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/****************************************
		 * 
		 * Also required : Add custom exceptions and shortest route
		 * Change friends, pending request numbers wherever required
		 * 
		 *****************************************/
		Network MyNetwork=new Network();
		MyNetwork.read_database();
		Scanner scan = new Scanner(System.in);
		
		int choice;
		while(true)
		{
			MyNetwork.printhomemenu();
			choice = scan.nextInt();
			switch(choice)
			{
				case 1: MyNetwork.signup();
						break;
				case 2: MyNetwork.login();
						break;
				case 3: scan.close();
						System.exit(0);
						break;
				default:System.out.println("Invalid choice, input again");
						System.out.println();
						break;
			}
		}
	}
	private void signup()
	{
		Scanner x = new Scanner(System.in);
		String[] details = new String[3];
		System.out.print("Enter username: ");
		details[0]=x.nextLine();
		System.out.print("Enter display name: ");
		details[1]=x.nextLine();
		System.out.print("Enter password: ");
		details[2]=x.nextLine();
		if(details[0].contains(",") || details[1].contains(",") || details[2].contains(","))
		{
			System.out.println("Invalid input, none of the entries can have a comma(,)");
		}
		else
		{
			Person temp = new Person(details[0]+","+details[2]+","+details[1]+",0,0, ");
			if(registered_users.contains(temp))
			{
				//System.out.println("Username already in use.");
				try
				{
					throw new UsernameAlreadyInUseException();
				}
				catch(UsernameAlreadyInUseException e)
				{
					System.out.println(e);
				}
			}
			else
			{
				System.out.println();
				System.out.println("Registration is successful. User " + details[0] + " created.");
				System.out.println();
				registered_users.add(temp);
				writetofile();
			}
		}
	}
	private void login()
	{
		Scanner x = new Scanner(System.in);
		int index;
		String[] details = new String[2];
		System.out.print("Please enter your username: ");
		details[0]=x.nextLine();
		System.out.print("Please enter your password: ");
		details[1]=x.nextLine();
		if(details[0].contains(",") || details[1].contains(","))
		{
			System.out.println("Wrong username or password.");
			System.out.println();
			return;
		}
		Person temp = new Person(details[0]+","+details[1]+",abc"+",0,0, ");
		if(((index=registered_users.indexOf(temp))!=-1) && registered_users.get(index).getPassword().equals(details[1]))
		{
			System.out.println();
			System.out.println(details[0] + " logged in now.");
			current_user=registered_users.get(index);
			loggedinmenu();
			//returns here after logging out
		}
		else
		{
			System.out.println("Wrong username or password.");
			System.out.println();
		}
	}
	private void loggedinmenu()
	{
		Scanner x = new Scanner(System.in);
		int choice;
		while(true)
		{
			System.out.println();
			System.out.println(current_user.getStatus());
			System.out.println();
			System.out.println("\t1. List friends");
			System.out.println("\t2. Search");
			System.out.println("\t3. Update status");
			System.out.println("\t4. Pending request");
			System.out.println("\t5. logout");
			choice = x.nextInt();
			switch(choice)
			{
			case 1:	listfriends();
					break;
			case 2: //code for search 
					search();
					break;
			case 3: updatestatus();
					break;
			case 4: //code for pending requests
					pending_requests();
					break;
			case 5: logout();
					return;
			default: System.out.println("Invalid choice, enter again");
					System.out.println();
						break;
			}
		}
	}
	private void shortestpath(Person other)
	{
		//BFS Approach
		System.out.print("Shortest route: ");
		Person temp=current_user;
		int n=registered_users.size(),x;
		boolean visited[]=new boolean[n];
		int parent[]=new int[n];
		for(int i=0;i<n;i++)
		{
			visited[i]=false;
			parent[i]=-1;
		}
		LinkedList<Person> queue = new LinkedList<Person>();
		visited[registered_users.indexOf(temp)]=true;
		queue.add(temp);
		while(queue.size()!=0)
		{
			temp=queue.poll();
			ArrayList<String> friends=temp.getFriends();
			for(String friend : friends)
			{
				Person f = new Person(friend+", "+", "+",0,0, ");
				x=registered_users.indexOf(f);
				f=registered_users.get(x);
				if(!visited[x])
				{
					parent[x]=registered_users.indexOf(temp);
					visited[x]=true;
					queue.add(f);
					if(f.equals(other))
					{
						//Print shortest path
						Stack<Integer> s = new Stack<Integer>();
						while(x!=-1)
						{
							s.push(x);
							x=parent[x];
						}
						while(s.empty()==false)
						{
							x=s.pop();
							if(s.empty())
							{
								System.out.println(registered_users.get(x).getUsername());
							}
							else
							{
								System.out.print(registered_users.get(x).getUsername() + " -> ");
							}
						}
						return;
					}
				}
			}
		}
		System.out.println("No route possible");
	}
	private void logout()
	{
		System.out.println();
		System.out.println(current_user.getUsername()+ " logged out successfully.");
		System.out.println();
		writetofile();
		current_user = null;
	}
	private void writetofile()
	{
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(database);
			for(Person temp : registered_users)
			{
				out.println(temp);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Exception caught: " + e);
		}
		finally
		{
			if(out!=null)
			{
				out.close();
			}
		}
	}
	private void read_database()
	{
		System.out.println("Reading database file...");
		FileInputStream in = null;
		InputStreamReader input = null;
		BufferedReader reader= null;
		try
		{
			in = new FileInputStream(database);
			input = new InputStreamReader(in);
			reader = new BufferedReader(input);
			Person temp=null;
			String userinfo;
			userinfo=reader.readLine();
			while(userinfo!=null)
			{
				temp=new Person(userinfo);
				registered_users.add(temp);
				userinfo=reader.readLine();
			}
			System.out.println("Network is ready.");
			System.out.println();
		}
		catch(IOException e)
		{
			System.out.println("Exception caught" + e);
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(IOException e)
			{
				System.out.println("Exception caught" + e);
			}
			try
			{
				input.close();
			}
			catch(IOException e)
			{
				System.out.println("Exception caught" + e);
			}
			try
			{
				in.close();
			}
			catch(IOException e)
			{
				System.out.println("Exception caught" + e);
			}
		}
	}
	private void listfriends()
	{
		ArrayList<String> f = current_user.getFriends();
		String x = "Your friends are: ";
		int i,temp=f.size()-1;
		for(i=0;i<temp;i++)
		{
			x=x + f.get(i)+ ", ";
		}
		if(i<f.size())
		{
			x=x+f.get(i);
		}
		else if(i==0)
		{
			x+= " None";
		}
		System.out.println(x);
	}
	private void updatestatus()
	{
		Scanner x = new Scanner(System.in);
		System.out.print("Enter status: ");
		String stat= x.nextLine();
		//stat = x.nextLine();
		current_user.setStatus(stat);
		System.out.println();
		System.out.println("Status updated!!");
	}
	private void printhomemenu()
	{
		System.out.println("\t1. Sign Up");
		System.out.println("\t2. Login");
		System.out.println("\t3. Exit");
	}
	private void search()
	{
		Scanner x = new Scanner(System.in);
		System.out.print("Enter name: ");
		String name=x.nextLine();
		int index;
		if(name.contains(","))
		{
			try
			{
				throw new UsernameNotFoundException();
			}
			catch(UsernameNotFoundException e)
			{
				System.out.println(e);
				return;
			}
		}
		Person temp= new Person(name+", , ,0,0, ");
		if((index=registered_users.indexOf(temp))!=-1)
		{
			temp=registered_users.get(index);
			ArrayList<String> friends = current_user.getFriends();
			ArrayList<String> temp_friends = temp.getFriends();
			if(friends.contains(temp.getUsername()))
			{
				//Friends
				char ch='a';
				while(ch!='b')
				{
					System.out.println(name + " is a friend.");
					System.out.println();
					System.out.println("Display name: " + temp.getDisplayName());
					System.out.println("Status: " + temp.getStatus());
					System.out.print("Friends: ");
					int i,n=temp_friends.size()-1;
					String f="";
					for(i=0;i<n;i++)
					{
						f=f +temp_friends.get(i) + ", ";
					}
					f=f+temp_friends.get(i);
					System.out.println(f);
					mutualfriends(friends,temp_friends);
					System.out.println("\tb. Back");
					ch=x.nextLine().charAt(0);
					if(ch!='b')
					{
						System.out.println("Invalid input");
						System.out.println();
					}
				}
			}
			else
			{
				//Not friends
				ArrayList<String> requests = current_user.getRequests();
				if(requests.contains(temp.getUsername()))
				{
					//User has a request from *name*
					System.out.println("You have a pending request from " + name);
					shortestpath(temp);
					temp=registered_users.get(index);
					System.out.println(temp.getDisplayName());
					System.out.println("1. Accept");
					System.out.println("2. Reject");
					System.out.println("b.Back");
					char choice = x.nextLine().charAt(0);
					if(choice=='1')
					{
						requests.remove(temp.getUsername());
						//ArrayList<String> friends = current_user.getFriends();
						friends.add(temp.getUsername());
						current_user.decrementrequests();
						current_user.incrementfriends();
						System.out.println("You are now friends with " + temp.getUsername());
					}
					else if(choice=='2')
					{
						requests.remove(temp.getUsername());
						current_user.decrementrequests();
						System.out.println("Rejected");
					}
					else if(choice=='b')
					{
						return;
					}
					else
					{
						System.out.println("Invalid input, returning to menu");
					}
				}
				else 
				{
					ArrayList<String> temp_requests = temp.getRequests();
					if(temp_requests.contains(current_user.getUsername()))
					{
						//user has sent them a request
						System.out.println("You have sent " + name + " a request.");
						shortestpath(temp);
						System.out.println("Request pending");
						System.out.println("\t1.Cancel Request");
						System.out.println("\tb.Back");
						String ch;
						ch=x.next();
						switch(ch.charAt(0))
						{
						case '1': temp_requests.remove(current_user.getUsername());
								  temp.decrementrequests();
								  System.out.println("Request cancelled");
							break;
						case 'b': //No code required here
							break;
						default: System.out.println("Invalid input. Returning to menu.");
							break;
						}
					}
					else
					{
						//No relationship of any sorts
					
						/*************************
						 * Mutual friends, shortest route, send/cancel request and back
						 *************************/
						char ch='a';
						/**********Enter Code for shortest route****************/
						while(ch!='b')
						{
							System.out.println(name + " is not a friend.");
							mutualfriends(friends,temp_friends);
							shortestpath(temp);
							if(temp_requests.contains(current_user.getUsername()))
							{
								System.out.println("\t1.Cancel Request");
							}
							else
							{
								System.out.println("\t1.Send Request");
							}
							System.out.println("\tb.Back");
							ch=x.nextLine().charAt(0);
							switch(ch)
							{
							case '1': 	if(temp_requests.contains(current_user.getUsername()))
										{
											temp_requests.remove(current_user.getUsername());
											temp.decrementrequests();
											System.out.println("Request cancelled");
											System.out.println();
										}
										else
										{
											temp_requests.add(current_user.getUsername());
											temp.incrementrequests();
											System.out.println("Request sent");
											System.out.println();
										}
										
								break;
							case 'b': 
								break;
							default: System.out.println("Invalid input");
							System.out.println();
							}
						}
						
					}
				}
				
			}
		}
		else
		{
			try
			{
				throw new UsernameNotFoundException();
			}
			catch(UsernameNotFoundException e)
			{
				System.out.println(e);
			}
		}
	}
	private void mutualfriends(ArrayList<String> friends, ArrayList<String> temp_friends)
	{
		ArrayList<String> mutual=new ArrayList<String>(friends);
		mutual.retainAll(temp_friends);
		System.out.print("Mutual friends: ");
		int size=mutual.size();
		if(size==0)
		{
			System.out.println("No mutual friends");
		}
		else
		{
			int j;
			size--;
			for(j=0;j<size;j++)
			{
				System.out.print(mutual.get(j) + ", ");
			}
			System.out.println(mutual.get(j));
		}
	}
	private void pending_requests()
	{
		ArrayList<String> requests = current_user.getRequests();
		int n,i;
		while(true)
		{
			n=requests.size();
			for(i=0;i<n;i++)
			{
				System.out.println(i+1+". " + requests.get(i));
			}
			if(n==0)
			{
				System.out.println("No new friend requests");
			}
			System.out.println("b.Back");
			Scanner x = new Scanner(System.in);
			String ch;
			ch=x.nextLine();
			if(ch.charAt(0)=='b')
			{
				return;
			}
			else if((ch.charAt(0)<'0' || ch.charAt(0)>'9') && ch.charAt(0)!='-' && ch.charAt(0)!='+')
			{
				System.out.println("Invalid input ");
				System.out.println();
			}
			else
			{
				int choice;
				choice = Integer.parseInt(ch);
				if(choice<1 || choice >n)
				{
					System.out.println("Invalid input");
					System.out.println();
				}
				else
				{
					Person temp = new Person(requests.get(choice-1)+", , ,0,0, ");
					int index;
					index=registered_users.indexOf(temp);
					if(index!=-1)
					{
						temp=registered_users.get(index);
						shortestpath(temp);
						System.out.println(temp.getDisplayName());
						System.out.println("1. Accept");
						System.out.println("2. Reject");
						System.out.println("b.Back");
						char c = x.nextLine().charAt(0);
						if(c=='1')
						{
							requests.remove(temp.getUsername());
							ArrayList<String> friends = current_user.getFriends();
							friends.add(temp.getUsername());
							current_user.decrementrequests();
							current_user.incrementfriends();
							System.out.println("You are now friends with " + temp.getUsername());
						}
						else if(c=='2')
						{
							requests.remove(temp.getUsername());
							current_user.decrementrequests();
							System.out.println("Friend request by " + temp.getUsername() + " deleted.");
						}
						else if(c =='b')
						{
							continue;
						}
						else
						{
							System.out.println("Invalid input, returning to menu");
							System.out.println();
						}
					}
				}
			}
		}
	}
}
