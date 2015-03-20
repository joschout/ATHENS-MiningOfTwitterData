package tweets;


public class HashTag extends KeyWord {

	private static final String HASHTAG = "#";
	
	public HashTag(String text){
		super(text);
	}
	
	@Override
	public String toString(){
		return (HashTag.HASHTAG + this.text);
	}
	
}
