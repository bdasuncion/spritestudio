package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class CreateNewFile extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1681395367584655149L;
	private JSpinner widthOptions;
	private JSpinner heightOptions;
	private JComboBox pixelSizeOptions;
	private JComboBox paletteDepthOptions;
	private JButton okButton,cancelButton;
	private int width,height,imageDepth,paletteDepth;
	private boolean OKStatus;
	
	private final static String COMMAND_OK = "OK";
	private final static String COMMAND_CANCEL = "CANCEL";
	
	public CreateNewFile(JFrame owner)
	{
		super(owner, true);
		
		SpinnerNumberModel numberModelWidth = new SpinnerNumberModel(8,8,256,8); 
		SpinnerNumberModel numberModelHeight = new SpinnerNumberModel(8,8,256,8); 
		
		widthOptions = new JSpinner(numberModelWidth);
		heightOptions = new JSpinner(numberModelHeight);
		
		String pixelSize[] = new String[2];
		pixelSize[0] = "4";
		pixelSize[1] = "8";
		
		pixelSizeOptions = new JComboBox(pixelSize);
		
		String paletteDepth[] = new String[2];
		paletteDepth[0] = "16";
		paletteDepth[1] = "32";
		
		paletteDepthOptions = new JComboBox(paletteDepth);
		
		widthOptions.setVisible(true);
		heightOptions.setVisible(true);
		pixelSizeOptions.setVisible(true);
		paletteDepthOptions.setVisible(true);
		
		okButton = new JButton("OK");
		okButton.setActionCommand(COMMAND_OK);
		okButton.addActionListener(new buttonPress());
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(COMMAND_CANCEL);
		cancelButton.addActionListener(new buttonPress());
		
		JPanel panelOptions1;
		panelOptions1 = new JPanel();
		
		panelOptions1.add(new JLabel("Width"));
		panelOptions1.add(widthOptions);
		panelOptions1.add(new JLabel("Height"));
		panelOptions1.add(heightOptions);
		
		JPanel panelOptions2;
		panelOptions2 = new JPanel();
		
		panelOptions2.add(new JLabel("Pixel Size"));
		panelOptions2.add(pixelSizeOptions);
		panelOptions2.add(new JLabel("Palette Size"));
		panelOptions2.add(paletteDepthOptions);
		
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
		panelOptions1.setVisible(true);
		panelOptions2.setVisible(true);
		panelButtons.setVisible(true);
		
		JPanel panelNewFile = new JPanel();
		panelNewFile.setLayout(new GridLayout(3,1));
		panelNewFile.add(panelOptions1);
		panelNewFile.add(panelOptions2);
		panelNewFile.add(panelButtons);
		
		
		this.add(panelNewFile);
		this.setSize(300,200);
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
	}
	
	public int getImageHeight() 
	{
		return height;
	}

	public int getPixelSize() 
	{
		return imageDepth;
	}

	public int getPaletteDepth() 
	{
		return paletteDepth;
	}

	public int getImageWidth() 
	{
		return width;
	}
	
	public boolean getOKStaus()
	{
		return OKStatus;
	}
	
	private class buttonPress implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			command(e.getActionCommand());	
		}	
	}
	
	private void command(String command)
	{
		if(command.contentEquals(COMMAND_OK)) {
			this.setVisible(false);
			width = ((SpinnerNumberModel) widthOptions.getModel()).getNumber().intValue();
			height = ((SpinnerNumberModel) heightOptions.getModel()).getNumber().intValue();
			imageDepth = Integer.parseInt((String)pixelSizeOptions.getSelectedItem());
			paletteDepth = Integer.parseInt((String)paletteDepthOptions.getSelectedItem());
			OKStatus = true;
		} else {
			this.setVisible(false);
			OKStatus = false;
		}
	}
}