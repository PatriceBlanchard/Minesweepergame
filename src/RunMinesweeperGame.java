import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe dédiée au lancement du jeu.
 * @author patrice
 */
public class RunMinesweeperGame 
{
	// Une variable renseignant le niveau de difficulté du jeu.
	private int difficultyLevel;

	// Un objet de type MinesweeperGame dédié au traitement du jeu.
	private static MinesweeperGame run;

	/**
	 * Constructeur de la classe RunMinesweeper assignant les attributs difficultyLevel et run.
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 */
	public RunMinesweeperGame(Scanner sc)
	{
		this.difficultyLevel = chooseDifficultyLevelOrQuit(sc);

		this.run = new MinesweeperGame(difficultyLevel);
	}

	/**
	 * Demander la difficulté du mode de jeu au joueur.
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur
	 * 
	 * @return un entier indiquant le choix de la difficulté (0,1 ou 2) ou si il faut quitter le jeu (3).
	 */
	public int chooseDifficultyLevelOrQuit(Scanner sc)
	{
		sc = new Scanner(System.in);
		int choix = 0;
		try 
		{
			System.out.println("\t\t\t-Jeu du Démineur-\n\n"
					+ "Règles du jeu :\n\n"
					+ "Dévoiler toutes les cases du plateau de jeu sans tomber sur une mine.\n\n\n"
					+ "En option :\n\n"
					+ "Vous pouvez marquer une case qui selon vous devrait contenir une mine.\n\n\n"
					+ "Choisir le mode de difficulté du jeu désiré :\n\n"
					+ "0 : DEBUTANT\t\t9 * 9 cellules\t\t10 Mines\n"
					+ "1 : INTERMEDIAIRE\t16 * 16 cellules\t40 mines\n"
					+ "2 : AVANCE\t\t24 * 24 cellules\t99 mines\n\n\n"

					+ "Quitter le jeu :\n\n"
					+ "3 : Fin du programme\n\n"
					+ "Saisir une des options du menu : 0, 1, 2, ou 3 :");

			choix = sc.nextInt();

			if (choix == 3)
			{
				System.exit(0);
			}

			if (choix != 0 && choix != 1 && choix != 2)
			{
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Saisir soit 0, soit 1 ou 2 ou 3.\n");
			}
		}
		catch(MessageException exception)
		{
			System.err.println("\n" + exception.getMessage() + "\n");
			chooseDifficultyLevelOrQuit(sc);
		}
		catch (InputMismatchException e) {
			System.err.print("La saisie est incorrecte!!\n\n"
					+ "Saisir un chiffre correspondant à une des options du menu.\n\n");
			chooseDifficultyLevelOrQuit(sc);
		}
		return choix;
	}

	/**
	 * Proposer au joueur à la fin de la partie, si il souhaite rejouer ou quitter le jeu.
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 */
	public void replayOrQuit(Scanner sc)
	{
		try 
		{
			sc = new Scanner(System.in);
			char car = 0;
			System.out.println("\nVoulez-vous commencer une nouvelle partie (O/N) :");
			String input = sc.nextLine();
			car = input.charAt(0);

			if (car != 'N' 
					&& car != 'O' 
					&& car != 'o' 
					&& car != 'n'
					|| input.length() > 1)
				throw new MessageException("La saisie est incorrecte!!\n\n"
						+ "Saisir O ou o pour Oui\nN ou n pour Non.\n");

			if (car == 'O' || car == 'o')
				RunMinesweeperGame.play_Game(sc);
			else
				System.exit(0);
		}
		catch(MessageException exception)
		{
			System.err.println("\n" + exception.getMessage() + "\n");
			replayOrQuit(sc);
		}
	}

	/**
	 * Instanciation de l'objet play de type RunMinesweeper et appeler la méthode nécessaire au lancement du jeu.
	 * 
	 * @param sc correspond à une variable qui va intégrer le flux de saisie du joueur.
	 * @throws MessageException gère les exceptions lors des saisies de l'utilisateur.
	 */
	public static void play_Game(Scanner sc)
	{
		RunMinesweeperGame play = new RunMinesweeperGame(sc);
		run.minesweeperGameProcessing(sc);
		play.replayOrQuit(sc);
	}

	/**
	 * Appeler la méthode de lancement du jeu.
	 * 
	 * @param args est un tableau a double dimension de type chaîne de caractères.
	 * @throws MessageException gère les exceptions lors des saisies utilisateur.
	 */
	public static void main(String[] args) throws MessageException 
	{
		Scanner sc = new Scanner(System.in);

		// Lancement du jeu.
		play_Game(sc);

		sc.close();
	}
}
