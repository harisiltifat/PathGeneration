package de.tum.in.aics.thesis.project.models;


public class Edge {
	 	private final Vertex target;
	    private final double weight;
	    private boolean forwardEdge=false;
	    private boolean backwardEdge=false;
	    	
	    public Edge(Vertex argTarget, double argWeight){ 
	    	target = argTarget;
	    	weight = argWeight; }
		public Vertex getTarget() {
			return target;
		}
		public double getWeight() {
			return weight;
		}
		public boolean isForwardEdge() {
			return forwardEdge;
		}
		public void setForwardEdge(boolean forwardEdge) {
			this.forwardEdge = forwardEdge;
		}
		public boolean isBackwardEdge() {
			return backwardEdge;
		}
		public void setBackwardEdge(boolean backwardEdge) {
			this.backwardEdge = backwardEdge;
		}
}
