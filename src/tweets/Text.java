package tweets;

import java.util.ArrayList;
import java.util.List;

public class Text {
	
	public final String content;
	public final List<Word> words = new ArrayList<Word>();
	
	public Text(String content, boolean parseContent){
		this.content = content.toLowerCase();
		if(parseContent)
			parseContent();
	}
	
	private void parseContent(){
		String tagged = Tweet.TAGGER.tagString(this.content);
		parseWords(tagged);
	}
	
	private void parseWords(String keyWords){
		String[] splitKeyWords = keyWords.split("\\s+");
		for(String word : splitKeyWords){
			String[] parts = word.split("_");
			if(parts.length == 2){
				if(!parts[0].startsWith("#")){
					if(parts[1].contains("NN")){
						this.words.add(new Word(parts[0],"noun"));
					}
					else if(parts[1].contains("JJ")){
						this.words.add(new Word(parts[0],"adjective"));
					}
					else if(parts[1].contains("VB")){
						this.words.add(new Word(parts[0],"verb"));
					}
				}
			}
		}
	}
}
