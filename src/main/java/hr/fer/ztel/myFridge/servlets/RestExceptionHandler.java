package hr.fer.ztel.myFridge.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

//TODO:
public class RestExceptionHandler {// implements JerseyExceptionHandler{

	public void handle(Throwable t, HttpServletResponse response) throws IOException {

		if (t instanceof IllegalArgumentException) {
			response.sendError(400);
		} else {
			response.sendError(500);
		}

	}
}
