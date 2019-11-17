package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealRestController mealController;

    @GetMapping("")
    public String getMeals(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        mealController.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/mealForm")
    public String getForm(HttpServletRequest request) {
        String action = request.getParameter("action");
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1333) :
                mealController.get(getId(request));
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/mealForm")
    public String setForm(HttpServletRequest request) {
        Meal meal = new Meal (
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        if(request.getParameter("id").isEmpty()){
            mealController.create(meal);
        }else{
            mealController.update(meal,getId(request));
        }
        return "redirect:/meals";
    }

    @PostMapping("")
    public String getBetween(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }



    private int getId(HttpServletRequest request) {
        return Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
    }
}
