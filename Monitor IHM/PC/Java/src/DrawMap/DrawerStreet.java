package DrawMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import frameTreatment.Area;
import frameTreatment.Streets;
import frameTreatment.Vertex;

//classe pour dessiner les streets 
public class DrawerStreet {

	private int posX1;
	private int posY1;
	private int posX2;
	private int posY2;
	private String vertexName1;
	private String vertexName2;
	private String streetName;
	//constructeur qui prend en parametre un objet Area et un Graphics
	public DrawerStreet(Area area, Graphics2D g) {
		//pour toutes les streets contenu dans l Area
		for (Streets street : area.getMap().getStreets()){
			//recuperation du nom de la street ainsi que des noms des 2 vertex
			streetName = street.getName();
			vertexName1 = street.getPath().get(0);
			vertexName2 = street.getPath().get(1);
			//pour tous les vertex contenu dans l Area
			for(Vertex vertex : area.getMap().getVertices()) {
				//on compare le nom du vertex et celui recuperer dans le street
				//si cest le meme nom 
				if (vertex.getName() == vertexName1){
					//recuperation des coordonnees du vertex
					posX1 = (int) vertex.getX();
					posY1 = (int) vertex.getY();
				}
				if (vertex.getName() == vertexName2){
					posX2 = (int) vertex.getX();
					posY2 = (int) vertex.getY();
				}
			}
			//execute la fonction de dessin de la steet
			buildStreet(streetName,posX1,posY1,posX2,posY2,g);		
		}

	}
	//fonction de dessin de la street
	public void buildStreet(String name, int x1, int y1, int x2, int y2, Graphics2D g) {

		int ptX1, ptY1, ptX2, ptY2; 

		//		Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		//		double width_frame = size.getWidth();
		//		double height_frame = size.getHeight();
		//		int width_frame_int = (int)width_frame - 100;
		//		int height_frame_int = (int)height_frame - 100;

		//conversion des coordonnees en fonction de la taille de la fenetre
		ptX1 = (int) (x1 * 1340);
		ptY1 = (int) (y1 * 800);
		ptX2 = (int) (x2 * 1340);
		ptY2 = (int) (y2 * 800);

		System.out.println("Street " + name +  " de (" + ptX1 + "," + ptY1 + ") a (" + ptX2 + "," + ptY2 + ") ");
		//definition d une couleur
		g.setColor(Color.blue);
		//dessin d une ligne representant une street
		g.drawLine(ptX1, ptY1, ptX2, ptY2);

	}
}
