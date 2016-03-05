package br.com.spektro.minispring.core.implfinder;

public class ContextSpecifier {

	private static ContextSpecifier instance;
	private String ctx;
	private String ctxAsFile;

	private ContextSpecifier() {

	}

	private static ContextSpecifier getInstance() {
		if (instance == null) {
			instance = new ContextSpecifier();
		}
		return instance;
	}

	public static void setContext(String ctx) {
		if ((getInstance().ctx == null)
				|| (getInstance().ctx != null && getInstance().ctx.equals(""))) {
			getInstance().ctx = ctx;
			getInstance().ctxAsFile = ctx.replace(".", "/");
		}
	}

	public static String getContext() {
		return getInstance().ctx;
	}

	public static String getContextAsFile() {
		return getInstance().ctxAsFile;
	}

}
