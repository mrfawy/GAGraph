package model;


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
public class Node implements Cloneable{
    int value;    
    Coordinates coordinates;
    byte[] adjacentNodePlaces;//contain flags for all possible adjacents
    
    private static final int edgeLength=Graph.EDGE_LENGTH;
    private static final double  ratio=edgeLength*(1/(Math.sqrt(2)));
    private int minAngle;
    
   public  Node(int value,int minAngle) {
        this.value=value;
        //default coordinates , may be ovveridded later
        coordinates=new Coordinates(500, 500);
        this.minAngle=minAngle;
        adjacentNodePlaces=new byte[360/minAngle];
    }
    
    public boolean hasAvailableAdjacentNodePlace(){
    	
    	return getNextAvailableAdjacentNodePlaceIndexes()!=null;
      
    }
    
    public List<Integer> getNextAvailableAdjacentNodePlaceIndexes(){    	
    	
    	int lastOneIndex=-1;
    	List<Integer> result=new ArrayList<Integer>();
    	//set index at first one
    	for(int i=0;i<adjacentNodePlaces.length;i++){
    		if(adjacentNodePlaces[i]==1){
    			lastOneIndex=i;
    			
    		}
    	}
        if(lastOneIndex>=adjacentNodePlaces.length-2){
          return null;           
        }
        //no nodes , set next at 0 
        if(lastOneIndex==-1){    		
    		lastOneIndex=0;
        }
        //skip one space , make angle 
        for(int i=lastOneIndex+1;i<adjacentNodePlaces.length;i++){
        	result.add(new Integer(i));
        }
        return result;
       
    }
   
    public void markAdjacentNodePlaceAtIndex(int index,byte mark){
        adjacentNodePlaces[index]=mark;
    }
    
    public Coordinates getAdjacentNodeCoordinatesAtIndex(int index){
    	
		int angleDiff = index * minAngle;
		double deltaXCoordinate = edgeLength * Math.cos(Math.toRadians(angleDiff));
		double deltaYCoordinate = -1*edgeLength * Math.sin(Math.toRadians(angleDiff));//-1 to revert direction as y increases down 
		Coordinates result = null;
		result = new Coordinates(coordinates.getxAxis() + deltaXCoordinate,
				coordinates.getyAxis() + deltaYCoordinate);
		return result;
    }
    
    public Coordinates getAdjacentCoordinatesAtIndex(int nextPlaceIndex){      
       
       Coordinates result = getAdjacentNodeCoordinatesAtIndex(nextPlaceIndex);       
       return result;
    }  

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public byte[] getAdjacentNodePlaces() {
        return adjacentNodePlaces;
    }

    public void setAdjacentNodePlaces(byte[] adjacentNodePlaces) {
        this.adjacentNodePlaces = adjacentNodePlaces;
    }
    
    

    @Override
    protected Node clone() {
        try {
            Node resultNode=new Node(value,minAngle);
            resultNode.setCoordinates(coordinates.clone());
            for(int i=0;i<8;i++){
                resultNode.adjacentNodePlaces[i]=this.adjacentNodePlaces[i];
            }
            return resultNode;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node{" + "value=" + value + ", coordinates=" + coordinates + ", adjacentNodePlaces=" + java.util.Arrays.toString(adjacentNodePlaces) + '}';
    }
    
    
    
   
    
    
}
