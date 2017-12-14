package com.grx.springboot.controller;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grx.springboot.entity.User;

@RestController
@RequestMapping("/userController")
public class UserController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6326927976828453288L;

	@RequestMapping("/{id}")
    public User view(@PathVariable("id") Long id) {
        User user = new User();
        user.setId(id);
        user.setName("view");
        return user;
    }
	
	@RequestMapping("/responseEntity/{id}")
    public ResponseEntity<Object> test(@PathVariable("id") Long id) {
		User user = new User();
        user.setId(id);
        user.setName("test");
		return new ResponseEntity<Object>(user, HttpStatus.OK);
    }
}
