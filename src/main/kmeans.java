package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class kmeans {
	static BufferedImage bimg;
	static int K = 5;

	public static void main(String[] args) {

		try {
			bimg = ImageIO.read(new File("img/colorido.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Pixel> pixels = new ArrayList<>();
		ArrayList<Centro> centros = new ArrayList<>();

		for (int i = 0; i < bimg.getWidth(); i++) {
			for (int j = 0; j < bimg.getHeight(); j++) {
				Pixel pixel = new Pixel();
				pixel.setX(i);
				pixel.setY(j);
				Color color = new Color(bimg.getRGB(i, j));
				pixel.setR(color.getRed());
				pixel.setG(color.getGreen());
				pixel.setB(color.getBlue());
				pixels.add(pixel);
			}
		}
		/**************Centros********************************/
		
		Centro centro = new Centro();
		centro.setR(pixels.get(0).getR());
		centro.setG(pixels.get(0).getG());
		centro.setB(pixels.get(0).getB());
		centros.add(centro);
		for(int i = 1;i<K;i++) {
			kmeansPlusPlus(pixels,centros);
		}
		/***********************************************************************/
		ArrayList<Centro> centrosbk = new ArrayList<Centro>();
		Pertence(pixels,centros);
		centrosbk = centros;
		double j = J(centrosbk);
		MoverCentro(centros);
		Pertence(pixels,centros);
		while(J(centros)<j) {
			centrosbk = centros;
			j = J(centrosbk);
			MoverCentro(centros);
			Pertence(pixels,centros);
		}
		
		PintarPixel(centrosbk);
		BufferedImage saida = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
		CriarImg(saida,centrosbk);
		File f = new File("img/Saida_colorido.png");
		try {
			ImageIO.write(saida,"png",f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<centros.size();i++) {
			System.out.println("centro "+i+ " tem: "+ centros.get(i).getPixels().size());
		}
	}
	
	
	public static void kmeansPlusPlus(ArrayList<Pixel> pixels, ArrayList<Centro> centros) {
		double max = 0;
		int x=0;
		for(int i = 0;i<centros.size();i++) {
			for(int j=0;j<pixels.size();j++) {
				if(Distancia(pixels.get(j),centros.get(i))>max && isCenter(pixels.get(j),centros)) {
					max = Distancia(pixels.get(j),centros.get(i));
					x = j;
				}
			}
		}
		Centro centro = new Centro();
		centro.setR(pixels.get(x).getR());
		centro.setG(pixels.get(x).getG());
		centro.setB(pixels.get(x).getB());
		centros.add(centro);
	}


	public static boolean isCenter(Pixel pixel, ArrayList<Centro> centros) {
		for(int i=0;i<centros.size();i++) {
			if(pixel.getR() == centros.get(i).getR() && pixel.getG() == centros.get(i).getG() && pixel.getB() == centros.get(i).getB()) {
				return false;
			}
		}
		return true;
	}


	public static void CriarImg(BufferedImage saida, ArrayList<Centro> centrosbk) {
		for(int i=0;i<centrosbk.size();i++) {
			for(int j=0;j < centrosbk.get(i).getPixels().size();j++) {
				saida.setRGB(centrosbk.get(i).getPixels().get(j).getX(), centrosbk.get(i).getPixels().get(j).getY(), centrosbk.get(i).getColor().getRGB());	
			}
		}
	}



	public static void PintarPixel(ArrayList<Centro> centros) {
		for(int i=0;i<centros.size();i++) {
			Color color = new Color(centros.get(i).getPixels().get(0).getR(),centros.get(i).getPixels().get(0).getG(),centros.get(i).getPixels().get(0).getB());
			centros.get(i).setColor(color);
			for(int j=0;j<centros.get(i).getPixels().size();j++) {
				centros.get(i).getPixels().get(j).setColor(centros.get(i).getColor());
			}
		}
	}



	public static double J(ArrayList<Centro> centros) {
		double x = 0;
		for (int i = 0; i < centros.size(); i++) {
			double n = 0;
			for (int j = 0; j < centros.get(i).getPixels().size(); j++) {
				n = n + Math.pow(Distancia(centros.get(i).getPixels().get(j), centros.get(i)), 2);
			}
			x = x + n;
		}
		return x;
	}
	
	public static double Distancia(Pixel x1, Centro centro) {
		double x = x1.getR() - centro.getR();
		x = x * x;

		double y = x1.getG() - centro.getG();
		y = y * y;
		
		double z = x1.getB() - centro.getB();
		z = z * z;

		return Math.sqrt(x + y + z);// ok
	}
	
	public static void MoverCentro(ArrayList<Centro> centros) {

		for (int j = 0; j < centros.size(); j++) {
			double x = 0;
			double y = 0;
			double z = 0;
			int Q = centros.get(j).getPixels().size();
			for (int i = 0; i < Q; i++) {
				x = x + centros.get(j).getPixels().get(i).getR();
				y = y + centros.get(j).getPixels().get(i).getG();
				z = z + centros.get(j).getPixels().get(i).getB();
			}
			x = x / Q;
			y = y / Q;
			z = z/Q;
			centros.get(j).setR(x);
			centros.get(j).setG(y);
			centros.get(j).setB(z);
		}

	}
	
	public static void Pertence(ArrayList<Pixel> pixels, ArrayList<Centro> centros) {
		for(int i=0;i<centros.size();i++) {
			centros.get(i).getPixels().clear();
		}
		for (int i = 0; i < pixels.size(); i++) {
			ArrayList<Pixel> p = centroMaisProximo(pixels.get(i), centros).getPixels();
			p.add(pixels.get(i));
			centroMaisProximo(pixels.get(i), centros).setPixels(p);
		}
	}
	
	public static Centro centroMaisProximo(Pixel p, ArrayList<Centro> centros) {
		int x = 0;
		Double min = Distancia(p, centros.get(0));
		for (int i = 0; i < centros.size(); i++) {
			if (Distancia(p, centros.get(i)) <= min) {
				min = Distancia(p, centros.get(i));
				x = i;
			}
		}
		return centros.get(x);
	}
	public static void ShowCenters(ArrayList<Centro> centros) {
		for (int i = 0; i < centros.size(); i++) {
			System.out.println(
					"Centro (" + centros.get(i).getR() + ", " + centros.get(i).getG() + centros.get(i).getB()+ ") tem esses pontos: ");
			for (int j = 0; j < centros.get(i).getPixels().size(); j++) {
				System.out.println("(" + centros.get(i).getPixels().get(j).getR() + ", "
						+ centros.get(i).getPixels().get(j).getG() + centros.get(i).getPixels().get(j).getB()+ ")");
			}
		}
		System.out.println("-----------------------------------------------------");
	}
}
