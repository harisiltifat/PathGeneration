package de.tum.in.aics.thesis.project.implementations;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import de.tum.in.aics.thesis.project.interfaces.IPathFindAlgorithm;
import de.tum.in.aics.thesis.project.models.Edge;
import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;
import de.tum.in.aics.thesis.project.models.Vertex;
import de.tum.in.aics.thesis.project.models.PathParams;
import de.tum.in.aics.thesis.project.util.Utilities;

public class PathFind_DynammicAlgoImpl implements IPathFindAlgorithm {

	PriorityQueue<Vertex> vertexQueue;
	Vertex destinationVertex=null;
	Vertex sourceVertex=null;
	public List<Place> findPath(Location sourceLoc, Location destinationLoc,
			List<Place> lstplaces, float time, float budget) {
		// TODO Auto-generated method stub
		CreateGraph(sourceLoc, destinationLoc, lstplaces);
		Vertex v=null;
		while (!vertexQueue.isEmpty()) {
			v = vertexQueue.poll();
			for(Edge edge:v.getAdjacencies()){
				//Iterating on backward edges only
				if(edge.isBackwardEdge()){
					//If the backward node is source then handle specifically
					if(edge.getTarget().equals(sourceVertex)){
						PathParams pathObj=new PathParams();
						pathObj.setMaxEntertainment(v.getRating());
						pathObj.setMaxCost(v.getCost());
						pathObj.lstPath.add(edge.getTarget());
						Map.Entry<Double,PathParams> keyValuePair=new AbstractMap.SimpleEntry<Double,PathParams>(edge.getWeight(),pathObj);
						performComparisionSteps(time, budget, v, keyValuePair);
					}
					//If the backward node is not source
					else{
						Map<Double,PathParams> previousMapPath=edge.getTarget().getMapPath();
						for(Entry previousEntry:previousMapPath.entrySet()){
							PathParams pathObj=new PathParams();
							pathObj.setMaxEntertainment(v.getRating()+((PathParams)previousEntry.getValue()).getMaxEntertainment());
							pathObj.setMaxCost(v.getCost()+ ((PathParams)previousEntry.getValue()).getMaxCost());
							pathObj.lstPath.addAll(((PathParams)previousEntry.getValue()).lstPath);
							pathObj.lstPath.add(edge.getTarget());
							Map.Entry<Double,PathParams> keyValuePair=new AbstractMap.SimpleEntry<Double,PathParams>(edge.getWeight()+(Double)previousEntry.getKey(),pathObj);
							performComparisionSteps(time, budget, v, keyValuePair);
						}
					}
				}
			}
		}
		double maximumEntertainment=0;
		List<Vertex> finalPathVertex=null;
		
		for(Entry destinationEntry:destinationVertex.getMapPath().entrySet()){
			double otherEntertainment=((PathParams)destinationEntry.getValue()).getMaxEntertainment();
			if(otherEntertainment>maximumEntertainment){
				maximumEntertainment=otherEntertainment;
				finalPathVertex=((PathParams)destinationEntry.getValue()).lstPath;
			}
		}
		
		List<Place> lstPathPlaces=new ArrayList<Place>();
		for(Vertex vertex:finalPathVertex){
			
			lstPathPlaces.add(createPlace(vertex));
		}
		lstPathPlaces.add(createPlace(destinationVertex));
		return lstPathPlaces;
	}

	private void performComparisionSteps(float time, float budget, Vertex v,
			Map.Entry<Double, PathParams> keyValuePair) {
		
		//Checking cost constraint
		if(checkCostConstraint(keyValuePair.getValue().getMaxCost(),budget)){
			//Checking time constraint
			if(checkTimeConstraint(keyValuePair.getKey(),time)){
				int responseEquivalentKey=findEquivalentKey(v,keyValuePair);
				if(responseEquivalentKey==2){
					v.getMapPath().put(keyValuePair.getKey(),keyValuePair.getValue());
					findLargerKey(v,keyValuePair);
				}
				else if(responseEquivalentKey==1){
					if(findSmallerKey(v,keyValuePair)){
						findLargerKey(v,keyValuePair);
					}
				}
					
			}
		}
		
	}

	private void CreateGraph(Location sourceLoc, Location destinationLoc,
			List<Place> lstplaces) {

		//Declaring source place and destination place
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
				
				vertexQueue = new PriorityQueue<Vertex>();
				for(Vertex vertex: lstVertices){
					Location locVertices=new Location(vertex.getLat(), vertex.getLng());
					double distance= Utilities.distance(sourceLoc,locVertices,'k');
					vertex.setDistanceFromSource(distance);
					if(!vertex.equals(sourceVertex))
						vertexQueue.add(vertex);
				}
				
				//Edge creation and assigning to vertices. Creating a complete graph
				for(Vertex vertex: lstVertices){
					for(Vertex vertexToCompare: lstVertices){
						if(!vertex.equals(sourceVertex)){
							Location loc1=new Location(vertex.getLat(),vertex.getLng());
							Location loc2=new Location(vertexToCompare.getLat(), vertexToCompare.getLng());
							double distance= Utilities.distance(loc1,loc2,'k');
							Edge edge=new Edge(vertexToCompare,distance);
							
							//Creating forward edges to vertices who are further from source
							if(vertexToCompare.getDistanceFromSource()>vertex.getDistanceFromSource())
								edge.setForwardEdge(true);
							//Creating backward edges to vertices who are nearer from source
							else if(vertexToCompare.getDistanceFromSource()<vertex.getDistanceFromSource())
								edge.setBackwardEdge(true);
							//Creating backward or forward edges depending the type of edge equivalent vertex had
							else{
								for(Edge e:vertexToCompare.getAdjacencies()){
									if(e.getTarget().equals(vertexToCompare)){
										if(e.isForwardEdge())
											edge.setBackwardEdge(true);
										else
											edge.setForwardEdge(true);
									}
								}
							}
							vertex.getAdjacencies().add(edge);
						}
					}
				}
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
		vertex.setLng(place.getLongitude());
		vertex.setLat(place.getLatitude());
		
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
		place.setLongitude(vertex.getLng());
		place.setLatitude(vertex.getLat());
		return place;
	}
	
	private boolean checkTimeConstraint(double timeSpent, double timeAllowed){
		if(timeSpent<=timeAllowed)
			return true;
		else
			return false;
	}
	
	private boolean checkCostConstraint(double costSpent, double costAllowed){
		if(costSpent<=costAllowed)
			return true;
		else
			return false;
	}
	
	//0 means key-value pair needs to be rejected 
	//1 means proceed to step c and d
	//2 means insert key value and perform step d
	private int findEquivalentKey(Vertex currentVertex, Map.Entry<Double,PathParams> keyValuePair){
		if(currentVertex.getMapPath().containsKey(keyValuePair.getKey())){
			if(keyValuePair.getValue().getMaxEntertainment()>currentVertex.getMapPath().get(keyValuePair.getKey()).getMaxEntertainment())
				return 2;
			else
				return 0;
		}
		return 1;
	}
	
	private void findLargerKey(Vertex currentVertex, Map.Entry<Double,PathParams> keyValuePair){
		while(true){
			Double largerKey=currentVertex.getMapPath().higherKey(keyValuePair.getKey());
			if(largerKey!=null){
				if(currentVertex.getMapPath().get(largerKey).getMaxEntertainment()<=keyValuePair.getValue().getMaxEntertainment()){
					currentVertex.getMapPath().remove(largerKey);
				}
				else
					break;
			}
			else 
				break;
		}
	}
		
	private boolean findSmallerKey(Vertex currentVertex, Map.Entry<Double,PathParams> keyValuePair){
		Double smallerKey=currentVertex.getMapPath().lowerKey(keyValuePair.getKey());
		if(smallerKey == null){
			currentVertex.getMapPath().put(keyValuePair.getKey(), keyValuePair.getValue());
			return true;
		}
			
		if(currentVertex.getMapPath().get(smallerKey).getMaxEntertainment()<keyValuePair.getValue().getMaxEntertainment()){
			currentVertex.getMapPath().put(keyValuePair.getKey(), keyValuePair.getValue());
			return true;
		}
			
		return false;
	}
}
