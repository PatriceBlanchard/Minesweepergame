/**
 * Classe dédiée à la gestion des exceptions.
 * @author patrice
 */
public class MessageException extends Exception
{
	/**
	 * Héritage d’un constructeur de la classe Exception.
	 */
	public MessageException()
	{
		super();
	}

	/**
	 * Il s’agit d’un constructeur qui hérite de la classe Exception prenant en paramètre une variable texte de type String.
	 * @param texte est une chaîne de caractère qui sera affichée en retour lors de la détection de l'exception.
	 */
	public MessageException(String texte) 
	{
		super(texte);
	}
}