package egovframework.upa.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import egovframework.upa.restController.web.MainRestController;

@Controller
public class mainController {
	Logger logger = LogManager.getLogger(MainRestController.class);
	
	/*@RequestMapping("/login.do")
	public String login() {
		System.out.println("login called");
		return "upa/com/login";
	}*/
}
