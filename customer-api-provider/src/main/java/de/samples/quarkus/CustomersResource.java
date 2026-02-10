package de.samples.quarkus;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource {

  private static final Set<String> VALID_STATES = Set.of("active", "locked", "disabled");
  private final Map<UUID, Customer> customers = new HashMap<>();

  @GET
  public Collection<Customer> getAllCustomers(
    @QueryParam("state")
    String state
  ) {
    if (state != null) {
      if (!VALID_STATES.contains(state)) {
        throw new BadRequestException();
      }
      return customers.values().stream()
        .filter(c -> state.equals(c.getState()))
        .toList();
    }
    return customers.values();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createCustomer(
    Customer customer,
    @Context UriInfo uriInfo
  ) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
    final var location = uriInfo
      .getAbsolutePathBuilder()
      .path("{uuid}")
      .build(customer.getUuid());
    return Response
      .created(location)
      .entity(customer)
      .build();
  }

  @GET
  @Path("/{uuid}")
  public Customer findCustomerById(@PathParam("uuid") UUID uuid) {
    final var customer = customers.get(uuid);
    if (customer == null) {
      throw new NotFoundException();
    }
    return customer;
  }

}
