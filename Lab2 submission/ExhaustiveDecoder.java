import java.util.ArrayList;
import java.util.Set;

import edu.neumont.nlp.DecodingDictionary;


public class ExhaustiveDecoder {

	ArrayList<PossibleMessage> possibilities;
	DecodingDictionary dd;
	int thresh = 100;
	public ExhaustiveDecoder(DecodingDictionary dd, int threshHold)
	{
		thresh = threshHold;
		this.dd = dd;
	}
	
	public String[] decode(String morse)
	{
		possibilities = new ArrayList<PossibleMessage>();
		findPossibilities(morse, new PossibleMessage(new PossibleWord[0]));
		//weedOutInitials();
		sortPossibles();
		return getWordsFromPossiblesList();
	}
	
	public void sortPossibles()
	{
		possibilities.sort(new WordCompare());
		if (possibilities.size() > 20)
		{
			ArrayList<PossibleMessage> temp = new ArrayList<PossibleMessage>();
			temp.addAll(possibilities.subList(0, 20));
			possibilities = temp;
		}
	}
	
	public void weedOutInitials()
	{
		for(int i = 0; i < possibilities.size(); i ++)
		{
			boolean removeIt = false;
			for(int j = 0; !removeIt && j < possibilities.get(i).words.length; j ++)
			{
				if (possibilities.get(i).words[j].word.length() > 1 && possibilities.get(i).words[j].word.charAt(1)== '.')
				{
					removeIt = true;
				}
			}
			if (removeIt)
			{
				possibilities.remove(i);
				i--;
			}
		}
	}
	
	public String[] getWordsFromPossiblesList()
	{
		String[] words = new String[possibilities.size()];
		int ind = 0;
		for(PossibleMessage s : possibilities)
		{
			String tot = "";
			for(PossibleWord str : s.words)
			{
				tot += str.word + " ";
			}
			words[ind++] = tot + s.score;
		}
		return words;
	}
	
	int tCounter = 0;
	
	public void findPossibilities(String code, PossibleMessage message)
	{
		if (code.length() == 0)
		{
			message.calculateScore();
			possibilities.add(message);
			return;
		}
		else
		{
			
			PossibleWord[] nextWordList = getPossibleFirstWords(code);
			if (nextWordList.length == 0)
			{
				//Dead end. Don't add to possibilities list, but return.
				return;
			}
			else
			{
				for(int i = 0; i < nextWordList.length; i ++)
				{
					boolean branchHere = true;
					if (message.words.length > 0)
					{
						nextWordList[i].score = dd.frequencyOfFollowingWord(message.words[message.words.length-1].word, nextWordList[i].word);
						if (nextWordList[i].score < thresh)
						{
							branchHere = false;
						}
					}
					if (branchHere)
					{
						PossibleWord[] mess = new PossibleWord[message.words.length + 1];
						for(int j = 0 ; j < message.words.length; j ++)
						{
							mess[j] = message.words[j];
						}
						mess[mess.length - 1] = nextWordList[i];
						
						findPossibilities(code.substring(nextWordList[i].lengthInMorse), new PossibleMessage(mess));
					}
				}
			}
		}
	}
	
	public PossibleWord[] getPossibleFirstWords(String morse)
	{
		ArrayList<PossibleWord> words = new ArrayList<PossibleWord>();
		for(int i = 0 ; i < morse.length(); i ++)
		{
			int lm = morse.length() - i;
			Set<String> wordsOfThisLength = dd.getWordsForCode(morse.substring(0, lm));
			
			if (wordsOfThisLength != null)
			{
				for (String s : wordsOfThisLength)
				{
					if (!(s.length() > 1 && s.charAt(1)== '.'))
					{
						words.add(new PossibleWord(s, lm));
					}
				}
			}
		}
		
		return  words.toArray(new PossibleWord[0]);
		
	}
}
