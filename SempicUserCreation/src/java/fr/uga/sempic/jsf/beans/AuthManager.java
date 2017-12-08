/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.sempic.jsf.beans;

import fr.uga.miashs.sempic.model.SempicUser;
import fr.uga.miashs.sempic.model.datalayer.SempicUserDao;
import fr.uga.miashs.sempic.util.Pages;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@SessionScoped
public class AuthManager  implements Serializable {

    private String login;
    private String password;
    private SempicUser connectedUser;
    private String requestedPage;
    
    @EJB
    private SempicUserDao dao;

    public AuthManager() {
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public SempicUser getConnectedUser() {
        return connectedUser;
    }
    
    public void setRequestPage(String page) {
        requestedPage =page;
    }
    

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        SempicUser u = dao.getByEmail(login);
        //String requestedPage = null;
        if (u!=null && u.verifyPassword(password)) {
            /*HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute(AuthFilter.AUTH_USER, u);
            requestedPage = (String) session.getAttribute(AuthFilter.REQUESTED_PAGE);
            session.removeAttribute(AuthFilter.REQUESTED_PAGE);*/
            connectedUser=u;
        }
        else {
            context.addMessage(null, new FacesMessage("Login failed."));
            return Pages.login;
        }
        if (requestedPage!=null) { 
            return requestedPage+"?faces-redirect=true";
        }
        return "";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        //connectedUser=null;
        return Pages.login+"?faces-redirect=true";
    }
}
