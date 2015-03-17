/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.loginReg;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import unl.edu.jpa.entities.Theme;
import unl.edu.themeService.ThemeService;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class TemaBean implements Serializable{

    private String option;   
    private Theme theme; 
    private List<Theme> themes;
     
    @ManagedProperty("#{themeService}")
    private ThemeService service;
     
    @PostConstruct
    public void init() {
        themes = service.getThemes();
        theme =  themes.get(themes.size()-22);
    }
    
    public void establecerTema(){
        theme = this.theme;
    }
 
    public String getOption() {
        return option;
    }
 
    public void setOption(String option) {
        this.option = option;
    }
 
    public Theme getTheme() {
        return theme;
    }
 
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
 
    public List<Theme> getThemes() {
        return themes;
    }
 
    public void setService(ThemeService service) {
        this.service = service;
    }

    
}
