
package services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Renter;
import model.Room;
import model.TypeSex;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;



@Path("/tenant")
public class RenterService {
    protected EntityManager em;

    public RenterService() {
        em = Persistence.createEntityManagerFactory("Service")
                        .createEntityManager();
    }
    
    
    public RenterService(EntityManager em){
        this.em=em;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postRenter(Renter renter) 
    {
        if (UserNameExistsRenter(renter.getUsername())) return Response.status(Response.Status.CONFLICT).entity("Renter with username: " + renter.getUsername()+" already exists").build();
        Renter newrenter=this.createRenter(renter.getUsername(),  renter.getSex(), renter.getAge(), renter.isSmoker(), renter.isHaspets());
        return Response.status(Response.Status.CREATED).entity(newrenter).build();
    }
    
        public boolean UserNameExistsRenter(String username)
    {
        List<Renter> renters = this.queryAllRenters();
        int i=0;
        while(i<renters.size()&&(!renters.get(i).getUsername().equals(username))){
            i++;
        }
        return i != renters.size();
    }
    
    


     public Renter createRenter(String user,TypeSex sex,int age, boolean smoker,boolean haspets){
        Renter renter = new Renter();
        renter.setUsername(user);
        renter.setSex(sex);
        renter.setAge(age);
        renter.setSmoker(smoker);
        renter.setHaspets(haspets);
        em.getTransaction().begin();
        em.persist(renter);
        em.getTransaction().commit();
        return renter;
    }
    
     
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getjsonAllRenters(){
        GenericEntity<List<Renter>> entity = new GenericEntity<List<Renter>>(queryAllRenters()) {};
         return Response.ok(entity).build();
    }
     
    public List<Renter> queryAllRenters() {
        return (List<Renter>) em.createQuery(
                "SELECT r FROM Renter r").getResultList();
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRenterwithid(@PathParam("id") int id){
        Renter renter = this.queryRenterwithid(id);
        if (renter==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        return Response.ok().entity(renter).build();
    }
    
    public Renter queryRenterwithid(int id){
        String query="SELECT r FROM Renter r WHERE r.id = :id";
        try {
        return (Renter) em.createQuery(query)
                        .setParameter("id", id)
                        .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @PUT    
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putUpdateRenter(@PathParam("id") int id, Renter renter)
    {
        Renter checkusername = UserNameRenter(renter.getUsername());
        if((checkusername!=null)&&(checkusername.getId()!=id)){
            return Response.status(Response.Status.CONFLICT).entity("Renter with username: " + renter.getUsername()+" already exists").build();
        }
        renter=this.updateRenter(id,renter.getUsername(),renter.getSex(),renter.getAge(),renter.isSmoker(),renter.isHaspets());
        if (renter==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        else return Response.ok().entity(renter).build();
    }
    
    public Renter UserNameRenter(String username)
    {
        List<Renter> renters = this.queryAllRenters();
        int i=0;
        while(i<renters.size()&&(!renters.get(i).getUsername().equals(username))){
            i++;
        }
        if (i==renters.size()) return null;
        else return renters.get(i);
    }
            
    public Renter updateRenter(int id,String user,TypeSex sex,int age, boolean smoker,boolean haspets){
        Renter renter=queryRenterwithid(id);
        if(renter!=null){
            em.getTransaction().begin();
            renter.setUsername(user);
            renter.setSex(sex);
            renter.setAge(age);
            renter.setSmoker(smoker);
            renter.setHaspets(haspets);
            em.getTransaction().commit();
            return renter;
        }
        else return null;
        
    }
    
    @DELETE    
    @Path("{id}")
    public Response deleteRenter(@PathParam("id") int id)
    {
        Renter renter =this.deleteRenterDB(id);
        if (renter==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        else return Response.noContent().build();
    }
    
    public Renter deleteRenterDB(int id){
        Renter renter=queryRenterwithid(id);
        if(renter!=null){
            em.getTransaction().begin();
            if(renter.getRoom()!=null ){
                renter.getRoom().setRenter(null);
                renter.setRoom(null);
            }
            em.remove(renter);
            em.getTransaction().commit();
            return renter;
        }
        else return null;
    }
    
    @POST
    @Path("{id}/rent")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postRenterRoom(@PathParam("id") int id, Room room) 
    {
        Renter renter=queryRenterwithid(id);
        //If Renter does not exist
        if (renter==null) return Response.status(Response.Status.NOT_FOUND).entity("Tenant not found for ID: " + id).build();
        Room roomindb = em.find(Room.class, room.getId());
        //If renter exists, room exists but has a renter
        if(renter.getRoom()!=null) return Response.status(Response.Status.CONFLICT).entity("Tenant with ID: " + renter.getId()+" already has a room").build();
        //If renter exists, room exists and has no renter
        if((roomindb!=null)&&(roomindb.getRenter()==null)){
            em.getTransaction().begin();
            renter.setRoom(roomindb);
            roomindb.setRenter(renter);
            em.getTransaction().commit();
        }
        //If room is rented
        else if((roomindb!=null)&&(roomindb.getRenter()!=null)){
            return Response.status(Response.Status.CONFLICT).entity("Room with ID: " + renter.getId()+" already has a renter").build();
        }
        //If renter exists but room doesn't
        else if((roomindb==null)){
            em.getTransaction().begin();
            renter.setRoom(room);
            room.setRenter(renter);
            em.persist(room);
            em.getTransaction().commit();
        }
        return Response.ok().entity(renter).build();
    }
    
    public Renter assignRoomToRenter(int id,Room room)
    {
        Renter renter=queryRenterwithid(id);
        if(renter!=null){
            em.getTransaction().begin();
            renter.setRoom(room);
            room.setRenter(renter);
            em.persist(room);
            em.getTransaction().commit();
            return renter;
        }
        else return null;
    }
    
    
    
    
}
