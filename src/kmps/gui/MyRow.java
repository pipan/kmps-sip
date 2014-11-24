package kmps.gui;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MyRow {

	protected Object[] val;
	protected long start;
	
	public MyRow(Object[] a){
		val = a;
		start = 0;
	}
	
	public Object getLength(){
		return val[MyTable.dynamicColumn - 1];
	}
	
	public void setStart(long start){
		this.start = start;
		DateFormat d = new SimpleDateFormat("HH:mm:ss");
		val[MyTable.dynamicColumn - 2] = d.toString();
		val[MyTable.dynamicColumn] = new String("ACTIVE");
	}
	
	public boolean update(){
		if (start > 0){
			long time = System.currentTimeMillis();
			val[MyTable.dynamicColumn - 1] = getTime(time - start);
			return true;
		}
		return false;
	}
	
	public String getTime(long l){
		Integer time = (int) Math.floor(l / 1000);
		Integer sec = time % 60;
		Integer min = (int) Math.floor(time / 60);
		return min + ":" + sec;
	}
}
