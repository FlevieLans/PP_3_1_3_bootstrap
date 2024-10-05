package application.pp_3_1_3_bootstrap.controller;

import application.pp_3_1_3_bootstrap.entity.User;
import application.pp_3_1_3_bootstrap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @ModelAttribute("newUser")
    public User getPerson() { return new User(); }

    @GetMapping
    public String showAllUsers(Model model){
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @PostMapping
    public String addNewUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allUsers", userService.getAllUsers());
            return "admin";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@ModelAttribute("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@ModelAttribute("id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { return "admin"; }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            User existingUser = userService.getUser(id);
            user.setPassword(existingUser.getPassword());
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

}