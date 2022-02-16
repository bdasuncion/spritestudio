package componentInterface;

import java.awt.image.BufferedImage;
import java.util.Vector;

import infoObjects.SpriteFrame;

public interface ViewTypeInterface {
	//public void setAnimation(Vector<SpriteFrame> framesAnimation);
	public void setNormalView();
	public void setTileView();
	public void setAutoTileView();
}
