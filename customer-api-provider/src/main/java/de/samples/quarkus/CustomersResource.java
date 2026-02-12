package de.samples.quarkus;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CustomersResource {

  private final CustomersService customersService;

  @GET
  public Collection<Customer> getAllCustomers(
    @QueryParam("state")
    @ValidState
    String state
  ) {
    if (state != null) {
      return customersService
        .getCustomersByState(state)
        .toList();
    }
    return customersService
      .getAllCustomers()
      .toList();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createCustomer(
    @Valid
    Customer customer,
    @Context UriInfo uriInfo
  ) {
    customersService.createCustomer(customer);
    final var location = uriInfo
      .getAbsolutePathBuilder()
      .path("{uuid}")
      .build(customer.getUuid());
    return Response
      .created(location)
      .entity(customer)
      .build();
  }

  @PATCH
  @Path("/{uuid}")
  @Consumes("application/merge-patch+json")
  public void patchCustomer(
    @PathParam("uuid")
    UUID uuid,
    @Valid
    CustomerPatch patch
  ) {
    final var customer = customersService
      .getCustomerById(uuid)
      .orElseThrow(NotFoundException::new);
    customer.setState(patch.getState());
    customersService.updateCustomer(customer);
  }

  @GET
  @Path("/{uuid}")
  public Customer findCustomerById(@PathParam("uuid") UUID uuid) {
    return customersService
      .getCustomerById(uuid)
      .orElseThrow(NotFoundException::new);
  }

}
