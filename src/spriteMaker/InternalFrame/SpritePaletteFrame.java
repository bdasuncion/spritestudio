package spriteMaker.InternalFrame;

import javax.swing.JInternalFrame;

import spriteMaker.Canvas.SpriteCanvas;
import spriteMaker.Frames.SpriteFramePanel;
import spriteMaker.Palette.SpritePalette;

public class SpritePaletteFrame extends JInternalFrame {
	private SpritePalette spritepalette;
	
    public SpritePaletteFrame(int numColor, int pixelSize, 
    		//SpriteFrames spriteframes,
    		SpriteFramePanel spriteFramePanel,
    		SpriteCanvas spritecanvas) {
    	super("Palette", false, true);
    	
    	//TODO support 256 insdex color
    	spritepalette = new SpritePalette(numColor, pixelSize);
		//spritepalette.setPaletteUpdater(spriteframes.getActivePanel());
		spritepalette.setPaletteUpdater(spriteFramePanel);
		spritepalette.setRgbSelect(spritecanvas);
	
		add(spritepalette);
		
		setSize(500, 500);
		pack();
		setVisible(true);
		setLocation(500, 250);
    }
    
    public SpritePalette getSpritePallete() {
    	return spritepalette;
    }
}
