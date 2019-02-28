package filter;

import jwt.JwtBuilder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class JwtFilter {
    public void filter(ContainerRequestContext rc) {
        if ( rc.getUriInfo().getPath().contains("jwt") ||
                rc.getMethod().equals("OPTIONS") )
            return;
        JwtBuilder jwtbuilder = new JwtBuilder();
        try {
            String [] auth = rc.getHeaderString("Authorization")
                    .split("\\s");
            String subject = jwtbuilder.checkSubject(auth[1]);
        } catch ( Exception ex ) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}