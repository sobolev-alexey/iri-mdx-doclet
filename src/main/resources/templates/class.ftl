<#import "lib/java.ftl" as java>
---
title: ${subject.name()}
linktitle: ${subject.name()}
<#if subject.firstSentenceTags()?has_content>description: <@compress  single_line=true><@java.tags subject.firstSentenceTags() /></@compress></#if>
---
<#-- Doc -->
<@java.tags subject.inlineTags() />


<#-- Base class/interface -->
${java.annotations_for(subject)} **<@compress  single_line=true>${java.modifiers(subject)}  ${subject.typeName()}</@compress>**
<#if subject.isClass()>

${java.classHierarchyWithInterfaces(subject, " * ", true)}
<#elseif subject.isInterface() >
<#-- ${java.interfaceHierarchy(subject, " * extends ")} -->

${java.classHierarchyWithInterfaces(subject, " * ", true)}

</#if>


<#list subject.typeParameters()>
##  Type Parameters

|Name|Description|
|--------|---------------|
  <#items  as typeVariable>
| &lt;${typeVariable.name()}<#list typeVariable.bounds()> extends <#items  as bound>${java.link(bound)}<#sep> & </#sep></#items><#else></#list>&gt;| ${util.getTypeParamComment(typeVariable)} |
  </#items>
</#list>

<#-- Methods -->

<#list subject.methods()>
## Methods
|Method                | Description  |
|----------------------|--------------------------|
  <#items  as method>
| ${java.modifier(method)}  ${java.link(method.returnType())} ${method.name()}(<@java.parameterList method.parameters() />) <@java.exceptionList method.thrownExceptionTypes() /> |  <@compress  single_line=true><@java.tags method.firstSentenceTags() /></@compress> |
  </#items>
</#list>

<#list subject.methods() as method>
---
### ${method.name()}

${java.annotations_for(method)} ${java.link(method.returnType())} ${method.name()}(<@java.parameterList method.parameters() />) <@java.exceptionList method.thrownExceptionTypes() />

<@java.tags method.inlineTags() />


<#if method.typeParameters()?has_content>
<@java.typeParameterWithTags util method.typeParameters()/>
</#if>

<#if method.paramTags()?has_content>
<@java.parameterTags method.paramTags() />
</#if>

  <#if method.throwsTags()?has_content>
#### Exceptions

TODO: ThrowsTag[]throwsTags();
  </#if>
</#list>


<#list subject.constructors()>
## Constructor
<#items as constructor>
---
### ${constructor.name()}(<@java.parameterList constructor.parameters() />) <@java.exceptionList constructor.thrownExceptionTypes() />

<@java.tags constructor.inlineTags() />


<#if constructor.typeParameters()?has_content>
<@java.typeParameterWithTags util constructor.typeParameters()/>
</#if>
<#if constructor.paramTags()?has_content>
<@java.parameterTags constructor.paramTags() />
</#if>
</#items>

</#list>



<#if subject.fields()?has_content>
## Fields
  <#list subject.fields() as field>
*  ${field.name()}
  </#list>
</#if>



`package` `${subject.containingPackage().name()}`