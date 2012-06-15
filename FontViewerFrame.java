import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
   This frame contains a combo box, check boxes and radio buttons
   to change the font of the text.
 */
public class FontViewerFrame extends JFrame
{
	private static final int FIELD_WIDTH = 450, FIELD_HEIGHT = 100;

	private JLabel sampleField, sampleImage, infoLabel;
	private JPanel samplePanel;
	private JComboBox facenameCombo;
	private JCheckBox italicCheckBox, boldCheckBox;
	private JSlider sizeSlider;
	private Color defaultColor,newColor;
	private ActionListener listener;
	private int fontStyle;

	/**
     Constructs the frame.
	 */
	public FontViewerFrame() 
	{  
		setSize(FIELD_WIDTH,FIELD_WIDTH);
		// Construct text sample
		sampleField = new JLabel("Big Java",SwingConstants.CENTER);
		sampleField.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		sampleImage = new JLabel(new ImageIcon("BigJava.jpg"),SwingConstants.CENTER);

		samplePanel = new JPanel();
		defaultColor = samplePanel.getBackground();
		newColor = defaultColor;
		samplePanel.setLayout(new BorderLayout());
		samplePanel.add(sampleField, BorderLayout.CENTER);
		samplePanel.add(sampleImage, BorderLayout.SOUTH);

		add(samplePanel, BorderLayout.CENTER);

		// This listener is shared among all components
		listener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{  
				setFontAction();
			}
		};

		createControlPanel();
		setFontAction();

		pack();

	}    

	/**
     Creates the control panel to change the font.
	 */
	private void createControlPanel() 
	{

		// Creates the control panel and line up component panels
		JPanel controlPanel = new JPanel(new GridLayout(3, 1));
		controlPanel.add(createFaceNamePanel());
		controlPanel.add(createStyleGroupPanel());
		controlPanel.add(createSizeGroupPanel());

		// Add panel to content pane
		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new GridLayout(1,1));
		borderPanel.setBorder(new EmptyBorder(20,20,20,20));
		borderPanel.add(controlPanel);
		add(borderPanel, BorderLayout.SOUTH);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2,1));
		topPanel.add(addJMenu());
		topPanel.add(addJToolbar());

		add(topPanel, BorderLayout.NORTH);

		pack();
	}

	/**
     Creates the combo box with the font style choices.
     @return the panel containing the combo box
	 */
	private JPanel createFaceNamePanel() 
	{
		facenameCombo = new JComboBox();
		facenameCombo.addItem("Serif");
		facenameCombo.addItem("SansSerif");
		facenameCombo.addItem("Monospaced");
		facenameCombo.setEditable(true);
		facenameCombo.addActionListener(listener);

		JPanel flowPanel = new JPanel();
		flowPanel.add(facenameCombo);

		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new BorderLayout());
		borderPanel.add(new JLabel(" "), BorderLayout.NORTH);
		borderPanel.add(addJInfoLabel(),BorderLayout.CENTER);
		borderPanel.add(new JLabel(" "), BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(borderPanel, BorderLayout.NORTH);
		panel.add(flowPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
     Creates the check boxes for selecting bold and italic styles.
     @return the panel containing the check boxes
	 */
	private JPanel createStyleGroupPanel() 
	{
		italicCheckBox = new JCheckBox("Italic");
		italicCheckBox.setHorizontalAlignment(JCheckBox.RIGHT);
		italicCheckBox.addActionListener(listener);
		boldCheckBox = new JCheckBox("Bold");
		boldCheckBox.addActionListener(listener);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(italicCheckBox);
		panel.add(boldCheckBox);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Style"));

		return panel;
	}

	/**
     Creates the radio buttons to select the font size
     @return the panel containing the radio buttons
	 */
	private JPanel createSizeGroupPanel() 
	{

		sizeSlider = new JSlider(10, 72);
		sizeSlider.setValue(40);
		sizeSlider.setMajorTickSpacing(10);
		sizeSlider.setMinorTickSpacing(2);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		sizeSlider.setToolTipText("Font size");
		add(sizeSlider, BorderLayout.EAST);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(sizeSlider);
		sizeSlider.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent arg0) 
			{
				setFontAction();

			}
		});
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Size"));

		return panel;
	}

	/**
	 * Creates a JMenu
	 * @return The JMenu
	 */
	private JMenuBar addJMenu() 
	{

		JMenuBar jMenuBar;
		JMenu file,color;
		JMenuItem save,exit,setCol,resetCol;

		jMenuBar = new JMenuBar();

		file = new JMenu("File");
		file.setMnemonic('M');


		save = new JMenuItem("Save", new ImageIcon("save.gif"));
		save.setMnemonic('S');
		save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		exit = new JMenuItem("Exit", new ImageIcon("exit.gif") );
		exit.setMnemonic('E');
		exit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitAction();				
			}
		});

		color = new JMenu("Color");

		setCol = new JMenuItem("Set color");
		setCol.setMnemonic('C');
		setCol.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		setCol.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setColorAction();				
			}
		});
		resetCol = new JMenuItem("Reset Color");
		resetCol.setMnemonic('R');
		resetCol.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		resetCol.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetColorAction();				
			}
		});

		jMenuBar.add(file);

		file.add(save);
		file.add(exit);  
		file.addSeparator();
		color.add(setCol);
		color.add(resetCol);
		file.add(color);


		return jMenuBar;
	}

	/**
	 * Creates the JToolBar
	 * @return The JToolBar
	 */
	private JToolBar addJToolbar()
	{

		JToolBar jToolBar;
		JButton save,exit,setCol,resetCol;
		jToolBar = new JToolBar();
		jToolBar.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		save = new JButton("Save", new ImageIcon("save.gif") );
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		exit = new JButton("Exit", new ImageIcon("exit.gif") );
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitAction();				
			}
		});
		setCol = new JButton("Set color");
		setCol.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setColorAction();				
			}
		});
		resetCol = new JButton("Reset Color");
		resetCol.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetColorAction();				
			}
		});

		c.gridx = 1;
		c.ipadx = 20;
		c.insets = new Insets(0, 0, 0, 15);
		c.anchor = GridBagConstraints.NORTHWEST;
		jToolBar.add(save,c);
		c.gridx = 2;
		c.insets = new Insets(0, 0, 0, 0);
		jToolBar.add(setCol,c);
		c.gridx = 3;
		jToolBar.add(resetCol,c);
		c.gridx = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.weightx = 1.0;
		c.weighty = 1.0;
		jToolBar.add(exit,c);        

		return jToolBar;
	}

	/**
	 * Creates a JLabel for storing informations
	 * @return the JLabel
	 */
	private JLabel addJInfoLabel()
	{
		infoLabel = new JLabel("");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		Border paddingBorder = BorderFactory.createEmptyBorder(5,5,5,5);
		Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		infoLabel.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		return infoLabel;
	}


	/* Actions */

	/**
     Gets user choice for font name, style, and size
     and sets the font of the text sample.
	 */
	private void setFontAction() 
	{  
		// Get font name   
		String facename = (String)facenameCombo.getSelectedItem();

		// Get font style
		fontStyle = 0;
		if (italicCheckBox.isSelected()) 
			fontStyle = fontStyle + Font.ITALIC;
		if (boldCheckBox.isSelected()) 
			fontStyle = fontStyle + Font.BOLD;

		// Get font size   
		int size = 0;
		size = sizeSlider.getValue();

		// Set font of text field
		sampleField.setFont(new Font(facename, fontStyle, size));
		infoLabel.setText("Font size: "+size);
	}

	/**
	 * Sets the chosen file location to the InfoLabel
	 */
	private void saveAction()
	{
		String filename = File.separator+"tmp";
		JFileChooser jFileChooser = new JFileChooser(new File(filename));

		// Show save dialog
		int fileInt = jFileChooser.showSaveDialog(this);
		File selFile = jFileChooser.getSelectedFile();

		if(fileInt == JFileChooser.APPROVE_OPTION)
		{

			OutputStream output;
			try {
				output = new FileOutputStream(selFile);
				outputWrite(output);
			} 
			catch (FileNotFoundException e) 
			{
				File file = new File(selFile.getPath());

				// Create file if it does not exist
				try 
				{
					file.createNewFile();
					output = new FileOutputStream(selFile);
					outputWrite(output);

				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}	
			}

			infoLabel.setText("Saved to: "+selFile.toString());

		}
	}

	/**
	 * Writes to the file
	 */
	private void outputWrite(OutputStream output)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		Font font = new Font((String)facenameCombo.getSelectedItem(), fontStyle, sizeSlider.getValue());

		String outputString = "Title: Backup " + dateFormat.format(date)+"\nFont: " + font.toString();
		outputString += "\nBackground color: " + newColor.toString();
		try 
		{
			output.write(outputString.getBytes());
			output.close();	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	/**
	 * Closes the Application
	 */
	private void exitAction() 
	{
		System.exit(0);
	}

	/**
	 * sets the background color to the chosen one
	 */
	private void setColorAction() 
	{
		JColorChooser myChooser = new JColorChooser();		
		newColor = myChooser.showDialog(this, "Choose Color", Color.BLACK);

		
		samplePanel.setBackground(newColor); 
		infoLabel.setText("Color set to: "+newColor.getRed()+" "+newColor.getGreen()+" "+newColor.getBlue());
	}

	/**
	 * resets the background color to the default one
	 */
	private void resetColorAction() 
	{
		samplePanel.setBackground(defaultColor);
		infoLabel.setText("Color set to: "+defaultColor.getRed()+" "+defaultColor.getGreen()+" "+defaultColor.getBlue());
	}

}
