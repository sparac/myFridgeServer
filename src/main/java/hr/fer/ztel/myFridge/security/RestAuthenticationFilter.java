package hr.fer.ztel.myFridge.security;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.data.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RestAuthenticationFilter implements Filter {

	private static final String HEADER_KEY_AUTHENTICATE = "WWW-Authenticate";
	private static final String HEADER_KEY_AUTHORIZATION = "Authorization";
	private static final String AUTHENTICATION_METHOD = "BASIC";
	private static final String AUTHENTICATION_REALM = "myFridge";

	private static final Set<String> PATH_WHITELIST;

	static {
		Set<String> pathWhitelist = new HashSet<String>();
		pathWhitelist.add("/user/register");

		PATH_WHITELIST = Collections.unmodifiableSet(pathWhitelist);
	}

	private static final Logger log = Logger.getLogger(RestAuthenticationFilter.class.getName());

	private FilterConfig filterConfig = null;
	private AuthenticationService authenticationService = null;

	@Override
	public void init(FilterConfig config) throws ServletException {

		this.filterConfig = config;
		this.authenticationService = new AuthenticationService();

		String contextPath = this.filterConfig.getServletContext().getContextPath();
		String filterName = this.filterConfig.getFilterName();
		log.info("Filter " + filterName + " initialized for context " + contextPath);
	}

	@Override
	public void destroy() {

		String contextPath = this.filterConfig.getServletContext().getContextPath();
		String filterName = this.filterConfig.getFilterName();
		log.info("Filter " + filterName + " destroyed for context " + contextPath);

		this.authenticationService = null;
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("Received unsupported request/response pair: " + request);
		}

		HibernateUtil.closeSession();

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String ipAddress = httpRequest.getRemoteAddr();

		// check whitelist
		String requestPath = httpRequest.getPathInfo();
		if (PATH_WHITELIST.contains(requestPath)) {
			// skip authentication
			log.debug("Received request from " + ipAddress + " for whitelisted resource " + requestPath
					+ ", skipping authentication");

			chain.doFilter(request, response);
			return;
		}

		// process authorization header
		BasicAuthorizationHeader authorizationHeader = null;
		try {
			String headerValue = httpRequest.getHeader(HEADER_KEY_AUTHORIZATION);
			authorizationHeader = BasicAuthorizationHeader.newInstance(headerValue);
		} catch (ParseException e) {
			// invalid or missing authorization header, send HTTP 401
			log.warn("Authentication failed for client @ " + ipAddress + ": " + e.getMessage());

			requestClientAuthentication(httpRequest, httpResponse);
			return;
		}

		// authenticate user
		User authenticatedUser = null;
		try {
			authenticatedUser = authenticationService.getAuthenticatedUser(
					authorizationHeader.getUsername(), authorizationHeader.getPassword());
		} catch (DaoException e) {
			log.error("Authentication failed for client @ " + ipAddress, e);

			httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			log.trace("Returned HTTP 500 to client @ " + ipAddress);
			return;
		}

		if (authenticatedUser == null) {
			// authentication failed, user not found
			requestClientAuthentication(httpRequest, httpResponse);
			return;
		}

		// authentication success
		httpRequest.setAttribute("REMOTE_USER", authenticatedUser.getUsername());
		httpRequest.setAttribute("AUTHENTICATED_USER", authenticatedUser);

		log.debug("Authentication success for client " + authenticatedUser.getUsername() + " @ "
				+ ipAddress);

		// forward request for processing
		chain.doFilter(httpRequest, httpResponse);
	}

	private void requestClientAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String remoteAddress = request.getRemoteAddr();

		response.setHeader(HEADER_KEY_AUTHENTICATE, AUTHENTICATION_METHOD + " realm="
				+ AUTHENTICATION_REALM);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

		log.trace("Returned HTTP 401 to client @ " + remoteAddress);
	}
}
