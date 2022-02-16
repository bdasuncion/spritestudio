package fileRW;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import infoObjects.MultiSpriteAnimation;
import infoObjects.SpriteFrame;
import infoObjects.SpriteFrameSet;
import tools.ImageTools;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class MultiSpriteFileReaderWriter {
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	private static final String EXT = ".kotr";
	private static final String ROOT = "multiSprite";
	private static final String FILES = "files";
	private static final String FILE = "file";
	private static final String MULTISPRITESET = "multiSpriteSet";
	private static final String SPRITEFRAMESET = "spriteFrameSet";
	private static final String SPRITEFRAME = "spriteFrame";
	private static final String FRAMENAME = "frameName";
	private static final String XOFFSET = "xOffset";
	private static final String YOFFSET = "yOffset";
	private static final String VERTICALFLIP = "verticalFlip";
	private static final String HORIZONTALFLIP = "horizontalFlip";
	private static final String FRAMEDISPLAYCOUNT = "frameDisplayCount";
	private static final String ISBASEFRAME = "isBaseFrame";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	
	private static final String ANIMATIONFILEEXT = ".knight";
	private static final String IDXMARKER = "_image";
	
    public MultiSpriteFileReaderWriter() {
    	
    }
    
    public void write(MultiSpriteAnimation multiSpriteAnimation, File f) {
    	
    	File fileOut = new File(f.getParent() + "\\" + this.appendExtension(f.getName()));
    	
    	factory = DocumentBuilderFactory.newInstance();
    	try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Document doc = builder.newDocument();
    	
    	Element rootElement = doc.createElement(ROOT);
        doc.appendChild(rootElement);
        
        rootElement.appendChild(createFiles(doc, multiSpriteAnimation));
        
        
        rootElement.appendChild(createMultiSpriteSet(doc, multiSpriteAnimation));
     
     // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(fileOut);
	        transformer.transform(source, result);
	        
	        // Output to console for testing
	        StreamResult consoleResult = new StreamResult(System.out);
	        transformer.transform(source, consoleResult);
	        
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Element createFileElement(Document doc, String fileName) {
        Element file = doc.createElement(FILE);
        file.appendChild(doc.createTextNode(fileName));
         
        return file;
    }
    
    private String appendExtension(String fileName) {
		if(fileName.length() <= EXT.length())
			return fileName + EXT;

		String ext = fileName.substring(fileName.length() - EXT.length()).toLowerCase();
          
		if(ext.matches(EXT) == true)
			return fileName;

		return fileName + EXT;
	}
    
    private Element createFiles(Document doc, 
    		MultiSpriteAnimation multiSpriteAnimation ) {
        Element files = doc.createElement(FILES);
        
        Collection<String> sourceFiles = multiSpriteAnimation.getSourceFiles();
        for (String sourceFile : sourceFiles) {
        	files.appendChild(createFileElement(doc, sourceFile));	
		}
    	return files;
    }
    
    private Element createMultiSpriteSet(Document doc, 
    		MultiSpriteAnimation multiSpriteAnimation) {
    	Element multispriteSet = doc.createElement(MULTISPRITESET);
    	
    	for (SpriteFrameSet spriteFrameSet: multiSpriteAnimation.getAnimation()) {
    		multispriteSet.appendChild(createSpriteFrameSet(doc, spriteFrameSet));
    	}
    	return multispriteSet;
    }
    
    private Element createSpriteFrameSet(Document doc, 
    		SpriteFrameSet spriteFrameSet) {
    	Element spriteFrameSetElement = doc.createElement(SPRITEFRAMESET);
    	
    	Element displayframecount = doc.createElement(FRAMEDISPLAYCOUNT);
    	displayframecount.appendChild(doc.createTextNode(Integer.toString(spriteFrameSet.getDisplayForNumberOfFrames())));
    	
    	Vector<SpriteFrame> spriteFrames = spriteFrameSet.getSpriteFrames();
    	for (int i = 0; i < spriteFrames.size(); ++i) {
    		spriteFrameSetElement.appendChild(createSpriteFrame(doc, spriteFrames.get(i),
    				i == spriteFrameSet.getBaseFrameIdx()));
    	}
    	
    	spriteFrameSetElement.appendChild(displayframecount);
    	
    	return spriteFrameSetElement;
    }
    
    private Element createSpriteFrame(Document doc, 
    		SpriteFrame spriteFrame, boolean isBaseFrame) {
    	
    	Element spriteFrameElement = doc.createElement(SPRITEFRAME);
    	
    	Element framename = doc.createElement(FRAMENAME);
    	framename.appendChild(doc.createTextNode(spriteFrame.getCompressedFrameName()));
    	
       	Element xoffset = doc.createElement(XOFFSET);
       	xoffset.appendChild(doc.createTextNode(Integer.toString(spriteFrame.getXOffset(), 0)));
    	
       	Element yoffset = doc.createElement(YOFFSET);
       	yoffset.appendChild(doc.createTextNode(Integer.toString(spriteFrame.getYOffset(), 0)));
       	
       	Element flipVertical = doc.createElement(VERTICALFLIP);
       	flipVertical.appendChild(doc.createTextNode(spriteFrame.isFlippedVertical() ? TRUE : FALSE));
       	
       	Element flipHorizontal = doc.createElement(HORIZONTALFLIP);
       	flipHorizontal.appendChild(doc.createTextNode(spriteFrame.isFlippedHorizontal() ? TRUE : FALSE));
       	
       	Element isbaseframe = doc.createElement(ISBASEFRAME);
       	isbaseframe.appendChild(doc.createTextNode(isBaseFrame ? TRUE : FALSE));
    	
    	spriteFrameElement.appendChild(framename);
    	spriteFrameElement.appendChild(xoffset);
    	spriteFrameElement.appendChild(yoffset);
    	spriteFrameElement.appendChild(flipVertical);
    	spriteFrameElement.appendChild(flipHorizontal);
    	spriteFrameElement.appendChild(isbaseframe);
    	return spriteFrameElement;
    }
    
    public MultiSpriteAnimation read(File f, MultiSpriteAnimation multiSpriteAnimation) {
    	//MultiSpriteAnimation multiSpriteAnimation = new MultiSpriteAnimation();
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	
        DocumentBuilder dBuilder;
        Document doc;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(f);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		 doc.getDocumentElement().normalize();
         //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList fileList = doc.getElementsByTagName(FILE);
         
         HashMap<String, BufferedImage[]> ssfrs = new HashMap<String, BufferedImage[]>();
         for (int idx = 0; idx < fileList.getLength(); idx++) {
        	 Node fileNode = fileList.item(idx);
        	 if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) fileNode;
                 String fileName = eElement.getTextContent();
                 multiSpriteAnimation.addSourceFile(fileName);
                 File sourceFile = new File(f.getParent() + "\\" + fileName);
                 
                 SpriteStudioFileReader ssfr = new SpriteStudioFileReader(sourceFile);
                 BufferedImage[] images = ImageTools.createMultiImages(ssfr.getWidth(), ssfr.getHeight(), ssfr.getNumOfFrames(), 
                		 ssfr.getPalette(), ssfr.getData());
                 ssfrs.put(fileName.substring(0, fileName.length() - ANIMATIONFILEEXT.length()), images);
        	 }
         }
         
         multiSpriteAnimation.getAnimation().removeAllElements();
         NodeList spriteFrameSet = doc.getElementsByTagName(SPRITEFRAMESET);
         for (int idx = 0; idx < spriteFrameSet.getLength(); idx++) {
        	 Node frameSetNode = spriteFrameSet.item(idx);
        	 if (frameSetNode.getNodeType()  == Node.ELEMENT_NODE) {
        		 Element eElement = (Element) frameSetNode;
        		 if (eElement.getTagName().matches(SPRITEFRAMESET)) {
		        	 //System.out.println("SPRITEFRAME SET");
		        	 multiSpriteAnimation.getAnimation().add(createSpriteFrameSet(frameSetNode.getChildNodes(), ssfrs));
		        	 //System.out.println("/SPRITEFRAME SET");
        		 }
        	 }
         }
         
         return multiSpriteAnimation;
    }
    
    private SpriteFrameSet createSpriteFrameSet(NodeList spriteFrames,  HashMap<String, BufferedImage[]> ssfrs) {
    	SpriteFrameSet spriteFrameSet = new SpriteFrameSet();
    	for (int idx = 0; idx < spriteFrames.getLength(); ++idx) {
    		
    		Node frameNode = spriteFrames.item(idx);
    		
    		if (frameNode.getNodeType() == Node.ELEMENT_NODE) {
    			Element eElement = (Element) frameNode;
    			if (eElement.getTagName() == SPRITEFRAME) {
    				//System.out.println("  SPRITEFRAME");
    				spriteFrameSet.addLayer(spriteFrameAttributes(frameNode.getChildNodes(), ssfrs));
	    			//System.out.println("  /SPRITEFRAME");
    			} else if (eElement.getTagName() == FRAMEDISPLAYCOUNT) {
    				spriteFrameSet.setDisplayForNumberOfFrames(Integer.parseInt(eElement.getTextContent()));
    				//System.out.println("  " + eElement.getTagName() + ":" + eElement.getTextContent());
    			}
    		}
    		
    	}
    	
    	return spriteFrameSet;
    }
    
    private SpriteFrame spriteFrameAttributes(NodeList attributes, HashMap<String, BufferedImage[]> ssfrs) {
    	SpriteFrame spriteFrame = new SpriteFrame();
    	for (int idx = 0; idx < attributes.getLength(); ++idx) {
    		Node attributeNode = attributes.item(idx);
    		
    		if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
    			Element eElement = (Element) attributeNode;
    			
    		    //System.out.println("    " + eElement.getTagName() + ":" + eElement.getTextContent());
    		    if (eElement.getTagName().matches(XOFFSET)) {
    		    	spriteFrame.setXOffset(Integer.parseInt(eElement.getTextContent()));
    		    } else if (eElement.getTagName().matches(YOFFSET)) {
    		    	spriteFrame.setYOffset(Integer.parseInt(eElement.getTextContent()));
    		    } else if (eElement.getTagName().matches(VERTICALFLIP)) {
    		    	//System.out.println(Boolean.parseBoolean(eElement.getTextContent()));
    		    	spriteFrame.flipImageVertical(Boolean.parseBoolean(eElement.getTextContent()));
    		    } else if (eElement.getTagName().matches(HORIZONTALFLIP)) {
    		    	//System.out.println(Boolean.parseBoolean(eElement.getTextContent()));
    		    	spriteFrame.flipImageHorizontal(Boolean.parseBoolean(eElement.getTextContent()));
    		    } else if (eElement.getTagName().matches(FRAMENAME)) {
    		    	String frameName = eElement.getTextContent();
    		    	spriteFrame.setCompressedFrameName(frameName);
    		    	
    		    	int idxMarker = frameName.lastIndexOf(IDXMARKER);
    		    	String keyName = frameName.substring(0, idxMarker);
    		    	BufferedImage[] images = ssfrs.get(keyName);
    		        //System.out.println("KEYNAME" + ":"+  keyName);
    		    	
    		    	if (images != null) {
    		    		int imgIdx = Integer.parseInt(frameName.substring(idxMarker + IDXMARKER.length()));
    		    		spriteFrame.setImage(images[imgIdx]);
    		    		spriteFrame.setImageBGTransparent(true);
    		    	}
    		    }
    		}
    	}
    	
    	return spriteFrame;
    }
}
