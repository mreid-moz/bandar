package com.mozilla.bandar.query.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.ICdaEnvironment;
import pt.webdetails.cpf.repository.IRepositoryAccess;
import pt.webdetails.cpf.repository.IRepositoryFile;

public class CdaFileList {
    Logger logger = LoggerFactory.getLogger(CdaFileList.class);

    public List<String> get() {
        ICdaEnvironment environment = CdaEngine.getEnvironment();
        logger.error("Getting repo files...");
        IRepositoryAccess repo = environment.getRepositoryAccess();
        IRepositoryFile[] repositoryFiles = repo.listRepositoryFiles();
        List<String> files = new ArrayList<String>(repositoryFiles.length);
        for (IRepositoryFile file : repositoryFiles) {
            files.add(file.getFileName().replace(".cda", ""));
        }
        return files;
    }
}
