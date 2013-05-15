package com.mozilla.bandar.cvb;

import java.util.Map;

import pt.webdetails.cpf.http.ICommonParameterProvider;
import pt.webdetails.cpk.elements.IElement;
import pt.webdetails.cpk.security.IAccessControl;


class DummyAccessControl implements IAccessControl {
    @Override
    public boolean isAllowed(IElement element) {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public void throwAccessDenied(Map<String, ICommonParameterProvider> parameterProviders) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}