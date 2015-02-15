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

package org.apache.ignite.internal.processors.dataload;

import org.apache.ignite.configuration.*;
import org.apache.ignite.internal.*;
import org.apache.ignite.internal.util.tostring.*;
import org.apache.ignite.internal.util.typedef.internal.*;
import org.apache.ignite.lang.*;
import org.apache.ignite.plugin.extensions.communication.*;
import org.jetbrains.annotations.*;

import java.nio.*;
import java.util.*;

/**
 *
 */
public class GridDataLoadRequest extends MessageAdapter {
    /** */
    private static final long serialVersionUID = 0L;

    /** */
    private long reqId;

    /** */
    private byte[] resTopicBytes;

    /** Cache name. */
    private String cacheName;

    /** */
    private byte[] updaterBytes;

    /** Entries to put. */
    private byte[] colBytes;

    /** {@code True} to ignore deployment ownership. */
    private boolean ignoreDepOwnership;

    /** */
    private boolean skipStore;

    /** */
    private DeploymentMode depMode;

    /** */
    private String sampleClsName;

    /** */
    private String userVer;

    /** Node class loader participants. */
    @GridToStringInclude
    @GridDirectMap(keyType = UUID.class, valueType = IgniteUuid.class)
    private Map<UUID, IgniteUuid> ldrParticipants;

    /** */
    private IgniteUuid clsLdrId;

    /** */
    private boolean forceLocDep;

    /**
     * {@code Externalizable} support.
     */
    public GridDataLoadRequest() {
        // No-op.
    }

    /**
     * @param reqId Request ID.
     * @param resTopicBytes Response topic.
     * @param cacheName Cache name.
     * @param updaterBytes Cache updater.
     * @param colBytes Collection bytes.
     * @param ignoreDepOwnership Ignore ownership.
     * @param skipStore Skip store flag.
     * @param depMode Deployment mode.
     * @param sampleClsName Sample class name.
     * @param userVer User version.
     * @param ldrParticipants Loader participants.
     * @param clsLdrId Class loader ID.
     * @param forceLocDep Force local deployment.
     */
    public GridDataLoadRequest(long reqId,
        byte[] resTopicBytes,
        @Nullable String cacheName,
        byte[] updaterBytes,
        byte[] colBytes,
        boolean ignoreDepOwnership,
        boolean skipStore,
        DeploymentMode depMode,
        String sampleClsName,
        String userVer,
        Map<UUID, IgniteUuid> ldrParticipants,
        IgniteUuid clsLdrId,
        boolean forceLocDep) {
        this.reqId = reqId;
        this.resTopicBytes = resTopicBytes;
        this.cacheName = cacheName;
        this.updaterBytes = updaterBytes;
        this.colBytes = colBytes;
        this.ignoreDepOwnership = ignoreDepOwnership;
        this.skipStore = skipStore;
        this.depMode = depMode;
        this.sampleClsName = sampleClsName;
        this.userVer = userVer;
        this.ldrParticipants = ldrParticipants;
        this.clsLdrId = clsLdrId;
        this.forceLocDep = forceLocDep;
    }

    /**
     * @return Request ID.
     */
    public long requestId() {
        return reqId;
    }

    /**
     * @return Response topic.
     */
    public byte[] responseTopicBytes() {
        return resTopicBytes;
    }

    /**
     * @return Cache name.
     */
    public String cacheName() {
        return cacheName;
    }

    /**
     * @return Updater.
     */
    public byte[] updaterBytes() {
        return updaterBytes;
    }

    /**
     * @return Collection bytes.
     */
    public byte[] collectionBytes() {
        return colBytes;
    }

    /**
     * @return {@code True} to ignore ownership.
     */
    public boolean ignoreDeploymentOwnership() {
        return ignoreDepOwnership;
    }

    /**
     * @return Skip store flag.
     */
    public boolean skipStore() {
        return skipStore;
    }

    /**
     * @return Deployment mode.
     */
    public DeploymentMode deploymentMode() {
        return depMode;
    }

    /**
     * @return Sample class name.
     */
    public String sampleClassName() {
        return sampleClsName;
    }

    /**
     * @return User version.
     */
    public String userVersion() {
        return userVer;
    }

    /**
     * @return Participants.
     */
    public Map<UUID, IgniteUuid> participants() {
        return ldrParticipants;
    }

    /**
     * @return Class loader ID.
     */
    public IgniteUuid classLoaderId() {
        return clsLdrId;
    }

    /**
     * @return {@code True} to force local deployment.
     */
    public boolean forceLocalDeployment() {
        return forceLocDep;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridDataLoadRequest.class, this);
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
                if (!writer.writeField("cacheName", cacheName, MessageFieldType.STRING))
                    return false;

                writer.incrementState();

            case 1:
                if (!writer.writeField("clsLdrId", clsLdrId, MessageFieldType.IGNITE_UUID))
                    return false;

                writer.incrementState();

            case 2:
                if (!writer.writeField("colBytes", colBytes, MessageFieldType.BYTE_ARR))
                    return false;

                writer.incrementState();

            case 3:
                if (!writer.writeField("depMode", depMode != null ? (byte)depMode.ordinal() : -1, MessageFieldType.BYTE))
                    return false;

                writer.incrementState();

            case 4:
                if (!writer.writeField("forceLocDep", forceLocDep, MessageFieldType.BOOLEAN))
                    return false;

                writer.incrementState();

            case 5:
                if (!writer.writeField("ignoreDepOwnership", ignoreDepOwnership, MessageFieldType.BOOLEAN))
                    return false;

                writer.incrementState();

            case 6:
                if (!writer.writeMapField("ldrParticipants", ldrParticipants, MessageFieldType.UUID, MessageFieldType.IGNITE_UUID))
                    return false;

                writer.incrementState();

            case 7:
                if (!writer.writeField("reqId", reqId, MessageFieldType.LONG))
                    return false;

                writer.incrementState();

            case 8:
                if (!writer.writeField("resTopicBytes", resTopicBytes, MessageFieldType.BYTE_ARR))
                    return false;

                writer.incrementState();

            case 9:
                if (!writer.writeField("sampleClsName", sampleClsName, MessageFieldType.STRING))
                    return false;

                writer.incrementState();

            case 10:
                if (!writer.writeField("skipStore", skipStore, MessageFieldType.BOOLEAN))
                    return false;

                writer.incrementState();

            case 11:
                if (!writer.writeField("updaterBytes", updaterBytes, MessageFieldType.BYTE_ARR))
                    return false;

                writer.incrementState();

            case 12:
                if (!writer.writeField("userVer", userVer, MessageFieldType.STRING))
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
                cacheName = reader.readField("cacheName", MessageFieldType.STRING);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 1:
                clsLdrId = reader.readField("clsLdrId", MessageFieldType.IGNITE_UUID);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 2:
                colBytes = reader.readField("colBytes", MessageFieldType.BYTE_ARR);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 3:
                byte depModeOrd;

                depModeOrd = reader.readField("depMode", MessageFieldType.BYTE);

                if (!reader.isLastRead())
                    return false;

                depMode = DeploymentMode.fromOrdinal(depModeOrd);

                readState++;

            case 4:
                forceLocDep = reader.readField("forceLocDep", MessageFieldType.BOOLEAN);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 5:
                ignoreDepOwnership = reader.readField("ignoreDepOwnership", MessageFieldType.BOOLEAN);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 6:
                ldrParticipants = reader.readMapField("ldrParticipants", MessageFieldType.UUID, MessageFieldType.IGNITE_UUID, false);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 7:
                reqId = reader.readField("reqId", MessageFieldType.LONG);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 8:
                resTopicBytes = reader.readField("resTopicBytes", MessageFieldType.BYTE_ARR);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 9:
                sampleClsName = reader.readField("sampleClsName", MessageFieldType.STRING);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 10:
                skipStore = reader.readField("skipStore", MessageFieldType.BOOLEAN);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 11:
                updaterBytes = reader.readField("updaterBytes", MessageFieldType.BYTE_ARR);

                if (!reader.isLastRead())
                    return false;

                readState++;

            case 12:
                userVer = reader.readField("userVer", MessageFieldType.STRING);

                if (!reader.isLastRead())
                    return false;

                readState++;

        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public byte directType() {
        return 62;
    }
}
