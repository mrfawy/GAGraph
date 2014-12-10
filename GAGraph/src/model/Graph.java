package model;


import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed
 */
public class Graph implements Cloneable , Comparable<Graph>{
	
	public static final double NODE_SIZE_WEIGHT=1;
	public static final double DIAMETER_WEIGHT=1;
	public static final double AREA_WEIGHT=1;
	
	public static final int EDGE_LENGTH=10;
	
    
    int lastIndex=0;
    boolean visitedBefore=false;
    
    List<Node> nodes=new ArrayList<Node>();
    
    List<Node>convexHullPoints;
    int targetNumberOFNodes;
    
    double fitnessValue=-1;
    double diameter=-1;
    double area=-1;
	double maxBoundingLength=-1;
	int minAngle;
    private List<Line2D>linesConnectingNodes;
    
    public Graph(int targetNumberOFNodes,int minAngle) {  
    	
    	this.targetNumberOFNodes=targetNumberOFNodes;
    	this.minAngle=minAngle;
    	maxBoundingLength=EDGE_LENGTH*(targetNumberOFNodes-1)/Math.sqrt(2);
    }   
    
    
    public boolean hasNodeAtCoordinates(Coordinates coordinates){
        
        return getNodeAtCoordinates(coordinates)!=null;
    }
    public Node getNodeAtCoordinates(Coordinates coordinates){
     Node result=null;
        for(Node n:nodes){
            if(n.getCoordinates().equals(coordinates)){
                result=n;
                break;
            }
        }
        return result;   
    }
    
    @Override
    public int compareTo(Graph g) {
    	
    	if(isFitterThan(g)){
    		return 1;
    	}    	
    	return -1;
    }
    
    
    public boolean isFitterThan(Graph graph){
        if(calcFitnessValue()>=graph.calcFitnessValue() ||(this.getNodes().size()<=targetNumberOFNodes&&this.getNodes().size()>graph.getNodes().size())){
        	return true;
        }
        return false;
    }
    public double getFitnessDiff(Graph graph){
    	return fitnessValue-graph.calcFitnessValue();
    }
    public double calcFitnessValue(){
    	
    	if(fitnessValue==-1){
    		double nodeSizeDiff=targetNumberOFNodes-nodes.size();
    		nodeSizeDiff=getNormalizedValue(nodeSizeDiff, 0, targetNumberOFNodes);
    		double nodeSizePart=0;
    		if(nodes.size()>1){
    			nodeSizePart=nodeSizeDiff==0?1:1/nodeSizeDiff;
    		}
    		
    		
    		
    		double diam=getDiameterNormalzied();    		
    		double diameterPart=diam==0?0:(1/diam);
    		
    		double graphArea=getAreaNormalized();    		
    		double areaPart=graphArea==0?0:(1/graphArea);
    		
    		//normalized = (x-min(x))/(max(x)-min(x))
    		
    		 fitnessValue=NODE_SIZE_WEIGHT*nodeSizePart+DIAMETER_WEIGHT*diameterPart+AREA_WEIGHT*areaPart;
    	}       
        return fitnessValue;
    }
    /**
     * Diameter is defined as max geometric direct line distance
     * @return
     */
    public double getDiameter(){
    	if(diameter!=-1){
    		return diameter;
    	}
    	if(nodes.size()==1){
    		this.diameter=0;
    		return this.diameter;
    	}
    	double maxDiameter=0;
    	for(int i=0;i<nodes.size();i++){
    		for(int j=i;j<nodes.size();j++){
    			if(i==j){
    				continue;
    			}
    			double dist=getDistance(nodes.get(i).getCoordinates(), nodes.get(j).getCoordinates());
    			if(dist>maxDiameter){
    				maxDiameter=dist;
    			}
    		}
    		
    	}
    	diameter=maxDiameter;
    	return diameter;
    }
    public double getDiameterNormalzied(){
    	
    	return getNormalizedValue(getDiameter(), 0, EDGE_LENGTH*(targetNumberOFNodes-1));
    	
    }
    public double getAreaNormalized(){
    	/*max possible area mathematical formula
    	 *  total length = C 
    	 *  construct rectangle with edges X , X , C-2X , convex hull based
    	 *  area = x * c-2x ---> (1)
    	 *  Area' ( derivative for max area) => x= C/4
    	 *  max area (x=C/4) => (C/4)* (C-2(C/4))
    	 *  here C= EDGE_LENGTH*targetNumberOFNodes
    	 *  AKA : the fence problem
    	 *  ref:https://www.youtube.com/watch?v=-SQirLL_pqU	 
    	 */
    	double max = getMaxArea();
    	// min area is one node , assue it takes .25*Edge in all directions 
    	double min =.25*EDGE_LENGTH;
    	return getNormalizedValue(getArea(), min, max);
    	
    }


	private double getMaxArea() {
		double c = EDGE_LENGTH*(targetNumberOFNodes-1);
    	double x=c/4;
    	double max=x*(c-2*x);
		return max;
	}
    
    private double getDistance(Coordinates source,Coordinates dest){
    	double dx=Math.abs(source.getxAxis()-dest.getxAxis());
    	double dy=Math.abs(source.getyAxis()-dest.getyAxis());
    	double distance=Math.sqrt(dx*dx+dy*dy);
    	return distance;
    }
    
    
    
    public List<Node> getConvexHullPoints() {
		if(convexHullPoints==null){
			convexHullPoints=ConvexHull.execute((ArrayList)nodes);
		}
		return convexHullPoints;
	}
    
    /**
     * calculate area as a general irregular polygon , convex hull closed one
     * the mathematical formula is :
     * 0.5*(X0*Y1 + X1*Y2 + ... + XN-1*Y0 - Y0*X1 - Y1*X2 - ... - YN-1*X0)
     * Use Y axis reverted for orientation issues
     * ref :http://www.wikihow.com/Sample/Area-of-an-Irregular-Polygon
     * @return
     */
    		
    private double calculatePolygonArea(){
    	double total=0;
    	
    	
    	if(nodes.size()<3){
    		return 0;
    	}
    	int size=getConvexHullPoints().size();
		for(int i = 0; i < size-1; i++)
        {
            double X1 = (convexHullPoints.get(i).getCoordinates().getxAxis());
            double Y1 = (convexHullPoints.get(i).getCoordinates().getYAxisReverted());
            double X2 = (convexHullPoints.get(i + 1).getCoordinates().getxAxis());
            double Y2 = (convexHullPoints.get(i + 1).getCoordinates().getYAxisReverted());
            total += (X1 * Y2 - Y1 * X2);
        }
		//last point in calculation
    	 double Xn = (convexHullPoints.get(size-1).getCoordinates().getxAxis());
         double Y0 = (convexHullPoints.get(0).getCoordinates().getYAxisReverted());
         double X0 = (convexHullPoints.get(0).getCoordinates().getxAxis());
         double Yn = (convexHullPoints.get(size-1).getCoordinates().getYAxisReverted());
         total += (Xn * Y0 - Yn * X0);
         
        double result= 0.5 * total;
        /*
         * Due to some orientation and directions in vectors , we need abs
         * ref :http://stackoverflow.com/questions/21335091/python-area-of-irregular-polygon-results-in-negative-value
         */
        return Math.abs(result);
    }

	public double getArea(){
    	if(area==-1){
    		area=calculatePolygonArea();
    		//if n<2 or N>3 but strait line with area = zero
    		//use max area , i.e, area[N]=1 -> infavor other graph if exists
    		if(area==0 ||nodes.size()<=2){
    			area=getMaxArea();
    		}
    		
    		
    	}
    	return area;
    }
    //the bounding box area of the graph
    public Rectangle getBoundingArea() {
        Rectangle rect = new Rectangle(getMinXcoordinate(), getMinYcoordinate(), getMaxXcoordinate() - getMinXcoordinate(), getMaxYcoordinate() - getMinYcoordinate());
        return rect;
    } 
    
    private Coordinates adjustCoordinatesFirstQuarter(Coordinates inputCoordinates) {
    	double coordinateX = inputCoordinates.getxAxis();
    	double coordinateY = inputCoordinates.getyAxis();
        //mirror X
        if (coordinateX < 0) {
            coordinateX *= -1;
        }
        //mirror Y
        if (coordinateY < 0) {
            coordinateY *= -1;
        }
        return new Coordinates(coordinateX, coordinateY);
    }
    
    private int getMinXcoordinate() {
    	double minXAxis = Integer.MAX_VALUE;
        
        for (Node n : getNodes()) {
            Coordinates adjustedCoordinates = adjustCoordinatesFirstQuarter(n.getCoordinates());
            if (adjustedCoordinates.getxAxis() < minXAxis) {
                minXAxis = adjustedCoordinates.getxAxis();
            }
        }
        
        return(int) minXAxis;
    }

    private int getMaxXcoordinate() {
    	double maxXAxis = Integer.MIN_VALUE;
    
        for (Node n : getNodes()) {
            Coordinates adjustedCoordinates = adjustCoordinatesFirstQuarter(n.getCoordinates());
            if (adjustedCoordinates.getxAxis() > maxXAxis) {
                maxXAxis = adjustedCoordinates.getxAxis();
            }
        }
    
        return (int)maxXAxis;
    }

    private int getMinYcoordinate() {
        double minYAxis = Integer.MAX_VALUE;
        
        for (Node n : getNodes()) {
            Coordinates adjustedCoordinates = adjustCoordinatesFirstQuarter(n.getCoordinates());
            if (adjustedCoordinates.getyAxis() < minYAxis) {
                minYAxis = adjustedCoordinates.getyAxis();
            }
        }
        
        return (int)minYAxis;
    }

    private int getMaxYcoordinate() {
        double maxYAxis = Integer.MIN_VALUE;
    
        for (Node n : getNodes()) {
            Coordinates adjustedCoordinates = adjustCoordinatesFirstQuarter(n.getCoordinates());
            if (adjustedCoordinates.getyAxis() > maxYAxis) {
                maxYAxis = adjustedCoordinates.getyAxis();
            }
        }
   
        return (int)maxYAxis;
    }
    
    public double getNormalizedValue(double d,double min, double max){
    	
    	return (d-min)/(max-min);
    }
    public  boolean isCreatingIntersection(Line2D line){
    	for(Line2D l:getLinesConnectingNodes()){
    		if(l.intersectsLine(line)){
    			//check to make sure the intersection isn't at the ends 
    			if(line.getP1().equals(l.getP1())||line.getP1().equals(l.getP2())||line.getP2().equals(l.getP1())||line.getP2().equals(l.getP2())){
    				return false;
    			}
    			else{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    private List<Line2D>getLinesConnectingNodes(){
    	if(linesConnectingNodes==null){
    		linesConnectingNodes=new ArrayList<Line2D>();
    		for(Node n:nodes){
    			Coordinates start=n.getCoordinates();
    			for(int i=0;i<n.adjacentNodePlaces.length;i++){
    				if(n.adjacentNodePlaces[i]==1){
    					Coordinates end=n.getAdjacentNodeCoordinatesAtIndex(i);
    					if(this.hasNodeAtCoordinates(end)){
    						linesConnectingNodes.add(new Line2D.Double(start.getxAxis(),start.getyAxis(),end.getxAxis(),end.getyAxis()));
    					}
    					
    				}
    				
    			}
    		}
    	}
    	return linesConnectingNodes;
    
    }
    
    @Override
    public Graph clone() {
        Graph resultGraph=new Graph(targetNumberOFNodes,minAngle);
        
        for(Node n:nodes){            
            resultGraph.getNodes().add(n.clone());
        }
            return resultGraph;
    }
    
    /**
     * Each graph has some footprint , it's defined as number of nodes , number of edges , bounding area , and diameter 
     * the foot print can be the same but mirrored in 4 directions , meaning the same result has been generated before
     * So , save these foot prints and eliminate duplicate graphs ( mirrored versions )
     * #Hash these variables 
     * @return
     */
    public int  getFootPrint(){
    	 int hash = 1;
         hash = hash * 17 + getNodes().size();
         hash = hash * 31 + new Double(getDiameter()).hashCode();
         hash = hash * 13 + new Double(getArea()).hashCode();
    	return hash;
    }
    public int getIncrementLastIndex() {
        lastIndex++;
        return lastIndex;
    }
    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public boolean isVisitedBefore() {
        return visitedBefore;
    }

    public void setVisitedBefore(boolean visitedBefore) {
        this.visitedBefore = visitedBefore;
    }
    


	public int getTargetNumberOFNodes() {
		return targetNumberOFNodes;
	}


	public void setTargetNumberOFNodes(int targetNumberOFNodes) {
		this.targetNumberOFNodes = targetNumberOFNodes;
	}


	public double getFitnessValue() {
		return fitnessValue;
	}


	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}


	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}


	public void setArea(double area) {
		this.area = area;
	}
	


	public double getMaxBoundingLength() {
		return maxBoundingLength;
	}


	public void setMaxBoundingLength(double maxBoundingLength) {
		this.maxBoundingLength = maxBoundingLength;
	}
	

	public int getMinAngle() {
		return minAngle;
	}


	public void setMinAngle(int minAngle) {
		this.minAngle = minAngle;
	}


	@Override
	public String toString() {
		String str= "Graph [lastIndex=" + lastIndex + ", visitedBefore="
				+ visitedBefore + ",targetNumberOFNodes="
				+ targetNumberOFNodes + ", fitnessValue=" + fitnessValue
				+ ", diameter=" + diameter + ", area=" + area + "]"+
				" + nodes:\n ";
		for(Node n:nodes){
			str+=n.toString()+"\n";
		}
		return str;
	}

    
    
    
    
    
    
}
