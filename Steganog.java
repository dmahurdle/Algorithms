import java.awt.Color;
import java.io.IOException;

import edu.neumont.ui.Picture;



public class Steganog {
	//Takes a clean image and changes the prime-indexed pixels to secretly carry the message
	
	public boolean useEscapeString = true;
	String escapeString = "<%*$END$*%>";
	String metaDataString = "<%*$META$*%>";
	public boolean useMetaData = true;
	
	public int readMetaData(Picture img)
	{
		int charCount = 0;
		PrimeIterator pi = new PrimeIterator(100);
		boolean hasMetaData = true;
		int width = img.width();
		int height = img.height();
		
		for(int i = 0 ; hasMetaData && i < metaDataString.length(); i ++)
		{
			int prime = pi.next();
			int y = prime / width;
			int x = prime - (y * width);
			Color c = img.get(x,  y);
			
			int red = c.getRed();
			int blue = c.getBlue();
			int green = c.getGreen();
			int bitmask = 0x03;
			
			int redBitWise = (red & bitmask) << 4;
			int greenBitWise = (green & bitmask) << 2;
			int blueBitWise = (blue & bitmask);
			
			int charValue = redBitWise + greenBitWise + blueBitWise;
			char realChar = (char)(charValue + 32);
			
			hasMetaData = realChar == metaDataString.charAt(i);
		}
		
		if (hasMetaData)
		{
			int[] rgbs[] = new int[6][3];
			int numberOfPrimes = 0;
			
			for(int i = 0 ; i < 5; i ++)
			{
				int prime = pi.next();
				int y = prime / width;
				int x = prime - (y * width);
				Color c = img.get(x,  y);
				
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				int bitmask = 0x03;
				
				//Iteration shift
				int ishift = 6 * (4 - i);
				
				int redMask = (red & bitmask) << (ishift + 4);
				int greenMask = (green & bitmask) << (ishift + 2);
				int blueMask = (blue & bitmask) << (ishift);
				//Two bits per int are important... Or we could get the int value here and just & them in 1 by 1....
				
				numberOfPrimes = numberOfPrimes | redMask | greenMask | blueMask;
			}
			charCount = numberOfPrimes;
		
		}
		return charCount;
	}
	
	public int getUpperValue(int n)
	{
		return (int)(n * (Math.log(n) + Math.log(Math.log(n))));
	}
	
	public Picture embedIntoImage(Picture cleanImage, String message) throws IOException
	{
		String messageToWrite = message.toUpperCase();
		if (useEscapeString)
		{
			messageToWrite += escapeString;
		}
		if (useMetaData)
		{
			int primeCount = messageToWrite.length() + metaDataString.length() + 5;
			
			String charsToAppend = metaDataString;
			
			int mask = 0x3F000000;
			int first = (primeCount & mask) >> 24;
			charsToAppend += (char) (first + 32);
			
			mask = 0xFC0000;
			int second = (primeCount & mask) >> 18;
			charsToAppend += (char) (second + 32);
			
			mask = 0x3F000;
			int third = (primeCount & mask) >> 12;
			charsToAppend += (char) (third + 32);
			
			mask = 0xFC0;
			int fourth = (primeCount & mask) >> 6;
			charsToAppend += (char) (fourth + 32);
			
			mask = 0x3F;
			int fifth = (primeCount & mask);
			charsToAppend += (char) (fifth + 32);
			
			messageToWrite = charsToAppend + messageToWrite;
		}
		int width = cleanImage.width();
		int height = cleanImage.height();
		int numberOfPrimesToGenerate = getUpperValue(messageToWrite.length());
		PrimeIterator pi = new PrimeIterator(numberOfPrimesToGenerate);
				
		for(int i = 0; i < messageToWrite.length(); i ++)
		{
			int prime = pi.next();
			int y = prime / width;
			int x = prime - (y * width);
			Color c = cleanImage.get(x,  y);
			
			int rgb = c.getRGB();
			int fullBitmask = 0xFFFCFCFC;
			int rgbWithoutLastTwoBits = rgb & fullBitmask;
			
			int characterInt = (int)(messageToWrite.charAt(i) - 32);
			int redBits = characterInt & 0x30;
			int redShifted = redBits << 12;
			int greenBits = characterInt & 0xC;
			int greenShifted = greenBits << 6;
			int blueShifted = characterInt & 0x3;
			
			int fullCharMask = redShifted | greenShifted | blueShifted;
			int newColor = rgbWithoutLastTwoBits | fullCharMask;
			
			cleanImage.set(x, y, new Color(newColor));
		}
		return cleanImage;
	}
	
	public int decimalOfChar(char c)
	{
		return (int)c - 32;
	}
	
	//Retreives the embedded secret from the secret-carrying image
	public String retreiveFromImage(Picture imageWithSecretMessage) throws IOException 
	{
		int width = imageWithSecretMessage.width();
		int height = imageWithSecretMessage.height();
		
		int primeMax = width * height;
		boolean foundMetaData = false;
		int stringBuilderInitialCapacity = 16;
		
		//metaData
		if (useMetaData)
		{
			int numToAccountFor = readMetaData(imageWithSecretMessage);
			if (numToAccountFor > 0)
			{
				foundMetaData = true;
				stringBuilderInitialCapacity = numToAccountFor;
				primeMax = getUpperValue(numToAccountFor);
			}
		}
		
		StringBuilder sb = new StringBuilder(stringBuilderInitialCapacity);
		
		PrimeIterator pi = new PrimeIterator(primeMax);
		
		if (foundMetaData)
		{
			//Get past the primes used for metadata.
			for(int i = 0 ; i < metaDataString.length() + 5; i ++)
			{
				pi.next();
			}
		}
		
		int currentEscapeStringIndex = 0;
		
		boolean foundEscapeString = false;
		while(pi.hasNext() && !foundEscapeString)
		{
			int prime = pi.next();
			int y = prime / width;
			int x = prime - (y * width);
			Color c = imageWithSecretMessage.get(x,  y);
			
			int red = c.getRed();
			int blue = c.getBlue();
			int green = c.getGreen();
			int bitmask = 0x03;
			
			int redBitWise = (red & bitmask) << 4;
			int greenBitWise = (green & bitmask) << 2;
			int blueBitWise = (blue & bitmask);
			
			int charValue = redBitWise + greenBitWise + blueBitWise;
			char realChar = (char)(charValue + 32);
			sb.append(realChar);
			
			if (useEscapeString)
			{
				if (realChar == escapeString.charAt(currentEscapeStringIndex))
				{
					currentEscapeStringIndex ++;
				}
				else
				{
					currentEscapeStringIndex = 0;
				}
				if (currentEscapeStringIndex >= escapeString.length())
				{
					foundEscapeString = true;
				}
			}
			
		}
		
		String result = sb.toString();
		
		if (useEscapeString)
		{
			result = result.substring(0, result.length() - escapeString.length());
			
		}
		
		return result;
	}
}
