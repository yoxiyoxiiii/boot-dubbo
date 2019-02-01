package com.example.dubbocustomer;

import com.example.dubbointerface.interfaces.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@SpringBootApplication
@ImportResource(locations = "classpath:dubbo-customer.xml")
public class DubboCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboCustomerApplication.class, args);
	}

	@Autowired
	private IRedis iRedis;

	@GetMapping
	public String add(HttpServletRequest httpServletRequest) throws InterruptedException {
		String ipAddress = getIpAddress(httpServletRequest);
		iRedis.add(ipAddress);
		return "sucess";
	}


	public static String getIpAddress(HttpServletRequest request) {
		        String ip = request.getHeader("x-forwarded-for");
		        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			            ip = request.getHeader("Proxy-Client-IP"); }
		         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			             ip = request.getHeader("WL-Proxy-Client-IP");
			         }
		        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			            ip = request.getHeader("HTTP_CLIENT_IP");
			        }
		        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			        }
		        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			             ip = request.getRemoteAddr();
			        }
		         return ip;
		    }

}

