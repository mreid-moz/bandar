package com.mozilla.bandar.cvb;

import pt.webdetails.cpf.impl.SimpleSessionUtils;
import pt.webdetails.cpf.impl.SimpleUserSession;
import pt.webdetails.cpf.repository.IRepositoryAccess;
import pt.webdetails.cpf.repository.VfsRepositoryAccess;
import pt.webdetails.cpf.session.ISessionUtils;
import pt.webdetails.cpf.utils.IPluginUtils;
import pt.webdetails.cpk.ICpkEnvironment;
import pt.webdetails.cpk.security.IAccessControl;

public class DummyCpkEnvironment implements ICpkEnvironment {
    protected IPluginUtils pluginUtils;
    IRepositoryAccess repoAccess;
    IAccessControl accessControl;

    public DummyCpkEnvironment() {
        this("/tmp/test-resources");
    }

    public DummyCpkEnvironment(String path) {
        String repo = path + "/repo";
        String settings = path + "/settings";
        pluginUtils = new DummyPluginUtils("test plugin", path);
        repoAccess = new VfsRepositoryAccess(repo, settings);
        accessControl = new DummyAccessControl();
    }
    @Override
    public IPluginUtils getPluginUtils() {
        return pluginUtils;
    }

    @Override
    public IRepositoryAccess getRepositoryAccess() {
        return repoAccess;
    }

    @Override
    public IAccessControl getAccessControl() {
        return accessControl;
    }

    @Override
    public String getPluginName() {
        return pluginUtils.getPluginName();
    }

    @Override
    public ISessionUtils getSessionUtils() {
        return new SimpleSessionUtils(new SimpleUserSession("userName", null, true, null), null, null);
    }
}