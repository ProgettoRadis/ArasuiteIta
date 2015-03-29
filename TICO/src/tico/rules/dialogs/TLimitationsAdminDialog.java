/*
 * File: TLmitationsAdminDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Sep 7, 2007
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS, GIGA
 * 
 * License:
 * 		This program is free software: you can redistribute it and/or 
 * 		modify it under the terms of the GNU General Public License 
 * 		as published by the Free Software Foundation, either version 3
 * 		of the License, or (at your option) any later version.
 * 
 * 		This program is distributed in the hope that it will be useful,
 * 		but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 		GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package tico.rules.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import tico.components.TButton;
import tico.components.TComboBox;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.rules.TAttribute;
import tico.rules.TLimitation;
import tico.rules.database.TDeleteLimitation;
import tico.rules.database.TLoadLimitation;

/**
 * The limitations administration dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Sep 7, 2007
 */
public class TLimitationsAdminDialog extends TDialog implements ActionListener {
	/**
	 * Creates a new <code>TLimitationsAdminDialog</code> for the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	private static int MAX_LIMITATIONS = 8;
	
	private static String PARAMS_FILE_PATH = "conf" + File.separator
	+ "params_resolution.xml";
	private static File PARAMS_FILE = new File(PARAMS_FILE_PATH);
	
	private static String DEFAULT_PARAMS_FILE_PATH = "conf" + File.separator
	+ "default_params_resolution.xml";
	private static File DEFAULT_PARAMS_FILE = new File(DEFAULT_PARAMS_FILE_PATH);
	
	private TComboBox limitCombo;
	private TComboBox limitEditCombo;
	private TComboBox paramEditCombo;
	private JTable table;
	private int maxRow;
	private String type = "int";
	private Vector<TLimitation> limitationList = new Vector<TLimitation>();
	

	// Creates the limitations administration dialog
	public TLimitationsAdminDialog(TEditor editor) {
		super(editor, TLanguage.getString("TLimitationsAdminDialog.TITLE"), true);
		createComponents(editor);
	}

	
	// Creates and places components
	private void createComponents(TEditor editor) {	

		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TLimitationsAdminDialog.TAB"), 
				createEntryPanel(editor));
		add(tabbedPane);
		
        // Place components
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		getContentPane().add(createButtonPanel(editor), BorderLayout.SOUTH);

		// Display the dialog
		setDefaultCloseOperation(TDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	}
	
	
	private JPanel createEntryPanel(TEditor editor) {	
		JPanel entryPanel = new JPanel();
		// Create components
		createCombos();
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		entryPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		entryPanel.add(createLimitationPanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 1, 15);
		c.gridx = 0;
		c.gridy = 1;
		entryPanel.add(createParameterPanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 1, 15);
		c.gridx = 0;
		c.gridy = 2;
		entryPanel.add(createTablePanel(), c);

        return entryPanel;
	}
	
	
	private void createCombos() {				
        limitationList.removeAllElements();
        TLoadLimitation limits = new TLoadLimitation();
		limitationList = limits.loadAllLimitations();
		// create not editable combo to select a limitation
		limitCombo = new TComboBox();
		// create editable combo to add or delete a limitation
		limitEditCombo = new TComboBox();
		
		for (int i=0; i<limitationList.size(); i++) {
			limitCombo.addItem(limitationList.elementAt(i).getName());
			limitEditCombo.addItem(limitationList.elementAt(i).getName());
		}
        limitCombo.setSelectedIndex(0);
        limitCombo.addActionListener(this);
        limitCombo.setMinimumSize(new Dimension(100, 25));
        limitCombo.setPreferredSize(new Dimension(100, 25));
        
        limitEditCombo.setEditable(true);
        limitEditCombo.setSelectedIndex(0);
        limitEditCombo.setMinimumSize(new Dimension(100, 25));
        limitEditCombo.setPreferredSize(new Dimension(100, 25));
        // create editable combo to add or delete an attribute
        paramEditCombo = new TComboBox();
        paramEditCombo.setEditable(true);
	}

	
	private JPanel createLimitationPanel() {
		JPanel limitationPanel = new JPanel();
		
		JLabel labelLimit = new JLabel(TLanguage.
				getString("TLimitationsAdminDialog.LABEL_LIMT"));
		
		// button to delete limitations
		TButton deleteLimitButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.DELETE_LIMIT"));
		deleteLimitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lim = (String) limitEditCombo.getSelectedItem();
				if (lim.compareTo("")!=0) {
					// warning
					Object[] options = {TLanguage.getString(
						"TLimitationsAdminDialog.ACCEPT_OPTION"),
						TLanguage.getString(
						"TLimitationsAdminDialog.CANCEL_OPTION")};

					int n = JOptionPane.showOptionDialog(null,
						TLanguage.getString("TLimitationsAdminDialog.DELETE_LIM_MESAGE"),
						TLanguage.getString("WARNING"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);

					if (n == JOptionPane.YES_OPTION) {
						int index = limitEditCombo.getSelectedIndex();
						// remove from XML file
						removeLimitation(lim);
						for (int i=0; i<limitationList.size(); i++)
							if (lim.compareTo(limitationList.get(i).getName())==0)
								limitationList.remove(i);
						limitCombo.removeItemAt(index);
						limitCombo.setSelectedIndex(0);
						limitEditCombo.removeItemAt(index);
						limitEditCombo.setSelectedIndex(0);				
					}	
				}
			}
		});
		// button to add limitations
		TButton addLimitButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.ADD_LIMIT"));
		addLimitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((limitEditCombo.getItemCount()-1) < MAX_LIMITATIONS) {
					Boolean exist = false;
					String lim = (String) limitEditCombo.getSelectedItem();
					for (int i=0; i<limitationList.size(); i++)
						if (lim.compareTo(limitationList.get(i).getName())==0)
							exist = true;
					if (!exist) {
						limitationList.add(new TLimitation(lim, lim));
						limitCombo.addItem(lim);
						limitEditCombo.addItem(lim);
					}
				} else
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TLimitationsAdminDialog.MAX_LIMIT"), 
							TLanguage.getString("ERROR"),
							JOptionPane.ERROR_MESSAGE);
					
			}
		});
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		limitationPanel.setLayout(new GridBagLayout());
		limitationPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TLimitationsAdminDialog.TEXT_LIMIT")));
		
		labelLimit.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 1, 15);
		c.gridx = 0;
		c.gridy = 0;
		limitationPanel.add(labelLimit, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 1;
		limitationPanel.add(limitEditCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 1;
		limitationPanel.add(addLimitButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 2;
		c.gridy = 1;
		limitationPanel.add(deleteLimitButton, c);
		
		return limitationPanel;
	}
	
	private JPanel createParameterPanel() {
		JPanel attributePanel = new JPanel();
		
		JLabel labelLimit = new JLabel(TLanguage.
				getString("TLimitationsAdminDialog.LABEL_LIMT"));
		JLabel labelAttrib = new JLabel(TLanguage.
				getString("TLimitationsAdminDialog.LABEL_PARAM"));
		
		JRadioButton intRadio = new JRadioButton(TLanguage.getString("TLimitationsAdminDialog.INT"));
	    intRadio.setActionCommand("int");
	    intRadio.setSelected(true);
	        
	    JRadioButton boolRadio = new JRadioButton(TLanguage.getString("TLimitationsAdminDialog.BOOL"));
	    boolRadio.setActionCommand("bool");
	       
	    ButtonGroup group = new ButtonGroup();
	    group.add(intRadio);
	    group.add(boolRadio);
	        
	    intRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type=e.getActionCommand();
			}
		});
	    
	    boolRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type=e.getActionCommand();
			}
		});
	    
	    // button to delete parameters
		TButton deleteAttribButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.DELETE_PARAM"));
		deleteAttribButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// warning message
				String limit = (String) paramEditCombo.getSelectedItem();
				if (limit!=null) {
					if (limit.compareTo("")!=0) {
						Object[] options = {TLanguage.getString(
								"TLimitationsAdminDialog.ACCEPT_OPTION"),
								TLanguage.getString(
								"TLimitationsAdminDialog.CANCEL_OPTION")};

						int n = JOptionPane.showOptionDialog(null,
								TLanguage.getString("TLimitationsAdminDialog.DELETE_PARAM_MESAGE"),
								TLanguage.getString("WARNING"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE,
								null,
								options,
								options[1]);

						if (n == JOptionPane.YES_OPTION) {
							// Attribute name to remove
							String lim = (String) limitCombo.getSelectedItem();
							String atr = (String) paramEditCombo.getSelectedItem();
							TTableModel model = (TTableModel) table.getModel();
							model.removeColumn(lim, atr);
							paramEditCombo.removeItemAt(paramEditCombo.getSelectedIndex());
						}
					}
				}
			}
		});
		
		// button to add parameters
		TButton addParamButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.ADD_PARAM"));
		addParamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Attribute name to remove
				String lim = (String) limitCombo.getSelectedItem();
				if (lim!=null && lim.compareTo("")!=0) {
					String atr = (String) paramEditCombo.getSelectedItem();
					TTableModel model = (TTableModel) table.getModel();
					model.addColumn(lim, atr);		
					paramEditCombo.addItem(atr);
				}
			}
		});
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		attributePanel.setLayout(new GridBagLayout());
		attributePanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TLimitationsAdminDialog.TEXT_PARAM")));
		
		labelLimit.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 1, 15);
		c.gridx = 0;
		c.gridy = 0;
		attributePanel.add(labelLimit, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 1;
		attributePanel.add(limitCombo, c);
		
		labelAttrib.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 1, 15);
		c.gridx = 1;
		c.gridy = 0;
		attributePanel.add(labelAttrib, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 1;
		attributePanel.add(paramEditCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 2;
		c.gridy = 1;
		attributePanel.add(addParamButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 3;
		c.gridy = 1;
		attributePanel.add(deleteAttribButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 2;
		attributePanel.add(intRadio, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 3;
		attributePanel.add(boolRadio, c);
		    
		return attributePanel;
	}
	
	private JPanel createButtonPanel(final TEditor editor) {
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new FlowLayout());
		
		// button to close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
	
	private JPanel createTablePanel() {	
		JPanel tablePanel = new JPanel();
		
		// Create and set up the content pane.
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS));
        tablePanel.setOpaque(true); //content panes must be opaque
		tablePanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TLimitationsAdminDialog.TEXT_TABLE")));
		tablePanel.setMinimumSize(new Dimension(200, 250));
        tablePanel.setPreferredSize(new Dimension(200, 250));
        
		table = new JTable(new TTableModel());
        //table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredScrollableViewportSize(new Dimension(200, 250));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.doLayout();
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
		// button to delete rows
		TButton deleteRowButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.DELETE_ROW"));
		deleteRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TTableModel model = (TTableModel) table.getModel();
				if (model.getRowCount() > 0)
					model.removeRow(table.getSelectedRow());
			}
		});
		// button to add rows
		TButton addRowButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.ADD_ROW"));
		addRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TTableModel model = (TTableModel) table.getModel();
				if (model.getRowCount() > 0)
					model.addRow();
			}
		});
		
		JPanel tableButtonPanel = new JPanel();		
		tableButtonPanel.setLayout(new FlowLayout());
		tableButtonPanel.add(addRowButton);
		tableButtonPanel.add(deleteRowButton);
		
		// Add button table panel
        tablePanel.add(tableButtonPanel);
		// Add the scroll pane to this panel.
        tablePanel.add(scrollPane);
        
        // button to save values
		TButton saveButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.SAVE_BUTTON"));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// save parameters in XML file
				TTableModel model = (TTableModel) table.getModel();

				int ret = model.saveTable(table);
				if (ret!=-1)
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TLimitationsAdminDialog.MESSAGE"), 
							TLanguage.getString("INFO"),
							JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TLimitationsAdminDialog.ERROR_MESSAGE"), 
							TLanguage.getString("ERROR"),
							JOptionPane.ERROR_MESSAGE);
			}
		});
		
		// button to load parameters
		TButton loadButton = new TButton(TLanguage.
				getString("TLimitationsAdminDialog.LOAD_BUTTON"));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) limitCombo.getSelectedItem();
				TTableModel model = (TTableModel) table.getModel();
				model.updateTable(name, "default");
	        	computeColumnWidths();
	        	paramEditCombo.removeAllItems();
	        	paramEditCombo.addItem("");
	            for (int i=1; i<model.columnNames.size(); i++)
	            	paramEditCombo.addItem(model.columnNames.get(i));
			}
		});
	
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        
        // Add button panel
        tablePanel.add(buttonPanel);
    
        return tablePanel;
	}
	

	/** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
        TComboBox cb = (TComboBox)e.getSource();
        String limitName = (String)cb.getSelectedItem();
        	TTableModel model = (TTableModel) table.getModel();
        	model.updateTable(limitName, null);
        	computeColumnWidths();
        	paramEditCombo.removeAllItems();
        	paramEditCombo.addItem("");
            for (int i=1; i<model.columnNames.size(); i++)
            	paramEditCombo.addItem(model.columnNames.get(i));
    }
	
    
    private void removeLimitation(String limit) {
    	// select the XML name of the limitation select in the combo box
    	String limitName = null;
    	for (int i=0; i<limitationList.size(); i++)
    		if (limit.compareTo(limitationList.get(i).getName())==0)
    			limitName = limitationList.get(i).getNameXML();
    	
    	if (limitName!=null) {
    		TDeleteLimitation delLimit = new TDeleteLimitation();
    		int ret = delLimit.deleteLimitationByName(limitName);
    		if (ret!=-1) 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TLimitationsAdminDialog.DELETE_OK_MESSAGE"), 
						TLanguage.getString("INFO"),
						JOptionPane.INFORMATION_MESSAGE);
    		else 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TLimitationsAdminDialog.DELETE_ERROR_MESSAGE"), 
						TLanguage.getString("ERROR"),
						JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    
	// Table Model to load and save the XML attributes
	class TTableModel extends AbstractTableModel {

		private Vector<String> columnNames = new Vector<String>();
		private Vector<String> columnNamesXML = new Vector<String>();
		private Object[][] data = new Object[100][100];
		public Object[] longValues;
		
		/**
         * Returns the number of columns
         */
        public int getColumnCount() {
        	return columnNames.size();
        }

        
        /**
         * Returns the number of rows
         */
        public int getRowCount() {
            //return data.length;
        	return maxRow+1;
        }

        
        /**
         * Returns the column name of the specified columns
         */
        public String getColumnName(int col) {
        	return columnNames.elementAt(col);
        }

        
        /**
         * Returns the value in the specified row and column
         */
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
        	try {
        		return getValueAt(0, c).getClass();
        	}catch (Exception e){
        		if (type.compareTo("int")==0)
        			return Integer.class.getClass();
        		else
        			return Boolean.class.getClass();
        	}
        }


        /**
         * Return true of false if the cell is editable or not
         */
        public boolean isCellEditable(int row, int col) {
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        
        /**
         * Sets a value in a cell
         */
        public void setValueAt(Object value, int row, int col) {            
            if (value==null) {
            	JOptionPane.showMessageDialog(null,
						TLanguage.getString("TLimitationsAdminDialog.DELETE_MESSAGE"), 
						TLanguage.getString("WARNING"),
						JOptionPane.WARNING_MESSAGE);
            }
			else // value is not null
            	data[row][col] = value;
            
            if (row>maxRow)
            	maxRow=row; // update maxRow
            
            // Enumerate rows
            for (int i=0; i<=maxRow; i++)
            	data[i][0]=i;

            fireTableStructureChanged();
            computeColumnWidths();
            // Change the color of the first column
    		TableColumn column = table.getColumnModel().getColumn(0);
    		column.setCellRenderer(new ColorColumnRenderer(Color.LIGHT_GRAY, Color.BLACK));
        }
        
        
        /**
         * Removes a row
         */
        public void removeRow (int row)
        {
        	if (table.getSelectedRow()>=0) {
        		// there is a row selected
        		fireTableRowsDeleted(row, row);
        		for (int i=row; i<getRowCount(); i++)
        			for (int j=0; j<getColumnCount(); j++)
        				data[i][j]=data[i+1][j];
        		maxRow--;
        		// Enumerate rows
        		for (int i=0; i<=maxRow; i++)
        			data[i][0]=i;          
        	}
        }
        
        /**
         * Add a row to the end of the table
         */
        public void addRow ()
        {
        	maxRow++;
        	fireTableRowsInserted(maxRow,maxRow);
        }
        
        private void deleteContents() {
        	for (int i=0; i<getRowCount(); i++)
        		for (int j=0; j<getColumnCount(); j++)
        		data[i][j]=null;
        }
        
        public void addColumn(String lim, String atr) {
        	if (lim.compareTo("")!=0) {
        		for (int i=0; i<limitationList.size(); i++) {
        			if (limitationList.get(i).getName().compareTo(lim)==0) {
        				Boolean exist = false;
        				for (int j=0; j<limitationList.get(i).getAttributeCount(); j++)  {
        					if (atr.compareTo(limitationList.get(i).getAttribute(j).getName())==0)
        						exist = true;
        				}
        				if (!exist && atr.compareTo("")!=0) {
        					// if attribute name not exist
        					columnNames.add(atr);
        					columnNamesXML.add(atr);
        					limitationList.get(i).addAttribute(new TAttribute(atr,atr));
        					if (type.compareTo("int")==0)
        						for (int k=0; k<getRowCount(); k++)
        							setValueAt(Integer.valueOf(0),k,getColumnCount()-1);
        					else {
        						for (int k=0; k<getRowCount(); k++)
        							setValueAt(false,k,getColumnCount()-1);
        						// update the table structure
        						fireTableStructureChanged();
        						// fit the column widths
        						computeColumnWidths();
        					}
        				}
        			}
        		}
        		// Change the color of the first column
        		TableColumn column = table.getColumnModel().getColumn(0);
                column.setCellRenderer(new ColorColumnRenderer(Color.LIGHT_GRAY, Color.BLACK));
        	}
        	
        }
        
        
        /**
         * Removes a column
         */
        public void removeColumn(String lim, String atr) {
        	if (lim.compareTo("")!=0) {
        		int index =  columnNames.indexOf(atr);
        		columnNames.remove(atr);
        		columnNamesXML.remove(index);
        		// update the table structure
        		fireTableStructureChanged();
        		// fit the column widths
        		computeColumnWidths();
        		// Change the color of the first column
        		TableColumn column = table.getColumnModel().getColumn(0);
        		column.setCellRenderer(new ColorColumnRenderer(Color.LIGHT_GRAY, Color.BLACK));
        	}
        }
        
 
        /**
         * Update the entire table
         */
        public void updateTable(String name, String option) {
        	deleteContents();
        	maxRow=0;
        	columnNames.removeAllElements();
        	columnNames.add(TLanguage.getString("TLimitationsAdminDialog.RANGE"));
        	columnNamesXML.removeAllElements();
        	columnNamesXML.add("range");
        	
        	// select the XML name of the limitation select in the combo box
        	String limitName = null;
        	for (int i=0; i<limitationList.size(); i++)
        		if (name.compareTo(limitationList.get(i).getName())==0)
        			limitName = limitationList.get(i).getNameXML();

        	if (limitName != null && limitName.compareTo("")!=0) {
        	// Get the limitations in the document
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    		Document doc;
    		SAXBuilder builder = new SAXBuilder ();
    		try {
    			if (option == "default")
    				doc = builder.build(DEFAULT_PARAMS_FILE);
    			else doc = builder.build(PARAMS_FILE);
    			// Get the root element
    			Element root=doc.getRootElement();
    			// List of resolution elements
    			List resolution=root.getChildren("res");
    			Iterator i = resolution.iterator();
    			Element e;
    			int val;
    			// Select the element with the same value as the system resolution
    			do {
    				e = (Element)i.next();
    				val = Integer.parseInt(e.getAttributeValue("type"));
    			} while (i.hasNext() && val!=screenSize.width);
    			
    			List limitations = e.getChildren(limitName);
    			Iterator i_lim = limitations.iterator();
    			if (i_lim.hasNext()) {
    				// get the different attributes of the specified limitation (name)
    				int row = 0;
    				do {
    					int col = 1;
    					e = (Element)i_lim.next();
    					val = Integer.parseInt(e.getAttributeValue("value"));	
    					setValueAt(val, row, 0);
    					List attributes = e.getChildren();
    					Iterator i_atr = attributes.iterator();
    					if (i_atr.hasNext()) {
    						do {
    							e = (Element)i_atr.next();
    							String nameCol = e.getName();
    							// if it's a new attribute, not access language file        					
    							if (!columnNamesXML.contains(nameCol)) {
    								if ((TLanguage.getString("Rules." + 
    										nameCol.toUpperCase())).startsWith("!")) {
    									columnNames.add(nameCol);
    									columnNamesXML.add(nameCol);
    								} else {
    									columnNames.add(TLanguage.getString("Rules."+
    											e.getName().toUpperCase()));
    									columnNamesXML.add(e.getName());
    								}
    							}	
    							if (e.getText().compareTo("true")==0)
    								setValueAt(new Boolean(true), row, col);
    							else if (e.getText().compareTo("false")==0)
    								setValueAt(new Boolean(false), row, col);
    							else
    								setValueAt(Integer.parseInt(e.getText()), row, col);
    							col++;
    						} while (i_atr.hasNext());
    						}
    						row++;
    					} while (i_lim.hasNext());
    					maxRow=row-1;
    				}
    			} catch (JDOMException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
    		// update the table structure
    		fireTableStructureChanged();
    		// fit the column widths
    		computeColumnWidths();
    		// Change the color of the first column
    		TableColumn column = table.getColumnModel().getColumn(0);
            column.setCellRenderer(new ColorColumnRenderer(Color.LIGHT_GRAY, Color.BLACK));
        }
        
        
        /**
         * Save the values of the table in the XML file
         */
        public int saveTable(JTable table) {
        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	Document doc;
    		SAXBuilder builder = new SAXBuilder ();
    		
    		// select the XML name of the limitation select in the combo box
    		String name = (String)limitCombo.getSelectedItem();
        	String limitName = null;
        	for (int i=0; i<limitationList.size(); i++)
        		if (name.compareTo(limitationList.get(i).getName())==0)
        			limitName = limitationList.get(i).getNameXML();

        	if (limitName!=null && limitName.compareTo("")!=0) {
    		try {
    			doc = builder.build(PARAMS_FILE);
    			// Get the root element
    			Element root=doc.getRootElement();
    			// List of resolution features
    			List resolution=root.getChildren("res");
    			Iterator i = resolution.iterator();
    			Element e;
    			String val;
    			// Select the element with the same resolution as system resolution
    			if (i.hasNext()) {
    				do {
    					e = (Element)i.next();
    					val = e.getAttributeValue("type");
    				} while (i.hasNext() && Integer.parseInt(val)!=screenSize.width);
    				// delete old contents
    				List limitations = e.getChildren(limitName);
    				Iterator i_lim = limitations.iterator();
    				if (i_lim.hasNext()) {
    					do {
    						i_lim.next();
    						i_lim.remove();
        				} while (i_lim.hasNext());
    				}
    				// save new attributes by columns
    				for (int r=0; r<getRowCount(); r++) {
    					if (data[r][0]!=null) {
    						Element limit = new Element(limitName);
    						limit.setAttribute("value", String.valueOf(data[r][0]));
    						for (int c=1; c<getColumnCount(); c++) {	
    							if (data[r][c]!=null) {
    								limit.addContent(new Element (columnNamesXML.elementAt(c)).
    										setText(String.valueOf(data[r][c])));
    							} else {
    								JOptionPane.showMessageDialog(null,
    										TLanguage.getString("TLimitationsAdminDialog.INFO_BLANKCELLS"), 
    										TLanguage.getString("WARNING"),
    										JOptionPane.WARNING_MESSAGE);
    								return -1;
    							}
    								
    						}
    						e.addContent(limit);
    					}
    				}
    			} else { // it is empty file
    				Element res = new Element("res");
    				res.setAttribute("type", Integer.toString(screenSize.width));
    				// save new attributes by columns
    				for (int r=0; r<getRowCount(); r++) {
    					if (data[r][0]!=null) {
    						Element limit = new Element(limitName);
    						limit.setAttribute("value", String.valueOf(data[r][0]));
    						for (int c=1; c<getColumnCount(); c++) {	
    							if (data[r][c]!=null) {
    								limit.addContent(new Element (columnNamesXML.elementAt(c)).
    										setText(String.valueOf(data[r][c])));
    							}	
    						}
    						res.addContent(limit);
    					}
    				}
    				root.addContent(res);
    			}
    			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
    			FileOutputStream file = new FileOutputStream(PARAMS_FILE);
    	   		xml.output(doc,file);
    			return 0;
    		} catch (JDOMException e) {
    			return -1;
    		} catch (IOException e) {
    			return -1;
    		}
    		}
    		return -1;
        }
	}
    
	
	class ColorColumnRenderer extends DefaultTableCellRenderer {
	   Color bkgndColor, fgndColor;
	 	
	   public ColorColumnRenderer(Color bkgnd, Color foregnd) {
	      super(); 
	      bkgndColor = bkgnd;
	      fgndColor = foregnd;
	   }
	  	
	   public Component getTableCellRendererComponent
		    (JTable table, Object value, boolean isSelected,
		     boolean hasFocus, int row, int column) 
	   {
	      Component cell = super.getTableCellRendererComponent
	         (table, value, isSelected, hasFocus, row, column);
	 
	      cell.setBackground( bkgndColor );
	      cell.setForeground( fgndColor );
	     
	      return cell;
	   }
	}
	
	 private void computeColumnWidths() {
        //horizontal spacing
        int hspace = 6;
        TTableModel model = (TTableModel) table.getModel();
	 
        //rows no
        int cols = model.getColumnCount();
 
        //columns no
        int rows = model.getRowCount();
	 
        //width vector
        int w[] = new int[ model.getColumnCount() ];
	 
        //computes headers widths
        for( int i=0; i<cols; i++ ) {
            w[i] = (int)table.getDefaultRenderer( String.class ).
                    getTableCellRendererComponent( table, table.getColumnName( i ), false, false, -1, i ).
                    getPreferredSize().getWidth() + hspace;
        }
	 
        //check if cell values fit in their cells and if not
        //keep in w[i] the necessary with
        for( int i=0; i<rows; i++ )
            for(int j=0; j<cols; j++ ) {
                Object o = model.getValueAt( i, j );
                int width  = 0;
                if ( o != null )
                    width = (int)table.getCellRenderer( i, j ).
                            getTableCellRendererComponent( table, o, false, false, i, j ).
                            getPreferredSize().getWidth() + hspace;
                if( w[j] < width )
                    w[j] = width;
            }
	 
        TableColumnModel colModel = table.getColumnModel();
	 
        //and finally setting the column widths
        for(int i=0; i<cols; i++ )
           colModel.getColumn( i ).setPreferredWidth( w[i] );
    }
}