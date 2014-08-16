package de.tum.in.aics.thesis.project.util;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.aics.thesis.project.models.Location;
import de.tum.in.aics.thesis.project.models.Place;

public class Utilities {

	static Location midloc = new Location(0, 0);

	public static Location midPoint(Location loc1, Location loc2) {
		
		double dLon = Math.toRadians(loc2.getLng() - loc1.getLng());
		double lat1 = Math.toRadians(loc1.getLat());
		double lat2 = Math.toRadians(loc2.getLat());
		double lng1 = Math.toRadians(loc1.getLng());

		double Bx = Math.cos(lat2) * Math.cos(dLon);
		double By = Math.cos(lat2) * Math.sin(dLon);
		midloc.setLat(Math.atan2(Math.sin(lat1) + Math.sin(lat2),Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)));
		midloc.setLng(lng1 + Math.atan2(By, Math.cos(lat1) + Bx));
		midloc.setLat(Math.toDegrees(midloc.getLat()));
		midloc.setLng(Math.toDegrees(midloc.getLng()));
		return midloc;
	}

	public static double distance(Location loc1, Location loc2, char unit) {
		double theta = loc1.getLng() - loc2.getLng();
		double dist = Math.sin(deg2rad(loc1.getLat())) * Math.sin(deg2rad(loc2.getLat())) + Math.cos(deg2rad(loc1.getLat())) * Math.cos(deg2rad(loc2.getLat())) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static List<Place> mergeList(List<Place> allList, List<Place> list) {
		List<Place> tmpList = new ArrayList<Place>();
		for (Place place : list) {
			boolean exist = false;
			for (Place place2 : allList) {
				if (place.getName().equalsIgnoreCase(place2.getName()))
					exist = true;
			}
			if (!exist)
				tmpList.add(place);
		}

		allList.addAll(tmpList);
		return allList;
	}
}
