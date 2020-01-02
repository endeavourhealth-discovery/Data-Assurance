package org.endeavourhealth.dataassurance.endpoints;

import com.codahale.metrics.annotation.Timed;
import org.endeavourhealth.core.database.dal.DalProvider;
import org.endeavourhealth.core.database.dal.audit.UserAuditDalI;
import org.endeavourhealth.core.database.dal.audit.models.AuditModule;
import org.endeavourhealth.core.database.dal.reference.CodingDalI;
import org.endeavourhealth.core.database.dal.reference.models.Concept;
import org.endeavourhealth.coreui.endpoints.AbstractEndpoint;
import org.endeavourhealth.dataassurance.models.CodeSetValue;
import org.endeavourhealth.dataassurance.models.FhirConcept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

@Path("/ekb")
public final class EkbEndpoint extends AbstractEndpoint {
	private static final Logger LOG = LoggerFactory.getLogger(EkbEndpoint.class);
	private static final UserAuditDalI userAudit = DalProvider.factoryUserAuditDal(AuditModule.EdsUiModule.Ekb);
	private static final CodingDalI codingDal = DalProvider.factoryCodingDal();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed(absolute = true, name = "EkbEndpoint.Search")
	@Path("/search/sct")
	public Response search(@Context SecurityContext sc, @QueryParam("term") String term, @QueryParam("maxResultsSize") int maxResultsSize, @QueryParam("start") int start) throws Exception {
		super.setLogbackMarkers(sc);
		// userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load);

		List<Concept> concepts = codingDal.search(term, maxResultsSize, start);
		List<CodeSetValue> ret = concepts.stream().map(CodeSetValue::new).collect(Collectors.toList());

		clearLogbackMarkers();

		return Response
				.ok()
				.entity(ret)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed(absolute = true, name = "EkbEndpoint.Concepts")
	@Path("/concepts/{code}")
	public Response getConcept(@Context SecurityContext sc, @PathParam("code") String code) throws Exception {
		super.setLogbackMarkers(sc);
		// userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load);

		Concept concept = codingDal.getConcept(code);
		FhirConcept ret = new FhirConcept(concept);

		clearLogbackMarkers();

		return Response
				.ok()
				.entity(ret)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed(absolute = true, name = "EkbEndpoint.ChildHierarchy")
	@Path("/hierarchy/{code}/childHierarchy")
	public Response getChildren(@Context SecurityContext sc, @PathParam("code") String code) throws Exception {
		super.setLogbackMarkers(sc);
		// userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load);

		List<Concept> concepts = codingDal.getChildren(code);
		List<CodeSetValue> ret = concepts.stream().map(CodeSetValue::new).collect(Collectors.toList());

		clearLogbackMarkers();

		return Response
				.ok()
				.entity(ret)
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed(absolute = true, name = "EkbEndpoint.ParentHierarchy")
	@Path("/hierarchy/{code}/parentHierarchy")
	public Response getParents(@Context SecurityContext sc, @PathParam("code") String code) throws Exception {
		super.setLogbackMarkers(sc);
		// userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load);

		List<Concept> concepts = codingDal.getParents(code);
		List<CodeSetValue> ret = concepts.stream().map(CodeSetValue::new).collect(Collectors.toList());

		clearLogbackMarkers();

		return Response
				.ok()
				.entity(ret)
				.build();
	}}
