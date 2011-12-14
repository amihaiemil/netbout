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
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
package com.netbout.rest.rexsl.scripts.catapult

import com.netbout.spi.client.RestSession
import com.netbout.spi.client.RestUriBuilder
import com.netbout.utils.Cipher
import com.rexsl.test.RestTester
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

def home = new URI(System.getProperty('catapult.home'))
def cipher = new Cipher()
def name = 'urn:netbout:jeff'
def jeff = new RestSession(home).authenticate(name, cipher.encrypt(name))
def bout = jeff.start()
bout.rename('Catapult inbox testing')

RestTester.start(RestUriBuilder.from(jeff))
    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
    // @todo #160 This HTML retrieval doesn't work because of a defect in
    //  Tomcat 6 (I think it's a defect). It passes all requests to RestfulServlet,
    //  even those who are for "/xsl/*". That's why there is an endless cycle
    //  which breaks the build. I think that we should switch to Tomcat 7
    //  somehow.
    // .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
    .get()
    .assertStatus(HttpURLConnection.HTTP_OK)
