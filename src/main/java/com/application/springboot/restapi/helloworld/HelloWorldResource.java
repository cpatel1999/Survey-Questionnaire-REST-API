package com.application.springboot.restapi.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
	
	//returns string
	@RequestMapping("/hello-world")
	public String helloWorld(){
		return "Hello World!";
	}
	
	//returns json
	@RequestMapping("/hello-world-bean")
	public HelloWorldBean helloWorldBean(){
		return new HelloWorldBean("Hello World!");
	}
	
	//Path Variable or Path Params
	// /user/charit/todos/1
	@RequestMapping("/hello-world-path-param/{name}")
	public HelloWorldBean helloWorldPathParam(@PathVariable String name){
		return new HelloWorldBean("Hello " + name);
	}
	
	@RequestMapping("/hello-world-path-param/{name}/message/{message}")
	public HelloWorldBean helloWorldMultiplePathParam(@PathVariable String name,
			@PathVariable String message){
		return new HelloWorldBean("Hello " + name + ", " + message);
	}
	
	//Optional path variable
	@RequestMapping({"/hello-world-optional-param", "/hello-world-optional-param/{name}"})
	public HelloWorldBean helloWorldOptionalParam(@PathVariable(required = false) String name){
		return new HelloWorldBean("Hello " + name);
	}
}
