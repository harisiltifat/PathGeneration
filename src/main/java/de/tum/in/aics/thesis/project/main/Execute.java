package de.tum.in.aics.thesis.project.main;

import java.io.IOException;
import java.util.List;

import de.tum.in.aics.thesis.project.implementations.PathFind_DijkstraDivImpl;
import de.tum.in.aics.thesis.project.implementations.PathFind_DijkstraSubImpl;
import de.tum.in.aics.thesis.project.implementations.PathFind_DijkstraImpl;
import de.tum.in.aics.thesis.project.implementations.SearchServiceImpl;
import de.tum.in.aics.thesis.project.interfaces.IPathFindAlgorithm;
import de.tum.in.aics.thesis.project.interfaces.ISearchService;
import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;

public class Execute {
	  public static void main( String[] args ) throws IOException
	   {
		  ISearchService service=new SearchServiceImpl();
		  Location sourceLoc=new Location(48.1438442,11.578250300000036);
		  Location destLoc= new Location(48.137048000000000000,11.575385999999980000); 
	      List<Place> lstplaces= service.search(sourceLoc,destLoc);
	      IPathFindAlgorithm algo=new PathFind_DijkstraDivImpl();
	      List<Place> lstPath= algo.findPath(sourceLoc, destLoc, lstplaces, 0, 0);
	     /* for(Place place:lstPath){
	    	  System.out.println(place.getName()+"Minimum distance:");
	    	  
	      }*/
	   }
}
