package model;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed
 */
public class Coordinates implements Cloneable {
	private static  volatile Double maxYAxis=new Double(0);
	
   private double xAxis;
   private  double yAxis;

    public Coordinates(double xAxis, double yAxis) {
        this.xAxis = xAxis;
        this.yAxis=yAxis;
        synchronized (maxYAxis) {
          	 if(this.yAxis>maxYAxis){
               	maxYAxis=this.yAxis;
               }	
   		}
       
    }

    public double getxAxis() {
        return xAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public double getyAxis() {
        return yAxis;
    }

    public void setyAxis(double yAxis) {
    	this.yAxis=yAxis;
    	synchronized (maxYAxis) {
       	 if(this.yAxis>maxYAxis){
            	maxYAxis=this.yAxis;
            }	
		}
    }  
    
    //due to some algorithm orientation , y is increasing up , instead of down , need to revert this so Y will be increasing upwards
    public  double getYAxisReverted(){
    	return Coordinates.maxYAxis-getyAxis();
    }   

    @Override
    protected Coordinates clone() throws CloneNotSupportedException {
        return new Coordinates(xAxis, yAxis);
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash +(int) Math.floor(this.xAxis);
        hash = 13 * hash + (int) Math.floor(this.yAxis);
        return hash;
    }

    
    public boolean equals(Coordinates obj) {
        if (obj == null) {
            return false;
        }        
        final Coordinates other = (Coordinates) obj;
        if (this.xAxis != other.xAxis) {
            return false;
        }
        if (this.yAxis != other.yAxis) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "xAxis=" + xAxis + ", yAxis=" + yAxis + '}';
    }

    
    
    
    
}
