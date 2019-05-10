
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.Language;

/**
 * Home object for domain model class Language.
 * 
 * @see Language
 */
public class LanguageDAO extends JPADAO<Language, Long> {
	public LanguageDAO() {
		super(Language.class);
	}
}