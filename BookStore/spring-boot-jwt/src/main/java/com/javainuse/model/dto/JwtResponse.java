package com.javainuse.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

	private String jwttoken;
	private String clientId;
	private String rol;

	public JwtResponse(String jwttoken, String clientId, String rol) {
		this.jwttoken = jwttoken;
		this.clientId = clientId;
		this.rol = rol;
	}
}