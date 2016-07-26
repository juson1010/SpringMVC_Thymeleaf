package com.juson.springMVC.Controller;

import com.juson.springMVC.dto.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianliang on 26/6/2016.
 */
@RestController
@RequestMapping(value = "secured")
public class SecuredController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(new Message("hello, you are secured."));
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok(new Message("hello, admin."));
    }


}
