<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step3.intro.head"/></h1>
    <p><g:message code="post.edit.step3.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <g:set var="multimedia" value=""/>
    <postUtil:ifHasMultimedia post="${post}">
        <g:set var="multimedia" value="multimedia"/>
    </postUtil:ifHasMultimedia>

    <article class="kakareo post sponsor ${multimedia}" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
        <div class="wrapper">
            <g:render template="/cluck/cluckMain" model="[post:post]"/>
        </div>
        <g:render template="/cluck/footerCluck" model="[cluck:post,displayingColumnC:false]"/>
    </article><!-- /article -->

    <h2>Para qué patrocinar</h2>
    <p class="lead">Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>
    <p>Para que llegue mas y más lejos, at vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.</p>
    <p>Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>

    %{--<h2>Invita a tus amigos para que hagan crecer la propuesta</h2>--}%
    %{--<ul class="socialSponsor clearfix">--}%
        %{--<li><a class="btn tw" href="#"><span class="fa fa-twitter fa-lg"></span> Publicar</a></li>--}%
        %{--<li><a class="btn fb" href="#"><span class="fa fa-facebook fa-lg"></span> Compartir</a></li>--}%
        %{--<li><a class="btn gog" href="#"><span class="fa fa-google-plus fa-lg"></span> Buscar contactos</a></li>--}%
    %{--</ul>--}%

    <ul class="btns">
        <g:if env="production">
            %{--DESACTIVADO PROCESO PAGO EN PRODUCCION--}%
        </g:if>
        <g:else>
            <li>
                <g:link mapping="postPayPost" class="btn btn-blue btn-lg" params="${post.encodeAsLinkProperties()}">
                    Patrocina mi propuesta
                </g:link>
            </li>
        </g:else>
        <li>
            <g:link mapping="postShow" class="btn btn-grey-light btn-lg" params="${post.encodeAsLinkProperties()}">
                Ver mi propuesta
            </g:link>
        </li>
    </ul>
    <g:if test="${gamificationData}">
        <script>
            $(function(){
                var gamification = {
                    title: "${gamificationData.title}",
                    text:"${gamificationData.text}",
                    eggs:${gamificationData.eggs},
                    plumes:${gamificationData.plumes},
                    corns:${gamificationData.corns}
                }
                karma.open(gamification)
            });
        </script>
    </g:if>
</content>

<content tag="cColumn">
    <section class="boxes noted likes">
        <h1><g:message code="post.edit.step3.firstVoteTitle"/> </h1>
        <g:render template="likesContainer" model="[post:post]"/>
        <g:render template="/post/postSocialShare" model="[post:post]"/>

    </section>
</content>
