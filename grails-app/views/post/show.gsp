<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="postMetaTags" model="[post:post]"/>
</head>


<content tag="mainContent">
<article class="kakareo post" role="article" itemscope itemtype="http://schema.org/Article">
    <div class="wrapper">
        <g:render template="/cluck/cluckMain" model="[post:post]"/>
    </div>
    <g:render template="/cluck/footerCluck" model="[cluck:post.firstCluck]"/>

    <div class="row options">
        <div class="col-xs-12 col-sm-6 col-md-6 editPost">
            <a href="#"><span class="fa fa-edit fa-lg"></span>Editar publicación</a>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
            <a target="_blank" href="#">Texto de la ley</a>
        </div>
    </div>

    ${raw(post.text)}
    %{--<p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>--}%
    %{--<p>Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei.</p>--}%
    %{--<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim.</p>--}%
    %{--<p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>--}%
    %{--<p>Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei.</p>--}%
    %{--<p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>--}%
    %{--<p>Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei.</p>--}%
    %{--<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.</p>--}%
    %{--<p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.</p>--}%

    <div class="wrapper">
        <g:render template="/cluck/cluckUsers" model="[post:post]"/>
    </div>
    <g:render template="/cluck/footerCluck" model="[post:post]"/>
</article><!-- /article -->

<aside class="othersPost">
    <h1>Otras publicaciones relacionadas:</h1>
    <div id="postNav" class="carousel slide">
        <a href="#postNav" data-slide="prev" class="prev">
            <span class="fa fa-caret-left fa-lg"></span>
            <span class="sr-only">Post anterior</span>
        </a>
        <div class="carousel-inner">
            <div class="item user link-wrapper">
                <a href="#" class="hidden">Ir al post</a>
                <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy. Cum sociis natoque penatibus et magnis dis partarient <a href="#">#modesntes</a></p>
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario 1</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <div class="read-later pull-right"><a class="enabled allow" href="#"><span class="fa fa-bookmark fa-lg"></span><span class="sr-only">Leer después</span></a></div>
            </div>
            <div class="item user link-wrapper">
                <a href="#" class="hidden">Ir al post</a>
                <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy. Cum sociis natoque penatibus et magnis dis partarient <a href="#">#modesntes</a></p>
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario 2</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <div class="read-later pull-right"><a class="enabled allow" href="#"><span class="fa fa-bookmark fa-lg"></span><span class="sr-only">Leer después</span></a></div>
            </div>
            <div class="item user link-wrapper">
                <a href="#" class="hidden">Ir al post</a>
                <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy. Cum sociis natoque penatibus et magnis dis partarient <a href="#">#modesntes</a></p>
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario 3</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <div class="read-later pull-right"><a class="enabled allow" href="#"><span class="fa fa-bookmark fa-lg"></span><span class="sr-only">Leer después</span></a></div>
            </div>
        </div>
        <a href="#postNav" data-slide="next" class="next">
            <span class="fa fa-caret-right fa-lg"></span>
            <span class="sr-only">Post siguiente</span>
        </a>
    </div>
</aside>

<aside class="comments">
<h1>Ya hay 6 comentarios</h1>
<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy.</p>
<ul class="listComments">
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus disabled">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
<li>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                </span>
                <!-- POPOVER PARA IMÁGENES USUARIOS -->
                <div class="popover">
                    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                    <div class="popover-user">
                        <div class="user" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                            <span class="user-type">
                                <small>Activista digital</small>
                            </span>
                        </div><!-- /user -->
                        <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                        <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                        <ul class="infoActivity clearfix">
                            <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                            <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                            <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                            <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                            <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                        </ul>
                    </div><!-- /popover-user -->
                </div>
                <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                <span class="user-type">
                    <small>Activista digital</small>
                </span>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
            </span>
        </div>
        <p><span class="say">Dice:</span> Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend.</p>
    </div>
    <div class="actions clearfix">
        <a href="#" class="pull-left">eliminar</a>
        <ul class="pull-right">
            <li>14 <a href="#" class="plus">+</a></li>
            <li>23 <a href="#" class="minus">-</a></li>
        </ul>
    </div>
</li>
</ul>

<div class="text-center" id="ver-mas"><a href="#">Ver más</a></div>

<form id="addComment">
    <div class="form-group">
        <label for="comment">Añade tu comentario:</label>
        <textarea id="comment" placeholder="Expresa tu opinión sobre la propuesta..." rows="5" class="form-control"></textarea>
    </div>
    <div class="form-group btns clearfix">
        <input type="submit" class="btn btn-grey btn-lg pull-right" value="Publicar comentario">
    </div>
</form>
</aside>

</content>

<content tag="cColumn">


</content>

<content tag="footerStats">

    %{--<modulesUtil:delayedModule mapping="ajaxModuleLawBottomStats" params="[hashtag:law.hashtag.decodeHashtag()]" elementId="idAjaxModuleLawBottomStats"/>--}%
    %{--<g:include controller="modules" action="bottomLawStats" params="[law:law]"/>--}%
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="law.up"/>
    </a>
</content>