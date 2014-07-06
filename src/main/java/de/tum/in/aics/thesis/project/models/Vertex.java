package de.tum.in.aics.thesis.project.models;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.aics.thesis.project.models.Place;



public class Vertex implements Comparable<Vertex> {
		private  String name;
	    private List<Edge> adjacencies=new ArrayList<Edge>();
	    private double minDistance = Double.POSITIVE_INFINITY;
	    private double maxEntertainment =0;
	    private double distanceFromSource=Double.POSITIVE_INFINITY;
	    private Vertex previous;
	    private String geometry;
	    private double lat;
	    private double lng;
	    private float rating;
	    private int likes;
	    private int stats;
	    private String types;
	    private boolean openNow;
	    private boolean explored=false;
	    
	    public String toString() { return getName(); }
	    
	    public Vertex(String argName) 
	    { setName(argName); }
	    
	    public int compareTo(Vertex other)
	    {
	        return Double.compare(getDistanceFromSource(), other.getDistanceFromSource());
	    }
		public int getLikes() {
			return likes;
		}
		public void setLikes(int likes) {
			this.likes = likes;
		}
		public int getStats() {
			return stats;
		}
		public void setStats(int stats) {
			this.stats = stats;
		}
		public String getTypes() {
			return types;
		}
		public void setTypes(String types) {
			this.types = types;
		}
		public boolean getOpenNow() {
			return openNow;
		}
		public void setOpenNow(boolean openNow) {
			this.openNow = openNow;
		}

		public String getGeometry() {
			return geometry;
		}

		public void setGeometry(String geometry) {
			this.geometry = geometry;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public float getRating() {
			return rating;
		}

		public void setRating(float rating) {
			this.rating = rating;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Edge> getAdjacencies() {
			return adjacencies;
		}

		public void setAdjacencies(List<Edge> adjacencies) {
			this.adjacencies = adjacencies;
		}

		public double getMinDistance() {
			return minDistance;
		}

		public void setMinDistance(double minDistance) {
			this.minDistance = minDistance;
		}

		public Vertex getPrevious() {
			return previous;
		}

		public void setPrevious(Vertex previous) {
			this.previous = previous;
		}

		public double getMaxEntertainment() {
			return maxEntertainment;
		}

		public void setMaxEntertainment(double maxEntertainment) {
			this.maxEntertainment = maxEntertainment;
		}

		public double getDistanceFromSource() {
			return distanceFromSource;
		}

		public void setDistanceFromSource(double distanceFromSource) {
			this.distanceFromSource = distanceFromSource;
		}

		public boolean isExplored() {
			return explored;
		}

		public void setExplored(boolean explored) {
			this.explored = explored;
		}

}
