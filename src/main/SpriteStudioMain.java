package main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import componentInterface.ExternalFileInterface;
import fileRW.BraveKnightFileFilter;
import fileRW.ConvertFileToTiles;
import fileRW.SpriteStudioFileReader;
import fileRW.SpriteStudioFileWriter;
import infoObjects.MultiSpriteAnimation;
import javafx.stage.FileChooser;
import fileRW.ExportToC;
import fileRW.ExternalFileManager;
import fileRW.KnightsOfTheRoundFileFilter;
import fileRW.MultiSpriteCExport;
import fileRW.MultiSpriteFileReaderWriter;
import spriteMaker.MultiSpriteDesktop;
import spriteMaker.SpriteDesktop;


public class SpriteStudioMain
{
	private JFrame MainFrame;
	
	private JMenuBar mainMenu;
	private JMenu fileMenu;
	private JMenuItem newFile;
	private JMenuItem newMultiSprite;
	private JMenuItem open;
	private JMenuItem openMultiSprite;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem export1DGBA;
	private JMenuItem export1DGBALZSS;
	private JMenuItem export1DGBALZSSFrame;
	private JMenuItem exportMultiSprite;
	private JMenuItem convertToTiles; 
	private JMenuItem exit; 
	private JTabbedPane FilePane; 
	private CreateNewFile newFileDialog;
	
	public SpriteStudioMain()
	{
		MainFrame = new JFrame("Brave Knight Sprite Studio");
		this.createMenu();
		MainFrame.setSize(800,600);
		MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		MainFrame.setVisible(true);
		MainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void createMenu()
	{
		mainMenu = new JMenuBar();
	
		fileMenu = new JMenu("File");
		
		newFile = new JMenuItem("New");
		newFile.addActionListener(new newFileCreate());
		
		newMultiSprite = new JMenuItem("New multisprite");
		newMultiSprite.addActionListener(new CreateMultiSprite());
		
		open = new JMenuItem("Open");
		open.addActionListener(new openFile());
		
		openMultiSprite = new JMenuItem("Open multisprite");
		openMultiSprite.addActionListener(new openMultiSpriteFile());
		
		save = new JMenuItem("Save");
		save.addActionListener(new saveFile());
		save.setEnabled(false);
		
		saveAs = new JMenuItem("Save As...");
		saveAs.addActionListener(new saveAsFile());
		saveAs.setEnabled(false);
		
		export1DGBA = new JMenuItem("Export to 1D GBA");
		export1DGBA.addActionListener(new exportCFile1DGBA());
		export1DGBA.setEnabled(false);
		
		export1DGBALZSS = new JMenuItem("Export to GBA Compressed(LZSS)");
		export1DGBALZSS.addActionListener(new exportCFile1DGBALZSS());
		export1DGBALZSS.setEnabled(false);
		
		export1DGBALZSSFrame = new JMenuItem("Export to GBA Compressed/Frame(LZSS)");
		export1DGBALZSSFrame.addActionListener(new exportCFile1DGBALZSSFrame());
		export1DGBALZSSFrame.setEnabled(false);
		
		exportMultiSprite = new JMenuItem("Export to GBA Multisprite");
		exportMultiSprite.addActionListener(new exportMultiSpriteGBA());
		exportMultiSprite.setEnabled(false);
		
	 	convertToTiles = new JMenuItem("Convert To Tiles");
		convertToTiles.addActionListener(new convertToTiles());
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(new exitTabbedPaned());
		
		fileMenu.add(newFile);
		fileMenu.add(newMultiSprite);
		fileMenu.add(open);
		fileMenu.add(openMultiSprite);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(export1DGBA);
		fileMenu.add(export1DGBALZSS);
		fileMenu.add(export1DGBALZSSFrame);
		fileMenu.add(exportMultiSprite);
		fileMenu.add(convertToTiles);
		fileMenu.add(exit);
		mainMenu.add(fileMenu);
		
		MainFrame.setJMenuBar(mainMenu);
		FilePane = new JTabbedPane();
		FilePane.addChangeListener(new TabbedPaneSelectionListener());
		MainFrame.getContentPane().add(FilePane);
		MainFrame.setVisible(true);
		MainFrame.createBufferStrategy(2);
	}
	
	private MultiSpriteDesktop createMultiSpriteDesktop(String filename) {
		MultiSpriteDesktop desktop;
		desktop = new MultiSpriteDesktop(MainFrame);
		if (filename == null) {
		    FilePane.addTab("New", desktop);
		} else {
			FilePane.addTab(filename, desktop);
		}
		FilePane.setVisible(true);
		return desktop;
	}
	
	class newFileCreate implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			newFileDialog = new CreateNewFile(MainFrame);
			if(newFileDialog.getOKStaus() == false)
				return;
			SpriteDesktop desktop;
			desktop = new SpriteDesktop(newFileDialog.getImageWidth(),newFileDialog.getImageHeight(),
					newFileDialog.getPixelSize(),newFileDialog.getPaletteDepth(), MainFrame);
			FilePane.addTab("New", desktop);
			FilePane.setVisible(true);
			
			if(FilePane.getComponentCount()>0) {
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export1DGBA.setEnabled(true);
				export1DGBALZSS.setEnabled(true);
				export1DGBALZSSFrame.setEnabled(true);
			}
			
			newFileDialog = null;
		}
	}
	
	class openFile extends saveFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ExternalFileManager fileManager = new ExternalFileManager();
			File openFile = fileManager.getFileFromFileDialog(true, MainFrame, new BraveKnightFileFilter());
			
			if(openFile == null)
				return;
			
			SpriteStudioFileReader fileIn = new SpriteStudioFileReader(openFile);
			
			SpriteDesktop desktop;
			desktop = new SpriteDesktop(fileIn.getWidth(),fileIn.getHeight(),
					fileIn.getPixelSize(),fileIn.getPaletteDepth(), MainFrame);
			
			desktop.setFrames(fileIn.getWidth(),fileIn.getHeight(),fileIn.getNumOfFrames(),fileIn.getPalette(),fileIn.getData());
			desktop.repaint();
			FilePane.addTab(openFile.getName(), desktop);
			FilePane.setVisible(true);
			
			/*if(FilePane.getComponentCount()>0) {
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export1DGBA.setEnabled(true);
				export1DGBALZSS.setEnabled(true);
				export1DGBALZSSFrame.setEnabled(true);
			}*/
			desktop.setSaveFile(openFile);
		}
	}
	
	class openMultiSpriteFile extends saveFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ExternalFileManager fileManager = new ExternalFileManager();
			File openFile = fileManager.getFileFromFileDialog(true, MainFrame, new KnightsOfTheRoundFileFilter());
			
			if(openFile == null)
				return;
			
			MultiSpriteDesktop desktop = createMultiSpriteDesktop(openFile.getName());
			
			MultiSpriteFileReaderWriter fileIn = new MultiSpriteFileReaderWriter();
			
			fileIn.read(openFile, desktop.getMultiSpriteAnimation());
			//fileIn.read(openFile, new MultiSpriteAnimation());
			
			desktop.setMultiSprite(openFile.getParent(), openFile);
		}
	}
	
	class saveAsFile extends saveFile implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			ExternalFileInterface saveInterface = (ExternalFileInterface)FilePane.getSelectedComponent();
			String title = saveInterface.saveAs();
			if (title != null) {
			    FilePane.setTitleAt(FilePane.getSelectedIndex(), title);
			}
		}
	}
	
	class saveFile implements ActionListener 
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			ExternalFileInterface saveInterface = (ExternalFileInterface)FilePane.getSelectedComponent();
			String title = saveInterface.save();
			
			if (title != null) {
			    FilePane.setTitleAt(FilePane.getSelectedIndex(), title);
			}
		}
		
		/*protected File getFileFromFileDialog(boolean isOpenFile) {
			return getFileFromFileDialog(null, isOpenFile);
		}
		
		protected File getFileFromFileDialog(File dir, boolean isOpenFile) {
			JFileChooser fileDialog;
			fileDialog = new JFileChooser();
			fileDialog.setAcceptAllFileFilterUsed(false);
			fileDialog.addChoosableFileFilter(new BraveKnightFileFilter());
			
			if (dir != null) {
				fileDialog.setCurrentDirectory(dir);
			}
			
			if (!isOpenFile) {
				fileDialog.showSaveDialog(MainFrame);	
			} else {
				fileDialog.showOpenDialog(MainFrame);
			}
			

			return fileDialog.getSelectedFile();
		}*/
		
	}
	
	class exportCFile1DGBA implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser exporter = new JFileChooser();
			exporter.setAcceptAllFileFilterUsed(false);
			exporter.setFileFilter(new CFileFilter());
			exporter.showDialog(MainFrame, "Export");
			SpriteDesktop desktop = (SpriteDesktop)FilePane.getSelectedComponent();
			BufferedImage sprites[] = desktop.getAllImages();
			ExportToC.exportToCGBA1D(exporter.getSelectedFile(), 
					sprites, (IndexColorModel) sprites[0].getColorModel());
			exporter = null;
		}
	}
	
	class exportCFile1DGBALZSS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser exporter = new JFileChooser();
			exporter.setAcceptAllFileFilterUsed(false);
			exporter.setFileFilter(new CFileFilter());
			exporter.showDialog(MainFrame, "Export");
			SpriteDesktop desktop = (SpriteDesktop)FilePane.getSelectedComponent();
			BufferedImage sprites[] = desktop.getAllImages();
			ExportToC.exportToCGBA1DLZSS(exporter.getSelectedFile(), 
					sprites, (IndexColorModel) sprites[0].getColorModel());
			exporter = null;
		}
	}
	
	class exportCFile1DGBALZSSFrame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser exporter = new JFileChooser();
			exporter.setAcceptAllFileFilterUsed(false);
			exporter.setFileFilter(new CFileFilter());
			exporter.showDialog(MainFrame, "Export");
			SpriteDesktop desktop = (SpriteDesktop)FilePane.getSelectedComponent();
			BufferedImage sprites[] = desktop.getAllImages();
			
			File file = exporter.getSelectedFile();
			String baseImageFile = file.getName();
			String cSpriteBaseName = baseImageFile.substring(0, baseImageFile.lastIndexOf("."));
			
			ExportToC.expToCGBA1DLZSSFrame(file, cSpriteBaseName,
					sprites, (IndexColorModel) sprites[0].getColorModel());
			exporter = null;
		}
	}
	
	class exportCFile2DGBA implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser exporter = new JFileChooser();
			exporter.setAcceptAllFileFilterUsed(false);
			exporter.setFileFilter(new CFileFilter());
			exporter.showDialog(MainFrame, "Export");
			SpriteDesktop desktop = (SpriteDesktop)FilePane.getSelectedComponent();
			BufferedImage sprites[] = desktop.getAllImages();
			ExportToC.exportToCGBA2D(exporter.getSelectedFile(), 
					sprites, (IndexColorModel) sprites[0].getColorModel());
			
			exporter = null;
		}
	}
	
	class exportMultiSpriteGBA implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//JFileChooser exporter = new JFileChooser();
			//exporter.setAcceptAllFileFilterUsed(false);
			//exporter.setFileFilter(new CFileFilter());
			//exporter.showDialog(MainFrame, "Export");
			
			MultiSpriteDesktop desktop = (MultiSpriteDesktop)FilePane.getSelectedComponent();
			File multiSpriteFile = desktop.getFile();
			
			MultiSpriteAnimation multiSpriteAnimation = desktop.getMultiSpriteAnimation();
			MultiSpriteCExport fileExporter = new MultiSpriteCExport();
						
			String name = multiSpriteFile.getName();
			name = name.substring(0, name.lastIndexOf("."));
			File outputFile = new File(multiSpriteFile.getParent() + "//" + "spriteset_" + name  + ".thumb.c");
			fileExporter.write(outputFile, name, multiSpriteAnimation);
		}
		
	}
	
	class convertToTiles implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser openFileDialog;
			openFileDialog = new JFileChooser();
			openFileDialog.setAcceptAllFileFilterUsed(false);
			openFileDialog.addChoosableFileFilter(new BraveKnightFileFilter());
			openFileDialog.setMultiSelectionEnabled(true);
			openFileDialog.showOpenDialog(MainFrame);
			
			if(openFileDialog.getSelectedFiles().length == 0)
			    return;
			File[] files = openFileDialog.getSelectedFiles();
			
			for(int i = 0; i<openFileDialog.getSelectedFiles().length; i++) {
			    ConvertFileToTiles.convertFile(files[i]);
			}
		}
	}
	
	class exitTabbedPaned implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			FilePane.remove(FilePane.getSelectedIndex());
		}
	}
	
	class CreateMultiSprite implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			createMultiSpriteDesktop(null);
		}
		
	}
	
	public static void showGUI() {
		@SuppressWarnings("unused")
		SpriteStudioMain display = new SpriteStudioMain();	
	}
	
	public static void main(String[] Args) {
		javax.swing.SwingUtilities.invokeLater (
			new Runnable(){
				public void run(){
					showGUI();	
				}
			}
		);
	}
	
	private class TabbedPaneSelectionListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent event) {
			if (FilePane.getSelectedComponent() instanceof SpriteDesktop) {
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export1DGBA.setEnabled(true);
				export1DGBALZSS.setEnabled(true);
				export1DGBALZSSFrame.setEnabled(true);
				exportMultiSprite.setEnabled(false);
			} else if (FilePane.getSelectedComponent() instanceof MultiSpriteDesktop) {
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export1DGBA.setEnabled(false);
				export1DGBALZSS.setEnabled(false);
				export1DGBALZSSFrame.setEnabled(false);
				exportMultiSprite.setEnabled(true);
			}
		}
		
	}
	
	private class CFileFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) 
		     {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1)
	        {
	            ext = fileName.substring(i+1).toLowerCase();
	        }
	        else 
	        	return false;
	        if(ext.matches("c") == true || ext.matches("h") == true)
	        	return true;

	        return false;
		}

		public String getDescription()  {
			return "C File(*.c,*.h)";
		}
	}
}