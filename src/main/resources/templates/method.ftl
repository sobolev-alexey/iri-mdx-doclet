<#import "lib/java.ftl" as java>
---
title: ${subject.name()}
linktitle: ${subject.name()}
<#if subject.firstSentenceTags()?has_content>description: <@compress  single_line=true><@java.tags subject.firstSentenceTags() /></@compress></#if>


---
### ${subject.name()}

${java.annotations_for(subject)} ${java.link(subject.returnType())} ${subject.name()}(<@java.parameterList subject.parameters() />) <@java.exceptionList subject.thrownExceptionTypes() />

<@java.tags subject.inlineTags() />


<#if subject.typeParameters()?has_content>
<@java.typeParameterWithTags util subject.typeParameters()/>
</#if>

<#if subject.paramTags()?has_content>
<@java.parameterTags subject.paramTags() />
</#if>

  <#if subject.throwsTags()?has_content>
#### Exceptions

TODO: ThrowsTag[]throwsTags();
  </#if>