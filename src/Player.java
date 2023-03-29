import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe dédiée aux différentes saisies proposées à un joueur dans le cadre du jeu.
 * @author patrice
 */
public class Player 
{
	// markACellOrNot est une variable de type char qui prendra la valeur O ou N ou n ou o.
	private static char markACellOrNot;

	// MoveCellX intégrera la coordonnée x d’une cellule choisie par l’utilisateur.
	private static int moveCellX;

	// MoveCellY correspond à la coordonnée y d’une cellule choisie par le joueur.
	private static int moveCellY;

	/**
	 *  La constructeur de la classe Player, assignant les attributs markACellOrNot, moveCellX, moveCellY.
	 */
	public Player()
	{
		this.markACellOrNot = 0;

		this.moveCellX = 0;

		this.moveCellY = 0;
	}

	/**
	 * Saisir par l’utilisateur les coordonnées x et y d’une cellule à évaluer par le traitement du jeu.
	 * 
	 * @param object un objet de type MinesweeperGameBoard autrement dit un plateau de jeu.
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 * 
	 * @throws MessageException gère les différents cas d'exceptions lors de la saisie de l'utilisateur.
	 */
	public void makeAMove(MinesweeperGameBoard object, Scanner sc)
	{
		try
		{
			System.out.println("Saisir la case désirée :\n\n"
					+ "Exemple : a b\n"
					+ "(pour la case la ligne a et la colonne b)");

			String str = sc.nextLine();

			if (!str.matches("[a-z] [a-z]"))
			{
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Elle doit ressembler à : a b\n"
						+ "avec a précisant la ligne et b indiquant la colonne.\n"
						+ "Seules les valeurs entre a et " + (char) (97 + object.getSide() - 1)
						+" peuvent être saisies.");
			}

			String[] parts = str.split(" ");
			String input1 = parts[0];
			String input2 = parts[1];

			int nb1 = parts[0].charAt(0);
			int nb2 = parts[1].charAt(0);

			this.moveCellX = nb1 - 97 ;
			this.moveCellY = nb2 - 97;

			if (!str.matches("[a-z] [a-z]")
					|| nb1 < 97 
					|| nb1 > (97 + object.getSide())
					|| nb2 < 97
					|| nb2 > (97 + object.getSide()))
			{
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Elle doit ressembler à : a b\n"
						+ "avec a précisant la ligne et b indiquant la colonne.\n"
						+ "Seules les valeurs entre a et " + (char) (97 + object.getSide() - 1)
						+" peuvent être saisies.");
			}
		}
		catch(MessageException exception)
		{
			System.err.println("\n" + exception.getMessage() + "\n");
			makeAMove(object, sc);
		}
	}

	/**
	 * Proposer à l’utilisateur la possibilité de marquer une cellule ou non.
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 */
	public void markACell(Scanner sc)
	{
		try 
		{
			System.out.println("\nMarquer la case ? O/N");
			String input = sc.nextLine();
			this.markACellOrNot = input.charAt(0);

			if (this.markACellOrNot != 'N' 
					&& this.markACellOrNot != 'O' 
					&& this.markACellOrNot != 'o' 
					&& this.markACellOrNot != 'n'
					|| input.length() > 1)
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Saisir O ou o pour Oui\nN ou n pour Non.\n");
		}
		catch(MessageException exception)
		{
			System.err.println("\n" + exception.getMessage() + "\n");
			markACell(sc);
		}
	}

	/**
	 * Appeler les fonctions MarkACel() et MakeAMove().
	 * 
	 * @param object un objet de type MinesweeperGameBoard autrement dit un plateau de jeu.
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 * 
	 * @throws MessageException est dédié aux méthodes markACell() et makeAMove().
	 */
	public void inputMove(MinesweeperGameBoard object, Scanner sc)
	{
		// Effacer le scanner
		sc = new Scanner(System.in);

		markACell(sc);
		makeAMove(object, sc);
	}

	/**
	 * Retourner la valeur de la variable markACellOrNot.
	 * 
	 * @return un caractère, la valeur de la variable markACellOrNot.
	 */
	public char getMarkACellOrNot() 
	{
		return this.markACellOrNot;
	}

	/**
	 * Retourner la valeur de la variable moveCellX.
	 * 
	 * @return un entier, la valeur de la variable moveCellX.
	 */
	public int getMoveCellX() 
	{
		return this.moveCellX;
	}

	/**
	 * Retourner la valeur de la variable moveCellY.
	 * 
	 * @return une entier, la valeur de la variable moveCellY.
	 */
	public int getMoveCellY() 
	{
		return this.moveCellY;
	}


	/**
	 * Développer quelques tests pour vérifier le bon fonctionnement 
	 * des méthodes de la classe Player.
	 * 
	 * @param args est un tableau à double dimension de type chaîne de caractères.
	 * @throws MessageException est dédié aux méthodes markACell() et makeAMove().
	 */
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		Player user = new Player();
		MinesweeperGameBoard gameBoard = new MinesweeperGameBoard(0, false);
		
		// Tester le marquage d'une cellule.
		user.markACell(sc);
		System.out.println("Valeur de retour : " + user.markACellOrNot + "\n");

		// Tester la saisie d'une coordonnée x et d'une coordonnée y.
		user.makeAMove(gameBoard, sc);
		System.out.println("\nCoordonnées x : " + moveCellX + "\n" + "Coordonnées y : " + moveCellY);
	}
}
