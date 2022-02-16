package multiSprite.Canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import componentInterface.ArrangementPanelInterface;
import componentInterface.ButtonCommandsInterface;
import componentInterface.CanvasButtonPanelInterface;
import componentInterface.CanvasInterface;
import componentInterface.ImportFramesInterface;
import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;
import componentInterface.SpriteFrameDetailsInterface;
import componentInterface.SpriteFramesInterface;
import fileRW.BraveKnightFileFilter;
import fileRW.SpriteStudioFileReader;
import infoObjects.SpriteFrame;
import spriteMaker.Frames.SpriteFrameCanvas;
import spriteMaker.Frames.SpriteFramePanel;
import spriteMaker.Frames.Layer.SpriteLayerPanel;
import tools.ImageTools;

public class MultiSpriteButtonPanelLayers extends JPanel implements ActionListener, 
CanvasInterface, ItemListener {
	private JButton importSprite;
	private JButton increasePriority;
	private JButton decreasePriority;
	private JButton deleteLayer;
	
	private JButton showPosition;
	private JButton showArrangement;
	private SpriteLayerPanel spriteLayers;
	
	private MultiSpriteFrameInterface msInterface;
	private ArrangementPanelInterface apInterface;
	private MultispriteFrameSetInterface msfsInterface;
	private ImportFramesInterface fsiInterface;
	//private JFrame modalOwner;
	SpriteFrameDetailsInterface spriteFrameDetailsInterface;
	
	ButtonCommandsInterface buttonCommandsCallback;
	
	static final private String IMPORT = "IMPORT";
	static final public String INCREASE_PRIORITY = "INCREASE_PRIORITY";
	static final public String DECREASE_PRIORITY = "DECREASE_PRIORITY";
	static final public String DELETE_LAYER = "DELETELAYER";
	
	//private 
    public MultiSpriteButtonPanelLayers(ButtonCommandsInterface cb, 
    		MultiSpriteFrameInterface msfi, ArrangementPanelInterface api, ImportFramesInterface fsi,
    		SpriteFrameDetailsInterface sfdi) {
    	super();
    	
    	spriteFrameDetailsInterface = sfdi;
    	msInterface = msfi;
    	apInterface = api;
    	fsiInterface = fsi;
    	//msfsInterface = msfsi;
    	buttonCommandsCallback = cb;
    	//modalOwner = owner;
    	importSprite = new JButton("Import");
    	importSprite.setActionCommand(IMPORT);
    	importSprite.addActionListener(this);
    	
    	spriteLayers = new SpriteLayerPanel(null);
    	spriteLayers.addItemListener(this);
    	//spriteLayers.getModel().addListDataListener(this);
    	
    	
    	increasePriority = new JButton("+");
    	increasePriority.setActionCommand(INCREASE_PRIORITY);
    	increasePriority.addActionListener(this);
    	
    	decreasePriority = new JButton("-");
    	decreasePriority.setActionCommand(DECREASE_PRIORITY);
    	decreasePriority.addActionListener(this);
    	
    	deleteLayer = new JButton("Delete");
    	deleteLayer.setActionCommand(DELETE_LAYER);
    	deleteLayer.addActionListener(this);
    	
    	showPosition = new JButton(">>");
    	showPosition.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_POSITION);
    	showPosition.addActionListener(this);
    	
    	showArrangement = new JButton("<<");
    	showArrangement.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_ARRANGEMENT);
    	showArrangement.addActionListener(this);
    	
    	GroupLayout groupLayout = new GroupLayout(this);
    	this.setLayout(groupLayout);
    	
    	groupLayout.setHorizontalGroup(
    		groupLayout.createSequentialGroup()
    		.addGroup(groupLayout.createParallelGroup()
    	        .addGroup(groupLayout.createSequentialGroup()
    	            .addComponent(importSprite)
    	            .addComponent(spriteLayers))
    	        .addGroup(groupLayout.createSequentialGroup()
        	            .addComponent(increasePriority)
        	            .addComponent(decreasePriority)
        	            .addComponent(deleteLayer))
    	        .addGroup(groupLayout.createSequentialGroup()
        	            .addComponent(showArrangement)
        	            .addComponent(showPosition))));
    	
    	groupLayout.setVerticalGroup(
    		groupLayout.createSequentialGroup()
    		.addGroup(groupLayout.createParallelGroup()
    				.addComponent(importSprite)
    				.addComponent(spriteLayers))
    		.addGroup(groupLayout.createParallelGroup()
        		    .addComponent(increasePriority)
        		    .addComponent(decreasePriority)
        		    .addComponent(deleteLayer))
    		.addGroup(groupLayout.createParallelGroup()
    		    .addComponent(showArrangement)
    		    .addComponent(showPosition)));
    	this.setVisible(true);
    }
    
    public void setMultispriteFrameSetInterface(MultispriteFrameSetInterface msfsi) {
    	msfsInterface = msfsi;
    }
    
    private File getFileFromFileDialog(File dir, boolean isOpenFile) {
		JFileChooser fileDialog;
		fileDialog = new JFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(new BraveKnightFileFilter());
		
		if (dir != null) {
			fileDialog.setCurrentDirectory(dir);
		}
		
		if (!isOpenFile) {
			fileDialog.showSaveDialog(this);	
		} else {
			fileDialog.showOpenDialog(this);
		}
		

		return fileDialog.getSelectedFile();
	}

    private void importImage() {
    	File file = getFileFromFileDialog(null, true);
		
		if(file == null)
			return;
		
		SpriteStudioFileReader fileIn = new SpriteStudioFileReader(file);
		
		fsiInterface.importFrameSelection(fileIn, file.getAbsolutePath(),  file.getName(), this);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals(MultiSpriteButtonPanelLayers.INCREASE_PRIORITY)) {
			if (msInterface.getSpriteFrames().increasePriority()) {
				refreshItems(-1);	
			}
		} else if (e.getActionCommand().contentEquals(MultiSpriteButtonPanelLayers.DECREASE_PRIORITY)) {
			if (msInterface.getSpriteFrames().decreasePriority()) {
				refreshItems(1);	
			}
		} else if (e.getActionCommand().contentEquals(MultiSpriteButtonPanelLayers.DELETE_LAYER)) {
			msInterface.getSpriteFrames().removeLayer();
			refreshItems(0);
		} else if(!e.getActionCommand().contentEquals(IMPORT)) {
			buttonCommandsCallback.onButtonPressed(e.getActionCommand());
		} else{
			importImage();
		}
		
	}
	
	private void refreshItems(int offsetIdx) {
		int selectedIdx = spriteLayers.getSelectedIndex() + offsetIdx;
		resetFrames();
		
		if (selectedIdx >= 0 && selectedIdx < spriteLayers.getItemCount()) {
			spriteLayers.setSelectedIndex(selectedIdx);
			spriteLayers.repaint();
		}
		
		msInterface.updateView();
		updateViews();
	}
	
	public void resetFrames() {
		spriteLayers.removeAllItems();
		Vector<SpriteFrame> frames = msInterface.getSpriteFrames().getSpriteFrames();
		
		for (SpriteFrame spriteFrame : frames) {
			//spriteLayers.addItem(spriteFrame.getImage());
			spriteLayers.addItem(spriteFrame);
		}
	}

	@Override
	public void setSpriteFrame(SpriteFrame sp) {
		//SpriteFrame spriteFrame = new SpriteFrame(sp.getImage());
		SpriteFrame spriteFrame = new SpriteFrame(sp);
		//spriteLayers.addItem(spriteFrame.getImage());
		spriteLayers.addItem(spriteFrame);
		spriteFrame.setImageBGTransparent(true);
		msInterface.getSpriteFrames().addLayer(spriteFrame);
		msInterface.updateView();
		updateViews();
	}

	@Override
	public void setCanvasButtonPanelInterface(CanvasButtonPanelInterface cbpInterface) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CanvasButtonPanelInterface getCanvasButtonPanelInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED &&
				e.getSource() == spriteLayers) {
			msInterface.getSpriteFrames().setCurrentIdx(spriteLayers.getSelectedIndex());
			SpriteFrame current = msInterface.getSpriteFrames().getCurrentSelected();
			if (apInterface != null && current != null) {
				apInterface.onSetFrame(current.isFlippedVertical(),
					current.isFlippedHorizontal(), 
					 msInterface.getSpriteFrames().getDisplayForNumberOfFrames());				
			}
			
			spriteFrameDetailsInterface.setSpriteFrameDetails(current);
		}
	}

	/*@Override
	public void contentsChanged(ListDataEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intervalAdded(ListDataEvent arg0) {
		//System.out.print("ADD ITEM AT:");
	}

	@Override
	public void intervalRemoved(ListDataEvent arg0) {
		//System.out.print("REMOVE ITEM");
	}*/
	
	private void updateViews() {
		if (msfsInterface != null) {
			msfsInterface.updateView();	
		}
	}
}
