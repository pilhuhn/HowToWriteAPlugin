/*
 * RHQ Management Platform
 * Copyright (C) 2005-2008 Red Hat, Inc.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.rhq.plugins.httptest2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ManualAddFacet;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;

/**
 * @author Heiko W. Rupp
 *
 */
public class HttpServiceDiscoveryComponent implements ResourceDiscoveryComponent, ManualAddFacet {

    // No auto-discovery
    public Set<DiscoveredResourceDetails> discoverResources(ResourceDiscoveryContext context)
        throws  Exception {

        return  Collections.emptySet();
    }

    @Override
    public DiscoveredResourceDetails discoverResource(Configuration pluginConfiguration,
                                                      ResourceDiscoveryContext context) throws InvalidPluginConfigurationException {
        ResourceType resourceType = context.getResourceType();
        Configuration childConfig = context.getDefaultPluginConfiguration();
        String key = childConfig.getSimpleValue("url", null);
        if (key == null)
            throw new InvalidPluginConfigurationException("No URL provided");
        String name = key;
        String description = "Http server at " + key;
        DiscoveredResourceDetails detail = new DiscoveredResourceDetails(
            resourceType, key, name, null,
            description, childConfig, null);

        return detail;
    }
}
