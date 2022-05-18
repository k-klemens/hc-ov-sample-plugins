# hc-ov-sample-plugins

This repository contains a sample implementations of a `IContextProviderPlugin`, `IVerificationTaskPlugin` and `ICrowdsourcingConnectorPlugin`
for [hc-ov-core](https://github.com/k-klemens/hc-ov-core). (for more
information about the interfaces see [hc-ov-sdk](https://github.com/k-klemens/hc-ov-sdk)).

The plugins are mainly used for demonstration and testing purposes and provide following functionality:

* **MovieContextProvider**: Provides a sample context extraction for a given ontological class by using it's name.
* **MovieVerificationTaskPlugin**: Extracts all subclasses of the class `http://xmlns.com/foaf/0.1/Person`, provides a simple template and provides an exemplary
  implementation of the template variable resolving.
* **CrowdsourcingConnectorStubPlugin**: Mocks a connection to a crowdsourcing platform for demonstration and testing purposes.

