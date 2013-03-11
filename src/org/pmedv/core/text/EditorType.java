package org.pmedv.core.text;

/**
 * Determines which type of {@link IStructuredTextEditor} we want to create by the
 * {@link EditorFactory}
 * 
 * @author Matthias Pueski
 * 
 */

public enum EditorType {

	TEXT_PLAIN {

		@Override
		public String toString() {
			return "Normal-Text";
		}
	},
	TEXT_HTML {

		@Override
		public String toString() {
			return "HTML-Text";
		}
	}

}
