/**
 * Copyright (c) 2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pmedv.core.ui.transform;

import java.awt.RenderingHints;
import java.util.Map;

import javax.swing.JComponent;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
import org.pbjar.jxlayer.plaf.ext.transform.TransformModel;

/**
 * Some convenience methods to create a populated transforming {@link JXLayer}.
 * 
 * @author Piet Blok
 */
public class TransformUtils {

    public static  JXLayer<JComponent> createTransformJXLayer(
	    JComponent component) {
	return createTransformJXLayer(component, 1.0, null);
    }

    public static  JXLayer<JComponent> createTransformJXLayer(
	    JComponent component, double scale) {
	return createTransformJXLayer(component, scale, null);
    }

    public static  JXLayer<JComponent> createTransformJXLayer(
	    JComponent component, double scale, Map<RenderingHints.Key, Object> hints) {
	DefaultTransformModel model = new DefaultTransformModel();
	model.setScale(scale);
	return createTransformJXLayer(component, model, hints);
    }

    public static  JXLayer<JComponent> createTransformJXLayer(
	    JComponent component, TransformModel model) {
	return createTransformJXLayer(component, model, null);
    }

    public static  JXLayer<JComponent> createTransformJXLayer(
	    JComponent component, TransformModel model,
	    Map<RenderingHints.Key, Object> hints) {
	TransformUI ui = new TransformUI(model);
	ui.setRenderingHints(hints);
	return new JXLayer<JComponent>(component, ui);
    }

    private TransformUtils() {
    }

}
