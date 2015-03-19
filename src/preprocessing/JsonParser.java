package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import tweets.HashTag;
import tweets.Tweet;
import utils.Timer;

public class JsonParser {
	
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public final String directory;

	public JsonParser(String directory){
		this.directory = directory;
	}
	
	public List<Tweet> parseFile(String fileName) throws IOException{
		Timer parseTimer = new Timer("parsing \"" + fileName + "\"");
		parseTimer.start();
		
		FileSystem defaultFileSystem = FileSystems.getDefault();
		Path file = defaultFileSystem.getPath(this.directory, fileName);
	
		BufferedReader reader = Files.newBufferedReader(file, JsonParser.CHARSET);
	    String line = reader.readLine();
	    List<Tweet> tweets = new ArrayList<Tweet>();
	    while (line != null){
	    	parseLine(line, tweets);
	    	line = reader.readLine();
	    }
	    
		parseTimer.stop();
	    
		return tweets;
	}
	
	public List<Tweet> parseAllFiles() throws IOException{
			
	    List<Tweet> tweets = new ArrayList<Tweet>();

		File directory = new File(this.directory);
		File[] files = directory.listFiles();
		
		if (files != null) {
			for (File file : files) {
				try{
					if(!file.getName().equals(".DS_Store"))
						tweets.addAll(parseFile(file.getName()));
				}
				catch(MalformedInputException ex){
					System.out.println("MalformedInputException: " + ex + " while parsing file \"" + file.getName() + "\"");
				}
			}
		}
		else
			throw new IllegalArgumentException("\"" + this.directory + "\" is not a directory!");
		return tweets;
		
	}
	
	private void parseLine(String line, List<Tweet> tweets){

	  JSONObject parsedLine = (JSONObject) JSONValue.parse(line);
	  
	  long parsedTweetId = Long.parseLong(parsedLine.get("id").toString());
	 
	  JSONObject user = (JSONObject) parsedLine.get("user");
	  long parsedUserId = Long.parseLong(user.get("id").toString());

	  String parsedText = parsedLine.get("text").toString();
	  
	  JSONObject entities = (JSONObject) parsedLine.get("entities");
	  JSONArray hashtags = (JSONArray) entities.get("hashtags");
	  List<HashTag> parsedHashtags = new ArrayList<HashTag>();
	  for(Object hashTag : hashtags){
		  parsedHashtags.add(new HashTag(((JSONObject) hashTag).get("text").toString()));
	  }

	  if(!parsedHashtags.isEmpty())
		  tweets.add(new Tweet(parsedTweetId, parsedUserId, parsedText, parsedHashtags));
	}
}
