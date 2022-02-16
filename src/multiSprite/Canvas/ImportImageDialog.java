package multiSprite.Canvas;

import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JFrame;

import componentInterface.CanvasInterface;
import fileRW.SpriteStudioFileReader;
import spriteMaker.Frames.SpriteFramePanel;
import tools.ImageTools;

public class ImportImageDialog extends JDialog {
	SpriteFramePanel spriteFramePanel;
    public ImportImageDialog(JFrame owner, SpriteStudioFileReader file, String fileName, CanvasInterface ci) {
    	super(owner);
    	//TODO update
    	spriteFramePanel = new SpriteFramePanel(file.getNumOfFrames() , file.getWidth(),
    			file.getHeight(), file.getPixelSize(), ci);
    	
    	BufferedImage[] images = ImageTools.createMultiImages(file.getWidth(), file.getHeight(), 
    			file.getNumOfFrames(), file.getPalette(), file.getData());
    	
    	spriteFramePanel.setFramePanel(images.length, images);
    	spriteFramePanel.setFileName(fileName);
    	add(spriteFramePanel);
    	//this.setSize(300,200);
    	this.pack();
    }
}
