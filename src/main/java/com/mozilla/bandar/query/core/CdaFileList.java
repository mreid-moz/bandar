package com.mozilla.bandar.query.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.ICdaEnvironment;
import pt.webdetails.cpf.repository.IRepositoryAccess;
import pt.webdetails.cpf.repository.IRepositoryFile;
import pt.webdetails.cpf.repository.IRepositoryFileFilter;

public class CdaFileList {
    private static final String CDA_FILE_EXTENSION = ".cda";
    Logger logger = LoggerFactory.getLogger(CdaFileList.class);

    public List<String> get() {
        ICdaEnvironment environment = CdaEngine.getEnvironment();
        logger.debug("Getting repo files...");
        IRepositoryAccess repo = environment.getRepositoryAccess();
        IRepositoryFile[] repositoryFiles = repo.listRepositoryFiles(new IRepositoryFileFilter(){
            @Override
            public boolean accept(IRepositoryFile file) {
                String fileName = file.getFileName();
                return fileName.endsWith(CDA_FILE_EXTENSION);
            }});
        List<String> files = new ArrayList<String>(repositoryFiles.length);
        for (IRepositoryFile file : repositoryFiles) {
            files.add(file.getFileName().replace(CDA_FILE_EXTENSION, ""));
        }
        return files;
    }
}
