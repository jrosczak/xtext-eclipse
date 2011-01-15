/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.impl;

import static org.eclipse.xtext.ui.junit.util.IResourcesSetupUtil.*;

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtext.builder.clustering.CopiedResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.util.PluginProjectFactory;

import com.google.common.collect.Iterables;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug334456Test extends AbstractBuilderTest {

	public void testNoCopiedResourceDescription() throws Exception {
		createPluginProject("foo");
		waitForAutoBuild();
		IResourceDescriptions descriptions = BuilderUtil.getBuilderState();
		assertFalse(Iterables.isEmpty(descriptions.getAllResourceDescriptions()));
		for(IResourceDescription description: descriptions.getAllResourceDescriptions()) {
			if (description instanceof CopiedResourceDescription) {
				fail("Did not expect an instance of copied resource description in builder state");
			}
		}
	}
	
	public void testSameResourceCountForTwoProjects() throws Exception {
		IProject fooProject = createPluginProject("foo");
		waitForAutoBuild();
		IResourceDescriptions descriptions = BuilderUtil.getBuilderState();
		int firstSize = Iterables.size(descriptions.getAllResourceDescriptions());
		IProject barProject = createPluginProject("bar");
		waitForAutoBuild();
		descriptions = BuilderUtil.getBuilderState();
		int secondSize = Iterables.size(descriptions.getAllResourceDescriptions());
		assertEquals(firstSize, secondSize);
		barProject.close(null);
		waitForAutoBuild();
		descriptions = BuilderUtil.getBuilderState();
		int thirdSize = Iterables.size(descriptions.getAllResourceDescriptions());
		assertEquals(firstSize, thirdSize);
		fooProject.close(null);
		waitForAutoBuild();
		descriptions = BuilderUtil.getBuilderState();
		int forthSize = Iterables.size(descriptions.getAllResourceDescriptions());
		// no remaining references to archives - fewer entries in index
		assertTrue(firstSize > forthSize);
	}

	private IProject createPluginProject(String name) throws CoreException {
		PluginProjectFactory projectFactory = getInstance(PluginProjectFactory.class);
		projectFactory.setProjectName(name);
		projectFactory.addFolders(Collections.singletonList("src"));
		projectFactory.addBuilderIds(
			JavaCore.BUILDER_ID, 
			"org.eclipse.pde.ManifestBuilder",
			"org.eclipse.pde.SchemaBuilder",
			XtextProjectHelper.BUILDER_ID);
		projectFactory.addProjectNatures(JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature", XtextProjectHelper.NATURE_ID);
		projectFactory.addRequiredBundles(Collections.singletonList("org.eclipse.xtext"));
		IProject result = projectFactory.createProject(new NullProgressMonitor(), null);
		return result;
	}


}
