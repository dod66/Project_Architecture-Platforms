package DrawMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import frameTreatment.Area;
import frameTreatment.Vertex;

//classe pour dessiner les vertex 
public class DrawerVertex {

	private int posX;
	private int posY;
	private float x, y;
	private String name;
	//constructeur qui prend en parametre un objet Area et un Graphics
	public DrawerVertex(Area area, Graphics2D g) {
		System.out.println(area.getMap());
		//pour tous les objets vertex contenu dans l Area
		for (Vertex vertex : area.getMap().getVertices()){
			//recuperation des coordonnees et du nom du vertex
			x = vertex.getX();
			y = vertex.getY();
			name = vertex.getName();
			//execute la fonction pour dessiner le vertex
			buildVertex(name,x,y,g);		
		}
	}
	//fonction pour dessiner le vertex
	public void buildVertex(String name, float x, float y, Graphics2D g) {

		//		Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		//		double width_frame = size.getWidth();
		//		double height_frame = size.getHeight();
		//		int width_frame_int = (int)width_frame - 100;
		//		int height_frame_int = (int)height_frame - 100;

		//conversion des coordonnees en fonction de la taille de la fenetre
		posX = (int) (x * 1340);
		posY = (int) (y * 800);

		System.out.println("Vertex " + name +  " :" + posX + ", " + posY);
		//definition d une couleur
		g.setColor(Color.BLUE);
		//dessin du rectable representant un vertex
		g.drawRect(posX, posY, 20, 20);

	}
}
