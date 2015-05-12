import java.util.ArrayList;


public class ClusteringEngine implements Runnable{
	volatile Thread timer;
	public ArrayList<Attribute> dataSet;
	public ArrayList<Cluster> kSet;
	public int length;
	public double threshold = 0.005;

	public double err=0;

	public void run() {
		// TODO Auto-generated method stub
		while(timer == Thread.currentThread()){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){ }
			// running Method
			clustering();
			if(!rePosition())
				stop();
		}
	}

	public void start(){
		timer = new Thread(this);
		timer.start();
	}

	public void stop(){
		timer = null;
	}

	public ClusteringEngine(int length,int dataSize, int kSize){
		dataSet = new ArrayList<Attribute>();
		kSet = new ArrayList<Cluster>();
		this.length = length;

		for(int i = 0; i < dataSize; i++) // initializing dataSet
		dataSet.add(new Attribute(length,true));

		for(int i = 0; i < kSize; i++) // initializing ClusterSet
			kSet.add(new Cluster(length, true, i));
	}

	public void clustering(){
		for(Attribute w : dataSet)
			w.setNumber(getBestClass(w));
	}

	public int getBestClass(Attribute w){ // 웨이트와 가장 가까운 k를 찾아 거기에 해당하는 넘버를 리턴
		Cluster min = kSet.get((int)(Math.random() * kSet.size()));
		for(Cluster k : kSet)
			if(!min.equals(k) && min.distance(w) > k.distance(w))
				min = k;
		return min.getNumber();
	}

	public boolean rePosition(){
		double avgDist = 0;
		for(int i = 0; i < kSet.size(); i++){
			Attribute avgAttribute = averaging(i);
			Cluster k = kSet.get(i);
			avgDist += avgAttribute.distance(k);
			k.setAttribute(avgAttribute);
		}
		avgDist /= kSet.size();
		err = avgDist;
		if(avgDist > threshold)
			return true;  // 재배치를 하였으면 true
			else
				return false; // 아니면 false
	}

	public Attribute averaging(int num){ // 클래스 넘버에 해당하는 데이터만 찾아서 평균위치를 찾음.
		Attribute avg = new Attribute(length, false);
		int count = 0;
		for(Attribute w : dataSet)
			if(num == w.getNumber()){
				for(int i = 0; i < length; i++)
					avg.set(i, avg.get(i) + w.get(i));
				count++;
			}
		for(int i = 0; i < length; i++)
			avg.set(i, avg.get(i) / count);
		return avg;
	}

	public ArrayList<Attribute> getDataArray(){
		return dataSet;
	}

	public ArrayList<Cluster> getKArray(){
		return kSet;
	}
}