package org.jenkinsci.plugins.buildtagfixer;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.model.AbstractBuild;
import hudson.model.listeners.RunListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A BuildWrapper that fixes the ${BUILD_TAG} variable to be hierarchical aware.
 * 
 * <p>
 * This allows versions prior to 1.456 to have a correct ${BUILD_TAG} - 1.456
 * has pull <a href="https://github.com/jenkinsci/jenkins/pull/402">402</a>
 * merged in.
 * </p>
 * 
 * @author James Nord
 */
@Extension
public class BuildTagFixer extends RunListener<AbstractBuild> {

	private final String BUILD_TAG_ENV = "BUILD_TAG";
	
	@Override
	public Environment setUpEnvironment(AbstractBuild build, Launcher launcher, BuildListener listener)
	      throws IOException, InterruptedException {
		String jobName = build.getProject().getFullName();
		jobName = jobName.replaceAll("/", "-");
		
		String buildtag = "jenkins-"+jobName+'-'+build.getNumber();
		Map<String, String> envMap = new HashMap<String, String>(2);
		envMap.put(BUILD_TAG_ENV, buildtag);
		EnvVars buildTagVars = new EnvVars(envMap);
		return Environment.create(buildTagVars);
	}
	
}
