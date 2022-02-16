package spriteMaker.Palette;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import componentInterface.SpritePaletteInterface;
import componentInterface.PaletteInterface;
import componentInterface.SelectRGBInterface;

import fileRW.PaletteFileReader;
import fileRW.PaletteFileWriter;

import java.awt.image.IndexColorModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SpritePalette extends JPanel implements PaletteInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7741741647069956770L;
	private int rgbVal = 0;
	int buttIdxSel = 0;
	private short R[];
	private short G[];
	private short B[];
	private JButton[] colourButton;
	private JSlider sliderR;
	private JSlider sliderG;
	private JSlider sliderB;
	private JLabel RVal;
	private JLabel GVal;
	private JLabel BVal;
	private int colorsAdjust = 1;
	private JFileChooser fileChooser;
	private int colorSize;
	public final static int COLOR_16BIT = 16;
	public final static int COLOR_32BIT = 32;
	public final static int PIXEL_4BITS = 4;
	public final static int PIXEL_8BITS = 8;
	/*TODO
	 * change to interface
	 * */
	private SelectRGBInterface rgbSelect;
	private SpritePaletteInterface paletteInterface;
	
	//public SpritePalette(IndexColorModel palette, int paletteDepth,
	//		SelectRGBInterface selectRGBInterface,  SpriteFramesInterface frames)	
	public SpritePalette(int colorSize, int pixelSize)	
	{
		super();
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|
				AWTEvent.MOUSE_WHEEL_EVENT_MASK|
				AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
		//rgbSelect = selectRGBInterface;
		//spriteFrames = frames;
		
		//JPanel palettePanel = new JPanel();
		
		this.colorSize = colorSize;
		if(this.colorSize == COLOR_16BIT)
			colorsAdjust = 8;
		else
			colorsAdjust = 1;
		
		this.setLayout(new BorderLayout());
		
		JPanel palettePanel = this.createButtonPanel(pixelSize);
		JPanel mixerValuePanel = this.createMixerPanel(pixelSize);
		
		this.add(palettePanel, BorderLayout.CENTER);
		this.add(mixerValuePanel, BorderLayout.SOUTH);		
		
		this.setVisible(true);
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new PaletteFileFilter());
		//
		//frames.setName("change");
	}
	
	private JPanel createButtonPanel(int pixelSize) {
		JPanel palettePanel = new JPanel();
		int mapSize = (int) Math.pow(2, pixelSize);
		colourButton = new JButton[mapSize];
		R = new short[mapSize];
		G = new short[mapSize];
		B = new short[mapSize];
		
		palettePanel.setLayout(new BoxLayout(palettePanel,BoxLayout.Y_AXIS));
		for(int k = 0; k < mapSize/16; k++) {
			JPanel buttonPanel16 = new JPanel();
			for(int i = 0; i < 16; i++) {
				rgbVal = 0;
			
				modifyRGB(i + k*16, 0, 0, 0);
				colourButton[i + k*16] = new JButton();
				colourButton[i + k*16].setPreferredSize(new Dimension(15,15));
				colourButton[i + k*16].addActionListener(new selectColor());
				modifyButton(i + k*16, rgbVal);
				buttonPanel16.add(colourButton[i + k*16]);
			}
			palettePanel.add(buttonPanel16);
		}
		return palettePanel;
	}
	
	private JPanel createMixerPanel(int pixelSize) {
		sliderR = new JSlider(0,255/colorsAdjust,0/colorsAdjust);
		sliderR.addChangeListener(new modifySlider());
		sliderR.setBackground(Color.RED);
		sliderG = new JSlider(0,255/colorsAdjust, 0/colorsAdjust);
		sliderG.addChangeListener(new modifySlider());
		sliderG.setBackground(Color.GREEN);
		sliderB = new JSlider(0,255/colorsAdjust, 0/colorsAdjust);
		sliderB.addChangeListener(new modifySlider());
		sliderB.setBackground(Color.BLUE);
		
		RVal = new JLabel("R: " + Integer.toString(sliderR.getValue()));
		GVal = new JLabel("G: " + Integer.toString(sliderG.getValue()));
		BVal = new JLabel("B: " + Integer.toString(sliderB.getValue()));
		
		JPanel coulourMixerPanel = new JPanel();
		coulourMixerPanel.setLayout(new GridLayout(3,0));
		
		coulourMixerPanel.add(sliderR);
		coulourMixerPanel.add(sliderG);
		coulourMixerPanel.add(sliderB);
		
		JPanel coulourValuePanel = new JPanel();
		coulourValuePanel.setLayout(new GridLayout(3,0));
		coulourValuePanel.add(RVal);
		coulourValuePanel.add(GVal);
		coulourValuePanel.add(BVal);
		
		JPanel mixerValuePanel = new JPanel();
		
		mixerValuePanel.add(this.createMenu());
		mixerValuePanel.add(coulourMixerPanel);
		mixerValuePanel.add(coulourValuePanel);
		
		return mixerValuePanel;
	}
	
	private JPanel createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu mainMenu;
		JMenuItem importPalette;
		JMenuItem exportPalette;
		JPanel panelForMenu;

		mainMenu = new JMenu("Options");
		importPalette = new JMenuItem("Import");
		importPalette.addActionListener(new importPalette());
		exportPalette = new JMenuItem("Export");
		exportPalette.addActionListener(new exportPalette());

		mainMenu.add(importPalette);
		mainMenu.add(exportPalette);
		menuBar.add(mainMenu);
		panelForMenu = new JPanel();
		panelForMenu.add(menuBar);		
		return panelForMenu;		
	}
	
	/*public void setInterfaceComponents(CanvasInterface canvas, 
			SpriteFramesInterface frames, SpriteAnimation animation)
	{
		spriteCanvas = canvas;
		spriteFrames = frames;
		spriteAnimation = animation;		
	}*/

	public int getRval()
	{
		return (int)R[buttIdxSel];
	}
	
	public int getGval()
	{
		return (int)G[buttIdxSel];
	}
	
	public int getBval()
	{
		return (int)B[buttIdxSel];
	}
	
	public int getIdxSel()
	{
		return buttIdxSel;	
	}
	
	public int getPaletteDepth()
	{
		return colorSize;
	}
	
	class selectColor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			Color colorVal = new Color(Integer.parseInt(command.substring(0, command.indexOf("_"))));
			String buttonSel = e.getActionCommand();
			buttIdxSel = Integer.parseInt(buttonSel.substring(buttonSel.indexOf("_")+1, buttonSel.length()));
			rgbVal = colorVal.getRGB();
			sliderR.setValue(colorVal.getRed()/colorsAdjust);
			sliderG.setValue(colorVal.getGreen()/colorsAdjust);
			sliderB.setValue(colorVal.getBlue()/colorsAdjust);
			RVal.setText("R: " + Integer.toString(sliderR.getValue()));
			GVal.setText("G: " + Integer.toString(sliderG.getValue()));
			BVal.setText("B: " + Integer.toString(sliderB.getValue()));
			colourButton[buttIdxSel].setSelected(true);

			if (rgbSelect != null) {
				rgbSelect.setRGB(rgbVal);
			}
		}
	}
	
	class modifySlider implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			modifyRGB(buttIdxSel, sliderR.getValue()*colorsAdjust, 
					sliderG.getValue()*colorsAdjust, sliderB.getValue()*colorsAdjust);
			
			Color colorVal = new Color(sliderR.getValue()*colorsAdjust,sliderG.getValue()*colorsAdjust,sliderB.getValue()*colorsAdjust);
			rgbVal = colorVal.getRGB();
			
			modifyButton(buttIdxSel, rgbVal);

			RVal.setText("R: " + Integer.toString(sliderR.getValue()));
			GVal.setText("G: " + Integer.toString(sliderG.getValue()));
			BVal.setText("B: " + Integer.toString(sliderB.getValue()));	
			
			if (paletteInterface != null) {
				paletteInterface.updatePalette(getIdxSel(), 
						getRval(), getGval(), getBval());				
			}

			if (rgbSelect != null) {
				rgbSelect.setRGB(rgbVal);
			}
		}
	}
	
	private void modifyRGB(int idx, int red, int green, int blue) {
		R[idx] = (short) red;
		G[idx] = (short) green;
		B[idx] = (short) blue;		
	}
	
	private void modifyButton(int idx, int rgb) {
		colourButton[idx].setBackground(new Color(rgb));
		colourButton[idx].setActionCommand(String.valueOf(rgb) + "_" + String.valueOf(idx));
	}
	
	class importPalette implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			importPal();
		}
	}
	
	private void importPal() {
		fileChooser.showOpenDialog(this);
		if(fileChooser.getSelectedFile() != null) {
			PaletteFileReader fileIn = new PaletteFileReader(fileChooser.getSelectedFile());
			setPallete(fileIn.getPalette());
			paletteInterface.updatePalette(fileIn.getPalette());
			repaint();
		}
	}
	
	class exportPalette implements ActionListener {
		public void actionPerformed(ActionEvent e){
			exportPal();
		}
	}
	
	private void exportPal() {
		byte r[] = new byte[R.length];
		byte g[] = new byte[G.length];
		byte b[] = new byte[B.length];
		
		for(int i = 0; i< R.length; i++) {
			r[i] = (byte)R[i];
			g[i] = (byte)G[i];
			b[i] = (byte)B[i];
		}
		IndexColorModel palette = paletteInterface.getPallete();
		int pixelSize = palette.getPixelSize();
		int numcolors = palette.getMapSize();
		palette = new IndexColorModel(pixelSize,numcolors,r,g,b);
		fileChooser.showSaveDialog(this);
		@SuppressWarnings("unused")
		//PaletteFileWriter fileOut = new PaletteFileWriter(fileChooser.getSelectedFile(), palette, palDepth);
		PaletteFileWriter fileOut = new PaletteFileWriter(fileChooser.getSelectedFile(), palette, 16);
	}
	
	private class PaletteFileFilter extends FileFilter {

		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1) {
	            ext = fileName.substring(i+1).toLowerCase();
	        } else 
	        	return false;
	        if(ext.matches("pal") == true)
	        	return true;

	        return false;
		}

		public String getDescription() {
			return "Palette File(*.pal)";
		}
	}
	
	//@Override
	/*public IndexColorModel getPalette() {
		//return spriteFrames.getActivePanel().getIndexColorModel();
		return null;
	}*/
	
	public void setRgbSelect(SelectRGBInterface rgbSelect) {
		this.rgbSelect = rgbSelect;
	}

	public void setPaletteUpdater(SpritePaletteInterface paletteUpdater) {
		this.paletteInterface = paletteUpdater;
	}
	
	@Override
	public void setPallete(IndexColorModel cm) {
		for (int i = 0; i < cm.getMapSize(); ++i) {
			modifyRGB(i, cm.getRed(i), cm.getGreen(i), cm.getBlue(i));
			modifyButton(i, cm.getRGB(i));
		}
		this.colourButton[buttIdxSel].doClick();
	}
}