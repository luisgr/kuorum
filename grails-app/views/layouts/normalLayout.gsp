<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
    <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name:'page.extraCssContainer')}">
            <section id="main" role="main">
                <g:pageProperty name="page.mainContent"/>
            </section>
        </div>
    </div><!-- #main -->

    <g:if test="${pageProperty(name:'page.preFooter')}">
        <aside class="row preFooter" role="complementary">
            <g:pageProperty name="page.preFooter"/>
        </aside>
    </g:if>
    <g:if test="${Boolean.parseBoolean(pageProperty(name:'page.showDefaultPreFooter').toString())}">
        <g:include controller="modules" action="registerFooterRelevantUsers"/>
    </g:if>
    <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>