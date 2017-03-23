package demo;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

public class CustomRemoteServices extends RemoteTokenServices {

	private String accessToken;

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		this.accessToken = accessToken;
		return super.loadAuthentication(accessToken);
	}

	public String getAccessToken() {
		return accessToken;
	}

}
