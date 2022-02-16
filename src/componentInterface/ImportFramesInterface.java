package componentInterface;

import fileRW.SpriteStudioFileReader;

public interface ImportFramesInterface {
    public void importFrameSelection(SpriteStudioFileReader file, String filePath, String fileName, CanvasInterface ci);
   //public void importFrameSelection(String filePath, String fileName, CanvasInterface ci);
}
