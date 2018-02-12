package idv.ron.imagetojson.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
@WebServlet("/DataUploadServlet")
public class DataUploadServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	private ServletContext context;

	@Override
	public void init() throws ServletException {
		context = getServletContext();
	}

	@Override
	public void doPost(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		rq.setCharacterEncoding("utf-8");
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
		String imageBase64 = jsonObject.get("imageBase64").getAsString();
		// java.util.Base64 (Java 8 supports)
		byte[] image = Base64.getMimeDecoder().decode(imageBase64);
		context.setAttribute("image", image);

		rp.setContentType(CONTENT_TYPE);
		PrintWriter out = rp.getWriter();
		out.println(jsonIn);
		System.out.println("output: " + jsonIn);
	}

	@Override
	public void doGet(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		byte[] image = (byte[]) context.getAttribute("image");
		if (image == null) {
			rp.setContentType(CONTENT_TYPE);
			PrintWriter out = rp.getWriter();
			out.println("No data!");
			out.close();
			return;
		}
		rp.setContentType("image/jpeg");
		rp.setContentLength(image.length);
		ServletOutputStream out = rp.getOutputStream();
		out.write(image);
	}

}