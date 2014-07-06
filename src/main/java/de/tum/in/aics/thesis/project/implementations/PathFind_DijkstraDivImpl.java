package de.tum.in.aics.thesis.project.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;








import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;
import de.tum.in.aics.thesis.project.interfaces.IPathFindAlgorithm;
import de.tum.in.aics.thesis.project.interfaces.ISearchService;
import de.tum.in.aics.thesis.project.models.Edge;
import de.tum.in.aics.thesis.project.models.Vertex;
import de.tum.in.aics.thesis.project.util.Utilities;

public class PathFind_DijkstraDivImpl implements IPathFindAlgorithm {
	//Compute paths i.e. distance to nodes
	private void computePaths(Vertex source)
	{
		source.setMinDistance(0.);

		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);
		
		
		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			
			// Visit each edge exiting u
			for (Edge e : u.getAdjacencies())
			{
				u.setExplored(true);
				Vertex v = e.getTarget();
				//It means it is a source. Setting distance from source for priority queue
				if(u.getMinDistance()==0){
					v.setDistanceFromSource(e.getWeight());
				}
				if(!v.isExplored()){
					double weight = e.getWeight();
					double distanceThroughU = u.getMinDistance() + weight;
					double entertainmentThroughU=u.getMaxEntertainment()+v.getRating();
					if (entertainmentThroughU/distanceThroughU > v.getMaxEntertainment()/v.getMinDistance()) {
					/*if (distanceThroughU/entertainmentThroughU < v.getMinDistance()/v.getMaxEntertainment()){*/
						vertexQueue.remove(v);
						v.setMinDistance(distanceThroughU) ;
						v.setMaxEntertainment(entertainmentThroughU);
						v.setPrevious(u);
						vertexQueue.add(v);
					}
				}
				
			}
		}
	}
	
	//Retrieve shortest paths
	private List<Vertex> getShortestPathTo(Vertex target)
	{
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.getPrevious())
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

	public List<Place> findPath(Location sourceLoc, Location destinationLoc,
			List<Place> lstplaces, float time, float budget) 
			{
		//Declaring source place and destination place
		Vertex sourceVertex=null, destinationVertex=null;

		List<Vertex> lstVertices=new ArrayList<Vertex>();

		//Creating vertices and finding source place and destination place

		for(Place place: lstplaces){
			
			Vertex vertex=createVertex(place);
			//Checking if source exist in the list of places
			if(sourceLoc.getLat()==vertex.getLat() && sourceLoc.getLng()==vertex.getLng())
				sourceVertex=vertex;
			//Checking if destination exist in the list of places
			else if(destinationLoc.getLat()==vertex.getLat() && destinationLoc.getLng()==vertex.getLng())
				destinationVertex=vertex;
			
			lstVertices.add(vertex);
		}

		//source location may not be in the list of places. Then add source manually
		if(sourceVertex==null){
			sourceVertex=new Vertex("source");
			sourceVertex.setLat(sourceLoc.getLat());
			sourceVertex.setLng(sourceLoc.getLng());
			sourceVertex.setRating(1);
			lstVertices.add(sourceVertex);
		}
		//destination location may not be in the list of places. Then add destination manually
		if(destinationVertex==null){
			destinationVertex=new Vertex("destination");
			destinationVertex.setLat(destinationLoc.getLat());
			destinationVertex.setLng(destinationLoc.getLng());
			destinationVertex.setRating(1);
			lstVertices.add(destinationVertex);
		}

		//Edge creation and assigning to vertices. Creating a complete graph
		for(Vertex vertex: lstVertices){
			for(Vertex vertexToCompare: lstVertices){
				if(!vertex.equals(vertexToCompare)){
					Location loc1=new Location(vertex.getLat(),vertex.getLng());
					Location loc2=new Location(vertexToCompare.getLat(), vertexToCompare.getLng());
					double distance= Utilities.distance(loc1,loc2,'k');
					Edge edge=new Edge(vertexToCompare,distance);
					vertex.getAdjacencies().add(edge);
				}
			}
		}
		//Compute path from source to destination
		computePaths(sourceVertex);

		//Retrieving the shortest path
		List<Vertex> path = getShortestPathTo(destinationVertex);
		List<Place> lstPath=new ArrayList<Place>();

		for(Vertex vertex:path){
			lstPath.add(createPlace(vertex));
		}
		return lstPath;
			}

	private Vertex createVertex(Place place) {
		Vertex vertex= new Vertex(place.getName());
		vertex.setGeometry(place.getGeometry());

		vertex.setRating(place.getRating());


		if(place.getLikes()!=null)
			vertex.setLikes(place.getLikes());
		vertex.setTypes(place.getTypes());
		vertex.setOpenNow(place.getOpenNow());
		vertex.setStats(place.getStats());
		JSONObject geometryJsonObj;

		try {
			geometryJsonObj = new JSONObject(vertex.getGeometry());
			vertex.setLng(Double.parseDouble(geometryJsonObj.getString("lng")));
			vertex.setLat(Double.parseDouble(geometryJsonObj.getString("lat")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vertex;
	}

	private Place createPlace(Vertex vertex){
		Place place= new Place();
		place.setName(vertex.getName());
		place.setGeometry(vertex.getGeometry());
		place.setRating(vertex.getRating());
		place.setLikes(vertex.getLikes());
		place.setTypes(vertex.getTypes());
		place.setOpenNow(vertex.getOpenNow());
		place.setStats(vertex.getStats());
		return place;
	}
}
