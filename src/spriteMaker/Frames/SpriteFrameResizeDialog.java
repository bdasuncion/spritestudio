package spriteMaker.Frames;

import java.awt.Frame;
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

public class SpriteFrameResizeDialog extends JDialog implements ActionListener {
	private JSpinner widthOptions;
	private JSpinner heightOptions;
	private JButton okButton,cancelButton;
	private int width,height;
	private boolean OKStatus;
	private final static String COMMAND_OK = "OK";
	private final static String COMMAND_CANCEL = "CANCEL";
	
	SpriteFrameResizeDialog(JFrame owner) {
		super(owner, true);
		SpinnerNumberModel numberModelWidth = new SpinnerNumberModel(8,8,256,8); 
		SpinnerNumberModel numberModelHeight = new SpinnerNumberModel(8,8,256,8); 
		
		widthOptions = new JSpinner(numberModelWidth);
		heightOptions = new JSpinner(numberModelHeight);
		
		okButton = new JButton("OK");
		okButton.setActionCommand(COMMAND_OK);
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(COMMAND_CANCEL);
		cancelButton.addActionListener(this);
		
		JPanel panelOptions;
		panelOptions = new JPanel();
		
		panelOptions.add(new JLabel("Width"));
		panelOptions.add(widthOptions);
		panelOptions.add(new JLabel("Height"));
		panelOptions.add(heightOptions);
		
		JPanel panelButtons;
		panelButtons = new JPanel();
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		
		panelOptions.setVisible(true);
		panelOptions.setVisible(true);
		
		JPanel panelOptionsMain = new JPanel();
		panelOptionsMain.setLayout(new GridLayout(3,1));
		panelOptionsMain.add(panelOptions);
		panelOptionsMain.add(panelButtons);
		
		this.add(panelOptionsMain);
		this.setSize(300,200);
		this.setResizable(false);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		command(e.getActionCommand());	
	}
	
	private void command(String command) {
		if(command.contentEquals(COMMAND_OK)) {
			width = ((SpinnerNumberModel) widthOptions.getModel()).getNumber().intValue();
			height = ((SpinnerNumberModel) heightOptions.getModel()).getNumber().intValue();
			OKStatus = true;
		}
		else {
			OKStatus = false;
		}
		this.setVisible(false);
	}

	
	public void showDialog(int currentWidth, int currentHeight) {
		widthOptions.getModel().setValue(currentWidth);
		heightOptions.getModel().setValue(currentHeight);
		
		setVisible(true);
	}
	public boolean getOkStatus() {
		return OKStatus;
	}
	
	public int getResizeWidth() {
		return width;
	}
	
	public int getResizeHeight() {
		return height;
	}
}
