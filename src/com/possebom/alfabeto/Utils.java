package com.possebom.alfabeto;

public final class Utils {
	
	public static String fixLetter(final String letter){
		String play = letter;
		if (letter.equals("A")) {
			play = "há";
		} else if (letter.equals("O")) {
			play = "ó";
		} else if (letter.equals("T")) {
			play = "tê";
		} else if (letter.equals("Q")) {
			play = "quê";
		} else if (letter.equals("E")) {
			play = "é";
		} else if (letter.equals("M")) {
			play = "eme";
		} else if (letter.equals("N")) {
			play = "ênê";
		} else if (letter.equals("D")) {
			play = "de";
		} else if (letter.equals("G")) {
			play = "gê";
		}
		return play;
	}
	
	public static String letterToWord(final String word){
		String play = word;
		if (word.equals("A")) {
			play = "há de abelha";
		} else if (word.equals("B")) {
			play = "b de borboleta";
		} else if (word.equals("C")) {
			play = "cê de cachorro";
		} else if (word.equals("D")) {
			play = "dee, de dinossauro";
		} else if (word.equals("E")) {
			play = "E de elefante";
		} else if (word.equals("F")) {
			play = "éfê de formiga";
		} else if (word.equals("G")) {
			play = "gê de gato";
		} else if (word.equals("H")) {
			play = "h de hipopotamo";
		} else if (word.equals("I")) {
			play = "i de índio";
		} else if (word.equals("J")) {
			play = "jota de jacaré";
		} else if (word.equals("L")) {
			play = "élê de leão";
		} else if (word.equals("M")) {
			play = "eme de macaco";
		} else if (word.equals("N")) {
			play = "ene de navio";
		} else if (word.equals("O")) {
			play = "hô de ônibus";
		} else if (word.equals("P")) {
			play = "pê de pinguin";
		} else if (word.equals("Q")) {
			play = "quê de queijo";
		} else if (word.equals("R")) {
			play = "r de rato";
		} else if (word.equals("S")) {
			play = "ésse de sapo";
		} else if (word.equals("T")) {
			play = "tê de tartaruga";
		} else if (word.equals("U")) {
			play = "u de uva";
		} else if (word.equals("V")) {
			play = "v de vaca";
		} else if (word.equals("X")) {
			play = "x de xícara";
		} else if (word.equals("Z")) {
			play = "z de zebra";
		}
		return play;
	}

}
