<#function annotations_for executableMemberDoc delim="\n">
  <#assign  ret="" />
  <#if executableMemberDoc.annotations??>
    <#list executableMemberDoc.annotations() as annotationDesc>
      <#assign ret += "@" + link(annotationDesc.annotationType()) + "\n" />
    </#list>
  </#if>
  <#return ret>
</#function>


<#function annotations items>
  <#assign  ret="" />
  <#if items?has_content>
    <#list items as annotationDesc>
<#assign ret += "@" + link(annotationDesc.annotationType()) + "\n" />
    </#list>
  </#if>
<#return ret>
</#function>

<#function modifiers programElementDoc >
  <#if programElementDoc.isClass()>
    <#return programElementDoc.modifiers() + " class"/>
  <#else>
    <#return programElementDoc.modifiers() />
  </#if>
</#function>

<#function type doc>
  <#if doc.isInterface()>
    <#return "interface">
  <#elseif doc.isEnum()>
    <#return "enum"/>
  <#else>
    <#return "class"/>
  </#if>
</#function>

<#--
  static, abstract, final, public|private|protected
-->
<#function static doc>
  <#if doc.isStatic()>
    <#return "static">
  <#else>
    <#return "">
  </#if>
</#function>

<#function final programElementDoc>
  <#if programElementDoc.isFinal()>
    <#return "final">
  <#else>
    <#return "">
  </#if>
</#function>

<#function abstract classOrMethodDoc>
<#-- interfaces are also abstract but we do not show that -->
  <#if classOrMethodDoc.isAbstract() && !classOrMethodDoc.isInterface()  >
    <#return "abstract">
  <#else>
    <#return "">
  </#if>
</#function>


<#function synchronized methodDoc>
  <#if  methodDoc.isSynchronized?? && methodDoc.isSynchronized()>
    <#return "synchronized">
  <#else>
    <#return "">
  </#if>
</#function>

<#function modifier classOrMethodDoc>
  <#return classOrMethodDoc.modifiers() />
<#-- <#return "${scope(classOrMethodDoc)} ${static(classOrMethodDoc)} ${synchronized(classOrMethodDoc)} ${abstract(classOrMethodDoc)} ${final(classOrMethodDoc)}" > -->
</#function>

<#function scope programElementDoc>
  <#if programElementDoc.isPrivate()>
    <#return "private">
  <#elseif programElementDoc.isProtected()>
    <#return "protected"/>
  <#elseif programElementDoc.isPublic()>
    <#return "public"/>
  <#else>
    <#return ""/>
  </#if>
</#function>


<#--  Creating links:
TODO: configure root url
TODO: configure root package
-->
<#function url type>
  <#return "/javadoc/" + type.qualifiedTypeName()?lower_case ?replace('.','/') + "/"/>
</#function>

<#function link type>
  <#if type.isPrimitive()>
    <#return type.typeName()>
  <#else>
    <#if type.qualifiedTypeName()?starts_with("name.neuhalfen.") >
      <#return "[" + type.typeName() + "](" + url(type) + ")">
    <#else>
      <#return type.qualifiedTypeName()>
    </#if> </#if>
</#function>

<#function extends doc>
  <#if doc.superclassType()?? && doc.superclassType().name()!="Object" && doc.superclassType().name()!="Enum">
    <#return   "extends " + doc.superclassType().name()>
  <#else>
    <#return "">
  </#if>
</#function>


<#function interfaceHierarchy doc prefix="" verb="extends">
  <#if doc.interfaces()?has_content >
    <#return  _interfaceHierarchy(doc.interfaces(),  prefix,verb ) />
  <#else>
    <#return "" />
  </#if>
</#function>

<#function _interfaceHierarchy interfaces  prefix verb>
  <#assign v = "" />
  <#list interfaces as baseInterface>
    <#assign v +=  "\n" +  prefix + verb + link(baseInterface)  />
    <#if baseInterface.interfaces()?has_content >
      <#assign v +=  _interfaceHierarchy(baseInterface.interfaces(),  " " + prefix, verb) />
    </#if>
  </#list>
  <#return v />
</#function>


<#function classHierarchy doc prefix="">
  <#return  _classHierarchy(doc, prefix ) />
</#function>

<#function _classHierarchy doc  prefix>
  <#if !doc.superclassType()?? || doc.superclassType().name()=="Enum" ||  doc.superclassType().name()=="Object">
    <#return "" />
  </#if>

  <#assign v=prefix + link(doc) />
  <#if doc.superclassType()??>
    <#assign v +=  "\n" + _classHierarchy(doc.superclassType(),  "  " + prefix) />
  </#if>
  <#return v />
</#function>

<#--
 skipFirstClass: skip the first classdef but include the implemented interfaces
-->
<#function classHierarchyWithInterfaces classDoc  prefix skipFirstClass=false>
  <#if !classDoc?? || classDoc.name()=="Enum" ||  classDoc.name()=="Object">
    <#return "" />
  </#if>
  <#if skipFirstClass>
    <#assign v="" />
  <#else>
    <#assign v="\n" + prefix + " extends " + link(classDoc)  />
  </#if>
  <#if classDoc.interfaces?? && classDoc.interfaces()?has_content>
    <#assign v+=  _interfaceHierarchy(classDoc.interfaces(), "  " + prefix, " implements " ) />
  </#if>
  <#if classDoc.superclassType()??>
    <#assign v += classHierarchyWithInterfaces(classDoc.superclassType(),  "  " + prefix) />
  </#if>
  <#return v />
</#function>

<#-- Method parameters -->
<#macro annotations annotations>
  <@compress>
    <#list annotations as annotation>@link(annotation) </#list>
  </@compress>
</#macro>

<#macro parameter param isisvarag>
  <@compress>
    <@annotations param.annotations() />  ${link(param.type())}<#if isisvarag>...</#if> ${param.name()}
  </@compress>
</#macro>

<#macro parameterList params isisvarag=false>
  <@compress  single_line=true>
    <#if params??><#list params as param><@parameter param=param isisvarag=(isisvarag && !param?has_next)/><#sep>, </#sep></#list></#if>
  </@compress>
</#macro>

<#macro parameterTag tag>
  <@compress  single_line=true>
  ${tag.parameterName()} - ${tag.parameterComment()}
  </@compress>
</#macro>

<#macro parameterTags parameterTags>
  <#if parameterTags??>
    <#list parameterTags as tag>
      <@compress  single_line=true>
        * <@parameterTag tag />
    </@compress>
      <#sep>

      </#sep>
    </#list>
  </#if>
</#macro>

<#macro typeParameterWithTag util typeParameter >
<#-- typeParameter: TypeVariable
 -->
  <@compress  single_line=true>
    <#if util.findTag(typeParameter)??>
&lt;${typeParameter.toString()}&gt;  -- ${util.findTag(typeParameter).parameterComment()}
    <#else >
&lt;${typeParameter.toString()}&gt;
    </#if>
  </@compress>
</#macro>

<#macro typeParameterWithTags util typeParameters >
  <#if typeParameters??>
    <#list typeParameters as typeParameter>
      <@compress  single_line=true>
        * ${annotations_for(typeParameter, " ")}<@typeParameterWithTag util typeParameter  />
      </@compress>
    <#sep>

    </#sep>
    </#list>
  </#if>
</#macro>

<#macro exceptionList exceptions>
  <@compress  single_line=true>
    <#list exceptions>throws <#items as exception>${link(exception)}<#sep>, </#sep></#items></#list>
  </@compress>
</#macro>

<#macro ClassDoc_decl  util classDoc>
${modifier(classDoc)} ${type(classDoc)} ${classDoc.name()} ${extends(classDoc)}
</#macro>

<#macro tags tags>
  <#list tags as tag>
    <#switch tag.kind()>
      <#case "@see">${link(tag.referencedClass())}<#break>
      <#case "@code">`${tag.text()}`<#break>
      <#case "@throws">@throws ${link(tag.referencedClass())}<#break>
      <#case "Text">${tag.text()}<#break>
      <#default>${tag.toString()}
    </#switch>
  </#list>
</#macro>