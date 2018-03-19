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
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * Resource exposing a user.
 *
 * @author msimonin
 * @author ctedeschi
 *
 */
public class UserResource extends ServerResource
{

    /** Backend.*/
    private Backend backend_;

    /** Utilisateur géré par cette resource.*/
    private User user_;

    /**
     * Constructor.
     * Call for every single user request.
     */
    public UserResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }


    @Get("json")
    public Representation getUser() throws Exception
    {
        String userIdString = (String) getRequest().getAttributes().get("userId");
        int userId = Integer.valueOf(userIdString);
        user_ = backend_.getDatabase().getUser(userId);

        JSONObject userObject = new JSONObject();
        userObject.put("name", user_.getName());
        userObject.put("age", user_.getAge());
        userObject.put("id", user_.getId());
        
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
        userObject.put("tweets", jsonArray);

        return new JsonRepresentation(userObject);
    }

    
    @Delete("json")
    public Representation DeleteUser() throws Exception
    {
        String userIdString = (String) getRequest().getAttributes().get("userId");
        int userId = Integer.valueOf(userIdString);
       User user=backend_.getDatabase().getUser(userId);
       boolean deleted=backend_.getDatabase().DeleteUser(userId,user);
       
       JSONObject object= new JSONObject();
       object.put("User Delete", deleted);
       return new JsonRepresentation(object);
    }
}
