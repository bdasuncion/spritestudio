package spriteMaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;


import componentInterface.ExternalFileInterface;
import fileRW.BraveKnightFileFilter;
import fileRW.ExternalFileManager;
import fileRW.SpriteStudioFileWriter;
import infoObjects.SpriteAnimationSet;
import infoObjects.SpriteFrame;

import spriteMaker.Animation.SpriteAnimationPanel;
import spriteMaker.Canvas.SpriteCanvas;
import spriteMaker.Frames.SpriteFramePanel;
import spriteMaker.InternalFrame.SpriteAnimationDisplayFrame;
import spriteMaker.InternalFrame.SpriteAnimationFrameSelectionFrame;
import spriteMaker.InternalFrame.SpriteEditorFrame;
import spriteMaker.InternalFrame.SpritePaletteFrame;
import spriteMaker.Palette.SpritePalette;
import tools.ImageTools;

public class SpriteDesktop extends JDesktopPane implements ExternalFileInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -371233384262486528L;
	private SpriteEditorFrame spriteEditFrame;
	private SpriteAnimationFrameSelectionFrame spriteAnimationFrameSelectionFrame;
	private SpriteAnimationDisplayFrame spriteAnimationDisplayFrame;
	private SpriteCanvas spritecanvas;
	private SpritePalette spritepalette;
	//private SpriteFrames spriteframes;
	private SpriteFramePanel spriteFramePanel;
	private SpriteAnimationPanel spriteanimation;
	private SpritePaletteFrame spritePaletteFrame;
	private File saveFile = null;
	private SpriteAnimationSet spriteAnimationSet;

	public SpriteDesktop(int width, int height, int pixelSize, int numColor, JFrame owner) {
		super();
		this.setVisible(true);
		this.setBackground(Color.gray);
		
		spriteAnimationSet = new SpriteAnimationSet();
		
		spriteEditFrame = new SpriteEditorFrame(width, height, pixelSize);
		spritecanvas = spriteEditFrame.getSpriteCanvas();
		
		spriteAnimationFrameSelectionFrame = 
				new SpriteAnimationFrameSelectionFrame(width, height, pixelSize, 
						spriteAnimationSet, spritecanvas, owner);
		spriteFramePanel = spriteAnimationFrameSelectionFrame.getSpriteFramePanel();
		
		spriteAnimationDisplayFrame = new SpriteAnimationDisplayFrame(spriteAnimationSet);
		spriteanimation = spriteAnimationDisplayFrame.getSpriteAnimationPanel();
		
		
		spritePaletteFrame = new SpritePaletteFrame(numColor, pixelSize, spriteFramePanel, spritecanvas);
		spritepalette = spritePaletteFrame.getSpritePallete();
		
		spriteFramePanel.setPaletteInterface(spritepalette);
		

		this.add(spriteEditFrame);
		this.add(spritePaletteFrame);
		this.add(spriteAnimationFrameSelectionFrame);
		this.add(spriteAnimationDisplayFrame);
		this.setSize(500,500);
	}
	
	//TODO create interface to get all images
	public BufferedImage[] getAllImages()
	{
		//Vector<SpriteFrame> sprites = spriteframes.getActivePanel().getAllSpriteFrames();
		Vector<SpriteFrame> sprites = spriteFramePanel.getAllSpriteFrames();
		BufferedImage images[] = new BufferedImage[sprites.size()];
		for (int i = 0; i < images.length; ++i) {
			images[i] = sprites.get(i).getImage();
		}
		return images;
	}
	
	//public IndexColorModel getPaletteOfImage()
	//{
	//	return spritepalette.getPalette();
	//}
	
	public int getPaletteDepth() {
		return spritepalette.getPaletteDepth();
	}
		
	public void setFrames(int w, int h, int frames,IndexColorModel cm, byte[] data)
	{
		BufferedImage[] multiImage = ImageTools.createMultiImages(w, h, frames, cm, data);
		
		//spriteframes.setSpriteFrames(frames, multiImage, spritecanvas);
		spriteFramePanel.setFramePanel(frames, multiImage);
		spriteFramePanel.setCanvas(spritecanvas);
		
		//spriteframes.setAnimationInterface(spriteanimation);
		this.repaint();
	}
	
	public File getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
	}

	@Override
	public String save() {
		ExternalFileManager fileManager = new ExternalFileManager();
		
		if (saveFile == null) {
			saveFile = fileManager.getFileFromFileDialog(false, this, new BraveKnightFileFilter());
		}
		
		if (saveFile == null) {
			return null;
		}
		
		BufferedImage sprites[] = getAllImages();
		@SuppressWarnings("unused")
		SpriteStudioFileWriter fileOut =
			new SpriteStudioFileWriter(saveFile, 
				sprites, (IndexColorModel) sprites[0].getColorModel(), getPaletteDepth());
		
		return saveFile.getName();
	}

	@Override
	public String saveAs() {
		ExternalFileManager fileManager = new ExternalFileManager();
		
		if (saveFile != null) {
			saveFile = fileManager.getFileFromFileDialog(new File(saveFile.getAbsolutePath()), false, 
					this, new BraveKnightFileFilter());	
		} else {
			saveFile = fileManager.getFileFromFileDialog(false, this, new BraveKnightFileFilter());
		}
	
		if(saveFile == null) {
			return null;
		}
			
		BufferedImage sprites[] = getAllImages();
		@SuppressWarnings("unused")
		SpriteStudioFileWriter fileOut =
			new SpriteStudioFileWriter(saveFile, 
				sprites, (IndexColorModel) sprites[0].getColorModel(), getPaletteDepth());
		
		return saveFile.getName();
	}

	@Override
	public String openFile() {
		return null;
	}
}