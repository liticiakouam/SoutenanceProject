package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.CityService;
import com.liticia.soutenanceApp.service.SpecialityService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private CityService cityService;

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

        Page<User> page = userService.findAll(pageable);
        List<User> users = page.getContent();

        List<City> cityList = cityService.findAll();
        List<Speciality> specialityList = specialityService.findAll();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", users);
        model.addAttribute("cities", cityList);
        model.addAttribute("specialities", specialityList);
        return "users";
    }

    @GetMapping("/user")
    public String getTransactionById(@RequestParam("userId") long userId, Model model) {
        Optional<User> optionalUser = userService.findById(userId);
        model.addAttribute("user", optionalUser.get());

        return "agenda";
    }

    @GetMapping("/user/search")
    public String searchUser (@Param("keyword") String keyword, @Param("city") String city,  @Param("speciality") String speciality,  Model model, RedirectAttributes redirectAttributes) {
        List<User> users = userService.searchUser(city, speciality, keyword);
        int userSize = users.size();
        if (userSize > 0) {
            model.addAttribute("userSearch", users);
            model.addAttribute("userSize", userSize);
        } else {
            redirectAttributes.addFlashAttribute("error", "sorry, there are no user existing with this word : " );
            return "redirect:/users?pageNumber=1";
        }

        return "users";
    }

    @GetMapping("/use")
    public String show(){
        userService.findUsers();
        return "availability";
    }
}
