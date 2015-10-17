package drocck.sp.beesandhoney.web.controllers;

import drocck.sp.beesandhoney.business.entities.Location;
import drocck.sp.beesandhoney.business.services.LocationService;
import drocck.sp.beesandhoney.business.services.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Chai
 *         Created on 10/9/2015.
 */
@Controller
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ModelAttribute("locationService")
    public Location construct() {
        return new Location();
    }

    @RequestMapping(value = "/location/create", method = RequestMethod.GET)
    public String create() {
        return "location/create";
    }

    @RequestMapping(value = "/location/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("location") Location location) {
        locationService.save(location);
        return "redirect:/location/list";
    }

    @RequestMapping(value = "/location/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("location", locationService.findById(id));
        return "location/read";
    }

    @RequestMapping(value = "/location/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("location", locationService.findById(id));
        return "location/update";
    }

    @RequestMapping(value = "/location/update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute("location") Location location) {
        locationService.save(location);
        return "redirect:/location/list";
    }

    @RequestMapping(value = "/location/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("location", locationService.findById(id));
        return "location/delete";
    }

    @RequestMapping("/location/confirmedDelete/{id}")
    public String confirmedDelete(@PathVariable Long id) {
        locationService.delete(id);
        return "redirect:/location/list";
    }

    @RequestMapping("/location/list")
    public String list(Model model) {
        model.addAttribute("location", locationService.findAll());
        return "location/list";
    }
}