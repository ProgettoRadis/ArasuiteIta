package components;

import java.io.File;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ImageTableModel extends DefaultTableModel {

	Vector<String> dataNames;
	
	private static final int COLUMN_NUMBER = 6;

	public ImageTableModel() {
		this.dataNames = new Vector<String>();
	}

	public java.lang.Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public void setData(Vector<String> dataNames) {
		this.dataNames = dataNames;
		double rows = Math.round(this.dataNames.size() / COLUMN_NUMBER + 0.5d);
		for (int i = 0; i < rows; i++) {
			this.addRow(new Object[COLUMN_NUMBER]);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		try {
			return this.dataNames.get(row * this.getColumnCount() + column);
		} catch (Exception e) {
			// It may be out of bounds.
		}
		return null;
	}

	public String getImageName(int row, int column) {
		if (this.dataNames.size() > (row * this.getColumnCount() + column)) {
			String name = this.dataNames.get(row * this.getColumnCount()
					+ column);
			return name.substring(name.lastIndexOf(File.separator) + 1);

		}
		return null;
	}

	public Vector<String> getDataNames() {
		return dataNames;
	}
}
