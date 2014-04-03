<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="normalLayout">
</head>



<content tag="mainContent">
    Total Results: ${docs.numResults}<br/>
    <g:if test="${docs.suggest}">
        Quiso decir: <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery]" > ${docs.suggest.suggestedQuery} </g:link> => ${docs.suggest.hits}
    </g:if>
    <hr/>
    <ul>
    <g:each in="${docs.elements}" var="doc">
        <g:if test="${doc instanceof kuorum.core.model.solr.SolrKuorumUser}">
            <li>
                <g:link mapping="userShow" params="${doc.encodeAsLinkProperties()}">Usuario ${raw(doc.name)} </g:link>
            </li>
        </g:if>
        <g:elseif test="${doc instanceof kuorum.core.model.solr.SolrPost}">
            <li><g:link mapping="postShow" params="${doc.encodeAsLinkProperties()}">Post ${raw(doc.name)} </g:link> </li>
            <p><strong>Extracto</strong>: ${raw(doc.text)}</p>
        </g:elseif>
        <g:elseif test="${doc instanceof kuorum.core.model.solr.SolrLaw}">
            <li><g:link mapping="lawShow" params="${doc.encodeAsLinkProperties()}">${doc.hashtag}: ${raw(doc.name)} </g:link> </li>
            <p><strong>Extracto</strong>: ${raw(doc.text)}</p>
        </g:elseif>
        <g:else>
            %{--<sec:ifAllGranted roles="ROLE_ADMIN">--}%
                <li style="background: red">WAAARRNNN => ${doc} (${doc.name} - ${doc.id})</li>
            %{--</sec:ifAllGranted>--}%
        </g:else>
    </g:each>
    </ul>
</content>