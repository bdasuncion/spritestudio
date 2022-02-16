package multiSprite.Canvas;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import componentInterface.ButtonCommandsInterface;
import componentInterface.CanvasInterface;
import componentInterface.ImportFramesInterface;
import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;
import componentInterface.SpriteFrameDetailsInterface;
import componentInterface.UpdaterInterface;
import infoObjects.SpriteFrame;
import spriteMaker.Frames.Layer.SpriteLayerPanel;

public class MultiSpriteButtonPanel extends JPanel implements ButtonCommandsInterface, UpdaterInterface {
	
	private MultiSpriteButtonPanelPosition panelPosition;
	private MultiSpriteButtonPanelArrangement panelArrangement;
	private MultiSpriteButtonPanelLayers panelLayers;
	private MultiSpriteFrameInterface spriteSetInterface;
	private MultispriteFrameSetInterface msfsInterface;
	private SpriteFrameDetailsInterface spriteFrameInterface;
	public static final String CMD_SHOW_POSITION = "SHOW_POSITION";
	public static final String CMD_SHOW_LAYERS = "SHOW_LAYERS";
	public static final String CMD_SHOW_ARRANGEMENT = "SHOW_ARRANGEMENT";
	
    public MultiSpriteButtonPanel(MultiSpriteFrameInterface msf, ImportFramesInterface fsi,
    		SpriteFrameDetailsInterface sfdi) {
    	super();
    	

    	spriteFrameInterface = sfdi;
    	
    	spriteSetInterface = msf;
    
    	panelPosition = new MultiSpriteButtonPanelPosition(this);
    	
    	panelArrangement = new MultiSpriteButtonPanelArrangement(this);
    	
    	panelLayers = new MultiSpriteButtonPanelLayers(this, spriteSetInterface, 
    			panelArrangement, fsi, sfdi);
    	
        this.add(panelPosition);
    	this.add(panelArrangement);
    	this.add(panelLayers);
    }
    
    public void setMultispriteFrameSetInterface(MultispriteFrameSetInterface msfsi) {
    	panelLayers.setMultispriteFrameSetInterface(msfsi);
    	msfsInterface = msfsi;
    }
    
	@Override
	public void onButtonPressed(String command) {
		if (command.contentEquals(CMD_SHOW_ARRANGEMENT)) {
			panelArrangement.setVisible(true);
			panelPosition.setVisible(false);
			panelLayers.setVisible(false);
		} else if (command.contentEquals(CMD_SHOW_POSITION)) {
			panelPosition.setVisible(true);
			panelArrangement.setVisible(false);
			panelLayers.setVisible(false);
		} else if (command.contentEquals(CMD_SHOW_LAYERS)) {
			panelLayers.setVisible(true);
			panelArrangement.setVisible(false);
			panelPosition.setVisible(false);
		} else if (command.contentEquals(MultiSpriteButtonPanelPosition.CMD_UP)) {
			SpriteFrame spriteFrame = spriteSetInterface.
					getSpriteFrames().getCurrentSelected();
			spriteFrame.setYOffset(spriteFrame.getYOffset() - 1);
		} else if (command.contentEquals(MultiSpriteButtonPanelPosition.CMD_DOWN)) {
			SpriteFrame spriteFrame = spriteSetInterface.
					getSpriteFrames().getCurrentSelected();
			spriteFrame.setYOffset(spriteFrame.getYOffset() + 1);
		} else if (command.contentEquals(MultiSpriteButtonPanelPosition.CMD_LEFT)) {
			SpriteFrame spriteFrame = spriteSetInterface.
					getSpriteFrames().getCurrentSelected();
			spriteFrame.setXOffset(spriteFrame.getXOffset() - 1);
		} else if (command.contentEquals(MultiSpriteButtonPanelPosition.CMD_RIGHT)) {
			SpriteFrame spriteFrame = spriteSetInterface.
					getSpriteFrames().getCurrentSelected();
			spriteFrame.setXOffset(spriteFrame.getXOffset() + 1);
		} else if (command.contentEquals(MultiSpriteButtonPanelArrangement.FLIP_VERTICAL)) {
			spriteSetInterface.getSpriteFrames().getCurrentSelected().flipImageVertical(true);
		} else if (command.contentEquals(MultiSpriteButtonPanelArrangement.FLIP_HORIZONTAL)) {
			spriteSetInterface.getSpriteFrames().getCurrentSelected().flipImageHorizontal(true);
		} else if (command.contentEquals(MultiSpriteButtonPanelArrangement.NORMAL_VERTICAL)) {
			spriteSetInterface.getSpriteFrames().getCurrentSelected().flipImageVertical(false);
		} else if (command.contentEquals(MultiSpriteButtonPanelArrangement.NORMAL_HORIZONTAL)) {
			spriteSetInterface.getSpriteFrames().getCurrentSelected().flipImageHorizontal(false);
		} else if (command.contains(MultiSpriteButtonPanelArrangement.SET_FRAMES_DISPLAY)) {
			spriteSetInterface.getSpriteFrames().setDisplayForNumberOfFrames(getFrameDisplay(command));
		}
		
		SpriteFrame spriteFrame = spriteSetInterface.getSpriteFrames().getCurrentSelected();
		spriteFrameInterface.setSpriteFrameDetails(spriteFrame);
		
		spriteSetInterface.updateView();
		
		if (msfsInterface != null) {
			msfsInterface.updateView();
		}
	}
	@Override
	public void onUpdate() {
		panelLayers.resetFrames();
	}
	
	private int getFrameDisplay(String frameDisplayCmd) {
		return Integer.parseInt(frameDisplayCmd.substring(MultiSpriteButtonPanelArrangement.SET_FRAMES_DISPLAY.length()));
	}
	
	public CanvasInterface getCanvasInterface() {
		return panelLayers;
	}
}
