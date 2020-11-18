import java.util.HashMap;
import java.util.Map.Entry;

public class SlowBFS
{
	int x;
	int currX;
	int xLevel;
	int remaining;
	HashMap<Integer, Integer> ancestors;
	HashMap<Integer, Integer> xList;
	Queue<Integer> xQ;
	Digraph G;
	
	public SlowBFS(Digraph G_, int x_){
		x = x_;
		currX = x;
		xLevel = 0;
		remaining = 1;
		ancestors = new HashMap<Integer, Integer>();
		xList = new HashMap<Integer, Integer>();
		xList.put(x, xLevel);
		xQ = new Queue<Integer>();
		xQ.enqueue(x);
		G = G_;
	}
	
	public boolean update(){
		if(remaining == 0){
			return false;
		}
		currX = xQ.dequeue();
		remaining--;
		xList.put(currX, xLevel);
		for(Integer n : G.adj(currX)){
			if(!xList.containsKey(n)){
				xQ.enqueue(n);
			}
		}
		if(remaining == 0){
			xLevel++;
			remaining = xQ.size();
		}
		return true;
	}
	
	public int distance(HashMap<Integer, Integer> yList){
		for(int n : yList.keySet()){
			if(xList.containsKey(n)){
				ancestors.put(n, xList.get(n));
				System.out.println("found "+n+"="+xList.get(n));
				print(xList.entrySet(), "xList");
				print(xQ, "xQ");
				return xList.get(n);
			}
		}
		System.out.println("not found");
		print(xList.entrySet(), "xList");
		print(xQ, "xQ");
		return -1;
	}
	
	public HashMap<Integer, Integer> getAncestors(){
		return ancestors;
	}
	
	public int ancestor(HashMap<Integer, Integer> yList){
		for(int n : yList.keySet()){
			if(xList.containsKey(n)){
				return n;
			}
		}
		return -1;
	}
	
	public HashMap<Integer, Integer> getList(){
		return xList;
	}
	
	public int getLevel(){
		return xLevel; 
	}
	
	public void print(Iterable n, String title){
		System.out.print(title+": [");
		for(Object o : n){
			System.out.print(o+"|");
		}
		System.out.println("]");
	}
}
