/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.managers.communication;

import org.apache.ignite.internal.*;
import org.apache.ignite.internal.util.tostring.*;
import org.apache.ignite.internal.util.typedef.internal.*;
import org.apache.ignite.plugin.extensions.communication.*;

import java.io.*;
import java.nio.*;

/**
 * Wrapper for all grid messages.
 */
public class GridIoMessage extends MessageAdapter {
    /** */
    private static final long serialVersionUID = 0L;

    /** Policy. */
    private GridIoPolicy plc;

    /** Message topic. */
    @GridToStringInclude
    @GridDirectTransient
    private Object topic;

    /** Topic bytes. */
    private byte[] topicBytes;

    /** Topic ordinal. */
    private int topicOrd = -1;

    /** Message ordered flag. */
    private boolean ordered;

    /** Message timeout. */
    private long timeout;

    /** Whether message can be skipped on timeout. */
    private boolean skipOnTimeout;

    /** Message. */
    private MessageAdapter msg;

    /**
     * No-op constructor to support {@link Externalizable} interface.
     * This constructor is not meant to be used for other purposes.
     */
    public GridIoMessage() {
        // No-op.
    }

    /**
     * @param plc Policy.
     * @param topic Communication topic.
     * @param topicOrd Topic ordinal value.
     * @param msg Message.
     * @param ordered Message ordered flag.
     * @param timeout Timeout.
     * @param skipOnTimeout Whether message can be skipped on timeout.
     */
    public GridIoMessage(
        GridIoPolicy plc,
        Object topic,
        int topicOrd,
        MessageAdapter msg,
        boolean ordered,
        long timeout,
        boolean skipOnTimeout
    ) {
        assert plc != null;
        assert topic != null;
        assert topicOrd <= Byte.MAX_VALUE;
        assert msg != null;

        this.plc = plc;
        this.msg = msg;
        this.topic = topic;
        this.topicOrd = topicOrd;
        this.ordered = ordered;
        this.timeout = timeout;
        this.skipOnTimeout = skipOnTimeout;
    }

    /**
     * @return Policy.
     */
    GridIoPolicy policy() {
        return plc;
    }

    /**
     * @return Topic.
     */
    Object topic() {
        return topic;
    }

    /**
     * @param topic Topic.
     */
    void topic(Object topic) {
        this.topic = topic;
    }

    /**
     * @return Topic bytes.
     */
    byte[] topicBytes() {
        return topicBytes;
    }

    /**
     * @param topicBytes Topic bytes.
     */
    void topicBytes(byte[] topicBytes) {
        this.topicBytes = topicBytes;
    }

    /**
     * @return Topic ordinal.
     */
    int topicOrdinal() {
        return topicOrd;
    }

    /**
     * @return Message.
     */
    public Object message() {
        return msg;
    }

    /**
     * @return Message timeout.
     */
    public long timeout() {
        return timeout;
    }

    /**
     * @return Whether message can be skipped on timeout.
     */
    public boolean skipOnTimeout() {
        return skipOnTimeout;
    }

    /**
     * @return {@code True} if message is ordered, {@code false} otherwise.
     */
    boolean isOrdered() {
        return ordered;
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object obj) {
        throw new AssertionError();
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        throw new AssertionError();
    }

    /** {@inheritDoc} */
    @Override public boolean writeTo(ByteBuffer buf, MessageWriter writer) {
        writer.setBuffer(buf);

        if (!writer.isTypeWritten()) {
            if (!writer.writeMessageType(directType()))
                return false;

            writer.onTypeWritten();
        }

        switch (writer.state()) {
            case 0:
                if (!writer.writeField("msg", msg, MessageFieldType.MSG))
                    return false;

                writer.incrementState();

            case 1:
                if (!writer.writeField("ordered", ordered, MessageFieldType.BOOLEAN))
                    return false;

                writer.incrementState();

            case 2:
                if (!writer.writeField("plc", plc != null ? (byte)plc.ordinal() : -1, MessageFieldType.BYTE))
                    return false;

                writer.incrementState();

            case 3:
                if (!writer.writeField("skipOnTimeout", skipOnTimeout, MessageFieldType.BOOLEAN))
                    return false;

                writer.incrementState();

            case 4:
                if (!writer.writeField("timeout", timeout, MessageFieldType.LONG))
                    return false;

                writer.incrementState();

            case 5:
                if (!writer.writeField("topicBytes", topicBytes, MessageFieldType.BYTE_ARR))
                    return false;

                writer.incrementState();

            case 6:
                if (!writer.writeField("topicOrd", topicOrd, MessageFieldType.INT))
                    return false;

                writer.incrementState();

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public boolean readFrom(ByteBuffer buf) {
        reader.setBuffer(buf);

        switch (readState) {
            case 0:
                msg = reader.readField("msg", MessageFieldType.MSG);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 1:
                ordered = reader.readField("ordered", MessageFieldType.BOOLEAN);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 2:
                byte plcOrd;

                plcOrd = reader.readField("plc", MessageFieldType.BYTE);

                if (!reader.isLastRead())
                    return false;

                plc = GridIoPolicy.fromOrdinal(plcOrd);

                readState++;

            case 3:
                skipOnTimeout = reader.readField("skipOnTimeout", MessageFieldType.BOOLEAN);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 4:
                timeout = reader.readField("timeout", MessageFieldType.LONG);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 5:
                topicBytes = reader.readField("topicBytes", MessageFieldType.BYTE_ARR);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 6:
                topicOrd = reader.readField("topicOrd", MessageFieldType.INT);

                if (!reader.isLastRead())
                    return false;

                readState++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return 8;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridIoMessage.class, this);
    }
}
