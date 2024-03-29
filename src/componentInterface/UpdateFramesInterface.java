package componentInterface;

import infoObjects.SpriteFrame;
import java.util.Vector;

public interface UpdateFramesInterface {
    public void updateFrames(Vector<SpriteFrame> spriteFrames);
    public void deleteFrames(Vector<SpriteFrame> spriteFrames, String sourceFile);
}
