/*******************************************************************************
 * Copyright (c) 2009, 2019 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.contentassist.antlr;

import org.eclipse.xtext.ide.editor.contentassist.antlr.BaseFollowElement;

/**
 * Simple bean that reflects the current state, when the content assist parser
 * hit EOF.
 *
 * @author Sebastian Zarnekow - Initial contribution and API
 * @deprecated Use {@link org.eclipse.xtext.ide.editor.contentassist.antlr.FollowElement} instead
 */
@Deprecated
public class FollowElement extends BaseFollowElement<LookAheadTerminal> {
}
