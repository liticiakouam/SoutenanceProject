package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.liticia.soutenanceApp.utils.StartDayOfWeek.getStartOfWeekDay;
import static com.liticia.soutenanceApp.utils.Week.getFullWeek;

@Controller
public class ProfessionnalController {

    @Autowired
    private ProfessionnalService professionnalService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getUsers(
            @RequestParam("pageNumber") int pageNumber,
            Model model
    ) {
        return findPaginated(pageNumber, model);
    }

    private String findPaginated(@RequestParam("pageNumber") int pageNumber,
                                 Model model
    ) {

        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<User> page = professionnalService.findAll(pageable);
        List<User> users = page.getContent();

        List<City> cityList = cityService.findAll();
        List<Speciality> specialityList = specialityService.findAll();
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        model.addAttribute("cities", cityList);
        model.addAttribute("specialities", specialityList);
        return "users";
    }

    @GetMapping("/user")
    public String getProAvailabilities(@RequestParam("userId") long userId, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, Model model) {

        try {
            Optional<User> optionalUser = professionnalService.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException();
            }
            LocalDate date = LocalDate.now();
            LocalDate startDayOfWeek = getStartOfWeekDay(date);
            if (startDate.isBefore(startDayOfWeek)) {
                startDate = startDayOfWeek;
            }

            AvailabilityResponse availabilities = availabilityService.getAvailabilities(startDate, optionalUser.get());
            long[][] availabilityArray = availabilities.getAvailabilities();
            List<LocalDate> fullWeek = getFullWeek(startDate);

            User user = userService.findById().get();
            model.addAttribute("user", optionalUser.get());
            model.addAttribute("userAuth", user);
            model.addAttribute("availabilities", availabilityArray);
            model.addAttribute("fullWeek", fullWeek);
            model.addAttribute("startDate", startDate);
        } catch (UserNotFoundException ex) {
            return "serverError";
        }

        return "agenda";
    }

    @GetMapping("/user/search")
    public String searchUser (@Param("keyword") String keyword, @Param("city") String city,  @Param("speciality") String speciality,  Model model, RedirectAttributes redirectAttributes) {
        List<User> users = professionnalService.searchUser(city, speciality, keyword);
        User user = userService.findById().get();

        int userSize = users.size();
        if (userSize > 0) {
            model.addAttribute("user", user);
            model.addAttribute("userSearch", users);
            model.addAttribute("userSize", userSize);
        } else {
            redirectAttributes.addFlashAttribute("error", "Désolé, aucun utilisateur ne correspond à la recherche effectué" );
            return "redirect:/users?pageNumber=1";
        }

        return "users";
    }


    @GetMapping("/admin/professionals")
    public String getAllProfessionals(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();
        List<User> users = userService.findProfessionals();
        List<Speciality> specialities = specialityService.findAll();
        List<City> cities = cityService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("professional", new ProfessionalCreate());
        model.addAttribute("users", users);
        model.addAttribute("specialities", specialities);
        model.addAttribute("cities", cities);
        model.addAttribute("roleId", roleId);
        return "adminProList";
    }

    @PostMapping("/professional/add")
    public String saveProfessionals(@ModelAttribute("professional")ProfessionalCreate professionalCreate) {
        userService.saveProfessional(professionalCreate);
        return "redirect:/admin/professionals";
    }

}
