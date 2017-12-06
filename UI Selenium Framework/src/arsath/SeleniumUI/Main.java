package arsath.SeleniumUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Main {

	private JFrame frmGuiexecutionhelper;
	private JTable table;
	@SuppressWarnings("unused")
	private JTextField textField;
	public static String TCFolder = "";
	public static String ProjectFolder = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmGuiexecutionhelper.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frmGuiexecutionhelper = new JFrame();
		frmGuiexecutionhelper.setTitle("GUI_Execution_Helper");
		frmGuiexecutionhelper.setBounds(100, 100, 1065, 556);
		frmGuiexecutionhelper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblA = new JLabel("Status - No Browser is open");
		lblA.setBackground(Color.blue);

		ExecuteActions EXA = new ExecuteActions();

		JButton btnSaveActions = new JButton("Save Actions");

		TCFolder = System.getProperty("user.dir") + "\\WorkSpace\\";
		ProjectFolder = "TC Files\\";

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();

		DefaultMutableTreeNode RootTreeNode = new DefaultMutableTreeNode("Test Cases");

		JComboBox comboBox = new JComboBox();

		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Chrome", "Firefox", "IE" }));

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(5);
		formatter.setMaximum(200);
		formatter.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of
		// focus lost
		formatter.setCommitsOnValidEdit(true);
		JFormattedTextField textField = new JFormattedTextField(formatter);
		textField.setText("60");
		textField.setEditable(true);

		JTree tree = new JTree(RootTreeNode);
		tree.setVisibleRowCount(100);
		tree.addKeyListener(new KeyAdapter() {
			String ValueOFEditableNode = "";
			DefaultMutableTreeNode currentNode = null;
			@SuppressWarnings({ })
			List<String> StringListTestCases = new ArrayList();

			@SuppressWarnings({})
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == 10) {
					boolean tcNameAlreadyExist = false;

					{

						String nowselected = tree.getLastSelectedPathComponent().toString();
						if (StringListTestCases.contains(nowselected)) {
							currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
							JOptionPane.showMessageDialog(null, "Test case with the same name already exists!!");
							currentNode.setUserObject(ValueOFEditableNode);
							tcNameAlreadyExist = true;

						}
					}
					if (!tcNameAlreadyExist) {
						File oldName = new File(TCFolder + ProjectFolder + ValueOFEditableNode + ".txt");
						File newName = new File(
								TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString() + ".txt");
						oldName.renameTo(newName);
					}

				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (e.getKeyCode() == 113) {
					currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					ValueOFEditableNode = tree.getLastSelectedPathComponent().toString();
					System.out.println(currentNode);

					DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
					Enumeration Nodes = rootNode.depthFirstEnumeration();

					while (Nodes.hasMoreElements()) {
						TreeNode NodeTreeNode = (TreeNode) Nodes.nextElement();
						StringListTestCases.add(NodeTreeNode.toString());
					}
				}
			}
		});

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {

			}
		});

		DefaultTableModel TableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "Action", "Element", "XPATH", "Test Data" });
		table.setModel(TableModel);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JComboBox ActionComboBox = new JComboBox();
		table.addFocusListener(new FocusListener() {
			@SuppressWarnings({ })
			@Override
			public void focusGained(FocusEvent e) {

				File folder = new File(TCFolder + ProjectFolder);
				File[] FileList = folder.listFiles();
				ActionComboBox.removeAllItems();
				ActionComboBox.updateUI();
				ActionComboBox.addItem("Action_Open URL");
				ActionComboBox.addItem("Action_Set Variable");
				ActionComboBox.addItem("Action_Click");
				ActionComboBox.addItem("Action_Select");
				ActionComboBox.addItem("Action_Enter Text");
				ActionComboBox.addItem("Action_Wait Box");
				ActionComboBox.addItem("Action_Wait For Secs");
				ActionComboBox.addItem("Action_Assert Text");

				for (File IndividualFile : FileList) {
					if (!(IndividualFile.getName().replace(".txt", "")
							.equals(tree.getLastSelectedPathComponent().toString()))) {
						ActionComboBox.addItem("TC_" + IndividualFile.getName().replace(".txt", ""));
					}
				}
				table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(ActionComboBox));

			}

			@Override
			public void focusLost(FocusEvent arg0) {

			}

		});

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception e4) {
				}
				try {
					int rowCount = TableModel.getRowCount();
					for (int i = rowCount - 1; i >= 0; i--) {
						TableModel.removeRow(i);
					}
				} catch (Exception e) {
				}
				// System.out.println(tree.getLastSelectedPathComponent().toString());
				BufferedReader br = null;
				List<String> ContentList = new ArrayList<String>();
				try {
					br = new BufferedReader(new FileReader(
							TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString() + ".txt"));

				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}

				@SuppressWarnings("unused")
				StringBuilder sb = new StringBuilder();
				try {
					String line = br.readLine();
					while (line != null) {
						ContentList.add(line);
						line = br.readLine();
					}

				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					for (int i = 0; i < ContentList.size(); i = i + 4) {
						TableModel.addRow(new String[] { ContentList.get(i), ContentList.get(i + 1),
								ContentList.get(i + 2), ContentList.get(i + 3) });
					}
				} catch (Exception e) {
				}
			}
		});

		tree.setEditable(true);

		DefaultMutableTreeNode RootNode = new DefaultMutableTreeNode("Test Cases");
		DefaultTreeModel TreeModel = new DefaultTreeModel(RootNode);
		tree.setModel(TreeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollPane_1.setViewportView(tree);

		// Taking all the files from the folder and loading the test case tree

		File folder = new File(TCFolder + ProjectFolder);
		File[] FileList = folder.listFiles();
		for (File IndividualFile : FileList) {
			TreeModel.insertNodeInto(new DefaultMutableTreeNode(IndividualFile.getName().replace(".txt", "")), RootNode,
					0);
			tree.expandRow(0);
		}
		JButton btnNewButton = new JButton("Add TC");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long CurrentVarLong = System.currentTimeMillis();

				TreeModel.insertNodeInto(new DefaultMutableTreeNode(Long.toString(CurrentVarLong)), RootNode, 0);
				tree.expandRow(0);
				String PathName = TCFolder + ProjectFolder + Long.toString(CurrentVarLong) + ".txt";
				File newCSVFile = new File(PathName);
				try {
					newCSVFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Delete TC");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to delete the Selected TestCase");
				if (dialogResult == JOptionPane.YES_OPTION) {

					TreePath[] SelectedPaths = tree.getSelectionPaths();
					for (TreePath path : SelectedPaths) {
						File DeleteFile = new File(
								TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString() + ".txt");
						DeleteFile.delete();
						TreeModel.removeNodeFromParent((DefaultMutableTreeNode) path.getLastPathComponent());
					}

				}
			}
		});

		JButton btnNewButton_2 = new JButton("Add Action");

		btnNewButton_2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception e4) {
				}
				if (!tree.getLastSelectedPathComponent().toString().equalsIgnoreCase("")
						&& !tree.getLastSelectedPathComponent().toString().equalsIgnoreCase("Test Cases")) {
					try {
						TableModel.insertRow(table.getSelectedRow() + 1, new String[] { "", "", "", "" });
					} catch (Exception e) {
						TableModel.addRow(new String[] { "", "", "", "" });
					}
				}
			}
		});
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				try {
					if (tree.getLastSelectedPathComponent().toString().length() > 0) {
						if (!arg0.getNewValue().equals(arg0.getOldValue())) {
							btnSaveActions.setText("Save Actions - *");

						}
					}

				} catch (Exception e) {
				}
			}
		});

		JButton btnNewButton_3 = new JButton("Delete Action");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception e4) {
				}

				int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to delete the Selected Action");
				if (dialogResult == JOptionPane.YES_OPTION) {
					int SelectedRow = table.getSelectedRow();
					TableModel.removeRow(SelectedRow);
				}
			}
		});

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVisible(false);

		DefaultMutableTreeNode RootTreeNodeObjects = new DefaultMutableTreeNode("Objects");
		DefaultTreeModel TreeModel2 = new DefaultTreeModel(RootTreeNodeObjects);
		JTree tree_1 = new JTree();
		tree_1.setVisible(false);
		tree_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					System.out.println(tree.getLastSelectedPathComponent());
					// Name of the test case has changed, change it in file
				}
			}
		});
		tree_1.setEditable(true);
		tree_1.setModel(TreeModel2);
		tree_1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollPane_2.setViewportView(tree_1);

		JButton btnAddObjects = new JButton("Add Object");
		btnAddObjects.setVisible(false);
		btnAddObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TreeModel2.insertNodeInto(new DefaultMutableTreeNode(System.currentTimeMillis()), RootTreeNodeObjects,
						0);
				tree_1.expandRow(0);
			}
		});

		JButton btnDeleteObjects = new JButton("Delete Objects");
		btnDeleteObjects.setVisible(false);
		btnDeleteObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to delete the Selected Object");
				if (dialogResult == JOptionPane.YES_OPTION) {

					TreePath[] SelectedPaths = tree_1.getSelectionPaths();
					for (TreePath path : SelectedPaths) {
						TreeModel2.removeNodeFromParent((DefaultMutableTreeNode) path.getLastPathComponent());
					}
				}
			}
		});

		JButton btnNewButton_4 = new JButton("Run Current Test Case");
		btnNewButton_4.setBackground(Color.MAGENTA);
		btnNewButton_4.setForeground(Color.WHITE);
		btnNewButton_4.setVisible(false);
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblA.setText("Status - Script is Running");
				try {
					// System.out.println(textField.getText());
					frmGuiexecutionhelper.setVisible(false);
					EXA.SetWaitTimeInSecs(Integer.parseInt(textField.getText()));
					try {
						tree.getLastSelectedPathComponent().toString();
					} catch (Exception e) {
						throw new Exception("No Test Cases Are Selected");
					}
					EXA.ExecuteSCriptsFromTXTFile(
							TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString() + ".txt");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.toString());
				}
				lblA.setText("Status - Browser is open");
				frmGuiexecutionhelper.setVisible(true);
			}
		});

		JButton btnNewButton_5 = new JButton("Open Browser");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				EXA.OpenBrowser(comboBox.getSelectedItem().toString());
				btnNewButton_4.setVisible(true);
				lblA.setText("Status - Browser is open");
				lblA.setForeground(Color.green);
			}
		});

		btnSaveActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception e4) {
				}
				try {

					btnSaveActions.setText("Save Actions");

					Object[][] tableValues = getTableData(table);
					String SelectedTestcase = tree.getLastSelectedPathComponent().toString();
					if (!SelectedTestcase.equalsIgnoreCase("Test Cases")) {
						File FileToWrite = new File(TCFolder + ProjectFolder + SelectedTestcase + ".txt");
						FileWriter FW = new FileWriter(FileToWrite);
						// System.out.println(tableValues.length);
						BufferedWriter bw = new BufferedWriter(FW);
						for (int i = 0; i < tableValues.length; i++) {
							for (int j = 0; j < 4; j++) {
								bw.write(tableValues[i][j].toString());
								bw.newLine();
							}

						}
						bw.close();
						FW.close();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JButton btnNewButton_6 = new JButton("Close Open Browsers");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EXA.CloseBrowser();
				lblA.setText("Status - No Browser is open");
				btnNewButton_4.setVisible(false);
				lblA.setForeground(Color.black);
				lblA.setBackground(Color.LIGHT_GRAY);
			}
		});

		JButton btnDuplicatedSelectedTc = new JButton("Duplicated Selected TC");
		btnDuplicatedSelectedTc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File FileToDuplicate = new File(
						TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString() + ".txt");
				File Duplicate = new File(TCFolder + ProjectFolder + tree.getLastSelectedPathComponent().toString()
						+ "_" + Long.toString(System.currentTimeMillis()) + ".txt");
				try {
					Files.copy(FileToDuplicate.toPath(), Duplicate.toPath(), StandardCopyOption.REPLACE_EXISTING);
					TreeModel.insertNodeInto(new DefaultMutableTreeNode(Duplicate.getName().replace(".txt", "")),
							RootNode, tree.getLeadSelectionRow());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JLabel lblWaitSecs = new JLabel("Wait secs");

		JLabel lblSwitchWorkspace = new JLabel("Current WorkSpace : " + ProjectFolder.replace("\\", ""));
		lblSwitchWorkspace.setFont(new Font("Tahoma", Font.BOLD, 14));
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				comboBox_1.removeAllItems();

				File Projectfolder = new File(TCFolder);
				String[] projectList = Projectfolder.list();
				for (String IndividualProject : projectList) {
					if (new File(Projectfolder.getName() + "\\" + IndividualProject).isDirectory()) {
						comboBox_1.addItem(IndividualProject);
					}
				}
			}
		});

		JButton btnNewButton_7 = new JButton("Switch to WorkSpace");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					tree.clearSelection();
				} catch (Exception e) {
				}
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception e4) {
				}
				ProjectFolder = comboBox_1.getSelectedItem().toString() + "\\";
				lblSwitchWorkspace.setText("Current WorkSpace : " + ProjectFolder.replace("\\", ""));
				File folder = new File(TCFolder + ProjectFolder);
				File[] FileList = folder.listFiles();
				// Remove Existing TC in TC Tree
				// ((DefaultMutableTreeNode)
				// tree.getModel().getRoot()).removeAllChildren();
				((DefaultMutableTreeNode) TreeModel.getRoot()).removeAllChildren();

				// Re populate the Test case tree with TC from newly switched
				// Folder
				for (File IndividualFile : FileList) {
					TreeModel.insertNodeInto(new DefaultMutableTreeNode(IndividualFile.getName().replace(".txt", "")),
							RootNode, 0);
					tree.expandRow(0);
				}
				try {
					((DefaultTreeModel) TreeModel).reload();
				} catch (Exception e5) {
				}

			}
		});

		GroupLayout groupLayout = new GroupLayout(frmGuiexecutionhelper.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout
								.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(
												groupLayout.createSequentialGroup().addGap(10).addGroup(groupLayout
														.createParallelGroup(Alignment.LEADING).addGroup(
																groupLayout.createSequentialGroup().addComponent(
																		scrollPane_1, GroupLayout.PREFERRED_SIZE, 183,
																		GroupLayout.PREFERRED_SIZE)
																		.addGap(
																				4)
																		.addComponent(scrollPane,
																				GroupLayout.PREFERRED_SIZE, 849,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(Alignment.TRAILING, groupLayout
																.createSequentialGroup()
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(groupLayout
																		.createParallelGroup(Alignment.LEADING, false)
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(btnNewButton_1)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(btnDuplicatedSelectedTc)
																				.addGap(70)
																				.addComponent(btnNewButton_2))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(btnNewButton_4)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(textField,
																						GroupLayout.PREFERRED_SIZE, 46,
																						GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(lblWaitSecs)
																				.addPreferredGap(
																						ComponentPlacement.RELATED,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(btnNewButton_5)))
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addGroup(groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(btnNewButton_3)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(btnSaveActions))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(comboBox,
																						GroupLayout.PREFERRED_SIZE, 76,
																						GroupLayout.PREFERRED_SIZE)
																				.addGap(18).addComponent(btnNewButton_6,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)))
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addGroup(groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(btnNewButton_7).addGap(18)
																				.addComponent(comboBox_1,
																						GroupLayout.PREFERRED_SIZE, 151,
																						GroupLayout.PREFERRED_SIZE))
																		.addGroup(groupLayout
																				.createParallelGroup(Alignment.LEADING)
																				.addComponent(lblSwitchWorkspace,
																						GroupLayout.PREFERRED_SIZE, 316,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(lblA, Alignment.TRAILING,
																						GroupLayout.PREFERRED_SIZE, 442,
																						GroupLayout.PREFERRED_SIZE))))))
										.addGroup(groupLayout.createSequentialGroup().addContainerGap()
												.addComponent(btnNewButton)))
								.addGap(596)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(btnAddObjects, GroupLayout.PREFERRED_SIZE, 104,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnDeleteObjects, GroupLayout.PREFERRED_SIZE, 123,
														GroupLayout.PREFERRED_SIZE))
										.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblA, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton_4)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton_6).addComponent(btnNewButton_5)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblWaitSecs))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton)
								.addComponent(lblSwitchWorkspace))
						.addGap(2)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton_3)
								.addComponent(btnNewButton_2).addComponent(btnAddObjects).addComponent(btnDeleteObjects)
								.addComponent(btnSaveActions).addComponent(btnDuplicatedSelectedTc)
								.addComponent(btnNewButton_1).addComponent(btnNewButton_7).addComponent(comboBox_1,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(14)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_1, 0, 0, Short.MAX_VALUE))
						.addContainerGap(11, Short.MAX_VALUE)));

		frmGuiexecutionhelper.getContentPane().setLayout(groupLayout);
		frmGuiexecutionhelper.repaint();

	}

	public Object[][] getTableData(JTable table) {
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
		Object[][] tableData = new Object[nRow][nCol];
		for (int i = 0; i < nRow; i++)
			for (int j = 0; j < nCol; j++)
				tableData[i][j] = dtm.getValueAt(i, j);
		return tableData;
	}
}
