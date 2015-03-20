package tweets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Tweet {

	public final static MaxentTagger TAGGER = new MaxentTagger("taggers/english-left3words-distsim.tagger");
	
	public final long tweetId;
	public final long userId;
	public final Text text;
	public final List<HashTag> hashtags;
	public final List<KeyWord> keywords;
	
	public Tweet(long tweetId, long userId, Text text, List<HashTag> hashtags, List<KeyWord> alreadyCreatedKeywords){
		this.tweetId = tweetId;
		this.userId = userId;
		this.text = text;
		this.hashtags = hashtags;
		this.keywords = getKeyWords(alreadyCreatedKeywords);
	}
	
	@Override
	public String toString(){
		String result = "Tweet " + this.tweetId + " by user " + this.userId + ":\n";
		result += this.text + "\n";
		for(HashTag hashtag : this.hashtags){
			result += hashtag + " ";
		}
		return result;
	}
	
	@Override
	public boolean equals(Object other){
		if(this.getClass() != other.getClass()){
			throw new UnsupportedOperationException("Tweets cannot be compared to other objects through \"equals()\" method");
		}
		return (this.keywords.equals(((Tweet)other).keywords));
	}
	
	@Override
	public int hashCode(){
		return this.keywords.hashCode();
	}
	
	private List<KeyWord> getKeyWords(List<KeyWord> alreadyCreatedKeywords){
		Set<KeyWord> keywords = new TreeSet<KeyWord>();
		for(HashTag hashtag : this.hashtags){
			KeyWord keyword;
			if(alreadyCreatedKeywords.contains(hashtag)){
				keyword = alreadyCreatedKeywords.get(alreadyCreatedKeywords.indexOf(hashtag));
				keyword.addTweet(this);
			}
			else{
				keyword = hashtag;
				keyword.addTweet(this);
				alreadyCreatedKeywords.add(keyword);
			}
			keywords.add(keyword);
		}
		for(Word word : this.text.words){
			KeyWord keyword;
			if(alreadyCreatedKeywords.contains(word)){
				keyword = alreadyCreatedKeywords.get(alreadyCreatedKeywords.indexOf(word));
				keyword.addTweet(this);
			}
			else{
				keyword = word;
				keyword.addTweet(this);
				alreadyCreatedKeywords.add(keyword);
			}
			keywords.add(keyword);
		}
		return new ArrayList<KeyWord>(keywords);
	}
	
	private boolean approximatelyEqual(KeyWord first, KeyWord second){
		return (hammingDistance(first.text, second.text) <= 2);
	}
	
	private int hammingDistance(String firstWord, String secondWord){
		int differenceInLength = firstWord.length() - secondWord.length();
		if(differenceInLength == 0){
			return hammingDistanceEqualLength(firstWord, secondWord);
		}
		else if(differenceInLength == 1){
			return Math.min(hammingDistanceEqualLength(" " + firstWord, secondWord), hammingDistanceEqualLength(firstWord + " ", secondWord));
		}
		else if(differenceInLength == 2){
			return Math.min(Math.min(hammingDistanceEqualLength("  " + firstWord, secondWord), hammingDistanceEqualLength(" " + firstWord + " ", secondWord)),hammingDistanceEqualLength(firstWord + "  ", secondWord));
		}
		else return Integer.MAX_VALUE;
	}
	
	private int hammingDistanceEqualLength(String firstWord, String secondWord){
		if(firstWord.length() != secondWord.length())
			throw new UnsupportedOperationException("Words are not of equal length!");
		int counter = 0;
		for (int i = 0; i < firstWord.length(); i++) {
		    if (firstWord.charAt(i) != secondWord.charAt(i)) {
		        ++counter;
		    }
		}
		return counter;
	}
	
}
