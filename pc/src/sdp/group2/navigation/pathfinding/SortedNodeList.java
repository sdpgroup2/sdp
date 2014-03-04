package sdp.navigation.pathfinding;

import java.util.ArrayList;
import java.util.Collections;


class SortedNodeList extends ArrayList<Node>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(Node node) {
		boolean temp = super.add(node);
		Collections.sort(this);
                return temp;
	}

}