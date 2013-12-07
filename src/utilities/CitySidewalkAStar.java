package utilities;

import java.util.Comparator;

import city.gui.CitySidewalk;

public class CitySidewalkAStar {

	// Data

	private double heuristicScore;
	private int numMovesFromStart;
	private double finalScore;
	private CitySidewalk sidewalk; 
	private CitySidewalkAStar previousSidewalkAStar;

	// Constructor

	public CitySidewalkAStar(CitySidewalkAStar previousSidewalk, CitySidewalk currentSidewalk, CitySidewalk endingSidewalk, int numMovesFromStart) {
		this.sidewalk = currentSidewalk;
		this.previousSidewalkAStar = previousSidewalk;
		this.numMovesFromStart = numMovesFromStart;
		heuristicScore = Math.sqrt((double)(Math.pow(endingSidewalk.getX() - currentSidewalk.getX(), 2) + Math.pow(currentSidewalk.getY() - currentSidewalk.getY(), 2)));
		this.finalScore = numMovesFromStart + heuristicScore;
	}

	// Getters

	public double getHeuristicScore() {
		return heuristicScore;
	}

	public int getNumMovesFromStart() {
		return numMovesFromStart;
	}

	public CitySidewalk getSidewalk() {
		return sidewalk;
	}

	public CitySidewalkAStar getPreviousAStarSidewalk() {
		return previousSidewalkAStar;
	}

	public double getFinalScore() {
		return finalScore;
	}

	// Setters

	public void setPreviousSidewalk(CitySidewalkAStar previousSidewalk) {
		this.previousSidewalkAStar = previousSidewalk;
	}

	// Utilities

	public static Comparator<CitySidewalkAStar> CitySidewalkAStarNameComparator = new Comparator<CitySidewalkAStar>() {

		public int compare(CitySidewalkAStar CitySidewalkAStar1, CitySidewalkAStar CitySidewalkAStar2) {

			if(CitySidewalkAStar1.getFinalScore() > CitySidewalkAStar2.getFinalScore())
				return 1;
			else if(CitySidewalkAStar1.getFinalScore() < CitySidewalkAStar2.getFinalScore())
				return -1;
			else if(CitySidewalkAStar1.getNumMovesFromStart() > CitySidewalkAStar2.getNumMovesFromStart())
				return 1;
			else if(CitySidewalkAStar1.getNumMovesFromStart() < CitySidewalkAStar2.getNumMovesFromStart())
				return -1;
			else
				return 0;
		}

	};
}
