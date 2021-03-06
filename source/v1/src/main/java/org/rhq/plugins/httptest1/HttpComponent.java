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

package org.rhq.plugins.httptest1;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;

/**
 * @author hrupp
 *
 */
public class HttpComponent implements ResourceComponent, MeasurementFacet {

    URL url;
    long time;
    String status;

    /* (non-Javadoc)
     * @see org.rhq.core.pluginapi.inventory.ResourceComponent#getAvailability()
     */
    public AvailabilityType getAvailability() {
        if (status == null || status.startsWith("5"))
            return AvailabilityType.DOWN;
        return AvailabilityType.UP;
    }

    /* (non-Javadoc)
     * @see org.rhq.core.pluginapi.inventory.ResourceComponent#start(org.rhq.core.pluginapi.inventory.ResourceContext)
     */
    public void start(ResourceContext context) throws InvalidPluginConfigurationException, Exception {
        url = new URL("http://localhost:7080/");
        status = "200"; // Provide an initial status, so getAvailability() returns up
    }

    /* (non-Javadoc)
     * @see org.rhq.core.pluginapi.inventory.ResourceComponent#stop()
     */
    public void stop() {
        // TODO Auto-generated method stub

    }

    public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {

        getData();
        for (MeasurementScheduleRequest request : metrics) {
            if (request.getName().equals("responseTime")) {
                report.addData(new MeasurementDataNumeric(request, new Double(time)));
            } else if (request.getName().equals("status")) {
                report.addData(new MeasurementDataTrait(request, status));
            }
        }
    }

    private void getData() {

        HttpURLConnection con = null;
        int code = 0;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            long now = System.currentTimeMillis();
            con.connect();
            code = con.getResponseCode();
            long t2 = System.currentTimeMillis();
            time = t2 - now;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (con != null)
            con.disconnect();

        status = String.valueOf(code);
    }

}
