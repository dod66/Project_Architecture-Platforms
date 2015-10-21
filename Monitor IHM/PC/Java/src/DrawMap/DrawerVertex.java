package DrawMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import frameTreatment.Area;
import frameTreatment.Vertex;


public class DrawerVertex {

	private int posX;
	private int posY;
	private float x, y;
	private String name;
	
		public DrawerVertex(Area area, Graphics2D g) {
			
			for (Vertex vertex : area.getMap().getVertices()){
				x = vertex.getX();
				y = vertex.getY();
				name = vertex.getName();
				
				buildVertex(name,x,y,g);		
			}
		}
		
		public void buildVertex(String name, float x, float y, Graphics2D g) {
			
			Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	        double width_frame = size.getWidth();
	        double height_frame = size.getHeight();
	        int width_frame_int = (int)width_frame;
	        int height_frame_int = (int)height_frame;
	        
	        posX = (int) (x * width_frame_int);
			posY = (int) (y * height_frame_int);
			
			System.out.println("Vertex " + name +  " :" + posX + ", " + posY);
			
			g.setColor(Color.blue);
			g.drawRect(posX, posY, 20, 20);
	        
		}
}
