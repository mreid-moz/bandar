package com.mozilla.bandar.health;

import java.io.File;

import com.yammer.metrics.core.HealthCheck;

public class BaseDirHealthCheck extends HealthCheck {
    private final String basePath;
    
    public BaseDirHealthCheck(String basePath) {
        super("basePath");
        this.basePath = basePath;
    }
    
    @Override
    protected Result check() throws Exception {
        final File file = new File(basePath);
        
        if (file.exists() && file.isDirectory() && file.canRead()) {
            return Result.healthy();
        }
        
        return Result.unhealthy("basePath must be a readable directory");
    }

}
