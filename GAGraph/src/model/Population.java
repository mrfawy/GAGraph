package model;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed
 */
public class Population {
    
	PriorityBlockingQueue<Graph> graphQueue=new PriorityBlockingQueue<Graph>(100) ;
	
	private Set<Integer> populationFootPrints=new HashSet<Integer>();
    
    public void addGraphToPopulation(Graph graph){
    	if(!isGraphInPopulation(graph)){
    		 graphQueue.add(graph);
    	}
       
    }
    public Graph getNextGraph(){
        return graphQueue.poll();
    }
    
    /**
     * if not exists it'll register it 
     * @param g
     * @return
     */
    public synchronized boolean isGraphInPopulation(Graph g){
    	boolean exists=populationFootPrints.contains(g.getFootPrint());
    	if(!exists){
    		populationFootPrints.add(g.getFootPrint());
    	}
    	return exists;
    }
}
