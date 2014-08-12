package de.tum.in.aics.thesis.project.implementations;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.aics.thesis.project.interfaces.IPathFindAlgorithm;
import de.tum.in.aics.thesis.project.models.Edge;
import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;
import de.tum.in.aics.thesis.project.models.Vertex;
import de.tum.in.aics.thesis.project.util.Utilities;

public class DynammicAlgo implements IPathFindAlgorithm {

	public List<Place> findPath(Location sourceLoc, Location destinationLoc,
			List<Place> lstplaces, float time, float budget) {
		// TODO Auto-generated method stub
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
		return null;
	}

}
