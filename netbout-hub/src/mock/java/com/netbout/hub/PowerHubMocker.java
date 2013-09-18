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
package com.netbout.hub;

import com.jcabi.urn.URN;
import com.netbout.bus.BusMocker;
import com.netbout.bus.TxBuilder;
import com.netbout.inf.Infinity;
import com.netbout.inf.InfinityMocker;
import com.netbout.spi.Identity;
import com.netbout.spi.IdentityMocker;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mocker of {@link PowerHub}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class PowerHubMocker {

    /**
     * The object.
     */
    private final transient PowerHub hub = Mockito.mock(PowerHub.class);

    /**
     * Mocked bus.
     */
    private final transient BusMocker bmocker = new BusMocker();

    /**
     * Public ctor.
     */
    public PowerHubMocker() {
        Mockito.doAnswer(
            new Answer<TxBuilder>() {
                public TxBuilder answer(final InvocationOnMock invocation) {
                    final String mnemo = (String) invocation.getArguments()[0];
                    return PowerHubMocker.this.bmocker.mock().make(mnemo);
                }
            }
        ).when(this.hub).make(Mockito.anyString());
        Mockito.doAnswer(
            new Answer<Infinity>() {
                public Infinity answer(final InvocationOnMock invocation) {
                    return new InfinityMocker().mock();
                }
            }
        ).when(this.hub).infinity();
        this.withURNResolver(new URNResolverMocker().mock());
        this.withBoutMgr(new BoutMgrMocker().mock());
        try {
            Mockito.doAnswer(
                new Answer<Identity>() {
                    public Identity answer(final InvocationOnMock invocation) {
                        final URN name = (URN) invocation.getArguments()[0];
                        return new IdentityMocker().namedAs(name).mock();
                    }
                }
            ).when(this.hub).identity(Mockito.any(URN.class));
        } catch (Identity.UnreachableURNException ex) {
            throw new IllegalArgumentException(ex);
        }
        Mockito.doAnswer(
            new Answer<Identity>() {
                public Identity answer(final InvocationOnMock invocation) {
                    return (Identity) invocation.getArguments()[0];
                }
            }
        )
            .when(this.hub)
            .join(Mockito.any(Identity.class), Mockito.any(Identity.class));
    }

    /**
     * Expecting this mnemo.
     * @param val The value to return
     * @param mnemo The mnemo name
     * @param args Optional arguments
     * @return This object
     */
    public PowerHubMocker doReturn(final Object val, final String mnemo,
        final Object... args) {
        this.bmocker.doReturn(val, mnemo, args);
        return this;
    }

    /**
     * With this URN resolver.
     * @param resolver The resolver to use
     * @return This object
     */
    public PowerHubMocker withURNResolver(final URNResolver resolver) {
        Mockito.doReturn(resolver).when(this.hub).resolver();
        return this;
    }

    /**
     * With this BoutMgr.
     * @param mgr The manager to use
     * @return This object
     */
    public PowerHubMocker withBoutMgr(final BoutMgr mgr) {
        Mockito.doReturn(mgr).when(this.hub).manager();
        return this;
    }

    /**
     * With this identity on board.
     * @param name The name of it
     * @return This object
     */
    public PowerHubMocker withIdentity(final String name) {
        return this.withIdentity(
            URN.create(name),
            new IdentityMocker().namedAs(name).mock()
        );
    }

    /**
     * With this identity on board.
     * @param name The name of it
     * @param identity The identity
     * @return This object
     */
    public PowerHubMocker withIdentity(final URN name,
        final Identity identity) {
        try {
            Mockito.doReturn(identity).when(this.hub).identity(name);
        } catch (Identity.UnreachableURNException ex) {
            throw new IllegalArgumentException(ex);
        }
        return this;
    }

    /**
     * Build it.
     * @return The bout
     */
    public PowerHub mock() {
        return this.hub;
    }

}