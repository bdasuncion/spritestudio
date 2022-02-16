package componentInterface;

import infoObjects.SpriteFrameSet;

public interface MultiSpriteFrameInterface {
     public SpriteFrameSet getSpriteFrames();
     public void setSpriteFrames(SpriteFrameSet sfs);
     public void updateView();
}
