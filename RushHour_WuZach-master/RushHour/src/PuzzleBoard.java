import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class PuzzleBoard
{
	// Do not change the name or type of this field
	private Vehicle[] idToVehicle;
	private Vehicle[][] coordToVehicle;
	
	// You may add additional private fields here
	
	public PuzzleBoard(Vehicle[] idToVehicleP)
	{
		idToVehicle = new Vehicle[idToVehicleP.length];
		coordToVehicle = new Vehicle[6][6];
		for(int i = 0; i < idToVehicleP.length; i++){
			Vehicle curr = idToVehicleP[i];
			if(curr != null){
				idToVehicle[i] = curr;
				int r = curr.getLeftTopRow();
				int c = curr.getLeftTopColumn();
				int l = curr.getLength();
				coordToVehicle[r][c] = curr;
				if(curr.getIsHorizontal()){
					for(int j = 0; j < l; j++){
						coordToVehicle[r][c+j] = curr;
					}
				}else{
					for(int j = 0; j < l; j++){
						coordToVehicle[r+j][c] = curr;
					}
				}
			}
		}
	}
	
	public PuzzleBoard(Vehicle[] idToVehicleP, Vehicle diff){
		idToVehicle = new Vehicle[idToVehicleP.length];
		coordToVehicle = new Vehicle[6][6];
		for(int i = 0; i < idToVehicleP.length; i++){
			Vehicle curr = idToVehicleP[i];
			if(curr != null){
				if(curr.getId() == diff.getId()){
					curr = diff;
				}
				idToVehicle[i] = curr;
				int r = curr.getLeftTopRow();
				int c = curr.getLeftTopColumn();
				int l = curr.getLength();
				coordToVehicle[r][c] = curr;
				if(curr.getIsHorizontal()){
					for(int j = 0; j < l; j++){
						coordToVehicle[r][c+j] = curr;
					}
				}else{
					for(int j = 0; j < l; j++){
						coordToVehicle[r+j][c] = curr;
					}
				}
			}
		}
	}
	
	public Vehicle getVehicle(int id)
	{
		return idToVehicle[id];
	}

	public Vehicle getVehicle(int row, int column)
	{
		return coordToVehicle[row][column];
	}
	
	public Vehicle[] getIdArray(){
		return idToVehicle;
	}
	
	public int heuristicCostToGoal()
	{
		int t = 0;
		int c = idToVehicle[0].getLeftTopColumn()+1;
		t += 4-c;
		for(; c < 6; c++){
			if(coordToVehicle[2][c] != null){
				t++;
			}
		}
		return t;
	}
	
	public boolean isGoal()
	{
		Vehicle g = getVehicle(2, 5);
		if(g != null && g.getId() == 0){
			return true;
		}
		return false;
	}
	
	public Iterable<PuzzleBoard> getNeighbors()
	{
		ArrayList<PuzzleBoard> a = new ArrayList<PuzzleBoard>();
		for(int i = 0; i < idToVehicle.length; i++){
			Vehicle v = idToVehicle[i];
			if(v != null){
				int l = v.getLength();
				int r = v.getLeftTopRow();
				int c = v.getLeftTopColumn();
				if(v.getIsHorizontal()){
					int e = c+l-1;
					if(c != 0){
						if(coordToVehicle[r][c-1] == null){
							a.add(new PuzzleBoard(idToVehicle, new Vehicle(v.getId(), true, r, c-1, l)));
						}
					}
					if(e != 5){
						if(coordToVehicle[r][e+1] == null){
							a.add(new PuzzleBoard(idToVehicle, new Vehicle(v.getId(), true, r, c+1, l)));
						}
					}
				}else{
					int e = r+l-1;
					if(r != 0){
						if(coordToVehicle[r-1][c] == null){
							a.add(new PuzzleBoard(idToVehicle, new Vehicle(v.getId(), false, r-1, c, l)));
						}
					}
					if(e != 5){
						if(coordToVehicle[e+1][c] == null){
							a.add(new PuzzleBoard(idToVehicle, new Vehicle(v.getId(), false, r+1, c, l)));
						}
					}
				}
			}
		}
		return a;
	}
	
	@Override
	public String toString()
	{
		// You do not need to modify this code, but you can if you really
		// want to.  The automated tests will not use this method, but
		// you may find it useful when testing within Eclipse
		
		String ret = "";
		for (int row=0; row < PuzzleManager.NUM_ROWS; row++)
		{
			for (int col=0; col < PuzzleManager.NUM_COLUMNS; col++)
			{
				Vehicle vehicle = getVehicle(row, col);
				if (vehicle == null)
				{
					ret += " . ";
				}
				else
				{
					int id = vehicle.getId(); 
					ret += " " + id;
					if (id < 10)
					{
						ret += " ";
					}
				}
			}
			ret += "\n";
		}
		
		for (int id = 0; id < PuzzleManager.MAX_NUM_VEHICLES; id++)
		{
			Vehicle v = getVehicle(id);
			if (v != null)
			{
				ret += "id " + v.getId() + ": " + 
						(v.getIsHorizontal() ? "h (" : "v (") + 
						v.getLeftTopRow() + "," + v.getLeftTopColumn() + "), " + v.getLength() + "  \n";
			}
		}
		
		return ret;
	}
	
	@Override
	public int hashCode()
	{
		// DO NOT MODIFY THIS METHOD
		
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(idToVehicle);
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
		
		PuzzleBoard other = (PuzzleBoard) obj;
		if (!Arrays.equals(idToVehicle, other.idToVehicle))
		{
			return false;
		}
		return true;
	}
}
