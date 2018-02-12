package idv.ron.texttojson.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@SuppressWarnings("serial")
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	// private ServletContext context;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	private List<User> userList;

	@Override
	public void init() throws ServletException {
		super.init();
		userList = new ArrayList<>();
		userList.add(new User("mary2017", "Mary", "mary2017"));
		userList.add(new User("john2017", "John", "john2017"));
	}

	@Override
	public void doPost(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		rq.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = rq.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		User loginUser = gson.fromJson(jsonIn.toString(), User.class);
		String outStr = "";
		for (User user : userList) {
			if (loginUser.getId().equals(user.getId()) || 
					loginUser.getPassword().equals(user.getPassword())) {
				user.setPassword("");
				outStr = gson.toJson(user);
				break;
			}
		}
		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println(outStr);
		System.out.println("output: " + outStr);
	}

	@Override
	public void doGet(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		Gson gson = new Gson();
		String userListJson = gson.toJson(userList);
		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println("<h3>User List</h3>");
		out.println(userListJson);
	}

}