package com.iota.mdxdoclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;

public final class Util {

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

    if( owner instanceof ExecutableMemberDoc) {
      for (ParamTag paramTag : ((ExecutableMemberDoc) owner).typeParamTags()) {
        if (paramTag.parameterName().equals(typeVar.toString())) {
            return paramTag;
          }
      }
    }

    if( owner instanceof ClassDoc) {
      for (ParamTag paramTag : ((ExecutableMemberDoc) owner).typeParamTags()) {
        if (paramTag.parameterName().equals(typeVar.toString())) {
          return paramTag;
        }
      }
    }

    return null;
  }
  }