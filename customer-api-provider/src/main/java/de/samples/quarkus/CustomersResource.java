package de.samples.quarkus;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("/customers")
public class CustomersResource {

  private final Map<UUID, Customer> customers = new HashMap<>();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAllCustomers() {
    return "[]";
  }

  // TODO prüfen, ob notwendig?
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Customer createCustomer(Customer customer) {
    // ???
    return customer;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{uuid}")
  public Customer findCustomerById(@PathParam("uuid") UUID uuid) {
    return customers.get(uuid); // TODO ???
  }

}
