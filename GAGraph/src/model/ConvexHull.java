package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * based of Jarvi's present wrapping algorithm for convex hull 
 * @author abdelm2
 *
 */
public class ConvexHull {
	
	private static boolean CCW(Coordinates  p, Coordinates q, Coordinates r)
    {
        double val = ((q.getYAxisReverted() - p.getYAxisReverted()) * (r.getxAxis() - q.getxAxis())) - ((q.getxAxis() - p.getxAxis()) * (r.getYAxisReverted() - q.getYAxisReverted()));
 
         if (val >= 0)
             return false;
         return true;
    }
	public static List<Node> sortCCW(List<Node> points){
		Collections.sort(points,new NodeComparator());
		System.out.println("");
		return points;
	}
	
	
	//refer to : 
	//https://code.google.com/p/convex-hull/source/browse/Convex+Hull/src/algorithms/FastConvexHull.java?r=4
    public static List<Node> execute(ArrayList<Node> points) 
    {
    	
            List<Node> xSorted = (ArrayList<Node>)points.clone();
            Collections.sort(xSorted, new NodeComparator());
            
            int n = xSorted.size();
            
            Node[] lUpper = new Node[n];
            
            lUpper[0] = xSorted.get(0);
            lUpper[1] = xSorted.get(1);
            
            int lUpperSize = 2;
            
            for (int i = 2; i < n; i++)
            {
                    lUpper[lUpperSize] = xSorted.get(i);
                    lUpperSize++;
                    
                    while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1]))
                    {
                            // Remove the middle point of the three last
                            lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
                            lUpperSize--;
                    }
            }
            
            Node[] lLower = new Node[n];
            
            lLower[0] = xSorted.get(n - 1);
            lLower[1] = xSorted.get(n - 2);
            
            int lLowerSize = 2;
            
            for (int i = n - 3; i >= 0; i--)
            {
                    lLower[lLowerSize] = xSorted.get(i);
                    lLowerSize++;
                    
                    while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1]))
                    {
                            // Remove the middle point of the three last
                            lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
                            lLowerSize--;
                    }
            }
            
            ArrayList<Node> result = new ArrayList<Node>();
            
            for (int i = 0; i < lUpperSize; i++)
            {
                    result.add(lUpper[i]);
            }
            
            for (int i = 1; i < lLowerSize - 1; i++)
            {
                    result.add(lLower[i]);
            }
            
            return result;
    }
    
    private static boolean rightTurn(Node a, Node b, Node c)
    {
            return (b.getCoordinates().getxAxis() - a.getCoordinates().getxAxis())*(c.getCoordinates().getYAxisReverted() - a.getCoordinates().getYAxisReverted()) - (b.getCoordinates().getYAxisReverted() - a.getCoordinates().getYAxisReverted())*(c.getCoordinates().getxAxis() - a.getCoordinates().getxAxis()) > 0;
    }
   
    /*public static List<Node> GetconvexHull(List<Node> points)
    {
    	List<Node>  result=new ArrayList<Node>();
        int n = points.size();
        *//** if less than 3 points return **//*        
        if (n < 3) {
        	for(int i=0;i<points.size()-1;i++){
        		result.add(points.get(i));
        		
        	}
        	return result;     
        }
            
        int[] next = new int[n];
        Arrays.fill(next, -1);
 
        *//** find the leftmost point **//*
        int leftMost = 0;
        for (int i = 0; i < n; i++){
        	if (points.get(i).getCoordinates().getxAxis() < points.get(leftMost).getCoordinates().getxAxis())
                leftMost = i;
			if (points.get(i).getCoordinates().getxAxis() == points
					.get(leftMost).getCoordinates().getxAxis()
					&& points.get(i).getCoordinates().getYAxisReverted() > points
							.get(leftMost).getCoordinates().getYAxisReverted()) {
				 leftMost = i;
			}
        }
            
        int p = leftMost, q;
        *//** iterate till p becomes leftMost **//*
        do
        {
            *//** wrapping **//*
            q = (p + 1) % n;
            for (int i = 0; i < n; i++){
              if (CCW(points.get(p).getCoordinates(), points.get(i).getCoordinates(), points.get(q).getCoordinates())){
                 q = i;                
              }
            }
 
            next[p] = q;  
            result.add(points.get(q));
            p = q; 
        } while (p != leftMost);
 
       
        
        return result;
            
               
    }*/
    
   
    public void display(Coordinates[] points, int[] next)
    {
        System.out.println("\nConvex Hull points : ");
        for (int i = 0; i < next.length; i++)
            if (next[i] != -1)
               System.out.println("("+ points[i].getxAxis() +", "+ points[i].getyAxis() +")");
    }
   /* *//** Main function **//*
    public static void main (String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Jarvis Algorithm Test\n");
        *//** Make an object of Jarvis class **//*
        Jarvis j = new Jarvis();        
 
        System.out.println("Enter number of points n :");
        int n = scan.nextInt();
        Point[] points = new Point[n];
        System.out.println("Enter "+ n +" x, y cordinates");
        for (int i = 0; i < n; i++)
        {
            points[i] = new Point();
            points[i].x = scan.nextInt();
            points[i].y = scan.nextInt();
        }        
        j.convexHull(points);        
    }*/
}
