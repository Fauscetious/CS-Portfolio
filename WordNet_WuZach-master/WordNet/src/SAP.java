import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class SAP
{
	BreadthFirstDirectedPaths B;
	Digraph G;
	
    public SAP(Digraph G)
    {
    	this.G = G;
    }

    public int length(int v, int w)
    {
    	if(v < 0 || v > G.V() || w < 0 || w > G.V()){
    		throw new IndexOutOfBoundsException();
    	}
    	/*
    	SlowBFS vB = new SlowBFS(G, v);
    	SlowBFS wB = new SlowBFS(G, w);
    	int key = intersect(vB, wB);//
    	return vB.getAncestors().get(key)+wB.getAncestors().get(key);
    	*/
    	BreadthFirstDirectedPaths vB = new BreadthFirstDirectedPaths(G, v);
    	BreadthFirstDirectedPaths wB = new BreadthFirstDirectedPaths(G, w);
    	return intersect(vB, wB, v, w)[1];
    }

    public int ancestor(int v, int w)
    {
    	if(v < 0 || v > G.V() || w < 0 || w > G.V()){
    		throw new IndexOutOfBoundsException();
    	}
    	/*
    	SlowBFS vB = new SlowBFS(G, v);
    	SlowBFS wB = new SlowBFS(G, w);
    	*/
    	BreadthFirstDirectedPaths vB = new BreadthFirstDirectedPaths(G, v);
    	BreadthFirstDirectedPaths wB = new BreadthFirstDirectedPaths(G, w);
    	return intersect(vB, wB, v, w)[0];
    }
    
    private int[] intersect(BreadthFirstDirectedPaths xB, BreadthFirstDirectedPaths yB, int x, int y){
    	Queue<Integer> xQ = new Queue<Integer>();
    	Queue<Integer> yQ = new Queue<Integer>();
    	HashSet<Integer> xList = new HashSet<Integer>();
    	HashSet<Integer> yList = new HashSet<Integer>();
    	xQ.enqueue(x);
    	yQ.enqueue(y);
    	xList.add(x);
    	yList.add(y);
    	HashMap<Integer, Integer> ancestorList = new HashMap<Integer, Integer>();
    	int[] ancestor = new int[2];
    	ancestor[0] = -1;
    	ancestor[1] = 2147483647;
    	while(true){
    		for(int n : ancestorList.keySet()){
				if(xB.hasPathTo(n)){
					int length = xB.distTo(n)+yB.distTo(n);
					if(length < ancestor[1]){
						ancestor[0] = n;
						ancestor[1] = length;
					}
				}
			}
    		ancestorList.clear();
    		for(int test : xQ){
    			if(yB.hasPathTo(test)){
    				ancestorList.put(test, yB.distTo(test));
    			}
    		}
			int ySize = yQ.size();
    		for(int i = 0; i < ySize; i++){
    			int k = yQ.dequeue();
    			for(int n : G.adj(k)){
        			if(!yList.contains(n)){
            			yQ.enqueue(n);
            			yList.add(n);
        			}
    			}
    		}
    		for(int n : ancestorList.keySet()){
				if(yB.hasPathTo(n)){
					int length = xB.distTo(n)+yB.distTo(n);
					if(length < ancestor[1]){
						ancestor[0] = n;
						ancestor[1] = length;
					}
				}
			}
    		ancestorList.clear();
    		for(int test : yQ){
    			if(xB.hasPathTo(test)){
    				ancestorList.put(test, xB.distTo(test));
    			}
    		}
			int xSize = xQ.size();
			for(int i = 0; i < xSize; i++){
				int k = xQ.dequeue();
				for(int n : G.adj(k)){
					if(!xList.contains(n)){
	        			xQ.enqueue(n);
	        			xList.add(n);
	    			}
				}
			}
    		if(xQ.size() == 0 && yQ.size() == 0 && ancestorList.size() == 0){
    			if(ancestor[1] == 2147483647){
    				ancestor[1] = -1;
    			}
    			return ancestor;
    		}
    	}
    }
    
    /*
    private int intersect(SlowBFS xB, SlowBFS yB){
    	boolean searchX = true;
    	boolean searchY = true;
    	while(true){
			("began v");
			if(searchX){
    			searchX = xB.update();
			}
			xB.distance(yB.getList());
    		("began w");
    		if(searchY){
        		searchY = yB.update();
    		}
    		yB.distance(xB.getList());
    		if(!searchX && !searchY){
    			break;
    		}
    	}
    	HashMap<Integer, Integer> xList = xB.getAncestors();
    	HashMap<Integer, Integer> yList = yB.getAncestors();
    	int smallest = -1;
    	for(int n : xList.keySet()){
    		("smallest: "+smallest);
    		int temp = xList.get(n) + yList.get(n);
    		("temp: "+n+", "+temp);
    		if(smallest == -1){
    			smallest = n;
    		}else if(temp < (xList.get(smallest)+yList.get(smallest))){
    			smallest = n;
    		}
    	}
    	return smallest;
    	
    }
    */

    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
    	if(v == null || w == null){
    		throw new NullPointerException();
    	}
    	/*
    	int smallest = 2147483647;
    	for(int i : v){
    		for(int j : w){
    			SlowBFS vB = new SlowBFS(G, i);
    			SlowBFS wB = new SlowBFS(G, j);
    			int key = intersect(vB, wB);
    			int curr = vB.getAncestors().get(key)+wB.getAncestors().get(key);
    			if(curr < smallest){
    				smallest = curr;
    				if(smallest == 0){
    					return 0;
    				}
    			}
    		}
    	}
    	if(smallest == 2147483647){
    		return -1;
    	}
    	return smallest;
    	*/
    	int smallest = 2147483647;
    	for(int i : v){
    		for(int j : w){
    			BreadthFirstDirectedPaths vB = new BreadthFirstDirectedPaths(G, i);
    			BreadthFirstDirectedPaths wB = new BreadthFirstDirectedPaths(G, j);
    			int[] ancestor = intersect(vB, wB, i, j);
    			if(ancestor[1] < smallest){
    				smallest = ancestor[1];
    				if(smallest == 0){
    					return 0;
    				}
    			}
    		}
    	}
    	if(smallest == 2147483647){
    		return -1;
    	}
    	return smallest;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
    	if(v == null || w == null){
    		throw new NullPointerException();
    	}
    	/*
    	int ancestor = -1;
    	int smallest = 2147483647;
    	for(int i : v){
    		for(int j : w){
    			SlowBFS vB = new SlowBFS(G, i);
    			SlowBFS wB = new SlowBFS(G, j);
    			int key = intersect(vB, wB);
    			int curr = vB.getAncestors().get(key)+wB.getAncestors().get(key);
    			if(curr < smallest){
    				smallest = curr;
    				ancestor = key; 
    				if(smallest == 0){
    					return ancestor;
    				}
    			}
    		}
    	}
    	return ancestor;
    	*/
    	int[] ancestor = new int[2];
    	ancestor[0] = -1;
    	ancestor[1] = 2147483647;
    	for(int i : v){
    		for(int j : w){
    			BreadthFirstDirectedPaths vB = new BreadthFirstDirectedPaths(G, i);
    			BreadthFirstDirectedPaths wB = new BreadthFirstDirectedPaths(G, j);
    			int[] newAncestor = intersect(vB, wB, i, j);
    			if(newAncestor[1] < ancestor[1]){
    				ancestor = newAncestor;
    				if(ancestor[1] == 0){
    					return ancestor[0];
    				}
    			}
    		}
    	}
    	return ancestor[0];
    }

    public static void main(String[] args)
    {
    	String digraphFile = "testInput/digraph5.txt";
    	
        In in = new In(digraphFile);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty())
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
