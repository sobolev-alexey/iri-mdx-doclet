package com.iota.mdxdoclet;

import java.util.ArrayList;

import com.iota.mdxdoclet.data.ReturnParam;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;

public final class Util {

	private RootDoc rootDoc;

	public Util(RootDoc root) {
		this.rootDoc = root;
	}
	
	public boolean hasReturnClass(Tag[] tags) {
		return tags.length > 0 && tags[0].text().startsWith("{");
	}
	
	public ClassDoc getReturnClass(Tag[] tags) {
		if (tags.length > 0) {
			Tag tag = tags[0];
			if (tag.text().startsWith("{")) {
				ClassDoc d = findClassDoc(tag.text());
				return d;
			}
			return null;
		}
		return null;
	}

	public ReturnParam[] parseReturnTag(Tag[] tags, ClassDoc doc) {
		if (tags.length > 0) {
			Tag tag = tags[0];
			if (tag.text().startsWith("{")) {
				if (doc == null) {
					return new ReturnParam[] {};
				}

				ReturnParam[] finalTags =  parsePublicGetters(doc).toArray(new ReturnParam[] {});
				return finalTags;
			} else {
				//NO class reference, single return object?
				int ret = tag.text().indexOf(" ");
				return new ReturnParam[] { new ReturnParam(tag.text().substring(0,  ret), tag.text().substring(ret)) };
			}
		}

		return new ReturnParam[] {};
	}

	public ArrayList<ReturnParam> parsePublicGetters(ClassDoc doc) {
		return parsePublicGetters(doc, new ArrayList<ReturnParam>());
	}

	private ArrayList<ReturnParam> parsePublicGetters(ClassDoc doc, ArrayList<ReturnParam> tags) {
		if (doc.superclass() != null) {
			parsePublicGetters(doc.superclass(), tags);
		}

		for (MethodDoc m : doc.methods(false)) {
			if (m.isPublic() && m.inlineTags().length > 0 && m.name().startsWith("get")) {
				Tag t = m.inlineTags()[0];
				String name = m.name().substring(3);
				String output = name.substring(0, 1).toLowerCase() + name.substring(1); 
				ReturnParam rp = new ReturnParam(output, t.text());
				tags.add(rp);
			}
		}
		return tags;
	}

	// Format: {@link com.iota.package.ResponseClass }
	private ClassDoc findClassDoc(String text) {
		String docName = text.substring(7, text.length() - 1);

		for (ClassDoc c : rootDoc.classes()) {
			if (c.qualifiedName().equals(docName)) {
				return c;
			}
		}

		return null;
	}

	public String dimension(Type type) {
		try {
			StringBuilder ret = new StringBuilder(type.qualifiedTypeName());
			int dimension = Integer.parseInt(type.dimension());
			for (int dim = 0; dim < dimension; dim++) {
				ret.append("[]");
			}
			return ret.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public String processDescription(String text) {
		try {
			text = text.replaceAll("<code>", "`").replaceAll("</code>", "`");
			text = text.replaceAll("<b>", "**").replaceAll("</b>", "**");
			
			return text;
		} catch (Exception e) {
			return "";
		}
	}

	public String getTypeParamComment(TypeVariable variable) {
		final String name = variable.simpleTypeName();
		final ProgramElementDoc owner = variable.owner();

		if (owner.isClass()) {
			ClassDoc asClassDoc = (ClassDoc) owner;
			for (ParamTag tag : asClassDoc.typeParamTags()) {
				if (name.equals(tag.parameterName())) {
					return tag.parameterComment();
				}
			}
		}
		return "";
	}

	public String getParamComment(TypeVariable variable) {
		final String name = variable.simpleTypeName();
		final ProgramElementDoc owner = variable.owner();

		if (owner.isClass()) {
			ClassDoc asClassDoc = (ClassDoc) owner;
			for (ParamTag tag : asClassDoc.typeParamTags()) {
				if (name.equals(tag.parameterName())) {
					return tag.parameterComment();
				}
			}
		}
		return "";
	}

	public Tag findTag(TypeVariable typeVar) {
		final ProgramElementDoc owner = typeVar.owner();

		if (owner instanceof ExecutableMemberDoc) {
			for (ParamTag paramTag : ((ExecutableMemberDoc) owner).typeParamTags()) {
				if (paramTag.parameterName().equals(typeVar.toString())) {
					return paramTag;
				}
			}
		}

		if (owner instanceof ClassDoc) {
			for (ParamTag paramTag : ((ExecutableMemberDoc) owner).typeParamTags()) {
				if (paramTag.parameterName().equals(typeVar.toString())) {
					return paramTag;
				}
			}
		}

		return null;
	}
}