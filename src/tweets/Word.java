package tweets;


public class Word extends KeyWord {
	
	private final String label;

	public Word(String text, String label) {
		super(text);
		this.label = label;
	}

	@Override
	public String toString() {
		return (this.text + " (" + this.label + ")");
	}
	
}
