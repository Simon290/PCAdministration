package com.endress.infoserve.test.pca.view.validation;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.util.validation.IValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.UserQuery;

public class UniqueUsernameValidator implements IValidator {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UniqueUsernameValidator.class);

	ICompound compound;
	String errorMessage;

	/**
	 * create a converter which check for a mandatory field with your customm error message.
	 * 
	 * @param errorMessage - the string should be translated already
	 */
	public UniqueUsernameValidator(String errorMessage, ICompound compound) {
		super();
		this.errorMessage = errorMessage;
		this.compound = compound;
	}

	/**
	 * check if there is an empty entry...
	 */
	public void validate(String value) throws ValidationException {
		if (compound.getMode() == ICompound.MODE_NEW && existsUsername(value)) {
			throw new ValidationException(errorMessage);
		}
	}

	private boolean existsUsername(String userName) {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserName(userName);
		Collection<IUser> allUser = query.execute();
		if (allUser != null && allUser.size() > 0) {
			return true;
		}

		return false;
	}

	public String getTooltip() {
		return null;
	}
}
