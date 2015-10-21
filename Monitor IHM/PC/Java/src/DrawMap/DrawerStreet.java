package DrawMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import frameTreatment.Area;
import frameTreatment.Streets;
import frameTreatment.Vertex;

public class DrawerStreet {
	
	private int posX1;
	private int posY1;
	private int posX2;
	private int posY2;
	private String vertexName1;
	private String vertexName2;
	private String streetName;
	
		public DrawerStreet(Area area, Graphics2D g) {
			
			for (Streets street : area.getMap().getStreets()){
				streetName = street.getName();
				vertexName1 = street.getPath().get(0);
				vertexName2 = street.getPath().get(1);
				
				for(Vertex vertex : area.getMap().getVertices()) {
					if (vertex.getName() == vertexName1){
						posX1 = (int) vertex.getX();
						posY1 = (int) vertex.getY();
					}
					if (vertex.getName() == vertexName2){
						posX2 = (int) vertex.getX();
						posY2 = (int) vertex.getY();
					}
				}
				buildStreet(streetName,posX1,posY1,posX2,posY2,g);		
			}
			
		}
		
		public void buildStreet(String name, int x1, int y1, int x2, int y2, Graphics2D g) {
			
			int ptX1, ptY1, ptX2, ptY2; 
			
			Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	        double width_frame = size.getWidth();
	        double height_frame = size.getHeight();
	        int width_frame_int = (int)width_frame;
	        int height_frame_int = (int)height_frame;
			
	        ptX1 = (int) (x1 * width_frame_int);
	        ptY1 = (int) (y1 * height_frame_int);
	        ptX2 = (int) (x2 * width_frame_int);
	        ptY2 = (int) (y2 * height_frame_int);
				
			System.out.println("Street " + name +  " de (" + ptX1 + "," + ptY1 + ") à (" + ptX2 + "," + ptY2 + ") ");
				
			g.setColor(Color.blue);
			g.drawLine(ptX1, ptY1, ptX2, ptY2);
			
		}
}
