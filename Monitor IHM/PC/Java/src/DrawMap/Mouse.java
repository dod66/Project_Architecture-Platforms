package DrawMap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Mouse implements MouseListener
{
	public int x, y;
	
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.printf("(x = %d,y = %d)",e.getX(),e.getY());
            x = e.getX();
            y = e.getY(); 
        }
        
    }
    
    public void sendJsonVertex() {
    	String json = "{\"cabRequest\":[{\"area\":\"Quartier-Nord\",\"location\":[{\"area\":\"Quartier-Nord\",\"locationType\":\"vertex\",\"location\":\"m\"}]}]}";
    	System.out.println(json);
    }
    public void sendJsonStreet() {
    	String json = "{\"cabRequest\":[{\"area\":\"Quartier-Nord\",\"location\":[{\"area\":\"Quartier-Nord\",\"locationType\":\"vertex\",\"location\":{\"from\":\"m\",\"to\":\"b\",\"progression\":\"1\"}}]}]}";
    	System.out.println(json);
    }

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    
}