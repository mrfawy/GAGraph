package view;


import model.Graph;
import model.WorkerThread;
import model.Heaven;
import model.Population;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.EmptyBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mohamed
 */
public class GUIThread implements Runnable {

    Heaven heaven;
    Population population;
    int drawlimit;

    public GUIThread(Heaven Heaven, Population population,int drawLimit) {
        this.heaven = Heaven;
        this.population = population;
        this.drawlimit=drawLimit;
    }

    @Override
    public void run() {
        
        while(!WorkerThread.isTerminateCondition()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GUIThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MainFrame mainFrame = new MainFrame();
        mainFrame.init();
        for(int i=0;i<drawlimit;i++){
        	Graph g=heaven.getAngel();   
        	if(g==null){
        		break;
        	}
        GraphViewerPanel heavenViewerPanel = new GraphViewerPanel(i,heaven, population,g);    
        //heavenViewerPanel.setBorder(new EmptyBorder(15, 15, 15, 15) );
        mainFrame.add(heavenViewerPanel);       
        }
//        mainFrame.setTitle("SOLN:["+i+"]");
        mainFrame.setVisible(true);
    }

}
