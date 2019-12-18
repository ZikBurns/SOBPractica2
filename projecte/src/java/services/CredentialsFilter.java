package services;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class CredentialsFilter implements ContainerRequestFilter {
    private EntityManager em;
    private CredentialsService ups;
    private static final String X_USER = "username";
    private static final String X_PASSWORD = "password";  

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String user = requestContext.getHeaderString(X_USER);
        String pass = requestContext.getHeaderString(X_PASSWORD);
        UriInfo uri = requestContext.getUriInfo();
        if(uri.getPath().contains("register")) { return;}
        else if(!ups.validCredentials(user,pass)) requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
    }
    
    public CredentialsFilter(EntityManager em){
        this.em=em;
        ups=new CredentialsService (em);
    }
    public CredentialsFilter() {
        em = Persistence.createEntityManagerFactory("Service")
                        .createEntityManager();
        ups=new CredentialsService ();
    }
    
   
    
    
}