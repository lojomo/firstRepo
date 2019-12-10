package com.myeclipseide.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.spi.resource.Singleton;

@Produces("application/xml")
@Path("customers")
@Singleton
public class CustomersResource {

	@GET
	public List<Customer> getCustomers() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@GET
	@Path("${id}")
	public java.util.List<Customer> getCustomers(@PathParam("id") int cId) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@POST
	@Path("add")
	@Produces("text/html")
	@Consumes("application/xml")
	public String addCustomer(Customer customer) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
}