package org.inria.restlet.mta.resources;

import java.util.ArrayList;
import java.util.Iterator;

import org.inria.restlet.mta.backend.Backend;
import org.inria.restlet.mta.internals.Tweet;
import org.inria.restlet.mta.internals.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class TweetResource extends ServerResource {
	
	/** Backend.*/
    private Backend backend_;

    /** Utilisateur géré par cette resource.*/
    private User user_;

    /**
     * Constructor.
     * Call for every single user request.
     */
    public TweetResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }
    
    @Post("json")
    public Representation AddTweet(JsonRepresentation representation) throws Exception
    {
    	System.out.println("adding a tweet");
        String userIdString = (String) getRequest().getAttributes().get("userId");
        int userId = Integer.valueOf(userIdString);
        JSONObject object = representation.getJsonObject();
        String Tweet = object.getString("tweet");
        
        user_ = backend_.getDatabase().getUser(userId);
        
        
        user_.AddTweet(Tweet);

        return new JsonRepresentation(Tweet);
    }
    
    @Get("json")
    public Representation GetTweets() throws Exception
    {
        String userIdString = (String) getRequest().getAttributes().get("userId");
        int userId = Integer.valueOf(userIdString);
        user_ = backend_.getDatabase().getUser(userId);

       
        Iterator<Tweet> tweets=user_.GetTweets();
        int number=0;
        ArrayList<JSONObject> User_Tweets=new ArrayList<JSONObject>();
        
        while(tweets.hasNext())
        {
        	
        	JSONObject This = new JSONObject();
        	This.put("Tweet "+String.valueOf(number)+":",tweets.next().GetTweet());
        	number++;
        	User_Tweets.add(This);
        	
        }
        
        JSONArray jsonArray = new JSONArray(User_Tweets);

        return new JsonRepresentation(jsonArray);
    }
    

}
