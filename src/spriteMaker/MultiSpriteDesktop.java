package spriteMaker;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import componentInterface.CanvasInterface;
import componentInterface.ImportFramesInterface;
import componentInterface.ExternalFileInterface;
import componentInterface.UpdateFramesInterface;
import fileRW.ExternalFileManager;
import fileRW.KnightsOfTheRoundFileFilter;
import fileRW.MultiSpriteFileReaderWriter;
import fileRW.SpriteStudioFileReader;
import infoObjects.MultiSpriteAnimation;
import infoObjects.SpriteFrame;
import infoObjects.SpriteFrameSet;
import javafx.util.Pair;
import multiSprite.Frame.ImportFrameSelectionFrame;
import multiSprite.Frame.MultiSpriteAnimationFrame;
import multiSprite.Frame.MultiSpriteAnimationFrameSelectionFrame;
import multiSprite.Frame.MultiSpriteCanvasFrame;

public class MultiSpriteDesktop  extends JDesktopPane implements 
    ImportFramesInterface, UpdateFramesInterface, ExternalFileInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JInternalFrame SpriteAnimationFrame;
	private MultiSpriteCanvasFrame multiSpriteCanvasFrame;
	private MultiSpriteAnimationFrameSelectionFrame multiSpriteSelectionFrame;
	private MultiSpriteAnimationFrame multiSpriteAnimationFrame;
	private MultiSpriteAnimation animation;
	private File saveFile = null;

	public MultiSpriteDesktop(JFrame owner) {
		super();
		
		animation = new MultiSpriteAnimation();
		multiSpriteCanvasFrame = new MultiSpriteCanvasFrame(this);
		multiSpriteCanvasFrame.setLocation(50, 100);
		
		this.add(multiSpriteCanvasFrame);
		
		multiSpriteSelectionFrame = new MultiSpriteAnimationFrameSelectionFrame
				(multiSpriteCanvasFrame.getMultiSpriteFrameInterface(), animation);
		multiSpriteSelectionFrame.setLocation(200, 100);
		
		this.add(multiSpriteSelectionFrame);
		
		multiSpriteCanvasFrame.
		    setMultispriteFrameSetInterface(multiSpriteSelectionFrame.getMultispriteFrameSetInterface());
		
		multiSpriteAnimationFrame = new MultiSpriteAnimationFrame();
		multiSpriteAnimationFrame.setMultiSpriteAnimation(animation);
		multiSpriteAnimationFrame.setLocation(0, 0);
		multiSpriteAnimationFrame.setSize(100, 100);
		this.add(multiSpriteAnimationFrame);
	}

	@Override
	public void importFrameSelection(SpriteStudioFileReader file, String filePath, String fileName, CanvasInterface ci) {
		ImportFrameSelectionFrame spriteFrameSelection = 
				new ImportFrameSelectionFrame(file, filePath,  fileName, ci, this);
		this.add(spriteFrameSelection);
		animation.addSourceFile(fileName);
	}

	@Override
	public void updateFrames(Vector<SpriteFrame> spriteFramesRenewed) {
		Vector<SpriteFrameSet> spriteFrameSets = animation.getAnimation();
		
		for (SpriteFrameSet spriteFrameSet : spriteFrameSets) {
			for (SpriteFrame spriteFrame : spriteFrameSet.getSpriteFrames()) {
				for (SpriteFrame spriteFrameRenewed : spriteFramesRenewed) {
					if (spriteFrameRenewed.getCompressedFrameName().equalsIgnoreCase(spriteFrame.getCompressedFrameName())) {
						spriteFrame.setImage(spriteFrameRenewed.getImage());
						spriteFrame.setImageBGTransparent(true);
					}
				}
			}
		}
		multiSpriteCanvasFrame.getContentPane().repaint();
		multiSpriteSelectionFrame.getContentPane().repaint();
	}
	
	@Override
	public void deleteFrames(Vector<SpriteFrame> spriteFramesToDelete, String sourceFileToDelete) {
		Vector<SpriteFrameSet> spriteFrameSets = animation.getAnimation();
		
		for (String sourceFile : animation.getSourceFiles()) {
			if (sourceFileToDelete.contains(sourceFile)) {
				animation.getSourceFiles().remove(sourceFile);
				break;
			}
		}
		
		for (SpriteFrame spriteFrameToDelete : spriteFramesToDelete) {
			findAndDelete(spriteFrameToDelete, spriteFrameSets);
		}
		
		multiSpriteCanvasFrame.getContentPane().repaint();
		multiSpriteSelectionFrame.getContentPane().repaint();
	}
	
	void findAndDelete(SpriteFrame frameToDelete, Vector<SpriteFrameSet> spriteFrameSets) {
		boolean hasRemoved = true;
		while(hasRemoved) {
			hasRemoved = false;
			for (SpriteFrameSet spriteFrameSet : spriteFrameSets) {
				int idxFrame = 0;
				for (SpriteFrame spriteFrame : spriteFrameSet.getSpriteFrames()) {
					if (frameToDelete.getCompressedFrameName().equalsIgnoreCase(spriteFrame.getCompressedFrameName())) {
						spriteFrameSet.getSpriteFrames().removeElementAt(idxFrame);
						hasRemoved = true;
						break;
					}
					++idxFrame;
				}
			}
		}
	}

	@Override
	public String save() {
        ExternalFileManager fileManager = new ExternalFileManager();
		
		if (saveFile == null) {
			saveFile = fileManager.getFileFromFileDialog(false, this, new KnightsOfTheRoundFileFilter());
		}
		
		if (saveFile == null) {
			return null;
		}
		
		
		@SuppressWarnings("unused")
		MultiSpriteFileReaderWriter fileOut =
			new MultiSpriteFileReaderWriter();
		
		fileOut.write(animation, saveFile);
		
		return saveFile.getName();		
	}

	@Override
	public String saveAs() {
		 ExternalFileManager fileManager = new ExternalFileManager();
		saveFile = fileManager.getFileFromFileDialog(false, this, new KnightsOfTheRoundFileFilter());
	
		if (saveFile == null) {
			return null;
		}
		
		@SuppressWarnings("unused")
		MultiSpriteFileReaderWriter fileOut =
			new MultiSpriteFileReaderWriter();
		
		fileOut.write(animation, saveFile);
		
		return saveFile.getName();		
	}

	@Override
	public String openFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMultiSprite(String path, File file) {
		Collection<String> fileNames = animation.getSourceFiles();
		for (String fileName : fileNames) {
			String fullpath = path + "\\" + fileName;
			System.out.println(fullpath);
			File sourceFile = new File(fullpath);
			SpriteStudioFileReader ssfr = new SpriteStudioFileReader(sourceFile);
			importFrameSelection(ssfr, fullpath, fileName, multiSpriteCanvasFrame.getCanvasInterface());
		}
		saveFile = file;
		multiSpriteSelectionFrame.getMultispriteFrameSetInterface().setFrameSet();
	}
	
	public MultiSpriteAnimation getMultiSpriteAnimation() {
		return animation;
	}
	
	public File getFile() {
		return saveFile;
	}
}
