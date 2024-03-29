<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.organizations"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerOrganizations']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.organizations"/></h1>
        <h2><g:message code="footer.menu.footerOrganizations.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerOrganizations.description1"/>
            </p>
            <p>
                <g:message code="footer.menu.footerOrganizations.description2"/>
            </p>
            <p>
                <g:message code="footer.menu.footerOrganizations.description3"/>
            </p>
            <p>
                <g:link mapping="register">
                    <g:message code="footer.menu.footerOrganizations.description4"/>
                </g:link>
            </p>
            %{--<blockquote>--}%
            %{--<span class="fa fa-quote-right fa-2x"></span>--}%
            %{--<p><g:message code="footer.menu.footerCitizens.description3"/></p>--}%
            %{--</blockquote>--}%
        </div>
        <img src="${resource(dir: 'images', file: 'image-info-ngo.jpg')}" alt="foto-zodiak" itemprop="image">
    </article>
</content>
