package de.frittenburger.cache.impl;

import java.util.function.Function;

import de.frittenburger.io.bo.HttpRequest;

public class ReqKeyMapperImpl implements Function<HttpRequest, String> {

	@Override
	public String apply(HttpRequest request) {
		return request.getHttpHeaders().getHost()+":"+ request.getUrl();
	}

}
