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
package com.netbout.inf.predicates;

import com.netbout.inf.Atom;
import com.netbout.inf.Index;
import com.netbout.inf.Meta;
import com.netbout.inf.PredicateException;
import com.netbout.spi.Message;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Allows messages with unique value of parameter.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
@Meta(name = "unique", extracts = true)
public final class UniquePred extends AbstractVarargPred {

    /**
     * Cached bouts and their messages.
     */
    private static final ConcurrentMap<Long, Long> BOUTS =
        new ConcurrentHashMap<Long, Long>();

    /**
     * List of already passed bout numbers.
     */
    private final transient Set<Long> passed = new HashSet<Long>();

    /**
     * Public ctor.
     * @param args The arguments
     * @param index The index to use for searching
     */
    public UniquePred(final List<Atom> args, final Index index) {
        super(args, index);
        if (!"bout.number".equals(this.arg(0).value())) {
            throw new PredicateException(
                "Only $bout.number can be used in (unique)"
            );
        }
    }

    /**
     * Extracts necessary data from message.
     * @param msg The message to extract from
     * @param dest The index to extract to
     */
    public static void extract(final Message msg, final Index dest) {
        UniquePred.BOUTS.put(msg.number(), msg.bout().number());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long next() {
        throw new PredicateException("UNIQUE#next()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        throw new PredicateException("UNIQUE#hasNext()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final Long message) {
        final Long bout = this.BOUTS.get(message);
        boolean allow;
        if (this.passed.contains(bout)) {
            allow = false;
        } else {
            this.passed.add(bout);
            allow = true;
        }
        return allow;
    }

}