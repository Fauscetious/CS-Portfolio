import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Solver
{
	private UpdateableMinPQ<SearchNode> Q;
	private SearchNode S;
	private PuzzleBoard I;
	private HashMap<PuzzleBoard, Integer> O;
	private HashSet<PuzzleBoard> C;
	
	private static class SearchNode implements Comparable<SearchNode>
	{
		// Important!! Do not change the names or types of these fields!
		private PuzzleBoard board;
		private int costFromBeginningToHere;
		private SearchNode previous;

		public SearchNode(PuzzleBoard b, SearchNode p){
			board = b;
			if(p == null){
				costFromBeginningToHere = 0;
			}else{
				costFromBeginningToHere = p.costFromBeginningToHere+1;	
			}
			previous = p;
		}
		
		public int compareTo(SearchNode that)
		{
			return this.board.heuristicCostToGoal() - that.board.heuristicCostToGoal();
		}

		@Override
		public int hashCode()
		{
			// DO NOT MODIFY THIS METHOD

			final int prime = 31;
			int result = 1;
			result = prime * result + ((board == null) ? 0 : board.hashCode());
			result = prime * result + costFromBeginningToHere;
			result = prime * result + ((previous == null) ? 0 : previous.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			// DO NOT MODIFY THIS METHOD

			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			SearchNode other = (SearchNode) obj;
			if (board == null)
			{
				if (other.board != null)
				{
					return false;
				}
			} 
			else if (!board.equals(other.board))
			{
				return false;
			}
			if (costFromBeginningToHere != other.costFromBeginningToHere)
			{
				return false;
			}
			if (previous == null)
			{
				if (other.previous != null)
				{
					return false;
				}
			}
			else if (!previous.equals(other.previous))
			{
				return false;
			}
			return true;
		}
	}

	public Solver(PuzzleBoard initial)
	{
		I = initial;
		Q = new UpdateableMinPQ<SearchNode>();
		Q.insert(new SearchNode(initial, null));
		SearchNode f = null;
		O = new HashMap<PuzzleBoard, Integer>();
		C = new HashSet<PuzzleBoard>();
		while(Q.size() > 0){
			SearchNode d = Q.delMin();
			O.remove(d.board);
			C.add(d.board);
			if(d.board.isGoal()){
				f = d;
				break;
			}else{
				ArrayList<PuzzleBoard> l = (ArrayList<PuzzleBoard>) d.board.getNeighbors();
				for(PuzzleBoard b : l){
					if(!C.contains(b)){
						if(O.containsKey(b)){
							if(d.costFromBeginningToHere < O.get(b)){
								O.put(b, d.costFromBeginningToHere);
							}
						}else{
							O.put(b, d.costFromBeginningToHere);
							Q.insert(new SearchNode(b, d));
						}
					}
				}
			}
		}
		S = f;
	}

	public Solver(PuzzleBoard initial, boolean extraCredit)
	{
		// DO NOT TOUCH unless you are passing all of the tests and wish to
		// attempt the extra credit.
		throw new UnsupportedOperationException();
	}

	public Iterable<PuzzleBoard> getPath()
	{
		ArrayList<PuzzleBoard> a = new ArrayList<PuzzleBoard>();
		SearchNode n = S;
		a.add(I);
		while(n.previous != null){
			PuzzleBoard y = new PuzzleBoard(n.board.getIdArray());
			a.add(1, y);
			n = n.previous;
		}
		return a;
	}
}
