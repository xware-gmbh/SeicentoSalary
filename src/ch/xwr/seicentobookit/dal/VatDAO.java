
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.Vat;

/**
 * Home object for domain model class Vat.
 * 
 * @see Vat
 */
public class VatDAO extends JPADAO<Vat, Long> {
	public VatDAO() {
		super(Vat.class);
	}
}