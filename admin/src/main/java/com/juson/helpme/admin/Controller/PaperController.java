package com.juson.helpme.admin.Controller;

import com.juson.helpme.admin.Security.CurrentUser;
import com.juson.helpme.admin.Security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cqx on 16/7/24.
 */
@Controller
public class PaperController {

    @RequestMapping(value = "/papers", method = RequestMethod.GET)
    public String getHome(Model model, @CurrentUser CustomUserDetails userDetails) {

        System.out.println("username is "+userDetails.getUsername());
//        model.addAttribute("username",userDetails.getUsername());

        return "home/papers";
    }
}
