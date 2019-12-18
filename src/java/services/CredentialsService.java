
package services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Credentials;

@Path("/register")
public class CredentialsService {
    private EntityManager em;
    
    public CredentialsService(EntityManager em){
        this.em=em;
    }
    public CredentialsService() {
        em = Persistence.createEntityManagerFactory("Service")
                        .createEntityManager();
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postUserRegister(Credentials userpass)
    {
        if (UserExists(userpass.getUsername())) return Response.status(Response.Status.CONFLICT).entity("User with username: " + userpass.getUsername()+" already exists").build();
        userpass = createCredentialsWithCripto(userpass.getUsername(),userpass.getPassword());
        return Response.status(Response.Status.CREATED).build();
    }
    
    public Credentials createCredentials(String user, String pass){
        Credentials userpass = new Credentials(user,pass);
        em.getTransaction().begin();
        em.persist(userpass);
        em.getTransaction().commit();
        return userpass;
    }
    public Credentials createCredentialsWithCripto(String user, String pass){
        Credentials userpass = new Credentials(user,Credentials.sha256(pass));
        em.getTransaction().begin();
        em.persist(userpass);
        em.getTransaction().commit();
        return userpass;
    }
    
    public Credentials queryCredentialswithid(String user){
        String query="SELECT u FROM Credentials u WHERE u.username = :user";
        try {
        return (Credentials) em.createQuery(query)
                        .setParameter("user", user)
                        .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<Credentials> queryAllCredentials() {
        return (List<Credentials>) em.createQuery(
                "SELECT u FROM Credentials u").getResultList();
    }
    
    public boolean UserExists(String user)
    {
        List<Credentials> list=this.queryAllCredentials();
        int i=0;
        Credentials userpass=list.get(i);
        while(i<list.size() && (!userpass.getUsername().equals(user)))
        {
            userpass=list.get(i);
            i++;
        }
        return userpass.getUsername().equals(user);
    }
    
    public boolean validCredentials(String user,String pass){
        List<Credentials> list=this.queryAllCredentials();
        int i=0;
        Credentials userpass=list.get(i);
        while(i<list.size() && (!userpass.getUsername().equals(user)))
        {
            userpass=list.get(i);
            i++;
        }
        if(!userpass.getUsername().equals(user)) return false;
        return userpass.getPassword().equals(Credentials.sha256(pass));
    }
    
    
    
}