//
// MessagePack-RPC for Java
//
// Copyright (C) 2010 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.rpc.loop.netty;

import org.msgpack.MessagePack;
import org.msgpack.rpc.loop.EventLoop;
import org.msgpack.rpc.loop.EventLoopFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class NettyEventLoopFactory implements EventLoopFactory {
    public NettyEventLoopFactory() {
    }

    public EventLoop make(ExecutorService workerExecutor,
            ExecutorService ioExecutor,
            ScheduledExecutorService scheduledExecutor, MessagePack messagePack) {
        return new NettyEventLoop(workerExecutor, ioExecutor,
                scheduledExecutor, messagePack);
    }
}
