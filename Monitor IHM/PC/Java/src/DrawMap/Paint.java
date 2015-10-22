package DrawMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthSeparatorUI;

import frameTreatment.Area;

//classe "principale" de la partie graphique
public class Paint extends JPanel {

	private static final long serialVersionUID = 1L;

	private int posX;
	private int posY;
	private Area area;
	private DrawerVertex drawVertex;
	private DrawerStreet drawStreet;

	//constructeur prennant en parametre un Area
	public Paint(Area area){
		this.area = area;  
	}

	public void paintComponent(Graphics g) { 
		//appel pour la recuperation des coordonnees au clique de la souris
		addMouseListener(new Mouse());  
		super.paintComponent(g);
		//on cast le Graphics en Graphics2D
		Graphics2D g2 = (Graphics2D) g.create();
		Color c = g2.getColor();
		//definition d une couleur
		g2.setColor(Color.blue);
		//appel des classes de dessin des vertex et des street
		drawVertex = new DrawerVertex(area, g2);
		drawStreet = new DrawerStreet(area, g2);
		g2.setColor(c);
	}

	//	public int getPosX() {
	//		return posX;
	//	}
	//
	//	public void setPosX(int posX) {
	//		this.posX = posX;
	//	}
	//
	//	public int getPosY() {
	//		return posY;
	//	}
	//
	//	public void setPosY(int posY) {
	//		this.posY = posY;
	//	}
}