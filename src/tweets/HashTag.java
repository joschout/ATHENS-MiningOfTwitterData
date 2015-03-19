package tweets;

public class HashTag implements Comparable<HashTag> {

	private static final String HASHTAG = "#";
	private final String text;
	
	public HashTag(String text){
		this.text = text.toLowerCase();
	}
	
	@Override
	public String toString(){
		return (HashTag.HASHTAG + this.text);
	}
	
	@Override
	public boolean equals(Object other){
		if(this.getClass() != other.getClass()){
			throw new UnsupportedOperationException("Hashtags cannot be compared to other objects through \"equals()\" method");
		}
		return (this.text.equals(((HashTag)other).text));
	}
	
	@Override
	public int compareTo(HashTag other){
		return this.text.compareTo(other.text);
	}
	
	@Override
	public int hashCode(){
		return this.text.hashCode();
	}
	
}
