package com.walmartlabs.concord.server.plugins.noderoster;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2019 Walmart Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.walmartlabs.concord.server.plugins.noderoster.dao.HostsDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.sonatype.siesta.Resource;
import org.sonatype.siesta.ValidationErrorsException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Named
@Singleton
@Path("/api/v1/noderoster/hosts")
@Api(value = "Node Roster Hosts", authorizations = {@Authorization("api_key"), @Authorization("session_key"), @Authorization("ldap")})
public class HostsResource implements Resource {

    private static final String DEFAULT_LIMIT = "30";
    private static final String DEFAULT_OFFSET = "0";

    private final HostsDao hostsDao;

    @Inject
    public HostsResource(HostsDao hostsDao) {
        this.hostsDao = hostsDao;
    }

    @GET
    @Path("/")
    @ApiOperation(value = "List all known hosts", responseContainer = "list", response = HostEntry.class)
    @Produces(MediaType.APPLICATION_JSON)
    public List<HostEntry> list(@ApiParam @QueryParam("host") String host,
                                @ApiParam @QueryParam("artifact") String artifact,
                                @ApiParam @QueryParam("processInstanceId") UUID processInstanceId,
                                @ApiParam @QueryParam("include") Set<HostsDataInclude> includes,
                                @ApiParam @QueryParam("limit") @DefaultValue(DEFAULT_LIMIT) int limit,
                                @ApiParam @QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) int offset) {

        assertLimitAndOffset(limit, offset);

        HostFilter filter = HostFilter.builder()
                .host(host)
                .artifact(artifact)
                .processInstanceId(processInstanceId)
                .build();

        return hostsDao.list(filter, includes, limit, offset);
    }

    @GET
    @Path("/{hostId}")
    @ApiOperation(value = "Get a host")
    @Produces(MediaType.APPLICATION_JSON)
    public HostEntry get(@ApiParam @PathParam("hostId") UUID hostId) {
        return hostsDao.get(hostId);
    }

    private static void assertLimitAndOffset(int limit, int offset) {
        if (limit <= 0) {
            throw new ValidationErrorsException("'limit' must be a positive number");
        }

        if (offset < 0) {
            throw new ValidationErrorsException("'offset' must be equal or more than zero");
        }
    }
}
