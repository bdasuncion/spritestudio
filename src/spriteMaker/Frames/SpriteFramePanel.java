package spriteMaker.Frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import componentInterface.CanvasInterface;
import componentInterface.FramePanelInterface;
import componentInterface.PaletteInterface;
import componentInterface.SpritePaletteInterface;
import fileRW.SpriteStudioFileReader;
import infoObjects.SpriteAnimationSet;
import infoObjects.SpriteFrame;
import tools.ImageTools;

//this class should hold all of the data about images
public class SpriteFramePanel extends JPanel implements FramePanelInterface,
	SpritePaletteInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5041317897151945559L;
	private int idx = 0;
	//private int numOfFrames;
	private CanvasInterface spriteCanvas;
	//private SpriteFrameCanvas display;
	private SpriteFrame activeSpriteFrame;
	private PaletteInterface paletteInterface;
	private String sourceFile;
	private SpriteAnimationSet spriteAnimationSet;
	private Timer updater;
	
	private static int SPACING = 20;
	public SpriteFramePanel(int numFrames, int width, int height, int pixelSize, CanvasInterface canvas) {
		super();
		spriteAnimationSet = new SpriteAnimationSet();
		initialize(numFrames, width, height, pixelSize, canvas);
	}
	
	public SpriteFramePanel(int numFrames, int width, int height, int pixelSize, 
			SpriteAnimationSet sas, CanvasInterface canvas) {
		super();
		spriteAnimationSet = sas;
		initialize(numFrames, width, height, pixelSize, canvas);
	}
	
	private void initialize(int numFrames, int width, int height, int pixelSize, CanvasInterface canvas) {
		spriteCanvas = canvas;
		for(int i = 0; i<numFrames; i++ ) {
			BufferedImage img = ImageTools.createEmptyImage(width, height, pixelSize);
			JButton button = createButton(img, Integer.toString(i));
			if(i == 0 ) {
				button.setBackground(Color.blue);
				//display = new SpriteFrameCanvas(img);
			} else {
				button.setBackground(Color.lightGray);
			}
			
			this.add(button);
		}
		idx = 0;
		
		//System.out.println("W " + this.getWidth() + " H " + this.getHeight());
		//this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		//TODO change this process
		updater = new Timer(300,new updateAll());
		updater.start();
		
		this.revalidate();
	}
	
	private JButton createButton(BufferedImage img,
			String actionCommand) {
		spriteAnimationSet.add(img);
		JButton button = new SpriteFrameButton(this, 
				spriteAnimationSet.getSpriteFrames().lastElement());
		button.setActionCommand(actionCommand);
		return button;
	}
	
	public void setFramePanel(int numFrames, BufferedImage[] img) {
		this.removeAll();
		int totalWidth = 0, height = 0;
		spriteAnimationSet.getSpriteFrames().clear();
		for(int i = 0; i<numFrames; i++ ) {
			JButton button = createButton(img[i], Integer.toString(i));
			if(i == 0 ) {
				button.setBackground(Color.blue);
				//display =  new SpriteFrameCanvas(img[i]);
			} else {
				button.setBackground(Color.lightGray);
			}

			totalWidth = button.getWidth() + SPACING;
			height = button.getHeight() + SPACING;
			this.add(button);
		}
		
		//setPreferredSize(new Dimension(totalWidth, height));
		//System.out.println("W " + this.getWidth() + " H " + this.getHeight());
		//this.setPreferredSize(new Dimension(1000, 100));
		this.setVisible(true);
		this.revalidate();
	}
	
	public void setCanvas(CanvasInterface s) {
		spriteCanvas = s;
		JButton button = (JButton)this.getComponent(idx);
		button.doClick();
	}
	
	public IndexColorModel getIndexColorModel()
	{
		return activeSpriteFrame.getImageColorModel();
	}

	public void setSelectedButton()
	{
		//for(int i = 0; i<numOfFrames; i++) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			JButton button = (JButton)this.getComponent(i);
			button.setBackground(Color.LIGHT_GRAY);
		}
		JButton selected = (JButton)this.getComponent(idx);
		selected.setBackground(Color.blue);
		selected.doClick();
	}
	
	public void resizeFrames(int width, int height) {
		//for(int i = 0; i<numOfFrames; i++) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			SpriteFrameButton button = (SpriteFrameButton)this.getComponent(i);
			button.resizeFrame(width, height);
			spriteAnimationSet.get(i).resizeImage(width, height);
		}
		this.repaint();
	}
	
	public void copyFrame(BufferedImage img) {		
		//JButton button = createButton(img, Integer.toString(numOfFrames));
		JButton button = createButton(img, Integer.toString(spriteAnimationSet.getSize()));
		this.add(button);
		
		//this.setPreferredSize(new Dimension(this.getWidth() + img.getHeight()*2,
		this.setPreferredSize(new Dimension(this.getWidth() + img.getWidth()*2,
				this.getHeight()));
		this.revalidate();
		
		//numOfFrames++;
		this.repaint();
	}

	public void removeFrame() {
		if(!(spriteAnimationSet.getSize() > 1))
			return;
		this.remove(idx);
		spriteAnimationSet.removeAt(idx);
		//numOfFrames--;
		if(idx >= spriteAnimationSet.getSize())
			idx--;
		this.resetButtonsActionCommand();
		this.setSelectedButton();
		
		this.setPreferredSize(new Dimension(this.getWidth() - this.activeSpriteFrame.getImage().getWidth()*2,
				this.getHeight()));
		this.revalidate();
		this.repaint();
	}
	
	public void resetButtonsActionCommand()
	{
		//for(int i = 0; i<numOfFrames; i++) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			JButton button = (JButton)this.getComponent(i);
			button.setActionCommand(Integer.toString(i));
		}
	}
	
	public void shiftLeft()
	{
		if(idx == 0)
			return;

		//spriteAnimationSet.exchangeWithPreviousFrame(idx);
		spriteAnimationSet.exchangeFrames(idx, idx - 1);
		SpriteFrameButton fromButton = (SpriteFrameButton)this.getComponent(idx);
		fromButton.setSpriteFrame(spriteAnimationSet.get(idx));
		SpriteFrameButton toButton = (SpriteFrameButton)this.getComponent(idx - 1);
		toButton.setSpriteFrame(spriteAnimationSet.get(idx - 1));
		idx--;
		toButton.doClick();
		revalidate();
	}
	
	public void shiftRight()
	{
		if(idx + 1 == spriteAnimationSet.getSize())
			return;
		
		//spriteAnimationSet.exchangeWithNextFrame(idx);
		spriteAnimationSet.exchangeFrames(idx, idx + 1);
		SpriteFrameButton fromButton = (SpriteFrameButton)this.getComponent(idx);
		fromButton.setSpriteFrame(spriteAnimationSet.get(idx));
		SpriteFrameButton toButton = (SpriteFrameButton)this.getComponent(idx + 1);
		toButton.setSpriteFrame(spriteAnimationSet.get(idx + 1));
		
		idx++;
		toButton.doClick();
		revalidate();
	}
	
	public BufferedImage getImageAndOffsetOfFrame() {
		JButton button = (JButton)this.getComponent(idx);
		SpriteFrameCanvas panel = (SpriteFrameCanvas)button.getComponent(0);
		SpriteFrame frame = panel.getFrame();	
		return frame.getImage();
	}
	
	public BufferedImage getImageAndOffsetOfFrame(int i) {
		JButton button = (JButton)this.getComponent(i);
		SpriteFrameCanvas panel = (SpriteFrameCanvas)button.getComponent(0);
		SpriteFrame frame = panel.getFrame();
		return frame.getImage();
	}
	
	public BufferedImage getImageAt(int i) {
		JButton button = (JButton)this.getComponent(i);
		SpriteFrame panel = 
				((SpriteFrameCanvas)button.getComponent(0)).getFrame();
		return panel.getImage();		
	}
	
	private Vector<SpriteFrame> getAllImages() {
		Vector<SpriteFrame> all = new Vector<SpriteFrame>();
		
		//for(int i = 0; i<numOfFrames; i++) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			JButton button = (JButton)this.getComponent(i);
			SpriteFrame frame = 
					((SpriteFrameCanvas)button.getComponent(0)).getFrame();
			all.add(frame);
		}
		return all;
	}
	
	public void updatePanel() {	
		this.repaint();
	}
	
	public int getIdx() {
		return idx;
	}
	
	public SpriteFrameCanvas getFrameCanvasAt(int idx)
	{
		SpriteFrameButton button = (SpriteFrameButton)this.getComponent(idx);
		SpriteFrameCanvas spriteFrameCanvas = button.getDisplay();
		return spriteFrameCanvas;
	}
	
	//For dropdown layer select
	/*public Component getImageDisplay() {
		display.updateImage(this.getImageAndOffsetOfFrame(0).getData());
		return display;
	}*/

	public SpriteFrame getCurrentSpriteFrame() {
		return activeSpriteFrame;
	}
	
	@Override
	public void updatePalette(int idxSel, int RPal, int GPal, int BPal)
	{
		activeSpriteFrame.updatePalette(idxSel, RPal, GPal, BPal);		
		this.updatePaletteOfFrames(activeSpriteFrame.getImageColorModel());
		this.repaint();
	}
	
	@Override
	public IndexColorModel getPallete() {
		return activeSpriteFrame.getImageColorModel();
	}
	
	@Override
	public void updatePalette(IndexColorModel icm) {
		updatePaletteOfFrames(icm);
	}
	
	private void updatePaletteOfFrames(IndexColorModel cm) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			JButton button = (JButton)this.getComponent(i);
			SpriteFrame frame = ((SpriteFrameCanvas)button.getComponent(0)).getFrame();
			frame.updatePalette(cm);
		}
		repaint();
	}
	
	@Override
	public void setSpriteFrame(SpriteFrame s, JButton b) {
		if (spriteCanvas == null) {
			return;
		}
		
		activeSpriteFrame = s;
		spriteCanvas.setSpriteFrame(s);
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			JButton button = (JButton)this.getComponent(i);
			button.setBackground(Color.LIGHT_GRAY);
		}
		b.setBackground(Color.blue);
		if (paletteInterface != null) {
			paletteInterface.setPallete(s.getImageColorModel());
		}
		
		//TODO set index of animation here
		Component components[] = this.getComponents();
		
		for(int i = 0; i < components.length; ++i) {
			if (b == components[i]) {
				idx = i;
				break;
			}
		}
	}

	@Override
	public Vector<SpriteFrame> getAllSpriteFrames() {
		return getAllImages();
	}

	public void setPaletteInterface(PaletteInterface paletteInterface) {
		this.paletteInterface = paletteInterface;
		paletteInterface.setPallete(activeSpriteFrame.getImageColorModel());
	}
	
	public void setFileName(String filename) {
		//for(int i = 0; i<numOfFrames; i++) {
		for(int i = 0; i<spriteAnimationSet.getSize(); i++) {
			SpriteFrameButton button = (SpriteFrameButton)this.getComponent(i);
			button.setBackground(Color.LIGHT_GRAY);
			button.getDisplay().getFrame().setSrcFileName(filename);
			button.getDisplay().getFrame().setCompressedFrameName( 
					filename.substring(0,filename.length() - ".knight".length()) + "_image" + i);
		}
	}
	
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public String getSourceFile() {
		return this.sourceFile;
	}
	
	public void reload() {
		if (sourceFile != null) {
		    File spriteFile = new File(sourceFile);
		    SpriteStudioFileReader fileReader = new SpriteStudioFileReader(spriteFile);
		    
			BufferedImage[] images = ImageTools.createMultiImages(fileReader.getWidth(), fileReader.getHeight(), 
					fileReader.getNumOfFrames(), fileReader.getPalette(), fileReader.getData());
			
			setFramePanel(images.length, images);
			setFileName(spriteFile.getName());
			setSourceFile(spriteFile.getAbsolutePath());
		}
		revalidate();
		repaint();
	}
	
	private class updateAll implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//spriteLayerPanel.getActivePanel().updatePanel();
			updatePanel();
		}
	}
}
