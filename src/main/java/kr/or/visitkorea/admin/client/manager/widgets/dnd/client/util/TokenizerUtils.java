package kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util;



/**
 * Tokenizer
 */
public class TokenizerUtils  {
	
	private int currentPosition;
	private int maxPosition;
	private String str;
	private String delimiter;
	private int delimiterSize;
	
	private int countTokens	 = -1;
	
	public TokenizerUtils(String str) {
		this(str, " ");
	}

	public TokenizerUtils(String str, String delim) {
		this.str = str;
		this.delimiter = delim;
		currentPosition = 0;
		maxPosition = this.str.length();
		delimiterSize = this.delimiter.length(); 
		
	}	

	public boolean hasMoreTokens() {
		return ( (currentPosition <= maxPosition) || (str.indexOf(delimiter, currentPosition) > -1));
	}

	public String nextToken() {
		String result = null;
		int index = str.indexOf(delimiter, currentPosition);
		if (index > -1) {
			result = str.substring(currentPosition, index);
			currentPosition = index + delimiterSize;
		} else {
			result = str.substring(currentPosition);
			currentPosition = maxPosition + delimiterSize;
		}
		return result;
	}

	public int countTokens() {
		if (countTokens < 0) {
			int tCountTokens = 1;
			int cp = 0;
			int index = 0;
			while ((index = str.indexOf(delimiter, cp)) > -1) {
			    tCountTokens++;
				cp = index + delimiterSize;
			}
			countTokens = tCountTokens;
		}
		return countTokens;
	}

	public String[] toArray(int arraySize) {
	    String[] array = new String[arraySize];
	    for (int i = 0; i < arraySize && hasMoreTokens(); i++) {
	        array[i] = nextToken();
	    }
	    return array;
	}

	public String[] toArray() {
	    return toArray(countTokens());
	}	
}
