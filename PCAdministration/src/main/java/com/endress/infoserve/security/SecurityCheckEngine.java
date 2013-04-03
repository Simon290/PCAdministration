/**
 * 
 */
package com.endress.infoserve.security;

import org.apache.commons.lang.StringUtils;

import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IRole;
import com.endress.infoserve.test.pca.model.IUser;

/**
 * Checks the roles and permissions for a given user within the application context
 */
public class SecurityCheckEngine {

	public static final String APP_CONTEXT_MENUBAR = "menubar";
	public static final String APP_CONTEXT_PCLIST = "pclist";
	public static final String APP_CONTEXT_BUTTONBAR = "buttonbar";

	/**
	 * 
	 */
	public SecurityCheckEngine() {
	}

	/**
	 * 1. Identify which security constraints apply to the given application context
	 * 2. check whether user meets security constraints (e.g. roles and resource permissions) by comparing the resources and permissions
	 * which the user has with the resources and permissions required in the applicaiton context.
	 * 
	 * e.g. has user "test-user" with roles "read-only user" the permission "UPDATE" in the application context "list all items"
	 * 
	 * @param applicationContext logical and unique name of a part of the application
	 * @param checkUser user for which the permissions should be checked
	 * @param requestedPermission permission for the action the user needs to perform within the application context
	 * @return true if the user has the required permission false otherwise
	 */
	public boolean hasPermission(final String applicationContext, final IUser checkUser, final IPermission requestedPermission) {
		boolean isAccessAllowed = false;
		String requiredRolename = null;

		// 1. get required security constraints for the given application context
		if (StringUtils.equals(applicationContext, APP_CONTEXT_MENUBAR)) {
			requiredRolename = IRole.ROLENAME_ADMIN;
		}

		// 2. compare security constraints with those which the user has
		if (StringUtils.isNotEmpty(requiredRolename)) {
			isAccessAllowed = checkUser.hasRole(requiredRolename);
		}
		return isAccessAllowed;
	}

	/**
	 * 1. Identify which security constraints apply to the given application context
	 * 2. check whether user meets security constraints (e.g. roles and resource permissions) by comparing the resources and permissions
	 * which the user has with the resources and permissions required in the applicaiton context.
	 * 
	 * e.g. has user "test-user" with roles "read-only user" the permission "UPDATE" in the application context "list all items"
	 * 
	 * @param applicationContext logical and unique name of a part of the application
	 * @param checkUser user for which the permissions should be checked
	 * @param requestedPermission permission for the action the user needs to perform within the application context
	 * @param checkResource resource within the application context for which the permission of the user should be checked
	 * @return true if the user has the required permission false otherwise
	 */
	public boolean hasPermission(final String applicationContext, final IUser checkUser, final IPermission requestedPermission,
			final IResource checkResource) {
		boolean isAccessAllowed = false;

		if (StringUtils.equals(applicationContext, APP_CONTEXT_PCLIST)) {
			isAccessAllowed = checkUser.hasResourcePermission(checkResource, requestedPermission)
					|| checkUser.hasRole(IRole.ROLENAME_ADMIN);
		}
		else if (StringUtils.equals(applicationContext, APP_CONTEXT_BUTTONBAR)) {
			isAccessAllowed = checkUser.hasResourcePermission(checkResource, requestedPermission);
		}

		return isAccessAllowed;
	}
}
