package com.clouby.tetris.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONException;



public class HighScore extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7003944782821100785L;
	static final String ENTITY_NAME = "HighScoreRecord";
	static final int numOfEntities = 5; 
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(ENTITY_NAME);
		query.addSort("score",SortDirection.DESCENDING );
		PreparedQuery pq = datastore.prepare(query);
		JSONObject jsonEntity = new JSONObject(); 
		JSONArray jsonEntityArray = new JSONArray(); 

		List<Entity> entities =  pq.asList(FetchOptions.Builder.withLimit(numOfEntities));
		for (Entity entity : entities) {
			long score = (long) entity.getProperty("score");
			String name  = (String) entity.getProperty("name");
		
			try {
				jsonEntity.put("score", score);
				jsonEntity.put("name", name);
				jsonEntityArray.put(jsonEntity);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonEntity = new JSONObject(); 
		}
		
	    try {
			jsonEntityArray.write(resp.getWriter());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// Places the location parameters in the same entity group as the Location record
		Entity highscoreInst = new Entity(ENTITY_NAME);

		highscoreInst.setProperty("name", req.getParameter("name"));
		highscoreInst.setProperty("score", Long.parseLong(req.getParameter("score")));
		
		// Now put the entry to Google data store
		DatastoreService datastore =
					DatastoreServiceFactory.getDatastoreService();
		datastore.put(highscoreInst);
	}
}