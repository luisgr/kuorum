<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createLaw.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editLaw.title" args="[law.hashtag]"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.editLaw.title" args="[law.hashtag]"/></h1>
    <formUtil:validateForm bean="${command}" form="createLaw"/>
    <g:form method="POST" mapping="adminEditLaw" params="${law.encodeAsLinkProperties()}" name="createLaw" role="form">
        <g:render template="formLaw" model="[command:command, institutions:institutions]"/>
        <div class="form-group">
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
