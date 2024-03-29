<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
    <div class="row main">
        <section id="main" role="main" class="home">
            <g:pageProperty name="page.mainContent"/>
        </section>
        <div class="container-fluid">

            <aside role="complementary" class="row homeSub">
                <g:pageProperty name="page.subHome"/>
            </aside>
        </div>
    </div><!-- #main -->

    <g:if test="${Boolean.parseBoolean(pageProperty(name:'page.showDefaultPreFooter').toString())}">
        <g:include controller="modules" action="registerFooterRelevantUsers"/>
    </g:if>
    <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>