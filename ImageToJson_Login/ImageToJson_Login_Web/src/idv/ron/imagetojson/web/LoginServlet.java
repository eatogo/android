package idv.ron.imagetojson.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	public void doPost(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		rq.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = rq.getReader();
		StringBuffer jsonIn = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(),
				JsonObject.class);
		String name = jsonObject.get("name").getAsString();
		String password = jsonObject.get("password").getAsString();
		boolean isUserValid = false;
		if (name != null && password != null && name.equals("a") && password.equals("1")) {
			isUserValid = true;
		}
		jsonObject = new JsonObject();
		jsonObject.addProperty("isUserValid", isUserValid);
		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println(jsonObject.toString());
		System.out.println("output: " + jsonObject.toString());
	}

	@Override
	public void doGet(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		doPost(rq, rp);
	}

}