/*
 * #%L
 * Wildfly Camel :: Example :: Camel REST
 * %%
 * Copyright (C) 2013 - 2014 RedHat
 * %%
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
 * #L%
 */
package org.wildfly.camel.test.docker.domain;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.camel.test.common.HttpRequest;
import org.wildfly.camel.test.common.HttpRequest.HttpResponse;
import org.wildfly.camel.test.common.docker.DeployCommand;
import org.wildfly.camel.test.common.docker.DockerCommand.Result;


@RunAsClient
@RunWith(Arquillian.class)
public class DockerDomainTest {

    static String DEPLOYMENT_NAME = "domain-endpoint.war";

    @ContainerResource
    ManagementClient mgmtClient;

    @Test
    public void testEndpoint() throws Exception {
        String mgmtAddress = mgmtClient.getMgmtAddress();
        String host = System.getenv("DOCKER_IP");

        String[] files = new File("target").list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".war");
            }
        });
        Assert.assertNotNull("File found", files);
        Assert.assertEquals("One file found", 1, files.length);

        String runtimeName = "domain-endpoint.war";
        DeployCommand docker = new DeployCommand(runtimeName, new File("target/" + files[0]));

        Result result = docker.connect(mgmtAddress, 9990).exec().printOut(System.out).printErr(System.err);
        Assert.assertEquals("Deploy success", 0, result.exitValue());

        HttpResponse response = HttpRequest.get("http://" + host + ":8181/domain-endpoint")
            .timeout(30, TimeUnit.SECONDS)
            .getResponse();

        String responseBody = response.getBody();
        Assert.assertTrue("Starts with Hello: " + responseBody, responseBody.startsWith("Hello"));
    }
}
