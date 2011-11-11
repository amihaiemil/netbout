/**
 * Copyright (c) 2009-2011, netBout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are PROHIBITED without prior written permission from
 * the author. This product may NOT be used anywhere and on any computer
 * except the server platform of netBout Inc. located at www.netbout.com.
 * Federal copyright law prohibits unauthorized reproduction by any means
 * and imposes fines up to $25,000 for violation. If you received
 * this code occasionally and without intent to use it, please report this
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
package com.netbout.hub.data;

import com.netbout.hub.HelpQueue;

/**
 * Bout with data.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class ParticipantData {

    /**
     * The participant.
     */
    private String identity;

    /**
     * Is it confirmed?
     */
    private boolean confirmed;

    /**
     * Set identity.
     * @param idnt The identity
     */
    public void setIdentity(final String idnt) {
        if (this.identity != null) {
            throw new IllegalStateException(
                "setIdentity() can only set identity one time, not change"
            );
        }
        this.identity = idnt;
    }

    /**
     * Get identity.
     * @return The identity
     */
    public String getIdentity() {
        return this.identity;
    }

    /**
     * Set status.
     * @param flag The flag
     */
    public void setConfirmed(final boolean flag) {
        this.confirmed = flag;
        // HelpQueue.exec(
        //     "changed-participant-confirmed-status",
        //     Boolean.class,
        //     HelpQueue.SYNCHRONOUSLY,
        //     this.number,
        //     data.getIdentity()
        // );
    }

    /**
     * Is it confirmed?
     * @return The flag
     */
    public boolean isConfirmed() {
        return this.confirmed;
    }

}
