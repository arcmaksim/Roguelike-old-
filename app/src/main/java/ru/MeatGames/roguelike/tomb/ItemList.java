package ru.MeatGames.roguelike.tomb;

/**
 *  ласс дл€ списка предметов €чейки карты.
 */

public class ItemList{
    /**
	 * @uml.property  name="next"
	 * @uml.associationEnd  readOnly="true"
	 */
    public ItemList next;
    /**
	 * @uml.property  name="item"
	 * @uml.associationEnd  readOnly="true"
	 */
    public Item item;
}
