
public class Cluster extends Attribute{
	public int num;

	public Cluster(int length, boolean rnd, int num){
		super(length, rnd);
		this.num = num;
	}

	public int getNumber(){
		return num;
	}

	public void setAttribute(Attribute w){
		value = w.value;
	}
}
