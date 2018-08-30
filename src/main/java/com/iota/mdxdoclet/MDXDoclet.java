package com.iota.mdxdoclet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import com.iota.mdxdoclet.example.CURL;
import com.iota.mdxdoclet.example.NodeJS;
import com.iota.mdxdoclet.example.Python;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class MDXDoclet extends Doclet  {
	
	private static final String API_NAME = "API";
	private static String version = "Unknown";
	
	private Parser parser;
	
	public MDXDoclet(RootDoc root) {
		
		Configuration configuration = new Configuration(new Version(2, 3, 26));
	    // Where do we load the templates from:
	    configuration.setClassForTemplateLoading(Parser.class, "/templates");

	    // Some other recommended settings:
	    configuration.setDefaultEncoding("UTF-8");
	    configuration.setLocale(Locale.US);

	    configuration.setBooleanFormat("yes,no");
	    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

	    parser = new Parser(configuration, new Util(root));
	    parser.addExport(new Python());
	    parser.addExport(new NodeJS());
	    parser.addExport(new CURL());
	}

    private void generate(ClassDoc apiDoc) {
    	try {
	        for (MethodDoc m : apiDoc.methods(false)) {
	        	ApiCall call = ApiCall.getApiCall(m.name());
	        	if (call != null) {
	        		System.out.println("Generating " + m.name());
		        	File classFile = new File(call.toString() + ".md");
		    		try  (
	    				FileOutputStream fileOutputStream = new FileOutputStream(classFile);
		    	        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
		        		 ){
		    			
		    			parser.renderMethod(bufferedOutputStream, m, call);
	
		    		} catch (TemplateException | IOException e) {
		    			System.err.println("Processing method " + m.name() + " failed");
		    			e.printStackTrace();
	    	      	} finally {
						
					}
	        	}
	        }
    	} catch (Exception e) {
    		System.err.println("Generating API documentation failed");
    		e.printStackTrace();
		}
	}
    
    /*
     * STATIC METHODS - USED BY DOCLET
     * 
     */
    
    public static boolean start(RootDoc root) {
		System.out.println("Generating MDX docs for IRI V" + version);
		MDXDoclet doclet = new MDXDoclet(root);
		
		for (ClassDoc c : root.classes()) {
        	if (c.name().equals(API_NAME)) {
        		doclet.generate(c);
        		break;
        	}
        }
        
        System.out.println("Documentation generated");
        return true;
    }

    /**
	 * Doclet class method that returns how many arguments would be consumed if
	 * <code>option</code> is a recognized option.
	 * 
	 * @param option the option to check
	 */
	public static int optionLength(String option) {
		if (option.equals("-version")) {
			return 2;
		}
		return Doclet.optionLength(option);
	}
	
	/**
	 * Doclet class method that checks the passed options and their arguments
	 * for validity.
	 * 
	 * @param args the arguments to check
	 * @param err the interface to use for reporting errors
	 */
	static public boolean validOptions(String[][] args, DocErrorReporter err) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i][0].equals("-version")) {
				version = args[i][1];
			}
		}
		
		return Doclet.validOptions(args, err);
	}
	
	/**
	 * Without this method present and returning LanguageVersion.JAVA_1_5,
     * Javadoc will not process generics because it assumes LanguageVersion.JAVA_1_1
	 */
	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
    }
}