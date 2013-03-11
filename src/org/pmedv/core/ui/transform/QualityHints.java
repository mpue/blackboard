/*
 *
 * QualityHints.java version 0.1 
 *
 * Copyright (C) 2008 Piet Blok. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created on 20 jan 2008
 */

package org.pmedv.core.ui.transform;

import java.awt.RenderingHints;
import java.util.HashMap;

/**
 * Rendering hints for quality rendering.
 * 
 * @author Piet Blok
 */
public class QualityHints extends HashMap<RenderingHints.Key,Object> {

    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance, populated with the appropriate key value pairs.
     */
    public QualityHints() {

	put(RenderingHints.KEY_ALPHA_INTERPOLATION,
		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

	put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	put(RenderingHints.KEY_COLOR_RENDERING,
		RenderingHints.VALUE_COLOR_RENDER_QUALITY);

	put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

	put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

	put(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

}
