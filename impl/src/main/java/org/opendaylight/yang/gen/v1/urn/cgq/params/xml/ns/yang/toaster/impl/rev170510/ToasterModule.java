package org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.impl.rev170510;

import io.github.chenguangqi.toaster.impl.ToasterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ToasterModule extends org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.impl.rev170510.AbstractToasterModule {
    private static final Logger LOG = LoggerFactory.getLogger(ToasterModule.class);

    public ToasterModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public ToasterModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.cgq.params.xml.ns.yang.toaster.impl.rev170510.ToasterModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        LOG.info("Creating a new Toaster instance");

        final ToasterProvider provider = new ToasterProvider();
        LOG.info("Provider " + provider.toString());
        getBrokerDependency().registerProvider(provider);

        return provider;
    }
}
