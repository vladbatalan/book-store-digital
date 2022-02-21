package com.javainuse.controller;

import java.util.Objects;

import com.javainuse.model.dto.UserFromUserDetails;
import com.javainuse.model.erd.User;
import com.javainuse.service.BlackListService;
import com.javainuse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import com.javainuse.config.security.JwtTokenUtil;
import com.javainuse.model.dto.JwtRequest;
import com.javainuse.model.dto.JwtResponse;
import com.javainuse.model.dto.UserNoPass;


import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin
@RequestMapping("/api/identity-provider")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		User fromDb = userService.getUserByUsername(authenticationRequest.getUsername());

		final UserDetails userDetails =
				new UserFromUserDetails(fromDb);

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ok(new JwtResponse(token, fromDb.getClientId(), fromDb.getRol()));
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logOut(@RequestHeader("Authorization") String token)
	{
        String token_nr = token.split(" ", 0)[1];
        if (blackListService.save(token_nr))
            return ok().build();
        else
            return ResponseEntity.badRequest().build();
   }

	@RequestMapping(value = "/validate-token", method = RequestMethod.POST)
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token)
	{
		String token_nr = token.split(" ", 0)[1];
		String username = jwtTokenUtil.getUsernameFromToken(token_nr);
		User user = userService.getUserByUsername(username);

		// User could not be found
		if(user == null){
			return status(HttpStatus.NOT_FOUND).build();
		}

		try {
			boolean valid = jwtTokenUtil.validateToken(token_nr, new UserFromUserDetails(user));

			// Return value
			if (!valid)
				return status(HttpStatus.NOT_FOUND).build();
			return ok(new UserNoPass(user));
		}
		catch (RuntimeException e){
			return status(HttpStatus.NOT_FOUND).build();
		}

	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
