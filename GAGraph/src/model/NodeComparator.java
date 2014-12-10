package model;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
		
                 return (new Double(o1.getCoordinates().getxAxis())).compareTo(new Double(o2.getCoordinates().getxAxis()));
       
	}

}
