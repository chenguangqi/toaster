/*
 * Copyright © 2015 Copyright(c) Chen Guangqi and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package io.github.chenguangqi.toaster.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.rev170510.DisplayString;
import org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.rev170510.Toaster;
import org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.rev170510.Toaster.ToasterStatus;
import org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.rev170510.ToasterBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class ToasterProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ToasterProvider.class);

    // making this public because this unique ID is required later on in other classes.
    public static final InstanceIdentifier<Toaster> TOASTER_IID =
            InstanceIdentifier.builder(Toaster.class).build();

    private static final DisplayString TOASTER_MANUFACTURER = new DisplayString("OpenDaylight by Chen Guangqi");
    private static final DisplayString TOASTER_MODEL_NUMBER = new DisplayString("Model 111 - Binding Aware");

    private ProviderContext providerContext;
    private DataBroker dataBroker;

    @Override
    public void onSessionInitiated(ProviderContext session) {
        this.providerContext = session;
        this.dataBroker = providerContext.getSALService(DataBroker.class);
        initToasterOperational();
        LOG.info("ToasterProvider Session Initiated");
    }

    private void initToasterOperational() {
        // Build the initial toaster operational data
        Toaster toaster = new ToasterBuilder()
                .setToasterManufacturer(TOASTER_MANUFACTURER)
                .setToasterModelNumber(TOASTER_MODEL_NUMBER)
                .setToasterStatus(ToasterStatus.Up)
                .build();

        // Put the toaster operational data into the MD-SAL data store.
        WriteTransaction tx = dataBroker.newWriteOnlyTransaction();
        tx.put(LogicalDatastoreType.OPERATIONAL, TOASTER_IID, toaster);

        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                LOG.info("initToasterOperational: transaction succeeded");
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOG.error("initToasterOperational: transaction failed");
            }
        });

    }

    @Override
    public void close() throws Exception {
        if (null != dataBroker) {
            WriteTransaction tx = dataBroker.newWriteOnlyTransaction();
            tx.delete(LogicalDatastoreType.OPERATIONAL, TOASTER_IID);
            Futures.addCallback(tx.submit(), new FutureCallback<Void>() {
                @Override
                public void onSuccess(@Nullable Void aVoid) {
                    LOG.info("close: transaction succeeded");
                }

                @Override
                public void onFailure(Throwable throwable) {
                    LOG.error("close: transaction failed");
                }
            });
        }
        LOG.info("ToasterProvider Closed");
    }

}
