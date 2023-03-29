import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe dédiée à la création de plateau de jeu spécifique au jeu du Minesweeper.
 * @author patrice
 */
public class MinesweeperGameBoard 
{
	// Un entier qualifiant les dimensions du plateau de jeu side x side.
	private int side;

	// Un entier dédié au nombre maximum de mines.
	private int nbMines;

	// Un tableau à double dimensions correspondant au plateau de jeu.
	private char aBoard[][];

	// Un tableau à double dimension enregistrant les emplacements des mines pour faciliter leur accès
	// et ainsi éviter de parcourir tout le tableau de jeu
	private int mines[][];

	/**
	 * Constructeur de la classe MinesweeperGameBoard assignant les attributs side, nbMines, aBoard, mines.
	 * 
	 * @param difficultyLevel, un entier saisi par l'utilisateur précisant le niveau de difficulté choisi 
	 * et influant sur le nombre de mines et la taille du plateau de jeu.
	 * @param isSolutionBoard, un booléen spécifie si le plateau de jeu à instancier doit être celui dédié à un plateau intégrant
	 * les mines (GameSolution) ou non (GameBoard).
	 */
	public MinesweeperGameBoard(int difficultyLevel, boolean isSolutionBoard)
	{
		this.side = difficultyLevel == 0 ? 9 : 
			difficultyLevel == 1 ? 16 : 
				difficultyLevel == 2 ? 24 : 0;

		this.nbMines = difficultyLevel == 0 ? 10 : 
			difficultyLevel == 1 ? 40 : 
				difficultyLevel == 2 ? 99 : 0;

		this.aBoard = new char[this.side][this.side];

		this.mines = new int[this.nbMines][2];

		if (isSolutionBoard)
		{
			this.aBoard = initBoard(this.aBoard);
			this.aBoard = placeMines(this.aBoard);
		}
		else
			this.aBoard = initBoard(this.aBoard);
	}

	/**
	 * Initialisation de l’ensemble des caractères du tableau à deux dimensions aBoard, 
	 * chaque cellule prendra comme valeur le symbole - autrement dit le demi cadratin.
	 * 
	 * @param aBoard un tableau à double dimension de type char équivalent au plateau de jeu.
	 * 
	 * @return le tableau de jeu aBoard une fois modifié.
	 */
	public char[][] initBoard(char[][] aBoard)
	{
		for (int line = 0; line < this.side; line ++)
		{
			for (int column = 0; column < this.side; column++)
			{
				this.aBoard[line][column] = '-';
			}
		}
		return this.aBoard;
	}

	/**
	 * Placer un nombre de mines dans le plateau de jeu dépendant de la difficultée 
	 * désirée par l’utilisateur et conserver leurs coordonnées dans un tableau à deux 
	 * dimension dénommé mines.
	 * 
	 * @param aBoard un tableau à double dimension de type char équivalent au plateau de jeu.
	 * 
	 * @return le tableau de jeu aBoard une fois modifié.
	 */
	public char[][] placeMines(char[][] aBoard)
	{
		boolean[] mark = new boolean[this.side * this.side];

		// Initialisation du tableau mark à false.
		for (int indiceMark = 0; indiceMark < (this.side * this.side); indiceMark ++)
			mark[indiceMark] = false;

		int indice = 0;

		while(indice < this.nbMines)
		{
			// Déterminer aléatoirement des coordonnées dédiées à l'emplacement d'une mine.
			int random = (int) (Math.random() * (this.side * this.side));
			int coordX = (random / this.side);
			int coordY = random % this.side;

			/*
			 *  Si l'indice dans mark n'a pas déja été utilisé, 
			 *  enregistrer les nouvelles coordonnées de l'emplacement d'une mine 
			 *  dans le tableau à double dimension dénommé mines.
			 */

			if (mark[random] == false)
			{
				this.mines[indice][0] = coordX;
				this.mines[indice][1] = coordY;
				//Place la mine dans aBoard.
				this.aBoard[coordX][coordY] = '*';
				mark[random] = true;
				indice ++;
			}
		}
		return aBoard;
	}

	/**
	 * Affiche le plateau de jeu avec un encadrement de 
	 * lettres nécessaires pour aider l’utilisateur à déterminer 
	 * les coordonnées d’une cellule qu’il choisirait à chaque tour.
	 */
	public void printBoard()
	{
		int line, column;

		System.out.print("    ");

		for (line = 97; line < (97 + this.side); line ++)
			System.out.print((char) line + " ");

		System.out.println();

		for (line = 97; line < (97 + this.side); line++)
		{
			System.out.print((char) line + "   ");
			for (column = 0; column < this.side; column++)
			{
				System.out.print(this.aBoard[line - 97][column] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Placer une mine à un autre emplacement du plateau de jeu pour éviter que le joueur perdre dès le premier coup.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 */
	public void replaceMine(int row, int column)
	{
		boolean findIt = false;
		int indiceMines = 0;
		int iMine = 0;

		// Trouver l'emplacement de la mine dans le tableau mines aux coordonnées line et column saisies par l'utilisateur.
		while (!findIt && indiceMines < this.nbMines)
		{
			if (this.mines[indiceMines][0] == row && this.mines[indiceMines][1] == column)
			{
				iMine = indiceMines;
				findIt = true;
			}
			indiceMines++;
		}

		boolean replacementDone = false;
		int lineaBoard = 0;
		int columnaBoard = 0;

		// Trouver un nouvel emplacement libre, pour y placer une mine 
		// et effectuer les changements nécessaires dans aBoard et mines à ces nouvelles coordonnées.
		while (!replacementDone && columnaBoard < this.side)
		{
			if (this.aBoard[lineaBoard][columnaBoard] != '*')
			{
				this.aBoard[row][column] = '-';
				this.aBoard[lineaBoard][columnaBoard] = '*';
				this.mines[iMine][0] = lineaBoard;
				this.mines[iMine][1] = columnaBoard;

				replacementDone = true;
			}
			lineaBoard++;
			columnaBoard++;
		}
	}

	/**
	 * Vérifier si les coordonnées d’une cellule provenant d’une saisie utilisateur 
	 * et proposées en entrée de la fonction existent dans le plateau de jeu.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un booléen, celui-ci atteste que les coordonnées de la cellule 
	 * figure dans le plateau de jeu.
	 */
	public boolean checkValidCell(int row, int column) 
	{
		return (row >= 0) && (row < this.side) && (column >= 0) && (column < this.side);
	}

	/**
	 * Vérifier si les coordonnées provenant d’une saisie utilisateur 
	 * proposées en entrée de la méthode présente une mine.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un booléen, celui-ci mentionne si aux coordonnées de la cellule se trouvent une mine.
	 */
	public boolean checkMinedCell(int row, int column)
	{
		return (this.aBoard[row][column] == '*') ? true : false;
	}

	/**
	 * Incrémenter  de 1 la variable count si aux coordonnées d’une cellule proposées en entrée se trouvent une mine.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 * @param count un entier qui sera incrémenté si une cellule est minée.
	 * 
	 * @return un entier correspondant à la valeur de count et incrémenté si la cellule présente une mine.
	 * 
	 */
	public int countMinedCell(int row, int column, int count)
	{
		return count =  checkValidCell(row, column) == true 
				&& checkMinedCell (row, column) == true ? 
						count += 1 : count;
	}

	/**
	 * Comptabiliser le nombre de mines présentes dans les huit cases adjacentes d’une cellule 
	 * dont les coordonnées sont proposées en entrée de la fonction.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un entier représente le nombre de mines situées autour d'une cellule de coordonnées row, column.
	 */
	public int countAdjacentMines(int row, int column)
	{
		int count = 0;

		/* 
		 * Cellule  --> (row, col)
		 * N    	--> (row - 1, col)
		 * S     	--> (row + 1, col)
		 * E    	--> (row, col + 1)
		 * O    	--> (row, col - 1)
		 * N.E  	--> (row - 1, col + 1)
		 * N.O  	--> (row - 1, col - 1)
		 * S.E  	--> (row + 1, col + 1)
		 * S.O  	--> (row + 1, col - 1)
		 */

		count = countMinedCell(row - 1, column, count);
		count = countMinedCell(row + 1, column, count);
		count = countMinedCell(row, column + 1, count);
		count = countMinedCell(row, column - 1, count);
		count = countMinedCell(row - 1, column + 1, count);
		count = countMinedCell(row - 1, column - 1, count);
		count = countMinedCell(row + 1, column + 1, count);
		count = countMinedCell(row + 1, column - 1, count);

		return count;
	}

	/**
	 * Retourner la valeur de la variable side.
	 * 
	 * @return un entier, la valeur de la variable side.
	 */
	public int getSide() 
	{
		return this.side;
	}

	/**
	 * Retourner la valeur de la variable aBoard[coordX][coordY].
	 * 
	 * @param coordX est la coordonnée se reportant à la ligne de la cellule.
	 * @param coordY est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un caractère, la valeur de la variable aBoard[coordX][coordY].
	 */
	public char getaBoard(int coordX, int coordY) 
	{
		return this.aBoard[coordX][coordY];
	}

	/**
	 * Assigner la valeur de arg à la valeur de la variable aBoard[coordX][coordY].
	 * 
	 * @param coordX est la coordonnée se reportant à la ligne de la cellule.
	 * @param coordY est un entier dédié à la colonne de la cellule.
	 * @param arg est un caractère assigné à l'emplacement aBoard[coordX][coordY].
	 */
	public void setaBoard(int coordX, int coordY, char arg) 
	{
		this.aBoard[coordX][coordY] = arg;
	}

	/**
	 * Retourner la valeur de la variable NbMines.
	 * 
	 * @return un entier, la valeur de la variable nbMines.
	 */
	public int getNbMines() 
	{
		return this.nbMines;
	}

	/**
	 * Retourner la valeur de la variable mines[coordX][coordY].
	 * 
	 * @param coordX est la coordonnée se reportant à la ligne de la cellule.
	 * @param coordY est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un entier, la valeur de l'emplacement mémoire mines[coordX][coordY].
	 */
	public int getMines(int coordX, int coordY) 
	{
		return this.mines[coordX][coordY];
	}

	/**
	 * Demander une saisie utilisateur dans le but de choisir un des tests implémentés dans la fonction main.
	 * 
	 * @return un entier correspondant au choix de l'utilisateur.
	 */
	public static int input_Tests()
	{
		int input = 0;
		try 
		{
			System.out.println("Choisir une des options du menu :\n\n"
					+ "0 : Initialiser et afficher un plateau de jeu;\n"
					+ "1 : Initaliser et afficher le plateau de solution du jeu;\n"
					+ "2 : Changer les coordonnées d'une mine, si le joueur tombe\n"
					+ "    sur une cellule minée dès le premier coup;\n"
					+ "3 : Vérifier si une cellule figure dans le plateau de jeu;\n"
					+ "4 : Vérifier si une cellule intègre une mine;\n"
					+ "5 : Retour la valeur 1 si la cellule présente une mine;\n"
					+ "6 : Compter le nombre de mines présentes autour une cellule;\n"
					+ "7 : Quitter le menu.");

			Scanner sc = new Scanner(System.in);
			input = sc.nextInt();
			if (input < 0 || input > 7)
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Saisir un chiffre correspondant à une des options du menu.\n\n");

		}
		catch(MessageException exception)
		{
			System.err.println("\n" + exception.getMessage() + "\n");
			input_Tests();
		}
		catch (InputMismatchException e) {
			System.err.print("La saisie est incorrecte!!\n\n"
					+ "Saisir un chiffre correspondant à une des options du menu.\n\n");
			input_Tests();
		}
		return input;
	}

	/**
	 * Implémentation de différents tests pour vérifier l’efficacité de l’ensemble des méthodes de la classe MinesweeperGameBoard.
	 * 
	 * @param args est un tableau à double dimension de type chaîne de caractères.
	 * @throws MessageException est dédié à la fonction inputTest().
	 */

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);

		RunMinesweeperGame play = new RunMinesweeperGame(sc);
		Player user = new Player();
		MinesweeperGameBoard gameBoard = new MinesweeperGameBoard(0, false);
		MinesweeperGameBoard gameSolution = new MinesweeperGameBoard(0, true);

		int choix = 0;
		do 
		{
			choix = input_Tests();

			// Tests unitaires :
			switch(choix)
			{
			// Initialiser et afficher un plateau de jeu.
			case 0 :
				gameBoard.printBoard();
				System.out.println();
				break;
				// Initialiser et afficher le plateau de solution du jeu.
			case 1 :
				gameSolution.printBoard();
				System.out.println();
				break;
				// Changer les coordonnées d'une mine.
			case 2 :
				gameSolution.printBoard();
				user.makeAMove(gameBoard, sc);
				gameSolution.replaceMine(user.getMoveCellX(), user.getMoveCellY());
				gameSolution.printBoard();
				System.out.println();
				break;
				// Vérifier si une cellule figure dans le plateau de jeu.
			case 3 :
				gameBoard.printBoard();
				user.makeAMove(gameBoard, sc);
				System.out.println("\nRetour de checkValidCell : " + gameBoard.checkValidCell(user.getMoveCellX(), user.getMoveCellY()) + "\n");
				break;
				// Vérifier si une cellule intègre une mine.
			case 4 :
				gameSolution.printBoard();
				user.makeAMove(gameBoard, sc);
				System.out.println("\nRetour de checkMinedcell : " + gameSolution.checkMinedCell(user.getMoveCellX(), user.getMoveCellY()) + "\n");
				System.out.println();
				break;
				// Retour de la valeur 1 si la cellule présente une mine.
			case 5 :
				gameSolution.printBoard();
				user.makeAMove(gameBoard, sc);
				int count = 0;
				count = gameSolution.countMinedCell(user.getMoveCellX(), user.getMoveCellY(), count);
				System.out.println("\nRetour de countMinedCell : " + count + "\n");
				break;
				// Compter le nombre de mines présentes autour une cellule.
			case 6 : 
				gameSolution.printBoard();
				user.makeAMove(gameBoard, sc);
				int count2 = 0;
				count2 = gameSolution.countAdjacentMines(user.getMoveCellX(), user.getMoveCellY());
				System.out.println("\nNombre de mines présentes dans les cases adjacentes : " + count2 + "\n");
				break;
			default :
				break;
			}
		}
		while (choix != 7);

		sc.close();
	}
}
