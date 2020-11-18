import java.awt.Color;

public class SeamCarver
{
	private SmC_Picture P;
	private final int R;
	private final int G;
	private final int B;
	private int T;
	private double[][] E;
	
	public SeamCarver(SmC_Picture pictureP)
	{
		P = pictureP;
		R = 0;
		G = 1;
		B = 2;
		T = 0;
		E = new double[P.height()][P.width()];
		for(int i = 0; i < P.height(); i++){
			for(int j = 0; j < P.width(); j++){
				E[i][j] = energy(j, i);
			}
		}
	}

	public SmC_Picture picture()
	{
		return P;
	}

	public int width()
	{
		return P.width();
	}

	public int height()
	{
		return P.height();
	}

	public double energy(int x, int y)
	{
		if(x < 0 || x >= P.width() || y < 0 || y >= P.height()){
			throw new IndexOutOfBoundsException();
		}
		if(x == 0 || x == P.width()-1 || y == 0 || y == P.height()-1){
			return 1000;
		}
		return Math.sqrt(sng(x, y, true) + sng(y, x, false));
	}
	
	private double sng(int a, int b, boolean isX){
		return Math.pow(rgb(a,b,R,isX), 2)+Math.pow(rgb(a,b,G,isX), 2)+Math.pow(rgb(a,b,B,isX),2);
	}
	
	private int rgb(int a, int b, int c, boolean isX){
		Color pos;
		Color neg;
		if(isX){
			pos = P.get(a+1, b);
			neg = P.get(a-1, b);
		}else{
			pos = P.get(b, a+1);
			neg = P.get(b, a-1);
		}
		if(c == R){
			return Math.abs(pos.getRed() - neg.getRed());
		}else if(c == G){
			return Math.abs(pos.getGreen() - neg.getGreen());
		}else if(c == B){
			return Math.abs(pos.getBlue() - neg.getBlue());
		}
		return -1;
	}

	public int[] findHorizontalSeam()
	{
		transposeH();
		int[] returned = findVerticalSeam();
		transposeV();
		return returned;
	}

	public int[] findVerticalSeam()
	{
		double[][] distTo = new double[P.height()][P.width()];
		int[][] prev = new int[P.height()][P.width()];
		int smallEnd = -1;
		double endSmallest = Double.MAX_VALUE;
		for(int i = 0; i < P.height(); i++){
			for(int j = 0; j < P.width(); j++){
				if(i == 0){
					distTo[i][j] = 0;
				}else if(i == 1){
					distTo[i][j] = E[i][j];
					
					prev[i][j] = j;
				}else{
					double smallest = Double.MAX_VALUE;
					double test = 0;
					int last = 0;
					if(j-1 > 0){
						test = distTo[i-1][j-1];
						if(test < smallest){
							smallest = test;
							last = j-1;
						}
					}
					test = distTo[i-1][j];
					if(test < smallest){
						smallest = test;
						last = j;
					}
					if(j+1 < P.width()){
						test = distTo[i-1][j+1];
						if(test < smallest){
							smallest = test;
							last = j+1;
						}
					}
					distTo[i][j] = smallest + E[i][j];
					
					if(i == P.height()-1){
						if(distTo[i][j] < endSmallest){
							endSmallest = distTo[i][j];
							smallEnd = j;
						}
					}
					prev[i][j] = last;
				}
			}
		}
		int[] returned = new int[P.height()];
		for(int i = P.height()-1; i >= 0; i--){
			returned[i] = smallEnd;
			smallEnd = prev[i][smallEnd];
		}
		return returned;
	}
	
	private void transposeH(){
		SmC_Picture Q = new SmC_Picture(P.height(), P.width());
		double[][] F = new double[P.width()][P.height()];
		Q.setOriginLowerLeft();
		for(int i = 0; i < P.width(); i++){
			for(int j = 0; j < P.height(); j++){
				Q.set(j, i, P.get(i, j));
				F[i][j] = E[j][i];
			}
		}
		P = Q;
		E = F;
	}
	
	private void transposeV(){
		SmC_Picture Q = new SmC_Picture(P.height(), P.width());
		double[][] F = new double[P.width()][P.height()];
		P.setOriginLowerLeft();
		for(int i = 0; i < P.width(); i++){
			for(int j = 0; j < P.height(); j++){
				Q.set(j, i, P.get(i, j));
				F[i][j] = E[j][i];
			}
		}
		P = Q;
		E = F;
	}

	public void removeHorizontalSeam(int[] a)
	{
		transposeH();
		removeVerticalSeam(a);
		transposeV();
	}

	public void removeVerticalSeam(int[] a)
	{
		if(T == 1){
			T = 0;
			transposeV();
		}
		if(P.width() <= 1){
			throw new IllegalArgumentException();
		}
		SmC_Picture Q = new SmC_Picture(P.width()-1, P.height());
		for(int i = 0; i < P.height(); i++){
			int mod = 0;
			if(a[i] < 0 || a[i] > P.width()){
				throw new IllegalArgumentException();
			}
			for(int j = 0; j < P.width(); j++){
				if(j == a[i]){
					mod = 1;
				}else{
					Q.set(j-mod, i, P.get(j, i));
					E[i][j-mod] = E[i][j];
				}
			}
		}
		P = Q;
	}
}