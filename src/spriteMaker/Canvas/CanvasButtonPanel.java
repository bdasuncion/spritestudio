package spriteMaker.Canvas;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;

import componentInterface.CanvasButtonPanelInterface;

public class CanvasButtonPanel extends JPanel 
implements ActionListener, ItemListener, CanvasButtonPanelInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7732009389138106661L;
	JPanel mainPanelImageEdit;
	JPanel mainPanelImagePosition;
	JPanel mainPanelImageLayer;
	JButton up;
	JButton down;
	JButton left;
	JButton right;
	JButton offset_up;
	JButton offset_down;
	JButton offset_left;
	JButton offset_right;
	JButton flipVertical;
	JButton flipHorizontal;
	JButton resizeTileBG;
	JButton switchPositionToEdit;
	JButton switchPositionToLayer;
	JButton switchEditToPosition;
	JButton switchEditToLayer;
	JButton switchLayerToEdit;
	JButton switchLayerToPosition;
	JRadioButton pixel;
	JRadioButton tile;
	JCheckBox showAllFrames;
	JSpinner tileWidthSize;
	JSpinner tileHeightSize;
	JSpinner width;
	JSpinner height;
	JCheckBox transparentBG;
	JCheckBox gridLinesVisible;
	BufferedImage image;
	SpriteCanvas spriteCanvas;
	JRadioButton drawMode;
	JRadioButton copyMode;
	static final String SWITCH_TO_POSITION = "switch_position";
	static final String SWITCH_TO_EDIT = "switch_edit";
	static final String SWITCH_TO_LAYER = "switch_layer";
	
	public static final String INCREASE_TRANSPARENCY = "increase_tranparency";
	public static final String DECREASE_TRANSPARENCY = "decrease_tranparency";
	
	public CanvasButtonPanel(SpriteCanvas canvas) {
		super();
		spriteCanvas = canvas;
		spriteCanvas.setCanvasButtonPanelInterface(this);
		//build main panel image edit
		up = new JButton("Shift Up");
		up.setActionCommand("UP");
		up.addActionListener(this);
		down = new JButton("Shift Down");
		down.setActionCommand("DOWN");
		down.addActionListener(this);;
		left = new JButton("Shift Left");
		left.setActionCommand("LEFT");
		left.addActionListener(this);
		right = new JButton("Shift Right");
		right.setActionCommand("RIGHT");
		right.addActionListener(this);
		flipVertical = new JButton("Flip Vertical");
		flipVertical.setActionCommand("FLIPV");
		flipVertical.addActionListener(this);
		flipHorizontal = new JButton("Flip Horizontal");
		flipHorizontal.setActionCommand("FLIPH");
		flipHorizontal.addActionListener(this);
		
		ChangeValue changeValue = new ChangeValue();
		SpinnerNumberModel widthModel = new SpinnerNumberModel(1, 1, 256, 1);
		width = new JSpinner(widthModel);
		width.addChangeListener(changeValue);
		setSpinnerFormats(width);
		
		SpinnerNumberModel heightModel = new SpinnerNumberModel(1, 1, 256, 1);
		height = new JSpinner(heightModel);
		height.addChangeListener(changeValue);
		setSpinnerFormats(height);
		
		pixel = new JRadioButton("Pixel");
		pixel.setActionCommand("pixel");
		pixel.addActionListener(this);
		tile = new JRadioButton("Tile");
		tile.setActionCommand("tile");
		tile.addActionListener(this);
		tile.setSelected(true);
		ButtonGroup tilePixelBGgroup = new ButtonGroup();
		tilePixelBGgroup.add(pixel);		
		tilePixelBGgroup.add(tile);
		
		transparentBG = new JCheckBox("transparent background");
		transparentBG.addItemListener(this);
		gridLinesVisible = new JCheckBox("grid lines");
		gridLinesVisible.addItemListener(this);
		
		SpinnerNumberModel numberModelWidth = new SpinnerNumberModel(8,1,256,1); 
		SpinnerNumberModel numberModelHeight = new SpinnerNumberModel(8,1,256,1);
		
		tileWidthSize = new JSpinner(numberModelWidth);
		tileHeightSize = new JSpinner(numberModelHeight);
		
		tileWidthSize.addChangeListener(new resizeBGTilesWidth());
		tileHeightSize.addChangeListener(new resizeBGTilesHeight());
		
		setSpinnerFormats(tileWidthSize);
		setSpinnerFormats(tileHeightSize);
		
		drawMode = new JRadioButton("Draw");
		drawMode.setActionCommand("draw");
		drawMode.addActionListener(this);
		copyMode = new JRadioButton("Copy");
		copyMode.setActionCommand("copy");
		copyMode.addActionListener(this);
		drawMode.setSelected(true);
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(drawMode);		
		modeGroup.add(copyMode);
		
		JPanel bgDesignControl = new JPanel();
		GroupLayout bgDesignControlLayout = new GroupLayout(bgDesignControl);
		bgDesignControl.setLayout(bgDesignControlLayout);
		bgDesignControlLayout.setHorizontalGroup(bgDesignControlLayout.createSequentialGroup().
			addComponent(pixel).
			addComponent(tile).
			addComponent(tileWidthSize).
			addComponent(tileHeightSize));
		
		bgDesignControlLayout.setVerticalGroup(bgDesignControlLayout.createParallelGroup()
			.addComponent(pixel).
			addComponent(tile).
			addComponent(tileWidthSize).
			addComponent(tileHeightSize));
		
		JPanel bgPanel = new JPanel();
		bgPanel.add(transparentBG);
		bgPanel.add(gridLinesVisible);
		
		JPanel modeOptionsPanel = new JPanel();
		modeOptionsPanel.add(drawMode);
		modeOptionsPanel.add(copyMode);
		JLabel widthLabel = new JLabel("width");
		modeOptionsPanel.add(widthLabel);
		modeOptionsPanel.add(width);
		JLabel heightLabel = new JLabel("height");
		modeOptionsPanel.add(heightLabel);
		modeOptionsPanel.add(height);
		
		
		JPanel switchFromLayer = new JPanel();
		switchLayerToPosition = new JButton("<<");
		switchLayerToPosition.setActionCommand(SWITCH_TO_POSITION);
		switchLayerToPosition.addActionListener(this);
		switchLayerToEdit = new JButton(">>");
		switchLayerToEdit.setActionCommand(SWITCH_TO_EDIT);
		switchLayerToEdit.addActionListener(this);
		switchFromLayer.add(switchLayerToPosition);
		switchFromLayer.add(switchLayerToEdit);
		
		//build layer control panel
		JPanel bgControl = new JPanel();
		GroupLayout bgControlLayout = new GroupLayout(bgControl);
		bgControl.setLayout(bgControlLayout);
		bgControlLayout.setHorizontalGroup(bgControlLayout.createParallelGroup().
			//addComponent(drawPriorityPanel).
			addComponent(bgDesignControl).
			addComponent(bgPanel).
			addComponent(modeOptionsPanel).
			addComponent(switchFromLayer));
		bgControlLayout.setVerticalGroup(bgControlLayout.createSequentialGroup().
			//addComponent(drawPriorityPanel).
			addComponent(bgDesignControl).
			addComponent(bgPanel).
			addComponent(modeOptionsPanel).
			addComponent(switchFromLayer));
		
		mainPanelImageLayer = new JPanel();
		mainPanelImageLayer.add(bgControl);
		
		mainPanelImageLayer.setVisible(true);
		this.add(mainPanelImageLayer);
		
		JPanel flipperPanel = new JPanel();
		GroupLayout flipperLayout = new GroupLayout(flipperPanel);
		flipperPanel.setLayout(flipperLayout);
		flipperLayout.setHorizontalGroup(flipperLayout.createSequentialGroup()
			.addComponent(flipVertical).addComponent(flipHorizontal));
		flipperLayout.setVerticalGroup(flipperLayout.createParallelGroup()
			.addComponent(flipVertical).addComponent(flipHorizontal));
		
		JPanel upButtonPanel = new JPanel();
		upButtonPanel.add(up);
		
		JPanel downButtonPanel = new JPanel();
		downButtonPanel.add(down);

//		build main panel image editing
		JPanel switchPanelFromEdit = new JPanel();
		switchEditToLayer = new JButton("<<");
		switchEditToLayer.setActionCommand(SWITCH_TO_LAYER);
		switchEditToLayer.addActionListener(this);
		switchEditToPosition = new JButton(">>");
		switchEditToPosition.setActionCommand(SWITCH_TO_POSITION);
		switchEditToPosition.addActionListener(this);
		switchPanelFromEdit.add(switchEditToLayer);
		switchPanelFromEdit.add(switchEditToPosition);
		
		mainPanelImageEdit = new JPanel();
		GroupLayout buttonPanelLayout = new GroupLayout(mainPanelImageEdit);
		mainPanelImageEdit.setLayout(buttonPanelLayout);
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createSequentialGroup()
			.addComponent(left)
			.addGroup(buttonPanelLayout.createParallelGroup()
			.addComponent(upButtonPanel).addComponent(flipperPanel)
			.addComponent(downButtonPanel).addComponent(switchPanelFromEdit))
			.addComponent(right));
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup()
			.addComponent(upButtonPanel).addGroup(buttonPanelLayout.createParallelGroup()
			.addComponent(left).addComponent(flipperPanel).addComponent(right))
			.addComponent(downButtonPanel).addComponent(switchPanelFromEdit));
		
		mainPanelImageEdit.setVisible(false);
		this.add(mainPanelImageEdit);
		
		offset_right = new JButton("+ALPHA");
		offset_right.setActionCommand(INCREASE_TRANSPARENCY);
		offset_right.addActionListener(this);
		offset_left = new JButton("-ALPHA");
		offset_left.setActionCommand(DECREASE_TRANSPARENCY);
		offset_left.addActionListener(this);
		
		mainPanelImagePosition = new JPanel();
		GroupLayout buttonPanelPositionLayout = new GroupLayout(mainPanelImagePosition);
		mainPanelImagePosition.setLayout(buttonPanelPositionLayout);
		
		JPanel switchPanelFromPosition = new JPanel();
		switchPositionToEdit = new JButton("<<");
		switchPositionToEdit.setActionCommand(SWITCH_TO_EDIT);
		switchPositionToEdit.addActionListener(this);
		switchPositionToLayer = new JButton(">>");
		switchPositionToLayer.setActionCommand(SWITCH_TO_LAYER);
		switchPositionToLayer.addActionListener(this);
		switchPanelFromPosition.add(switchPositionToEdit);
		switchPanelFromPosition.add(switchPositionToLayer);
		
		buttonPanelPositionLayout.setHorizontalGroup(
				buttonPanelPositionLayout.createSequentialGroup().
				addComponent(offset_left).addGroup(
				buttonPanelPositionLayout.createParallelGroup().
				addComponent(switchPanelFromPosition)).
				addComponent(offset_right));
		
		buttonPanelPositionLayout.setVerticalGroup(
				buttonPanelPositionLayout.createSequentialGroup().
				addGroup(
				buttonPanelPositionLayout.createParallelGroup().
				addComponent(offset_left).addComponent(offset_right)).
				addComponent(switchPanelFromPosition));
		
		mainPanelImagePosition.setVisible(false);
		this.add(mainPanelImagePosition);
	}

	private void setSpinnerFormats(JSpinner spinner) {
		NumberEditor editor = (NumberEditor) spinner.getEditor();
		NumberFormatter numberFormatter = (NumberFormatter) editor.getTextField().getFormatter();
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setCommitsOnValidEdit(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if(command == "UP")
			spriteCanvas.shiftImageUp();
		if(command == "DOWN")
			spriteCanvas.shiftImageDown();
		if(command == "LEFT")
			spriteCanvas.shiftImageLeft();
		if(command == "RIGHT")
			spriteCanvas.shiftImageRight();
		if(command == "FLIPV")
			spriteCanvas.flipImageVertical();
		if(command == "FLIPH")
			spriteCanvas.flipImageHorizontal();
		if(command == "pixel") {
			spriteCanvas.setMottledPixelBG();
			disableSpinners();
		}
		if(command == "tile") {
			enableSpinners();
			spriteCanvas.tileResize
			(((SpinnerNumberModel)tileWidthSize.getModel()).getNumber().intValue(),
			((SpinnerNumberModel)tileHeightSize.getModel()).getNumber().intValue());
		}
		
		if (command == "copy") {
			spriteCanvas.getSpriteFrame().setDrawMode(false);
		} else if(command == "draw") {
			spriteCanvas.getSpriteFrame().setDrawMode(true);
		}
		
		if(command == INCREASE_TRANSPARENCY)
			spriteCanvas.setXOffset(command);
		if(command == DECREASE_TRANSPARENCY)
			spriteCanvas.setXOffset(command);
		if(command == SWITCH_TO_POSITION)
		{
			mainPanelImageEdit.setVisible(false);
			mainPanelImageLayer.setVisible(false);
			mainPanelImagePosition.setVisible(true);
		}
		if(command == SWITCH_TO_EDIT) {
			mainPanelImageLayer.setVisible(false);
			mainPanelImagePosition.setVisible(false);
			mainPanelImageEdit.setVisible(true);
		}
		if(command == SWITCH_TO_LAYER) {
			mainPanelImagePosition.setVisible(false);
			mainPanelImageEdit.setVisible(false);
			mainPanelImageLayer.setVisible(true);
		}
		this.revalidate();
		this.repaint();
		spriteCanvas.revalidate();
		spriteCanvas.repaint();
		spriteCanvas.requestFocus();
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == transparentBG){
			if(e.getStateChange() == ItemEvent.SELECTED){
				spriteCanvas.setImageBGtransparent();
			}else{
				spriteCanvas.setImageBGOpaque();
			}
		}else if(e.getSource() == showAllFrames){
			if(e.getStateChange() == ItemEvent.SELECTED){
				spriteCanvas.setDisplayAll(true);
			}else{
				spriteCanvas.setDisplayAll(false);
			}
		} else if (e.getSource() == gridLinesVisible) {
			spriteCanvas.setGridLinesVisible(e.getStateChange() == ItemEvent.SELECTED);
		}
	}

	private void enableSpinners() {
		tileWidthSize.setEnabled(true);
		tileHeightSize.setEnabled(true);
	}
	
	private void disableSpinners() {
		tileWidthSize.setEnabled(false);
		tileHeightSize.setEnabled(false);
	}
	
	private class resizeBGTilesWidth implements ChangeListener {
		public void stateChanged(ChangeEvent e)
		{
			spriteCanvas.tileResize
			(((SpinnerNumberModel)tileWidthSize.getModel()).getNumber().intValue(),
			((SpinnerNumberModel)tileHeightSize.getModel()).getNumber().intValue());
		}
	}
	
	private class resizeBGTilesHeight implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			spriteCanvas.tileResize
			(((SpinnerNumberModel)tileWidthSize.getModel()).getNumber().intValue(),
			((SpinnerNumberModel)tileHeightSize.getModel()).getNumber().intValue());
		}
	}
	
	private class ChangeValue implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == width) {
				spriteCanvas.getSpriteFrame().setCopyImageWidth(((SpinnerNumberModel)width.getModel()).getNumber().intValue());
			} else if (e.getSource() == height) {
				spriteCanvas.getSpriteFrame().setCopyImageHeight(((SpinnerNumberModel)height.getModel()).getNumber().intValue());
			}
		}
	}
	
	public void changeImage(BufferedImage img) {
		image = img;
	}

	public void setMaxDrawPriority(int max) {
		
	}

	public void setdrawPriorityValue(int priorityVal) {

	}
}