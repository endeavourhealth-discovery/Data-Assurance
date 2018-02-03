package org.endeavourhealth.dataassurance.endpoints;

import org.endeavourhealth.common.security.SecurityUtils;
import org.endeavourhealth.core.database.dal.DalProvider;
import org.endeavourhealth.core.database.dal.audit.UserAuditDalI;
import org.endeavourhealth.core.database.dal.audit.models.AuditAction;
import org.endeavourhealth.core.database.dal.audit.models.AuditModule;
import org.endeavourhealth.core.xml.QueryDocument.LibraryItem;
import org.endeavourhealth.coreui.endpoints.AbstractEndpoint;
import org.endeavourhealth.dataassurance.logic.ReportLogic;
import org.endeavourhealth.dataassurance.models.FhirConcept;
import org.endeavourhealth.dataassurance.models.Practitioner;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;

/**
 * REST API for reports.  Provides all methods on the path "/report"
 */
@Path("/report")
public final class ReportEndpoint extends AbstractEndpoint {

    private static final UserAuditDalI userAudit = DalProvider.factoryUserAuditDal(AuditModule.EdsPatientExplorerModule.CountReport);
    /**
     * Run a predefined report
     * @param sc                Security context (provided)
     * @param reportUuid          UUID of the report to run
     * @param reportParamsJson  Appropriate report filtering parameters
     * @return  LibraryItem definition of the report that ran
     * @throws Exception
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/runReport")
    public Response runReport(@Context SecurityContext sc, @QueryParam("reportUuid") UUID reportUuid, String reportParamsJson) throws Exception {
        UUID userUuid = SecurityUtils.getCurrentUserId(sc);
        userAudit.save(userUuid, getOrganisationUuidFromToken(sc), AuditAction.Run, "Report",
            "uuid", reportUuid,
            "params", reportParamsJson);

        LibraryItem ret = new ReportLogic().runReport(reportUuid, reportParamsJson, userUuid);

        return Response
            .ok(ret, MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    /**
     * Export a list of NHS Numbers resulting from the most recent run of a report
     * @param sc        Security context (provided)
     * @param uuid  UUID of the report to export
     * @return  Line-break separated list of NHS Numbers
     * @throws Exception
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/exportNHS")
    public Response exportNHSNumbers(@Context SecurityContext sc, @QueryParam("uuid") UUID uuid) throws Exception {
            UUID userUuid = SecurityUtils.getCurrentUserId(sc);
            userAudit.save(userUuid, getOrganisationUuidFromToken(sc), AuditAction.Run, "Export NHS Numbers",
                "uuid", uuid);

            String ret = new ReportLogic().getNhsExport(uuid, userUuid);

            return Response
                .ok(ret, MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    /**
     * Export the data resulting from the most recent run of a report
     * @param sc        Security context (provided)
     * @param uuid  UUID of the report to export
     * @return  CSV export of the report data
     * @throws Exception
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/exportData")
    public Response exportData(@Context SecurityContext sc, @QueryParam("uuid") UUID uuid) throws Exception {
        UUID userUuid = SecurityUtils.getCurrentUserId(sc);
        userAudit.save(userUuid, getOrganisationUuidFromToken(sc), AuditAction.Run, "Export Data",
            "uuid", uuid);
        String ret = new ReportLogic().getDataExport(uuid, userUuid);

        return Response
            .ok(ret, MediaType.TEXT_PLAIN_TYPE)
            .build();
    }

    /**
     * Retrieve a distinct list of encounter types (based on code AND term)
     * @param sc Security context (provided)
     * @return  A JSON array of Concepts
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/encounterType")
    public Response getEncounterTypes(@Context SecurityContext sc) throws Exception {
        userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load, "Encounter Types");
        List<FhirConcept> ret = new ReportLogic().getEncounterTypes();

        return Response
            .ok(ret, MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    /**
     * Retrieve a distinct list of referral types
     * @param sc Security context (provided)
     * @return  A JSON array of Concepts
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/referralTypes")
    public Response getReferralTypes(@Context SecurityContext sc) throws Exception {
        userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load, "Referral Types");
        List<FhirConcept> ret = new ReportLogic().getReferralTypes();

        return Response
            .ok(ret, MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    /**
     * Retrieve a distinct list of referral priorities
     * @param sc Security context (provided)
     * @return  A JSON array of Concepts
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/referralPriorities")
    public Response getReferralPriorities(@Context SecurityContext sc) throws Exception {
        userAudit.save(SecurityUtils.getCurrentUserId(sc), getOrganisationUuidFromToken(sc), AuditAction.Load, "Referral Priorities");
        List<FhirConcept> ret = new ReportLogic().getReferralPriorities();

        return Response
            .ok(ret, MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/searchPractitioner")
    public Response searchPractitioner(
        @Context SecurityContext sc,
        @QueryParam("searchData") String searchData,
        @QueryParam("organisationUuid") UUID organisationUuid) throws Exception {
        userAudit.save(SecurityUtils.getCurrentUserId(sc), organisationUuid, AuditAction.Load, "Practitioner Search");

        List<Practitioner> ret = new ReportLogic().searchPractitioners(searchData, organisationUuid);

        return Response
            .ok(ret, MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

}