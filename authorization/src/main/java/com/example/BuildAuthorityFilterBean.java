package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
public class BuildAuthorityFilterBean extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		SecurityContext context = SecurityContextHolder.getContext();
		if ((context != null) && (context.getAuthentication() != null)
				&& !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if ((authentication != null) && (authentication.getAuthorities().isEmpty())) {
				// Utilisateur utilisateur = (Utilisateur)
				// (authentication.getPrincipal());
				// Set<Affectation> listeAffectation = utilisateur
				// .getAffectations();
				// if (listeAffectation == null || listeAffectation.size() == 0)
				// {
				// throw new ServletException(
				// "Aucune affectation pour l'utilisateur!");
				// } else if (listeAffectation.size() == 1) {
				// Affectation affectation = (Affectation) listeAffectation
				// .toArray()[0];
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
				grantedAuthorities.add(new GrantedAuthority() {

					@Override
					public String getAuthority() {
						// TODO Auto-generated method stub
						return "1440";
					}
				});
				// Set<Action> actions = affectation.getRole().getActions();
				// for (Action action : actions) {
				// grantedAuthorities.add(action);
				// }
				// // utilisateur.setRefUniteOrganisationnelle(affectation
				// // .getRefUnityOrganisationnelle());
				// // utilisateur.setConnectedRole(String.valueOf(affectation
				// // .getRefRole().getId()));
				// // utilisateur.setConnectedRoleLabel(affectation.getRefRole()
				// // .getLibelleRole());
				setNewPermissions(grantedAuthorities, authentication);
				// } else {
				// List<Affectation> affectationList = new
				// ArrayList<Affectation>();
				// for (Affectation affectation : listeAffectation) {
				// affectationList.add(affectation);
				// }
				// request.getSession().setAttribute("affectations",
				// affectationList);
				// }
				// }
			}
		}
		chain.doFilter(request, response);
	}

	private void setNewPermissions(List<GrantedAuthority> grantedAuthorities, Authentication authentication) {
		Authentication newAuthentication = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (authentication != null && grantedAuthorities != null) {
			newAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
					authentication.getCredentials(), grantedAuthorities);
			context.setAuthentication(newAuthentication);
		}
	}

}
