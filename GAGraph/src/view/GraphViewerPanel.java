package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Coordinates;
import model.Graph;
import model.Heaven;
import model.Node;
import model.Population;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mohamed
 */
public class GraphViewerPanel extends JPanel implements Runnable,MouseListener {

    Heaven heaven;
    Population population;
    Graph graph;
    Graph currentGraph;
    
    boolean done = false;

    double scalingFactor;

    int nodeWidthPx = 10;
    int marginpx = 15;
    
    int id;

    public GraphViewerPanel(int id ,Heaven heaven, Population population,Graph graph) {
    	this.id=id;
        this.heaven = heaven;
        this.population = population;
        this.graph=graph;     
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {   
    	
        currentGraph =graph;
        
        //background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //border
        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth(), getHeight());
        
        //id
        g.setColor(Color.black);
        g.drawString(""+id,getWidth()/2,getHeight()/2);
        
        if (currentGraph != null) {
        	
            drawGraphNodes((Graphics2D)g);
            drawConvex((Graphics2D)g);
        }
    }

	private void drawGraphNodes(Graphics2D g) {
		for (Node n : currentGraph.getNodes()) {
		    //draw node ( Vertix)
		    g.setColor(Color.red);
		    Coordinates mappedNodeCoordinates = applyTransformation(n.getCoordinates());             
		    g.drawOval((int)(mappedNodeCoordinates.getxAxis() - nodeWidthPx / 2), (int)(mappedNodeCoordinates.getyAxis() - nodeWidthPx / 2), nodeWidthPx, nodeWidthPx);
		    
		    g.drawString(""+currentGraph.calcFitnessValue(), marginpx, marginpx);
		    //draw all adjacent edges
		    g.setColor(Color.BLUE);
		    for (int i = 0; i < 8; i++) {       
		    	if(n.getAdjacentNodePlaces()[i]==1){
		    		 Coordinates targetCoordinate = n.getAdjacentNodeCoordinatesAtIndex(i) ;               
		             
		             if (targetCoordinate != null) {
		                 Coordinates mappedTargetCoordinate = applyTransformation(targetCoordinate);		                 
		                 g.setStroke(new BasicStroke(3));
		                 g.drawLine((int)Math.floor(mappedNodeCoordinates.getxAxis()), (int)Math.floor(mappedNodeCoordinates.getyAxis()), (int)Math.floor(mappedTargetCoordinate.getxAxis()),(int) Math.floor(mappedTargetCoordinate.getyAxis()));
		             }
		    	}
		       

		        

		    }

		}
	}
    
    private void drawConvex(Graphics2D g){
    	int size =currentGraph.getConvexHullPoints().size();
   	 	if(size<3){
   	 		return;
   	 	}    	
    	g.setColor(Color.green);
    	float  dash[] = {10.0f};
    	 Stroke thindashed =new BasicStroke(1.0f,
                 BasicStroke.CAP_BUTT,
                 BasicStroke.JOIN_MITER,
                 10.0f, dash, 0.0f);
    	 g.setStroke(thindashed);
    	 
    	 //draw from po - pn
    	 for(int i=0;i<size-1;i++){
    		 Coordinates p =applyTransformation(currentGraph.getConvexHullPoints().get(i).getCoordinates());
    		 Coordinates q =applyTransformation(currentGraph.getConvexHullPoints().get(i+1).getCoordinates());    		 
    		 g.drawLine((int)p.getxAxis(), (int)p.getyAxis(),(int)q.getxAxis(), (int)q.getyAxis());
    		 
    		 //mark point
             g.drawOval((int)p.getxAxis() - nodeWidthPx / 2, (int)p.getyAxis() - nodeWidthPx / 2, nodeWidthPx, nodeWidthPx);
    		
    	 }
    	 //close the convex to p0 again
    	 Coordinates p =applyTransformation(currentGraph.getConvexHullPoints().get(size-1).getCoordinates());
		 Coordinates q =applyTransformation(currentGraph.getConvexHullPoints().get(0).getCoordinates());
		 g.drawLine((int)p.getxAxis(), (int)p.getyAxis(),(int)q.getxAxis(), (int)q.getyAxis());
		 
    	 
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

    private Coordinates applyTransformation(Coordinates inputCoordinates) {
        Coordinates adjustedCoordinates = adjustCoordinatesFirstQuarter(inputCoordinates);      
        Coordinates translatedCoor = calculateTranslation(adjustedCoordinates);
        Coordinates scaledCoordinates=calculateScaling(translatedCoor);
        
        //shift for margin
        Coordinates adjustedMarginCoor=new Coordinates(scaledCoordinates.getxAxis()+marginpx, scaledCoordinates.getyAxis()+marginpx);
        
        
        return adjustedMarginCoor;
        
    }

   

    private Coordinates calculateTranslation(Coordinates inputCoordinates) {
        Rectangle rect = currentGraph.getBoundingArea();
        int x = (int) Math.floor(inputCoordinates.getxAxis() - rect.getX());
        int y = (int) Math.floor(inputCoordinates.getyAxis() - rect.getY());
        Coordinates resultCoordinates = new Coordinates(x, y);
        return resultCoordinates;
    }
   

    private Coordinates calculateScaling(Coordinates inputCoordinates) {
        Rectangle rect = currentGraph.getBoundingArea();
        double scaleFactor = determineScaleFactor(rect.width, rect.height, getWidth()-2*marginpx, getHeight()-2*marginpx);
        Coordinates resultCoordinates = new Coordinates((int) Math.floor(inputCoordinates.getxAxis() * scaleFactor), (int) Math.floor(inputCoordinates.getyAxis() * scaleFactor));
        return resultCoordinates;
    }

    

    private double determineScaleFactor(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
    	double scalex = (double) targetWidth / sourceWidth;
        double scaley = (double) targetHeight / sourceHeight;
        return Math.min(scalex, scaley);
    }

   @Override
public void mouseClicked(MouseEvent arg0) {
	  
	
}
    

    @Override
    public void run() {
        while (true) {
            currentGraph = heaven.getAngel();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		String msg="FitnessValue :"+currentGraph.calcFitnessValue()+"\n" +			
		"Diameter:"+currentGraph.getDiameter()+"\n"+
		"Diameter[N]:"+currentGraph.getDiameterNormalzied()+"\n"+
		"Area:"+currentGraph.getArea()+"\n"+
		"Area[N]:"+currentGraph.getAreaNormalized()+"\n"+
		"BoundingArea :"+currentGraph.getBoundingArea()+"\n"+
		"Max maxBoundingLength :"+currentGraph.getMaxBoundingLength()+"\n"+
		"LastIndex:"+currentGraph.getLastIndex()+"\n"+
		"Graph: \n"+currentGraph.toString();
		
		 JOptionPane.showMessageDialog(this.getParent(), msg,"Solution Statistics",JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
