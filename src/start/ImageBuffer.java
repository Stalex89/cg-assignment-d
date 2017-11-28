package start;

public class ImageBuffer
{
	private DrawPanel drawPanel;
	private MiniaturePanel miniaturePanel;
	private THPicture bPicture;
		
	public void setDrawPanel(DrawPanel drawPanel) 
	{
	   this.drawPanel = drawPanel;
	}
   
	public void setMiniaturePanel(MiniaturePanel miniaturePanel) 
	{
	   this.miniaturePanel = miniaturePanel;
	}
	
	public void setBufferImage(THPicture bufferImage) throws Exception
	{
		try 
		{
			this.bPicture = bufferImage;
			System.out.println("buffer loaded with image");
		}
		catch(Exception ex)
		{
			ex.getStackTrace();
		}
	}
	
	public THPicture getBufferImage() throws Exception
	{
		if(bPicture != null)
			return bPicture;
		else throw new Exception("Buffer has no image or failed to open");
	}
	
	public void clearBufferImage()
	{
		if(bPicture != null)
			bPicture = null;
		
	}
}
