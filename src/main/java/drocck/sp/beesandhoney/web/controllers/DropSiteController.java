package drocck.sp.beesandhoney.web.controllers;

import drocck.sp.beesandhoney.business.entities.DropSite;
import drocck.sp.beesandhoney.business.entities.User;
import drocck.sp.beesandhoney.business.entities.Yard;
import drocck.sp.beesandhoney.business.services.DropSiteService;
import drocck.sp.beesandhoney.business.services.UserService;
import drocck.sp.beesandhoney.business.services.YardService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by David on 10/10/2015.
 */
@Controller
@RequestMapping("dropsite")
public class DropSiteController {
    private static final Log logger = LogFactory.getLog(DropSiteController.class);

    @Autowired
    private DropSiteService dropSiteService;

    @Autowired
    private YardService yardService;

    @Autowired
    private UserService userService;

    /**
     * Models
     **/
    @ModelAttribute("allDropSites")
    public List<DropSite> populateDrops() {
        return dropSiteService.findAll();
    }

    @ModelAttribute("dropSite")
    public DropSite createModel() {
        return new DropSite();
    }

    @ModelAttribute("allUsers")
    public Collection<User> createUserList() {
        return userService.findAll();
    }


    @ModelAttribute("allYards")
    public List<Yard> createYardList() {
        return yardService.findAll();
    }

    /**
     * Request Mapping
     **/
    @RequestMapping({"/list"})
    public String list() {
        return "dropsite/list";
    }

    @RequestMapping("/list/{yardId}")
    public String listByYard(@PathVariable Long yardId, Model model) {
        Yard yardDisplayed = yardService.findOne(yardId);
        model.addAttribute("yard", yardDisplayed);
        if (yardDisplayed != null)
            model.addAttribute("allDropSites", dropSiteService.findAllByDropYard(yardDisplayed));
        else
            model.addAttribute("allDropSites", dropSiteService.findAll());
        return "dropsite/list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(Model model, @PathVariable Long id) {
        model.addAttribute("dropSite", dropSiteService.findOne(id));
        return "dropsite/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(DropSite dropSite, @RequestParam(value = "principalUser", required = true) String name) {
        Optional<User> u = userService.getUserByUsername(name); // extract user
        dropSite.setDropUser(userService.findOne(u.get().getId()));  // set dropsite by finding user by id
        dropSiteService.save(dropSite);
        long yardId = dropSite.getDropYard().getId();
        return "redirect:list/" + yardId;
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public String read(Model model, @PathVariable Long id) {
        model.addAttribute("dropSite", dropSiteService.findOne(id));
        return "dropsite/read";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable Long id) {
        model.addAttribute("dropSite", dropSiteService.findOne(id));
        return "dropsite/delete";
    }

    @RequestMapping(value = "/confirmedDelete/{id}", method = RequestMethod.GET)
    public String confirmedDelete(@PathVariable Long id) {
        ArrayList<DropSite> toBeDeleted = new ArrayList<>();
        DropSite drop = dropSiteService.findOne(id);
        long yardId = drop.getDropYard().getId();
        toBeDeleted.add(drop);
        dropSiteService.deleteInBatch(toBeDeleted);
        return "redirect:list/" +yardId;
    }

    @RequestMapping(value = "/create")
    public String create() {
        return "dropsite/create";
    }

    @RequestMapping(value = "/create/{yardId}", method = RequestMethod.GET)
    public String create(Model model, @PathVariable("yardId") Long yardID) {
        model.addAttribute("yard", yardService.findOne(yardID));
        return "dropsite/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(DropSite dropSite,
                         @RequestParam(value = "principalUser", required = true) String name,
                         @RequestParam(value = "yardid", required = true) long yardId) {
        Optional<User> u = userService.getUserByUsername(name); // extract user
        dropSite.setDropUser(userService.findOne(u.get().getId()));  // set dropsite by finding user by id
        dropSite.setDropYard(yardService.findOne(yardId));
        dropSiteService.save(dropSite); // insert into db
        return "redirect:list/" + yardId;
    }
}
