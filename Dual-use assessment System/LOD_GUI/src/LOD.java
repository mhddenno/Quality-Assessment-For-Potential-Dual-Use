import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class LOD {

	private JFrame frmLod;
	private JTextField txtNothingTillNow;
	private JTextField txtNothingTillNow_1;
	private JTextField txtNothingTillNow_2;
	private JTable table_3;
	private JTable table;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LOD window = new LOD();
					window.frmLod.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public  static  List<Float> LODSys(int tech, String NLQuestion, String RDFQuestion){
		
	//	String NLQuestion=(String) comboBox_4.getSelectedItem();
	//	String RDFQuestion =(String) comboBox_5.getSelectedItem();	
		
		if(tech==1){
		Tokenizer tokening;	
		
		float rank=0;
		try {
			tokening = new Tokenizer();				
			ArrayList<String> tokens= tokening.tokenize(NLQuestion);
			StringBuilder sb = new StringBuilder();
			 rank=JDBCMySQLConnection.calcualteTheQuestionRank(tokens);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Float> ResultRank=new ArrayList<Float>();
		if(rank>=0.5)
		{
	
		
			Rdf_Querier r1=new Rdf_Querier();
			List<String> ResListdbpedia = r1.enquirydbpedia(RDFQuestion,"http://dbpedia.org/sparql");
	
		    List<String> ResListwikidata=r1.enquirywiki(RDFQuestion,"https://query.wikidata.org/sparql");

		    // ResultRank=new ArrayList<Float>();
			
			float ResWikiRank=0;
				for(String element : ResListwikidata) {
					float ID=0;
					ID=JDBCMySQLConnection.checkIfResExistWIKIData(element);
					if(ID!=0)
						ResWikiRank+=1;
							}
					
			//	textField_1.setText(String.valueOf(ResWikiRank/ResListwikidata.size()));
					ResultRank.add(ResWikiRank/ResListwikidata.size());
				
				float ResDBpediaRank=0;
				for(String element : ResListdbpedia) {
					float ID=0;
					ID=JDBCMySQLConnection.checkIfResExistDBpedia(element);
					if(ID!=0)
						ResDBpediaRank+=1;
							}
				ResultRank.add(ResDBpediaRank/ResListdbpedia.size());
			//	textField.setText(String.valueOf(ResDBpediaRank/ResListdbpedia.size()));
			
		}
		
		return ResultRank;
		}
		else{
			int questionsNum=0;
			try {
				OutputStream out = new FileOutputStream("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt");
				PrintStream printStream = new PrintStream(out);
				
				//DefaultTableModel model = (DefaultTableModel) table.getModel();
				questionsNum=1;
			//	for(int i=0;i<questionsNum;i++)
					printStream.println(NLQuestion);
				
				printStream.close();
				
			} catch (FileNotFoundException es) {
				// TODO Auto-generated catch block
				es.printStackTrace();
			}
			
			//Ali's code
			try {
				Process p = Runtime.getRuntime().exec("python /home/deep-learning/workspace/CNN_Classification/test.py /home/deep-learning/workspace/CNN_Classification/runs/1478093648/vocab /home/deep-learning/workspace/CNN_Classification/runs/1478093648/checkpoints");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			BufferedReader br_test_date = null;

			
			String questions[]=new String[questionsNum];
			String answers[]=new String[questionsNum];
			

			 
				try {
					TimeUnit.SECONDS.sleep(5);
					// Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			try {

				String sCurrentLine;

				br_test_date = new BufferedReader(new FileReader("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt"));

				int questions_count=0;
				while ((sCurrentLine = br_test_date.readLine()) != null) {
					questions[questions_count]=sCurrentLine;
					//System.out.println(sCurrentLine);
					questions_count++;
				}

			} catch (IOException ek) {
				ek.printStackTrace();
			} finally {
				try {
					if (br_test_date != null)br_test_date.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			BufferedReader br = null;

			try {

				String sCurrentLine;
				
				//JOptionPane.showMessageDialog(btnNewButton_2,questionsNum);
				
				br = new BufferedReader(new FileReader("/home/deep-learning/workspace/LOD_GUI/result.txt"));
				int answers_count=0;
				while ((sCurrentLine = br.readLine()) != null) {
					//System.out.println(sCurrentLine);
					String mainChapterNumber = sCurrentLine.split("\\.", 2)[0];
					answers[answers_count]=mainChapterNumber;
					//System.out.println(mainChapterNumber);
					answers_count++;
				}

			} catch (IOException eoo) {
				eoo.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
										
			for(int i=0 ; i<questionsNum;i++){
				System.out.println(questions[i]);
				System.out.println(answers[i]);
				if(answers[i].equals("1")){
				}
				else
				{

					Rdf_Querier r1=new Rdf_Querier();
					List<String> ResListdbpedia = r1.enquirydbpedia(RDFQuestion,"http://dbpedia.org/sparql");
			
				    List<String> ResListwikidata=r1.enquirywiki(RDFQuestion,"https://query.wikidata.org/sparql");
		
					
					// This Block of Code Query a resource to the database and return the ID 
				    List<Float> ResultRank=new ArrayList<Float>();			
					float ResWikiRank=0;
						for(String element : ResListwikidata) {
							float ID=0;
							ID=JDBCMySQLConnection.checkIfResExistWIKIData(element);
							if(ID!=0)
								ResWikiRank+=1;
									}
							
						ResultRank.add(ResWikiRank/ResListwikidata.size());
							
						
						float ResDBpediaRank=0;
						for(String element : ResListdbpedia) {
							float ID=0;
							ID=JDBCMySQLConnection.checkIfResExistDBpedia(element);
							if(ID!=0)
								ResDBpediaRank+=1;
									}
							
						ResultRank.add(ResDBpediaRank/ResListdbpedia.size());
		return ResultRank;
		}
			}
		}
				List<Float> ResultRank=new ArrayList<Float>();		
				return ResultRank;
	}
	
		
	/**
	 * Create the application.
	 */
	public LOD() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JEditorPane editorPane = new JEditorPane();
		JPanel panel_5 = new JPanel();
		Charts t=new Charts();
		JEditorPane editorPane_4 = new JEditorPane();
		JEditorPane editorPane_5 = new JEditorPane();
		frmLod = new JFrame();
		frmLod.setTitle("LOD");
		frmLod.setResizable(false);
		frmLod.setBackground(Color.BLACK);
		frmLod.setBounds(100, 100, 723, 733);
		frmLod.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JEditorPane editorPane_1 = new JEditorPane();
		JEditorPane editorPane_2 = new JEditorPane();
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(tabbedPane.getSelectedIndex()==3)
					{
						panel_5.setVisible(false);
					}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frmLod.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Neural Network", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblNewLabel_8 = new JLabel("Write a question or upload a list of questions from a .txt file from here:");
		lblNewLabel_8.setBounds(10, 11, 517, 14);
		panel.add(lblNewLabel_8);
		
		
		
		JButton btnNewButton_1 = new JButton("Browse");
		btnNewButton_1.setIcon(new ImageIcon("/home/deep-learning/workspace/LOD_GUI/ico/Browse.png"));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				String line;
				final JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(btnNewButton_1);	
					try {
						InputStream read = new FileInputStream(fc.getSelectedFile());
						InputStreamReader isr = new InputStreamReader(read, Charset.forName("UTF-8"));
						BufferedReader br = new BufferedReader(isr);
						 while ((line = br.readLine()) != null) {
								if(model.getValueAt(0, 0)=="Nothing yet.....")
									model.removeRow(0);
						        model.addRow(new Object[]{line});
						    }
						 read.close();
						 isr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(btnNewButton_1,
							    "Error Reading file",
							    "Error catched",
							    JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		btnNewButton_1.setBounds(20, 37, 125, 23);
		panel.add(btnNewButton_1);
		

		// anueral
		JButton btnNewButton_2 = new JButton("Analyze");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int questionsNum=0;
				try {
					OutputStream out = new FileOutputStream("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt");
					PrintStream printStream = new PrintStream(out);
					
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					questionsNum=model.getRowCount();
					for(int i=0;i<questionsNum;i++)
						printStream.println(model.getValueAt(i, 0));
					
					printStream.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Ali's code
				try {
					Process p = Runtime.getRuntime().exec("python /home/deep-learning/workspace/CNN_Classification/test.py /home/deep-learning/workspace/CNN_Classification/runs/1478093648/vocab /home/deep-learning/workspace/CNN_Classification/runs/1478093648/checkpoints");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				BufferedReader br_test_date = null;

				
				String questions[]=new String[questionsNum];
				String answers[]=new String[questionsNum];
				

				 
					try {
						TimeUnit.SECONDS.sleep(5);
						// Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				try {

					String sCurrentLine;

					br_test_date = new BufferedReader(new FileReader("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt"));

					int questions_count=0;
					while ((sCurrentLine = br_test_date.readLine()) != null) {
						questions[questions_count]=sCurrentLine;
						//System.out.println(sCurrentLine);
						questions_count++;
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br_test_date != null)br_test_date.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				
				BufferedReader br = null;

				try {

					String sCurrentLine;
					
					//JOptionPane.showMessageDialog(btnNewButton_2,questionsNum);
					
					br = new BufferedReader(new FileReader("/home/deep-learning/workspace/LOD_GUI/result.txt"));
					int answers_count=0;
					while ((sCurrentLine = br.readLine()) != null) {
						//System.out.println(sCurrentLine);
						String mainChapterNumber = sCurrentLine.split("\\.", 2)[0];
						answers[answers_count]=mainChapterNumber;
						//System.out.println(mainChapterNumber);
						answers_count++;
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br != null)br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
						
				DefaultTableModel model = (DefaultTableModel) table.getModel();		
			
				if (model.getRowCount() > 0) {
				    for (int i = model.getRowCount() - 1; i > -1; i--) {
				    	model.removeRow(i);
				    }
				}
				
				for(int i=0 ; i<questionsNum;i++){
					System.out.println(questions[i]);
					System.out.println(answers[i]);
					if(answers[i].equals("1"))
						model.addRow(new Object[]{questions[i],"Safe"});
					else
						model.addRow(new Object[]{questions[i],"Malicious"});
				}
				
				JOptionPane.showMessageDialog(btnNewButton_2,"Done Successfully");
				
				
			}
		});
		btnNewButton_2.setIcon(new ImageIcon("/home/deep-learning/workspace/LOD_GUI/ico/Analyze.png"));
		btnNewButton_2.setBounds(40, 606, 125, 37);
		panel.add(btnNewButton_2);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(30, 70, 631, 524);
		panel.add(scrollPane_3);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Nothing yet.....", ""},
			},
			new String[] {
				"Questions", "Flages"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(550);
		scrollPane_3.setViewportView(table);
		
		JButton btnNewButton_3 = new JButton("Add");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/////////////now here 
				String s = (String)JOptionPane.showInputDialog(
						btnNewButton_3,
				                    "Write your question here:\n"
				                    ,
				                    "New Question",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    null,
				                    "here...");
				
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				if(model.getValueAt(0, 0)=="Nothing yet.....")
					model.removeRow(0);
		        model.addRow(new Object[]{s});
				
				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
				    return;
				}

			}
		});
		btnNewButton_3.setIcon(new ImageIcon("/home/deep-learning/workspace/LOD_GUI/ico/pluss.png"));
		btnNewButton_3.setBounds(541, 37, 130, 23);
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Clear");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DefaultTableModel model = (DefaultTableModel) table.getModel();
										
					for(int i=0;i<model.getRowCount();i++)
						model.removeRow(i);
					
					if(model.getRowCount()==0)
						model.addRow(new Object[]{"Nothing yet....."});		
			}
		});
		btnNewButton_4.setBounds(525, 606, 125, 37);
		panel.add(btnNewButton_4);

		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("DBpedia", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblWirteYourQues = new JLabel("Write or choose your natural language question here:");
		lblWirteYourQues.setBounds(10, 11, 651, 14);
		panel_1.add(lblWirteYourQues);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(20, 36, 641, 20);
		panel_1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"How to make a bomb using home ingredients ?", "How to make a Neutron bomb ?", "What are the most interesting computer worms/viruses ever designed ?", "Why is a Trojan useful in hacking ?", "How can we control a victim’s computer using Trojans ?", "How do I edit my trojan viurs using hex editor so that common antivirus program would not detect it ?", "How do you track someone with a Remote Access Tool or Trojan ?", "How do Hackers Hack your Passwords ?", "How are hackers hacking social media passwords nowadays ?"}));
		comboBox.setEditable(true);
		
		JLabel lblWriteOrChoose = new JLabel("Write or choose your RDF Query question here:");
		lblWriteOrChoose.setBounds(10, 67, 651, 14);
		panel_1.add(lblWriteOrChoose);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(20, 92, 641, 20);
		panel_1.add(comboBox_1);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"SELECT distinct ?y  WHERE {{ <http://dbpedia.org/resource/Dirty_bomb> dct:subject ?y }}", "SELECT distinct   ?m WHERE {{<http://dbpedia.org/resource/Neutron_bomb> dct:subject ?m }}", "SELECT DISTINCT ?company where {?company a <http://dbpedia.org/ontology/Company>} LIMIT 20", "SELECT distinct ?y WHERE {{ ?y dbp:products <http://dbpedia.org/resource/Weapon> }}"}));
		comboBox_1.setEditable(true);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBounds(10, 123, 92, 23);
		panel_1.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				String NLQuestion=(String) comboBox.getSelectedItem();
				String RDFQuestion =(String) comboBox_1.getSelectedItem();				
						
				float rank=0;
				try {
					Tokenizer tokening = new Tokenizer();				
					ArrayList<String> tokens= tokening.tokenize(NLQuestion);
					StringBuilder sb = new StringBuilder();
					for (String s : tokens)
					{
					    sb.append(s);
					    sb.append("\t");
					}	
					//first argument
					txtNothingTillNow.setText(sb.toString());
				
					 rank=JDBCMySQLConnection.calcualteTheQuestionRank(tokens);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//second argument XPX
				txtNothingTillNow_1.setText(String.valueOf(rank));
				
				//decide whether to log it or not based on the rank (To check with mohammed's logger)
				
				// This Block of Code Queries a query against DBpedia and returns the result in array list
				Rdf_Querier r1=new Rdf_Querier();
				//List<String> ResList = r1.enquirydbpedia(RDFQuestion,"http://dbpedia.org/sparql");
				
				List<String> ResList = r1.enquirydbpedia(RDFQuestion,"http://dbpedia-live.openlinksw.com/sparql");
				
				String resources="";
				float ResRank=0;
					for(String element : ResList) {
						float ID=0;
						ID=JDBCMySQLConnection.checkIfResExistDBpedia(element);
						if(ID!=0)
							ResRank+=1;
						if(ID==0)
						resources+="Resource: "+element+" was not found \n";
						else
							resources+="Resource: "+element+" was found under the category: "+JDBCMySQLConnection.category_name_DBPedia((int)ID)+" \n";
					}
					//third argument
				editorPane_1.setText(resources);
				
				//fourth argument
				txtNothingTillNow_2.setText(String.valueOf(ResRank/ResList.size()));
					
				// This part is Related to Goolge & Qoura 
				
				// This Block is to invoke results from Google:
		
		 		Google_Fetcher obj = new Google_Fetcher();
				List<String> result = obj.getDataFromGoogle(NLQuestion);
				Page_Extractor page = new Page_Extractor();
				float rankQoura=0;
				List<String> Answers;
				StringBuilder sb2 = new StringBuilder();
				sb2.append(result.get(0)); 	sb2.append("\n");
				try { 
					Answers=page.extractAnswers("https://"+result.get(0));
					rankQoura = page.extractRank(Answers);
					for(String item:Answers){
						sb2.append("Answer:\n");
						sb2.append(item);
						sb2.append("\n");
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			
				
				
				sb2.append("\n");
				//sb2.append("Answers for this question rank as: "+rankQoura);
					editorPane_2.setText(sb2.toString());			
					textField_5.setText(Float.toString(rankQoura));
				}
		});
		
		JLabel lblNewLabel = new JLabel("Result after tokenize The NLQuestion:");
		lblNewLabel.setBounds(7, 157, 654, 14);
		panel_1.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(596, 65, 2, 2);
		panel_1.add(scrollPane);
		
		txtNothingTillNow = new JTextField();
		txtNothingTillNow.setBounds(20, 182, 641, 20);
		panel_1.add(txtNothingTillNow);
		txtNothingTillNow.setText("Nothing till now");
		txtNothingTillNow.setEditable(false);
		txtNothingTillNow.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("The question duality rank depending on sentimental analysis from our profiles:");
		lblNewLabel_2.setBounds(7, 213, 654, 14);
		panel_1.add(lblNewLabel_2);
		
		txtNothingTillNow_1 = new JTextField();
		txtNothingTillNow_1.setBounds(20, 238, 641, 20);
		panel_1.add(txtNothingTillNow_1);
		txtNothingTillNow_1.setText("Nothing till now");
		txtNothingTillNow_1.setEditable(false);
		txtNothingTillNow_1.setColumns(10);
		
		JLabel lblFireTheQuery = new JLabel("Fire the query against DBpedia:");
		lblFireTheQuery.setBounds(7, 385, 654, 14);
		panel_1.add(lblFireTheQuery);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 410, 641, 95);
		panel_1.add(scrollPane_1);
		
		editorPane_1.setEditable(false);
		scrollPane_1.setViewportView(editorPane_1);

		JLabel lblTheRankIs = new JLabel("The rank after comparing the resources from DBPedia with malicious resources in our profiles:");
		lblTheRankIs.setBounds(10, 269, 651, 14);
		panel_1.add(lblTheRankIs);
		
		JLabel label = new JLabel("");
		label.setBounds(30, 390, 0, 0);
		panel_1.add(label);
		
		txtNothingTillNow_2 = new JTextField();
		txtNothingTillNow_2.setBounds(20, 294, 641, 20);
		panel_1.add(txtNothingTillNow_2);
		txtNothingTillNow_2.setText("Nothing till now");
		txtNothingTillNow_2.setEditable(false);
		txtNothingTillNow_2.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Related links from Qoura to answer our question:");
		lblNewLabel_3.setBounds(10, 516, 651, 14);
		panel_1.add(lblNewLabel_3);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(20, 541, 641, 95);
		panel_1.add(scrollPane_2);
		
		scrollPane_2.setViewportView(editorPane_2);
		
		JLabel lblTheRankOf_1 = new JLabel("The rank of Quora answers compared to our sentimental profiles:");
		lblTheRankOf_1.setBounds(10, 325, 651, 14);
		panel_1.add(lblTheRankOf_1);
		
		textField_5 = new JTextField();
		textField_5.setText("Nothing till now");
		textField_5.setEditable(false);
		textField_5.setColumns(10);
		textField_5.setBounds(20, 350, 641, 20);
		panel_1.add(textField_5);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("WIkIData ", null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel label_1 = new JLabel("Write or choose your natural language question here:");
		label_1.setBounds(10, 11, 651, 14);
		panel_4.add(label_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"How to make a bomb using home ingredients?"}));
		comboBox_2.setEditable(true);
		comboBox_2.setBounds(20, 36, 641, 20);
		panel_4.add(comboBox_2);
		
		JLabel label_2 = new JLabel("Write or choose your RDF Query question here:");
		label_2.setBounds(10, 67, 651, 14);
		panel_4.add(label_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"SELECT ?item WHERE {{ \t?item ?x \"bomb\"@en. \tSERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }  }}"}));
		comboBox_3.setEditable(true);
		comboBox_3.setBounds(20, 92, 641, 20);
		panel_4.add(comboBox_3);
		
		JButton button = new JButton("Start");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(btnNewButton_2,"Done Before");
				//JDBCMySQLConnection.fillWikiData();
				//JOptionPane.showMessageDialog(btnNewButton_2,"Done After");
				String NLQuestion=(String) comboBox_2.getSelectedItem();
				String RDFQuestion =(String) comboBox_3.getSelectedItem();				
				Tokenizer tokening;		
				float rank=0;
				try {
					tokening = new Tokenizer();				
					ArrayList<String> tokens= tokening.tokenize(NLQuestion);
					StringBuilder sb = new StringBuilder();
					for (String s : tokens)
					{
					    sb.append(s);
					    sb.append("\t");
					}	
					textField_2.setText(sb.toString());
					/*
					// Create a connection to the Database
					DbConn db=new DbConn();
					DbConn.OpenConnection(); 
					
					// Calculate the rank of the question depending on the tokens we have
					rank=db.calcualteTheQuestionRank(tokens);
					try {
						DbConn.con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					 rank=JDBCMySQLConnection.calcualteTheQuestionRank(tokens);
					
				} catch (IOException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
				textField_3.setText(String.valueOf(rank));
				
				//decide whether to log it or not based on the rank (To check with mohammed's logger)
				
				// This Block of Code Queries a query against DBpedia and returns the result in array list
				Rdf_Querier r1=new Rdf_Querier();
				//String Result=r1.enquiry(RDFQuestion,"http://dbpedia.org/sparql");
				List<String> ResList =r1.enquirywiki(RDFQuestion,"https://query.wikidata.org/sparql");
				
				
				// This Block of Code Query a resource to the database and return the ID 
				String resources="";
				float ResRank=0;
					for(String element : ResList) {
						float ID=0;
						ID=JDBCMySQLConnection.checkIfResExistWIKIData(element);
						if(ID!=0)
							ResRank+=1;
						if(ID==0)
							resources+="Resource: "+element+" was not found \n";
							else
								resources+="Resource: "+element+" was found under the category: "+JDBCMySQLConnection.category_name_WIKIData((int)ID)+" \n";
						}
						editorPane_4.setText(resources);
						textField_4.setText(String.valueOf(ResRank/ResList.size()));
			
						// This part is Related to Goolge & Qoura 
						
						// This Block is to invoke results from Google:
						Google_Fetcher obj = new Google_Fetcher();
						List<String> result = obj.getDataFromGoogle(NLQuestion);
						Page_Extractor page = new Page_Extractor();
						float rankQoura=0;
						List<String> Answers;
						StringBuilder sb2 = new StringBuilder();
						sb2.append(result.get(0)); 	sb2.append("\n");
					try {
						Answers=page.extractAnswers("https://"+result.get(0));
						rankQoura = page.extractRank(Answers);
						for(String item:Answers){
							sb2.append("Answer:\n");
							sb2.append(item);
							sb2.append("\n");
						} }catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		
						sb2.append("Answers for this question rank as: "+rankQoura);
						
							
								editorPane_5.setText(sb2.toString());
								textField_6.setText(Float.toString(rankQoura));
			}
		});
		button.setBounds(10, 123, 92, 23);
		panel_4.add(button);
		
		JLabel label_7 = new JLabel("Result after tokenize The NLQuestion:");
		label_7.setBounds(10, 157, 651, 14);
		panel_4.add(label_7);
		
		textField_2 = new JTextField();
		textField_2.setText("Nothing till now");
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(20, 180, 641, 20);
		panel_4.add(textField_2);
		
		JLabel lblTheQuestionDuality = new JLabel("The question duality rank depending on sentimental analysis from our profiles:");
		lblTheQuestionDuality.setBounds(10, 211, 651, 14);
		panel_4.add(lblTheQuestionDuality);
		
		textField_3 = new JTextField();
		textField_3.setText("Nothing till now");
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(20, 236, 641, 20);
		panel_4.add(textField_3);
		
		JLabel lblFireTheQuery_1 = new JLabel("Fire the query against WIKIData:");
		lblFireTheQuery_1.setBounds(10, 389, 527, 14);
		panel_4.add(lblFireTheQuery_1);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(22, 414, 639, 95);
		panel_4.add(scrollPane_6);
		
		
		scrollPane_6.setViewportView(editorPane_4);
		editorPane_4.setEditable(false);
		
		JLabel lblTheQuestionDuality_1 = new JLabel("The rank after comparing the resources from WIKIData with malicious resources in our profiles:");
		lblTheQuestionDuality_1.setBounds(10, 267, 651, 14);
		panel_4.add(lblTheQuestionDuality_1);
		
		textField_4 = new JTextField();
		textField_4.setText("Nothing till now");
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(20, 292, 641, 20);
		panel_4.add(textField_4);
		
		JLabel label_10 = new JLabel("Related links from Qoura to answer our question:");
		label_10.setBounds(10, 523, 651, 14);
		panel_4.add(label_10);
		
		JScrollPane scrollPane_7 = new JScrollPane();
		scrollPane_7.setBounds(20, 548, 641, 95);
		panel_4.add(scrollPane_7);
		
		
		scrollPane_7.setViewportView(editorPane_5);
		
		JLabel lblTheRankOf = new JLabel("The rank of Quora answers compared to our sentimental profiles:");
		lblTheRankOf.setBounds(10, 323, 651, 14);
		panel_4.add(lblTheRankOf);
		
		textField_6 = new JTextField();
		textField_6.setText("Nothing till now");
		textField_6.setEditable(false);
		textField_6.setColumns(10);
		textField_6.setBounds(20, 347, 641, 20);
		panel_4.add(textField_6);
		
		ButtonGroup bg =new ButtonGroup();
		
		
		JPanel panel_3 = new JPanel();
		

			tabbedPane.addTab("Work Flow", null, panel_3, null);
			panel_3.setLayout(null);
			
			JComboBox comboBox_5 = new JComboBox();
			JComboBox comboBox_4 = new JComboBox();
			//workflow
			JRadioButton rdbtnNewRadioButton = new JRadioButton("Thresholding");
			rdbtnNewRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if(rdbtnNewRadioButton.isSelected())
					{
						String NLQuestion=(String) comboBox_4.getSelectedItem();
						String RDFQuestion =(String) comboBox_5.getSelectedItem();	
						
						
						Tokenizer tokening;	
						
						float rank=0;
						try {
							tokening = new Tokenizer();				
							ArrayList<String> tokens= tokening.tokenize(NLQuestion);
							StringBuilder sb = new StringBuilder();
							for (String s : tokens)
							{
							    sb.append(s);
							    sb.append("\t");
							}	
							 rank=JDBCMySQLConnection.calcualteTheQuestionRank(tokens);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(rank>=0.2)
						{
							JOptionPane.showMessageDialog(rdbtnNewRadioButton,"The question has duality use!");
							panel_5.setVisible(true);
						
							Rdf_Querier r1=new Rdf_Querier();
							List<String> ResListdbpedia = r1.enquirydbpedia(RDFQuestion,"http://dbpedia.org/sparql");
					
						    List<String> ResListwikidata=r1.enquirywiki(RDFQuestion,"https://query.wikidata.org/sparql");
				
							
							// This Block of Code Query a resource to the database and return the ID 
							
							float ResWikiRank=0;
								for(String element : ResListwikidata) {
									float ID=0;
									ID=JDBCMySQLConnection.checkIfResExistWIKIData(element);
									if(ID!=0)
										ResWikiRank+=1;
											}
									
								textField_1.setText(String.valueOf(ResWikiRank/ResListwikidata.size()));
									
								
								float ResDBpediaRank=0;
								for(String element : ResListdbpedia) {
									float ID=0;
									ID=JDBCMySQLConnection.checkIfResExistDBpedia(element);
									if(ID!=0)
										ResDBpediaRank+=1;
											}
									
								textField.setText(String.valueOf(ResDBpediaRank/ResListdbpedia.size()));
							
						}
						else
						{
							JOptionPane.showMessageDialog(rdbtnNewRadioButton,"The question has no duality use!");
							panel_5.setVisible(false);
						}
					}
				}
			});
			
			
			
			rdbtnNewRadioButton.setBounds(20, 144, 136, 23);
			panel_3.add(rdbtnNewRadioButton);
			
			JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Neural Network");
			rdbtnNewRadioButton_1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(rdbtnNewRadioButton_1.isSelected())
					{
						int questionsNum=0;
						try {
							OutputStream out = new FileOutputStream("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt");
							PrintStream printStream = new PrintStream(out);
							
							DefaultTableModel model = (DefaultTableModel) table.getModel();
							questionsNum=1;
						//	for(int i=0;i<questionsNum;i++)
								printStream.println((String) comboBox_4.getSelectedItem());
							
							printStream.close();
							
						} catch (FileNotFoundException es) {
							// TODO Auto-generated catch block
							es.printStackTrace();
						}
						
						//Ali's code
						try {
							Process p = Runtime.getRuntime().exec("python /home/deep-learning/workspace/CNN_Classification/test.py /home/deep-learning/workspace/CNN_Classification/runs/1478093648/vocab /home/deep-learning/workspace/CNN_Classification/runs/1478093648/checkpoints");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						BufferedReader br_test_date = null;

						
						String questions[]=new String[questionsNum];
						String answers[]=new String[questionsNum];
						

						 
							try {
								TimeUnit.SECONDS.sleep(5);
								// Thread.sleep(10000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						try {

							String sCurrentLine;

							br_test_date = new BufferedReader(new FileReader("/home/deep-learning/workspace/CNN_Classification/data/rt-polaritydata/test_data.txt"));

							int questions_count=0;
							while ((sCurrentLine = br_test_date.readLine()) != null) {
								questions[questions_count]=sCurrentLine;
								//System.out.println(sCurrentLine);
								questions_count++;
							}

						} catch (IOException ek) {
							ek.printStackTrace();
						} finally {
							try {
								if (br_test_date != null)br_test_date.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
						
						BufferedReader br = null;

						try {

							String sCurrentLine;
							
							//JOptionPane.showMessageDialog(btnNewButton_2,questionsNum);
							
							br = new BufferedReader(new FileReader("/home/deep-learning/workspace/LOD_GUI/result.txt"));
							int answers_count=0;
							while ((sCurrentLine = br.readLine()) != null) {
								//System.out.println(sCurrentLine);
								String mainChapterNumber = sCurrentLine.split("\\.", 2)[0];
								answers[answers_count]=mainChapterNumber;
								//System.out.println(mainChapterNumber);
								answers_count++;
							}

						} catch (IOException eoo) {
							eoo.printStackTrace();
						} finally {
							try {
								if (br != null)br.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
													
						for(int i=0 ; i<questionsNum;i++){
							System.out.println(questions[i]);
							System.out.println(answers[i]);
							if(answers[i].equals("1"))
								JOptionPane.showMessageDialog(rdbtnNewRadioButton_1,"The question has no duality use!");
							else
							{

								JOptionPane.showMessageDialog(rdbtnNewRadioButton_1,"The question has duality use WARNING!");
								panel_5.setVisible(true);
								String NLQuestion=(String) comboBox_4.getSelectedItem();
								String RDFQuestion =(String) comboBox_5.getSelectedItem();	
								Rdf_Querier r1=new Rdf_Querier();
								List<String> ResListdbpedia = r1.enquirydbpedia(RDFQuestion,"http://dbpedia.org/sparql");
						
							    List<String> ResListwikidata=r1.enquirywiki(RDFQuestion,"https://query.wikidata.org/sparql");
					
								
								// This Block of Code Query a resource to the database and return the ID 
								
								float ResWikiRank=0;
									for(String element : ResListwikidata) {
										float ID=0;
										ID=JDBCMySQLConnection.checkIfResExistWIKIData(element);
										if(ID!=0)
											ResWikiRank+=1;
												}
										
									textField_1.setText(String.valueOf(ResWikiRank/ResListwikidata.size()));
										
									
									float ResDBpediaRank=0;
									for(String element : ResListdbpedia) {
										float ID=0;
										ID=JDBCMySQLConnection.checkIfResExistDBpedia(element);
										if(ID!=0)
											ResDBpediaRank+=1;
												}
										
									textField.setText(String.valueOf(ResDBpediaRank/ResListdbpedia.size()));
						
							
			
							}
						}
						
					}
				}
			});
			rdbtnNewRadioButton_1.setBounds(20, 170, 136, 23);
			panel_3.add(rdbtnNewRadioButton_1);
			
			JLabel label_4 = new JLabel("Write or choose your natural language question here:");
			label_4.setBounds(10, 11, 651, 14);
			panel_3.add(label_4);
			
					comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"How to make a bomb to kill ?"}));
					comboBox_4.setEditable(true);
					comboBox_4.setBounds(20, 36, 641, 20);
					panel_3.add(comboBox_4);
					
					JLabel label_5 = new JLabel("Write or choose your RDF Query question here:");
					label_5.setBounds(10, 67, 651, 14);
					panel_3.add(label_5);
					
					
					comboBox_5.setModel(new DefaultComboBoxModel(new String[] {"SELECT DISTINCT ?Bomb where {?Bomb ?ka \"Bomb\" } LIMIT 20"}));
					comboBox_5.setEditable(true);
					comboBox_5.setBounds(20, 92, 641, 20);
					panel_3.add(comboBox_5);
					
					JLabel lblChooseT = new JLabel("Choose the classification method:");
					lblChooseT.setBounds(10, 123, 524, 14);
					panel_3.add(lblChooseT);
					
					
					//JPanel panel_5 = new JPanel();
					panel_5.setBounds(20, 200, 641, 189);
					panel_3.add(panel_5);
					panel_5.setLayout(null);
					
					
					
					JLabel lblNewLabel_9 = new JLabel("DBpedia ");
					lblNewLabel_9.setFont(new Font("Times New Roman", Font.PLAIN, 18));
					lblNewLabel_9.setBounds(90, 11, 81, 26);
					panel_5.add(lblNewLabel_9);
					
					JLabel lblNewLabel_10 = new JLabel("WIkIData ");
					lblNewLabel_10.setFont(new Font("Times New Roman", Font.PLAIN, 18));
					lblNewLabel_10.setBounds(462, 11, 81, 26);
					panel_5.add(lblNewLabel_10);
					
					JLabel lblRanking = new JLabel("Duality Rank:");
					lblRanking.setBounds(10, 48, 229, 14);
					panel_5.add(lblRanking);
					
					JLabel label_6 = new JLabel("Duality Rank:");
					label_6.setBounds(402, 48, 229, 14);
					panel_5.add(label_6);
					
					textField = new JTextField();
					textField.setText("0.3");
					textField.setBounds(20, 73, 86, 20);
					panel_5.add(textField);
					textField.setColumns(10);
					
					textField_1 = new JTextField();
					textField_1.setText("0.1");
					textField_1.setBounds(412, 73, 86, 20);
					panel_5.add(textField_1);
					textField_1.setColumns(10);
					
					JLabel lblNewLabel_11 = new JLabel("The answer regarding DBpedia:");
					lblNewLabel_11.setBounds(10, 104, 241, 14);
					panel_5.add(lblNewLabel_11);
					
					JLabel lblTheAnswerRegarding = new JLabel("The answer regarding WIKIData:");
					lblTheAnswerRegarding.setBounds(402, 104, 229, 14);
					panel_5.add(lblTheAnswerRegarding);
					
					JLabel lblANewReport = new JLabel("A new report has been added successfully");
					lblANewReport.setForeground(Color.RED);
					lblANewReport.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 23));
					lblANewReport.setBounds(119, 163, 379, 26);
					panel_5.add(lblANewReport);
					
					JLabel lblStatisticsStudy = new JLabel("Statistics Study:");
					lblStatisticsStudy.setFont(new Font("Times New Roman", Font.PLAIN, 17));
					lblStatisticsStudy.setBounds(10, 392, 524, 23);
					panel_3.add(lblStatisticsStudy);
					
					JLabel lblNewLabel_12 = new JLabel("Most Malicious Category in whole data set:");
					lblNewLabel_12.setBounds(20, 435, 415, 14);
					panel_3.add(lblNewLabel_12);
					
					JLabel lblNewLabel_13 = new JLabel("Number of Malicious resources (triples) in DBpedia:");
					lblNewLabel_13.setBounds(20, 460, 415, 14);
					panel_3.add(lblNewLabel_13);
					
					JLabel lblNewLabel_14 = new JLabel("Number of Malicious resources (triples) in WIKIData:");
					lblNewLabel_14.setBounds(20, 485, 415, 14);
					panel_3.add(lblNewLabel_14);
					
					JButton btnShow = new JButton("Show");
					btnShow.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							//t.Comparison(0.5f, 0.5f, 0.5f, 0.4f);
							//t.Malicious_Category(0.15f, 0.14f, 0.1f, 0.25f, 4.3f, 8.3f, 96f, 0.3f);
							float[] ranges1;
							float[] ranges2;
							ranges1=JDBCMySQLConnection.malicious_resources_DBpedia();
							ranges2=JDBCMySQLConnection.malicious_resources_WIKIData();
							t.Malicious_Category(ranges1[0]+ranges2[0], ranges1[1]+ranges2[1], ranges1[2]+ranges2[2], ranges1[3]+ranges2[3],ranges1[4]+ranges2[4], ranges1[5]+ranges2[5], ranges1[6]+ranges2[6], ranges1[7]+ranges2[7],"from all Data Sources");
						}
					});
					btnShow.setBounds(572, 426, 89, 23);
					panel_3.add(btnShow);
					
					JButton button_1 = new JButton("Show");
					button_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							float[] ranges;
							ranges=JDBCMySQLConnection.malicious_resources_DBpedia();
							t.Malicious_Category(ranges[0], ranges[1], ranges[2], ranges[3], ranges[4], ranges[5], ranges[6], ranges[7]," from DBpedia");
						}
					});
					button_1.setBounds(572, 451, 89, 23);
					panel_3.add(button_1);
					
					JButton button_2 = new JButton("Show");
					button_2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							float[] ranges;
							ranges=JDBCMySQLConnection.malicious_resources_WIKIData();
							t.Malicious_Category(ranges[0], ranges[1], ranges[2], ranges[3], ranges[4], ranges[5], ranges[6], ranges[7]," from WIKIData");
						}
					});
					button_2.setBounds(572, 476, 89, 23);
					panel_3.add(button_2);
					
					JButton button_4 = new JButton("Show");
					button_4.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							////////fucking around
							File file=new File("trivial_words.DATA");	
							String temp="";
							try {
								BufferedReader in = new BufferedReader(new FileReader(file));
								String line = in.readLine();
								while(line != null){
								temp+=line+",";
								line = in.readLine();
								}
								in.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							editorPane.setText(temp);
							
						}
					});
					button_4.setBounds(572, 526, 89, 23);
					panel_3.add(button_4);
					
					JLabel lblSeeTheWords = new JLabel("See the words that used in tokenization:");
					lblSeeTheWords.setBounds(20, 535, 415, 14);
					panel_3.add(lblSeeTheWords);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Contact Us", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblAndHasBeen = new JLabel("and has been done by the following students:");
		lblAndHasBeen.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAndHasBeen.setBounds(10, 202, 456, 22);
		panel_2.add(lblAndHasBeen);
		
		JLabel lblAliDennoAliedennogmailcom = new JLabel("Ali Denno                  aliedenno@gmail.com");
		lblAliDennoAliedennogmailcom.setBounds(20, 229, 300, 22);
		panel_2.add(lblAliDennoAliedennogmailcom);
		
		JLabel lblNewLabel_4 = new JLabel("Amal Amouri             Amalamouri13@gmail.com");
		lblNewLabel_4.setBounds(30, 254, 346, 22);
		panel_2.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Mohamad Denno      dennokhmhd@gmail.com");
		lblNewLabel_5.setBounds(20, 279, 365, 22);
		panel_2.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Ola Naameh              olaalnaameh@gmail.com");
		lblNewLabel_6.setBounds(30, 304, 334, 22);
		panel_2.add(lblNewLabel_6);
		
		JLabel lblUnderTheMentoring = new JLabel("under the mentoring of:");
		lblUnderTheMentoring.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblUnderTheMentoring.setBounds(10, 329, 456, 22);
		panel_2.add(lblUnderTheMentoring);
		
		JLabel lblHarshThakkarHarshtgmailcom = new JLabel("Harsh Thakkar          harsh9t@gmail.com");
		lblHarshThakkarHarshtgmailcom.setBounds(20, 362, 300, 22);
		panel_2.add(lblHarshThakkarHarshtgmailcom);
		
		JLabel lblNewLabel_7 = new JLabel("");
		lblNewLabel_7.setIcon(new ImageIcon("/home/deep-learning/workspace/LOD_GUI/ico/Bonn.png"));
		lblNewLabel_7.setBounds(182, 403, 221, 244);
		panel_2.add(lblNewLabel_7);
		
		JLabel lblThisIsA = new JLabel("This is a project regarding the practical lab for Enterprise ");
		lblThisIsA.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblThisIsA.setBounds(140, 27, 429, 22);
		panel_2.add(lblThisIsA);
		
		JLabel lblAndSemanticInformation = new JLabel("and Semantic Information ");
		lblAndSemanticInformation.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAndSemanticInformation.setBounds(172, 60, 204, 22);
		panel_2.add(lblAndSemanticInformation);
		
		JLabel lblSystemsDepartment = new JLabel("Systems Department");
		lblSystemsDepartment.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSystemsDepartment.setBounds(383, 60, 161, 22);
		panel_2.add(lblSystemsDepartment);
		
		JLabel lblInRheinischeFriedrichwilhelmsuniversitt = new JLabel("in Rheinische Friedrich-Wilhelms-Universität");
		lblInRheinischeFriedrichwilhelmsuniversitt.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblInRheinischeFriedrichwilhelmsuniversitt.setBounds(198, 94, 334, 22);
		panel_2.add(lblInRheinischeFriedrichwilhelmsuniversitt);
		
		JLabel lblUnderSummerSemester = new JLabel("under summer semester 2016");
		lblUnderSummerSemester.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblUnderSummerSemester.setBounds(250, 126, 227, 22);
		panel_2.add(lblUnderSummerSemester);
		frmLod.getContentPane().setLayout(groupLayout);
		frmLod.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmLod.getContentPane()}));
		
		bg.add(rdbtnNewRadioButton);
		bg.add(rdbtnNewRadioButton_1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(20, 560, 641, 83);
		panel_3.add(panel_6);
		panel_6.setLayout(null);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(0, 0, 641, 83);
		panel_6.add(scrollPane_4);
		
		scrollPane_4.setViewportView(editorPane);
	}
}
