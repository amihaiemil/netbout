/**
 * Copyright (c) 2009-2012, Netbout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are PROHIBITED without prior written permission from
 * the author. This product may NOT be used anywhere and on any computer
 * except the server platform of netBout Inc. located at www.netbout.com.
 * Federal copyright law prohibits unauthorized reproduction by any means
 * and imposes fines up to $25,000 for violation. If you received
 * this code accidentally and without intent to use it, please report this
 * incident to the author by email.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.netbout.notifiers.email;

import com.jcabi.urn.URN;
import com.netbout.spi.Identity;
import com.netbout.spi.cpa.Farm;
import com.netbout.spi.cpa.IdentityAware;
import com.netbout.spi.cpa.Operation;
import com.netbout.spi.xml.JaxbPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;

/**
 * Stage farm.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
@Farm
public final class StageFarm implements IdentityAware {

    /**
     * Me.
     */
    private transient Identity identity;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final Identity idnt) {
        this.identity = idnt;
    }

    /**
     * Somebody was just invited to the bout.
     * @param number Bout where it is happening
     * @param who Who was invited
     * @return Allow invitation?
     * @throws Exception If some problem inside
     */
    @Operation("can-be-invited")
    public Boolean canBeInvited(final Long number, final URN who)
        throws Exception {
        Boolean allow = null;
        if (who.equals(this.identity.name())) {
            allow = this.identity.bout(number)
                .participants()
                .contains("urn:facebook:1531296526");
        }
        return allow;
    }

    /**
     * Somebody was just invited to the bout.
     * @param number Bout where it is happening
     * @param who Who was invited
     * @return Confirm participation?
     */
    @Operation("just-invited")
    public Boolean justInvited(final Long number, final URN who) {
        Boolean confirm = null;
        if (who.equals(this.identity.name())) {
            confirm = true;
        }
        if (who.nid().equals(EmailFarm.NID)) {
            confirm = true;
        }
        return confirm;
    }

    /**
     * Does this stage exist in the bout?
     * @param number Bout where it is happening
     * @param stage Name of stage to render
     * @return Does it?
     */
    @Operation("does-stage-exist")
    public Boolean doesStageExist(final Long number, final URN stage) {
        Boolean exists = null;
        if (this.identity.name().equals(stage)) {
            exists = Boolean.TRUE;
        }
        return exists;
    }

    /**
     * Get XML of the stage.
     * @param number Bout where it is happening
     * @param viewer The viewer
     * @param stage Name of stage to render
     * @param place The place in the stage to render
     * @return The XML document
     * @throws Exception If some problem inside
     * @checkstyle ParameterNumber (4 lines)
     */
    @Operation("render-stage-xml")
    public String renderStageXml(final Long number, final URN viewer,
        final URN stage, final String place) throws Exception {
        String xml = null;
        if (this.identity.name().equals(stage)) {
            xml = new JaxbPrinter(new Stage()).print();
        }
        return xml;
    }

    /**
     * Get XSL for the stage.
     * @param number Bout number
     * @param stage Name of the stage
     * @return The XSL source
     * @throws java.io.IOException If some problem inside
     */
    @Operation("render-stage-xsl")
    public String renderStageXsl(final Long number, final URN stage)
        throws java.io.IOException {
        String xsl = null;
        if (this.identity.name().equals(stage)) {
            xsl = IOUtils.toString(
                this.getClass().getResourceAsStream("stage.xsl"),
                CharEncoding.UTF_8
            );
        }
        return xsl;
    }

}