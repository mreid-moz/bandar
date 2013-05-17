package com.mozilla.bandar.query.tasks;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.eclipse.jetty.io.WriterOutputStream;

import com.google.common.collect.ImmutableMultimap;
import com.mozilla.bandar.query.resources.CvbResource;
import com.yammer.dropwizard.tasks.Task;

public class CvbRefreshTask extends Task {
    private CvbResource cvbResource;

    protected CvbRefreshTask(String name) {
        super(name);
    }

    public CvbRefreshTask(CvbResource cvbResource) {
        this("refresh-cvb");
        this.cvbResource = cvbResource;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter printWriter) throws Exception {
        OutputStream output = new WriterOutputStream(printWriter);
        if (cvbResource != null) {
            cvbResource.refresh(output);
        }
        output.flush();
        printWriter.println("\n=== Finished updating CVB Cache ===");
    }

}
