/**
* OWASP Benchmark Project v1.2
*
* This file is part of the Open Web Application Security Project (OWASP)
* Benchmark Project. For details, please see
* <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
*
* The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free Software Foundation, version 2.
*
* The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* @author Nick Sanidas <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*/
package org.owasp.benchmark.testcode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(value="/weakrand-01/BenchmarkTest00415")
public class BenchmarkTest00415 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
	
		String param = request.getParameter("BenchmarkTest00415");
		if (param == null) param = "";
		
		
		String bar = "";
		if (param != null) {
			java.util.List<String> valuesList = new java.util.ArrayList<String>( );
			valuesList.add("safe");
			valuesList.add( param );
			valuesList.add( "moresafe" );
			
			valuesList.remove(0); // remove the 1st safe value
			
			bar = valuesList.get(0); // get the param value
		}
		
		
		try {
			double rand = java.security.SecureRandom.getInstance("SHA1PRNG").nextDouble();
			
			String rememberMeKey = Double.toString(rand).substring(2); // Trim off the 0. at the front.
			
			String user = "SafeDonna";
			String fullClassName = this.getClass().getName();
			String testCaseNumber = fullClassName.substring(fullClassName.lastIndexOf('.')+1+"BenchmarkTest".length());
			user+= testCaseNumber;
			
			String cookieName = "rememberMe" + testCaseNumber;
			
			boolean foundUser = false;
			javax.servlet.http.Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (int i = 0; !foundUser && i < cookies.length; i++) {
					javax.servlet.http.Cookie cookie = cookies[i];
					if (cookieName.equals(cookie.getName())) {
						if (cookie.getValue().equals(request.getSession().getAttribute(cookieName))) {
							foundUser = true;
						}
					}
				}
			}
			
			if (foundUser) {
				response.getWriter().println(
"Welcome back: " + user + "<br/>"
);
			
			} else {			
				javax.servlet.http.Cookie rememberMe = new javax.servlet.http.Cookie(cookieName, rememberMeKey);
				rememberMe.setSecure(true);
	//			rememberMe.setPath("/benchmark/" + this.getClass().getSimpleName());
				rememberMe.setPath(request.getRequestURI()); // i.e., set path to JUST this servlet 
															 // e.g., /benchmark/sql-01/BenchmarkTest01001
				request.getSession().setAttribute(cookieName, rememberMeKey);
response.addCookie(rememberMe);
response.getWriter().println(
user + " has been remembered with cookie: " + rememberMe.getName() 
						+ " whose value is: " + rememberMe.getValue() + "<br/>"
);
			}
	    } catch (java.security.NoSuchAlgorithmException e) {
			System.out.println("Problem executing SecureRandom.nextDouble() - TestCase");
			throw new ServletException(e);
	    }
		response.getWriter().println(
"Weak Randomness Test java.security.SecureRandom.nextDouble() executed"
);
	}
	
}
