package application.pp_3_1_3_bootstrap.controller;

import application.pp_3_1_3_bootstrap.entity.User;
import application.pp_3_1_3_bootstrap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @ModelAttribute("newUser")
    public User getPerson() {
        return new User();
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String addNewUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allUsers", userService.getAllUsers());
            return "admin";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("admin/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    // Теперь этот метод просто добавляет данные пользователя в модель для модального окна
    @GetMapping("admin/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("editUser", userService.getUser(id)); // Передаем данные пользователя
        return "admin"; // Возвращаем admin для отображения
    }

    @PostMapping("admin/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute("editUser") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin"; // Если есть ошибки, показываем admin снова
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            User existingUser = userService.getUser(id);
            user.setPassword(existingUser.getPassword());
        }
        user.setId(id); // Устанавливаем ID для обновления
        userService.updateUser(user);
        return "redirect:/admin"; // Перенаправление на страницу администрирования
    }

}