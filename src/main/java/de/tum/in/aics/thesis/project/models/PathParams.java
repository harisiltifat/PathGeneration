package de.tum.in.aics.thesis.project.models;

import java.util.ArrayList;
import java.util.List;


public class PathParams {
	public List<Vertex> lstPath=new ArrayList<Vertex>();
	private float maxEntertainment;
	private float maxCost;
	public float getMaxEntertainment() {
		return maxEntertainment;
	}
	public void setMaxEntertainment(float entertainment) {
		this.maxEntertainment = entertainment;
	}
	public float getMaxCost() {
		return maxCost;
	}
	public void setMaxCost(float cost) {
		this.maxCost = cost;
	}
}
