package de.tum.in.aics.thesis.project.interfaces;

import java.util.List;

import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;


public interface IPathFindAlgorithm {
	List<Place> findPath(Location source, Location destination, List<Place> lstPlaces, float time, float budget);

}
