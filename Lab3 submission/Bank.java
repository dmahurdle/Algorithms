import edu.neumont.util.Client;
import edu.neumont.util.QueueableService;


public class Bank implements QueueableService {
	
	static class LineClient{
		Client client;
		int waitTime;
		public LineClient(Client client, int waitTime)
		{
			this.client = client;
			this.waitTime = waitTime;
		}
	}
	Client[] clientsAtTellers;
	LinkedList<LineClient> clientsInLine = new LinkedList<LineClient>();
	
	public Bank(int numberOfTellers) {
		clientsAtTellers = new Client[numberOfTellers];
	}
	
	public void advanceMinute() {
		//Handle clients being served first.
		for(int i = 0 ; i < clientsAtTellers.length; i ++)
		{
			if (clientsAtTellers[i] != null)
			{
				clientsAtTellers[i].servedMinute();
				if (clientsAtTellers[i].getExpectedServiceTime() == 0)
				{
					//Remove him.
					clientsAtTellers[i] = null;
				}
			}
		}
		//Advance all client's service times by 1.
		for(LineClient c : clientsInLine)
		{
			c.waitTime --;
		}
		
		//If any teller is open, put top client in teller slot, and remove him from clientsInLine.
		int nextOpenTeller = firstOpenTellerIndex();
		while (nextOpenTeller != -1 && clientsInLine.size > 0)
		{
			clientsAtTellers[nextOpenTeller] = clientsInLine.poll().client;
			nextOpenTeller = firstOpenTellerIndex();
		}
	}
	
	public int firstOpenTellerIndex()
	{
		int index = -1;
		for(int i = 0; i < clientsAtTellers.length; i ++)
		{
			if (clientsAtTellers[i] == null)
			{
				index = i;
			}
		}
		return index;
	}
	
	public double getClientWaitTime(Client client) {
		double waitTime = -1;
		for(LineClient lc : clientsInLine)
		{
			if (lc.client == client)
			{
				waitTime = lc.waitTime;
				break;
			}
		}
		return waitTime;
	}
	
	public double getAverageClientWaitTime() {
		int total = 0;
		for(LineClient c : clientsInLine)
		{
			total += c.waitTime;
		}
		return (double)total / clientsInLine.size;
	}
	
	public int getNextServiceTime()
	{
		//returns the amount of time a new client will wait before being served.
		int[] tellerTimes = new int[clientsAtTellers.length];
		for(int i = 0; i < tellerTimes.length; i ++)
		{
			if (clientsAtTellers[i] != null)
			{
				tellerTimes[i] = clientsAtTellers[i].getExpectedServiceTime();
			}
		}
		for(LineClient lc : clientsInLine)
		{
			int tellerAvailableSoonest = findLowestValueIndex(tellerTimes);
			tellerTimes[tellerAvailableSoonest] += lc.client.getExpectedServiceTime();
		}
		int tellerForThisClient = findLowestValueIndex(tellerTimes);
		return tellerTimes[tellerForThisClient];
	}
	
	public int findLowestValueIndex(int[] arr)
	{
		if (arr.length == 1)
		{
			return 0;
		}
		else
		{
			int minIndex = 0;
			for(int i = 1; i < arr.length; i ++)
			{
				if (arr[i] < arr[minIndex])
				{
					minIndex = i;
				}
			}
			return minIndex;
		}
	}
	
	public void printState()
	{
		//Prints state of bank line and tellers.
		//Line first.
		System.out.print("Line : ");
		for(LineClient lc : clientsInLine)
		{
			System.out.print(lc.waitTime + "/" + lc.client.getExpectedServiceTime() + "  ");
		}
		System.out.println();
		//Then the tellers.
		
		System.out.print("Tellers : ");
		for(int i = clientsAtTellers.length - 1 ;i >= 0; i --)
		{
			System.out.print((clientsAtTellers[i] == null ? "--" : clientsAtTellers[i].getExpectedServiceTime()) + "    ");
		}
	}
	
	@Override
	public boolean addClient(Client client) {
		//All the math involved in service time is done here. 
		//Since it'll never change after this, we can let the implementation of Client handle everything.
		//equals on Client works by id. Let's just make a new client, since the Client class doesn't let me modify the service time...
		int openTeller = firstOpenTellerIndex();
		if (openTeller == -1)
		{
			int nextClientTime = getNextServiceTime();
			clientsInLine.offer(new LineClient(client, nextClientTime));
			return true;
		}
		else
		{
			//Add the client directly to the teller.
			clientsAtTellers[openTeller] = client;
			return true;
		}
	}
}