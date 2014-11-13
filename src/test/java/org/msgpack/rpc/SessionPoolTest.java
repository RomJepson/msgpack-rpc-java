/*
 * MessagePack-RPC for Java
 *
 * Copyright (C) 2014 ZhangYouyi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.msgpack.rpc;

import org.junit.Test;
import org.msgpack.rpc.loop.EventLoop;

public class SessionPoolTest {
    private static final int PORT = 10086;

    public static interface Protocol {
        String echo(String message);
    }

    public static class ProtocolImpl implements Protocol {
        @Override
        public String echo(String message) {
            return "echoed: " + message;
        }
    }

    @Test
    public void testClient() throws Exception {
        EventLoop loop = EventLoop.defaultEventLoop();
        Client client = new Client("localhost", PORT);
        Protocol protocol = client.proxy(Protocol.class);
        long milliSeconds = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            System.out.println(protocol.echo("testClient" + i));
        }
        System.out.println("duration: " + (System.currentTimeMillis() - milliSeconds));
        client.close();
        loop.shutdown();
    }

    @Test
    public void testSession0() throws Exception {
        EventLoop loop = EventLoop.defaultEventLoop();
        SessionPool pool = new SessionPool(loop);
        Session session = pool.getSession("localhost", PORT);
        Protocol protocol = session.proxy(Protocol.class);
        long milliSeconds = System.currentTimeMillis();
        System.out.println(protocol.echo("testSession"));
        System.out.println("duration: " + (System.currentTimeMillis() - milliSeconds));
        session.closeSession();
        pool.close();
        loop.shutdown();
    }

    @Test
    public void testSession1() throws Exception {
        EventLoop loop = EventLoop.defaultEventLoop();
        SessionPool pool = new SessionPool(loop);
        Session session = pool.getSession("localhost", PORT);
        Protocol protocol = session.proxy(Protocol.class);
        long milliSeconds = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            System.out.println(protocol.echo("testSession" + i));
        }
        System.out.println("duration: " + (System.currentTimeMillis() - milliSeconds));
        session.closeSession();
        pool.close();
        loop.shutdown();
    }

    @Test
    public void testSession2() throws Exception {
        EventLoop loop = EventLoop.defaultEventLoop();
        SessionPool pool = new SessionPool(loop);
        Session session = pool.getSession("localhost", PORT);
        Protocol protocol = session.proxy(Protocol.class);
        long milliSeconds = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            final String message = "testSession" + i;
            Runnable runnable = () -> System.out.println(protocol.echo(message));
            runnable.run();
        }
        System.out.println("duration: " + (System.currentTimeMillis() - milliSeconds));
        session.closeSession();
        pool.close();
        loop.shutdown();
    }

    @Test
    public void testServer() throws Exception {
        EventLoop loop = EventLoop.defaultEventLoop();
        Server server = new Server(loop);
        server.serve(new ProtocolImpl());
        server.listen(PORT);
        loop.join();
    }
}
