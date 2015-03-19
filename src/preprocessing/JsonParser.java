package preprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import tweets.HashTag;
import tweets.Tweet;

public class JsonParser {
	
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public final String directory;

	public JsonParser(String directory){
		this.directory = directory;
	}
	
	public List<Tweet> parseFile(String fileName) throws IOException{
		
		FileSystem defaultFileSystem = FileSystems.getDefault();
		Path file = defaultFileSystem.getPath(this.directory, fileName);
	
		BufferedReader reader = Files.newBufferedReader(file, JsonParser.CHARSET);
	    String line = reader.readLine();
	    List<Tweet> tweets = new ArrayList<Tweet>();
	    while (line != null){
	    	parseLine(line, tweets);
	    	line = reader.readLine();
	    }
	    return tweets;
	}
	
	private void parseLine(String line, List<Tweet> tweets){

	  JSONObject parsedLine = (JSONObject) JSONValue.parse(line);
	  JSONObject entities = (JSONObject) parsedLine.get("entities");
	  JSONArray hashtags = (JSONArray) entities.get("hashtags");
	  List<HashTag> parsedHashtags = new ArrayList<HashTag>();
	  for(Object hashTag : hashtags){
		  parsedHashtags.add(new HashTag(((JSONObject) hashTag).get("text").toString()));
	  }
	  if(!parsedHashtags.isEmpty())
		  tweets.add(new Tweet(parsedHashtags));
	}
}
