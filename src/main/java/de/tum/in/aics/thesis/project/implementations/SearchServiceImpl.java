package de.tum.in.aics.thesis.project.implementations;

import java.util.ArrayList;
import java.util.List;
import de.tum.in.aics.thesis.project.fourSquare.FourSquarePlaces;
import de.tum.in.aics.thesis.project.interfaces.ISearchService;
import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;
import de.tum.in.aics.thesis.project.util.Utilities;


public class SearchServiceImpl  implements ISearchService {
	
	
	FourSquarePlaces places = new FourSquarePlaces();
	
	Utilities util = new Utilities();
	static Location midloc = new Location(0, 0);
	
	public List<Place> search(Location loc1, Location loc2) {
		
		List<Place> allPlaces = new ArrayList<Place>();
		
		midloc = util.midPoint(loc1, loc2);
		
		int dist = (int) Math.ceil(util.distance(midloc, loc1, 'K') * 1000);

		String categories = "4d4b7104d754a06370d81259";		
		List<Place> lstPlace = places.search(midloc.getLat(), midloc.getLng(), dist,categories, 50);
		util.mergeList(allPlaces, lstPlace);
		
		categories = "4d4b7105d754a06377d81259";
		List<Place> lstPlace2 = places.search(midloc.getLat(), midloc.getLng(), dist,categories, 50);
		util.mergeList(allPlaces, lstPlace2);
		
		categories = "4d4b7105d754a06373d81259,4d4b7105d754a06376d81259,4d4b7105d754a06374d81259";
		List<Place> lstPlace3 = places.search(midloc.getLat(), midloc.getLng(), dist,categories, 50);
		util.mergeList(allPlaces, lstPlace3);

		return allPlaces;	
	}
	
	
	public List<Place> explore(Location loc1, Location loc2) {
		
		List<Place> explorePlaces = new ArrayList<Place>();
		
		midloc = util.midPoint(loc1, loc2);
		int dist = (int) Math.ceil(util.distance(midloc, loc1, 'K') * 1000);
	
		explorePlaces = places.explore(midloc.getLat(), midloc.getLng(), dist, 100);
		
		return explorePlaces;	
	}

	
}
