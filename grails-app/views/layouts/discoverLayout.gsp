<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
    <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="container-fluid discover">
            <g:pageProperty name="page.mainContent"/>
            <g:pageProperty name="page.footerModules"/>
        </div>
    </div><!-- #main -->

    <g:if test="${Boolean.parseBoolean(pageProperty(name:'page.showDefaultPreFooter').toString())}">
        <g:include controller="modules" action="registerFooterRelevantUsers"/>
    </g:if>
    <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>