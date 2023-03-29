import java.util.Scanner;

/**
 * Classe dédiée au traiement du jeu.
 * @author patrice
 *
 */
public class MinesweeperGame 
{
	// Un objet de type MinesweeperGameBoard dédié au plateau de jeu visible par le joueur.
	private MinesweeperGameBoard gameBoard;

	// Un objet de type MinesweeperGameBoard dédié à un plateau de solution du jeu pour faciliter
	// l'élaboration de tests fonctionnels et éviter des parcours de tableau de coordonnées dénommé mines. 
	private MinesweeperGameBoard gameSolution;

	// un objet de type Player correspondant au joueur.
	private Player user;

	// Un entier relatant le nombre de coup restant (donc de tours de jeu) avant de dévoiler toutes les cases du jeu.
	private int movesLeft;

	/**
	 * Constructeur de la classe MinesweeperGame assignant les attributs gameBoard, gameSolution, user, movesLeft.
	 * @param difficultyLevel est un entier correspondant au niveau de difficulté du jeu choisi par le joueur.
	 */
	public MinesweeperGame(int difficultyLevel)
	{
		this.gameBoard = new MinesweeperGameBoard(difficultyLevel, false);

		this.gameSolution = new MinesweeperGameBoard(difficultyLevel, true);

		this.user = new Player();

		this.movesLeft = this.gameBoard.getSide() * this.gameBoard.getSide() - this.gameBoard.getNbMines();
	}

	/**
	 * Gérer le traitement du jeu suivant les situations :
	 * - Enregistrer dans une cellule le nombre de mines présentes dans les cases adjacentes 
	 *   à cette cellule et déployer ce processus sur l’ensemble du plateau de jeu; <br>
	 * - Dévoiler une mine et perdre la partie;<br>
	 * - Trouver l’emplacement de l’ensemble des mines sans les divulguer et gagner la partie;<br>
	 * - Gérer l’option permettant de marquer et démarquer une cellule possiblement minée.<br>
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 * 
	 * @throws MessageException est dédié à la méthode InputMove().
	 */
	public void minesweeperGameProcessing(Scanner sc)
	{
		boolean gameOver = false;

		int currentMoveIndex = 0;

		while(gameOver == false) 
		{
			//this.gameSolution.printBoard();
			this.gameBoard.printBoard();
			this.user.inputMove(gameBoard, sc);

			// Gestion du marquage d'une cellule.
			if (this.user.getMarkACellOrNot() == 'O' || this.user.getMarkACellOrNot() == 'o')
			{
				if (this.gameBoard.getaBoard(this.user.getMoveCellX(), this.user.getMoveCellY()) == '-' )
					this.gameBoard.setaBoard(this.user.getMoveCellX(), this.user.getMoveCellY(), '#');
				else 
					System.out.println("\nLa cellule a déjà été évaluée");
			}

			if (this.user.getMarkACellOrNot() == 'N' || this.user.getMarkACellOrNot() == 'n')
			{
				if (this.gameBoard.getaBoard(this.user.getMoveCellX(), this.user.getMoveCellY()) == '#')
					this.gameBoard.setaBoard(this.user.getMoveCellX(), this.user.getMoveCellY(), '-');
			}

			// Si le joueur découvre une mine dès le premier coup, la mine est déplacée dans une autre case du plateau.
			if (currentMoveIndex == 0)
			{
				if (this.gameSolution.checkMinedCell(this.user.getMoveCellX(), this.user.getMoveCellY()) == true)
					this.gameSolution.replaceMine(this.user.getMoveCellX(), this.user.getMoveCellY());
			}

			// Traitement du jeu.
			currentMoveIndex ++;

			gameOver = minesweeperGameProcessingUtil(this.user.getMoveCellX(), this.user.getMoveCellY());

			// Si l'ensemble des cases ont été révélé sauf les cellules présentant une mine, la partie est gagnée.
			if ((gameOver == false) && (this.movesLeft == 0))
			{
				// Affichage de l'ensemble des mines dans gameBoard, en recupérant leurs coordonnées dans le tableau mines[][2]
				for (int i = 0; i < this.gameBoard.getNbMines(); i++)
					this.gameBoard.setaBoard(this.gameSolution.getMines(i, 0), this.gameSolution.getMines(i, 1), '*');
				this.gameBoard.printBoard();
				
				System.out.println("\nVous avez gagné !\n");

				gameOver = true;
			}
		}
	}

	/**
	 * Enregistrer dans une cellule le nombre de mines présentes dans les cases adjacentes 
	 * à cette cellule et déployer ce processus sur l’ensemble du plateau de jeu. 
	 * De plus, cette méthode gère la fin de la partie dans le cas où une mine est dévoilée.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 * 
	 * @return un booléen spécifiant si la partie est terminée ou non.
	 */
	public boolean minesweeperGameProcessingUtil(int row, int column)
	{
		// Cas de base de la récursion :
		// Si une cellule a déja été évaluée.
		if (this.gameBoard.getaBoard(row, column) != '-')
			return false;

		// Autre cas de base de la récursion : 
		// Si le joueur révèle une mine, la partie est perdue.

		int indice;

		if (this.gameSolution.getaBoard(row, column) == '*')
		{
			for (indice = 0; indice < this.gameBoard.getNbMines(); indice++)
				this.gameBoard.setaBoard(this.gameSolution.getMines(indice, 0), this.gameSolution.getMines(indice, 1), '*');
			this.gameBoard.printBoard();

			System.out.println("\nVous avez perdu !\n");

			return true;
		}
		// Sinon la cellule de coordonnées row et column présente un '-' et engendre l'évaluation des cases adjacentes à cette cellule.
		else
		{
			int count = this.gameSolution.countAdjacentMines(row, column);
			this.movesLeft--;
			// Afficher l'évaluation la cellule dans le plateau de jeu.
			char arg = (char) (count + '0'); 
			this.gameBoard.setaBoard(row, column, arg);

			// Récursion sur l'ensemble des cases adjacentes.
			if (count == 0)
			{
				recursiveProcessing(row - 1, column); 
				recursiveProcessing(row + 1, column); 
				recursiveProcessing(row, column + 1); 
				recursiveProcessing(row, column - 1); 
				recursiveProcessing(row - 1, column + 1); 
				recursiveProcessing(row - 1, column - 1);
				recursiveProcessing(row + 1, column + 1);
				recursiveProcessing(row + 1, column - 1);
			}
			return false;
		}
	}

	/**
	 * Les conditions de la récursion de la fonction gameProcessingUtil ont été intégrées dans cette fonction de manière à alléger le code.
	 * 
	 * @param row est la coordonnée se reportant à la ligne de la cellule.
	 * @param column est un entier dédié à la colonne de la cellule.
	 */
	public void recursiveProcessing(int row, int column) 
	{
		// Si la cellule fait partie du plateau de jeu.
		if (this.gameSolution.checkValidCell(row, column) == true)
		{
			// Si la cellule ne présente pas de mine.
			if (this.gameSolution.checkMinedCell(row, column) == false)
				// Récursivité.
				minesweeperGameProcessingUtil(row, column);
		}
	}
}
