package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rays.pro4.bean.BaseBean;
import com.rays.pro4.bean.RoleBean;
import com.rays.pro4.bean.UserBean;
import com.rays.pro4.exception.ApplicationException;
import com.rays.pro4.model.RoleModel;
import com.rays.pro4.model.UserModel;
import com.rays.pro4.util.DataUtility;
import com.rays.pro4.util.DataValidator;
import com.rays.pro4.util.PropertyReader;
import com.rays.pro4.util.ServletUtility;

/**
 * Servlet implementation class LoginCtl
 * 
 * @author Ramveer Singh
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	public static final String OP_REGISTER = "Register";
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";
	public static final String OP_LOG_OUT = "logout";

	private static Logger log = Logger.getLogger(LoginCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("Inside loginctl  validate");
		log.debug("LoginCtl Method validate Started");

		boolean pass = true;
		String op = request.getParameter("operation");
		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			System.out.println("checking op singhup or logout and returning pass");
			return pass;
		}
		if (DataValidator.isNull(request.getParameter("login"))) {
			System.out.println("loginctl condition if login is null");
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("login"))) {
			System.out.println("loginctl condition if email not valid");
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login Id"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			System.out.println("loginctl condition if password is null");
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		System.out.println("loginctl validate ended");
		log.debug("LoginCtl Method validate Ended");

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("LoginCtl Method populatebean Started");
		//System.out.println("inside loginctl Populatebean");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));// get kiya loginctl
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		log.debug("LoginCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Display Login form
	 *
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Inside Loginctl Do get");
		HttpSession session = request.getSession(false);
		String op = DataUtility.getString(request.getParameter("operation"));

		if (OP_LOG_OUT.equals(op) && !OP_SIGN_IN.equals(op)) {
			session.invalidate();
			ServletUtility.setSuccessMessage("You Have Logged Out Succesfully", request);
			ServletUtility.forward(getView(), request, response);
			System.out.println("Doget condition check if op logout and Not signin then invalidate session");
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
		System.out.println("LoginCtl doget over and forwarded to view");
	}

	/**
	 * Submitting or login action performing
	 *
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("inside loginctl dopost");
		HttpSession session = request.getSession(true);
		log.debug(" Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		UserModel model = new UserModel();
		RoleModel role = new RoleModel();
		System.out.println("get session, get operation, user model, role model object");

		// long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			System.out.println("Dopost condition check if op signin then populateBean");
			try {
				bean = model.authenticate(bean.getLogin(), bean.getPassword());
				String uri = request.getParameter("URI");
				System.out.println("model.authenticate method called stored in bean");
				if (bean != null) {
					session.setAttribute("user", bean);
					long rollId = bean.getRoleId();
					RoleBean rolebean = role.findByPK(rollId);
					if (rolebean != null) {
						session.setAttribute("role", rolebean.getName());
						System.out.println("if bean notEqual to null, role.findByPk, session.setAttribute Role");
					}
					if ("null".equalsIgnoreCase(uri)) {
						ServletUtility.forward(ORSView.WELCOME_VIEW, request, response);
						return;
					} else {
						ServletUtility.redirect(uri, request, response);
						return;
					}
				} else {
					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId Or Password", request);
					System.out.println("PopulateBean, setBean, setErrorMsg");
					System.out.println("Ended doPost of LoginCtl");
				}
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} /*
			 * else if (OP_LOG_OUT.equals(op)) { System.out.println(" Lctl Do post 44");
			 * 
			 * session = request.getSession(); session.invalidate();
			 * 
			 * ServletUtility.redirect(ORSView.LOGIN_CTL, request, response);
			 * 
			 * return;
			 * 
			 * }
			 */ else if (OP_SIGN_UP.equalsIgnoreCase(op))
		{
			System.out.println("Dopost if op is SignUp then redicret to UserRegistration");
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		System.out.println("forwarded to view");
		log.debug("UserCtl Method doPost Ended");
	}
	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}