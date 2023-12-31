package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.bean.BaseBean;
import com.rays.pro4.bean.RoleBean;
import com.rays.pro4.bean.UserBean;
import com.rays.pro4.exception.ApplicationException;
import com.rays.pro4.exception.DuplicateRecordException;
import com.rays.pro4.model.UserModel;
import com.rays.pro4.util.DataUtility;
import com.rays.pro4.util.DataValidator;
import com.rays.pro4.util.PropertyReader;
import com.rays.pro4.util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * User registration functionality Controller. Performs operation for User
 * Registration
 * 
 * @author Ramveer Singh
 */
@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseCtl {

	/** The Constant OP_SIGN_UP. */
	public static final String OP_SIGN_UP = "SignUp";

	/** The log. */
	private static Logger log = Logger.getLogger(UserRegistrationCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("UserRegistrationCtl Method validate Started");
		System.out.println("Inside URegCtl Validate Method");

		boolean pass = true;

		String login = request.getParameter("login");
		String dob = request.getParameter("dob");

		if (DataValidator.isNull(request.getParameter("firstName"))) {
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
			System.out.println("check condition if firstname is null and returning pass");
		} else if (!DataValidator.isName(request.getParameter("firstName"))) {
			request.setAttribute("firstName", "First name must contains alphabet only");
			pass = false;
			System.out.println("check condition if firstname is not alphabet and returning pass");
		}

		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
			System.out.println("check condition if lastname is null and returning pass");
		} else if (!DataValidator.isName(request.getParameter("lastName"))) {
			request.setAttribute("lastName", "Last name must contains alphabet only");
			pass = false;
			System.out.println("check condition if lastname is not alphabet and returning pass");
		}
		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
			System.out.println("check condition if loginId is null and returning pass");
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login Id"));
			pass = false;
			System.out.println("check condition if email is not valid and returning pass");
		}

		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Mobile No. contain 10 Digits & Series start with 6-9");
			pass = false;
		}
		if (!OP_UPDATE.equalsIgnoreCase(request.getParameter("operation"))) {

			if (DataValidator.isNull(request.getParameter("password"))) {
				request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
				pass = false;
			} else if (!DataValidator.isPassword(request.getParameter("password"))) {
				request.setAttribute("password",
						"Password must contain 8 letters with alpha-numeric & special Character");
				pass = false;
			}
			 if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
				request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
				pass = false;
			}
			else if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
					&& !"".equals(request.getParameter("confirmPassword"))) {
				request.setAttribute("ConfirmPassword", "Password and Confirm Password Must be Same");
				pass = false;
			}
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}

		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
			pass = false;
		} else if (!DataValidator.isAge(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Minimum Age 18 year"));
			pass = false;
		}

		
		log.debug("UserRegistrationCtl Method validate Ended");

		return pass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("UserRegistrationCtl Method populatebean Started");
		System.out.println("Inside populatebean of URegisCtl");

		UserBean bean = new UserBean();

		bean.setRoleId(RoleBean.STUDENT);

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
//System.out.println("-------------------"+request.getParameter("dob"));

		populateDTO(bean, request);
		log.debug("UserRegistrationCtl Method populatebean Ended");
		return bean;
	}

	/**
	 * Contains Display logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserRegistrationCtl Method doGet Started");
		ServletUtility.forward(getView(), request, response);
		System.out.println("URegisCtl Doget ended by forwarding to view");

	}

	/**
	 * Contains Submit logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserRegistrationCtl Method doPost Started");
		System.out.println("UserRegistrationCtl Method doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));

		// get model object
		UserModel model = new UserModel();
		System.out.println("Get model object");
//        long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			System.out.println("if op SignUp then populateBean called and stored data in bean");
			try {
				long pk = model.registerUser(bean);
				bean.setId(pk);
				System.out.println("getting Pk by calling model.RegisterUser and passing bean");
				// request.getSession().setAttribute("UserBean", bean);
				ServletUtility.setSuccessMessage("User Successfully Register", request);
				ServletUtility.forward(getView(), request, response);
				System.out.println("Set msg and forwaede to view");
				return;

			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login Id Already Exists", request);
				ServletUtility.forward(getView(), request, response);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			System.out.println("if op is reset then redirect to URegisCtl");
		}

		log.debug("UserRegistrationCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.USER_REGISTRATION_VIEW;

	}

}
