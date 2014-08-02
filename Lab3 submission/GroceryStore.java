import edu.neumont.util.Client;
import edu.neumont.util.QueueableService;


public class GroceryStore implements QueueableService{
	
	static class LineClient{
		Client client;
		int waitTime;
		public LineClient(Client client, int waitTime)
		{
			this.client = client;
			this.waitTime = waitTime;
		}
	}
	
	ArrayList<LinkedList<LineClient>> lines;
	//Position 0 is being serviced.
	
	public GroceryStore(int numberOfLines)
	{
		lines = new ArrayList<LinkedList<LineClient>>();
		for(int i= 0 ; i < numberOfLines; i ++)
		{
			lines.add(new LinkedList<LineClient>());
		}
	}
	
	
	@Override
	public double getAverageClientWaitTime() {
		double totalTime = 0;
		int clientCount = 0;
		for(LinkedList<LineClient> ll : lines)
		{
			for(LineClient lc : ll)
			{
				totalTime += lc.waitTime;
				clientCount ++;
			}
		}
		return totalTime / clientCount;
	}

	@Override
	public double getClientWaitTime(Client client) {
		double waitTime = -1;
		for(LinkedList<LineClient> ll : lines)
		{
			for(LineClient lc : ll)
			{
				if (lc.client == client)
				{
					waitTime = lc.waitTime;
					break;
				}
			}
		}
		return waitTime;
	}

	@Override
	public boolean addClient(Client client) {
		LinkedList<LineClient> line = getShortestLine();
		int waitTimeOfLine = getWaitTimeOfLine(line);
		line.offer(new LineClient(client, waitTimeOfLine));
		return true;
	}
	
	public int getWaitTimeOfLine(LinkedList<LineClient> ll)
	{
		int total = 0;
		for(LineClient lc : ll)
		{
			total += lc.client.getExpectedServiceTime();
		}
		return total;
	}
	
	public LinkedList<LineClient> getShortestLine()
	{
		LinkedList<LineClient> shortest = lines.get(0);
		
		for(LinkedList<LineClient> ll : lines)
		{
			if (ll.size < shortest.size)
			{
				shortest = ll;
			}
		}
		return shortest;
	}

	public void printState()
	{
		System.out.println();
		//Print all lines.
		
		for(LinkedList<LineClient> ll : lines)
		{
			//Print each member
			if (ll.size == 0)
			{
				System.out.print("--");
			}
			else
			{
				for(LineClient lc : ll)
				{
					System.out.print(lc.waitTime + "/" + lc.client.getExpectedServiceTime() + "  ");
				}
			}
			System.out.println();
		}
		
	}
	
	
	@Override
	public void advanceMinute() {
		//The first clients in all the linked lists (peek) advance served minute.
		for(LinkedList<LineClient> ll : lines)
		{
			LineClient c = ll.peek();
			if (c != null)
			{
				c.client.servedMinute();
				if (c.client.getExpectedServiceTime() == 0)
				{
					ll.poll();
				}
			}
		}
		//Advance all client's wait times by 1.
		for(LinkedList<LineClient> ll : lines)
		{
			for(LineClient lc : ll)
			{
				if (lc.waitTime > 0)
				{
					lc.waitTime --;
				}
			}
		}
	}

}
