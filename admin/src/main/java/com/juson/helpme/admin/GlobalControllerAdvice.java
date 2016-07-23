package com.juson.helpme.admin;

import com.juson.helpme.admin.Security.CurrentUser;
import com.juson.helpme.admin.Security.CustomUserDetails;
import com.juson.helpme.dao.User;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;

/**
 * Created by cqx on 16/7/23.
 */

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private Datastore datastore;

    @ModelAttribute
    public void modelAddCurUserName(Model model, @CurrentUser CustomUserDetails userDetails){

        if (userDetails != null) {
            User user = datastore.find(User.class).field("id").equal(userDetails.getUser().getId()).get();
            model.addAttribute("username", user.getName());
        }
    }



    @ExceptionHandler(FileNotFoundException.class)
    public ModelAndView myError(Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.setViewName("error");
        return mav;
    }
}
