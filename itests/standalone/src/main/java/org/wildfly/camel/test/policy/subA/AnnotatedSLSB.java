/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.camel.test.policy.subA;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

@Stateless
@DeclareRoles({ "Role1", "Role2", "Role3" })
@LocalBean
@SecurityDomain("other")
public class AnnotatedSLSB {

    @PermitAll
    public String doAnything(String msg) {
        return "Hello " + msg;
    }

    @RolesAllowed({ "Role1" })
    public String doSelected(String msg) {
        return "Hello " + msg;
    }

    @DenyAll
    public void restrictedMethod() {
        throw new RuntimeException("This method was supposed to be restricted to all!");
    }
}
