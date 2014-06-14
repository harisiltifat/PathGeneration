package de.tum.in.aics.thesis.project.models;

public class Place {
	
	private String name;
	private String geometry;
	private boolean openNow;
	private float rating;
	private String types;
	private Integer stats; 
	private Integer likes;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	
	public boolean getOpenNow() {
		return openNow;
	}

	public void setOpenNow(boolean openNow) {
		this.openNow = openNow;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public Integer getStats() {
		return stats;
	}

	public void setStats(Integer stats) {
		this.stats = stats;
	}
	
	public Integer getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	
}
