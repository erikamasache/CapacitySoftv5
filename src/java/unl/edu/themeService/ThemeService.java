/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.themeService;

/**
 *
 * @author ERIKA
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import unl.edu.jpa.entities.Theme;
 
 
@ManagedBean(name="themeService", eager = true)
@ApplicationScoped
public class ThemeService {
     
    private List<Theme> themes;
     
    @PostConstruct
    public void init() {
        themes = new ArrayList<Theme>();
        
        themes.add(new Theme(0, "Afternoon", "afternoon"));
        themes.add(new Theme(1, "Afterwork", "afterwork"));
        themes.add(new Theme(2, "Aristo", "aristo"));
        themes.add(new Theme(3, "Blitzer", "blitzer"));
        themes.add(new Theme(4, "Bluesky", "bluesky"));
        themes.add(new Theme(5, "Bootstrap", "bootstrap"));
        themes.add(new Theme(6, "Casablanca", "casablanca"));
        themes.add(new Theme(7, "Cupertino", "cupertino"));
        themes.add(new Theme(8, "Delta", "delta"));
        themes.add(new Theme(9, "Excite-Bike", "excite-bike"));
        themes.add(new Theme(10, "Flick", "flick"));
        themes.add(new Theme(11, "Glass-X", "glass-x"));
        themes.add(new Theme(12, "Home", "home"));
        themes.add(new Theme(13, "Hot-Sneaks", "hot-sneaks"));
        themes.add(new Theme(14, "Humanity", "humanity"));
        themes.add(new Theme(15, "Le-Frog", "le-frog"));
        themes.add(new Theme(16, "Midnight", "midnight"));
        themes.add(new Theme(17, "Mint-Choc", "mint-choc"));
        themes.add(new Theme(18, "Overcast", "overcast"));
        themes.add(new Theme(19, "Pepper-Grinder", "pepper-grinder"));
        themes.add(new Theme(20, "Redmond", "redmond"));
        themes.add(new Theme(21, "Rocket", "rocket"));
        themes.add(new Theme(22, "Sam", "sam"));
        themes.add(new Theme(23, "Smoothness", "smoothness"));
        themes.add(new Theme(24, "South-Street", "south-street"));
        themes.add(new Theme(25, "Start", "start"));
        themes.add(new Theme(26, "Sunny", "sunny"));
        themes.add(new Theme(27, "UI-Darkness", "ui-darkness"));
        themes.add(new Theme(28, "UI-Lightness", "ui-lightness"));
        themes.add(new Theme(29, "Vader", "vader"));
        
    }
     
    public List<Theme> getThemes() {
        return themes;
    } 
}
