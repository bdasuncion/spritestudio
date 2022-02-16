package spriteMaker.Frames.Layer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

import componentInterface.SpriteFramesInterface;
import componentInterface.SpriteLayerPanelInterface;
import infoObjects.SpriteFrame;
//import infoObjects.SpriteFrameInformation;
import componentInterface.PaletteInterface;
import spriteMaker.Frames.SpriteFrameCanvas;
import spriteMaker.Frames.SpriteFramePanel;
import tools.ImageTools;

public class SpriteLayerPanel extends JComboBox<SpriteFrame> 
implements ListCellRenderer<SpriteFrame>, SpriteLayerPanelInterface
{
	private static final long serialVersionUID = -4501542134905797732L;
	//private Vector<SpriteFrameCanvas> spriteFramePanelCollection;
	//private SpriteFramesInterface spriteFrameInterface;
	
	public SpriteLayerPanel(SpriteFramesInterface sfinterface)
	{
		super(new DefaultComboBoxModel<SpriteFrame>());
		setRenderer(this);
		//addActionListener(this);
		//addItemListener(this);
	}
	
	@Override
	public void addItem(SpriteFrame spriteFrame) {
		DefaultComboBoxModel<SpriteFrame> model = 
				(DefaultComboBoxModel<SpriteFrame>) this.getModel();
		model.addElement(spriteFrame);
	}
	
	@Override
	public void remove(int idx) {
		DefaultComboBoxModel<SpriteFrame> model = 
				(DefaultComboBoxModel<SpriteFrame>) this.getModel();
		model.removeElement(idx);
	}
	
	@Override
	public void removeAllItems() {
		DefaultComboBoxModel<SpriteFrame> model = 
				(DefaultComboBoxModel<SpriteFrame>) this.getModel();
		model.removeAllElements();
	}

	public SpriteFramePanel getActivePanel()
	{
		return (SpriteFramePanel)getSelectedItem();
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends SpriteFrame> list, SpriteFrame value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JPanel emptyPanel = new JPanel();
		if (value != null) {
			emptyPanel.add(new SpriteFrameCanvas(value.getImage()));
			return emptyPanel;
		}
		emptyPanel.add(new SpriteFrameCanvas(
    			ImageTools.createEmptyImage(16, 16, ImageTools.SIZE_4BITS_PERPIXEL)));
		return emptyPanel;
	}
}
