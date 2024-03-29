%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${post.title}" />

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${post.title}">
<meta itemprop="description" content="${post.title}">
%{--<meta itemprop="image" content="${post.multimedia?.url}">--}%
<g:if test="${post.multimedia && post.multimedia.fileType == kuorum.core.FileType.IMAGE}">
    <meta itemprop="image" content="${post.multimedia?.url}" />
</g:if>

<!-- Twitter Card data -->
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${post.title}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${post.title}">
<g:if test="${post.owner.socialLinks?.twitter}">
    <meta name="twitter:creator" content="@${post.owner.socialLinks.twitter}">
</g:if>
<!-- Twitter summary card with large image must be at least 280x150px -->
%{--<meta name="twitter:image:src" content="${post.multimedia?.url}">--}%

<!-- Open Graph data -->
<meta property="og:title" content="${post.title}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'postShow', params:post.encodeAsLinkProperties(), absolute:true)}" />
<g:if test="${post.multimedia && post.multimedia.fileType == kuorum.core.FileType.IMAGE}">
    <meta property="og:image" content="${post.multimedia?.url}" />
</g:if>
<meta name="description" content="${post.title}" />
<meta name="title" content="${post.title}" />
<meta property="og:description" content="${post.title}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
<meta property="article:published_time" content="${formatDate(date:post.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:modified_time" content="${formatDate(date:post.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:section" content="Article Section" />
<meta property="article:tag" content="${post.law.hashtag}" />
<meta property="fb:admins" content="Facebook numberic ID" />