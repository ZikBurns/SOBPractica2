
package services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import model.*;

@Path("/room")
public class RoomService {
    protected EntityManager em;
    
    public RoomService(EntityManager em){
        this.em=em;
    }
    public RoomService() {
        em = Persistence.createEntityManagerFactory("Service")
                        .createEntityManager();
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postRoom(Room room, @Context UriInfo uriInfo) 
    {
        Room newroom=this.createRoomandRequirements(room.getDescription(), room.getAddress(), room.getCity(), room.getDimension(), room.getLocation(), room.isFurniture(),room.getPrice(),room.getReq());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(newroom.getId()));
        return Response.created(builder.build()).entity(newroom).build();
    }
    public Room createRoom(Room room){
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
        return room;
    }
    
    public Room createRoom(String description, String address, String city, 
                            TypeDimension dimension, TypeLocation location, 
                            boolean furniture, double price){
        Room room = new Room();
        room.setDescription(description);
        room.setAddress(address);
        room.setCity(city);
        room.setDimension(dimension);
        room.setLocation(location);
        room.setFurniture(furniture);
        room.setPrice(price);
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
        return room;
    }
    
    public Room createRoomandRequirements(String description, String address, String city, 
                            TypeDimension dimension, TypeLocation location, 
                            boolean furniture, double price,String ownermail,int ownerphone,TypeSex sex,
                            int maxage,int minage,boolean smokers, boolean pets){
        Room room = new Room();
        room.setDescription(description);
        room.setAddress(address);
        room.setCity(city);
        room.setDimension(dimension);
        room.setLocation(location);
        room.setFurniture(furniture);
        room.setPrice(price);
        Requeriments req = new Requeriments();
        req.setSex(sex);
        req.setPets(pets);
        req.setSmokers(smokers);
        req.setMaxage(maxage);
        req.setMinage(minage);
        req.setOwnermail(ownermail);
        req.setOwnerphone(ownerphone);
        room.setReq(req);
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
        return room;
    }
    public Room createRoomandRequirements(String description, String address, String city, 
                            TypeDimension dimension, TypeLocation location, 
                            boolean furniture, double price,Requeriments req){
        Room room = new Room();
        room.setDescription(description);
        room.setAddress(address);
        room.setCity(city);
        room.setDimension(dimension);
        room.setLocation(location);
        room.setFurniture(furniture);
        room.setPrice(price);
        room.setReq(req);
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
        return room;
    }
    
    

    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRoomwithid(@PathParam("id") int id){
        Room room = this.queryRoomwithid(id);
        if (room==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        return Response.ok().entity(room).build();
    }
    
    public Room queryRoomwithid(int id){
        String query="SELECT r FROM Room r WHERE r.id = :id";
        try {
        return (Room) em.createQuery(query)
                        .setParameter("id", id)
                        .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @DELETE    
    @Path("{id}")
    public Response deleteRoom(@PathParam("id") int id)
    {
        Room room =this.deleteRoomDB(id);
        if (room==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        else return Response.noContent().build();
    }
    
    public Room deleteRoomDB(int id){
        Room room=queryRoomwithid(id);
        if(room!=null){
            em.getTransaction().begin();
            if(room.getRenter()!=null )
            {
                room.getRenter().setRoom(null);
                room.setRenter(null);
            }
            em.remove(room);
            em.getTransaction().commit();
            return room;
        }
        else return null;
    }
    
    @PUT    
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putUpdateRoom(@PathParam("id") int id, Room room)
    {
        room=this.updateRoom(id,room.getDescription(), room.getAddress(), room.getCity(), room.getDimension(), room.getLocation(), room.isFurniture(),room.getPrice(),room.getReq());
        if (room==null) return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
        else return Response.ok().entity(room).build();
    }
    
    public Room updateRoom(int id,String description, String address, String city, 
                            TypeDimension dimension, TypeLocation location, 
                            boolean furniture, double price,Requeriments req){
        Room room=queryRoomwithid(id);
        if(room!=null){
            em.getTransaction().begin();
            room.setDescription(description);
            room.setAddress(address);
            room.setCity(city);
            room.setDimension(dimension);
            room.setLocation(location);
            room.setFurniture(furniture);
            room.setPrice(price);
            room.setReq(req);
            em.getTransaction().commit();
            return room;
        }
        else return null;
        
    }
    
    @GET
    @Path("location=${city}&sort=${criterion}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOrdered(@PathParam("city") String city,@PathParam("criterion") String criterion){
        List<Room> list = null;
        if ( !criterion.equals("asc") && !criterion.equals("desc")) return Response.status(Response.Status.NOT_FOUND).entity("criterion must be <asc> or <desc>").build();
        
        list= queryRoomsfromCity(city,criterion);
        if(list.isEmpty()) return Response.noContent().build();
        GenericEntity<List<Room>> entity= new GenericEntity<List<Room>>(list){};
        return Response.ok(entity).build();
    }
    @GET
    @Path("sort=${criterion}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOrdered(@PathParam("criterion") String criterion){
        List<Room> list = null;
        if ( !criterion.equals("asc") && !criterion.equals("desc")) return Response.status(Response.Status.NOT_FOUND).entity("criterion must be <asc> or <desc>").build();
        
        list = queryAllRooms(criterion);
        if(list.isEmpty()) return Response.noContent().build();
        GenericEntity<List<Room>> entity= new GenericEntity<List<Room>>(list){};
        return Response.ok(entity).build();
    }
    
    public List<Room> queryAllRooms(String criterion) {
                 return (List<Room>) em.createQuery("SELECT r FROM Room r ORDER BY r.price "+criterion,Room.class).getResultList();

    }
    
    public List<Room> queryRoomsfromCity(String city,String criterion){
        
         return (List<Room>) em.createQuery("SELECT r FROM Room r "+" WHERE r.city = '" + city + "'"+" ORDER BY r.price "+criterion,Room.class).getResultList();
    }
        
    
    
    
}
