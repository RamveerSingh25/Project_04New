package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.bean.BaseBean;
import com.rays.pro4.bean.UserBean;
import com.rays.pro4.exception.ApplicationException;
import com.rays.pro4.exception.RecordNotFoundException;
import com.rays.pro4.model.UserModel;
import com.rays.pro4.util.DataUtility;
import com.rays.pro4.util.DataValidator;
import com.rays.pro4.util.PropertyReader;
import com.rays.pro4.util.ServletUtility;


//TODO: Auto-generated Javadoc
/**
* Forget Password functionality Controller. Performs operation for Forget
* Password
* 
*  @author Ramveer Singh
*/
@WebServlet(name="ForgetPasswordCtl",urlPatterns={"/ForgetPasswordCtl"})
public class ForgetPasswordCtl extends BaseCtl{


    /** The log. */
    private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

    /* (non-Javadoc)
     * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("ForgetPasswordCtl Method validate Started");
        System.out.println("inside validate of FPassCtl");
        boolean pass = true;

        String login = request.getParameter("login");

        if (DataValidator.isNull(login)) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
        } else if (!DataValidator.isEmail(login)) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login Id"));
            pass = false;
        }
        log.debug("ForgetPasswordCtl Method validate Ended");

        return pass;
    }

    /* (non-Javadoc)
     * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("ForgetPasswordCtl Method populatebean Started");
        System.out.println("inside populatebean of fpassctl");
        UserBean bean = new UserBean();

        bean.setLogin(DataUtility.getString(request.getParameter("login")));

        log.debug("ForgetPasswordCtl Method populatebean Ended");

        return bean;
    }
    
    /**
     * Contains Display logics.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("ForgetPasswordCtl Method doGet Started");

        ServletUtility.forward(getView(), request, response);
        System.out.println("inside doget of FPassCtl");
    }
    
    /**
     * Contains Submit logics.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        log.debug("ForgetPasswordCtl Method doPost Started");
        System.out.println("inside doPost of FPassCtl");
        String op = DataUtility.getString(request.getParameter("operation"));
        UserBean bean = (UserBean) populateBean(request);

// get model
        UserModel model = new UserModel();

        if (OP_GO.equalsIgnoreCase(op)) {
            try {
            	System.out.println("email login " + bean.getLogin());
                model.forgetPassword(bean.getLogin());
                ServletUtility.setSuccessMessage("Password has been sent to your email id.", request);
            } catch (RecordNotFoundException e) {
            	System.out.println("inside record not found");
                ServletUtility.setErrorMessage(e.getMessage(), request);
                log.error(e);
            } catch (ApplicationException e) {
            	e.printStackTrace();
            	log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
            else if (OP_RESET.equalsIgnoreCase(op)) {
            	
            	ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
            	return;
			}
            ServletUtility.forward(getView(), request, response);
            

        log.debug("ForgetPasswordCtl Method doPost Ended");
    }

    /* (non-Javadoc)
     * @see in.co.rays.ors.controller.BaseCtl#getView()
     */
    @Override
    protected String getView(){
        return ORSView.FORGET_PASSWORD_VIEW;
    }

	
}
