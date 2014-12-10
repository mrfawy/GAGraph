package main;


import model.WorkerThread;
import view.GUIThread;
import model.Heaven;
import model.Population;
import model.Node;
import model.Graph;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mohamed
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int THREAD_NUMBER = 20;
        int TARGET_NUMBER_OF_NODES=4;
        int MIN_ANGLE=45;
        int HEAVEN_LIMIT=999999;
        int DRAW_LIMIT=10;
        Population population = new Population();
       Graph adamGraph = new Graph(TARGET_NUMBER_OF_NODES,MIN_ANGLE);
        adamGraph.getNodes().add(new Node(adamGraph.getIncrementLastIndex(),MIN_ANGLE));
       // Graph adamGraph = getInitGraph();
  
        population.addGraphToPopulation(adamGraph);
        Heaven heaven = new Heaven(HEAVEN_LIMIT);

        for (int i = 0; i < THREAD_NUMBER; i++) {
            WorkerThread worker1 = new WorkerThread(i,population, heaven);
            Thread th1 = new Thread(worker1);
            th1.start();
        }

      //start GUI
        SwingUtilities.invokeLater(new GUIThread(heaven, population,DRAW_LIMIT));

    }
    
    public static Graph getInitGraph(){
    	Graph g = Heaven.getDummyAngel2();
    	g.setVisitedBefore(true);
    	return g;
    }
}
