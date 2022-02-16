package multiSprite.Canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.text.NumberFormatter;

import componentInterface.ArrangementPanelInterface;
import componentInterface.ButtonCommandsInterface;
import spriteMaker.Frames.Layer.SpriteLayerPanel;

public class MultiSpriteButtonPanelArrangement extends JPanel implements ActionListener,
    ItemListener, ArrangementPanelInterface, ChangeListener{
	
	//JSpinner drawPriority;
	private JSpinner displayForNumberOfFrames;
	private JCheckBox showAllLayers;
	private JButton showPanelPosition;
	private JButton showPanelLayers;
	private JCheckBox flipVertical;
	private JCheckBox flipHorizontal;
	private ButtonCommandsInterface buttonCommandsCallback;
	
	public static final String FLIP_VERTICAL = "FLIP VERTICAL";
	public static final String NORMAL_VERTICAL = "NORMAL VERTICAL";
	public static final String FLIP_HORIZONTAL = "FLIP HORIZONTAL";
	public static final String NORMAL_HORIZONTAL = "NORMAL HORIZONTAL";
	public static final String SET_FRAMES_DISPLAY = "SET_FRAMES_DISPLAY:";
	
	public MultiSpriteButtonPanelArrangement(ButtonCommandsInterface buttonCommands) {
		super();
		
		buttonCommandsCallback = buttonCommands;
		//SpinnerNumberModel numberModelPriority = new SpinnerNumberModel(0,0,null,1);
		//drawPriority = new JSpinner(numberModelPriority);
		//drawPriority.addChangeListener(this);
		//setSpinnerFormats(drawPriority);
		
		SpinnerNumberModel numberModelDisplayFrames = new SpinnerNumberModel(0,0,null,1);
		displayForNumberOfFrames = new JSpinner(numberModelDisplayFrames);
		displayForNumberOfFrames.addChangeListener(this);
		setSpinnerFormats(displayForNumberOfFrames);
		JLabel displayFramesLabel = new JLabel("Display for");
		JLabel displayFramesLabel2 = new JLabel("/60 frames");
		
		flipVertical = new JCheckBox();
		flipVertical.addItemListener(this);
		flipHorizontal = new JCheckBox();
		flipHorizontal.addItemListener(this);
		JLabel flipVerticalLabel = new JLabel("Flip vertical");
		JLabel flipHorizontalLabel = new JLabel("Flip horizontal");
		
		showPanelPosition = new JButton("<<");
		showPanelPosition.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_POSITION);
		showPanelPosition.addActionListener(this);
		
		showPanelLayers = new JButton(">>");
		showPanelLayers.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_LAYERS);
		showPanelLayers.addActionListener(this);
		
		GroupLayout groupLayout = new GroupLayout(this);
		this.setLayout(groupLayout);
		
		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
				.addGroup(
				    groupLayout.createParallelGroup()
				        .addComponent(displayFramesLabel)
				        .addComponent(flipVerticalLabel))
				.addGroup(
					groupLayout.createParallelGroup()
					    .addComponent(displayForNumberOfFrames)
					    .addGroup(groupLayout.createSequentialGroup()
					    	.addComponent(flipVertical)
					    	.addComponent(flipHorizontalLabel))
					    .addGroup(groupLayout.createSequentialGroup()
					    	.addComponent(showPanelPosition)
					    	.addComponent(showPanelLayers)))
				.addGroup(
					groupLayout.createParallelGroup()
						.addComponent(flipHorizontal))
				.addComponent(displayFramesLabel2));
		
		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup()
						    .addComponent(displayFramesLabel)
						    .addComponent(displayForNumberOfFrames)
						    .addComponent(displayFramesLabel2))
				.addGroup(
						groupLayout.createParallelGroup()
						    .addComponent(flipVerticalLabel)
						    .addComponent(flipVertical)
						    .addComponent(flipHorizontalLabel)
						    .addComponent(flipHorizontal))
				.addGroup(
						groupLayout.createParallelGroup()
						    .addComponent(showPanelPosition)
						    .addComponent(showPanelLayers)));
		this.setVisible(false);
	}
	
	private void setSpinnerFormats(JSpinner spinner) {
		NumberEditor editor = (NumberEditor) spinner.getEditor();
		NumberFormatter numberFormatter = (NumberFormatter) editor.getTextField().getFormatter();
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setCommitsOnValidEdit(true);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		buttonCommandsCallback.onButtonPressed(actionEvent.getActionCommand());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == flipHorizontal) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				buttonCommandsCallback.onButtonPressed(FLIP_HORIZONTAL);
			} else {
				buttonCommandsCallback.onButtonPressed(NORMAL_HORIZONTAL);
			}
		}
		
		if (e.getSource() == flipVertical) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				buttonCommandsCallback.onButtonPressed(FLIP_VERTICAL);
			} else {
				buttonCommandsCallback.onButtonPressed(NORMAL_VERTICAL);
			}
		}
	}

	@Override
	public void onSetFrame(boolean flipVertical, boolean flipHorizontal, int displayForFrames) {
		this.flipVertical.setSelected(flipVertical);
		this.flipHorizontal.setSelected(flipHorizontal);
		this.displayForNumberOfFrames.setValue(displayForFrames);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == displayForNumberOfFrames) {
			buttonCommandsCallback.onButtonPressed(SET_FRAMES_DISPLAY + displayForNumberOfFrames.getValue());
		}
	}

}
