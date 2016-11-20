package ru.MeatGames.roguelike.tomb;

/**
 * Класс для создания инвентаря игрока.
 */

public class InvItem{
    /**
	 * @uml.property  name="next"
	 * @uml.associationEnd  readOnly="true"
	 */
    public InvItem next;
    /**
	 * @uml.property  name="nextList"
	 * @uml.associationEnd  readOnly="true"
	 */
    public InvItem nextList;
    /**
	 * @uml.property  name="item"
	 * @uml.associationEnd  readOnly="true"
	 */
    public Item item;
}
