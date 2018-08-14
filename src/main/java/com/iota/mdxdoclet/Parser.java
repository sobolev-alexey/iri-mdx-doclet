package com.iota.mdxdoclet;

import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * The main parser class. It scans the given Doclet document root and creates the MDX files.
 *
 * The work is done in Freemarker templates.
 *
 * Based on work from https://github.com/neuhalje/markdown-doclet
 * @author Jens Neuhalfen
 */
public class Parser {

  private final Configuration configuration;

  Parser(Configuration configuration) {
    this.configuration = configuration;
  }
  
  public void renderMethod(OutputStream out, MethodDoc methodDoc) throws IOException, TemplateException {
	  Writer w = new OutputStreamWriter(out);
	  render(w, methodDoc, "method.ftl");
  }

  public void renderClass(OutputStream out, ClassDoc classDoc) throws IOException, TemplateException {
	  Writer w = new OutputStreamWriter(out);

	  if (classDoc instanceof AnnotationTypeDoc) {
		  renderAnnotationTypeDoc(w, (AnnotationTypeDoc) classDoc);
	  } else if (classDoc.isEnum()) {
		  renderEnum(w, classDoc);
	  } else if (classDoc.isInterface()) {
		  renderInterface(w, classDoc);
	  } else {
		  renderClass(w, classDoc);
	  }
  }


  /**
   * Parse an annotation.
   *
   * @param annotationTypeDoc A AnnotationTypeDoc instance
   * @return the annotation node
   */
  private void renderAnnotationTypeDoc(Writer w, AnnotationTypeDoc annotationTypeDoc) throws IOException, TemplateException {
	  final String templateName = "class.ftl";
	  render(w, annotationTypeDoc, templateName);
  }


  private void renderEnum(Writer w, ClassDoc classDoc) throws IOException, TemplateException {
	  render(w, classDoc, "class.ftl");
  }


  private void renderInterface(Writer w, ClassDoc classDoc) throws IOException, TemplateException {
	  render(w, classDoc, "class.ftl");
  }

  private void renderClass(Writer w, ClassDoc classDoc) throws IOException, TemplateException {
	  render(w, classDoc, "class.ftl");
  }

  private void render(Writer w, ProgramElementDoc doc, String templateName) throws IOException, TemplateException {
	  Template template = configuration.getTemplate(templateName);
	  Map<String, Object> input = new HashMap<String, Object>();
	  input.put("subject", doc);
	  input.put("util", new Util());
	  template.process(input, w);
  }
}