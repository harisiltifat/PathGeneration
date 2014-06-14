package de.tum.in.aics.thesis.project.interfaces;
import java.util.List;

import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;


public interface ISearchService {
	List<Place> search(Location loc1, Location loc2);
	List<Place> explore(Location loc1, Location loc2);
}
