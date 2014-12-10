package model;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.List;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mohamed
 */
public class WorkerThread implements Runnable {
	
	int id;
    Population population;
    Heaven heaven;
    private Graph currentWorkGraph;

    static boolean terminateCondition = false;
    static final double MIN_FITNESS_DIFF=.001;
    static final int MIN_NODE_DIFF=0;
    
    private int sleepPeriod=(int)Math.random()*100;
    


    public WorkerThread(int id,Population population, Heaven heaven) {
        this.population = population;
        this.heaven = heaven;
        this.id=id;
    }

    @Override
    public void run() {
        do {
            currentWorkGraph = population.getNextGraph();
            try {
				doWork();
				Thread.sleep(sleepPeriod);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } while (currentWorkGraph != null && !terminateCondition);
        
    }

    private void doWork() throws InterruptedException {
    	
    	 if (currentWorkGraph == null) {
         	return;
         }
        
       
        if(!heaven.graphQueue.isEmpty()&&population.graphQueue.isEmpty()){
    		terminateCondition = true;
            System.out.println("WORK DONE");
            System.out.println("Population Size:"+population.graphQueue.size());
           
           
    	}
       
        //it was visited and fit couldn't improve any more ==> move to heaven
        if (currentWorkGraph.isVisitedBefore()||currentWorkGraph.targetNumberOFNodes-currentWorkGraph.nodes.size()<=MIN_NODE_DIFF) {
        	 heaven.addGraphToHeaven(currentWorkGraph);          
            
            return;
        }
        boolean offSpringFound = false;
        for (Node n : currentWorkGraph.getNodes()) {
            Node nodeToBeMarked = n.clone();    
           
            if (!terminateCondition&& nodeToBeMarked.hasAvailableAdjacentNodePlace()) {
            	
            	List<Integer> nextAvailableAdjacentNodePlaceIndexes = nodeToBeMarked.getNextAvailableAdjacentNodePlaceIndexes();
				
				for(Integer nextAvailableAdjacentIndex:nextAvailableAdjacentNodePlaceIndexes){            		
            		 
                     Coordinates coord = nodeToBeMarked.getAdjacentNodeCoordinatesAtIndex(nextAvailableAdjacentIndex);
                     //mark as some adjacent vertix is there 
                     if (!currentWorkGraph.hasNodeAtCoordinates(coord)) {
                         //nodeToBeMarked.markAdjacentNodePlaceAtIndex(nextAvailableAdjacentIndex, (byte) 1);
                    	 
                    	 //check if nodes creates a line intersection with any graph line
                    	 //if so , ignore this node
                    	 Double line = new Line2D.Double(coord.getxAxis(),coord.getyAxis(),n.getCoordinates().getxAxis(),n.getCoordinates().getyAxis());
                    	 if(!currentWorkGraph.isCreatingIntersection(line)){
                    		 Graph childGraph = currentWorkGraph.clone();
                             childGraph.setLastIndex(currentWorkGraph.getLastIndex());
                             Node mirrorNode = childGraph.getNodeAtCoordinates(n.getCoordinates());
                             //mark adjacent place as Used==1
                             nodeToBeMarked.markAdjacentNodePlaceAtIndex(nextAvailableAdjacentIndex, (byte) 1);
                             mirrorNode.markAdjacentNodePlaceAtIndex(nextAvailableAdjacentIndex, (byte) 1);
                             //clone offspring
                             Node newNode = new Node(childGraph.getIncrementLastIndex(),currentWorkGraph.getMinAngle());
                             newNode.setCoordinates(coord);
                             childGraph.getNodes().add(newNode);
                             
                             // check fitness
                             currentWorkGraph.calcFitnessValue();
                             childGraph.calcFitnessValue();                    
                             if (childGraph.isFitterThan(currentWorkGraph)) {
                             	offSpringFound = true;
                                 population.addGraphToPopulation(childGraph);
                                 Thread.sleep(sleepPeriod);
                                 
                             	
                                 
                             }
                    	 }
                         
                     }
            		
            	}
            	
                               
            }

        }
        if (!offSpringFound) {
            currentWorkGraph.setVisitedBefore(true);
            population.addGraphToPopulation(currentWorkGraph);            
        }
        

    }
    
    

    public static boolean isTerminateCondition() {
        return terminateCondition;
    }

    public static void setTerminateCondition(boolean terminateCondition) {
        WorkerThread.terminateCondition = terminateCondition;
    }
    
    

}
