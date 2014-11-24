package kmps.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MyTable extends JTable {
	
	protected List<MyRow> list;
	public static int dynamicColumn = 4;

	public MyTable(){
		super();
		list = new ArrayList<MyRow>();
	}
	
	public void addRow(MyRow r){
		list.add(r);
		((DefaultTableModel)getModel()).addRow(r.val);
	}
	
	@Override
	public void update(Graphics g){
		Integer i = 0;
		for (MyRow r : list){
			if (r.update()){
				setValueAt(r.getLength(), i, dynamicColumn);
			}
			i++;
		}
		super.update(g);
	}
}
