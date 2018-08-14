package com.iota.mdxdoclet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.sun.javadoc.*;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class MDXDoclet extends Doclet  {
	
	private static final String API_NAME = "API";
	private static String version;
	
	private ClassDoc apiDoc;
	private Parser generator;
	
	public MDXDoclet(ClassDoc c) {
		apiDoc = c;
		
		Configuration configuration = new Configuration(new Version(2, 3, 25));
	    // Where do we load the templates from:
	    configuration.setClassForTemplateLoading(Parser.class, "/templates");

	    // Some other recommended settings:
	    configuration.setDefaultEncoding("UTF-8");
	    configuration.setLocale(Locale.US);

	    configuration.setBooleanFormat("yes,no");
	    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		generator = new Parser(configuration);
		
	}

    private void generate() {
    	try {
	        for (MethodDoc m : apiDoc.methods(false)) {
	        	if (ApiCalls.isApiCall(m.name())) {
	        		System.out.println("Generating " + m.name());
		        	File classFile = new File(m.name() + ".mdx");
		    		try  (
	    				FileOutputStream fileOutputStream = new FileOutputStream(classFile);
		    	        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
		        		 ){
		    			
		    			generator.renderMethod(bufferedOutputStream, m);
	
		    		} catch (TemplateException | IOException e) {
		    			System.out.println("Processing method failed!!");
		    			e.printStackTrace();
	    	      	}
	        	}
	        }
    	} catch (Exception e) {
			System.out.println("Ohoh!");
			e.printStackTrace();
		}
	}
    
    /*
     * STATIC METHODS - USED BY DOCLET
     * 
     */

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
	
	public static boolean start(RootDoc root) {
		System.out.println("Making MDX docs for IRI V" + version);
        for (ClassDoc c : root.classes()) {
        	if (c.name().equals(API_NAME)) {
        		MDXDoclet doclet = new MDXDoclet(c);
        		doclet.generate();
        		break;
        	}
        }
        
        System.out.println("Docs generated!");
        return true;
    }
    
	private static void print(String name, String comment) throws IOException {
        if (comment != null && comment.length() > 0) {
            new FileWriter(name + ".txt").append(comment).close();
        }
    }
}