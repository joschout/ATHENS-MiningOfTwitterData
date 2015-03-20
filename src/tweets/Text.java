package tweets;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Text {
	
	private final static String TAGGER_DIRECTORY = "taggers";
	public final String content;
	public final List<Word> words = new ArrayList<Word>();
	
	public Text(String content){
		this.content = content.toLowerCase();
		parseContent();
	}
	
	private void parseContent(){
		FileSystem defaultFileSystem = FileSystems.getDefault();
    	Path path = defaultFileSystem.getPath(Text.TAGGER_DIRECTORY, "/english-left3words-distsim" + ".tagger");
		MaxentTagger tagger = new MaxentTagger(path.toString());
		String tagged = tagger.tagString(this.content);
		parseWords(tagged);
	}
	
	private void parseWords(String keyWords){
		String[] splitKeyWords = keyWords.split("\\s+");
		for(String word : splitKeyWords){
			String[] parts = word.split("_");
			if(parts.length == 2){
				switch(parts[1]){
				case "NN":
					this.words.add(new Word(parts[0],parts[1]));
					break;
				default:
					break;
				}
			}
		}
	}
}
