package model;


import java.util.Queue;

import com.google.common.collect.MinMaxPriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mohamed
 */
public class Heaven {

	
	
	MinMaxPriorityQueue<Graph> graphQueue = null;
	
	public Heaven(int heavenLimit) {
		graphQueue = MinMaxPriorityQueue.maximumSize(heavenLimit).create();
		
	}
   
    //add , sort by the fittest
    public synchronized void addGraphToHeaven(Graph graph) {    	
    	//if(graphQueue.size()<LIMIT){
    		graphQueue.add(graph);
    	//}  	
        
    }  

    public synchronized Graph getAngel() {
        return graphQueue.pollLast();
        //return getDummyAngel();
    }

    public static Graph getDummyAngel() {
    	int minAgnle=45;
        Graph g = new Graph(15,minAgnle);
        Node n1 = new Node(1,minAgnle);
        n1.setCoordinates(new Coordinates(100, 100));
        n1.markAdjacentNodePlaceAtIndex(1, (byte) 1);
        n1.markAdjacentNodePlaceAtIndex(3, (byte) 1);
        n1.markAdjacentNodePlaceAtIndex(5, (byte) 1);
        n1.markAdjacentNodePlaceAtIndex(7, (byte) 1);

        Node n2 = new Node(2,minAgnle);
        n2.setCoordinates(new Coordinates(101, 99));
        n2.markAdjacentNodePlaceAtIndex(2, (byte) 1);

        Node n3 = new Node(3,minAgnle);
        n3.setCoordinates(new Coordinates(101, 98));

        Node n4 = new Node(4,minAgnle);
        n4.setCoordinates(new Coordinates(101, 101));
        n4.markAdjacentNodePlaceAtIndex(4, (byte) 1);

        Node n5 = new Node(5,minAgnle);
        n5.setCoordinates(new Coordinates(102, 101));

        Node n6 = new Node(6,minAgnle);
        n6.setCoordinates(new Coordinates(99, 101));
        n6.markAdjacentNodePlaceAtIndex(6, (byte) 1);

        Node n7 = new Node(7,minAgnle);
        n7.setCoordinates(new Coordinates(99, 102));

        Node n8 = new Node(8,minAgnle);
        n8.setCoordinates(new Coordinates(99, 99));
        n8.markAdjacentNodePlaceAtIndex(0, (byte) 1);

        Node n9 = new Node(9,minAgnle);
        n9.setCoordinates(new Coordinates(98, 99));

        g.getNodes().add(n1);
        g.getNodes().add(n2);
        g.getNodes().add(n3);
        g.getNodes().add(n4);
        g.getNodes().add(n5);
        g.getNodes().add(n6);
        g.getNodes().add(n7);
        g.getNodes().add(n8);
        g.getNodes().add(n9);

        return g;

    }
    public static Graph getDummyAngel2() {
    	int minAgnle=45;
    	Graph g = new Graph(7,minAgnle);
        Node n0 = new Node(0,minAgnle);
        n0.setCoordinates(new Coordinates(100, 100));
        n0.markAdjacentNodePlaceAtIndex(0, (byte) 1);
        n0.markAdjacentNodePlaceAtIndex(2, (byte) 1);
        
        Node n1 = new Node(1,minAgnle);
        n1.setCoordinates(new Coordinates(99, 100));
        n1.markAdjacentNodePlaceAtIndex(0, (byte) 1);
        n1.markAdjacentNodePlaceAtIndex(4, (byte) 1);
        
        Node n2 = new Node(2,minAgnle);
        n2.setCoordinates(new Coordinates(98, 100));
        n2.markAdjacentNodePlaceAtIndex(0, (byte) 1);
        n2.markAdjacentNodePlaceAtIndex(4, (byte) 1);
        
        Node n3 = new Node(3,minAgnle);
        n3.setCoordinates(new Coordinates(97, 100));       
        n3.markAdjacentNodePlaceAtIndex(4, (byte) 1);
        
        Node n4 = new Node(4,minAgnle);
        n4.setCoordinates(new Coordinates(100, 99));
        n4.markAdjacentNodePlaceAtIndex(3, (byte) 1);
        n4.markAdjacentNodePlaceAtIndex(5, (byte) 1);
       
        
        Node n5 = new Node(5,minAgnle);
        n5.setCoordinates(new Coordinates(100, 98));   
        n5.markAdjacentNodePlaceAtIndex(3, (byte) 1);
        n5.markAdjacentNodePlaceAtIndex(6, (byte) 1);
        
        Node n6 = new Node(6,minAgnle);
        n6.setCoordinates(new Coordinates(100, 97));       
        n6.markAdjacentNodePlaceAtIndex(6, (byte) 1);
        
        g.getNodes().add(n0);
        g.getNodes().add(n1);
        g.getNodes().add(n2);
        g.getNodes().add(n3);
        g.getNodes().add(n4);
        g.getNodes().add(n5);
        g.getNodes().add(n6);
        
        return g;
        
    }

	
	
    
    
}
