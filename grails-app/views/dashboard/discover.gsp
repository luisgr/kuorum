<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover"/></title>
    <meta name="layout" content="discoverLayout">
</head>


<content tag="mainContent">
    <div class="introDiscover">
        <p class="pre">Las leyes más polémicas en debate</p>
        <ul class="steps">
            <li class="active"><a href="#" class="badge">1</a></li>
            <li><a href="#" class="badge">2</a></li>
            <li><a href="#" class="badge">3</a></li>
            <li><a href="#" class="badge">4</a></li>
        </ul>
    </div>

    <g:each in="${relevantLaws}" var="law" status="i">
        <div class="row" style="display:${i!=0?'none':'block'}" id="relevantLaw_${i}">
            <section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">
                <g:render template="/law/lawInfo" model="[law:law, linkToLaw:true]"/>
            </section>

            <aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">
                <modulesUtil:lawVotes law="${law}"/>
                <modulesUtil:lawActivePeople law="${law}"/>
                <modulesUtil:recommendedPosts law="${law}" title="${message(code:"modules.recommendedLawPosts.title")}"/>
            </aside>
        </div>
    </g:each>
</content>

<content tag="footerModules">
    <aside class="moreActives" role="complementary">
        <h1><g:message code="discover.module.mostActiveUsers.title"/> </h1>
        <userUtil:showListUsers users="${mostActiveUsers}" visibleUsers="13" messagesPrefix="discover.module.mostActiveUsers.userList"/>
        <p><g:message code="discover.module.mostActiveUsers.footerText"/></p>
    </aside>
    <aside class="row others">
        <g:render template="discover/recommendedLaws" model="[recommendedLaws:recommendedLaws]"/>
        <modulesUtil:recommendedPosts numPost="2" title="${message(code:"modules.recommendedLawPosts.title")}" specialCssClass="col-xs-12 col-sm-4 col-md-4"/>


        <div class="col-xs-12 col-sm-4 col-md-4">
            <section>
                <h1>Políticos de la semana</h1>
                <ul class="user-list-followers">
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger more-users" rel="popover" role="button">
                            <span class="counter">+121</span>
                        </span>
                    </li>
                </ul>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt.</p>
            </section>
            <section>
                <h1>Organizaciones de la semana</h1>
                <ul class="user-list-followers">
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger" rel="popover" role="button">
                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </span>
                    </li>
                    <li itemscope itemtype="http://schema.org/Person">
                        <span data-toggle="popover" class="popover-trigger more-users" rel="popover" role="button">
                            <span class="counter">+121</span>
                        </span>
                    </li>
                </ul>
                <p>Lorem ipsum dolor sit amet, ut labore et dolore magna aliqua.</p>
            </section>
        </div>
    </aside>
</content>

